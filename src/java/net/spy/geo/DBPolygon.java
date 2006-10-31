// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: DBPolygon.java,v 1.2 2001/06/16 11:11:11 dustin Exp $

package net.spy.geo;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.sql.ResultSet;

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
	public DBPolygon(ResultSet rs) throws Exception {
		super();
		setName(rs.getString("name"));
		source=rs.getString("source");
		boundary1=new Point(
				rs.getFloat("boundaryy1"),
				rs.getFloat("boundaryx1"));
		boundary2=new Point(
				rs.getFloat("boundaryy2"),
				rs.getFloat("boundaryx2"));

		if(source.startsWith("zt")) {
			setType(Type.zipcode);
		} else if(source.startsWith("co")) {
			setType(Type.county);
		} else if(source.startsWith("st")) {
			setType(Type.state);
		}
	}

	/**
	 * Get the source of this polygon.
	 */
	public String getSource() {
		return(source);
	}

	public double getWesternBorder() {
		return(min(boundary1.getLongitude(), boundary2.getLongitude()));
	}

	public double getEasternBorder() {
		return(max(boundary1.getLongitude(), boundary2.getLongitude()));
	}

	public double getNorthernBorder() {
		return(max(boundary1.getLatitude(), boundary2.getLatitude()));
	}

	public double getSouthernBorder() {
		return(min(boundary1.getLatitude(), boundary2.getLatitude()));
	}

	/**
	 * Get the center point of this thing.
	 */
	public Point getCenter() {
		double max=max(boundary1.getLatitude(), boundary2.getLatitude());
		double min=min(boundary1.getLatitude(), boundary2.getLatitude());
		double diff=(max-min);
		double newlat=max-(diff/2);
		max=max(boundary1.getLongitude(), boundary2.getLongitude());
		min=min(boundary1.getLongitude(), boundary2.getLongitude());
		diff=(max-min);
		double newlon=max-(diff/2);
		return(new Point(newlat, newlon));
	}

	/**
	 * Get the height of this thing.
	 */
	public double getHeight() {
		double max=max(boundary1.getLatitude(), boundary2.getLatitude());
		double min=min(boundary1.getLatitude(), boundary2.getLatitude());
		double diff=(max-min);
		return(diff);
	}

	/**
	 * Get the width of this thing.
	 */
	public double getWidth() {
		double max=max(boundary1.getLongitude(), boundary2.getLongitude());
		double min=min(boundary1.getLongitude(), boundary2.getLongitude());
		double diff=(max-min);
		return(diff);
	}

}
