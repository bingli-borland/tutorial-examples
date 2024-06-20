package javaeetutorial.cart.war2;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jndiName = req.getParameter("jndiName") == null ? "java:comp/env/jdbc/jdbcMysqlRef" : req.getParameter("jndiName");
        resp.getWriter().write("hello cart servlet2");
        try {
            Context context = new InitialContext();
            resp.getWriter().write( jndiName + " = " + context.lookup(jndiName));
        } catch (Exception e) {
            e.printStackTrace(resp.getWriter());
        }
    }
}