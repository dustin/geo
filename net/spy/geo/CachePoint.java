// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePoint.java,v 1.1 2001/06/12 23:57:48 dustin Exp $

package net.spy.geo;

import java.util.*;

/**
 * A Geocache point.
 */
public class CachePoint extends Point {

	private String description=null;

	/**
	 * Get an instance of CachePoint.
	 */
	public CachePoint(String description, Point p) {
		super();
		this.description=description;
		setLongitude(p.getLongitude());
		setLatitude(p.getLatitude());
	}

	/**
	 * Get the description of this CachePoint.
	 */
	public String getDescription() {
		return(description);
	}

	/**
	 * Print me.
	 */
	public String toString() {
		return(description + " - " + super.toString());
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		Vector v=new Vector();

		Point home=new Point(37, 22.110, -121, 59.164);

		v.addElement(
			new CachePoint("GC88C", new Point(37, 15.658, -121, 57.330)));
		v.addElement(
			new CachePoint("GC510", new Point(37, 22.299, -122, 05.059)));
		v.addElement(
			new CachePoint("BinDir", new Point(42.69, -87.91)));

		PointComparator pcompare=new PointComparator(home);
		Collections.sort(v, pcompare);
		for(Enumeration e=v.elements(); e.hasMoreElements(); ) {
			Point p=(Point)e.nextElement();
			System.out.println(p + " -- " + home.diff(p));
		}
	}

}
