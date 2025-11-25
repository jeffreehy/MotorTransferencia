///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.logic;
//
//import webservicestc.SDTPeticion;
//import webservicestc.SDTPeticionHeader;
//import webservicestc.SDTWSNICClientePeticion;
//import webservicestc.SDTWSNICCuentaPeticion;
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
////        try {
////            webservicestc.WSNICCuentaExecute parameters1 = null;
////            webservicestc.WSNICCuenta service1 = new webservicestc.WSNICCuenta();
////            webservicestc.WSNICCuentaSoapPort port1 = service1.getWSNICCuentaSoapPort();
////            // TODO process result here
////            webservicestc.WSNICCuentaExecuteResponse result = port1.execute(parameters1);
////            System.out.println("Result = " + result);
////        } catch (Exception ex) {
////            ex.printStackTrace();
////        }
//
//        try {
//            webservicestc.WSNICCuentaExecute parameters = new webservicestc.WSNICCuentaExecute();
//            SDTWSNICCuentaPeticion algo = new SDTWSNICCuentaPeticion();
//            SDTPeticionHeader algo1 = new SDTPeticionHeader();
//            SDTPeticion algo2 = new SDTPeticion();
//            algo.setTipoConsulta("01");
//            algo.setCuenta("2310000098");
//            algo.setMoneda("NIO");
//            algo.setCodigoBanco("2300");
//            algo.setCodigoCliente("3000000620");
//
////            algo.setIdentificacion(identificacion);
////            algo.setTipoIdentificacion(TipoIdentificacion);
//            algo1.setReferencia("234234234");
//            algo2.setHeader(algo1);
//            parameters.setPeticiongeneral(algo2);
//            parameters.setPeticion(algo);
////            parameters.setPeticiongeneral(algo1);
//            parameters.setPeticion(algo);
//            webservicestc.WSNICCuenta service = new webservicestc.WSNICCuenta();
//            webservicestc.WSNICCuentaSoapPort port = service.getWSNICCuentaSoapPort();
//            // TODO process result here
//            webservicestc.WSNICCuentaExecuteResponse result = port.execute(parameters);
//            System.out.println("Result = " + result.getRespuesta().getHeader().getCodigo().equals("0000"));
//            System.out.println("Result = " + result.getRespuesta().getHeader().getDetalle());
//            System.out.println("Result Cliente= " + result.getRespuesta().getDetail().getItem().get(0).getCliente());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//}
