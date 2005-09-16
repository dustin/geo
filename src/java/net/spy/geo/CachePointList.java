// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePointList.java,v 1.7 2001/06/21 00:17:49 dustin Exp $

package net.spy.geo;

import java.util.*;
import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * This object maintains the list of points.
 */
public class CachePointList extends Thread implements java.io.Serializable {

	private static String CACHE_MUTEX="CACHE_MUTEX";
	private static Vector cachePoints=null;

	private boolean done=false;

	/**
	 * Get an instance of CachePointList.
	 */
	public CachePointList() throws Exception {
		super("CachePointList");
		setDaemon(true);
		initPoints();
	}

	/**
	 * Print me.
	 */
	public String toString() {
		return(super.toString() + " - Herding " + numPoints() + " points.");
	}

	// Initially get the points.
	private void initPoints() throws Exception {
		synchronized(CACHE_MUTEX) {
			if(cachePoints==null) {
				cachePoints=new Vector();
				updatePoints();
				start();
			}
		}
	}

	// Get new points periodically.
	private void updatePoints() {
		Vector v=new Vector();
		try {
			DBSP dbsp=new GetAllPoints(new GeoConfig());
			ResultSet rs=dbsp.executeQuery();
			while(rs.next()) {
				v.addElement(new CachePoint(rs.getInt("point_id")));
			}
			rs.close();
			dbsp.close();
		} catch(Exception e) {
			System.err.println("Error getting points");
			e.printStackTrace();
		}

		// Now that we've found them all, put them in
		synchronized(CACHE_MUTEX) {
			// Out with the old
			cachePoints.removeAllElements();
			// In with the new
			for(Enumeration e=v.elements(); e.hasMoreElements(); ) {
				cachePoints.addElement(e.nextElement());
			}
		}
	}

	/**
	 * Keep going until it's time to stop.
	 */
	public void run() {
		while(!done) {
			try {
				synchronized(CACHE_MUTEX) {
					CACHE_MUTEX.wait(3600*1000);
				}
				// go ahead and do another check here.
				if(!done) {
					updatePoints();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tell the thread to stop at its next convenience.
	 */
	public void finish() {
		done=true;
	}

	/**
	 * Get an unsorted list of cache points.
	 */
	public Enumeration getPoints() {
		Enumeration e=null;
		synchronized(CACHE_MUTEX) {
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
		synchronized(CACHE_MUTEX) {
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
	 * Request an update of the point data.
	 */
	public void requestUpdate() {
		synchronized(CACHE_MUTEX) {
			CACHE_MUTEX.notifyAll();
		}
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

