// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: Country.java,v 1.1 2001/06/14 09:36:17 dustin Exp $

package net.spy.geo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import net.spy.db.DBSP;
import net.spy.db.DBSPLike;
import net.spy.geo.sp.GetAllCountries;
import net.spy.geo.sp.GetCountryByAbbr;
import net.spy.geo.sp.GetCountryByID;
import net.spy.util.CloseUtil;

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
		GetCountryByID db=new GetCountryByID(GeoConfig.getInstance());
		try {
			db.setId(cid);
			ResultSet rs=db.executeQuery();
			if(!rs.next()) {
				throw new Exception("No such country:  " + cid);
			}
			initFromResultSet(rs);
			rs.close();
		} finally {
			CloseUtil.close((DBSPLike)db);
		}
	}

	/**
	 * Get an instance of Country.
	 */
	public Country(String abbrev) throws Exception {
		super();
		GetCountryByAbbr db=new GetCountryByAbbr(GeoConfig.getInstance());
		try {
			db.setAbbr(abbrev);
			ResultSet rs=db.executeQuery();
			if(!rs.next()) {
				throw new Exception("No such country:  " + abbrev);
			}
			initFromResultSet(rs);
			rs.close();
		} finally {
			CloseUtil.close((DBSPLike)db);
		}
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
	public static Collection<Country> listCountries() throws Exception {
		DBSP db=new GetAllCountries(GeoConfig.getInstance());
		Collection<Country> rv=new ArrayList<Country>();
		try {
			ResultSet rs=db.executeQuery();
			while(rs.next()) {
				rv.add(new Country(rs));
			}
			rs.close();
		} finally {
			CloseUtil.close((DBSPLike)db);
		}
		return rv;
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
