// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Polygon.java,v 1.3 2001/06/19 07:34:29 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import net.spy.db.DBSP;
import net.spy.geo.sp.GetPossibleAreas;

/**
 * A closed set of points.
 */
public class Polygon extends ArrayList<Point> {

	private String name=null;

	/**
	 * Get an instance of Polygon.
	 */
	public Polygon(String n) {
		super();
		this.name=n;
	}

	/**
	 * For subclasses who will set the name specifically.
	 */
	protected Polygon() {
		super();
	}

	/**
	 * Get a collection of DBPolygons containing the given Point.
	 */
	public static Collection<DBPolygon> getAreasForPoint(Point p) throws Exception {
		Collection<DBPolygon> rv=new ArrayList<DBPolygon>();
		DBSP dbsp=new GetPossibleAreas(GeoConfig.getInstance());
		dbsp.set("latitude", (float)p.getLatitude());
		dbsp.set("longitude", (float)p.getLongitude());
		ResultSet rs=dbsp.executeQuery();
		ArrayList<Integer> a=new ArrayList<Integer>();
		while(rs.next()) {
			a.add(new Integer(rs.getInt("id")));
		}
		rs.close();
		dbsp.close();

		for(int i : a) {
			DBPolygon poly=new DBPolygon(i);
			if(poly.containsPoint(p)) {
				rv.add(poly);
			}
		}

		return(rv);
	}

	/**
	 * A string of this polygon.
	 */
	public String toString() {
		return(name + " has " + size() + " points.");
	}

	/**
	 * Does this polygon contain the given point?
	 */
	public boolean containsPoint(Point p) {
		int counter=0, i=0;
		Point p1=null, p2=null;
		int n=size();

		p1=get(i);
		for(i=1; i<=n; i++) {
			p2=get(i%n); // Mod so size()+1 == 0
			// Convenience variables.
			double y1=p1.getLatitude();
			double x1=p1.getLongitude();
			double y2=p2.getLatitude();
			double x2=p2.getLongitude();
			double y=p.getLatitude();
			double x=p.getLongitude();
			if(y > Math.min(y1, y2)) {
				if(y <= Math.max(y1, y2)) {
					if(x <= Math.max(x1, x2)) {
						if(y1 != y2) {
							double xinters = (y-y1)*(x2-x1)/(y2-y1)+x1;
							if(x1 == x2 || x <= xinters) {
								counter++;
							}
						}
					}
				}
			}
			p1=p2;
		}

		// If we crossed an even number of sides, we're outside.
		return( (counter%2)!=0 );
	}

	/**
	 * Get the name of this polygon.
	 */
	public String getName() {
		return(name);
	}

	/**
	 * Set the name of this polygon.
	 */
	protected void setName(String to) {
		this.name=to;
	}

}
