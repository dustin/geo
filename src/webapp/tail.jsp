<p>
<hr width="50%" align="left">
<font size="-2">
<% if(geo.isAuthenticated()) { %>
Logged in as <%= geo.getUser().getFullName() %>
(<%= geo.getUser().getUsername() %>) <br>
<% } %>
<a href="<%= response.encodeURL("/~dustin/geo/") %>">[Home]</a> |
<a href="<%= response.encodeURL("searchform.jsp") %>">[Search]</a> |
<a href="<%= response.encodeURL("loginform.jsp") %>">[Login]</a>

<br>

Copyright &copy; 2001 Dustin Sallings

</font>
