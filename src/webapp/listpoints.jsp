<%@ page import="net.spy.geo.*"%>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<html><head><title>Point List</title>
</head>
<body bgcolor="ffffff">

<h1>Point List</h1>

You may notice I make really crappy web pages.  If you think you can do
better, use the <a href="exportform.jsp">export form</a> and make your
own.  :)

<p>

<%
	String zip=request.getParameter("zip");
	String lon=request.getParameter("long");
	String lat=request.getParameter("lat");
	String max=request.getParameter("max");

	// If they didn't give us a point, chose life.  Chose Santa Clara.
	if(zip==null || zip.equals("")
		&& ( lon==null || lon.equals("")
			|| lat==null || lat.equals(""))) {
		zip="95051";
	}

	// Make sure they're not null.
	if(zip==null) zip="";
	if(lon==null) lon="";
	if(lat==null) lat="";
	if(max==null) max="";

	String center=null;

	if(zip.equals("")) {
		double lon_d=Double.parseDouble(lon);
		double lat_d=Double.parseDouble(lat);
		center="point " + new Point(lat_d, lon_d);
	} else {
		center="zip " + zip;
	}

	String url="http://bleu.west.spy.net/servlet/net.spy.geo.GeoDataServlet"
		+ "%3fzip=" + zip
		+ "%26long=" + lon
		+ "%26lat=" + lat
		+ "%26max=" + max;

	String inc="/servlet/net.spy.rss.RSSServlet?url="
		+ url + "&xsl="
		+ "/afs/spy.net/home/dustin/public_html/geo/pointdisplay.xsl";

	log("Including " + inc);
%>

<p>

Centered around <%= center %>
<% if(max.equals("")) { %>
.
<% } else { %>
, maximum distance:  <%= max %> miles.
<% } %>

<p>

	<jsp:include page="<%= inc %>"/>

<%@ include file="tail.jsp" %>

</body>
</html>
