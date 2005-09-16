<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>
<!--
  Copyright (c) 2001  Dustin Sallings <dustin+html@spy.net>
  -->
<html>
	<head>
		<title>Create a New Account</title>
	</head>

	<body bgcolor="#fFfFfF">

	<form method="POST" action="register.jsp">
		<table border="1">
			<tr>
				<td>Username *</td>
				<td><input name="username"></td>
			</tr>
			<tr>
				<td>Password *</td>
				<td><input type="password" name="password"></td>
			</tr>
			<tr>
				<td>Password (again) *</td>
				<td><input type="password" name="password2"></td>
			</tr>
			<tr>
				<td>Full Name *</td>
				<td><input name="fullname"></td>
			</tr>
			<tr>
				<td>Email *</td>
				<td><input name="email"></td>
			</tr>
			<tr>
				<td>URL</td>
				<td><input name="url"></td>
			</tr>
			<tr>
				<td>Zip code</td>
				<td><input name="zipcode"></td>
			</tr>
			<tr>
				<td>Latitude</td>
				<td><input name="longitude"></td>
			</tr>
			<tr>
				<td>Longitude</td>
				<td><input name="latitude"></td>
			</tr>
		</table>

		<input type="submit" value="Save">
	</form>

	<%@ include file="tail.jsp" %>

	</body>
</html>
