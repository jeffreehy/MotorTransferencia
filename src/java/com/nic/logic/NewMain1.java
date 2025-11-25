/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import javax.swing.text.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author oargueta
 */
public class NewMain1 {

    public  InputStream someMethod(String areaBancaria, String documento) throws MalformedURLException, IOException {
//        String areaBancaria = "2300";
//        String documento  = "000000100705";
//Code to make a webservice HTTP request
        String responseString = "";
        String outputString = "";
        String wsURL = "http://10.128.15.48/WEBServiceSTC/ws_nic_f9i8.aspx";
        URL url = new URL(wsURL);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        System.out.println("**************************  ENTRO A NEW MAIN1 *************************" );
        String soapMsg = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"WebServiceSTC\">"
                + "   <soapenv:Header/>"
                + "   <soapenv:Body>"
                + "      <web:WS_NIC_F9I8.Execute>"
                + "         <web:Peticiongeneral>"
                + "            <web:Header>"
                + "               <web:Sistema>?</web:Sistema>"
                + "               <web:Canal>?</web:Canal>"
                + "               <web:Aplicacion>?</web:Aplicacion>"
                + "               <web:Usuario>?</web:Usuario>"
                + "               <web:Fecha>?</web:Fecha>"
                + "               <web:Hora>?</web:Hora>"
                + "               <web:Lenguaje>?</web:Lenguaje>"
                + "               <web:IP>?</web:IP>"
                + "               <web:Dispositivo>?</web:Dispositivo>"
                + "               <web:Transaccion>?</web:Transaccion>"
                + "               <web:Paso>?</web:Paso>"
                + "               <web:GeoReferencia>?</web:GeoReferencia>"
                + "               <web:Token>?</web:Token>"
                + "               <web:Referencia>?</web:Referencia>"
                + "            </web:Header>"
                + "            <web:AdicionalColeccion>"
                + "               <web:Item>"
                + "                  <web:Linea>?</web:Linea>"
                + "                  <web:Tipo>?</web:Tipo>"
                + "                  <web:Valor>?</web:Valor>"
                + "               </web:Item>"
                + "            </web:AdicionalColeccion>"
                + "         </web:Peticiongeneral>"
                + "         <web:Peticion>"
                + "            <web:AreaBancaria>" + areaBancaria + "</web:AreaBancaria>"
                + "            <web:Documento>" + documento + "</web:Documento>"
                + "            <web:Adicional>"
                + "               <web:Item>"
                + "                  <web:Linea>?</web:Linea>"
                + "                  <web:Tipo>?</web:Tipo>"
                + "                  <web:Valor>?</web:Valor>"
                + "               </web:Item>"
                + "            </web:Adicional>"
                + "         </web:Peticion>"
                + "      </web:WS_NIC_F9I8.Execute>"
                + "   </soapenv:Body>"
                + "</soapenv:Envelope>";
        System.out.println("**************************  soapMsg ************************* "+soapMsg );
        byte[] buffer = new byte[soapMsg.length()];
        buffer = soapMsg.getBytes();
        bout.write(buffer);
        byte[] b = bout.toByteArray();
        String SOAPAction = "WebServiceSTCaction/AWS_NIC_F9I8.Execute";
                
// Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length",
                String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//        httpConn.setRequestProperty("SOAPAction", SOAPAction);  //User-Agent: Apache-HttpClient/4.5.5 (Java/16.0.2)
//        httpConn.setRequestProperty("User-Agent", "Apache-HttpClient/4.5.5 (Java/1.8.0_401)"); //1.8.0_401
//        httpConn.setRequestProperty("Accept-Encoding", "text/xml"); //1.8.0_401
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        OutputStream out = httpConn.getOutputStream();
//Write the content of the request to the outputstream of the HTTP Connection.
        out.write(b);
        out.close();
//Ready with sending the request.

//Read the response.
        InputStreamReader isr = null;
        if (httpConn.getResponseCode() == 200) {
            isr = new InputStreamReader(httpConn.getInputStream());
        } else {
            isr = new InputStreamReader(httpConn.getErrorStream());
        }

        BufferedReader in = new BufferedReader(isr);

//Write the SOAP message response to a String.
        while ((responseString = in.readLine()) != null) {
            outputString = outputString + responseString;
        }
        InputStream trama = null; 
        System.out.println("Result XML = " + outputString);
        trama = new ByteArrayInputStream(outputString.getBytes("UTF-8"));
     
    
//Parse the String output to a org.w3c.dom.Document and be able to reach every node with the org.w3c.dom API.
//        Document document = parseXmlFile(outputString); // Write a separate method to parse the xml input.
//        NodeList nodeLst = document.getElementsByTagName("<TagName of the element to be retrieved>");
//        String elementValue = nodeLst.item(0).getTextContent();
//        System.out.println(elementValue);
//
////Write the SOAP message formatted to the console.
//        String formattedSOAPResponse = formatXML(outputString); // Write a separate method to format the XML input.
//        System.out.println(formattedSOAPResponse);
        return trama;
    }

//    public static void main(String[] args) throws IOException {
//        NewMain ejecuta = new NewMain();
//        String algo  = ejecuta.someMethod();
//    }

}
