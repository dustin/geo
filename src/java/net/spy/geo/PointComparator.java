// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: PointComparator.java,v 1.2 2001/06/13 03:45:35 dustin Exp $

package net.spy.geo;

import java.util.Comparator;

/**
 * Order Points by distance from a common point.
 */
public class PointComparator extends Object
	implements Comparator, java.io.Serializable {

	private Point commonPoint=null;

	/**
	 * Get an instance of PointComparator.
	 */
	public PointComparator(Point p) {
		super();
		this.commonPoint=p;
	}

	/**
	 * Compare two Point objects against our common point.
	 */
	public int compare(Object o1, Object o2) {
		Point p1=(Point)o1;
		Point p2=(Point)o2;

		GeoVector gv1=commonPoint.diff(p1);
		GeoVector gv2=commonPoint.diff(p2);

		return(compare(gv1.getDistance(), gv2.getDistance()));
	}

	// double comparator.
	private int compare(double a, double b) {
		int rv=0;
		if(a<b) {
			rv=-1;
		} else if(a>b) {
			rv=1;
		} else {
			rv=0;
		}
		return(rv);
	}

}
