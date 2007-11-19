// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>

package net.spy.geo.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.spy.db.SpyCacheDB;
import net.spy.geo.GeoConfig;
import net.spy.rpc.services.Remote;
import net.spy.util.CloseUtil;

/**
 * Get zipcode information.
 */
public class ZipCode extends Remote {

	/**
	 * Get an instance of ZipCode.
	 */
	public ZipCode() {
		super();
	}

	@SuppressWarnings("unchecked")
	private Hashtable getZipData(ResultSet rs) throws Exception {
		Hashtable rv=new Hashtable();
		rv.put("zipcode", new Integer(rs.getInt("zipcode")));
		rv.put("state_code", rs.getString("state_code"));
		rv.put("state", rs.getString("state"));
		rv.put("city", rs.getString("city"));
		rv.put("county", rs.getString("county"));
		rv.put("longitude", new Double(rs.getString("longitude")));
		rv.put("latitude", new Double(rs.getString("latitude")));
		return(rv);
	}

	/**
	 * Lookup a zipcode.
	 */
	public Hashtable lookupZip(int zipcode) throws Exception {
		Hashtable h=null;

		SpyCacheDB db=new SpyCacheDB(GeoConfig.getInstance());
		PreparedStatement pst=db.prepareStatement(
			"select * from geo.zipcode_view where zipcode=?", 900);
		pst.setInt(1, zipcode);
		ResultSet rs=pst.executeQuery();

		try {
			if(!rs.next()) {
				throw new Exception(zipcode + " not found in this database.");
			}
			h=getZipData(rs);
		} finally {
			rs.close();
			pst.close();
			db.close();
		}

		return(h);
	}

	private boolean isValidKey(String key) {
		boolean isvalid=false;

		if(key.equals("zipcode")) {
			isvalid=true;
		} else if(key.equals("state_code")) {
			isvalid=true;
		} else if(key.equals("state")) {
			isvalid=true;
		} else if(key.equals("city")) {
			isvalid=true;
		} else if(key.equals("county")) {
			isvalid=true;
		}

		return(isvalid);
	}

	/**
	 * Lookup zipcodes by a specification.  The specification defines the
	 * limiters of the query.  The following attributes may be set (all
	 * lowercase):
	 *
	 * <ul>
	 *  <li>zipcode - To specify a single zipcode to look up</li>
	 *  <li>state_code - Two character state code (all caps)</li>
	 *  <li>state - Full name of the state</li>
	 *  <li>city - Name of the city</li>
	 *  <li>county - Name of the county</li>
	 * </ul>
	 *
	 * @return a Vector of Hashtables representing the zipcodes.  No more
	 * than 250 records may be returned.
	 */
	@SuppressWarnings("unchecked")
	public Vector lookupZips(Hashtable spec) throws Exception {
		Vector keys=new Vector();
		for(Enumeration e=spec.keys(); e.hasMoreElements();) {
			keys.addElement(e.nextElement());
		}

		StringBuffer query=new StringBuffer();
		query.append("select * from geo.zipcode_view ");

		if(keys.size() > 0) {
			query.append("where ");
			for(Enumeration e=keys.elements(); e.hasMoreElements(); ) {
				String k=(String)e.nextElement();

				if(!isValidKey(k)) {
					throw new Exception(k + " is an invalid parameter.");
				}

				query.append(k);
				query.append("=?");

				if(e.hasMoreElements()) {
					query.append(" and ");
				}
			}
		}

		query.append(" limit 250");

		Vector rv=new Vector();
		SpyCacheDB db=new SpyCacheDB(GeoConfig.getInstance());
		try {
			PreparedStatement pst=db.prepareStatement(query.toString(), 900);

			for(int i=0; i<keys.size(); i++) {
				String k=(String)keys.elementAt(i);
				pst.setString(i+1, spec.get(k).toString());
			}

			getLogger().debug("Query:  %s", query);

			ResultSet rs=pst.executeQuery();
			while(rs.next()) {
				rv.addElement(getZipData(rs));
			}
			rs.close();
			pst.close();
		} finally {
			CloseUtil.close(db);
		}

		return(rv);
	}

}
