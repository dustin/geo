// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Point.java,v 1.1 2001/06/11 10:23:13 dustin Exp $

package net.spy.geo;

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
	 * Print this point.
	 */
	public String toString() {
		StringBuffer sb=new StringBuffer();

		sb.append(Math.abs(longitude));
		if(longitude<0) {
			sb.append("S");
		} else {
			sb.append("N");
		}

		sb.append(" ");
		sb.append(Math.abs(latitude));
		if(latitude<0) {
			sb.append("W");
		} else {
			sb.append("E");
		}

		return(sb.toString());
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		Point p=new Point(37.17523, -122.03584);
		System.out.println("Your point:  " + p);
	}

}
