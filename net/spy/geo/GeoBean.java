// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoBean.java,v 1.1 2001/06/13 03:45:29 dustin Exp $

package net.spy.geo;

/**
 * The bean.
 */
public class GeoBean extends Object implements java.io.Serializable {

	private String username=null;
	private String pass=null;

	private GeoUser user=null;

	/**
	 * Get an instance of GeoBean.
	 */
	public GeoBean() {
		super();
	}

	/**
	 * Set the username field from the form.
	 */
	public void setUsername(String to) {
		username=to;
	}

	/**
	 * Set the password field from the form.
	 */
	public void setPass(String to) {
		pass=to;
	}

	/**
	 * Check the password.
	 */
	public void checkPassword() throws Exception {
		if(username==null || pass==null) {
			throw new Exception("You didn't fill out all the fields.");
		}
		user=new GeoUser(username);
		user.checkPassword(pass);
	}

	/**
	 * Get the user object.
	 */
	public GeoUser getUser() {
		return(user);
	}
}
