<%@ page import="net.spy.geo.*" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="geo" scope="session" class="net.spy.geo.GeoBean"/>

<%
	String point_s=request.getParameter("point");
	if(point_s==null) {
		throw new Exception("Point not given.");
	}
	int point=Integer.parseInt(point_s);

	if(!geo.isAuthenticated()) {
		session.setAttribute("geo_login_return", "logform.jsp?point=" + point);
		%> <jsp:forward page="loginform.jsp"/> <%
	}

	LogEntry le=new LogEntry();
	le.setUserId(geo.getUser().getUserid());
	le.setFound(new Boolean(request.getParameter("found")).booleanValue());
	le.setInfo(request.getParameter("info"));
	le.setPointId(point);
	le.save();

	String returnUrl="showpoint.jsp?point=" + point;
%>

<jsp:forward page="<%= returnUrl %>"/>
