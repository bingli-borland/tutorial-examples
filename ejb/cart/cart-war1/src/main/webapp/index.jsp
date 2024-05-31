<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>cart-war1</h2>

   <form action="change.jsp" method="post">
         javaeetutorial.cart.util.Constants.TEST_LIB_CONSTANT1<input type="text" name="TEST_LIB_CONSTANT1" value="<%= javaeetutorial.cart.util.Constants.TEST_LIB_CONSTANT1 %>"/><br>
         javaeetutorial.cart.war1.Constants.TEST_WAR1_CONSTANT1<input type="text" name="TEST_WAR1_CONSTANT1" value="<%= javaeetutorial.cart.war1.Constants.TEST_WAR1_CONSTANT1 %>"/><br>
         javaeetutorial.cart.ejb.Constants.TEST_EJB_CONSTANT1<input type="text" name="TEST_EJB_CONSTANT1" value="<%= javaeetutorial.cart.ejb.Constants.TEST_EJB_CONSTANT1 %>"/><br>
         <input type="submit" value="修改"/>
   </form>
</body>
</html>
