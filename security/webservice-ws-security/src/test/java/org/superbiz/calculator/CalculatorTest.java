/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.superbiz.calculator;

import junit.framework.TestCase;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CalculatorTest extends TestCase {

    //START SNIPPET: setup

    //Random port to avoid test conflicts
    private static final int port = Integer.parseInt(System.getProperty("httpejbd.port", "" + org.apache.openejb.util.NetworkUtil.getNextAvailablePort()));

    @Override
    protected void setUp() throws Exception {
        final Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");
        properties.setProperty("openejb.embedded.remotable", "true");

        //Just for this test we change the default port from 4204 to avoid conflicts
        properties.setProperty("httpejbd.port", "" + port);

        new InitialContext(properties);
    }
    //END SNIPPET: setup

    public void testCalculatorViaWsInterfaceWithEncryptSign() throws Exception {
        final Service calcService = Service.create(new URL("http://localhost:" + port + "/webservice-ws-security/CalculatorImplEncryptSign?wsdl"),
                new QName("http://superbiz.org/wsdl", "CalculatorWsService"));
        assertNotNull(calcService);

        // for debugging (ie. TCPMon)
        calcService.addPort(new QName("http://superbiz.org/wsdl",
                        "CalculatorWsService2"), SOAPBinding.SOAP12HTTP_BINDING,
                "http://127.0.0.1:8204/CalculatorImplEncryptSign");


        final CalculatorWs calc = calcService.getPort(CalculatorWs.class);

        final Client client = ClientProxy.getClient(calc);
        final Endpoint endpoint = client.getEndpoint();
        // endpoint.getInInterceptors().add(new SAAJInInterceptor());
        // endpoint.getOutInterceptors().add(new SAAJOutInterceptor());

        final Map<String, Object> inProps = new HashMap<>();
        inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT + " " + WSHandlerConstants.SIGNATURE);
        inProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CustomPasswordHandler());
        inProps.put(WSHandlerConstants.SIG_PROP_FILE, "META-INF/CalculatorImplSign-client.properties");
        inProps.put(WSHandlerConstants.SIG_KEY_ID, "IssuerSerial");
        inProps.put(WSHandlerConstants.DEC_PROP_FILE, "META-INF/CalculatorImplUsernameTokenPlainPasswordEncrypt-client.properties");

        final WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);
        endpoint.getInInterceptors().add(wssIn);

        final Map<String, Object> outProps = new HashMap<String, Object>();
        outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.ENCRYPT + " " + WSHandlerConstants.SIGNATURE);
        outProps.put(WSHandlerConstants.USER, "clientalias");
        outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new CustomPasswordHandler());
        outProps.put(WSHandlerConstants.SIG_PROP_FILE, "META-INF/CalculatorImplSign-client.properties");
        outProps.put(WSHandlerConstants.SIG_KEY_ID, "IssuerSerial");
        outProps.put(WSHandlerConstants.ENC_PROP_FILE, "META-INF/CalculatorImplUsernameTokenPlainPasswordEncrypt-client.properties");
        outProps.put(WSHandlerConstants.ENCRYPTION_USER, "serveralias");

        final WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
        endpoint.getOutInterceptors().add(wssOut);

        assertEquals(10, calc.sum(4, 6));
    }
    //END SNIPPET: webservice
}
