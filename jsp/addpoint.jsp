<%@ page import="java.util.*" %>
<%@ page import="net.spy.*" %>
<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<%
	if(!geo.isAuthenticated()) {
		session.setAttribute("geo_login_return", "newpoint.jsp");
		%> <jsp:forward page="loginform.jsp"/> <%
	}

	String name=request.getParameter("name");
	String long_sign=request.getParameter("longsign");
	float lon=Float.parseFloat(request.getParameter("longitude"));
	float lon_min=Float.parseFloat(request.getParameter("longitude_min"));
	if(long_sign!=null && long_sign.equals("-")) {
		lon=0-lon;
	}

	String lat_sign=request.getParameter("latsign");
	float lat=Float.parseFloat(request.getParameter("latitude"));
	float lat_min=Float.parseFloat(request.getParameter("latitude_min"));
	if(lat_sign!=null && lat_sign.equals("-")) {
		lat=0-lat;
	}

	CachePoint cp=new CachePoint(name, new Point(lat, lat_min, lon, lon_min));
	cp.setDifficulty(Float.parseFloat(request.getParameter("difficulty")));
	cp.setTerrain(Float.parseFloat(request.getParameter("terrain")));
	cp.setDescription(request.getParameter("description"));
	cp.setCreatorId(geo.getUser().getUserid());
	cp.setCountry(Integer.parseInt(request.getParameter("country")));
	cp.save();
%>

<jsp:forward page="index.jsp"/>
