// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Polygon.java,v 1.3 2001/06/19 07:34:29 dustin Exp $

package net.spy.geo;

import java.util.*;
import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * A closed set of points.
 */
public class Polygon extends Vector {

	private String name=null;

	/**
	 * Get an instance of Polygon.
	 */
	public Polygon(String name) {
		super();
		this.name=name;
	}

	/**
	 * For subclasses who will set the name specifically.
	 */
	protected Polygon() {
		super();
	}

	/**
	 * Ask for an Enumeration of DBPolygons containing the given Point.
	 */
	public static Enumeration getAreasForPoint(Point p) throws Exception {
		Vector rv=new Vector();
		DBSP dbsp=new GetPossibleAreas(new GeoConfig());
		dbsp.set("latitude", (float)p.getLatitude());
		dbsp.set("longitude", (float)p.getLongitude());
		ResultSet rs=dbsp.executeQuery();
		Vector v=new Vector();
		while(rs.next()) {
			v.addElement(new Integer(rs.getInt("id")));
		}
		rs.close();
		dbsp.close();

		for(Enumeration e=v.elements(); e.hasMoreElements(); ) {
			Integer io=(Integer)e.nextElement();
			int i=io.intValue();

			DBPolygon poly=new DBPolygon(i);
			if(poly.containsPoint(p)) {
				rv.addElement(poly);
			}
		}

		return(rv.elements());
	}

	/**
	 * A string of this polygon.
	 */
	public String toString() {
		// return(name + " has " + size() + " points:  " + super.toString());
		return(name + " has " + size() + " points.");
	}

	/**
	 * Does this polygon contain the given point?
	 */
	public boolean containsPoint(Point p) {
		int counter=0, i=0;
		Point p1=null, p2=null;
		int n=size();

		p1=(Point)elementAt(i);
		for(i=1; i<=n; i++) {
			p2=(Point)elementAt(i%n); // Mod so size()+1 == 0
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
	protected void setName(String name) {
		this.name=name;
	}

	public static void main(String args[]) throws Exception {
		Point p=new Point(Double.parseDouble(args[0]),
			Double.parseDouble(args[1]));

		Enumeration polys=getAreasForPoint(p);
		if(polys.hasMoreElements()) {
			for(; polys.hasMoreElements(); ) {
				DBPolygon poly=(DBPolygon)polys.nextElement();
				System.out.println("Point is in " + poly.getName());
				System.out.println("Center point is " + poly.getCenter());
				System.out.println("Width is " + poly.getWidth());
				System.out.println("Height is " + poly.getHeight());
			}
		} else {
			System.out.println("No matching area.");
		}
	}

}
