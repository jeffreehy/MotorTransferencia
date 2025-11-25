/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.ba.db;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
//import motortransferencia.SDTTransaccionEvento;
import motortransferencia.*;

/**
 *
 * @author oargueta
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DatatypeConfigurationException {
//        System.out.println("************* DATE " +new java.sql.Date(new java.util.Date().getTime()));
//        System.out.println("************* TIME " +new java.sql.Time(new java.util.Date().getTime()));
//        System.out.println("************* TIMESTAMP " +new java.sql.Timestamp(new java.util.Date().getTime()));
////        
//        java.sql.Time campo1 =  new java.sql.Time(new java.util.Date().getTime());
//        java.sql.Time campo2 =  new java.sql.Time(new java.util.Date().getTime());
//        Date dt = new Date();
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
//        String formattedDateTime = now.format(formatter);
//        System.out.println("Current date and time with milliseconds: " + formattedDateTime);
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        XMLGregorianCalendar xCal = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(cal);
        try {
            motortransferencia.WsInsertTransactionEventExecute parameters = new motortransferencia.WsInsertTransactionEventExecute();
            SDTTransaccionEvento deta = new SDTTransaccionEvento();
//            deta.setTipoEventoCodigo(formattedDateTime);
            deta.setTransaccionSecuencia(01);
            deta.setTransaccionEventoLinea(0);
            deta.setTransaccionEventoSistema(xCal);
            deta.setTransaccionEventoAccion("Revision4");
            deta.setTipoEventoCodigo("INSERT");
            deta.setTransaccionEventoInicio(xCal);
            deta.setTransaccionUsuarioCodigo("OARGUETA");
            deta.setTransaccionEventoDispositivo("DRAGONBALLZ");
            deta.setTransaccionEventoIP("127.0.0.1");
            deta.setTransaccionEventoSession("tovbkrsdqwj3erj4bisswler");
            deta.setTransaccionEventoComentario("Revision de xxxxxxx");
            deta.setTransaccionEventoFinal(xCal);
            deta.setTransaccionEventoGestion("TIPESTACT");
            deta.setTransaccionEventoEstado("TIPESTACT");
            parameters.setEvento(deta);
            motortransferencia.WsInsertTransactionEvent service = new motortransferencia.WsInsertTransactionEvent();
            motortransferencia.WsInsertTransactionEventSoapPort port = service.getWsInsertTransactionEventSoapPort();
            // TODO process result here
            motortransferencia.WsInsertTransactionEventExecuteResponse result = port.execute(parameters);
            System.out.println("Result = " + result.getRespuesta().getCode());
            System.out.println("Result = " + result.getRespuesta().getDetail());
            System.out.println("Result = " + result.getRespuesta().getMessage());
            System.out.println("Result = " + result.getRespuesta().getType());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
//System.out.println("Current date and time with milliseconds: " + campo1);

    }

}
