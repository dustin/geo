// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePointList.java,v 1.2 2001/06/13 01:01:25 dustin Exp $

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
	 * Get the list of cache points within a certain range sorted by distance
	 * from a given point.
	 *
	 * @param p the point of origin we want to compare distances from
	 * @param max_distance the maximum distance in miles we care about
	 *
	 * @return an Enumeration of CachePoint objects
	 */
	public Enumeration getPoints(Point p, double max_distance) {
		Vector v=new Vector();
		synchronized(cachePoints) {
			for(Enumeration e=cachePoints.elements(); e.hasMoreElements();) {
				if(max_distance>0) {
					CachePoint cp=(CachePoint)e.nextElement();
					GeoVector gv=p.diff(cp);
					if(gv.getDistance() <= max_distance) {
						v.addElement(cp);
					}
				} else {
					// Always add if the max_distance is <=0
					v.addElement(e.nextElement());
				}
			}
		}
		PointComparator pcompare=new PointComparator(p);
		Collections.sort(v, pcompare);
		return(v.elements());
	}

	/**
	 * Get all of the points sorted by distance from a given point.
	 *
	 * @param p the point of origin we want to compare distances from
	 *
	 * @return an Enumeration of CachePoint objects
	 */
	public Enumeration getPoints(Point p) {
		return(getPoints(p, -1.0d));
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
			CachePoint cp=(CachePoint)e.nextElement();
			System.out.println(cp + " - " + home.diff(cp));
		}

		System.out.println("\nOrdered:\n");
		for(Enumeration e=cpl.getPoints(home); e.hasMoreElements(); ) {
			CachePoint cp=(CachePoint)e.nextElement();
			System.out.println(cp + " - " + home.diff(cp));
		}

		System.out.println("\nOrdered, max 100 miles:\n");
		for(Enumeration e=cpl.getPoints(home, 100d); e.hasMoreElements(); ) {
			CachePoint cp=(CachePoint)e.nextElement();
			System.out.println(cp + " - " + home.diff(cp));
		}

	}

}

