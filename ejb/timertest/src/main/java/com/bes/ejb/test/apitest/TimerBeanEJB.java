/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * @(#)TimerBeanEJB.java	1.3 03/05/16
 */

package com.bes.ejb.test.apitest;


import java.util.*;
import java.util.logging.Logger;
import javax.ejb.*;

public class TimerBeanEJB implements SessionBean, TimedObject {
  
  private Logger logger = Logger.getLogger(TimerBean.class.getName());
  private SessionContext sctx = null;


  public void ejbCreate() throws CreateException {
  }

  public void setSessionContext(SessionContext sc) {
    System.out.println("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

  public void ejbTimeout(javax.ejb.Timer timer) {
    logger.info("Timer execute: " + timer.getInfo());
  }

  /*
   * ====================== Test Methods ======================
   */

  public boolean test2() {
    System.out.println("test2");
    boolean pass = false;
    long initialDuration = 1000;
    long intervalDuration = 5000;
    String info = "test2";
    try {
      TimerService ts = sctx.getTimerService();
      System.out.println("Create Timer");
      javax.ejb.Timer t = ts.createTimer(initialDuration, intervalDuration,
          info);
      logger.warning("No IllegalArgumentException occurred - expected");
      pass = true;
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
      logger.warning("IllegalArgumentException occurred - expected");
    } catch (Exception e) {
      e.printStackTrace();
      logger.warning("Unexpected exception: " + e.getMessage());
    }
    return pass;
  }


  /*
   * ====================== Miscellaneous Methods ======================
   */

  public void findAndCancelTimer() {
    try {
      System.out.println("findTimer method entered");
      TimerService ts = sctx.getTimerService();
      System.out.println("find Timers");
      Collection ccol = ts.getTimers();
      Iterator i = ccol.iterator();
      while (i.hasNext()) {
        javax.ejb.Timer t = (javax.ejb.Timer) i.next();
        t.cancel();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new EJBException("findTimer:" + e);
    }
  }

}
