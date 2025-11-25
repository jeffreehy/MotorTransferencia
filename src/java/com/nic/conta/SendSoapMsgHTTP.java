/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.conta;

import java.net.*;
import java.io.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import javax.lang.model.element.Element;

import sun.misc.BASE64Encoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendSoapMsgHTTP {

    //public final static String DEFAULT_SERVER = "http://65.167.25.97:50200/XISOAPAdapter/MessageServlet?channel=*:BS_PI_WEB_AS400_QA:CCH_PI_SIF_RMCA_SOAP_PAY2_01";
    //public final static String DEFAULT_SERVER = "http://65.167.25.97:50000/XISOAPAdapter/MessageServlet?channel=*:BS_PI_WEB_AS400:CCH_PI_SIF_RMCA_SOAP_PAY2_01";
    public final static String SOAP_ACTION = "urn:sap-com:document:sap:soap:functions:mc-style:ZWS_F9I4:ZcfF9i4Request";
    public final static String DEFAULT_USERNAME = "PIUSERCON";
    public final static String DEFAULT_PASSWORD = "Hondutel2010pi";

    public static InputStream EnviaXml() throws IOException {
        //public InputStream EnviaXml(String tipotransaccion, String lugargeo, String ncontrol, String tiposerv, String fecpago, String nservicio, String ctacontra, String valpago, String ntransaccion  ) throws IOException {
        String DEFAULT_SERVER = null;
        DEFAULT_SERVER = "http://saperpdevnic.adbancat.hn:8000/sap/bc/srt/rfc/sap/zws_f9i4/200/zws_f9i4/http";
        String soapMsg = null;

        //String fileName = args[0];
        String server = DEFAULT_SERVER;
        String userName = "TU_DEBITO";
        String password = "N!CdebitoB@N*01";
        String trama = "";
        String verboseStr = "";
        boolean verbose = true;
        System.out.println("URL Destino = " + DEFAULT_SERVER);

        soapMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:soap:functions:mc-style\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <urn:ZcfF9i4>\n"
                + "         <IAcnumExt>12345</IAcnumExt>\n"
                + "         <IAmount>10</IAmount>\n"
                + "         <IAmountCom>2</IAmountCom>\n"
                + "         <IBkkrs>2300</IBkkrs>\n"
                + "         <IOperacion>1</IOperacion>\n"
                + "         <IRemark>test1</IRemark>\n"
                + "         <!--Optional:-->\n"
                + "         <ItAdditionalFilters>\n"
                + "            <!--Zero or more repetitions:-->\n"
                + "            <item>\n"
                + "               <Fieldname>?</Fieldname>\n"
                + "               <Fieldvalue>?</Fieldvalue>\n"
                + "            </item>\n"
                + "         </ItAdditionalFilters>\n"
                + "      </urn:ZcfF9i4>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";

        System.out.println("Envía soapMsg = " + soapMsg);
        //in.close();

        //Get the HTTP connection setup start time
        Calendar cal_csu = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        String csu_starttime = sdf.format(cal_csu.getTime());
        long csu_startmillis = System.currentTimeMillis();

        //Create connection to the server
        URL u = new URL(server);
        URLConnection uc = u.openConnection();
        HttpURLConnection connection = (HttpURLConnection) uc;
        connection.setRequestProperty("Content-Type", "text/xml");

        //Set connection parms
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setUseCaches(false);
        
        
        //Set connection authorization
        String userInfo = userName + ":" + password;
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] userInfoBytes = userInfo.getBytes(); // I18n bug here!
        String authInfo = "Basic " + encoder.encode(userInfoBytes);
//        connection.setRequestProperty("Authorization", "");
        connection.setRequestProperty("SOAPAction", SOAP_ACTION);
//        connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");


        connection.setRequestProperty("Authorization", authInfo);

//        System.out.println("\nHTTP connection established.  Sending soap request...\n");
        OutputStream out = connection.getOutputStream();
        OutputStreamWriter wout = new OutputStreamWriter(out, "UTF-8");

        BufferedWriter bw = new BufferedWriter(wout);

        bw.write(soapMsg);
        bw.flush();
        InputStream in = null;
        try {
            in = connection.getInputStream();
            String eso = new Scanner(in).useDelimiter("\\A").next();
            System.out.println("Result XML = " + eso);
            in = new ByteArrayInputStream(eso.getBytes("UTF-8"));
            return in;
        } catch (ConnectException e) {

        }
        return in;
    }
//}

    public static void main(String[] args) {
        System.out.println("Inicia consumo ws");
        try {
            InputStream response =  EnviaXml();
            String eso = new Scanner(response).useDelimiter("\\A").next();
            System.out.println("Respuesta consumo ws : "+eso);
        } catch (IOException ex) {
            Logger.getLogger(SendSoapMsgHTTP.class.getName()).log(Level.SEVERE, null, ex);
        }
                
                
        
    }

}
