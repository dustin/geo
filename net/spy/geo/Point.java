// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Point.java,v 1.2 2001/06/12 07:52:52 dustin Exp $

package net.spy.geo;

import java.math.*;
import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * Represents a point on earth.
 */
public class Point extends Object {

	private double longitude=0;
	private double latitude=0;

	/**
	 * Get an instance of Point at the given longitude and latitude.
	 */
	public Point(double longitude, double latitude) {
		super();
		this.longitude=longitude;
		this.latitude=latitude;
	}

	/**
	 * Get an instance of Point at the given longitude and latitude.
	 */
	public Point(double longitude, double long_minutes,
			double latitude, double lat_minutes) {
		super();
		this.longitude=fromhms(longitude, long_minutes);
		this.latitude=fromhms(latitude, lat_minutes);
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
		rs.close();
		dbsp.close();
		return(new Point(lon, lat));
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
		StringBuffer sb=new StringBuffer();
		sb.append(Math.abs(longitude));
		sb.append(" ");
		sb.append(decimalPart(longitude)*60);
		sb.append(longitude<0 ? "S" : "N");
		sb.append(" ");
		sb.append(Math.abs(latitude));
		sb.append(" ");
		sb.append(decimalPart(latitude)*60);
		sb.append(longitude<0 ? "W" : "E");
		return(sb.toString());
	}

	/**
	 * Get the string in decimal format.
	 */
	public String toDecimalString() {
		StringBuffer sb=new StringBuffer();
		sb.append(Math.abs(longitude));
		sb.append(longitude<0 ? "S" : "N");
		sb.append(" ");
		sb.append(Math.abs(latitude));
		sb.append(latitude<0 ? "W" : "E");
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

		double cosaob=(cos(long_a)*cos(long_b)
			*cos(lat_b-lat_a)) + (sin(long_a)*sin(long_b));

		double aob=Math.acos(cosaob);
		
		// OK, got the distance.
		double distance=aob*3959;

		// Get the bearing
		double a=lat_a-lat_b;
		double b=long_a-long_b;

		// Find the bearing angle
		double bearing=Math.toDegrees(Math.atan(a/b));

		// Look, I'm a math hacker!  (get the raw angle)
		while(bearing<0) { bearing+=90; }
		while(bearing>90) { bearing-=90; }

		// HACK!  Now calculate the quadrant
		if(lat_a>lat_b) {
			bearing+=180;
			if(long_a<long_b) {
				bearing+=90;
			}
		} else {
			if(long_a>long_b) {
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
		Point pa=new Point(37, 22.110, -121, 59.164);
		// GC88C (south)
		// Point pb=new Point(37, 15.658, -121, 57.330);
		// GC510 (west)
		// Point pc=new Point(37, 22.299, -122, 05.059);
		// test, Bin Dir
		// Point pd=new Point(42.69, -87.91);

		System.out.println("Home point is " + pa);

		if(args.length>0) {
			Point pb=Point.getPointByZip(Integer.parseInt(args[0]));
			System.out.println("Other point is " + pb);
			System.out.println("Difference:  " + pa.diff(pb));
		}
	}

}
