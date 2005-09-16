<%@ page import="java.util.*" %>
<%@ page import="net.spy.*" %>
<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>
<jsp:setProperty name="geo" property="*"/>

<% geo.checkPassword(); %>

<%
	String returnUrl="index.jsp";
	String returnData=(String)session.getAttribute("geo_login_return");
	session.removeAttribute("geo_login_return");
	if(returnData!=null) {
		returnUrl=returnData;
	}
%>
<jsp:forward page="<%= returnUrl %>"/>
