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
import webservicestc.SDTWSNICCuentaPeticion;
//import webservicestc.SDTWSNICCuentaPeticion;

/**
 *
 * @author oargueta
 */
public class BuscaInfoCuenta {

    public RespuestaCuenta BuscarInformacionCuenta(String cuenta) {
        RespuestaCuenta deta = new RespuestaCuenta();

        try {
            webservicestc.WSNICCuentaExecute parameters = new webservicestc.WSNICCuentaExecute();
            SDTWSNICCuentaPeticion algo = new SDTWSNICCuentaPeticion();
            SDTPeticionHeader algo1 = new SDTPeticionHeader();
            SDTPeticion algo2 = new SDTPeticion();
            algo.setItem("1");
            algo.setTipoConsulta("01");
            algo.setCodigoBanco("2300");
            algo.setMoneda("NIO");
            algo.setCuenta(cuenta);
//            algo.setMoneda("NIO");
//            algo.setCodigoBanco("2300");
//            algo.setCodigoCliente("3000000620");

//            algo.setIdentificacion(identificacion);
//            algo.setTipoIdentificacion(TipoIdentificacion);
//            algo1.setReferencia("234234234");
            algo2.setHeader(algo1);
            parameters.setPeticiongeneral(algo2);
            parameters.setPeticion(algo);
//            parameters.setPeticiongeneral(algo1);
            parameters.setPeticion(algo);
            webservicestc.WSNICCuenta service = new webservicestc.WSNICCuenta();
            webservicestc.WSNICCuentaSoapPort port = service.getWSNICCuentaSoapPort();
            // TODO process result here
            webservicestc.WSNICCuentaExecuteResponse result = port.execute(parameters);
            deta.setCuenta(result.getRespuesta().getDetail().getItem().get(0).getCuenta());
            deta.setCliente(result.getRespuesta().getDetail().getItem().get(0).getCliente());
            deta.setDescripcionResultado(result.getRespuesta().getDetail().getItem().get(0).getDescripcionResultado());
            deta.setTipoProducto(result.getRespuesta().getDetail().getItem().get(0).getTipoProducto());
            deta.setEstadoCuenta(result.getRespuesta().getDetail().getItem().get(0).getEstadoCuenta());
            BuscaInfoCliente busca = new BuscaInfoCliente();
            RespuestaCliente deta1 = new RespuestaCliente();
            deta1 = busca.BuscarInformacionCuenta(deta.getCliente());
            deta.setNombreClienteBeneficiarioCore(deta1.getNombre());
//            System.out.println("Result = " + result.getRespuesta().getHeader().getDetalle());
//            System.out.println("Result Cliente= " + result.getRespuesta().getDetail().getItem().get(0).getCliente());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return deta;
    }
}
