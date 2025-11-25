/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.resp;

/**
 *
 * @author jtalaverad
 */
public class TransferenciaItem {

    private String linea;
    private String fechaHora;
    private String tipoAccion;
    private String accion;
    private CampoColeccion campoColeccion;

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public CampoColeccion getCampoColeccion() {
        return campoColeccion;
    }

    public void setCampoColeccion(CampoColeccion campoColeccion) {
        this.campoColeccion = campoColeccion;
    }


    
    

}
