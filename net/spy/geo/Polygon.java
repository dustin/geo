// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Polygon.java,v 1.1 2001/06/16 09:12:35 dustin Exp $

package net.spy.geo;

import java.util.*;
import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * A closed set of points.
 */
public class Polygon extends Vector {

	private String name=null;

	/**
	 * Get an instance of Polygon.
	 */
	public Polygon(String name) {
		super();
		this.name=name;
	}

	/**
	 * Get a polygon by integer ID.
	 */
	public static Polygon getPolygonByID(int id) throws Exception {
		DBSP dbsp=new GetPolygonByID(new GeoConfig());
		dbsp.set("id", id);
		ResultSet rs=dbsp.executeQuery();
		rs.next();
		String name=rs.getString("name");
		rs.close();
		dbsp.close();
		dbsp=new GetPolygonDataByID(new GeoConfig());
		dbsp.set("id", id);
		rs=dbsp.executeQuery();

		Polygon poly=new Polygon(name);
		while(rs.next()) {
			poly.addElement(new Point(
				rs.getFloat("latitude"), rs.getFloat("longitude") ));
		}
		return(poly);
	}

	/**
	 * Ask for a Polygon containing the given Point.
	 *
	 * @return null if there's no match.
	 */
	public static Polygon getAreaForPoint(Point p) throws Exception {
		Polygon rv=null;
		DBSP dbsp=new GetPossibleAreas(new GeoConfig());
		dbsp.set("latitude", (float)p.getLatitude());
		dbsp.set("longitude", (float)p.getLongitude());
		ResultSet rs=dbsp.executeQuery();
		Vector v=new Vector();
		while(rs.next()) {
			v.addElement(new Integer(rs.getInt("id")));
		}
		rs.close();
		dbsp.close();

		for(Enumeration e=v.elements(); rv==null && e.hasMoreElements(); ) {
			Integer io=(Integer)e.nextElement();
			int i=io.intValue();

			Polygon poly=getPolygonByID(i);
			if(poly.containsPoint(p)) {
				rv=poly;
			}
		}

		return(rv);
	}

	/**
	 * A string of this polygon.
	 */
	public String toString() {
		// return(name + " has " + size() + " points:  " + super.toString());
		return(name + " has " + size() + " points.");
	}

	/**
	 * Does this polygon contain the given point?
	 */
	public boolean containsPoint(Point p) {
		int counter=0, i=0;
		Point p1=null, p2=null;
		int n=size();

		p1=(Point)elementAt(i);
		for(i=1; i<=n; i++) {
			p2=(Point)elementAt(i%n); // Mod so size()+1 == 0
			// Convenience variables.
			double y1=p1.getLatitude();
			double x1=p1.getLongitude();
			double y2=p2.getLatitude();
			double x2=p2.getLongitude();
			double y=p.getLatitude();
			double x=p.getLongitude();
			if(y > Math.min(y1, y2)) {
				if(y <= Math.max(y1, y2)) {
					if(x <= Math.max(x1, x2)) {
						if(y1 != y2) {
							double xinters = (y-y1)*(x2-x1)/(y2-y1)+x1;
							if(x1 == x2 || x <= xinters) {
								counter++;
							}
						}
					}
				}
			}
			p1=p2;
		}

		// If we crossed an even number of sides, we're outside.
		return( (counter%2)!=0 );
	}

	/**
	 * Get the name of this polygon.
	 */
	public String getName() {
		return(name);
	}

	public static void main(String args[]) throws Exception {
		Point p=new Point(Double.parseDouble(args[0]),
			Double.parseDouble(args[1]));

		Polygon poly=getAreaForPoint(p);
		if(poly==null) {
			System.out.println("No matching area.");
		} else {
			System.out.println("Point is in " + poly.getName());
		}
	}

}
