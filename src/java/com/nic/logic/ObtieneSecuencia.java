/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import com.nic.ba.db.OperacionesDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oargueta
 */
public class ObtieneSecuencia {

    public Integer getSecuencia(Connection con) {
        try {
            CallableStatement cstmt = null;
            OperacionesDB operDB = new OperacionesDB();
            String sql = "dbo.NoEvaluacion_Get";
            ArrayList datos = new ArrayList();
//            System.out.println("Dato 0  Aqui voy ********** ");
            datos.add(new String[]{"String", "outEvaluacion", "OUT"});
//            System.out.println("EjecutaProcedimiento dbo.NoEvaluacion_Get : " + sql);
//                cstmt.registerOutParameter(1, java.sql.Types.NVARCHAR);
            cstmt = operDB.ejecutaProcedimiento(datos, sql, con, false);
//            System.out.println("operDB.ejecutaProcedimiento regreso");
//            System.out.println("MANAGER ID: " + cstmt.getString(1));
            int sequence = Integer.valueOf(cstmt.getString(1));
//                rs1 = cstmt.getResultSet();
            System.out.println("Extrayendo datos de la consulta..");
            return sequence;
        } catch (SQLException ex) {
            System.out.println("SQLException : " + ex.getMessage());
            Logger.getLogger(ObtieneSecuencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
