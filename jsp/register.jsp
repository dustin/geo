<%@ page import="java.util.*" %>
<%@ page import="net.spy.*" %>
<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<%

	String pass1=request.getParameter("password");
	String pass2=request.getParameter("password2");
	if(!pass1.equals(pass2)) {
		throw new Exception("Passwords don't match.");
	}

	GeoUser gu=new GeoUser();
	gu.setUsername(request.getParameter("username"));
	gu.setPassword(request.getParameter("password"));
	gu.setFullName(request.getParameter("fullname"));
	gu.setEmail(request.getParameter("email"));
	gu.setUrl(request.getParameter("url"));
	try {
		gu.setZipcode(Integer.parseInt(request.getParameter("zipcode")));
	} catch(Exception e) {
		// Ignore.
	}
	try {
		gu.setLongitude(Float.parseFloat(request.getParameter("longitude")));
		gu.setLatitude(Float.parseFloat(request.getParameter("latitude")));
	} catch(Exception e) {
		// Ignore.
	}

	gu.save();

	geo.setUsername(gu.getUsername());
	geo.setPass(request.getParameter("password"));
	geo.checkPassword(); // Get us authenticated
%>

<jsp:forward page="index.jsp"/>
