// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Point.java,v 1.9 2001/06/20 11:00:03 dustin Exp $

package net.spy.geo;

import java.text.NumberFormat;


/**
 * Represents a point on earth.
 */
public class Point extends Object implements java.io.Serializable {

	private double longitude=0;
	private double latitude=0;

	/**
	 * Get an instance of Point at the given longitude and latitude.
	 */
	public Point(double lat, double lon) {
		super();
		this.longitude=lon;
		this.latitude=lat;
	}

	/**
	 * Get an instance of Point at the given longitude and latitude.
	 */
	public Point(double lat, double lat_minutes,
			double lon, double long_minutes) {
		super();
		this.longitude=fromhms(lon, long_minutes);
		this.latitude=fromhms(lat, lat_minutes);
	}

	/**
	 * An internal constructor for building the objects more specifically.
	 */
	protected Point() {
		super();
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

		StringBuilder sb=new StringBuilder();
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
		StringBuilder sb=new StringBuilder();
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
	protected void setLongitude(double to) {
		this.longitude=to;
	}

	/**
	 * Set the latitude.
	 */
	protected void setLatitude(double to) {
		this.latitude=to;
	}

	// Degree sin.
	private double sin(double a) {
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

}
