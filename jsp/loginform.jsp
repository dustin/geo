<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<html>
<head><title>Login</title></head>

<body bgcolor="#fFfFfF">

	<center>
	<form method="POST" action="login.jsp">
	<table>
	<tr><td>Login</td><td><input name="username"></td></tr>
	<tr><td>Password</td><td><input type="password" name="pass"></td></tr>
	<tr><td align="center" colspan="2">
		<input type="submit" value="Login">
		<input type="reset" name="Clear"></td></tr>
	</table>
	</form>

	<p>

	<a href="newuserform.jsp">Click here for an account.</a>

	</center>

	<%@ include file="tail.jsp" %>

</body>
</html>
