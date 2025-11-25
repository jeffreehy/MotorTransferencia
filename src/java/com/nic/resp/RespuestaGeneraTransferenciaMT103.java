/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.resp;

/**
 *
 * @author oargueta
 */
public class RespuestaGeneraTransferenciaMT103 {
    private String Codigo;
    private String Tipo;
    private String Mensaje;
    private String Detalle;
    private String Referencia;
    private String ReferenciaComision;

    public String getReferenciaComision() {
        return ReferenciaComision;
    }

    public void setReferenciaComision(String ReferenciaComision) {
        this.ReferenciaComision = ReferenciaComision;
    }
    private String MT103;
    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String Detalle) {
        this.Detalle = Detalle;
    }

    public String getReferencia() {
        return Referencia;
    }

    public void setReferencia(String Referencia) {
        this.Referencia = Referencia;
    }

    public String getMT103() {
        return MT103;
    }

    public void setMT103(String MT103) {
        this.MT103 = MT103;
    }
    
    
    
    
}
