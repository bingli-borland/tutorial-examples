package com.bes.ejb.test.apitest;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet({ "/TestEJB" })
public class TestEJB extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        Context nctx = null;
        TimerBeanHome beanHome = null;
        TimerBean beanRef = null;
        try {
            nctx = new InitialContext();
            beanHome = (TimerBeanHome) nctx.lookup("java:global/timertest/TimerBeanEJB!com.bes.ejb.test.apitest.TimerBeanHome");
            beanRef = beanHome.create();
            boolean pass = beanRef.test2();
            System.out.println(pass);
            out.println(pass);
            Thread.sleep(20000);
            beanRef.findAndCancelTimer();
        } catch (Exception e) {
            e.printStackTrace();
            out.println(false);
            out.println(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        doGet(request, response);
    }
}