/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.resp;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author oargueta
 */
public class RequestEvento {

    private long TransaccionSecuencia;
    private long TransaccionEventoLinea;
    private XMLGregorianCalendar TransaccionEventoSistema;
    private String TransaccionEventoAccion;
    private String TipoEventoCodigo;
    private XMLGregorianCalendar TransaccionEventoInicio;
    private String TransaccionUsuarioCodigo;
    private String TransaccionEventoDispositivo;
    private String TransaccionEventoIP;
    private String TransaccionEventoSession;
    private String TransaccionEventoComentario;
    private XMLGregorianCalendar TransaccionEventoFinal;
    private String TransaccionEventoGestion;
    private String TransaccionEventoEstado;    

    public XMLGregorianCalendar getTransaccionEventoSistema() {
        return TransaccionEventoSistema;
    }

    public void setTransaccionEventoSistema(XMLGregorianCalendar TransaccionEventoSistema) {
        this.TransaccionEventoSistema = TransaccionEventoSistema;
    }

    public XMLGregorianCalendar getTransaccionEventoInicio() {
        return TransaccionEventoInicio;
    }

    public void setTransaccionEventoInicio(XMLGregorianCalendar TransaccionEventoInicio) {
        this.TransaccionEventoInicio = TransaccionEventoInicio;
    }

    public XMLGregorianCalendar getTransaccionEventoFinal() {
        return TransaccionEventoFinal;
    }

    public void setTransaccionEventoFinal(XMLGregorianCalendar TransaccionEventoFinal) {
        this.TransaccionEventoFinal = TransaccionEventoFinal;
    }

    public long getTransaccionSecuencia() {
        return TransaccionSecuencia;
    }

    public void setTransaccionSecuencia(long TransaccionSecuencia) {
        this.TransaccionSecuencia = TransaccionSecuencia;
    }

    public long getTransaccionEventoLinea() {
        return TransaccionEventoLinea;
    }

    public void setTransaccionEventoLinea(long TransaccionEventoLinea) {
        this.TransaccionEventoLinea = TransaccionEventoLinea;
    }


    public String getTransaccionEventoAccion() {
        return TransaccionEventoAccion;
    }

    public void setTransaccionEventoAccion(String TransaccionEventoAccion) {
        this.TransaccionEventoAccion = TransaccionEventoAccion;
    }

    public String getTipoEventoCodigo() {
        return TipoEventoCodigo;
    }

    public void setTipoEventoCodigo(String TipoEventoCodigo) {
        this.TipoEventoCodigo = TipoEventoCodigo;
    }


    public String getTransaccionUsuarioCodigo() {
        return TransaccionUsuarioCodigo;
    }

    public void setTransaccionUsuarioCodigo(String TransaccionUsuarioCodigo) {
        this.TransaccionUsuarioCodigo = TransaccionUsuarioCodigo;
    }

    public String getTransaccionEventoDispositivo() {
        return TransaccionEventoDispositivo;
    }

    public void setTransaccionEventoDispositivo(String TransaccionEventoDispositivo) {
        this.TransaccionEventoDispositivo = TransaccionEventoDispositivo;
    }

    public String getTransaccionEventoIP() {
        return TransaccionEventoIP;
    }

    public void setTransaccionEventoIP(String TransaccionEventoIP) {
        this.TransaccionEventoIP = TransaccionEventoIP;
    }

    public String getTransaccionEventoSession() {
        return TransaccionEventoSession;
    }

    public void setTransaccionEventoSession(String TransaccionEventoSession) {
        this.TransaccionEventoSession = TransaccionEventoSession;
    }

    public String getTransaccionEventoComentario() {
        return TransaccionEventoComentario;
    }

    public void setTransaccionEventoComentario(String TransaccionEventoComentario) {
        this.TransaccionEventoComentario = TransaccionEventoComentario;
    }


    public String getTransaccionEventoGestion() {
        return TransaccionEventoGestion;
    }

    public void setTransaccionEventoGestion(String TransaccionEventoGestion) {
        this.TransaccionEventoGestion = TransaccionEventoGestion;
    }

    public String getTransaccionEventoEstado() {
        return TransaccionEventoEstado;
    }

    public void setTransaccionEventoEstado(String TransaccionEventoEstado) {
        this.TransaccionEventoEstado = TransaccionEventoEstado;
    }

  
    
    
    
}
