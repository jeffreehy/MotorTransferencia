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
public class SendSoapF9I8 {

    //public final static String DEFAULT_SERVER = "http://65.167.25.97:50200/XISOAPAdapter/MessageServlet?channel=*:BS_PI_WEB_AS400_QA:CCH_PI_SIF_RMCA_SOAP_PAY2_01";
    //public final static String DEFAULT_SERVER = "http://65.167.25.97:50000/XISOAPAdapter/MessageServlet?channel=*:BS_PI_WEB_AS400:CCH_PI_SIF_RMCA_SOAP_PAY2_01";
    public final static String SOAP_ACTION = "WebServiceSTCaction/AWS_NIC_F9I8.Execute";
    public final static String DEFAULT_USERNAME = "PIUSERCON";
    public final static String DEFAULT_PASSWORD = "Hondutel2010pi";
//        SendSoapMsgHTTP envia = new SendSoapMsgHTTP();

    public InputStream EnviaXml(String areaBancaria, String documento) throws IOException {
        //public String EnviaXml(String areaBancaria, String documento) throws IOException {
        //public InputStream EnviaXml(String tipotransaccion, String lugargeo, String ncontrol, String tiposerv, String fecpago, String nservicio, String ctacontra, String valpago, String ntransaccion  ) throws IOException {
        String DEFAULT_SERVER = "";
        DEFAULT_SERVER = "http://10.128.15.48/WEBServiceSTC/ws_nic_f9i8.aspx";
//        DEFAULT_SERVER = "http://10.128.14.143/WEBServiceSTC/ws_nic_f9i8.aspx";
        String soapMsg = null;
//        int a = Integer.valueOf(areaBancaria) + 1 ;
//        a = Integer.valueOf(areaBancaria) - 1 ;
//        areaBancaria = String.valueOf(a+1);
//        int b = Integer.valueOf(documento) + 1 ;
//        b = Integer.valueOf(documento) - 1 ;
//        documento = String.valueOf(b+1);
    
        //String fileName = args[0];
        String server = DEFAULT_SERVER;
        String userName = DEFAULT_USERNAME;
        String password = DEFAULT_PASSWORD;
        String trama = "";
        String verboseStr = "";
        boolean verbose = true;
        System.out.println("URL Destino = " + DEFAULT_SERVER);
        soapMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"WebServiceSTC\">\n"
                + "   <soapenv:Header/>\n"
                + "   <soapenv:Body>\n"
                + "      <web:WS_NIC_F9I8.Execute>\n"
                + "         <web:Peticiongeneral>\n"
                + "            <web:Header>\n"
                + "               <web:Sistema>?</web:Sistema>\n"
                + "               <web:Canal>?</web:Canal>\n"
                + "               <web:Aplicacion>?</web:Aplicacion>\n"
                + "               <web:Usuario>?</web:Usuario>\n"
                + "               <web:Fecha>?</web:Fecha>\n"
                + "               <web:Hora>?</web:Hora>\n"
                + "               <web:Lenguaje>?</web:Lenguaje>\n"
                + "               <web:IP>?</web:IP>\n"
                + "               <web:Dispositivo>?</web:Dispositivo>\n"
                + "               <web:Transaccion>?</web:Transaccion>\n"
                + "               <web:Paso>?</web:Paso>\n"
                + "               <web:GeoReferencia>?</web:GeoReferencia>\n"
                + "               <web:Token>?</web:Token>\n"
                + "               <web:Referencia>?</web:Referencia>\n"
                + "            </web:Header>\n"
                + "            <web:AdicionalColeccion>\n"
                + "               <web:Item>\n"
                + "                  <web:Linea>?</web:Linea>\n"
                + "                  <web:Tipo>?</web:Tipo>\n"
                + "                  <web:Valor>?</web:Valor>\n"
                + "               </web:Item>\n"
                + "            </web:AdicionalColeccion>\n"
                + "         </web:Peticiongeneral>\n"
                + "         <web:Peticion>\n"
                + "            <web:AreaBancaria>" + areaBancaria + "</web:AreaBancaria>\n"
                + "            <web:Documento>" + documento + "</web:Documento>\n"
                + "            <web:Adicional>\n"
                + "               <web:Item>\n"
                + "                  <web:Linea>?</web:Linea>\n"
                + "                  <web:Tipo>?</web:Tipo>\n"
                + "                  <web:Valor>?</web:Valor>\n"
                + "               </web:Item>\n"
                + "            </web:Adicional>\n"
                + "         </web:Peticion>\n"
                + "      </web:WS_NIC_F9I8.Execute>\n"
                + "   </soapenv:Body>\n"
                + "</soapenv:Envelope>";
//        soapMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"WebServiceSTC\">\n"
//                + "   <soapenv:Header/>\n"
//                + "   <soapenv:Body>\n"
//                + "      <web:WS_NIC_F9I8.Execute>\n"
//                + "         <web:Peticiongeneral>\n"
//                + "            <web:Header>\n"
//                + "               <web:Sistema>?</web:Sistema>\n"
//                + "               <web:Canal>?</web:Canal>\n"
//                + "               <web:Aplicacion>?</web:Aplicacion>\n"
//                + "               <web:Usuario>?</web:Usuario>\n"
//                + "               <web:Fecha>?</web:Fecha>\n"
//                + "               <web:Hora>?</web:Hora>\n"
//                + "               <web:Lenguaje>?</web:Lenguaje>\n"
//                + "               <web:IP>?</web:IP>\n"
//                + "               <web:Dispositivo>?</web:Dispositivo>\n"
//                + "               <web:Transaccion>?</web:Transaccion>\n"
//                + "               <web:Paso>?</web:Paso>\n"
//                + "               <web:GeoReferencia>?</web:GeoReferencia>\n"
//                + "               <web:Token>?</web:Token>\n"
//                + "               <web:Referencia>?</web:Referencia>\n"
//                + "            </web:Header>\n"
//                + "            <web:AdicionalColeccion>\n"
//                + "               <!--Zero or more repetitions:-->\n"
//                + "               <web:Item>\n"
//                + "                  <web:Linea>?</web:Linea>\n"
//                + "                  <web:Tipo>?</web:Tipo>\n"
//                + "                  <web:Valor>?</web:Valor>\n"
//                + "               </web:Item>\n"
//                + "            </web:AdicionalColeccion>\n"
//                + "         </web:Peticiongeneral>\n"
//                + "         <web:Peticion>\n"
//                + "            <web:AreaBancaria>" + areaBancaria + "</web:AreaBancaria>\n"
//                //                + "            <web:Documento>000000025381</web:Documento>\n"
//                + "            <web:Documento>" + documento + "</web:Documento>\n"
//                + "            <web:Adicional>\n"
//                + "               <!--Zero or more repetitions:-->\n"
//                + "               <web:Item>\n"
//                + "                  <web:Linea>?</web:Linea>\n"
//                + "                  <web:Tipo>?</web:Tipo>\n"
//                + "                  <web:Valor>?</web:Valor>\n"
//                + "               </web:Item>\n"
//                + "            </web:Adicional>\n"
//                + "         </web:Peticion>\n"
//                + "      </web:WS_NIC_F9I8.Execute>\n"
//                + "   </soapenv:Body>\n"
//                + "</soapenv:Envelope>";

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
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setUseCaches(false);
        connection.setRequestProperty("SOAPAction", SOAP_ACTION);
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
//            String eso = new Scanner(in).useDelimiter("\\A").next();
//            System.out.println("Result XML = " + eso);
//            trama = new ByteArrayInputStream(eso.getBytes("UTF-8"));

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
//        SendSoapMsgHTTP envia = new SendSoapMsgHTTP();
//        String respuesta = envia.EnviaXml("2300", "000000025381");
//        System.out.println("Respuesta : "+respuesta);
//    }
}
