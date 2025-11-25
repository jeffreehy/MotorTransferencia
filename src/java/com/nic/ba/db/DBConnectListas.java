package com.nic.ba.db;


 
 import java.sql.Connection;
 import java.sql.SQLException;
 import javax.naming.InitialContext;
 import javax.naming.NamingException;
 import javax.sql.DataSource;
 

 public class DBConnectListas
 {
   public Connection obtenerConeccion() {
/* 35 */     DataSource ds = null;
/* 36 */     Connection connection = null;
     try {
/* 38 */       InitialContext ctx = null;
       try {
/* 40 */         ctx = new InitialContext();
/* 41 */       } catch (NamingException ex) {
/* 42 */         return connection;
       } 
/* 44 */       System.out.println("********** POR CONECTAR **********");
       try {//MSSQLDSMOVIMAHOCQ
/* 46 */         ds = (DataSource)ctx.lookup("java:/MSSQLDSListas");    // este es para JBoss pruebas y produccion
//        ds = (DataSource)ctx.lookup("jdbc/MSSQLDSListas");  // este es para hacer pruebas con Glassfich en desarrollo
/* 47 */       } catch (NamingException ex) {
/* 48 */         System.out.println("ERROR AL TRATAR DE CONECTAR ************" + ex);
/* 49 */         return connection;
       } 
       try {
/* 52 */         connection = ds.getConnection();
/* 53 */         System.out.println("************CONECTO OK *******");
/* 54 */       } catch (SQLException ex) {
/* 55 */         System.out.println("ERROR AL CONECTAR ************" + ex);
/* 56 */         return connection;
       } 
/* 58 */       if (connection == null) {
/* 59 */         System.out.println("ERROR AL CONECTAR null************");
/* 60 */         return connection;
       } 
/* 62 */     } catch (Exception ex) {
/* 63 */       System.out.println(ex);
/* 64 */       return connection;
     } 
/* 66 */     return connection;
   }
 }


/* Location:              C:\jeta\Jeta Banco Atlantida\war file for ws movimientos TC\new war coneccion pool\war anterior favor no borrar\wsMovimientos.war!\WEB-INF\classes\tools\DBConnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */