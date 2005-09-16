<%@ page isErrorPage="true"%>
<html>
<head><title>Error!</title></head>

<body bgcolor="#fFfFfF">

<!--
	<%= exception %>
-->

<% if(exception.getMessage().startsWith("Auth Failure")) { %>

	<center>
	<form method="POST" action="login.jsp">
	<table>
	<tr><td>Login</td><td><input name="username"></td></tr>
	<tr><td>Password</td><td><input type="password" name="password"></td></tr>
	<tr><td align="center" colspan="2">
		<input type="submit" value="Login">
		<input type="reset" name="Clear"></td></tr>
	</table>
	</form>

	<p>
	<font color="red">
	<%= exception.getMessage().substring(13) %>
	</font>
	</center>

<% } else { %>

	Whoops!  Had an error processing your request:
	<pre>
		<%= exception.getMessage() %>
	</pre>

<% } %>

</body>
</html>
