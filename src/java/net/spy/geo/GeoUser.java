// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoUser.java,v 1.1 2001/06/13 03:45:31 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.Date;

import net.spy.db.DBSP;
import net.spy.geo.sp.LookupUserByID;
import net.spy.geo.sp.LookupUserByName;
import net.spy.geo.sp.RegisterUser;
import net.spy.util.Digest;

/**
 * The User.
 */
public class GeoUser extends Object implements java.io.Serializable {

	private int userid=-1;
	private String username=null;
	private String password=null;
	private String fullName=null;
	private String email=null;
	private String url=null;
	private boolean active=true;
	private int zipcode=0;
	private float longitude=0;
	private float latitude=0;
	private Date timestamp=null;

	private boolean isNew=false;

	/**
	 * Get an instance of GeoUser for a new user.
	 */
	public GeoUser() {
		super();
		isNew=true;
	}

	/**
	 * Get a User by username.
	 */
	public GeoUser(String user) throws Exception {
		super();
		DBSP dbsp=new LookupUserByName(GeoConfig.getInstance());
		dbsp.set("username", user);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such user:  " + user);
		}
		initFromResultSet(rs);
		rs.close();
		dbsp.close();
	}

	/**
	 * Get a User by Id.
	 */
	public GeoUser(int uid) throws Exception {
		super();
		DBSP dbsp=new LookupUserByID(GeoConfig.getInstance());
		dbsp.set("user_id", uid);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such user:  " + uid);
		}
		initFromResultSet(rs);
		rs.close();
		dbsp.close();
	}

	private void initFromResultSet(ResultSet rs) throws Exception {
		userid=rs.getInt("user_id");
		username=rs.getString("username");
		password=rs.getString("password");
		fullName=rs.getString("full_name");
		email=rs.getString("email");
		url=rs.getString("url");
		active=rs.getBoolean("active");
		zipcode=rs.getInt("zipcode");
		longitude=rs.getFloat("longitude");
		latitude=rs.getFloat("latitude");
		timestamp=rs.getTimestamp("ts");
	}

	/**
	 * Save the user.
	 */
	public void save() throws Exception {
		DBSP dbsp=null;

		if(isNew) {
			dbsp=new RegisterUser(GeoConfig.getInstance());
		} else {
			throw new Exception("Update not implemented yet.");
		}

		dbsp.set("username", username);
		dbsp.set("password", password);
		dbsp.set("full_name", fullName);
		dbsp.set("email", email);
		dbsp.set("url", url);
		dbsp.set("zipcode", zipcode);
		dbsp.set("longitude", longitude);
		dbsp.set("latitude", latitude);

		dbsp.executeUpdate();
		dbsp.close();
	}

	/**
	 * String me.
	 */
	public String toString() {
		return(username + "(" + userid + ") - " + fullName);
	}

	/**
	 * Get the user ID.
	 */
	public int getUserid() {
		return(userid);
	}

	public String getUsername() {
		return(username);
	}

	public void setUsername(String to) {
		this.username=to;
	}

	public void checkPassword(String against) throws Exception {
		Digest d=new Digest();
		if(!d.checkPassword(against, password)) {
			throw new Exception("Password mismatch.");
		}
	}

	public void setPassword(String to) {
		Digest d=new Digest();
		this.password=d.getHash(to);;
	}

	public String getFullName() {
		return(fullName);
	}

	public void setFullName(String to) {
		this.fullName=to;
	}

	public String getEmail() {
		return(email);
	}

	public void setEmail(String to) {
		this.email=to;
	}

	public String getUrl() {
		return(url);
	}

	public void setUrl(String to) {
		this.url=to;
	}

	public boolean getActive() {
		return(active);
	}

	public void setActive(boolean to) {
		this.active=to;
	}

	public int getZipcode() {
		return(zipcode);
	}

	public void setZipcode(int to) {
		this.zipcode=to;
	}

	public float getLongitude() {
		return(longitude);
	}

	public void setLongitude(float to) {
		this.longitude=to;
	}

	public float getLatitude() {
		return(latitude);
	}

	public void setLatitude(float to) {
		this.latitude=to;
	}

	public Date getTimestamp() {
		return(timestamp);
	}

}
