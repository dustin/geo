<%@ page import="java.util.*" %>
<%@ page import="net.spy.*" %>
<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<% if(!geo.isAuthenticated()) {
	session.setAttribute("geo_login_return", "newpoint.jsp");
%>
	<jsp:forward page="loginform.jsp"/>
<% } %>

<!--
  Copyright (c) 2001  Dustin Sallings <dustin+html@spy.net>
  -->
<html>
	<head>
		<title>Add a Point</title>
	</head>

	<body bgcolor="#fFfFfF">

	<form method="POST" action="addpoint.jsp">
		<table border="1">
			<tr>
				<td>Point Name</td>
				<td><input name="name"></td>
			</tr>
			<tr>
				<td>Latitude</td>
				<td>
					<select name="latsign">
						<option value="+">N</option>
						<option value="-">S</option> </select>
					<input size="3" name="latitude">&deg;
					<input size="6" name="latitude_min">'
					(WGS-84)
				</td>
			</tr>
			<tr>
				<td>Longitude</td>
				<td>
					<select name="longsign">
						<option value="+">E</option>
						<option selected value="-">W</option>
					</select>
					<input size="3" name="longitude">&deg;
					<input size="6" name="longitude_min">'
					(WGS-84)
				</td>
			</tr>
			<tr>
				<td>Country</td>
				<td>
					<select name="country">
				<%
					for(Enumeration e=Country.listCountries();
						e.hasMoreElements(); ) {
						Country c=(Country)e.nextElement();

						String selected="";

						if(c.getAbbr().equals("US")) {
							selected="selected";
						}

						%>
							<option <%= selected %>
								value="<%= c.getId() %>"><%=
								c.getName() %></option>
						<%
					}
				%>
					</select>
				</td>
			</tr>
			<tr>
				<td>Difficulty</td>
				<td>
				<select name="difficulty">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				</select>
				</td>
			</tr>
			<tr>
				<td>Terrain</td>
				<td>
				<select name="terrain">
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
				</select>
				</td>
			</tr>
		</table>

		<textarea name="description" rows="5" cols="40" wrap="hard"></textarea>

		<br>
		<input type="submit" value="Save">
	</form>

<%@ include file="tail.jsp" %>

	</body>
</html>
