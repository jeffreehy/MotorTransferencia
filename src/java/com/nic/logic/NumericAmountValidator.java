/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.logic;

import java.text.Normalizer;

/**
 *
 * @author Jeffreehy Talavera
 */
public class NumericAmountValidator {



    // Method to sanitize the string by replacing special characters and accented vowels
    public static String sanitizeString(String input) {
        // Normalize the string to decompose accented characters into base characters
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Remove accents by keeping only ASCII letters and numbers
        String withoutAccents = normalized.replaceAll("[ñ+]", "n+");

        // Replace any non-alphanumeric characters with a blank space
//        String sanitized = withoutAccents.replaceAll("[0-9,.+-()/]", " ");
        String sanitized = withoutAccents.replaceAll("[^0-9,.+-/()]", " ");

        return sanitized;
    }

//    public static void main(String[] args) {
//        // Test cases
//        String test1 = "Café 123!@#45.678,90////";  // "Cafe 123   "
//        String test2 = "Niño?&%$AbC";  // "Nino   AbC"
//        String test3 = "Tést@Exámple"; // "Test Example"
//        String test4 = "123 ! @ # $ % ^ & * _ =  \\ | [ ] { } ;,,,... : ? > 456 á é í ó ú ü ñ 7890< > = + - () × ÷ √ ∑ ∏ $ € £ ¥ abcdefghijklmnopqrstuvwxyzABDE"; 
//
//        System.out.println("PRUEBA 1 : "+sanitizeString(test1)); // Output: "Cafe 123   "
//        System.out.println("PRUEBA 2 : "+sanitizeString(test2)); // Output: "Nino   AbC"
//        System.out.println("PRUEBA 3 : "+sanitizeString(test3)); // Output: "Test Example"
//        System.out.println("PRUEBA 4 : "+sanitizeString(test4));
//    }
}

