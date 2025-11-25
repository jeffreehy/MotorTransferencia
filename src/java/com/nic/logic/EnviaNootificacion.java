/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import motortransferencia.SDTGeneraNotificacionRequest;
import motortransferencia.SDTGeneraNotificacionRequestRequest;
import motortransferencia.SDTGeneraNotificacionRequestRequestData;
import motortransferencia.SDTGeneraNotificacionRequestRequestHeader;

/**
 *
 * @author oargueta
 */
public class EnviaNootificacion {

//    ProcesaTransferenciaIn procesaTransf = new ProcesaTransferenciaIn();
    public static RespuestaNotificaciones enviaCorreo(ProcesaTransferenciaIn input) {
        RespuestaNotificaciones deta = new RespuestaNotificaciones();

        try {
            motortransferencia.WsGeneraNotificacionExecute parametersNotificaciones = new motortransferencia.WsGeneraNotificacionExecute();
            motortransferencia.WsGeneraNotificacion service = new motortransferencia.WsGeneraNotificacion();
            motortransferencia.WsGeneraNotificacionSoapPort port = service.getWsGeneraNotificacionSoapPort();
//====================================================
            SDTGeneraNotificacionRequestRequestData someNOTI2 = new SDTGeneraNotificacionRequestRequestData();
            SDTGeneraNotificacionRequestRequestHeader someNOTI3 = new SDTGeneraNotificacionRequestRequestHeader();
            System.out.println("procesaTransf.getTransid() = " + input.getTransid());
            someNOTI2.setTransactionCode(input.getTransid());
            System.out.println("procesaTransf.getUsuario() = " + input.getUsuario());
            someNOTI2.setUserTransaction(input.getUsuario());
            SDTGeneraNotificacionRequestRequest someNOTI = new SDTGeneraNotificacionRequestRequest();
            SDTGeneraNotificacionRequest someNOTIX = new SDTGeneraNotificacionRequest();
            someNOTI3.setChannel("?");
            someNOTI3.setDate("?");
            someNOTI3.setIp("?");
            someNOTI3.setLanguage("?");
            someNOTI3.setSystem("?");
            someNOTI3.setTime("?");
            someNOTI3.setToken("?");
            someNOTI3.setUser("?");
            someNOTI.setData(someNOTI2);
            someNOTI.setHeader(someNOTI3);
            someNOTIX.setRequest(someNOTI);
            parametersNotificaciones.setPeticion(someNOTIX);
//====================================================
            motortransferencia.WsGeneraNotificacionExecuteResponse result = port.execute(parametersNotificaciones);
            System.out.println("ResultNotificaciones = " + result.getRespuesta().getCode());
            System.out.println("ResultNotificaciones = " + result.getRespuesta().getDetail());
            System.out.println("ResultNotificaciones = " + result.getRespuesta().getMessage());
            System.out.println("ResultNotificaciones = " + result.getRespuesta().getType());
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            deta.setCode("9999");
            deta.setMessage(ex.getMessage());
            deta.setType("NOOK");
        }
        return deta;
    }
}
