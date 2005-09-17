// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: DBPolygon.java,v 1.2 2001/06/16 11:11:11 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;

import net.spy.db.DBSP;
import net.spy.geo.sp.GetPolygonByID;
import net.spy.geo.sp.GetPolygonDataByID;

/**
 * A polygon that came from the DB (has a bit more info).
 */
public class DBPolygon extends Polygon {

	private String source=null;
	// One corner of the box that contains all these points.
	private Point boundary1=null;
	// Another corner of the box that contains all these points.
	private Point boundary2=null;

	/**
	 * Get an instance of DBPolygon.
	 */
	public DBPolygon(int id) throws Exception {
		super();
		DBSP dbsp=new GetPolygonByID(GeoConfig.getInstance());
		dbsp.set("id", id);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such polygon.");
		}
		setName(rs.getString("name"));
		source=rs.getString("source");
		boundary1=new Point(
			rs.getFloat("boundaryy1"),
			rs.getFloat("boundaryx1"));
		boundary2=new Point(
			rs.getFloat("boundaryy2"),
			rs.getFloat("boundaryx2"));
		rs.close();
		dbsp.close();

		dbsp=new GetPolygonDataByID(GeoConfig.getInstance());
		dbsp.set("id", id);
		rs=dbsp.executeQuery();

		while(rs.next()) {
			add(new Point(
				rs.getFloat("latitude"), rs.getFloat("longitude") ));
		}
	}

	/**
	 * Get the source of this polygon.
	 */
	public String getSource() {
		return(source);
	}

	/**
	 * Get one boundary corner.
	 */
	public Point getBoundary1() {
		return(boundary1);
	}

	/**
	 * Get the other boundary corner.
	 */
	public Point getBoundary2() {
		return(boundary2);
	}

	/**
	 * Get the center point of this thing.
	 */
	public Point getCenter() {
		double max=Math.max(boundary1.getLatitude(), boundary2.getLatitude());
		double min=Math.min(boundary1.getLatitude(), boundary2.getLatitude());
		double diff=(max-min);
		double newlat=max-(diff/2);
		max=Math.max(boundary1.getLongitude(), boundary2.getLongitude());
		min=Math.min(boundary1.getLongitude(), boundary2.getLongitude());
		diff=(max-min);
		double newlon=max-(diff/2);
		return(new Point(newlat, newlon));
	}

	/**
	 * Get the height of this thing.
	 */
	public double getHeight() {
		double max=Math.max(boundary1.getLatitude(), boundary2.getLatitude());
		double min=Math.min(boundary1.getLatitude(), boundary2.getLatitude());
		double diff=(max-min);
		return(diff);
	}

	/**
	 * Get the width of this thing.
	 */
	public double getWidth() {
		double max=Math.max(boundary1.getLongitude(), boundary2.getLongitude());
		double min=Math.min(boundary1.getLongitude(), boundary2.getLongitude());
		double diff=(max-min);
		return(diff);
	}

}
