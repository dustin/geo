// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: LoadBoundries.java,v 1.2 2001/06/20 11:00:01 dustin Exp $

package net.spy.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.spy.db.SpyDB;

/**
 * Load the boundries from http://www.census.gov/geo/www/cob/
 */
public class LoadBoundaries extends Object {

	private List<String> attributes=null;
	private BufferedReader polySource=null;

	/**
	 * Get an instance of LoadBoundries.
	 */
	public LoadBoundaries(File attributesFile, File polyFile)
		throws Exception {
		super();
		loadAttributes(attributesFile);
		polySource=new BufferedReader(new FileReader(polyFile));
	}

	private void loadAttributes(File af) throws Exception {
		attributes=new ArrayList<String>();
		BufferedReader r=new BufferedReader(new FileReader(af));

		String buf=r.readLine();
		while(buf!=null) {
			// buf is ID
			r.readLine();
			String name=r.readLine().trim();
			r.readLine();
			r.readLine();
			r.readLine();

			name=name.substring(1, name.length()-1);

			buf=r.readLine();
			attributes.add(name);
		}
	}

	/**
	 * Get the next Polygon in the polygon file.
	 *
	 * @return null if there are no more polygons.
	 */
	public Polygon nextPolygon() throws Exception {
		// Return null if it's closed.
		if(polySource==null) {
			return(null);
		}

		// If we have no data, close the file and return null
		String line=polySource.readLine();
		if(line==null || line.startsWith("END")) {
			close();
			return(null);
		}

		String part_s=line.substring(0, 10);
		int part_id=Integer.parseInt(part_s.trim());

		String name=null;
		if(part_id<0) {
			name="HOLE";
		} else {
			name=attributes.get(part_id);
		}

		Polygon poly=new Polygon(name);
		Point p=parsePoint(line.substring(10));
		if(p!=null) {
			poly.add(p);
		}
		line=polySource.readLine();

		while(line!=null && !line.startsWith("END")) {
			poly.add(parsePoint(line));
			line=polySource.readLine();
		}
		return(poly);
	}

	private Point parsePoint(String input) {
		// If we don't have enough, return null
		if(input.length() < 30) {
			return(null);
		}
		double lon=Double.parseDouble(input.substring(0, 30));
		double lat=Double.parseDouble(input.substring(30));

		Point p=new Point(lat, lon);
		return(p);
	}

	/**
	 * Close the thing.
	 */
	public void close() throws Exception {
		if(polySource!=null) {
			polySource.close();
			polySource=null;
		}
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		File fat=new File(args[0]);
		File f=new File(args[1]);
		LoadBoundaries lb=new LoadBoundaries(fat, f);

		SpyDB db=new SpyDB(GeoConfig.getInstance());
		Connection conn=db.getConn();
		conn.setAutoCommit(false);

		Polygon p=lb.nextPolygon();
		while(p!=null) {
			System.out.println("Got polygon:  " + p);

			PreparedStatement pst=conn.prepareStatement(
				"insert into geo_polys(source, name) values(?,?)");
			pst.setString(1, f.getName());
			pst.setString(2, p.getName());
			pst.executeUpdate();
			pst.close();

			Statement st=conn.createStatement();
			ResultSet rs=st.executeQuery("select currval('geo_polys_id_seq')");
			rs.next();
			int poly_id=rs.getInt(1);
			rs.close();
			st.close();

			pst=conn.prepareStatement(
				"insert into geo_poly_data(poly_id, latitude, longitude)\n"
				+ " values(?,?,?)");

			for(Point point : p) {
				pst.setInt(1, poly_id);
				pst.setFloat(2, (float)point.getLatitude());
				pst.setFloat(3, (float)point.getLongitude());
				pst.executeUpdate();
			}

			conn.commit();

			p=lb.nextPolygon();
		}
		conn.setAutoCommit(true);
		db.close();
	}

}
