<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<!--
  Copyright (c) 2001  Dustin Sallings <dustin+html@spy.net>
  -->
<html>
	<head>
		<title>Make Me An Export!</title>
	</head>

	<body bgcolor="#fFfFfF">

	<form method="GET" action="/servlet/net.spy.geo.GeoDataServlet">
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
							<td>Lat:</td>
							<td align="right"><input size="8" name="lat"></td>
						</tr>
						<tr>
							<td>Lon:</td>
							<td align="right"><input size="8" name="long"></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>Max distance</td>
				<td><input size="5" name="max">mi</td>
			</tr>
			<tr>
				<td>Format</td>
				<td>
					<select name="format">
						<option value="tab">Tab delimited</option>
						<option selected value="xml">XML</option>
					</select>
				</td>
			</tr>
		</table>

		<input type="submit" value="Export">
	</form>

	<%@ include file="tail.jsp" %>

	</body>
</html>
