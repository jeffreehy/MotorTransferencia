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
public class InsertaEvaluacion {

    public Integer insertaEvaluacion(long sequence, String tipoEvaluacion, String transactionId, StatusItem detalle, String horaInicio, LocalDateTime now, Connection con, String evaluacionLista) {
        int numberOfRowsInserted = 0;
        try {

            PreparedStatement prepStmt = null;
            String sql = "INSERT dbo.Evaluacion "
                    + "(EvaluacionSecuencia\n"
                    + ",EvaluacionTipo\n"
                    + ",TransaccionSecuencia\n"
                    + ",EvaluacionResultadoTipo\n"
                    + ",EvaluacionResultadoCodigo\n"
                    + ",EvaluacionResultadoMensaje\n"
                    + ",EvaluacionInicioFecha\n"
                    + ",EvaluacionInicioHora\n"
                    + ",EvaluacionFinalFecha\n"
                    + ",EvaluacionFinalHora\n"
                    + ",EvaluacionTiempo\n"
                    + ",EvaluacionLista\n"
                    + ",EvaluacionArgumento1\n"
                    + ",EvaluacionArgumento2\n"
                    + ",EvaluacionArgumento3\n"
                    + ",EvaluacionArgumento4\n"
                    + ",EvaluacionFlag1\n"
                    + ",EvaluacionFlag2\n"
                    + ",EvaluacionFlag3\n"
                    + ",EvaluacionFlag4\n"
                    + ",EvaluacionDictamenTipo\n"
                    + ",EvaluacionDictamenFecha\n"
                    + ",EvaluacionDictamenHora\n"
                    + ",EvaluacionDictamenUsuario\n"
                    + ",EvaluacionDictamenSession\n"
                    + ",EvaluacionDictamenComentario\n"
                    + ",EvaluacionEventoLinea) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            prepStmt = con.prepareStatement(sql);
            prepStmt.setLong(1, sequence);
            prepStmt.setString(2, tipoEvaluacion);
            prepStmt.setInt(3, Integer.valueOf(transactionId));
//            System.out.println("detalle.getReferencia()=" + detalle.getReferencia());
            prepStmt.setString(4, detalle.getReferencia());
//            System.out.println("detalle.getCodigoRetorno()=" + detalle.getCodigoRetorno());
            prepStmt.setString(5, detalle.getCodigoRetorno());
            prepStmt.setString(6, detalle.getMensaje());
            prepStmt.setDate(7, new java.sql.Date(new java.util.Date().getTime()));
//            System.out.println("************* TIME hora " + new java.sql.Time(new java.util.Date().getTime()));
            prepStmt.setString(8, horaInicio);
            //prepStmt.setTime(8, new java.sql.Time(new java.util.Date().getTime()));
            prepStmt.setDate(9, new java.sql.Date(new java.util.Date().getTime()));
//                System.out.println("************* TIME hora " + new java.sql.Time(new java.util.Date().getTime()));
            LocalDateTime now2 = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            String formattedDateTime2 = now2.format(formatter);
            prepStmt.setString(10, formattedDateTime2);
            Duration duration = Duration.between(now, now2);
            double hourDifference = duration.toMillis();
//            System.out.println("tiempo que tardo en procesar oriinal =" + hourDifference);
            hourDifference = hourDifference / 1000.000;
            prepStmt.setDouble(11, hourDifference);
            prepStmt.setString(12, evaluacionLista);
            prepStmt.setString(13, "?");
            prepStmt.setString(14, "?");
            prepStmt.setString(15, "?");
            prepStmt.setString(16, "?");
            prepStmt.setString(17, "?");
            prepStmt.setString(18, "?");
            prepStmt.setString(19, "?");
            prepStmt.setString(20, "?");
            prepStmt.setString(21, "?");
            prepStmt.setDate(22, java.sql.Date.valueOf("1753-01-01"));
            prepStmt.setString(23, "?");
            prepStmt.setString(24, "?");
            prepStmt.setString(25, "?");
            prepStmt.setString(26, "?");
            prepStmt.setDouble(27, 0.0);            
//            System.out.println("tiempo que tardo en procesar / 1000 =" + hourDifference);
//            System.out.println("Por insertar sequence : " + sequence);
//            System.out.println("Por insertar tipoEvaluacion : " + tipoEvaluacion);
//            System.out.println("Por insertar nteger.valueOf(transactionId) : " + Integer.valueOf(transactionId));
            numberOfRowsInserted = prepStmt.executeUpdate();
            System.out.println("numberOfRowsInserted : EvaluacionLista : " + numberOfRowsInserted + " -- "+evaluacionLista);
//            prepStmt.close(); // close PreparedStatement
//                        con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(InsertaEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SQLException : " + ex.getMessage());
        }
        return numberOfRowsInserted;
    }
}
