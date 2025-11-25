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
public class NombresListaNic {

    RespuestaNombresListasNegras deta = new RespuestaNombresListasNegras();
    double similarity = 0;
    int swx = 0;

    public RespuestaNombresListasNegras BuscarNombreListaNic(long sequence, String nombre, String ParametroValorDato, String unoOvarios, Connection con, Connection connection, String horaInicio, LocalDateTime now, String actor, String ParametroValorDatoPorcentaje) {
//        String input = "MARIA EUSEBIA L J";
//        String tableName = "dbo.LISTAS_NIC";
//        String fieldName = "Nombre";

//        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        InsertaEvaluacionDetalle inserta = new InsertaEvaluacionDetalle();
        ObtieneSecuencia secu = new ObtieneSecuencia();
        try {
            // Establish a database connection
//            connection = DatabaseConnection.getConnection();
// TOP (10)
            // Define the SQL query to retrieve values from the specified table field
            String sqlQuery = "SELECT Nombre FROM dbo.LISTAS_NIC";
//String sqlQuery = "SELECT TOP (10) Nombre FROM dbo.LISTAS_NIC";
//            String sqlQuery = "SELECT Nombre FROM " + tableName + " where Nombre like '%"+input+"%'";
//            System.out.println("sqlQuery  '" + sqlQuery);
            // Create a PreparedStatement
            preparedStatement = connection.prepareStatement(sqlQuery);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            int n = 0;
            if (unoOvarios.equals("uno")) {
//                System.out.println("Entro listas NIC en  : uno '");
                while (resultSet.next()) {
                    String nombreLista = resultSet.getString(1);
//                double similarity = jaccardSimilarity(input, fieldValue);
//                System.out.println("Similarity0 between '" + input + "' and '" + fieldValue + "': " + similarity0);
                    similarity = computeLevenshteinSimilarity(nombre, nombreLista);
//                    deta.setNombreLista("OK");

                    if (similarity >= Double.valueOf(ParametroValorDato)) {
//                        System.out.println("if Similaridad  '" + similarity + "' ParametroValordato '" + ParametroValorDato);
//                        System.out.println("entro al if");
//                if (similarity >= 0.7083333333333333) {
//                        System.out.println("Similarity between '" + nombre + "' and '" + nombreLista + "': " + similarity);
                        deta.setNombreLista(nombreLista);
                        deta.setValorSimilitud(similarity);
                        n++;
                        return deta;
                    }

                }
            } else {
//                System.out.println("Entro listas Nic en varios");
                int contadorExiste = 0;
//                if (!SINO.equals("SI")) {
//                System.out.println("Antes del while");
                while (resultSet.next()) {
//                    System.out.println("Despues del while");
                    String nombreLista = resultSet.getString(1);
                    similarity = computeLevenshteinSimilarity(nombre, nombreLista);
                    deta.setNombreLista("OK");
//                    System.out.println("if Similarity  '" + similarity + "' ParametroValorDatsPorcentaje '" + ParametroValorDatoPorcentaje);
                    if (similarity >= Double.valueOf(ParametroValorDatoPorcentaje)) {
//                        System.out.println("entro al if Similarity1 ");
//                if (similarity >= 0.7083333333333333) {
//                        System.out.println("Similarity between '" + nombre + "' and '" + nombreLista + "': " + similarity);
                        deta.setNombreLista(nombreLista);
                        deta.setValorSimilitud(similarity);
                        deta.setExiste("SI");
//                        n++;
//                        int sequence = secu.getSecuencia(con);
//                        System.out.println("Similarity  '" + similarity + "' ParametroValorDato '" + ParametroValorDato);
                        if (similarity >= Double.valueOf(ParametroValorDato)) {
//                            System.out.println("entro if Similarity1 ");
                            n++;
                            long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, nombreLista, horaInicio, now, con, actor, deta.getValorSimilitud(), "CN");
                            contadorExiste++;
                        } else {
//                            System.out.println("Entro else ");
//                            System.out.println("if Similarity between '" + similarity + "' ParametroValorDato '" + ParametroValorDatoPorcentaje);
                            if (similarity >= Double.valueOf(ParametroValorDatoPorcentaje)) {
//                                System.out.println("entro al if S '");
                                n++;
                                long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, "?", horaInicio, now, con, actor, deta.getValorSimilitud(), "OK");
                                contadorExiste++;
                            }

                        }

//                        return deta;
                    }
//                    System.out.println("Jaaaaa no hubo similaridad ");
//                    if(n==1){
//                        n++;
//                    }

                }
                if (contadorExiste++ == 0) {
//                    System.out.println("UPA entro al insert asaber. ");
                    n++;
                    long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, "?", horaInicio, now, con, actor, deta.getValorSimilitud(), "OK");
                }
//                } else {
//                    if (swx == 0) {
//                        n++;
//                        long InsertaResultado = inserta.insertaEvaluacionDetalle(sequence, n, nombre, "?", horaInicio, now, con, actor, deta.getValorSimilitud(), "OK");
//                        swx = 1;
//                        deta.setExiste("NO");
//                    }
//                }

//                       
//                    }
//
//                }
            }
//            System.out.println("Total registros procesados :  '" + n);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close database resources
//            try {
//                if (resultSet != null) {
////                    resultSet.close();
//                }
//                if (preparedStatement != null) {
////                    preparedStatement.close();
//                }
//                if (connection != null) {
////                    connection.close();
////                }
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
