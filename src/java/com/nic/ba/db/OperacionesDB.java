/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.ba.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author Jeffreehy
 */

public class OperacionesDB {

    Statement stmt;
    CallableStatement cstmt;
    PreparedStatement pstmt;
    ResultSet rs;

    Logger log = Logger.getLogger(this.getClass());
    
    public boolean conectado = false;
    
    public OperacionesDB(){
        
    }
        
    public Connection connectDB(String usuario, String clave, String url, String driver) {
        Connection con = null;
        conectado = false;
        //cargaPropiedades();
        try {
       
            Class.forName(driver);
            System.out.println("url, usuario, clave, driverDB " +url+ " -- "+usuario+" -- "+clave+" -- ");
            con = DriverManager.getConnection(url, usuario, clave);
            System.out.println("Conecto 1 yes ********** " );
        } catch (Exception ex) {
//            log.error("ERROR En Coneccion Base de Datos, Consulte Atenciön al cliente " + ex.getMessage());
            conectado = false;
            return con;
        }
        conectado = true;
        return con;
    }
       
    public CallableStatement ejecutaProcedimiento(ArrayList datos, String query, Connection cnn, boolean registros) {
        //rs = null;
        cstmt = null;
        query = preparaSql(query, datos);
        try {           
            cstmt = cnn.prepareCall(query);
            cstmt.clearParameters();
                        
            for (int i = 0; i < datos.size(); i++) {
                String[] valores = (String[]) datos.get(i);         //Obtenemos los valores
                if (valores[0].equalsIgnoreCase("int")) {           //Se evalua el tipo de dato
                    if (valores[2].equalsIgnoreCase("IN")){         //Se evalua que tipo de parametros es
                        cstmt.setInt(i + 1, Integer.parseInt(valores[1]));
                    }else{                                                                        //Parametros de salida
                        cstmt.registerOutParameter(i + 1, Types.INTEGER);
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("short")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setShort(i + 1, Short.parseShort(valores[1]));
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.INTEGER);   
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("double")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setDouble(i + 1, Double.parseDouble(valores[1]));
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.DECIMAL);
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("long")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setLong(i + 1, Long.parseLong(valores[1])); 
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.BIGINT);
                        //cstmt.registerOutParameter(i + 1, -10);
                    }
                }else if (valores[0].equalsIgnoreCase("string")) {
                    if (valores[2].equalsIgnoreCase("IN")){
                        cstmt.setString(i + 1, valores[1]);
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.VARCHAR);
                    }
                }else if (valores[0].equalsIgnoreCase("bool")){
                    if (valores[2].equalsIgnoreCase("IN")){
                        if (valores[1].equalsIgnoreCase("true") || valores[1].equalsIgnoreCase("1")){
                             cstmt.setBoolean(i + 1, true);
                        }else
                             cstmt.setBoolean(i + 1, false);
                    }else{
                        cstmt.registerOutParameter(i + 1, Types.BOOLEAN);
                    }
                }           
            }
            if (registros)
                cstmt.executeQuery();
            else
                cstmt.execute();
            
        } catch (SQLException ex) {
//            log.error("Error ejecutando procedimiento... ["+ query + "] " + ex );  
            System.out.println("Error ejecutando procedimiento : "+ex);
                    
        }
        
        return cstmt;
    }
    
    public ResultSet ejecutaQuery(String sql, ArrayList datos, Connection cnn) {
        rs = null;
       
        try {
            //pstmt = cnn.prepareCall(sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            
            for (int i=0; i< datos.size(); i++){
                String[] valores = (String[]) datos.get(i);
                
                if (valores[0].equalsIgnoreCase("int")){
                    pstmt.setInt(i + 1, Integer.parseInt(valores[1]));
                }else if (valores[0].equalsIgnoreCase("short")) {
                    pstmt.setShort(i + 1, Short.parseShort(valores[1]));
                }else if (valores[0].equalsIgnoreCase("double")) {
                    pstmt.setDouble(i + 1, Double.parseDouble(valores[1]));
                }else if (valores[0].equalsIgnoreCase("long")) {
                    pstmt.setLong(i + 1, Long.parseLong(valores[1])); 
                }else if (valores[0].equalsIgnoreCase("string")) {
                    pstmt.setString(i + 1, valores[1]);
                }else if (valores[0].equalsIgnoreCase("bool")){
                     if (valores[1].equalsIgnoreCase("true") || valores[1].equalsIgnoreCase("1")){
                         pstmt.setBoolean(i + 1, true);
                    }else
                         pstmt.setBoolean(i + 1, false);
                }              
            }
            
            rs = pstmt.executeQuery();              
        } catch (SQLException ex) {
//            log.error("No se puedo ejecutar el query " + ex.getMessage());
            //log.fatal("No se pudo ejecutar el Query - ["+ sql + "] " + ex.getMessage());
        }
        return rs;
    }        

    private String preparaSql(String sp, ArrayList datos){
        String sql = "";
        int tamano = datos.size();
        
        sql = "{call " + sp + "(";
        for (int i = 1; i <= tamano; i++) {
            if (tamano == 1) {
                sql = sql + "?";
                break;
            }
            if (i < tamano) {
                sql = sql + "?,";
            } else {
                sql = sql + "?";
            }
        }
        sql = sql + ")}";
        return sql;
    }
}
