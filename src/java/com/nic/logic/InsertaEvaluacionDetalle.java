/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import com.nic.resp.StatusItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oargueta
 */
public class InsertaEvaluacionDetalle {

    public Integer insertaEvaluacionDetalle(long sequence, Integer linea, String nombre, String nombreListas, String horaInicio, LocalDateTime now, Connection con, String actor, double similiritud,String OKDN) {
        int numberOfRowsInserted = 0;
        try {
            PreparedStatement prepStmt = null;
            String sql = "INSERT INTO dbo.EvaluacionDetalle(EvaluacionSecuencia\n"
                    + ",EvaluacionDetalleLinea\n"
                    + ",EvaluacionDetalleInicioFecha\n"
                    + ",EvaluacionDetalleInicioHora\n"
                    + ",EvaluacionDetalleFinalFecha\n"
                    + ",EvaluacionDetalleFinalHora\n"
                    + ",EvaluacionDetalleTiempo\n"
                    + ",EvaluacionDetalleTipoActor\n"
                    + ",EvaluacionDetalleNombre\n"
                    + ",EvaluacionDetalleNombreEnLista\n"
                    + ",EvaluacionDetallePorcentajeCoincidencia\n"
                    + ",EvaluacionDetalleObservacion\n"
                    + ",EvaluacionDetalleEstado\n"
                    + ",EvaluacionDetalleArgumento1\n"
                    + ",EvaluacionDetalleArgumento2\n"
                    + ",EvaluacionDetalleObsFecha\n"
                    + ",EvaluacionDetalleObsHora\n"
                    + ",EvaluacionDetalleObsUsuario\n"
                    + ",EvaluacionDetalleObsTipo) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            prepStmt = con.prepareStatement(sql);
            prepStmt.setLong(1, sequence);
            prepStmt.setInt(2, linea);//numero del registro
            prepStmt.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
            prepStmt.setString(4, horaInicio);
            prepStmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
            LocalDateTime now2 = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            String formattedDateTime2 = now2.format(formatter);
            prepStmt.setString(6, formattedDateTime2);
            Duration duration = Duration.between(now, now2);
            double hourDifference = duration.toMillis();
//            System.out.println("tiempo que tardo en procesar oriinal DETALLE =" + hourDifference);
            hourDifference = hourDifference / 1000.000;
            prepStmt.setDouble(7, hourDifference);
            prepStmt.setString(8, actor);   // beneficiario o remitente         
            prepStmt.setString(9, nombre);
            prepStmt.setString(10, nombreListas);
            prepStmt.setDouble(11, similiritud);
            prepStmt.setString(12, "?");
            prepStmt.setString(13, "TIPESTACT");
            prepStmt.setString(14, OKDN);    //?
            prepStmt.setString(15, "?");     //?   
            prepStmt.setDate(16, java.sql.Date.valueOf("1753-01-01"));
            prepStmt.setString(17, "?");
            prepStmt.setString(18, "?");
            prepStmt.setString(19, "?");
//            System.out.println("tiempo que tardo en procesar / 1000 =" + hourDifference);
//            System.out.println("Por insertarDetalle sequence : " + sequence);
//            System.out.println("Por insertarDetalle linea : " + linea);
//            System.out.println("Por insertarDetalle actor : " + actor); 
//            System.out.println("Por insertarDetalle nombre : " + nombre);
            numberOfRowsInserted = prepStmt.executeUpdate();
//            System.out.println("numberOfRowsInserted=" + numberOfRowsInserted);
//            prepStmt.close(); // close PreparedStatement
        } catch (SQLException ex) {
            Logger.getLogger(InsertaEvaluacionDetalle.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQLException :" + ex.getMessage());
        }
        return numberOfRowsInserted;
    }
}
