<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<!--
  Copyright (c) 2001  Dustin Sallings <dustin+html@spy.net>
  -->
<html>
	<head>
		<title>Find Points</title>
	</head>

	<body bgcolor="#fFfFfF">

	<form method="POST" action="listpoints.jsp">
		(All of these fields are optional)<br>
		<table border="1">
			<tr>
				<td>Zipcode</td>
				<td><input size="5" name="zip"></td>
			</tr>
			<tr>
				<td colspan="2">
					Origin point (decimal)<br>
					<table width="100%">
						<tr>
							<td valign="top">Lat:</td>
							<td align="right"><input size="8" name="lat"><br>
							(South is negative)
							</td>
						</tr>
						<tr>
							<td valign="top">Lon:</td>
							<td align="right"><input size="8" name="long"><br>
							(West is negative)
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>Max distance</td>
				<td><input size="5" name="max">mi</td>
			</tr>
		</table>

		<input type="submit" value="List">
	</form>

<%@ include file="tail.jsp" %>

	</body>
</html>
