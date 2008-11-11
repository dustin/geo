// Copyright (c) 2006  Dustin Sallings <dustin@spy.net>

package net.spy.geo;

import java.lang.ref.SoftReference;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import net.spy.SpyObject;
import net.spy.cache.SimpleCache;
import net.spy.geo.sp.GetPolygonByID;
import net.spy.geo.sp.GetPolygonDataByID;
import net.spy.geo.sp.GetPossibleAreas;
import net.spy.util.CloseUtil;

/**
 * Polygon loader.
 */
public class PolygonFactory extends SpyObject {

	private static String CACHE_PREFIX="polygon_";
	private static final long CACHE_TIME=3600000;

	private static PolygonFactory instance=new PolygonFactory();

	/**
	 * Get the polygon factory.
	 */
	public static PolygonFactory getInstance() {
		return instance;
	}

	/**
	 * Get the polygon with the given ID.
	 *
	 * @param id the ID of the polygon we want.
	 * @return the polygon
	 * @throws Exception if we can't get it
	 */
	public DBPolygon getPolygon(int id) throws Exception {
		SimpleCache sc=SimpleCache.getInstance();
		String cacheKey=CACHE_PREFIX + id;
		DBPolygon rv=(DBPolygon)sc.get(cacheKey);
		if(rv == null) {
			rv=getPolygonFromDB(id);
			sc.store(cacheKey, new SoftReference<DBPolygon>(rv), CACHE_TIME);
		}
		return rv;
	}

	private DBPolygon getPolygonFromDB(int id) throws Exception {
		DBPolygon rv=null;
		GetPolygonByID db=new GetPolygonByID(GeoConfig.getInstance());
		try {
			db.setId(id);
			ResultSet rs=db.executeQuery();
			if(!rs.next()) {
				throw new Exception("No such polygon.");
			}
			rv=new DBPolygon(rs);
			rs.close();
		} finally {
			CloseUtil.close(db);
		}

		GetPolygonDataByID db2=new GetPolygonDataByID(GeoConfig.getInstance());
		try {
			db2.set("id", id);
			ResultSet rs=db2.executeQuery();

			while(rs.next()) {
				rv.add(new Point(
						rs.getFloat("latitude"), rs.getFloat("longitude") ));
			}
		} finally {
			CloseUtil.close(db2);
		}

		return rv;
	}

	/**
	 * Get a collection of DBPolygons containing the given Point.
	 */
	public Collection<DBPolygon> getAreasForPoint(Point p)
		throws Exception {

		// Find the potential matches.
		Collection<DBPolygon> rv=new ArrayList<DBPolygon>();
		ArrayList<Integer> a=new ArrayList<Integer>();
		GetPossibleAreas db=new GetPossibleAreas(GeoConfig.getInstance());
		try {
			db.setLatitude((float)p.getLatitude());
			db.setLongitude((float)p.getLongitude());
			ResultSet rs=db.executeQuery();
			while(rs.next()) {
				a.add(new Integer(rs.getInt("id")));
			}
			rs.close();
		} finally {
			CloseUtil.close(db);
		}

		// Filter out all of the polygons that don't contain the given point
		PolygonFactory pf=getInstance();
		for(int i : a) {
			DBPolygon poly=pf.getPolygon(i);
			if(poly.containsPoint(p)) {
				rv.add(poly);
			}
		}

		return(rv);
	}
}
