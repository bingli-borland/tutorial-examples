<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>cart-war1-servletcontext</h2>

<%
    ServletContext context = getServletContext();
    ServletContext context2 = context.getContext("/cart-war1");
    ServletContext context3 = context.getContext("/cart-war2");
%>
<h2>cart-war1 ServletContext.getContext("/cart-war1"): <%= context2 %></h2>
<h2>cart-war1 ServletContext.getContext("/cart-war2"): <%= context3 %></h2>
</body>
</html>
