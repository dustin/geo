// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Point.java,v 1.7 2001/06/14 21:21:19 dustin Exp $

package net.spy.geo;

import java.math.*;
import java.sql.*;
import java.util.*;
import java.text.NumberFormat;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * Represents a point on earth.
 */
public class Point extends Object implements java.io.Serializable {

	private double longitude=0;
	private double latitude=0;

	/**
	 * Get an instance of Point at the given longitude and latitude.
	 */
	public Point(double latitude, double longitude) {
		super();
		this.longitude=longitude;
		this.latitude=latitude;
	}

	/**
	 * Get an instance of Point at the given longitude and latitude.
	 */
	public Point(double latitude, double lat_minutes,
			double longitude, double long_minutes) {
		super();
		this.longitude=fromhms(longitude, long_minutes);
		this.latitude=fromhms(latitude, lat_minutes);
	}

	/**
	 * An internal constructor for building the objects more specifically.
	 */
	protected Point() {
		super();
	}

	/**
	 * Get a Point for the given zipcode.
	 */
	public static Point getPointByZip(int zipcode) throws Exception {
		DBSP dbsp=new GetPointByZip(new GeoConfig());
		dbsp.set("zipcode", zipcode);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("Zipcode not found:  " + zipcode);
		}
		double lon=rs.getDouble("longitude");
		double lat=rs.getDouble("latitude");
		// XXX:  Backwards compatibility since those were backwards
		if(lat<0) {
			System.err.println("Using backwards compatibility!!!");
			double t=lon;
			lon=lat;
			lat=t;
		}
		rs.close();
		dbsp.close();
		return(new Point(lat, lon));
	}

	private double fromhms(double whole, double minutes) {
		double rv=whole;
		if(rv<0) {
			rv-=(minutes/60.0);
		} else {
			rv+=(minutes/60.0);
		}
		return(rv);
	}

	private double decimalPart(double a) {
		double tmp=Math.abs(a);
		tmp-=Math.floor(tmp);
		return(tmp);
	}

	/**
	 * Print this point (hms).
	 */
	public String toString() {

		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(4);

		StringBuffer sb=new StringBuffer();
		sb.append(Math.abs(getLatDegrees()));
		sb.append(" ");
		sb.append(nf.format(getLatMinutes()));
		sb.append("'");
		sb.append(getLatHemisphere());
		sb.append(" ");
		sb.append(Math.abs(getLongDegrees()));
		sb.append(" ");
		sb.append(nf.format(getLongMinutes()));
		sb.append("'");
		sb.append(getLongHemisphere());
		return(sb.toString());
	}

	/**
	 * Get the whole degrees of the longitude.
	 */
	public int getLongDegrees() {
		return((int)longitude);
	}

	/**
	 * Get the minutes of the longitude.
	 */
	public float getLongMinutes() {
		return((float)(decimalPart(longitude)*60f));
	}

	/**
	 * Get the longitude hemisphere (W or E).
	 */
	public String getLongHemisphere() {
		return(longitude<0 ? "W" : "E");
	}

	/**
	 * Get the whole degrees of the latitude.
	 */
	public int getLatDegrees() {
		return((int)latitude);
	}

	/**
	 * Get the minutes of the latitude.
	 */
	public float getLatMinutes() {
		return((float)(decimalPart(latitude)*60f));
	}

	/**
	 * Get the latitude hemisphere (S or N).
	 */
	public String getLatHemisphere() {
		return(latitude<0 ? "S" : "N");
	}

	/**
	 * Get the string in decimal format.
	 */
	public String toDecimalString() {
		StringBuffer sb=new StringBuffer();
		sb.append(Math.abs(latitude));
		sb.append(getLatHemisphere());
		sb.append(" ");
		sb.append(Math.abs(longitude));
		sb.append(getLongHemisphere());
		return(sb.toString());
	}

	/**
	 * Get the longitude.
	 */
	public double getLongitude() {
		return(longitude);
	}

	/**
	 * Get the latitude.
	 */
	public double getLatitude() {
		return(latitude);
	}

	/**
	 * Set the longitude.
	 */
	protected void setLongitude(double longitude) {
		this.longitude=longitude;
	}

	/**
	 * Set the latitude.
	 */
	protected void setLatitude(double latitude) {
		this.latitude=latitude;
	}

	// Degree sin.
	private double sin(double a) {
		double rv=Math.sin(Math.toRadians(a));
		return(Math.sin(Math.toRadians(a)));
	}

	// Degree cos.
	private double cos(double a) {
		double rv=Math.cos(Math.toRadians(a));
		return(rv);
	}

	/**
	 * Get the difference between this point and that point.
	 */
	public GeoVector diff(Point p) {
		double long_a=getLongitude();
		double lat_a=getLatitude();
		double long_b=p.getLongitude();
		double lat_b=p.getLatitude();

		double cosaob=(cos(lat_a)*cos(lat_b)
			*cos(long_b-long_a)) + (sin(lat_a)*sin(lat_b));

		double aob=Math.acos(cosaob);
		
		// OK, got the distance.
		double distance=aob*3959;

		// Get the bearing
		double a=long_a-long_b;
		double b=lat_a-lat_b;

		// Find the bearing angle
		double bearing=Math.toDegrees(Math.atan(a/b));

		// Look, I'm a math hacker!  (get the raw angle)
		while(bearing<0) { bearing+=90; }
		while(bearing>90) { bearing-=90; }

		// HACK!  Now calculate the quadrant
		if(long_a>long_b) {
			bearing+=180;
			if(lat_a<lat_b) {
				bearing+=90;
			}
		} else {
			if(lat_a>lat_b) {
				bearing+=90;
			}
		}

		return(new GeoVector(distance, bearing));
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		// Home
		Vector v=new Vector();

		Point home=new Point(37, 22.110, -121, 59.164);

		System.out.println("Home point is " + home);

		for(int i=0; i<args.length; i++) {
			Point p=Point.getPointByZip(Integer.parseInt(args[i]));
			System.out.println("Arg point is " + p);
			System.out.println("Difference:  " + home.diff(p));
			v.addElement(p);
		}
		System.out.println("Sorting...");
		PointComparator pcompare=new PointComparator(home);
		Collections.sort(v, pcompare);
		for(Enumeration e=v.elements(); e.hasMoreElements(); ) {
			Point p=(Point)e.nextElement();
			System.out.println(p + " -- " + home.diff(p));
		}
	}
}
