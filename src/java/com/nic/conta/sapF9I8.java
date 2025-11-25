///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.conta;
//
//import com.nic.resp.RespuestaTransferenciaMT103;
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import webservicestc.SDTPeticion;
//import webservicestc.SDTPeticionHeader;
//import webservicestc.SDTWSNICClientePeticion;
//import webservicestc.SDTWSNICF9I4Peticion;
//import webservicestc.SDTWSNICF9I8Peticion;
//
///**
// *
// * @author oargueta
// */
//public class sapF9I8 {
//
//    /**
//     * @param args the command line arguments
//     */
//    
//    public static void main(String[] args) throws IOException {
//        String algo2a = null;
////        webservicestc.WSNICF9I4Execute parameters = new webservicestc.WSNICF9I4Execute();
////        SDTWSNICClientePeticion algo = new SDTWSNICClientePeticion();
////        SDTPeticionHeader algo1 = new SDTPeticionHeader();
////        SDTPeticion algo2 = new SDTPeticion();
////        SDTWSNICF9I4Peticion algo3 = new SDTWSNICF9I4Peticion();
////        algo3.setOperacion("1");
////        algo3.setAreaBancaria("2300");
//////        System.out.println("AreaBancaria = " + AreaBancaria);
////        algo3.setCuenta("2320000439");
//////        System.out.println("Cuenta = " + Cuenta);
////        algo3.setComentario("nada");
//////        System.out.println("Comentario = " + Comentario);
////        algo3.setValorTransaccion("1.00");
//////        System.out.println("ValorTransaccion = " + ValorTransaccion);
////        algo3.setValorComision("0");
//////        System.out.println("ValorComision = " + ValorComision);
////        parameters.setPeticion(algo3);
////        webservicestc.WSNICF9I4 service = new webservicestc.WSNICF9I4();
////        webservicestc.WSNICF9I4SoapPort port = service.getWSNICF9I4SoapPort();
////        // TODO process result here
////        System.out.println("Contabiliza WSNICF9I4ExecuteResponse : ");
////        webservicestc.WSNICF9I4ExecuteResponse result = port.execute(parameters);
////        RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
////        String algo1a = result.getRespuesta().getHeader().getTipo();
////        algo2a = result.getRespuesta().getDetail().getItem().get(0).getMsgv2();
////        algo2a = "000000025344";
////        BufferedReader br = null;
////        String fileUrl = "";
////        try {
//////                br = new BufferedReader(new FileReader("C:/UniRed/URLCliente.txt"));
////            br = new BufferedReader(new FileReader("C:/jeta/jeta.txt"));
////            String line = null;
////            while ((line = br.readLine()) != null) {
////                fileUrl = line;
////            }
////            br.close();
////        } catch (FileNotFoundException e) {
////
////            System.out.println();
////        } catch (IOException e) {
////            System.out.println();
////        }
////        algo2a = fileUrl;
//    algo2a = "000000097466";
//        try {
//            webservicestc.WSNICF9I8Execute parameters2 = new webservicestc.WSNICF9I8Execute();
//            SDTWSNICF9I8Peticion algo4 = new SDTWSNICF9I8Peticion();
//            algo4.setAreaBancaria("2300");
////            System.out.println("AreaBancaria ******* " + algo1);
////            algo4.setDocumento("000000025381");
//            algo4.setDocumento(algo2a);
//            System.out.println("documentoValor ******* " + algo2a);
//            parameters2.setPeticion(algo4);
//            System.out.println("parameters2.getPeticion().getAreaBancaria() " + parameters2.getPeticion().getAreaBancaria());
//            System.out.println("parameters2.getPeticion().getDocumento() " + parameters2.getPeticion().getDocumento());
//            webservicestc.WSNICF9I8 service2 = new webservicestc.WSNICF9I8();
//            webservicestc.WSNICF9I8SoapPort port2 = service2.getWSNICF9I8SoapPort();
//            webservicestc.WSNICF9I8ExecuteResponse result2 = port2.execute(parameters2);
//            System.out.println("Result = " + result2.getRespuesta().getHeader().getCodigo());
//            System.out.println("Result = " + result2.getRespuesta().getHeader().getDetalle());
//            System.out.println("Result = " + result2.getRespuesta().getHeader().getMensaje());
//            System.out.println("Result = " + result2.getRespuesta().getHeader().getReferencia());
//            System.out.println("Result = " + result2.getRespuesta().getHeader().getTipo());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//}
