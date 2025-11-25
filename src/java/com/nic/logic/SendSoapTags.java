/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

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

/**
 *
 * This is a simple text mode application that reads a properly formed SOAP
 * message (including the envelope) from an input file and sends it to the
 * specified URL. Authorization information can be provided from the command
 * line.
 *
 * Basic performance metrics are provided to measure elapsed time to send and
 * receive the SOAP messages.
 *
 * @author <a href="mailto:mcarlson@conneva.com">Mark Carlson</a>
 */
public class SendSoapTags {

    //public final static String DEFAULT_SERVER = "http://65.167.25.97:50200/XISOAPAdapter/MessageServlet?channel=*:BS_PI_WEB_AS400_QA:CCH_PI_SIF_RMCA_SOAP_PAY2_01";
    //public final static String DEFAULT_SERVER = "http://65.167.25.97:50000/XISOAPAdapter/MessageServlet?channel=*:BS_PI_WEB_AS400:CCH_PI_SIF_RMCA_SOAP_PAY2_01";
    public final static String SOAP_ACTION = "";
    public final static String DEFAULT_USERNAME = "PIUSERCON";
    public final static String DEFAULT_PASSWORD = "Hondutel2010pi";
//        SendSoapMsgHTTP envia = new SendSoapMsgHTTP();

    public InputStream EnviaXml(String transactionId, String documentoValor , String documentoComision) throws IOException {
        //public String EnviaXml(String areaBancaria, String documento) throws IOException {
        //public InputStream EnviaXml(String tipotransaccion, String lugargeo, String ncontrol, String tiposerv, String fecpago, String nservicio, String ctacontra, String valpago, String ntransaccion  ) throws IOException {
        String DEFAULT_SERVER = "";
        DEFAULT_SERVER = "http://10.128.14.143/MotorTransferencia/wsinserttransactiontags.aspx";
//        DEFAULT_SERVER = "http://10.128.14.143/WEBServiceSTC/ws_nic_f9i8.aspx";
        String soapMsg = null;

        //String fileName = args[0];
        String server = DEFAULT_SERVER;
        String userName = DEFAULT_USERNAME;
        String password = DEFAULT_PASSWORD;
        String trama = "";
        String verboseStr = "";
        boolean verbose = true;
        System.out.println("URL Destino = " + DEFAULT_SERVER);
        soapMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:mot=\"MotorTransferencia\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <mot:WsInsertTransactionTags.Execute>\n"
                + "         <mot:Listatags>\n"
                + "            <mot:SDTTransaccionTag>\n"
                + "               <mot:TransaccionSecuencia>"+transactionId+"</mot:TransaccionSecuencia>\n"
                + "               <mot:TransaccionTagLinea>0</mot:TransaccionTagLinea>\n"
                + "               <mot:MensajeCodigo>MT103</mot:MensajeCodigo>\n"
                + "               <mot:TagCodigo>REFCONBCATRN</mot:TagCodigo>\n"
                + "               <mot:TransaccionTagNoTransaccion>1</mot:TransaccionTagNoTransaccion>\n"
                + "               <mot:TransaccionTagValor>"+documentoValor+"</mot:TransaccionTagValor>\n"
                + "               <mot:TransaccionTagEstado>TIPESTACT</mot:TransaccionTagEstado>\n"
                + "            </mot:SDTTransaccionTag>\n"
                + "            <mot:SDTTransaccionTag>\n"
                + "               <mot:TransaccionSecuencia>"+transactionId+"</mot:TransaccionSecuencia>\n"
                + "               <mot:TransaccionTagLinea>0</mot:TransaccionTagLinea>\n"
                + "               <mot:MensajeCodigo>MT103</mot:MensajeCodigo>\n"
                + "               <mot:TagCodigo>REFCONBCACOM</mot:TagCodigo>\n"
                + "               <mot:TransaccionTagNoTransaccion>1</mot:TransaccionTagNoTransaccion>\n"
                + "               <mot:TransaccionTagValor>"+documentoComision+"</mot:TransaccionTagValor>\n"
                + "               <mot:TransaccionTagEstado>TIPESTACT</mot:TransaccionTagEstado>\n"
                + "            </mot:SDTTransaccionTag>\n"
                + "         </mot:Listatags>\n"
                + "      </mot:WsInsertTransactionTags.Execute>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope";

//        System.out.println("Envía soapMsg = " + soapMsg);
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
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setUseCaches(false);
        //Set connection authorization
        String userInfo = userName + ":" + password;
        BASE64Encoder encoder = new BASE64Encoder();
        byte[] userInfoBytes = userInfo.getBytes(); // I18n bug here!
        String authInfo = "Basic " + encoder.encode(userInfoBytes);
        //connection.setRequestProperty("Authorization", "UElVU0VSQ09OOkhvbmR1dGVsMjAxMHBp");
//        connection.setRequestProperty("Authorization", authInfo);

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

//            System.out.println(" in = "+in);
//            String respuesta = "";
//            int c;
//            char car;
//
//            while ((c = in.read()) != -1) {
//            car = (char) c;
//            respuesta += car;
//            }
            //   } //catch exceptions
            //     catch (IOException e) {
            //        System.err.println(e);
            //    }
            return in;
        } catch (ConnectException e) {

        }
        return null;
    }

//    public static void main(String[] args) throws IOException {
//        SendSoapTags envia = new SendSoapTags();
//        String respuesta = envia.EnviaXml("2300", "000000025381");
//        System.out.println("Respuesta : "+respuesta);
//    }
}
