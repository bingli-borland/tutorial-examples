package javaeetutorial.cart.war1;

import javaeetutorial.cart.ejb.Cart;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestEJBServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            InitialContext initCtx = new InitialContext();
            Cart remote = (Cart) initCtx.lookup(req.getParameter("app") != null ? req.getParameter("app") : "java:global/ICBCPERBANKEAREx/cart-ejb/CartBean!javaeetutorial.cart.ejb.Cart");
            out.println("TestEJBServlet1 javaeetutorial.cart.ejb.Constants.TEST_EJB_CONSTANT1: " + remote.getConstant());
            out.println("<br>");
        } catch (Exception e) {
            out.println(e);
        }
    }
}