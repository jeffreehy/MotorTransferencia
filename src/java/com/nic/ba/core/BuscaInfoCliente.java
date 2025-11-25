/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.ba.core;

import com.nic.resp.RespuestaCliente;
import com.nic.resp.RespuestaCuenta;
import webservicestc.SDTPeticion;
import webservicestc.SDTPeticionHeader;
import webservicestc.SDTWSNICClientePeticion;

/**
 *
 * @author oargueta
 */
public class BuscaInfoCliente {

    public RespuestaCliente BuscarInformacionCuenta(String cliente) {
        RespuestaCliente deta = new RespuestaCliente();

        webservicestc.WSNICClienteExecute parameters = new webservicestc.WSNICClienteExecute();
        SDTWSNICClientePeticion algo = new SDTWSNICClientePeticion();
        SDTPeticionHeader algo1 = new SDTPeticionHeader();
        SDTPeticion algo2 = new SDTPeticion();

        algo.setTipoConsulta("02");
        algo.setIdentificacion(cliente);
//            algo.setTipoIdentificacion();
//            algo1.setReferencia(transactionId);
        algo2.setHeader(algo1);
        parameters.setPeticiongeneral(algo2);
        parameters.setPeticion(algo);
//            parameters.setPeticiongeneral(algo1);
        parameters.setPeticion(algo);
//            webservicestc.WSNICCliente service = new webservicestc.WSNICCliente();
        webservicestc.WSNICCliente service = new webservicestc.WSNICCliente();
        webservicestc.WSNICClienteSoapPort port = service.getWSNICClienteSoapPort();
        webservicestc.WSNICClienteExecuteResponse result = port.execute(parameters);
        System.out.println("Result = " + result.getRespuesta().getHeader().getCodigo());
        if (result.getRespuesta().getHeader().getCodigo().equals("0000")) {
            deta.setMensaje(result.getRespuesta().getHeader().getMensaje());
            deta.setNombre(result.getRespuesta().getDetail().getItem().get(0).getNombre());
            deta.setNumCliente(result.getRespuesta().getDetail().getItem().get(0).getNumCliente());
            deta.setId(result.getRespuesta().getDetail().getItem().get(0).getId());
            deta.setTipoId(result.getRespuesta().getDetail().getItem().get(0).getTipoId());
            deta.setTipoPersona(result.getRespuesta().getDetail().getItem().get(0).getTipoPersona());
//            deta.setEstado(result.getRespuesta().getDetail().getItem().get(0).getEstado());

            return deta; 
        }
        return deta;
        
    }
}
