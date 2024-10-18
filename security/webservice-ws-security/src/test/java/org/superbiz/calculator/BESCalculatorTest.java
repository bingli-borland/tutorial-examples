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
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class BESCalculatorTest extends TestCase {

    //START SNIPPET: setup

    //Random port to avoid test conflicts
    private static final int port = 8443;

    public void testCalculatorViaWsInterfaceWithEncryptSign() throws Exception {
        final Service calcService = Service.create(new URL("http://localhost:8080/CalculatorWsService/CalculatorImpl?wsdl"),
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

        // endpoint.getInInterceptors().add(new GZIPInInterceptor());
        // endpoint.getOutInterceptors().add(new GZIPOutInterceptor());

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

        // call the remote JAX-WS webservice
        assertEquals(10, calc.sum(4, 6));
        assertEquals(12, calc.multiply(3, 4));

        // 启用 MTOM
        ((BindingProvider) calc).getRequestContext().put("com.sun.xml.ws.mtom.enabled", true);

        // 发送文件
        DataHandler fileDataHandler = new DataHandler(new FileDataSource("C:\\managecat\\apache-tomee-plus-8.0.16\\RUNNING.txt"));
        DataHandler recive = calc.uploadFile(fileDataHandler);
        assertNotNull(recive.getDataSource());

        System.out.println(convert(recive.getInputStream()));
    }

    public static String convert(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString().trim();
    }

    public void testCalculatorViaWsInterfaceWithEncryptSignSSL() throws Exception {
        final Service calcService = Service.create(new URL("http://localhost:8080/CalculatorWsService/CalculatorImpl?wsdl"),
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

        // switch the target URL for invocation to HTTPS
        ((BindingProvider) calc).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://localhost:" + port + "/CalculatorWsService/CalculatorImpl");

        // add the SSL Client certificate, set the trust store and the hostname verifier
        setupTLS(calc);

        // call the remote JAX-WS webservice
        assertEquals(10, calc.sum(4, 6));
        assertEquals(12, calc.multiply(3, 4));

        // 发送文件
        DataHandler fileDataHandler = new DataHandler(new FileDataSource("C:\\managecat\\apache-tomee-plus-8.0.16\\RUNNING.txt"));
        DataHandler recive = calc.uploadFile(fileDataHandler);
        assertNotNull(recive.getDataSource());

        System.out.println(convert(recive.getInputStream()));

    }

    public static void setupTLS(final Object port) throws GeneralSecurityException, IOException {

        final HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(port).getConduit();

        final TLSClientParameters tlsCP = new TLSClientParameters();
        final String storePassword = "keystorePass";
        final String keyPassword = "clientPassword";
        final KeyStore keyStore = KeyStore.getInstance("jks");
        final String keyStoreLoc = "META-INF/clientStore.jks";
        keyStore.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(keyStoreLoc), storePassword.toCharArray());

        // set the key managers from the Java KeyStore we just loaded
        final KeyManager[] myKeyManagers = getKeyManagers(keyStore, keyPassword);
        tlsCP.setKeyManagers(myKeyManagers);
        tlsCP.setCertAlias("clientalias"); // in case there is multiple certs in the keystore, make sure we pick the one we want

        // Create a trust manager that does not validate certificate chains
        // this should not be done in production. It's recommended to create a cacerts with the certificate chain or
        // to rely on a well known CA such as Verisign which is already available in the JVM
        TrustManager[] trustAllCerts = getTrustManagers();
        tlsCP.setTrustManagers(trustAllCerts);

        // don't check the host name of the certificate to match the server (running locally)
        // this should not be done on a real production system
        tlsCP.setHostnameVerifier((s, sslSession) -> true);

        httpConduit.setTlsClientParameters(tlsCP);
    }

    private static TrustManager[] getTrustManagers() throws NoSuchAlgorithmException, KeyStoreException {
        return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
    }

    private static KeyManager[] getKeyManagers(KeyStore keyStore, String keyPassword) throws GeneralSecurityException, IOException {
        String alg = KeyManagerFactory.getDefaultAlgorithm();
        char[] keyPass = keyPassword != null ? keyPassword.toCharArray() : null;
        KeyManagerFactory fac = KeyManagerFactory.getInstance(alg);
        fac.init(keyStore, keyPass);
        return fac.getKeyManagers();
    }
    //END SNIPPET: webservice
}
