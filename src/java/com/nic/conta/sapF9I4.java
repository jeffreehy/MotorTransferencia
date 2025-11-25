///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.conta;
//
//import com.sap.document.sap.soap.functions.mc_style.ZstbcaSrvAddData;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.Authenticator;
//import java.net.PasswordAuthentication;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Properties;
//
///**
// *
// * @author oargueta
// */
//public class sapF9I4 {
//
//    static String usuario = null;
//    static String clave = null;
//    private static ZstbcaSrvAddData e;
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
////Autenticacion servicio SAP
//        Path path = Paths.get("resources");
//        String directorio = path.toAbsolutePath().toString();
//        int posicion = directorio.indexOf("resources");
//        directorio = directorio.substring(0, posicion);//
////        directorio = directorio + "resources\\";
//        directorio = "C:\\Users\\oargueta\\Documents\\NetBeansProjects\\wsTransferencias\\web\\resourses\\";
//        Properties prop = new Properties();
//        System.out.println("directorio : " + directorio+"resources\\");
//        File propFile = null;
//        propFile = new File(directorio +"propiedades.properties");
//        prop.load(new InputStreamReader(new FileInputStream(propFile), "UTF-8"));
//
//        usuario = prop.getProperty("usuario");
//        clave = prop.getProperty("clave");
//
//        Authenticator.setDefault(new Authenticator() {
//
////            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(prop.getProperty("usuario"), prop.getProperty("prop").toCharArray());
//            }
//        });
//        try {
//            java.lang.String iAcnumExt = "12345";
//            java.math.BigDecimal iAmount = null;
//            java.math.BigDecimal iAmountCom = null;
//            java.lang.String iBkkrs = "2300";
//            java.lang.String iOperacion = "1";
//            java.lang.String iRemark = "esta es una prueba";
//            com.sap.document.sap.soap.functions.mc_style.ZttbcaSrvAddData itAdditionalFilters = new com.sap.document.sap.soap.functions.mc_style.ZttbcaSrvAddData();
//
//            com.sap.document.sap.soap.functions.mc_style.ZWSF9I4_Service service = new com.sap.document.sap.soap.functions.mc_style.ZWSF9I4_Service();
//
//            com.sap.document.sap.soap.functions.mc_style.ZWSF9I4 port = service.getHTTPBASICSoap12();
//            // TODO process result here
//            com.sap.document.sap.soap.functions.mc_style.Zbdcmsgcoll result = port.zcfF9I4(iAcnumExt, iAmount, iAmountCom, iBkkrs, iOperacion, iRemark, itAdditionalFilters);
//            System.out.println("Result = " + result.getItem().get(0).getDyname());
//            System.out.println("Result = " + result.getItem().get(0).getDynumb());
//            System.out.println("Result = " + result.getItem().get(0).getEnv());
//            System.out.println("Result = " + result.getItem().get(0).getFldname());
//            System.out.println("Result = " + result.getItem().get(0).getMsgid());
//            System.out.println("Result = " + result.getItem().get(0).getMsgnr());
//            System.out.println("Result = " + result.getItem().get(0).getMsgspra());
//            System.out.println("Result = " + result.getItem().get(0).getMsgtyp());
//            System.out.println("Result = " + result.getItem().get(0).getMsgv1());
//            System.out.println("Result = " + result.getItem().get(0).getMsgv2());
//            System.out.println("Result = " + result.getItem().get(0).getMsgv3());
//            System.out.println("Result = " + result.getItem().get(0).getMsgv4());
//            System.out.println("Result = " + result.getItem().get(0).getTcode());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//}
