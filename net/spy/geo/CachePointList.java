// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePointList.java,v 1.1 2001/06/12 23:57:48 dustin Exp $

package net.spy.geo;

import java.util.*;

/**
 * This object maintains the list of points.
 */
public class CachePointList extends Thread {

	private Vector cachePoints=null;

	/**
	 * Get an instance of CachePointList.
	 */
	public CachePointList() throws Exception {
		super("CachePointList");
		initPoints();
		start();
	}

	/**
	 * Print me.
	 */
	public String toString() {
		return(super.toString() + " - Herding " + numPoints() + " points.");
	}

	// Initially get the points.
	private void initPoints() throws Exception {
		cachePoints=new Vector();
		updatePoints();
	}

	// Get new points periodically.
	private void updatePoints() {
		synchronized(cachePoints) {
			cachePoints.removeAllElements();
			cachePoints.addElement(
				new CachePoint("GC88C", new Point(37, 15.658, -121, 57.330)));
			cachePoints.addElement(
				new CachePoint("GC510", new Point(37, 22.299, -122, 05.059)));
			cachePoints.addElement(
				new CachePoint("BinDir", new Point(42.69, -87.91)));
		}
	}

	/**
	 * Get an unsorted list of cache points.
	 */
	public Enumeration getPoints() {
		Enumeration e=null;
		synchronized(cachePoints) {
			e=cachePoints.elements();
		}
		return(e);
	}

	/**
	 * Get the list of cache points sorted by distance from a given point.
	 */
	public Enumeration getPoints(Point p) {
		Vector v=new Vector();
		synchronized(cachePoints) {
			for(Enumeration e=cachePoints.elements(); e.hasMoreElements();) {
				v.addElement(e.nextElement());
			}
		}
		PointComparator pcompare=new PointComparator(p);
		Collections.sort(v, pcompare);
		return(v.elements());
	}

	/**
	 * How many elements are in the list right now?
	 */
	public int numPoints() {
		int rv=0;

		synchronized(cachePoints) {
			rv=cachePoints.size();
		}

		return(rv);
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		CachePointList cpl=new CachePointList();
		System.out.println(cpl);
		Point home=new Point(37, 22.110, -121, 59.164);

		System.out.println("Unordered:\n");
		for(Enumeration e=cpl.getPoints(); e.hasMoreElements(); ) {
			System.out.println("\t" + e.nextElement());
		}

		System.out.println("Ordered:\n");
		for(Enumeration e=cpl.getPoints(home); e.hasMoreElements(); ) {
			System.out.println("\t" + e.nextElement());
		}

	}

}

