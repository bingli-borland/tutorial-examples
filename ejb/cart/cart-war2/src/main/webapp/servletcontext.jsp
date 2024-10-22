<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>cart-war2-servletcontext</h2>

<%
    ServletContext context = getServletContext();
    ServletContext context2 = context.getContext("/cart-war2");
    ServletContext context3 = context.getContext("/cart-war1");
%>
<h2>cart-war2 ServletContext.getContext("/cart-war2"): <%= context2 %></h2>
<h2>cart-war2 ServletContext.getContext("/cart-war1"): <%= context3 %></h2>
</body>
</html>
