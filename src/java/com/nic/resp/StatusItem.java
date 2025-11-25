/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.resp;

/**
 *
 * @author jtalavera
 */
public class StatusItem {
    //2

    private String CodigoRetorno;
    private String Mensaje;
    private String Referencia;

    public String getCodigoRetorno() {
        return CodigoRetorno;
    }

    public void setCodigoRetorno(String CodigoRetorno) {
        this.CodigoRetorno = CodigoRetorno;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }
    
    

}
