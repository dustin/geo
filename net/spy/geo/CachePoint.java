// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePoint.java,v 1.3 2001/06/13 09:51:44 dustin Exp $

package net.spy.geo;

import java.util.*;
import java.util.Date;
import java.sql.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

// Some XML stuff
import org.w3c.dom.*;
import org.xml.sax.*;

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
	private float approach=-1;

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
	 * XML me.
	 *
	 * @return the Element we created to hold the data
	 */
	public Element appendXML(Document d) {
		Element root=d.createElement("point");
		d.appendChild(root);

		Element el=d.createElement("name");
		el.appendChild(d.createTextNode(name));
		root.appendChild(el);

		el=d.createElement("description");
		el.appendChild(d.createTextNode(description));
		root.appendChild(el);

		if(approach>=0)  {
			el=d.createElement("approach");
			el.appendChild(d.createTextNode("" + approach));
			root.appendChild(el);
		}

		el=d.createElement("longitude");
		el.appendChild(d.createTextNode("" + getLongitude()));
		root.appendChild(el);

		el=d.createElement("latitude");
		el.appendChild(d.createTextNode("" + getLatitude()));
		root.appendChild(el);

		el=d.createElement("created");
		el.appendChild(d.createTextNode(dateCreated.toString()));
		root.appendChild(el);

		el=d.createElement("terrain");
		el.appendChild(d.createTextNode("" + terrain));
		root.appendChild(el);

		el=d.createElement("difficulty");
		el.appendChild(d.createTextNode("" + difficulty));
		root.appendChild(el);

		el=d.createElement("creatorid");
		el.appendChild(d.createTextNode("" + creatorId));
		root.appendChild(el);

		el=d.createElement("waypoint");
		el.appendChild(d.createTextNode(waypointId));
		root.appendChild(el);

		return(root);
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
	public void setApproach(float approach) {
		this.approach=approach;
	}

	/**
	 * Get the recommended angle of approach.
	 */
	public float getApproach() {
		return(approach);
	}

	/**
	 * Save this new point.
	 */
	public void save() throws Exception {
		DBSP keyget=new GetNextID(new GeoConfig());
		ResultSet rs=keyget.executeQuery();
		rs.next();
		int wid=rs.getInt("id");
		keyget.close();
		DBSP dbsp=new AddPoint(new GeoConfig());

		dbsp.set("creator_id", creatorId);
		dbsp.set("name", name);
		dbsp.set("description", description);
		dbsp.set("longitude", (float)getLongitude());
		dbsp.set("latitude", (float)getLatitude());
		dbsp.set("waypoint_id", "C" + wid);
		dbsp.set("difficulty", difficulty);
		dbsp.set("terrain", terrain);
		dbsp.set("approach", approach);

		dbsp.executeUpdate();
		dbsp.close();
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
