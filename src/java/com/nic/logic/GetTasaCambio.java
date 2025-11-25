///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.logic;
//
//import webservicestc.SDTPeticion;
//import webservicestc.SDTPeticionHeader;
////import webservicestc.SDTWSNICClientePeticion;
//import webservicestc.SDTWSNICTasaCambioPeticion;
//
///**
// *
// * @author jtalaverad
// */
//public class GetTasaCambio {
//
//    public static  double ObtieneTasaCambio(String TasaDe, String TasaA, String fecha) {
//
//        try {
//            webservicestc.WSNICTasaCambioExecute parameters = new webservicestc.WSNICTasaCambioExecute();
//            SDTWSNICTasaCambioPeticion algo = new SDTWSNICTasaCambioPeticion();
//            SDTPeticionHeader algo1 = new SDTPeticionHeader();
//            SDTPeticion algo2 = new SDTPeticion();
//            algo.setFecha(fecha);
//            algo.setMonedaOrigen(TasaDe);
//            algo.setMonedaDestino(TasaA);
//            algo1.setReferencia("123123123123");
//            algo2.setHeader(algo1);
//            parameters.setPeticiongeneral(algo2);
//            parameters.setPeticion(algo);
//            webservicestc.WSNICTasaCambio service = new webservicestc.WSNICTasaCambio();
//            webservicestc.WSNICTasaCambioSoapPort port = service.getWSNICTasaCambioSoapPort();            
//            webservicestc.WSNICTasaCambioExecuteResponse result = port.execute(parameters);
//            System.out.println("Result = " + result.getRespuesta().getHeader().getCodigo());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getTipo());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getMensaje());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getReferencia());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(0).getTipoCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(0).getTasaCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(0).getFechaCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(1).getTipoCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(1).getTasaCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(1).getFechaCambio());
//             System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(2).getTipoCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(2).getTasaCambio());
//            System.out.println("Result = " + result.getRespuesta().getDetail().getItem().get(2).getFechaCambio());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//        return 0.0;
//    }
//
//    public static void main(String[] args) {
//        String TasaDe = "840";
//        String TasaA = "558";
//        String fecha = "20231016";
//        double respuesta = ObtieneTasaCambio( TasaDe,  TasaA,  fecha);
//        System.out.println(" Respuesta tasa de cambio hoy : "+fecha+ "  Tasa de compra : "+respuesta);
//    }
//
//}
