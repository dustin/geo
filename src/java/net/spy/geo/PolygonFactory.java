// Copyright (c) 2006  Dustin Sallings <dustin@spy.net>
// arch-tag: 6A6B2852-4E2B-4EC3-BF9B-623F298F1EAE

package net.spy.geo;

import java.lang.ref.SoftReference;
import java.sql.ResultSet;

import net.spy.SpyObject;
import net.spy.cache.SimpleCache;
import net.spy.db.DBSPLike;
import net.spy.geo.sp.GetPolygonByID;
import net.spy.geo.sp.GetPolygonDataByID;
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
			CloseUtil.close((DBSPLike)db);
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
			CloseUtil.close((DBSPLike)db2);
		}

		return rv;
	}
}
