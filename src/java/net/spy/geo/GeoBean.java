// Copyright (c) 2001  Dustin Sallings <dustin@spy.net>
//
// $Id: GeoBean.java,v 1.2 2001/06/13 09:51:46 dustin Exp $

package net.spy.geo;

/**
 * The bean.
 */
public class GeoBean extends Object implements java.io.Serializable {

	private String username=null;
	private String pass=null;

	private GeoUser user=null;
	private boolean authenticated=false;

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
	 * Check the user's password.
	 */
	public void checkPassword() throws Exception {
		if(username==null || pass==null) {
			throw new Exception("You didn't fill out all the fields.");
		}
		user=new GeoUser(username);
		user.checkPassword(pass);
		authenticated=true;
	}

	/**
	 * Have we been authenticated?
	 */
	public boolean isAuthenticated() throws Exception {
		if(!authenticated) {
			try {
				checkPassword();
			} catch(Exception e) {
				System.err.println("Error verifying " + username);
			}
		}

		return(authenticated);
	}

	/**
	 * Get the user object.
	 */
	public GeoUser getUser() {
		return(user);
	}
}
