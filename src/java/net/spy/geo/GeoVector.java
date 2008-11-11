// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoVector.java,v 1.3 2001/06/13 03:45:32 dustin Exp $

package net.spy.geo;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * Distance and bearing.
 */
public class GeoVector extends Object implements Serializable {

	private double distance=0d;
	private double bearing=0d;

	/**
	 * Get an instance of GeoVector.
	 */
	public GeoVector(double d, double b) {
		super();
		distance=d;
		bearing=b;
	}

	/**
	 * String representation.
	 */
	@Override
	public String toString() {
		NumberFormat nf=NumberFormat.getInstance();
		nf.setMaximumFractionDigits(4);
		return(nf.format(distance) + "mi at " + nf.format(bearing)
			+ " degrees (" + getDirection() + ")");
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

		for(int i=0; i*22.5<bearing; i++) {
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
