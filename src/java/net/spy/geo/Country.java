// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Country.java,v 1.1 2001/06/14 09:36:17 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Vector;

import net.spy.db.DBSP;
import net.spy.geo.sp.GetAllCountries;
import net.spy.geo.sp.GetCountryByAbbr;
import net.spy.geo.sp.GetCountryByID;

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
	public Country(int cid) throws Exception {
		super();
		DBSP dbsp=new GetCountryByID(GeoConfig.getInstance());
		dbsp.set("id", cid);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such country:  " + cid);
		}
		initFromResultSet(rs);
		rs.close();
		dbsp.close();
	}

	/**
	 * Get an instance of Country.
	 */
	public Country(String abbrev) throws Exception {
		super();
		DBSP dbsp=new GetCountryByAbbr(GeoConfig.getInstance());
		dbsp.set("abbr", abbrev);
		ResultSet rs=dbsp.executeQuery();
		if(!rs.next()) {
			throw new Exception("No such country:  " + abbrev);
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
		DBSP dbsp=new GetAllCountries(GeoConfig.getInstance());
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
