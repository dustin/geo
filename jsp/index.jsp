<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<html><head><title>Dustin's Geocaching Portal</title></head>

<body bgcolor="#fFfFfF">

<h1>Dustin's Open Geocaching Database</h1>

<ul>
	<li><a href="loginform.jsp">Login</a></li>
	<li><a href="newpoint.jsp">Record a new point</a></li>
	<li><a href="searchform.jsp">Find the points</a></li>
	<li><a href="pointinfoform.jsp">Get info for a given point</a></li>
	<li><a href="terms.jsp">Terms of Use</a></li>
</ul>

<%@ include file="tail.jsp" %>

</body>
</html>
