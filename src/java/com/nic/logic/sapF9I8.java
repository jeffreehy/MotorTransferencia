///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.nic.logic;
//
//import com.nic.conta.*;
//import com.nic.resp.RespuestaTransferenciaMT103;
//
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
//    webservicestc.WSNICF9I8ExecuteResponse result = new webservicestc.WSNICF9I8ExecuteResponse();
//
//    public webservicestc.WSNICF9I8ExecuteResponse contaF9I8(SDTWSNICF9I8Peticion algo) {
////        RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
//
//        try {
//            webservicestc.WSNICF9I8Execute parameters2 = new webservicestc.WSNICF9I8Execute();
//            SDTWSNICF9I8Peticion algo4 = new SDTWSNICF9I8Peticion();
//            algo4.setAreaBancaria(algo.getAreaBancaria());
//            System.out.println("AreaBancaria ******* " + algo.getAreaBancaria());
//            algo4.setDocumento(algo.getDocumento());
//            System.out.println("documentoValor ******* " + algo.getDocumento());
//            parameters2.setPeticion(algo4);
//            System.out.println("parameters2.getPeticion().getAreaBancaria() " + parameters2.getPeticion().getAreaBancaria());
//            System.out.println("parameters2.getPeticion().getDocumento() " + parameters2.getPeticion().getDocumento());
//            webservicestc.WSNICF9I8 service = new webservicestc.WSNICF9I8();
//            webservicestc.WSNICF9I8SoapPort port = service.getWSNICF9I8SoapPort();
//             result = port.execute(parameters2);
////            deta.setTipo(result.getRespuesta().getHeader().getTipo());
////            deta.setDetalle(result.getRespuesta().getHeader().getDetalle());
////            deta.setMensaje(result.getRespuesta().getHeader().getMensaje());
////            deta.setReferencia(result.getRespuesta().getHeader().getReferencia());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getCodigo());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getDetalle());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getMensaje());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getReferencia());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getTipo());
//            return result;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return result;
//    }
//        public static void main(String[] args) {
//        RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
//        SDTWSNICF9I8Peticion algito1 = new SDTWSNICF9I8Peticion();
//        algito1.setAreaBancaria("2300"); 
//        algito1.setDocumento("000000025314"); 
//        sapF9I8 ejecuta = new sapF9I8();
//        webservicestc.WSNICF9I8ExecuteResponse resultx = ejecuta.contaF9I8(algito1);
//            System.out.println("Result = " + resultx.getRespuesta().getHeader().getCodigo());
//            System.out.println("Result = " + resultx.getRespuesta().getHeader().getDetalle());
//            System.out.println("Result = " + resultx.getRespuesta().getHeader().getMensaje());
//            System.out.println("Result = " + resultx.getRespuesta().getHeader().getReferencia());
//            System.out.println("Result = " + resultx.getRespuesta().getHeader().getTipo());
//        }
//}
