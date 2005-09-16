<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<!--
  Copyright (c) 2001  Dustin Sallings <dustin+html@spy.net>
  -->
<html>
	<head>
		<title>Get Info About a Point</title>
	</head>

	<body bgcolor="#fFfFfF">

	<h1>Get Info About a Point</h1>

	<form method="POST" action="pointinfo.jsp">
		<table border="1">
			<tr>
				<td>Latitude</td>
				<td>
					<select name="latsign">
						<option value="+">N</option>
						<option value="-">S</option> </select>
					<input size="3" value="37" name="latitude">&deg;
					<input size="6" value="0" name="latitude_min">'
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
					<input size="3" value="121" name="longitude">&deg;
					<input size="6" value="0" name="longitude_min">'
					(WGS-84)
				</td>
			</tr>
		</table>

		<br>
		<input type="submit" value="Look up">
	</form>

<%@ include file="tail.jsp" %>

	</body>
</html>
