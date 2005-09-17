// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// arch-tag: B123301D-3503-11D9-A8FA-000393CFE6B8

package net.spy.geo.services;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import net.spy.geo.DBPolygon;
import net.spy.geo.Point;
import net.spy.geo.Polygon;
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
	public Vector getPointInfo(double lat, double lon) throws Exception {

		Point p=new Point(lat, lon);
		Collection<DBPolygon> polys=Polygon.getAreasForPoint(p);

		if(polys.size() == 0) {
			throw new Exception("No information found for that point.");
		}

		Vector rv=new Vector();
		for(DBPolygon poly : polys) {
			Point center=poly.getCenter();

			Double height=new Double(poly.getHeight());
			Double width=new Double(poly.getWidth());

			Hashtable h=new Hashtable();
			h.put("width", width);
			h.put("height", height);
			h.put("name", poly.getName());

			// Boundary1 is minx, miny
			// Boundary2 is maxx, maxy
			Point boundary1=poly.getBoundary1();
			Point boundary2=poly.getBoundary2();

			double western=Math.min(boundary1.getLongitude(),
				boundary2.getLongitude());
			double eastern=Math.max(boundary1.getLongitude(),
				boundary2.getLongitude());
			double northern=Math.max(boundary1.getLatitude(),
				boundary2.getLatitude());
			double southern=Math.min(boundary1.getLatitude(),
				boundary2.getLatitude());

			h.put("eastern_border", new Double(eastern));
			h.put("northern_border", new Double(northern));
			h.put("western_border", new Double(western));
			h.put("southern_border", new Double(southern));

			h.put("center_longitude", new Double(center.getLongitude()));
			h.put("center_latitude", new Double(center.getLatitude()));

			String source=poly.getSource();
			String type="unknown";
			if(source.startsWith("zt")) {
				type="zipcode";
			} else if(source.startsWith("co")) {
				type="county";
			} else if(source.startsWith("st")) {
				type="state";
			}

			h.put("type", type);

			rv.addElement(h);
		}

		return(rv);
	}

}