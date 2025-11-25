/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import com.nic.resp.RespuestaTransferenciaMT103;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SendParseRequestF9I8 {

    RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
    int codError;
    String mensaje = null;
    double monto = 0.0D;
    long noAutorizacion = 0L;
    String noCuenta = null;
    String nombre = null;
    long secCentral = 0L;

    public RespuestaTransferenciaMT103 SendParseRequest(String campo1, String campo2)
            throws ParserConfigurationException, SAXException {
        SendSoapF9I8 sendXML = new SendSoapF9I8();
//        NewMain1 someMethod = new NewMain1();
        try {
            InputStream trama = null;
            trama = sendXML.EnviaXml(campo1, campo2);
//            trama = someMethod.someMethod(campo1, campo2);

            String eso = new Scanner(trama).useDelimiter("\\A").next();
//            System.out.println("Result XML = " + eso);
            trama = new ByteArrayInputStream(eso.getBytes("UTF-8"));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(trama);
            doc.getDocumentElement().normalize();
            try {
                dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(SendParseRequestF9I8.class.getName()).log(Level.SEVERE, null, ex);
            }

            NodeList nList = null;

            nList = doc.getElementsByTagName("Respuesta");
            System.out.println("nList = " + nList.getLength());
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    deta.setTipo((getTagValue("Tipo", eElement)));
                    System.out.println("nList = " + deta.getTipo());
                    deta.setMensaje(getTagValue("Mensaje", eElement));
                    System.out.println("nList = " + deta.getMensaje());
                    deta.setDetalle(getTagValue("Detalle", eElement));
                    System.out.println("nList = " + deta.getDetalle());
                    deta.setReferencia(getTagValue("Referencia", eElement));
                    System.out.println("nList = " + deta.getReferencia());
                }
                try {
                    trama.close();
                } catch (IOException ex) {
                    System.out.println("SendParseRequest : " + ex.getMessage());
                }
            }
            System.out.println("return deta = " );
            return deta;
        } catch (IOException ex) {
            Logger.getLogger(SendParseRequestF9I8.class.getName()).log(Level.SEVERE, null, ex);
        }
        return deta;
    }

    private static String getTagValue(String sTag, Element eElement) {
        if (eElement.getElementsByTagName(sTag).item(0) == null) {
            String algo = " ";
            return algo;
        }
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        if (nValue == null) {
            String algo = " ";
            return algo;
        }
        return nValue.getNodeValue();
    }

//    public static void main(String[] args) {
//        try {
//            RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
//            SendParseRequestF9I8 ejecuta = new SendParseRequestF9I8();
//            SDTWSNICF9I8Peticion algito1 = new SDTWSNICF9I8Peticion();
//            algito1.setAreaBancaria("2300");
//            algito1.setDocumento("000000025314");
//            String campo1 = "2300";
//            String campo2 = "000000025314";
//            String respuesta = ejecuta.SendParseRequestF9I8(campo1, campo2);
////            System.out.println("Result = " + resultx.getRespuesta().getHeader().getCodigo());
////            System.out.println("Result = " + resultx.getRespuesta().getHeader().getDetalle());
////            System.out.println("Result = " + resultx.getRespuesta().getHeader().getMensaje());
////            System.out.println("Result = " + resultx.getRespuesta().getHeader().getReferencia());
////            System.out.println("Result = " + resultx.getRespuesta().getHeader().getTipo());
//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(SendParseRequestF9I8.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SAXException ex) {
//            Logger.getLogger(SendParseRequestF9I8.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
