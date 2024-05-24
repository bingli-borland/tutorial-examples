<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>cart-war1-change</h2>

<%
javaeetutorial.cart.util.Constants.TEST_LIB_CONSTANT1 = request.getParameter("TEST_LIB_CONSTANT1");
javaeetutorial.cart.war1.Constants.TEST_WAR1_CONSTANT1 = request.getParameter("TEST_WAR1_CONSTANT1");
response.sendRedirect("index.jsp");
%>
</body>
</html>
