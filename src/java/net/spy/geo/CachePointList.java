// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePointList.java,v 1.7 2001/06/21 00:17:49 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.spy.SpyThread;
import net.spy.db.DBSP;
import net.spy.geo.sp.GetAllPoints;

/**
 * This object maintains the list of points.
 */
public class CachePointList extends SpyThread implements java.io.Serializable {

	private static String CACHE_MUTEX="CACHE_MUTEX";
	private static Collection<CachePoint> cachePoints=null;

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
				cachePoints=new ArrayList<CachePoint>();
				updatePoints();
				start();
			}
		}
	}

	// Get new points periodically.
	private void updatePoints() {
		Collection<CachePoint> v=new ArrayList<CachePoint>();
		try {
			DBSP dbsp=new GetAllPoints(GeoConfig.getInstance());
			ResultSet rs=dbsp.executeQuery();
			while(rs.next()) {
				v.add(new CachePoint(rs.getInt("point_id")));
			}
			rs.close();
			dbsp.close();
		} catch(Exception e) {
			getLogger().info("Error getting points", e);
		}

		// Now that we've found them all, put them in
		synchronized(CACHE_MUTEX) {
			cachePoints=v;
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
	public Collection<CachePoint> getPoints() {
		synchronized(CACHE_MUTEX) {
			return cachePoints;
		}
	}

	/**
	 * Get the list of cache points within a certain range sorted by distance
	 * from a given point.
	 *
	 * @param p the point of origin we want to compare distances from
	 * @param max_distance the maximum distance in miles we care about
	 *
	 * @return a collection of CachePoint objects
	 */
	public Collection<CachePoint> getPoints(Point p, double max_distance) {
		List<CachePoint> v=new ArrayList<CachePoint>();
		synchronized(CACHE_MUTEX) {
			if(max_distance > 0) {
				for(CachePoint cp : cachePoints) {
					GeoVector gv=p.diff(cp);
					if(gv.getDistance() <= max_distance) {
						v.add(cp);
					}
				}
			} else {
				v.addAll(cachePoints);
			}
		}
		PointComparator pcompare=new PointComparator(p);
		Collections.sort(v, pcompare);
		return v;
	}

	/**
	 * Get all of the points sorted by distance from a given point.
	 *
	 * @param p the point of origin we want to compare distances from
	 *
	 * @return a Collection of CachePoint objects
	 */
	public Collection<CachePoint> getPoints(Point p) {
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

}

