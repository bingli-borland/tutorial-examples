package javaeetutorial.cart.war2;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("TestFilter的init方法被调用");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println(request.getServletContext().getContextPath() + ", javaeetutorial.cart.war2.TestFilter");
        ((HttpServletResponse)response).addHeader("TestHeader", request.getServletContext().getContextPath() + ", javaeetutorial.cart.war2.TestFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("TestFilter的destroy方法被调用");
    }
}
