// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoConfig.java,v 1.1 2001/06/12 07:52:51 dustin Exp $

package net.spy.geo;

import java.io.File;
import net.spy.SpyConfig;

public class GeoConfig extends SpyConfig {

	private static File configs[]={
		new File("/afs/spy.net/misc/web/etc/geo.conf"),
		new File("/afs/spy.net/misc/web/etc/misc.conf"),
		new File("geo.conf")
		};

	/**
	 * Get a new GeoConfig.
	 */
	public GeoConfig() {
		super();
		loadConfig(configs);
	}

}
