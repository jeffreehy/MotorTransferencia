/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.resp;

import java.sql.Connection;

/**
 *
 * @author oargueta
 */
public class ValidarTags {
    private String transactionId;
    private String usuarioId;
    private String pantalla;
    private String terminal;
    private String llaveSesion;
    private Connection conexion;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPantalla() {
        return pantalla;
    }

    public void setPantalla(String pantalla) {
        this.pantalla = pantalla;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getLlaveSesion() {
        return llaveSesion;
    }

    public void setLlaveSesion(String llaveSesion) {
        this.llaveSesion = llaveSesion;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
}
