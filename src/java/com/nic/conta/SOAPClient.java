///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.conta;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.xml.ws.BindingProvider;
//
//public class SOAPClient {
//
//    public static void main(String[] args) {
//        // Replace with the actual SOAP web service URL
//        String serviceUrl = "http://saperpdevnic.adbancat.hn:8000/sap/bc/srt/rfc/sap/zws_f9i4/200/zws_f9i4/http_basic";
//
//        // Replace with actual username and password for authentication
//        String username = "TU_DEBITO";
//        String password = "N!CdebitoB@N*01";
//
//        // Create a service instance
////        YourSOAPService service = new YourSOAPService();
////
////        // Get the port (service endpoint)
////        YourSOAPPortType port = service.getYourSOAPPort();
//
//        try {
//            java.lang.String iAcnumExt = "12345";
//            java.math.BigDecimal iAmount = null;
//            java.math.BigDecimal iAmountCom = null;
//            java.lang.String iBkkrs = "2300";
//            java.lang.String iOperacion = "1";
//            java.lang.String iRemark = "esta es una prueba";
//            com.sap.document.sap.soap.functions.mc_style.ZttbcaSrvAddData itAdditionalFilters = null;
//            com.sap.document.sap.soap.functions.mc_style.ZWSF9I4_Service service = new com.sap.document.sap.soap.functions.mc_style.ZWSF9I4_Service();
//            com.sap.document.sap.soap.functions.mc_style.ZWSF9I4 port = service.getHTTPBASICSoap12();
//                    Map<String, Object> requestContext = ((BindingProvider) port).getRequestContext();
//        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceUrl);
//
//        // Set the username and password for authentication
//        requestContext.put(BindingProvider.USERNAME_PROPERTY, username);
//        requestContext.put(BindingProvider.PASSWORD_PROPERTY, password);
//            // TODO process result here
//                   // Set the service URL
//
//            com.sap.document.sap.soap.functions.mc_style.Zbdcmsgcoll result = port.zcfF9I4(iAcnumExt, iAmount, iAmountCom, iBkkrs, iOperacion, iRemark, itAdditionalFilters);
//            System.out.println("Result = " + result);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
// 
//
//        // Make SOAP requests using the port
//        // (Add your SOAP request logic here)
//        // Example: Call a method on the SOAP service
////        String result = port.yourSOAPMethod();
//
//        // Process the result
////        System.out.println("Result: " + result);
//    }
//}
