/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.resp;

import java.util.ArrayList;

/**
 *
 * @author jtalaverad
 */
public class RepuestaTransferencia {
    
    private ArrayList<StatusGlobal> estadoGlobal;
    private ArrayList<DetalleColeccion> detalleColeccion;
//    private ArrayList<StatusItem> detalle;

    public ArrayList<StatusGlobal> getEstadoGlobal() {
        return estadoGlobal;
    }

    public void setEstadoGlobal(ArrayList<StatusGlobal> estadoGlobal) {
        this.estadoGlobal = estadoGlobal;
    }

//    public ArrayList<StatusItem> getDetalle() {
//        return detalle;
//    }
//
//    public void setDetalle(ArrayList<StatusItem> detalle) {
//        this.detalle = detalle;
//    }

    public ArrayList<DetalleColeccion> getDetalleColeccion() {
        return detalleColeccion;
    }

    public void setDetalleColeccion(ArrayList<DetalleColeccion> detalleColeccion) {
        this.detalleColeccion = detalleColeccion;
    }

    
    
}
