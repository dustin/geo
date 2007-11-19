// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>

package net.spy.geo.services;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import net.spy.geo.DBPolygon;
import net.spy.geo.Point;
import net.spy.geo.PolygonFactory;
import net.spy.rpc.services.Remote;

/**
 * XML RPC services for geography data.
 */
public class Geo extends Remote {

	/**
	 * Get an instance of Geo.
	 */
	public Geo() {
		super();
	}

	/**
	 * Get the info for the given point.
	 */
	@SuppressWarnings("unchecked")
	public Vector getPointInfo(double lat, double lon) throws Exception {

		Point p=new Point(lat, lon);
		PolygonFactory pf=PolygonFactory.getInstance();
		Collection<DBPolygon> polys=pf.getAreasForPoint(p);

		if(polys.isEmpty()) {
			throw new Exception("No information found for that point.");
		}

		Vector rv=new Vector();
		for(DBPolygon poly : polys) {
			Point center=poly.getCenter();

			Hashtable h=new Hashtable();
			h.put("width", new Double(poly.getWidth()));
			h.put("height", new Double(poly.getHeight()));
			h.put("name", poly.getName());

			h.put("eastern_border", new Double(poly.getEasternBorder()));
			h.put("northern_border", new Double(poly.getNorthernBorder()));
			h.put("western_border", new Double(poly.getWesternBorder()));
			h.put("southern_border", new Double(poly.getSouthernBorder()));

			h.put("center_longitude", new Double(center.getLongitude()));
			h.put("center_latitude", new Double(center.getLatitude()));

			h.put("type", String.valueOf(poly.getType()));

			rv.add(h);
		}

		return(rv);
	}

}
