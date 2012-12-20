<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="./css/style.css" />
<title>SWIM</title>
</head>
<body>
	<jsp:include page="/common/header.jsp"></jsp:include>

	<div id="pageContent">
		<%
			try {
				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.jnp.interfaces.NamingContextFactory");
				env.put(Context.PROVIDER_URL, "localhost:1099");
				InitialContext jndiContext = new InitialContext(env);
				Object ref = jndiContext.lookup("AccessManager/remote");
				AccessManagerRemote accessManager = (AccessManagerRemote) ref;
				accessManager.addFakeUsers();
				List<User> users = accessManager.getAllUsers();
				out.println("users on database:</br>");
				if (users != null) {
					out.println("<ul>");
					for (User u : users) {
						out.println("<li>" + u.getUsername() + "</li>");
					}
					out.println("</ul>");
				} else {
					out.println("there are no users in database </br>");
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		%>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>