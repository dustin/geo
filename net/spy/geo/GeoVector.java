// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoVector.java,v 1.1 2001/06/12 07:52:51 dustin Exp $

package net.spy.geo;

/**
 * Distance and bearing.
 */
public class GeoVector extends Object {

	private double distance=0d;
	private double bearing=0d;

	/**
	 * Get an instance of GeoVector.
	 */
	public GeoVector(double distance, double bearing) {
		super();
		this.distance=distance;
		this.bearing=bearing;
	}

	/**
	 * String representation.
	 */
	public String toString() {
		return(distance + "mi at " + bearing + " degrees ("
			+ getDirection() + ")");
	}

	/**
	 * Get a string description of the direction.
	 */
	public String getDirection() {
		String directions[]={
			"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S",
			"SSW", "SW", "WSW", "W", "WNW", "NW", "NNW", "N"
			};
		String rv=null;

		double d=11.25;
		for(int i=0; (double)i*22.5<bearing; i++) {
			rv=directions[i];
		}
		return(rv);
	}

	/**
	 * Get the distance (in miles).
	 */
	public double getDistance() {
		return(distance);
	}

	/**
	 * Get the bearing (in degrees).
	 */
	public double getBearing() {
		return(bearing);
	}

}
