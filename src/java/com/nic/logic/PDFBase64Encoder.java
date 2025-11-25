/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

/**
 *
 * @author Jeffreehy Talavera
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class PDFBase64Encoder {

    // Method to convert PDF to Base64 encoded string
    public static String encodePDFToBase64(String filePath) {
        String base64String = "";
        try {
            // Read the file as a byte array
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] pdfData = new byte[(int) file.length()];
            fileInputStream.read(pdfData);
            fileInputStream.close();

            // Encode the byte array to Base64 string
            base64String = Base64.getEncoder().encodeToString(pdfData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }

    public static void main(String[] args) {
        // Path to the PDF file
        String pdfFilePath = "C:\\jeta\\Archivo prueba MT103 PDF.pdf";

        // Encode the PDF file to Base64
        String encodedString = encodePDFToBase64(pdfFilePath);
        System.out.println("Base64 Encoded PDF: " + encodedString);
    }
}
