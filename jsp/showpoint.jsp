<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="net.spy.*" %>
<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<%
	String point_s=request.getParameter("point");
	if(point_s==null) {
		throw new Exception("Point parameter not given.");
	}
	int pointid=Integer.parseInt(point_s);

	CachePoint cp=new CachePoint(pointid);
	GeoUser creator=new GeoUser(cp.getCreatorId());
	NumberFormat nf=NumberFormat.getInstance();
	nf.setMaximumFractionDigits(4);
 %>

<html><head><title>Displaying point <%= cp.getName() %></title></head>

<body bgcolor="#fFfFfF">

<h1>Info for <%= cp.getName() %></h1>

<b>Location:</b>
			<%= Math.abs(cp.getLatDegrees()) %>&deg;
			<%= nf.format(cp.getLatMinutes()) %>'
			<%= cp.getLatHemisphere() %>
			<%= Math.abs(cp.getLongDegrees()) %>&deg;
			<%= nf.format(cp.getLongMinutes()) %>'
			<%= cp.getLongHemisphere() %> <br>
<b>Waypoint ID:</b> <%= cp.getWaypointId() %><br>

<b>Country:</b> <%= cp.getCountry() %><br>

<b>Difficulty:</b> <%= cp.getDifficulty() %><br>
<b>Terrain:</b> <%= cp.getTerrain() %><br>
<b>Created:</b> <%= cp.getDateCreated() %><br>

<b>Description:</b><br>
<blockquote>
	<%= cp.getDescription() %>
</blockquote>

<%
	String map="http://tiger.census.gov/cgi-bin/mapgen?"
		+ "lat=" + nf.format(cp.getLatitude())
		+ "&lon=" + nf.format(cp.getLongitude())
		+ "&iwd=300&iht=250&wid=.05&ht=.05&mark="
		+ nf.format(cp.getLongitude()) + "," + nf.format(cp.getLatitude())
		+ ",redstar";
%>

<b>Map:</b>

<center>
	<img src="<%= map %>" border="0">
</center>
<br>

Added by <%= creator.getFullName() %>.

<h2>Log Entries</h2>

<% if(geo.isAuthenticated()) { %>
	<a href="logform.jsp?point=<%= pointid %>">Add a log entry</a>
<% } %>

<ul>
<%
	for(Enumeration e=LogEntry.getEntriesForPoint(pointid);
		e.hasMoreElements(); ) {

		LogEntry le=(LogEntry)e.nextElement();
		GeoUser logger=new GeoUser(le.getUserId());

		String find_s=null;
		if(le.getFound()) {
			find_s="yep";
		} else {
			find_s="nope";
		}

		%>
			<li>
				<b>Log from:</b><%= logger.getFullName() %><br>
				<b>Found?:</b><%= find_s %><br>
				<b>Logged:</b><%= le.getTimestamp() %><br>
				<blockquote>
					<%= le.getInfo() %>
				</blockquote>
			</li>
		<%
	}
%>
</ul>

<%@ include file="tail.jsp" %>

</body>
</html>
