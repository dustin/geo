// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: CachePoint.java,v 1.7 2001/06/14 21:21:16 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.spy.db.DBSP;
import net.spy.geo.sp.AddPoint;
import net.spy.geo.sp.GetCachePointByID;
import net.spy.geo.sp.GetNextID;

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
	private String approach=null;
	private int country=-1;

	/**
	 * Get an instance of CachePoint.
	 */
	public CachePoint(String n, Point p) {
		super();
		this.name=n;
		setLongitude(p.getLongitude());
		setLatitude(p.getLatitude());
	}

	/**
	 * Get a cachepoint by ID.
	 */
	public CachePoint(int id) throws Exception {
		super();
		DBSP dbsp=new GetCachePointByID(GeoConfig.getInstance());
		dbsp.set("id", id);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such ID:  " + id);
		}
		initFromResultSet(rs);
		rs.close();
		dbsp.close();
	}

	private void initFromResultSet(ResultSet rs) throws Exception {
		pointId=rs.getInt("point_id");
		country=rs.getInt("country");
		name=rs.getString("name");
		description=rs.getString("description");
		waypointId=rs.getString("waypoint_id");
		approach=rs.getString("approach");
		creatorId=rs.getInt("creator_id");
		difficulty=rs.getFloat("difficulty");
		terrain=rs.getFloat("terrain");
		dateCreated=rs.getTimestamp("created");

		setLongitude(rs.getFloat("longitude"));
		setLatitude(rs.getFloat("latitude"));
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

		Element el=d.createElement("pointid");
		el.appendChild(d.createTextNode("" + pointId));
		root.appendChild(el);

		el=d.createElement("name");
		el.appendChild(d.createTextNode(name));
		root.appendChild(el);

		el=d.createElement("description");
		el.appendChild(d.createTextNode(description));
		root.appendChild(el);

		if(approach!=null)  {
			el=d.createElement("approach");
			el.appendChild(d.createTextNode(approach));
			root.appendChild(el);
		}

		el=d.createElement("longitude");
		el.appendChild(d.createTextNode("" + getLongitude()));
		root.appendChild(el);

		el=d.createElement("latitude");
		el.appendChild(d.createTextNode("" + getLatitude()));
		root.appendChild(el);

		try {
			el=d.createElement("country");
			el.appendChild(d.createTextNode(getCountry().getAbbr()));
			root.appendChild(el);
		} catch(Exception e) {
			e.printStackTrace();
		}

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
	protected void setPointId(int id) {
		this.pointId=id;
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
	public void setName(String n) {
		this.name=n;
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
	public void setDescription(String d) {
		this.description=d;
	}

	/**
	 * Set the creator ID.
	 */
	public void setCreatorId(int id) {
		this.creatorId=id;
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
	public void setWaypointId(String to) {
		this.waypointId=to;
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
	public void setDifficulty(float to) {
		this.difficulty=to;
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
	public void setTerrain(float to) {
		this.terrain=to;
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
	public void setDateCreated(Date to) {
		this.dateCreated=to;
	}

	/**
	 * Get the date this record was created.
	 */
	public Date getDateCreated() {
		return(dateCreated);
	}

	/**
	 * Set the approach hint.
	 */
	public void setApproach(String to) {
		this.approach=to;
	}

	/**
	 * Get the approach hint.
	 */
	public String getApproach() {
		return(approach);
	}

	/**
	 * Set the country (by number).
	 */
	public void setCountry(int to) {
		this.country=to;
	}

	/**
	 * Get the country.
	 */
	public Country getCountry() throws Exception {
		return(new Country(country));
	}

	/**
	 * Save this new point.
	 */
	public void save() throws Exception {
		DBSP keyget=new GetNextID(GeoConfig.getInstance());
		ResultSet rs=keyget.executeQuery();
		rs.next();
		int wid=rs.getInt("id");
		keyget.close();
		DBSP dbsp=new AddPoint(GeoConfig.getInstance());

		dbsp.set("creator_id", creatorId);
		dbsp.set("name", name);
		dbsp.set("description", description);
		dbsp.set("longitude", (float)getLongitude());
		dbsp.set("latitude", (float)getLatitude());
		dbsp.set("waypoint_id", "C" + wid);
		dbsp.set("difficulty", difficulty);
		dbsp.set("terrain", terrain);
		dbsp.set("approach", approach);
		dbsp.set("country", country);

		dbsp.executeUpdate();
		dbsp.close();

		// Let the cachepoint list know that we added one
		CachePointList cpl=new CachePointList();
		cpl.requestUpdate();
	}

}
