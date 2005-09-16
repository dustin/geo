<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="net.spy.*" %>
<%@ page import="net.spy.geo.*" %>
<%-- <%@ page errorPage="error.jsp" %> --%>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<%
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

	Point p=new Point(lat, lat_min, lon, lon_min);
	Enumeration polys=Polygon.getAreasForPoint(p);

	NumberFormat nf=NumberFormat.getInstance();
	nf.setMaximumFractionDigits(4);
 %>

<html><head><title>Info for <%= p %></title></head>

<body bgcolor="#fFfFfF">

<h1>Info for <%= p %></h1>

<% if(!polys.hasMoreElements()) { %>
	There is no matching polygon for this point.
<% } else { %>

<%
	for(; polys.hasMoreElements(); ) {
		DBPolygon poly=(DBPolygon)polys.nextElement();
		Point center=poly.getCenter();

// http://tiger.census.gov/cgi-bin/mapsurfer?infact=2&outfact=2&act=move&on=counties&on=miscell&on=shorelin&on=states&on=water&tlevel=-&tvar=-&tmeth=i&mlat=36.99894&mlon=-109.04461&msym=grnpin&mlabel=Your+Spot&murl=&lat=36.99894&lon=-109.04461&wid=6.000&ht=6.000&conf=palette2.con

		String map="http://tiger.census.gov/cgi-bin/mapgen?"
			+ "lat=" + nf.format(center.getLatitude())
			+ "&lon=" + nf.format(center.getLongitude())
			+ "&iwd=640&iht=480"
			+ "&wid=" + nf.format(poly.getWidth()+.1)
			+ "&ht=" + nf.format(poly.getHeight()+.1)
			+ "&msym=grnpin"
			+ "&mlon=" + nf.format(p.getLongitude())
			+ "&mlat=" + nf.format(p.getLatitude())
			+ "&mlabel=" + java.net.URLEncoder.encode(p.toString())
			+ "&conf=palette2.con"
			+ "&on=GRID&on=railroad&on=indian&on=shorelin&on=states&on=water"
			;

		String url="http://www.mapblast.com/myblast/map.mb?"
			+ "CMD=LFILL"
			+ "&CT=" + nf.format(p.getLatitude())
			+ "%3A" + nf.format(p.getLongitude()) + "%3A100000"
			+ "&IC=" + nf.format(p.getLatitude())
			+ "%3A" + nf.format(p.getLongitude()) + "%3A8%3A"
			+ "&AD4=USA"
			+ "&W=456&H=259"
			+ "&lv=3&serch=adv"
			+ "&MAP.x=236&MAP.y=126&MA=1";
%>

<b>Map of <%= poly.getName() %></b>

<center>
	<a href="<%= url %>"><img
	width="640" height="480" src="<%= map %>" border="0"></a>
</center>

<%
		} // poly loop
	}     // have polys
%>

<h2>Nearest Known Waypoint</h2>

<%
	CachePointList cpl=new CachePointList();
	Enumeration e=cpl.getPoints(p);
	CachePoint cp=null;
	if(e.hasMoreElements()) {
		cp=(CachePoint)e.nextElement();
	}
	while(e.hasMoreElements()) { e.nextElement(); }
%>

<% if(cp==null) { %>
	No known waypoint nearby.
<% } else { %>
	<% GeoVector gv=p.diff(cp); %>
	Nearest known waypoint is
	<a href="showpoint.jsp?point=<%= cp.getPointId() %>"><%=
		cp.getName() %></a>,
	which is <%= nf.format(gv.getDistance()) %> miles
	<%= gv.getDirection() %> of the point of interest.
<% } %>

<%@ include file="tail.jsp" %>

</body>
</html>
