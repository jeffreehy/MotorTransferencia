/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import com.nic.ba.db.DatabaseConnection;
import com.nic.resp.RespuestaNombresListasNegras;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author oargueta
 */
public class NombresListaVWONU_ALL {

    double similarity = 0;
    int swx = 0;
    RespuestaNombresListasNegras deta = new RespuestaNombresListasNegras();

    public RespuestaNombresListasNegras BuscarNombreListaVWONU_ALL(long sequence, String nombre, String ParametroValorDato, String unoOvarios, Connection con, Connection connection,String horaInicio, LocalDateTime now, String actor, String ParametroValorDatoPorcentaje) {
//        String input = "MARIA EUSEBIA L J";
//        String tableName = "dbo.LISTAS_NIC";
//        String fieldName = "Nombre";

//        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InsertaEvaluacionDetalle inserta = new InsertaEvaluacionDetalle();

        try {
            // Establish a database connection
//            connection = DatabaseConnection.getConnection();
//SELECT *  FROM DB_LISTAS.dbo.VWONU_ALL where      vwname1 + ' ' + vwname2+' '+ vwname3 +' ' + vwname4                             

//in ('MOHAMMAD HASSAN AKHUND ', 'MOHAMMAD ABBAS AKHUND', 'MOHAMMAD AMAN AKHUND ')
            // Define the SQL query to retrieve valu es from the specified table field
            String sqlQuery = "SELECT vwname1 + ' ' + vwname2+' '+ vwname3 +' ' + vwname4  FROM dbo.VWONU_ALL";//
//            String sqlQuery = "SELECT vwname1 + ' ' + vwname2+' '+ vwname3 +' ' + vwname4  FROM dbo.VWONU_ALL where vwname1 + ' ' + vwname2+' '+ vwname3 +' ' + vwname4 in ('MOHAMMAD HASSAN AKHUND ', 'MOHAMMAD ABBAS AKHUND', 'MOHAMMAD AMAN AKHUND ')";
//            String sqlQuery = "SELECT Nombre FROM " + tableName + " where Nombre like '%"+input+"%'";
//            System.out.println("sqlQuery  '" + sqlQuery);
            // Create a PreparedStatement
            preparedStatement = connection.prepareStatement(sqlQuery);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            int n = 0;
            if (unoOvarios.equals("uno")) {
                while (resultSet.next()) {
                    String nombreLista = resultSet.getString(1);
//                double similarity = jaccardSimilarity(input, fieldValue);
//                System.out.println("Similarity0 between '" + input + "' and '" + fieldValue + "': " + similarity0);
                    similarity = computeLevenshteinSimilarity(nombre, nombreLista);
//                    System.out.println("ParametroValorDato uno antes'" + ParametroValorDato + "' and '" + nombreLista + "': " + similarity);
                    deta.setNombreLista("OK");
                    if (similarity >= Double.valueOf(ParametroValorDato)) {
                        //if (similarity >= 0.7083333333333333) {
//                        System.out.println("Similarity1 between uno'" + nombre + "' and '" + nombreLista + "': " + similarity);
//                        System.out.println("Similarity1 between '" + nombre + "' and '" + nombreLista + "': " + similarity);
//                        System.out.println("ParametroValorDato uno despues'" + ParametroValorDato + "' and '" + nombreLista + "': " + similarity);
                        deta.setNombreLista(nombreLista);
                        deta.setValorSimilitud(similarity);
                        n++;
                        return deta;
                    }

                }
            } else {
                int contadorExiste = 0;
//                if (!SINO.equals("SI")) {
                while (resultSet.next()) {
                    String nombreLista = resultSet.getString(1);
//                double similarity = jaccardSimilarity(input, fieldValue);
////                System.out.println("Similarity0 between '" + input + "' and '" + fieldValue + "': " + similarity0);
                    similarity = computeLevenshteinSimilarity(nombre, nombreLista);
                    deta.setNombreLista("OK");

                    if (similarity >= Double.valueOf(ParametroValorDatoPorcentaje)) {

//                if (similarity >= 0.7083333333333333) {
//                        System.out.println("Similarity1 between '" + nombre + "' and '" + nombreLista + "': " + similarity);
                        deta.setNombreLista(nombreLista);
                        deta.setValorSimilitud(similarity);
                        deta.setExiste("SI");
//                        n++;
//                        int sequence = secu.getSecuencia(con);
                        if (similarity >= Double.valueOf(ParametroValorDato)) {
                            n++;
                            long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, nombreLista, horaInicio, now, con, actor, deta.getValorSimilitud(), "CN");
                            contadorExiste++;
                        } else {
                            if (similarity >= Double.valueOf(ParametroValorDatoPorcentaje)) {
                                n++;
                                long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, nombreLista, horaInicio, now, con, actor, deta.getValorSimilitud(), "OK");
                                contadorExiste++;
                            }

                        }

//                        return deta;
                    }
//                    if (n == 1) {
//                        n++;
//                    }

                }
                if (contadorExiste++ == 0) {
                    n++;
                    long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, "?", horaInicio, now, con, actor, deta.getValorSimilitud(), "OK");
                }
            }

//            System.out.println("Total registros procesados :  '" + n);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close database resources
//            try {
//                if (resultSet != null) {
//                    resultSet.close();
//                }
//                if (preparedStatement != null) {
//                    preparedStatement.close();
//                }
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
        return deta;
    }

    public static double computeLevenshteinSimilarity(String inputString, String fieldValue) {
        int inputLength = inputString.length();
        int fieldValueLength = fieldValue.length();

        int[][] distanceMatrix = new int[inputLength + 1][fieldValueLength + 1];

        for (int i = 0; i <= inputLength; i++) {
            distanceMatrix[i][0] = i;
        }

        for (int j = 0; j <= fieldValueLength; j++) {
            distanceMatrix[0][j] = j;
        }

        for (int i = 1; i <= inputLength; i++) {
            for (int j = 1; j <= fieldValueLength; j++) {
                int cost = (inputString.charAt(i - 1) == fieldValue.charAt(j - 1)) ? 0 : 1;
                distanceMatrix[i][j] = Math.min(
                        Math.min(distanceMatrix[i - 1][j] + 1, distanceMatrix[i][j - 1] + 1),
                        distanceMatrix[i - 1][j - 1] + cost
                );
            }
        }

        int maxLen = Math.max(inputLength, fieldValueLength);
        return 1.0 - (double) distanceMatrix[inputLength][fieldValueLength] / maxLen;
    }
}
