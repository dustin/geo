<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<html><head><title>Add a Log Entry</title></head>

<body bgcolor="#fFfFfF">

<h1>Add a Log Entry</h1>

<form method="POST" action="addlog.jsp">
	<input type="hidden" name="point"
		value="<%= request.getParameter("point") %>">

	<select name="found">
		<option value="true">Found</option>
		<option value="false">Didn't find</option>
	</select>
	<br>

	<textarea cols="60" rows="5" name="info"></textarea>
	<br>
	<input type="submit" value="Log">
</form>

<%@ include file="tail.jsp" %>

</body>
</html>
