// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePoint.java,v 1.2 2001/06/13 01:01:24 dustin Exp $

package net.spy.geo;

import java.util.*;

/**
 * A Geocache point.
 */
public class CachePoint extends Point {

	private int pointId=-1;
	private String name=null;
	private String description=null;
	private int creatorId=-1;
	private String waypointId=null;
	private float difficulty=1;
	private float terrain=1;
	private Date dateCreated=null;
	private double approach=-1;

	/**
	 * Get an instance of CachePoint.
	 */
	public CachePoint(String name, Point p) {
		super();
		this.name=name;
		setLongitude(p.getLongitude());
		setLatitude(p.getLatitude());
	}

	/**
	 * Print me.
	 */
	public String toString() {
		return(name + " - " + super.toString());
	}

	/**
	 * Set the point ID.
	 */
	protected void setPointId(int pointId) {
		this.pointId=pointId;
	}

	/**
	 * Get the point ID.
	 */
	public int getPointId() {
		return(pointId);
	}

	/**
	 * Set the name of this CachePoint.
	 */
	public void setName(String name) {
		this.name=name;
	}

	/**
	 * Get the name of this CachePoint.
	 */
	public String getName() {
		return(name);
	}

	/**
	 * Get the description of this CachePoint.
	 */
	public String getDescription() {
		return(description);
	}

	/**
	 * Set the description of this CachePoint.
	 */
	public void setDescription(String description) {
		this.description=description;
	}

	/**
	 * Set the creator ID.
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId=creatorId;
	}

	/**
	 * Get the creator ID.
	 */
	public int getCreatorId() {
		return(creatorId);
	}

	/**
	 * Set the waypoint ID.
	 */
	public void setWaypointId(String waypointId) {
		this.waypointId=waypointId;
	}

	/**
	 * Get the waypoint ID.
	 */
	public String getWaypointId() {
		return(waypointId);
	}

	/**
	 * Set the difficulty level.
	 */
	public void setDifficulty(float difficulty) {
		this.difficulty=difficulty;
	}

	/**
	 * Get the difficulty level.
	 */
	public float getDifficulty() {
		return(difficulty);
	}

	/**
	 * Set the terrain level.
	 */
	public void setTerrain(float terrain) {
		this.terrain=terrain;
	}

	/**
	 * Get the terrain level.
	 */
	public float getTerrain() {
		return(terrain);
	}

	/**
	 * Set the date this record was created.
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated=dateCreated;
	}

	/**
	 * Get the date this record was created.
	 */
	public Date getDateCreated() {
		return(dateCreated);
	}

	/**
	 * Set the approach bearing (negative number means no approach
	 * bearing).
	 */
	public void setApproach(double approach) {
		this.approach=approach;
	}

	/**
	 * Get the recommended angle of approach.
	 */
	public double getApproach() {
		return(approach);
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		Vector v=new Vector();

		Point home=new Point(37, 22.110, -121, 59.164);

		v.addElement(
			new CachePoint("GC88C", new Point(37, 15.658, -121, 57.330)));
		v.addElement(
			new CachePoint("GC510", new Point(37, 22.299, -122, 05.059)));
		v.addElement(
			new CachePoint("BinDir", new Point(42.69, -87.91)));

		PointComparator pcompare=new PointComparator(home);
		Collections.sort(v, pcompare);
		for(Enumeration e=v.elements(); e.hasMoreElements(); ) {
			Point p=(Point)e.nextElement();
			System.out.println(p + " -- " + home.diff(p));
		}
	}

}
