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

/**
 *
 * @author oargueta
 */
public class NombresListas {
    RespuestaNombresListasNegras deta = new RespuestaNombresListasNegras();
    double similarity = 0;

    public RespuestaNombresListasNegras BuscarNombreListaNic(String nombre, String nombreCampo, String nombreTabla) {
//        String input = "MARIA EUSEBIA L J";
//        String tableName = "dbo.LISTAS_NIC";
//        String fieldName = "Nombre";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish a database connection
            connection = DatabaseConnection.getConnection();

            // Define the SQL query to retrieve values from the specified table field
            String sqlQuery = "SELECT "+nombreCampo+" FROM dbo."+nombreTabla+"";
//            String sqlQuery = "SELECT Nombre FROM " + tableName + " where Nombre like '%"+input+"%'";
            System.out.println("sqlQuery  '" + sqlQuery);
            // Create a PreparedStatement
            preparedStatement = connection.prepareStatement(sqlQuery);

            // Execute the query
            resultSet = preparedStatement.executeQuery();
            int n = 0;
            while (resultSet.next()) {
                String fieldValue = resultSet.getString(1);
//                double similarity = jaccardSimilarity(input, fieldValue);
//                System.out.println("Similarity0 between '" + input + "' and '" + fieldValue + "': " + similarity0);
                similarity = computeLevenshteinSimilarity(nombre, fieldValue);
                deta.setNombreLista("");
                if (similarity >= 0.62) {
                    System.out.println("Similarity1 between '" + nombre + "' and '" + fieldValue + "': " + similarity);
                    deta.setNombreLista(fieldValue);
                    deta.setValorSimilitud(similarity);
                    n++;
                    return deta;
                }

            }
            
            System.out.println("Total registros procesados :  '" + n);

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
