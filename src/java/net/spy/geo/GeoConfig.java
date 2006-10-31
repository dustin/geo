// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoConfig.java,v 1.1 2001/06/12 07:52:51 dustin Exp $

package net.spy.geo;

import net.spy.util.SpyConfig;

public class GeoConfig extends SpyConfig {
	
	private static GeoConfig instance=new GeoConfig();

	/**
	 * Get a new GeoConfig.
	 */
	private GeoConfig() {
		super();
        put("dbConnectionSource", "net.spy.db.JNDIConnectionSource");
        put("dbSource", "java:/geodb");
	}

	public static GeoConfig getInstance() {
		if(instance == null) {
			instance=new GeoConfig();
		}
		return(instance);
	}

}
