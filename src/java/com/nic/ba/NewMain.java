///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.ba;
//
//import java.text.DecimalFormat;
//import webservicestc.SDTPeticionHeader;
//import webservicestc.SDTWSNICF9I4Peticion;
//import webservicestc.SDTWSNICPPSwiftPeticion;
//
///**
// *
// * @author oargueta
// */
//public class NewMain {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) {
//        
//        
//        double num1 = 20.00010921710449;
//        
//                                DecimalFormat df = new DecimalFormat("#.00");
//                                System.out.println(df.format(num1));
//        try {
//            webservicestc.WSNICPPSWIFTExecute parameters = new webservicestc.WSNICPPSWIFTExecute();
//            webservicestc.WSNICPPSWIFT service = new webservicestc.WSNICPPSWIFT();
//            webservicestc.WSNICPPSWIFTSoapPort port = service.getWSNICPPSWIFTSoapPort();
//            SDTPeticionHeader deta1 = new SDTPeticionHeader();
//            SDTWSNICPPSwiftPeticion deta2 = new SDTWSNICPPSwiftPeticion();
//            deta2.setAreaBancaria("2300");
//            deta2.setComentario("Prueba de Servicio");
//            deta2.setCuenta("2320000402");
//            deta2.setCuentaMayor("1102020106");
//            deta2.setOperacion("5159");
//            deta2.setReferenciaExterna("1231231");
//            deta2.setTasaCambio("36.61");
//            deta2.setValorComision("1.54");
//            deta2.setValorComisionBANI("1.12");
//            deta2.setValorTransaccion("20.10");
//            parameters.setPeticion(deta2);
////            parameters.setPeticiongeneral(deta1);            
//            webservicestc.WSNICPPSWIFTExecuteResponse result = port.execute(parameters);
//            System.out.println("Result = " + result.getRespuesta().getHeader().getCodigo());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getMensaje());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getDetalle());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getTipo());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getReferencia());
//            for (int i = 0; i < result.getRespuesta().getDetail().getItem().size(); i++) {
//                System.out.println("Result getDetail 1 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGTYP());
//                System.out.println("Result getDetail 2 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGID());
//                System.out.println("Result getDetail 3 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGNR());
//                System.out.println("Result getDetail 4 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGV1());
//                System.out.println("Result getDetail 5 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGV2());
//                System.out.println("Result getDetail 6 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGV3());
//                System.out.println("Result getDetail 7 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getMSGV4());
//                System.out.println("Result getDetail 8 = " + i + " -- " + result.getRespuesta().getDetail().getItem().get(i).getFLDNAME());
//
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//}
