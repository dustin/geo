// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Country.java,v 1.1 2001/06/14 09:36:17 dustin Exp $

package net.spy.geo;

import java.sql.*;
import java.util.*;

import net.spy.db.*;
import net.spy.geo.sp.*;

/**
 * Represents a country.
 */
public class Country extends Object {

	private int id=-1;
	private String abbr=null;
	private String name=null;

	/**
	 * Get an instance of Country.
	 */
	public Country(int id) throws Exception {
		super();
		DBSP dbsp=new GetCountryByID(new GeoConfig());
		dbsp.set("id", id);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such country:  " + id);
		}
		initFromResultSet(rs);
		rs.close();
		dbsp.close();
	}

	/**
	 * Get an instance of Country.
	 */
	public Country(String abbr) throws Exception {
		super();
		DBSP dbsp=new GetCountryByAbbr(new GeoConfig());
		dbsp.set("abbr", abbr);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such country:  " + abbr);
		}
		initFromResultSet(rs);
		rs.close();
		dbsp.close();
	}

	// List all countries.
	private Country(ResultSet rs) throws Exception {
		super();
		initFromResultSet(rs);
	}

	private void initFromResultSet(ResultSet rs) throws Exception {
		id=rs.getInt("id");
		abbr=rs.getString("abbr");
		name=rs.getString("name");
	}

	/**
	 * Get a list of Country objects.
	 */
	public static Enumeration listCountries() throws Exception {
		DBSP dbsp=new GetAllCountries(new GeoConfig());
		Vector v=new Vector();
		ResultSet rs=dbsp.executeQuery();
		while(rs.next()) {
			v.addElement(new Country(rs));
		}
		rs.close();
		dbsp.close();

		return(v.elements());
	}

	/**
	 * Get the name of the country.
	 */
	public String toString() {
		return(name);
	}

	/**
	 * Get the ID.
	 */
	public int getId() {
		return(id);
	}

	/**
	 * Get the 2 letter ISO abbreviation.
	 */
	public String getAbbr() {
		return(abbr);
	}

	/**
	 * Get the name of the country.
	 */
	public String getName() {
		return(name);
	}

}
