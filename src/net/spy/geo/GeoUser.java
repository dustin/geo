// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoUser.java,v 1.1 2001/06/13 03:45:31 dustin Exp $

package net.spy.geo;

import java.sql.*;
import java.util.*;
import java.util.Date;

import net.spy.util.*;
import net.spy.db.*;
import net.spy.geo.sp.*;

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
	public GeoUser(String username) throws Exception {
		super();
		DBSP dbsp=new LookupUserByName(new GeoConfig());
		dbsp.set("username", username);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such user:  " + username);
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
		DBSP dbsp=new LookupUserByID(new GeoConfig());
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
			dbsp=new RegisterUser(new GeoConfig());
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

	public void setUsername(String username) {
		this.username=username;
	}

	public void checkPassword(String against) throws Exception {
		Digest d=new Digest();
		if(!d.checkPassword(against, password)) {
			throw new Exception("Password mismatch.");
		}
	}

	public void setPassword(String password) {
		Digest d=new Digest();
		this.password=d.getHash(password);;
	}

	public String getFullName() {
		return(fullName);
	}

	public void setFullName(String fullName) {
		this.fullName=fullName;
	}

	public String getEmail() {
		return(email);
	}

	public void setEmail(String email) {
		this.email=email;
	}

	public String getUrl() {
		return(url);
	}

	public void setUrl(String url) {
		this.url=url;
	}

	public boolean getActive() {
		return(active);
	}

	public void setActive(boolean active) {
		this.active=active;
	}

	public int getZipcode() {
		return(zipcode);
	}

	public void setZipcode(int zipcode) {
		this.zipcode=zipcode;
	}

	public float getLongitude() {
		return(longitude);
	}

	public void setLongitude(float longitude) {
		this.longitude=longitude;
	}

	public float getLatitude() {
		return(latitude);
	}

	public void setLatitude(float latitude) {
		this.latitude=latitude;
	}

	public Date getTimestamp() {
		return(timestamp);
	}

	/**
	 * Testing and what not.
	 */
	public static void main(String args[]) throws Exception {
		GeoUser gu=new GeoUser(args[0]);
		System.out.println(gu);
	}

}
