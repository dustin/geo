// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: DBPolygon.java,v 1.1 2001/06/16 09:53:05 dustin Exp $

package net.spy.geo;

import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

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
		DBSP dbsp=new GetPolygonByID(new GeoConfig());
		dbsp.set("id", id);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such polygon.");
		}
		setName(rs.getString("name"));
		source=rs.getString("source");
		boundary1=new Point(
			(double)rs.getFloat("boundaryy1"),
			(double)rs.getFloat("boundaryx1"));
		boundary2=new Point(
			(double)rs.getFloat("boundaryy2"),
			(double)rs.getFloat("boundaryx2"));
		rs.close();
		dbsp.close();

		dbsp=new GetPolygonDataByID(new GeoConfig());
		dbsp.set("id", id);
		rs=dbsp.executeQuery();

		while(rs.next()) {
			addElement(new Point(
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
		double newlat=max-diff;
		max=Math.max(boundary1.getLongitude(), boundary2.getLongitude());
		min=Math.min(boundary1.getLongitude(), boundary2.getLongitude());
		diff=(max-min);
		double newlon=max-diff;
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
