/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nic.ba;

import com.nic.ba.core.BuscaInfoCuenta;
import com.nic.ba.db.DBConnectListas;
import com.nic.ba.db.DBConnectMotor;
import com.nic.ba.db.OperacionesDB;
import com.nic.logic.AlphaNumericValidator;
import com.nic.logic.EnviaNootificacion;
import com.nic.logic.InsertaEvaluacion;
import com.nic.logic.NombresListaNic;
import com.nic.logic.NombresListaVWCHIEFS;
import com.nic.logic.NombresListaVWOFAC_ALL;
import com.nic.logic.NombresListaVWONU_ALL;
import com.nic.logic.NumericAmountValidator;
import com.nic.logic.ObtieneSecuencia;
import com.nic.logic.ProcesaTransferenciaIn;
import com.nic.logic.SendParseRequestF9I8;
import com.nic.logic.SendParseRequestTags;
import com.nic.resp.RepuestaTransferencia;
import com.nic.resp.RespuestaNombresListasNegras;
import com.nic.resp.StatusItem;
import com.nic.resp.StatusGlobal;
import com.nic.resp.TransferenciaColeccion;
import com.nic.resp.DetalleColeccion;
import com.nic.resp.ParametrosAdicionales;
import com.nic.resp.RequestEvento;
import com.nic.resp.RespuestaCuenta;
import com.nic.resp.RespuestaEvento;
import com.nic.resp.RespuestaGeneraTransferenciaMT103;
import com.nic.resp.RespuestaTransferenciaMT103;
import com.nic.resp.ValidarTags;
import com.nic.resp.parametroAdicionalColeccion;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import motortransferencia.ArrayOfSDTTransaccionTag;
import motortransferencia.SDTTransaccionEvento;
import motortransferencia.SDTTransaccionTag;
import motortransferencia.WsInsertTransactionTags;
import webservicestc.SDTPeticion;
import webservicestc.SDTPeticionHeader;
import webservicestc.SDTWSNICClientePeticion;
import webservicestc.SDTWSNICDebitoCreditoPeticion;
//import webservicestc.SDTWSNICClientePeticion;
//import webservicestc.SDTWSNICF9I4Peticion;
//import webservicestc.SDTWSNICF9I8Peticion;
import webservicestc.SDTWSNICPPSwiftPeticion;
import webservicestc.SDTWSNICTasaCambioPeticion;
import webservicestc.SDTWSNICTasaCambioRespuestaDetalle;

/**
 *
 * @author jtalaverad
 */
@WebService(serviceName = "wsTransferencias")
public class wsTransferencias {

    int i = 0;
    int xx = 0;
    NumericAmountValidator valida = new NumericAmountValidator();
    AlphaNumericValidator validaString = new AlphaNumericValidator();
    ArrayList<String> LineaAdicional = new ArrayList<>();
    ArrayList<String> TipoRegistroAdicional = new ArrayList<>();
    ArrayList<String> ValorAdicional = new ArrayList<>();

//===============================================
    ArrayList<String> Accion = new ArrayList<>();
    ArrayList<String> FechaHora = new ArrayList<>();
    ArrayList<String> Linea = new ArrayList<>();
    ArrayList<String> TipoAccion = new ArrayList<>();
    ArrayList<String> Campo = new ArrayList<>();
    ArrayList<String> Dato = new ArrayList<>();
    ArrayList<String> TipoCampo = new ArrayList<>();
    RespuestaCuenta deta10 = new RespuestaCuenta();
    RespuestaCuenta deta11 = new RespuestaCuenta();
    ResultSet rs = null;
    int sw = 0;
    int sw00 = 0;
    String text = "";
    String sql = null;
    String coincidencia = "";
    String valorCoincidencia = "";
    String Nocoincidencia = "";
    String NovalorCoincidencia = "";
    String valorCoincidenciaECX = "?";
    String transaccionComentario5 = "?";
    String nombre40oMenos = "?";
    String ERROR = "?";
    String valorCoincidenciaICI = "?";
    RequestEvento req = new RequestEvento();
    RespuestaEvento respEvento = new RespuestaEvento();
    XMLGregorianCalendar HoraInicioAplicativo = null;
    XMLGregorianCalendar HoraInicioEvento = null;
    XMLGregorianCalendar HoraFinalizaEvento = null;
    Boolean TranDictamenTipo = false;
    String ParametroValorDatoFase = "";
    static double TasaCambioM = 0.00;
    static double TasaCambioC = 0.00;
    static double TasaCambioV = 0.00;
    static double valorCordobas = 0.00;
//<editor-fold defaultstate="collapsed" desc="public method: Procesar Transferencia Valida datos : procesaTransferencia">

    @WebMethod(operationName = "procesaTransferencia")
    public ArrayList<RepuestaTransferencia> processTransferencia(@WebParam(name = "transactionId") String transactionId,
            @WebParam(name = "aplicationId") String aplicationId,
            @WebParam(name = "paisId") String paisId,
            @WebParam(name = "empresaId") String empresaId,
            @WebParam(name = "regionId") String regionId,
            @WebParam(name = "canalId") String canalId,
            @WebParam(name = "version") String version,
            @WebParam(name = "llaveSesion") String llaveSesion,
            @WebParam(name = "usuarioId") String usuarioId,
            @WebParam(name = "accion") String accion,
            @WebParam(name = "token") String token,
            @WebParam(name = "clienteCoreId") String clienteCoreId,
            @WebParam(name = "TipoIdentificacion") String TipoIdentificacion,
            @WebParam(name = "identificacion") String identificacion,
            @WebParam(name = "terminal") String terminal,
            @WebParam(name = "pantalla") String pantalla,
            @WebParam(name = "paso") String paso,
            @WebParam(name = "parametroAdicional") parametroAdicionalColeccion parametroAdicional,
            @WebParam(name = "TransferenciaColeccion") TransferenciaColeccion TransferenciaColeccion) {
        System.out.println("**** Metodo  *****       processTransferencia ");//
        int Estado = 0;
        String mensaje = null;
        String nombre = null;
        String Nombre = null;
        String ACCOUNT = null;
        String NombreBeneficiario = null;
        String NombreBeneficiarioCompleto = null;
        String NumCliente = null;
        String Id = null;
        String TipoId = null;
        String TipoPersona = null;
        String CodPaisOrdenante = null;
        String CodPaisBeneficiario = null;
        String CodPaisIntermediario = null;
        String DesPais = null;
        String Direccion = null;
        String Telefono = null;
        String Correo = null;
        String SQL = null;
        Statement stmt = null;
        //jeta 15 septiembre 2024
        String NUMERO_CUENTA = "?";
        String NOMBRE_ORDENANTE = "?";
        String DIRECCION_ORDENANTE1 = "?";
        String DIRECCION_ORDENANTE2 = "?";
        String DIRECCION_ORDENANTE3 = "?";
        String respuesta = null;
        String ParametroCodigo = null;
        Connection con = null;
        Connection con1 = null;
        String ParametroValorDato = null;
        String ParametroValorDatoPorcentaje = null;
        String Valor = null;
        String ValorORI = null;
        String FechaTransferencia = "";
        String MonedaTransferencia = "";
        String MonedaTransferenciaORI = "";
        double totalValor = 0.0;
        RepuestaTransferencia deta = new RepuestaTransferencia();
        DetalleColeccion deta00 = new DetalleColeccion();
        StatusGlobal deta0 = new StatusGlobal();
        StatusItem detalleColeccion = new StatusItem();
        ArrayList<DetalleColeccion> detax = new ArrayList<>();
        ArrayList<StatusItem> deta1 = new ArrayList<>();
        ArrayList<RepuestaTransferencia> response = new ArrayList<>();
        ArrayList<StatusGlobal> deta2 = new ArrayList<>();
        RespuestaNombresListasNegras deta3 = new RespuestaNombresListasNegras();
        System.out.println("**** Accion *****  processTransferencia            " + accion);//
        System.out.println("**** transactionId *****        " + transactionId);
        System.out.println("**** aplicationId *****         " + aplicationId);
        System.out.println("**** paisId *****               " + paisId);
        System.out.println("**** empresaId *****            " + empresaId);
        System.out.println("**** regionId *****             " + regionId);
        System.out.println("**** canalId *****              " + canalId);
        System.out.println("**** version *****              " + version);
        System.out.println("**** llaveSesion *****          " + llaveSesion);
        System.out.println("**** usuarioId *****            " + usuarioId);
        System.out.println("**** accion ***** processTransferencia              " + accion);
        System.out.println("**** token *****                " + token);
        System.out.println("**** clienteCoreId *****        " + clienteCoreId);
        System.out.println("**** TipoIdentificacion *****   " + TipoIdentificacion);
        System.out.println("**** identificacion *****       " + identificacion);
        System.out.println("**** terminal *****             " + terminal);
        System.out.println("**** pantalla *****             " + pantalla);
        System.out.println("**** paso *****                 " + paso);

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new java.util.Date());

        try {
            HoraInicioAplicativo = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(cal);
            System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
        } catch (DatatypeConfigurationException ex) {
//            Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("********** DatatypeConfigurationException ********** : " + ex);
        }

        ArrayList<ParametrosAdicionales> detay = new ArrayList<>();
        System.out.println("**** int i = 0; i < parametroAdicional.getParametroAdicionalItem().size()  ***** ");
        System.out.println("**** parametroAdicional.getParametroAdicionalItem().size()  ***** " + parametroAdicional.getParametroAdicionalItem().size());
        for (int i = 0; i < parametroAdicional.getParametroAdicionalItem().size(); i++) {
//            System.out.println("**** Accion ***** " + accion);//
            if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("CON")) {
                valorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                System.out.println("**** cuando es CON  ***** " + valorCoincidencia);//
            }
            if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("SIN")) {
                NovalorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
            }
            if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ICI")) {
                valorCoincidenciaICI = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                System.out.println("**** cuando es ICI  ***** " + valorCoincidenciaICI);//
            }
            if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ECX")) {
                valorCoincidenciaECX = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                System.out.println("**** cuando es ECX  ***** " + valorCoincidenciaECX);//
            }
            if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ERR")) {
                ERROR = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                System.out.println("**** cuando es ERR  ***** " + ERROR);//
            }
//                LineaAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getLinea());
//                TipoRegistroAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getTipoRegistro());
//                ValorAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getValor());
        }
        DBConnectMotor coneccionMotor = new DBConnectMotor();
        con = coneccionMotor.obtenerConeccion();

        if (con == null) {//TIPESTECI    
            System.out.println("**** la conexion es nula   ***** null ");//
//            sql = "update Transaccion set TransaccionGestionEstado = '" + valorCoincidenciaICI + "', TransaccionPaso = " + paso + "  where TransaccionSecuencia = " + transactionId;
            deta0.setCodigoRetornoGlobal("9998");
            deta0.setMensaje(valorCoincidenciaICI);
            deta0.setReferencia("");
            deta1.add(detalleColeccion);
            deta2.add(deta0);
//                deta.setDetalle(deta1);
            deta.setEstadoGlobal(deta2);
            deta00.setDetalleItem(deta1);
            detax.add(deta00);
            deta.setDetalleColeccion(detax);
            response.add(deta);
            Accion.clear();
            FechaHora.clear();
            Linea.clear();
            TipoAccion.clear();
            Campo.clear();
            Dato.clear();
            TipoCampo.clear();
            LineaAdicional.clear();
            TipoRegistroAdicional.clear();
            ValorAdicional.clear();
            return response;
        }

        SQL = "SELECT ParametroValorDato FROM [MotorTransferencia].[dbo].[ParametroValor]\n"
                + "where ParametroCodigo = 'PARAMBACKOFFICE' and ParametroValorCodigo = 'FASE'";
        System.out.println("********** SQL CodPaisOrdenante** : " + SQL);
        Statement st = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery(SQL);
            while (rs.next()) {
                ParametroValorDatoFase = rs.getString(1);
                System.out.println("********** ParametroValorDato  ** : " + ParametroValorDatoFase);
            }
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex1) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex1);
                    System.out.println("********** SQLException ********** : " + ex1);
                }
            }
            System.out.println("SQLException : " + ex.getMessage());
        }
        if (!ParametroValorDatoFase.equals("2")) {
            System.out.println("**** ParametroValorDatoFase   <> 2 ");//
            deta0.setCodigoRetornoGlobal("9998");
            deta0.setMensaje(valorCoincidenciaICI);
            deta0.setReferencia("");
            deta1.add(detalleColeccion);
            deta2.add(deta0);
            deta.setEstadoGlobal(deta2);
            deta00.setDetalleItem(deta1);
            detax.add(deta00);
            deta.setDetalleColeccion(detax);
            response.add(deta);
            Accion.clear();
            FechaHora.clear();
            Linea.clear();
            TipoAccion.clear();
            Campo.clear();
            Dato.clear();
            TipoCampo.clear();
            LineaAdicional.clear();
            TipoRegistroAdicional.clear();
            ValorAdicional.clear();
            try {
                con.close();
            } catch (SQLException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("********** SQLException ********** : " + ex);
            }
            return response;
        }

        if (accion.equals("I") && paso.equals("1") || accion.equals("i") && paso.equals("1")) {
            System.out.println("************* sw NO entro  ");
            detalleColeccion = new StatusItem();
            deta0.setCodigoRetornoGlobal("0000");
            deta0.setMensaje("Al parecer - todo va bien 👌💲");
            deta0.setReferencia("");
            deta1.add(detalleColeccion);
            deta2.add(deta0);
//                deta.setDetalleColeccion(DetalleColeccion);
            deta.setEstadoGlobal(deta2);
            deta00.setDetalleItem(deta1);
            detax.add(deta00);
            deta.setDetalleColeccion(detax);   //jeton
            response.add(deta);
            System.out.println("*** ejecuta Update 1 *****");
            String resp1 = update(con, transactionId, "TIPESTPVE", "2", "OK");
//                sql = "update Transaccion set TransaccionGestionEstado = '" + NovalorCoincidencia + "' where TransaccionSecuencia = " + transactionId;
//                sql = "update Transaccion set TransaccionGestionEstado = '" + NovalorCoincidencia + "', TransaccionPaso = " + paso + "  where TransaccionSecuencia = " + transactionId;
//                System.out.println("SQl del Update sw = 0: " + sql);
//                stmt = con.createStatement();
//                int count = stmt.executeUpdate(sql);
            req.setTransaccionSecuencia(Long.valueOf(transactionId));
            req.setTransaccionEventoSistema(HoraInicioAplicativo);
            System.out.println("********** HoraInicioAplicativo *9 ********* : " + HoraInicioAplicativo);
            req.setTransaccionEventoAccion("InvocaValidacion");
            req.setTipoEventoCodigo("INSERT");
            req.setTransaccionEventoInicio(HoraInicioAplicativo);
            req.setTransaccionUsuarioCodigo(usuarioId);
            req.setTransaccionEventoDispositivo(pantalla);
            req.setTransaccionEventoIP(terminal);
            req.setTransaccionEventoSession(llaveSesion);
            req.setTransaccionEventoComentario("Finaliza InvocaValidacion: " + deta0.getCodigoRetornoGlobal() + "|" + deta0.getMensaje());
            cal.setTime(new java.util.Date());
            try {
                HoraFinalizaEvento = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(cal);
            } catch (DatatypeConfigurationException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("********** DatatypeConfigurationException ********** : " + ex);
            }
            req.setTransaccionEventoFinal(HoraFinalizaEvento);
            req.setTransaccionEventoGestion("TIPESTPRO");
            req.setTransaccionEventoEstado("TIPESTACT");
            System.out.println("FIN Pendiente de Validacion");
            respEvento = insertaEvento(req);
            System.out.println("transactionId : " + transactionId);
            try {
                //                System.out.println("Updated queries: " + count);
                con.close();
            } catch (SQLException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("********** SQLException ********** : " + ex);
            }
            sw = 0;
            Accion.clear();
            FechaHora.clear();
            Linea.clear();
            TipoAccion.clear();
            Campo.clear();
            Dato.clear();
            TipoCampo.clear();
            LineaAdicional.clear();
            TipoRegistroAdicional.clear();
            ValorAdicional.clear();
            return response;
        }
//*****************************************************************************************************************
//        String jdbcUrl = "jdbc:sqlserver://10.128.14.143:65078;encrypt=true;databaseName=MotorTransferencia;user=DebAutomatico;password=ffrhv6*w@67FXmbQ;TrustServerCertificate=True;";
////            String conexion = "jdbc:sqlserver://10.128.14.143:65078;databaseName=MotorTransferencia";
//        System.out.println("paso2");
//        try {
//            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            con = DriverManager.getConnection(jdbcUrl);
//            System.out.println("Connected to SQL Server database");
//        } catch (ClassNotFoundException | SQLException ex) {
//            Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
//        }
        for (int i = 0; i < TransferenciaColeccion.getTransferenciaItems().size(); i++) {
            Accion.add(TransferenciaColeccion.getTransferenciaItems().get(i).getAccion());
            FechaHora.add(TransferenciaColeccion.getTransferenciaItems().get(i).getFechaHora());
            Linea.add(TransferenciaColeccion.getTransferenciaItems().get(i).getLinea());
            TipoAccion.add(TransferenciaColeccion.getTransferenciaItems().get(i).getTipoAccion());
        }
        ArrayList<String> Campo = new ArrayList<>();
        ArrayList<String> Dato = new ArrayList<>();
        ArrayList<String> TipoCampo = new ArrayList<>();
//        System.out.println("**** PASO5a ***** " + TransferenciaColeccion.getTransferenciaItems().size());
        for (int i = 0; i < TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size(); i++) {
//            System.out.println("**** PASO5b ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size());
//            System.out.println("**** PASO6 mas i***** " + i);

            Campo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getCampo());
            Dato.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getDato());
            TipoCampo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getTipoCampo());
            System.out.println("**** Aqui inicia parse de TransferenciaColeccion ***** " + CodPaisOrdenante);
//            if (accion.equals("I")) {

            BuscaInfoCuenta busca = new BuscaInfoCuenta();
            // Evaluacion pais ordenante tag = 52A
            if ((Campo.get(i).equals("52A") || Campo.get(i).equals("52D")) && TipoCampo.get(i).equals("COUNTRY")) {
                CodPaisOrdenante = Dato.get(i);
                System.out.println("**** CodPaisOrdenante ***** " + CodPaisOrdenante);
            }
            if ((Campo.get(i).equals("56A") || Campo.get(i).equals("56D")) && TipoCampo.get(i).equals("COUNTRY")) {
                CodPaisIntermediario = Dato.get(i);
                System.out.println("**** CodPaisIntermediario***** " + CodPaisIntermediario);
            }
            if ((Campo.get(i).equals("57A") || Campo.get(i).equals("57D")) && TipoCampo.get(i).equals("COUNTRY")) {
                CodPaisBeneficiario = Dato.get(i);
                System.out.println("**** CodPaisBeneficiario***** " + CodPaisBeneficiario);
            }
            if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("AMOUNT")) {
                Valor = Dato.get(i);
                Valor = valida.sanitizeString(Valor);
                System.out.println("**** Valor ***** " + Valor.trim());
            }
            if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("AMOUNTORI")) {
                ValorORI = Dato.get(i);
                ValorORI = valida.sanitizeString(ValorORI);
                System.out.println("**** ValorORI ***** " + ValorORI.trim());
            }
            if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("DATE")) {
                FechaTransferencia = Dato.get(i);
                System.out.println("**** FechaTransferencia ***** " + FechaTransferencia);
            }
            if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("CURRENCY")) {
                MonedaTransferencia = Dato.get(i);
                System.out.println("**** MonedaTransferencia ***** " + MonedaTransferencia);
            }
            if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("CURRENCYORI")) {
                MonedaTransferenciaORI = Dato.get(i);
                System.out.println("**** MonedaTransferencia ***** " + MonedaTransferenciaORI);
            }
            if (accion.equals("I") || accion.equals("i")) {
                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
                    Nombre = Dato.get(i);
                    Nombre = validaString.sanitizeString(Nombre);
                    System.out.println("**** NombreRemitente ***** " + Nombre);
                }
                if ((Campo.get(i).equals("59") && TipoCampo.get(i).equals("ACCOUNT")) || (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ACCOUNT"))) {
                    ACCOUNT = Dato.get(i);
                    deta10 = busca.BuscarInformacionCuenta(ACCOUNT);
                }
                if ((Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME")) || (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME"))) {
                    NombreBeneficiario = Dato.get(i);
                    NombreBeneficiarioCompleto = Dato.get(i);
                    System.out.println("**** NombreBeneficiario Antes ***** " + NombreBeneficiario);
//                    NombreBeneficiario = validaString.sanitizeString(NombreBeneficiario);
//                    System.out.println("**** NombreBeneficiario Despues ***** " + NombreBeneficiario);
                    if (NombreBeneficiario.equals(deta10.getNombreClienteBeneficiarioCore())) {
                        int n = 0;
                        n++;
                    } else {
                        System.out.println("**** NombreBeneficiario ***** " + NombreBeneficiario);
                        if (NombreBeneficiario.equals("") || NombreBeneficiario.equals("") || NombreBeneficiario.isEmpty()) {
                            NombreBeneficiario = deta10.getNombreClienteBeneficiarioCore();
                            NombreBeneficiarioCompleto = deta10.getNombreClienteBeneficiarioCore();
                            System.out.println("**** NombreBeneficiario ***** " + NombreBeneficiario);

                        }
                    }
//                   deta10 = busca.BuscarInformacionCuenta(ACCOUNT);
                }
            }

            if (accion.equals("O") || accion.equals("o")) {
                if ((Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME")) || (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME"))) {
                    NombreBeneficiario = Dato.get(i);
                    NombreBeneficiarioCompleto = Dato.get(i);
                    System.out.println("**** NombreBeneficiarioCompleto 1ra vez Antes verifica listas ***** " + NombreBeneficiarioCompleto);
                    System.out.println("**** NombreBeneficiario Antes verifica listas ***** " + NombreBeneficiario);
                    NombreBeneficiario = validaString.sanitizeString(NombreBeneficiario);
                    System.out.println("**** NombreBeneficiario Despues verifica listas ***** " + NombreBeneficiario);

                    int longit = NombreBeneficiario.length();
                    System.out.println("**** NombreBeneficiario longitud ***** " + longit);
                    if (longit >= 35) {
                        NombreBeneficiario = NombreBeneficiario.substring(0, 35);
                    } else {
                        NombreBeneficiario = NombreBeneficiario.substring(0, longit);
                    }

                    System.out.println("**** NombreBeneficiario Despues verifica listas ***** " + NombreBeneficiario);
                }
                if ((Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ACCOUNT")) || (Campo.get(i).equals("50A") && TipoCampo.get(i).equals("ACCOUNT"))) {
                    ACCOUNT = Dato.get(i);
                    deta11 = busca.BuscarInformacionCuenta(ACCOUNT);
                }
                if ((Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) || (Campo.get(i).equals("50A") && TipoCampo.get(i).equals("NAME"))) {
                    Nombre = Dato.get(i);
                    if (Nombre.equals(deta11.getNombreClienteRemitenteCore())) {
                        int n = 0;
                        n++;
                    } else {
                        System.out.println("**** NombreRemitente ***** " + Nombre);
                        if (Nombre.equals("") || Nombre.equals("") || Nombre.isEmpty()) {
                            Nombre = deta11.getNombreClienteRemitenteCore();
                            System.out.println("**** Nombre ***** " + Nombre);

                        }
                    }
                    //jeta 15 septiembre 2024 begin
                    Nombre = validaString.sanitizeString(Nombre);
                    System.out.println("**** NombreRemitente despues de valida caracteres ***** " + Nombre);
                    //jeta 15 septiembre 2024 end
                }
                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ACCOUNT")) {
                    NUMERO_CUENTA = Dato.get(i);
                    //jeta 15 septiembre 2024 begin
                    NUMERO_CUENTA = validaString.sanitizeString(NUMERO_CUENTA);
                    System.out.println("**** ACCOUNT  despues de valida caracteres ***** " + NUMERO_CUENTA);
                    //jeta 15 septiembre 2024 end
                }

                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
                    NOMBRE_ORDENANTE = Dato.get(i);
                    //jeta 15 septiembre 2024 beg
                    NOMBRE_ORDENANTE = validaString.sanitizeString(NOMBRE_ORDENANTE);
                    System.out.println("**** NOMBRE_ORDENANTE  despues de valida caracteres ***** " + NOMBRE_ORDENANTE);
                    //jeta 15 septiembre 2024 end
                }
                //jeta 15 septiembre 2024 beg
                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                    DIRECCION_ORDENANTE1 = Dato.get(i);
                    DIRECCION_ORDENANTE1 = validaString.sanitizeString(DIRECCION_ORDENANTE1);
                    System.out.println("**** DIRECCION_ORDENANTE1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_ORDENANTE1);
                }
                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                    DIRECCION_ORDENANTE2 = Dato.get(i);
                    DIRECCION_ORDENANTE2 = validaString.sanitizeString(DIRECCION_ORDENANTE2);
                    System.out.println("**** DIRECCION_ORDENANTE2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_ORDENANTE2);

                }
                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                    DIRECCION_ORDENANTE3 = Dato.get(i);
                    DIRECCION_ORDENANTE3 = validaString.sanitizeString(DIRECCION_ORDENANTE3);
                    System.out.println("**** DIRECCION_ORDENANTE3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_ORDENANTE3);
                }
                //jeta 15 septiembre 2024 end
            }

//            }
            cal.setTime(new java.util.Date());
            try {
                HoraInicioEvento = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(cal);
            } catch (DatatypeConfigurationException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("********** DatatypeConfigurationException ********** : " + ex);
            }
        }
        try {
            webservicestc.WSNICClienteExecute parameters = new webservicestc.WSNICClienteExecute();
            SDTWSNICClientePeticion algo = new SDTWSNICClientePeticion();
            SDTPeticionHeader algo1 = new SDTPeticionHeader();
            SDTPeticion algo2 = new SDTPeticion();
            algo.setTipoConsulta("01");
            algo.setIdentificacion(identificacion);
            System.out.println("identificacion = " + algo.getIdentificacion());
            algo.setTipoIdentificacion(TipoIdentificacion);
            System.out.println("TipoIdentificacion = " + algo.getTipoIdentificacion());
            algo1.setReferencia(transactionId);
            System.out.println("transactionId = " + transactionId);
            algo2.setHeader(algo1);
            parameters.setPeticiongeneral(algo2);
            parameters.setPeticion(algo);
            webservicestc.WSNICCliente service = new webservicestc.WSNICCliente();
            webservicestc.WSNICClienteSoapPort port = service.getWSNICClienteSoapPort();
            webservicestc.WSNICClienteExecuteResponse result = port.execute(parameters);
            System.out.println("Result = " + result.getRespuesta().getHeader().getCodigo());
            if (result.getRespuesta().getHeader().getCodigo().equals("0000")) {
                mensaje = result.getRespuesta().getHeader().getMensaje();
                nombre = result.getRespuesta().getDetail().getItem().get(0).getNombre();
                NumCliente = result.getRespuesta().getDetail().getItem().get(0).getNumCliente();
                Id = result.getRespuesta().getDetail().getItem().get(0).getId();
                TipoId = result.getRespuesta().getDetail().getItem().get(0).getTipoId();
                TipoPersona = result.getRespuesta().getDetail().getItem().get(0).getTipoPersona();
//                CodPais = result.getRespuesta().getDetail().getItem().get(0).getDirecciones().get(0).getCodPais();
                DesPais = result.getRespuesta().getDetail().getItem().get(0).getDirecciones().get(0).getDesPais();
                Direccion = result.getRespuesta().getDetail().getItem().get(0).getDirecciones().get(0).getResidencia();
                Telefono = result.getRespuesta().getDetail().getItem().get(0).getDirecciones().get(0).getTelefonos().get(0).getNumTelefono();
                Correo = result.getRespuesta().getDetail().getItem().get(0).getCorreosElectronicos().get(0).getCorreo();
                Estado = result.getRespuesta().getDetail().getItem().get(0).getEstado();
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo2 ********** : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("Consulta cliente - CORE");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                System.out.println("********** HoraInicioEvento ********** : " + HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + mensaje);
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                System.out.println("********** HoraFinalizaEvento ********** : " + HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("Evento cliente en el CORE");
                respEvento = insertaEvento(req);
            } else {
                System.out.println("Cliente no existe o información invalida"+result.getRespuesta().getHeader().getCodigo());
                System.out.println("Cliente no existe o información invalida 2"+result.getRespuesta().getHeader().getMensaje());
                System.out.println("Cliente no existe o información invalida 3"+result.getRespuesta().getHeader().getDetalle());
                System.out.println("Cliente no existe o información invalida 4"+result.getRespuesta().getHeader().getTipo());
                detalleColeccion = new StatusItem();
                detalleColeccion.setCodigoRetorno(result.getRespuesta().getHeader().getCodigo());
                detalleColeccion.setMensaje("Cliente no existe o información invalida");
                detalleColeccion.setReferencia("ER");
                deta1.add(detalleColeccion);
                deta0.setCodigoRetornoGlobal("9999");
                deta0.setMensaje("Cliente no existe o información invalida.");
                deta0.setReferencia("ER");
                deta2.add(deta0);
                deta00.setDetalleItem(deta1);
                detax.add(deta00);
//                deta.setDetalleColeccion(deta3);
                deta.setEstadoGlobal(deta2);
                deta.setDetalleColeccion(detax);
                response.add(deta);
                //JETA CAMBIO 1
                System.out.println("*** ejecuta Update 1 *****");
                String respx = update(con, transactionId, ERROR, "0", "ICI");
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo 3 ********** : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("Consulta cliente / BP");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
//                req.setTransaccionEventoComentario("Consulta cliente en el CORE - respuesta No exitosa");
                req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + mensaje);
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("EConsulta cliente en el CORE - respuesta No exitosa");
                respEvento = insertaEvento(req);
                con.close();
                Accion.clear();
                FechaHora.clear();
                Linea.clear();
                TipoAccion.clear();
                Campo.clear();
                Dato.clear();
                TipoCampo.clear();
                LineaAdicional.clear();
                TipoRegistroAdicional.clear();
                ValorAdicional.clear();
                return response;
            }
            if (accion.equals("I") || accion.equals("i")) {
                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
                    Nombre = Dato.get(i);
                    System.out.println("**** NombreRemitente ***** " + Nombre);
                }

                if ((Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME")) || (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME"))) {
                    NombreBeneficiario = Dato.get(i);
                    NombreBeneficiarioCompleto = Dato.get(i);
                    if (NombreBeneficiario.equals(deta10.getNombreClienteBeneficiarioCore())) {
                        int n = 0;
                        n++;
                    } else {
                        System.out.println("**** NombreBeneficiario ***** " + NombreBeneficiario);
                        if (NombreBeneficiario.equals("") || NombreBeneficiario.equals("") || NombreBeneficiario.isEmpty()) {
                            NombreBeneficiario = deta10.getNombreClienteBeneficiarioCore();
                            NombreBeneficiarioCompleto = deta10.getNombreClienteBeneficiarioCore();
                            System.out.println("**** NombreBeneficiario ***** " + NombreBeneficiario);

                        }
                    }
//                   deta10 = busca.BuscarInformacionCuenta(ACCOUNT);
                }
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
            String horaInicio = now.format(formatter);
            cal.setTime(new java.util.Date());
            try {
                HoraInicioEvento = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(cal);
            } catch (DatatypeConfigurationException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("********** DatatypeConfigurationException ********** : " + ex);
            }
            System.out.println("********** ESTADO  ********** : " + Estado);
            if (Estado == 1) {
                TranDictamenTipo = false;
                sw = 0;
                SQL = "SELECT *  FROM MotorTransferencia.dbo.Transaccion where TransaccionDictamenTipo = 'APROBADO' AND TransaccionSecuencia ='" + transactionId + "'";
                System.out.println("********** SQL CodPaisOrdenante** : " + SQL);
                st = con.createStatement();// OJO modificacion del 16 de febrero 2025
                rs = st.executeQuery(SQL);
                while (rs.next()) {
                    System.out.println("********** TranDictamenTipo Antes ** : " + TranDictamenTipo);
                    TranDictamenTipo = true;
                    System.out.println("********** TranDictamenTipo Despues ** : " + TranDictamenTipo);
                }
///  Rutina pais ordenante ++++++++++++++++++++++++++++++++++++++++++++++++++++++
                SQL = "SELECT MotorTransferencia.dbo.Pais.*, MotorTransferencia.dbo.PaisRestriccion.* FROM MotorTransferencia.dbo.Pais INNER JOIN \n"
                        + "MotorTransferencia.dbo.PaisRestriccion ON MotorTransferencia.dbo.Pais.PaisCodigo = MotorTransferencia.dbo.PaisRestriccion.PaisCodigo and PaisRestriccionEstado = 'TIPESTACT' "
                        + "and MotorTransferencia.dbo.Pais.PaisISO2 = '" + CodPaisOrdenante + "' and PaisRestriccion.PaisRestriccionEstado = 'TIPESTACT'";
                System.out.println("********** SQL CodPaisOrdenante** : " + SQL);
                st = con.createStatement();
//                java.sql.Time campo1 = new java.sql.Time(new java.util.Date().getTime());
                rs = st.executeQuery(SQL);
//                OperacionesDB operDB = new OperacionesDB();
                while (rs.next()) {
                    if (TranDictamenTipo.equals(true)) {
                        sw = 2;
                    } else {
                        System.out.println("Pais encontrado en restricciones CodPais Remitente : " + CodPaisOrdenante);
                        respuesta = "Pais encontrado en restricciones CodPais Remitente : " + CodPaisOrdenante;
                        detalleColeccion = new StatusItem();
                        detalleColeccion.setCodigoRetorno("0098");
                        detalleColeccion.setMensaje(respuesta);
                        detalleColeccion.setReferencia("CN");
                        deta1.add(detalleColeccion);
                        sw = 1;
                        sw00 = 1;
                    }
                }
//                java.sql.Time campo2 = new java.sql.Time(new java.util.Date().getTime());
                if (sw != 1) {
                    if (sw == 2) {
                        System.out.println("Pais ENCONTRADO en restricciones Pero AUTORIZADO POR CUMPLIMIENTO , CodPais Remitente: " + CodPaisOrdenante);
                        respuesta = "Pais ENCONTRADO en restricciones, APROBADO por Cumplimiento. CodPais Remitente : " + CodPaisOrdenante;
                    } else {
                        System.out.println("Pais NO encontrado en restricciones CodPais Remitente: " + CodPaisOrdenante);
                        respuesta = "Pais NO encontrado en restricciones CodPais Remitente :" + CodPaisOrdenante;
                    }
                    detalleColeccion = new StatusItem();
                    detalleColeccion.setCodigoRetorno("0000");
                    detalleColeccion.setMensaje(respuesta);
                    detalleColeccion.setReferencia("OK");
                    deta1.add(detalleColeccion);
                }

//======================================================================================================================                
                System.out.println("********** Insertar Evaluación Pais**********");
//======================================================================================================================   
                ObtieneSecuencia secu = new ObtieneSecuencia();
                InsertaEvaluacion inserta = new InsertaEvaluacion();
                long sequence = secu.getSecuencia(con);
                long InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_COUNTRY", transactionId, detalleColeccion, horaInicio, now, con, "LSTCOUNTRY");
//                if (sw == 1) {
//                    req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno()+"|"+detalleColeccion.getMensaje());
//                } else {
//                    req.setTransaccionEventoComentario("Pais *NO* encontrado en restricciones CodPais Remitente");
//                }
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo 4 ********** : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("restricciones pais remitente ");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("EConsulta cliente en el CORE - respuesta exitosa");
                respEvento = insertaEvento(req);
//======================================================================================================================                 
                cal.setTime(new java.util.Date());
                try {
                    HoraInicioEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                sw = 0;
                SQL = "SELECT MotorTransferencia.dbo.Pais.*, MotorTransferencia.dbo.PaisRestriccion.* FROM MotorTransferencia.dbo.Pais INNER JOIN \n"
                        + "MotorTransferencia.dbo.PaisRestriccion ON MotorTransferencia.dbo.Pais.PaisCodigo = MotorTransferencia.dbo.PaisRestriccion.PaisCodigo and PaisRestriccionEstado = 'TIPESTACT' "
                        + "and MotorTransferencia.dbo.Pais.PaisISO2 = '" + CodPaisBeneficiario + "' and PaisRestriccion.PaisRestriccionEstado = 'TIPESTACT'";
                System.out.println("********** SQL CodPaisBeneficiario** : " + SQL);
                st = con.createStatement();
//                java.sql.Time campo1 = new java.sql.Time(new java.util.Date().getTime());
                rs = st.executeQuery(SQL);
//                operDB = new OperacionesDB();
                System.out.println("********** TranDictamenTipo *segundo * : " + TranDictamenTipo);
                while (rs.next()) {
                    if (TranDictamenTipo.equals(true)) {
                        sw = 2;
                        break;
                    } else {
                        System.out.println("Pais encontrado en restricciones CodPais Destinatario :" + CodPaisBeneficiario);
                        respuesta = "Pais encontrado en restricciones CodPais Destinatario : " + CodPaisBeneficiario;
                        detalleColeccion = new StatusItem();
                        detalleColeccion.setCodigoRetorno("0097");
                        detalleColeccion.setMensaje(respuesta);
                        detalleColeccion.setReferencia("CN");
                        deta1.add(detalleColeccion);
                        sw = 1;
                        sw00 = 1;
                    }
                }
//                java.sql.Time campo2 = new java.sql.Time(new java.util.Date().getTime());
                if (sw != 1) {
                    if (sw == 2) {
                        System.out.println("Pais ENCONTRADO en restricciones Pero AUTORIZADO POR CUMPLIMIENTO , CodPais Destinatario: " + CodPaisBeneficiario);
                        respuesta = "Pais ENCONTRADO en restricciones, APROBADO por Cumplimiento. CodPais Destinatario : " + CodPaisBeneficiario;
                    } else {
                        System.out.println("Pais NO encontrado en restricciones CodPais Destinatario: " + CodPaisBeneficiario);
                        respuesta = "Pais NO encontrado en restricciones CodPais Destinatario :" + CodPaisBeneficiario;
                    }
//                    System.out.println("Pais NO encontrado en restricciones CodPais Destinatario : " + CodPaisBeneficiario);
//                    respuesta = "Pais NO encontrado en restricciones CodPais Destinatario : " + CodPaisBeneficiario;
                    detalleColeccion = new StatusItem();
                    detalleColeccion.setCodigoRetorno("0000");
                    detalleColeccion.setMensaje(respuesta);
                    detalleColeccion.setReferencia("OK");
                    deta1.add(detalleColeccion);
                }

//======================================================================================================================                
                System.out.println("********** Insertar Evaluación Pais**********");
//======================================================================================================================   
                secu = new ObtieneSecuencia();
                inserta = new InsertaEvaluacion();
                sequence = secu.getSecuencia(con);
                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_COUNTRY", transactionId, detalleColeccion, horaInicio, now, con, "LSTCOUNTRY");
//                if (sw == 1) {
//                    req.setTransaccionEventoComentario("Pais encontrado en restricciones - Destinatario. : " + CodPaisBeneficiario);
//                } else {
//                    req.setTransaccionEventoComentario("Pais *NO* encontrado en restricciones - Destinatario");
//                }
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo 5********** : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("restricciones pais destinatario ");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("restricciones pais destinatarioX");
                respEvento = insertaEvento(req);
                sw = 0;
//*****************************************************************************************************************
// LOG de error pais con restricción y/o transaccion paso validación
//*****************************************************************************************************************
                System.out.println("***** veamos aqui entrar *****");
                now = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                horaInicio = now.format(formatter);
                int sw0 = 0;
                if (accion.equals("i") || accion.equals("I")) {
                    if (TipoId.equals("NI02") || TipoId.equals("NI07") || TipoId.equals("NI09") || TipoId.equals("NI07")) {
                        ParametroCodigo = "PARACUMPLEMP";
                        sw0 = 1;
                    } else {
                        ParametroCodigo = "PARACUMPL";
                        sw0 = 1;
                    }
                }
                if (accion.equals("o") || accion.equals("O")) {
                    if (TipoId.equals("NI02") || TipoId.equals("NI07") || TipoId.equals("NI09") || TipoId.equals("NI07")) {
                        ParametroCodigo = "PARACUMPLEMP_OUT";
                        sw0 = 1;
                    } else {
                        ParametroCodigo = "PARACUMPLPER_OUT";
                        sw0 = 1;
                    }
                }
//                if (accion.equals("o") || accion.equals("O")) {
//                    System.out.println("***** Entros accion :  ***** " + accion);
//                    System.out.println("***** Entros TipoId.equals :  *****" + TipoId);
//                    if (TipoId.equals("NI04")) {
//                        ParametroCodigo = "PARACUMPLPER";
//                        sw0 = 1;
//                    }
//                    if (TipoId.equals("NI02")) {
//                        ParametroCodigo = "PARACUMPLEMP";
//                        sw0 = 1;
//                    }
//                } else {
//                    if (TipoId.equals("NI04")) {
//                        ParametroCodigo = "PARACUMPLPER_OUT";
//                        sw0 = 1;
//                    }
//                    if (TipoId.equals("NI02")) {
//                        ParametroCodigo = "PARACUMPLEMP_OUT";
//                        sw0 = 1;
//                    }
//                }

                System.out.println("***** Salio ParametroCodigo :  *****" + ParametroCodigo);
                if (sw0 != 1) {
                    detalleColeccion = new StatusItem();
                    detalleColeccion.setCodigoRetorno("0001");
                    detalleColeccion.setMensaje("TipoId no valido");
                    detalleColeccion.setReferencia("CN");
                    deta1.add(detalleColeccion);
//                    deta.setDetalleColeccion(DetalleColeccion);

                    sw = 1;
//                    sw00 = 1;
//                    return deta;
                }
                sw0 = 0;
                cal.setTime(new java.util.Date());
                try {
                    HoraInicioEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
//======================================================================================================================                
                System.out.println("********** Evaluación Monto **********");
//======================================================================================================================                     
                SQL = "SELECT * FROM MotorTransferencia.dbo.ParametroValor WHERE ParametroCodigo='" + ParametroCodigo + "' AND ParametroValorCodigo='MONTOLIMITE';";
                System.out.println("**** SQL **** : " + SQL);
//                System.out.println("********** SQL ** : " + SQL);
//                st.close();
                st = con.createStatement();
                rs = st.executeQuery(SQL);
//                System.out.println("********** ANTES DEL WHILE VALOR**********");
                sw = 0;
                while (rs.next()) {

//                    System.out.println("********** DENTRO DEL WHILE VALOR**********");
                    ParametroValorDato = rs.getString("ParametroValorDato");
                    System.out.println("**** ParametroValorDato **** : " + ParametroValorDato);
                    System.out.println("**** Double.valueOf(Valor) **** : " + Double.valueOf(Valor));
//                    System.out.println("ParametroValorDato ** : " + ParametroValorDato);
                    if (Double.valueOf(ParametroValorDato) <= Double.valueOf(Valor)) {
                        if (TranDictamenTipo.equals(true)) {
                            sw = 2;
                        } else {
                            System.out.println("*** Valor transferencia mayor que la permitida *** NO Procede la transferencia");

                            respuesta = "Valor transferencia mayor que la permitida *** NO Procede la transferencia - Valor : " + Double.valueOf(Valor);
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("9997");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("CN");
                            deta1.add(detalleColeccion);
                            sw = 1;
                            sw00 = 1;
//                        return deta;
                        }
                    }
                }
                if (sw != 1) {
                    if (sw == 2) {
                        System.out.println("*** Valor transferencia mayor que la permitida, Pero autorizo cumplimiento  *** SI Procede la transferencia");
                        respuesta = "Valor transferencia mayor que la permitida, *** Pero autorizado por Cumplimiento  *** ";
                    } else {
                        System.out.println("*** Valor transferencia valido  *** SI Procede la transferencia");
                        respuesta = "Valor transferencia valido  *** ";
                    }
                    detalleColeccion = new StatusItem();
                    detalleColeccion.setCodigoRetorno("0000");
                    detalleColeccion.setMensaje(respuesta);
                    detalleColeccion.setReferencia("OK");
                    deta1.add(detalleColeccion);
                }
//======================================================================================================================                
                System.out.println("********** Insertar Evaluación Monto **********");
//======================================================================================================================     
                sequence = secu.getSecuencia(con);
                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_AMOUNT", transactionId, detalleColeccion, horaInicio, now, con, ParametroCodigo);
//                    if (sw == 1) {
//                        req.setTransaccionEventoComentario("Valor transferencia mayor que la permitida " + Valor);
//                    } else {
//                        req.setTransaccionEventoComentario("Valor transferencia valido");
//                    }
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo 6 ********** : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("valores permitidos ");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("valores permitidos");
                respEvento = insertaEvento(req);
                System.out.println("********** PASOXX1********** sw : " + sw);
//======================================================================================================================                
                System.out.println("********** Evaluación sumatoria de Montos mes **********");
//======================================================================================================================                     
//                if (sw != 1) {///OJO JETA
//                        st.close();
                LocalDate fechaActual = LocalDate.now();
//                        System.out.println("LocalDate fechaActual : " + fechaActual);
                LocalDate primerDiaDelMes = fechaActual.with(TemporalAdjusters.firstDayOfMonth());
//                        System.out.println("LocalDate primerDiaDelMes : " + primerDiaDelMes);
                LocalDate ultimoDiaDelMes = fechaActual.with(TemporalAdjusters.lastDayOfMonth());
//                        System.out.println("LocalDate ultimoDiaDelMes : " + ultimoDiaDelMes);
//                        SQL = "select sum(TransaccionMonto) as total from Transaccion where (TransaccionGestionEstado in ('TIPESTCAU','TIPESTAUT') and TransaccionArgumento3 = '" + NumCliente + "' and TransaccionSistemaFecha = cast(getdate() as date)) or \n"
//                                + "(TransaccionGestionEstado in ('TIPESTCAU','TIPESTAUT') and TransaccionArgumento2 = '" + Id + "' and TransaccionSistemaFecha = cast(getdate() as date)) ";
                cal.setTime(new java.util.Date());
                try {
                    HoraInicioEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
//                    System.out.println("********* Si la vemos ********* : ");
                SQL = "select sum(CAST(TransaccionArgumento7 as DECIMAL(15,2))) as total from Transaccion where (TransaccionGestionEstado in ('TIPESTCAU','TIPESTAUT', 'TIPESTAPR') and TransaccionArgumento3 = '" + NumCliente + "' and TransaccionSistemaFecha = cast(getdate() as date)) or \n"
                        //                        SQL = "select sum(TransaccionMonto) as total from Transaccion where (TransaccionGestionEstado in ('TIPESTCAU','TIPESTAUT', 'TIPESTAPR') and TransaccionArgumento3 = '" + NumCliente + "' and TransaccionSistemaFecha = cast(getdate() as date)) or \n"
                        + "(TransaccionGestionEstado in ('TIPESTCAU','TIPESTAUT','TIPESTAPR') and TransaccionArgumento2 = '" + Id + "' and TransaccionSistemaFecha between '" + primerDiaDelMes + "' and '" + ultimoDiaDelMes + "')  ";
                System.out.println("*** sentencia SQL : " + SQL);
                st = con.createStatement();
                rs = st.executeQuery(SQL);
                System.out.println("********** PASO AQUI1********** ");
                int n1 = 0;
                sw = 0;
                while (rs.next()) {
                    System.out.println("********** PASO AQUI2********** ");
                    totalValor = rs.getDouble(1);
                    if (totalValor == 0) {
                        sw = 0;
//                            break;
                    } else {
                        double total = totalValor + Double.valueOf(Valor);
                        if (Double.valueOf(ParametroValorDato) <= total) {
                            System.out.println("********** PASO AQUI4********** ");
                            if (TranDictamenTipo.equals(true)) {
                                System.out.println("********** PASO AQUI5********** ");
                                sw = 2;
//                                    break;
                            } else {
                                System.out.println("********** PASO AQUI6********** ");
                                total = (totalValor + Double.valueOf(Valor));
                                System.out.println("*** Valor suma de transferencias del día, mayor que la permitida : Valor Total : " + total + "  Valor parametro : " + ParametroValorDato);
                                respuesta = "OJO   *** Valor suma de transferencias del día, mayor que la permitida *** NO Procede la transferencia - Suma de Valores : " + total;
                                total = 0.00;
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9996");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
//                        return deta;
                            }
                        }
                    }

                }
                System.out.println("********** PASO AQUI3********** ");
                System.out.println("ParametroValorDato ** : " + ParametroValorDato);
                System.out.println("NO AUN NO Valor  *** NO **totalValor : " + totalValor);
                if (sw != 1) {
                    System.out.println("********** PASO AQUI8********** ");
                    if (sw == 2) {
                        System.out.println("********** PASO AQUI9********** ");

                        System.out.println("*** *** Valor suma de transferencias del día, mayor que la permitida , Pero autorizo cumplimiento  *** SI Procede la transferencia");
                        respuesta = "*** Valor suma de transferencias del día, mayor que la permitida , *** Pero autorizado por Cumplimiento  *** ";
                    } else {
                        System.out.println("********** PASO AQUI10 ********** ");
                        System.out.println("*** Valor transferencia valido  *** SI Procede la transferencia");
                        respuesta = "Valor transferencia valido  *** SI Procede la transferencia ";
                    }
                    detalleColeccion = new StatusItem();
                    detalleColeccion.setCodigoRetorno("0000");
                    detalleColeccion.setMensaje(respuesta);
                    detalleColeccion.setReferencia("OK");
                    deta1.add(detalleColeccion);
                }
//                    }
                System.out.println("********** PASO AQUI11 ********** ");
//                    System.out.println("********** PASOXX3**********");
                sequence = secu.getSecuencia(con);
                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_AMOUNT", transactionId, detalleColeccion, horaInicio, now, con, ParametroCodigo);

//                    if (sw == 1) {
//                        req.setTransaccionEventoComentario("Valor suma de transferencias del día, mayor que la permitida " + Valor);
//                    } else {
//                        req.setTransaccionEventoComentario("Valor suma de transferencias del día, *VÁLIDO*");
//                    }
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo *8 ********* : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("sumatoria valores permitidos");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("valores permitidos 2 b");
                respEvento = insertaEvento(req);
                System.out.println("PASO despues del respEvento");
//*****************************************************************************************************************
// LOG de error valor transacción con restricción y/o transaccion paso validación
//*****************************************************************************************************************

//                }//
                SQL = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'paracumpl' and ParametroValorCodigo = 'PORCENTAJE'";
                System.out.println("SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor : " + SQL);
                st = con.createStatement();
                System.out.println("PASO despues del st = con.createStatement() antes del executeQuery");
                rs = st.executeQuery(SQL);
                System.out.println("PASO despues del st.executeQuery(SQL)");
                while (rs.next()) {
                    ParametroValorDatoPorcentaje = rs.getString("ParametroValorDato");
                }
                cal.setTime(new java.util.Date());
                try {
                    HoraInicioEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }

                DBConnectListas coneccionListas = new DBConnectListas();
                System.out.println("*** Antes del connect de listas *****");
                con1 = coneccionListas.obtenerConeccion();
                System.out.println("*** Despues del connect de listas *****");

                if (con1 == null) {//TIPESTECE
                    System.out.println("*** Despues del connect de listas No conecto *****");
                    System.out.println("*** ejecuta Update 2 *****");
                    String respx = update(con, transactionId, valorCoincidenciaECX, "?", "ECX");
//                    sql = "update Transaccion set TransaccionGestionEstado = '" + valorCoincidenciaECX + "', TransaccionPaso = " + paso + "  where TransaccionSecuencia = " + transactionId;
//                    stmt = con.createStatement();
//                    int count = stmt.executeUpdate(sql);
//                    System.out.println("SQl del Update sw = 1: " + sql);
//                    System.out.println("Updated queries: " + count);
                    con.close();
                    deta0.setCodigoRetornoGlobal("9995");
                    deta0.setMensaje(valorCoincidenciaECX);
                    deta0.setReferencia("");
                    deta1.add(detalleColeccion);
                    deta2.add(deta0);
//                deta.setDetalle(deta1);
                    deta.setEstadoGlobal(deta2);
                    deta00.setDetalleItem(deta1);
                    detax.add(deta00);
                    deta.setDetalleColeccion(detax);
                    response.add(deta);
                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                    System.out.println("********** HoraInicioAplicativo *8 ********* : " + HoraInicioAplicativo);
                    req.setTransaccionEventoAccion("Conexion DB Listas");
                    req.setTipoEventoCodigo("INSERT");
                    req.setTransaccionEventoInicio(HoraInicioEvento);
                    req.setTransaccionUsuarioCodigo(usuarioId);
                    req.setTransaccionEventoDispositivo(pantalla);
                    req.setTransaccionEventoIP(terminal);
                    req.setTransaccionEventoSession(llaveSesion);
                    req.setTransaccionEventoComentario(deta0.getCodigoRetornoGlobal() + "|" + deta0.getMensaje());
                    cal.setTime(new java.util.Date());
                    try {
                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
//                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                    }
                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                    req.setTransaccionEventoGestion("TIPESTPRO");
                    req.setTransaccionEventoEstado("TIPESTACT");
                    System.out.println("valores permitidos 2");
                    respEvento = insertaEvento(req);
                    Accion.clear();
                    FechaHora.clear();
                    Linea.clear();
                    TipoAccion.clear();
                    Campo.clear();
                    Dato.clear();
                    TipoCampo.clear();
                    LineaAdicional.clear();
                    TipoRegistroAdicional.clear();
                    ValorAdicional.clear();
                    System.out.println("*** Despues del connect de listas No conecto return*****");
                    return response;
                }
                cal.setTime(new java.util.Date());
                try {
                    HoraInicioEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                System.out.println("**** NombreRemitente xx***** " + Nombre);
//============================================ LISTAS_NIC Solicitante=========================================================================                
                now = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                horaInicio = now.format(formatter);

                sql = "SELECT ParametroValorDato  FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'VALIDAINT'";
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    ParametroValorDato = rs.getString("ParametroValorDato");
//                    System.out.println("ParametroValorDato ** : " + ParametroValorDato);
                    if (!ParametroValorDato.equals("S")) {
                        break;
                    } else {
                        sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'PORCENTAJEINT'";
                        st = con.createStatement();
                        rs = st.executeQuery(sql);
                        while (rs.next()) {
                            ParametroValorDato = rs.getString("ParametroValorDato");
                        }
                        NombresListaNic similar = new NombresListaNic();
//                        System.out.println("************* Busca similitudes en tabla LISTAS_NIC : uno + REM ************* " + Nombre);
                        deta3 = similar.BuscarNombreListaNic(sequence, Nombre.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
//                        System.out.println("************* Busca similitudes Valor respuesta : " + deta3.getValorSimilitud());
//                        System.out.println("************* Busca similitudes NombreLista : " + deta3.getNombreLista());
//                if (deta3.getValorSimilitud() >= 0.7083333333333333) {
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            System.out.println("**************  TranDictamenTipo  ************** :  " + TranDictamenTipo);
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "Coincidencia LISTAS_NIC nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9994");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "LISTAS_NIC");
                                System.out.println("va a entrar a buscar para vario ");
                                deta3 = similar.BuscarNombreListaNic(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("LISTAS_NIC Solicitante");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                System.out.println("valores permitidos 2 a");
                                respEvento = insertaEvento(req);
                            }
//                    return deta;
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - Coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud() + " ,*** SI Procede la transferencia\");");
                                respuesta = "*Autorizo cumplimiento* - Pero Coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud();
                            } else {
                                System.out.println("Sin coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "Sin coincidencia LISTAS_NIC con un valor de : " + deta3.getValorSimilitud();
                            }
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "LISTAS_NIC");
                            deta3 = similar.BuscarNombreListaNic(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                        }
                        //============================================ LISTAS_NIC Beneficiario=========================================================================                
                        now = LocalDateTime.now();
                        formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                        horaInicio = now.format(formatter);

                        similar = new NombresListaNic();
//                        System.out.println("************* Busca similitudes en tabla LISTAS_NIC Beneficiario ************* " + NombreBeneficiarioCompleto);
                        deta3 = similar.BuscarNombreListaNic(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
//                        System.out.println("************* Busca similitudes Valor respuesta : " + deta3.getValorSimilitud());
//                        System.out.println("************* Busca similitudes NombreLista : " + deta3.getNombreLista());
//                if (deta3.getValorSimilitud() >= 0.7083333333333333) {
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Beneficiario Coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Coincidencia LISTAS_NIC nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
//                    respuesta = "Coincidencia LISTAS_NIC nombre : "+deta3.getNombreLista()+ " con valor : "+ deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9993");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                System.out.println("ojojo'");
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "LISTAS_NIC");
                                System.out.println("ojo ojo 222 '");
                                deta3 = similar.BuscarNombreListaNic(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("LISTAS_NIC Beneficiario");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - Coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud() + " ,*** SI Procede la transferencia\");");
                                respuesta = "*Autorizo cumplimiento* - 'Beneficiario' Coincidencia LISTAS_NIC nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                            } else {
                                System.out.println("Beneficiario. Sin coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Sin Coincidencia en Nombre Beneficiario tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud();
                            }
//                            System.out.println("Beneficiario. Sin coincidencia en tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud());
//                            respuesta = "'Beneficiario' Sin Coincidencia en Nombre Beneficiario tabla LISTAS_NIC con un valor de : " + deta3.getValorSimilitud();
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "LISTAS_NIC");
                            deta3 = similar.BuscarNombreListaNic(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                        }

//                        deta3 = similar.BuscarNombreListaNic(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                    }

                }

//============================================ VWCHIEFS Solicitante =========================================================================      
                now = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                horaInicio = now.format(formatter);

                sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'VALIDACHIEFS'";
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    ParametroValorDato = rs.getString("ParametroValorDato");
                    System.out.println("ParametroValorDato ** : " + ParametroValorDato);
                    if (!ParametroValorDato.equals("S")) {
                        break;
                    } else {
                        sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'PORCENTAJECHIEFS'";
                        st = con.createStatement();
                        rs = st.executeQuery(sql);
                        while (rs.next()) {
                            ParametroValorDato = rs.getString("ParametroValorDato");
                        }
                        NombresListaVWCHIEFS similar1 = new NombresListaVWCHIEFS();
//                        System.out.println("************* Busca similitudes en tabla VWCHIEFS ************* " + Nombre);
                        deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, Nombre.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);

                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud());
//                    respuesta = "Coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud();
                                respuesta = "Coincidencia VWCHIEFS nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9992");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWCHIEFS");
                                deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("VWCHIEFS Solicitante");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - Coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "*Autorizo cumplimiento* - Coincidencia VWCHIEFS con un valor de : " + deta3.getValorSimilitud();
                            } else {
                                System.out.println("Sin coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "Sin coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud();
                            }

                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWCHIEFS");
                            deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);

                        }

//============================================ VWCHIEFS NombreBeneficiario =========================================================================                
                        now = LocalDateTime.now();
                        formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                        horaInicio = now.format(formatter);
                        similar1 = new NombresListaVWCHIEFS();
//                        System.out.println("************* Busca similitudes en tabla VWCHIEFS ************* " + NombreBeneficiario);
                        deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
//                        System.out.println("************* Busca similitudes Valor respuesta : " + deta3.getValorSimilitud());
//                        System.out.println("************* Busca similitudes NombreLista : " + deta3.getNombreLista());
//                        if (deta3.getValorSimilitud() >= 0.7083333333333333) {
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Coincidencia VWCHIEFS nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
//                    respuesta = "Coincidencia nombre : "+deta3.getNombreLista()+ " con valor : "+ deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9991");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWCHIEFS");
                                deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("VWCHIEFS NombreBeneficiarioCompleto");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - 'Beneficiario' Coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud());
                                respuesta = "*Autorizo cumplimiento* - 'Beneficiario' Coincidencia VWCHIEFS con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                            } else {
                                System.out.println("'Beneficiario' Sin coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Sin coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud();
                            }

                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWCHIEFS");
                            deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                            System.out.println("Sin coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud());
                            respuesta = "Sin coincidencia en tabla VWCHIEFS con un valor de : " + deta3.getValorSimilitud();
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWCHIEFS");
                            deta3 = similar1.BuscarNombreListaVWCHIEFS(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                            req.setTransaccionSecuencia(Long.valueOf(transactionId));
                            req.setTransaccionEventoSistema(HoraInicioAplicativo);
                            req.setTransaccionEventoAccion("VWCHIEFS Beneficiario");
                            req.setTipoEventoCodigo("INSERT");
                            req.setTransaccionEventoInicio(HoraInicioEvento);
                            req.setTransaccionUsuarioCodigo(usuarioId);
                            req.setTransaccionEventoDispositivo(pantalla);
                            req.setTransaccionEventoIP(terminal);
                            req.setTransaccionEventoSession(llaveSesion);
                            req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                        }

                    }
                }
//============================================ VWOFAC_ALL Solicitante=========================================================================                
                now = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                horaInicio = now.format(formatter);

                sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where parametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'VALIDAOFAC'";
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    ParametroValorDato = rs.getString("ParametroValorDato");
                    System.out.println("ParametroValorDato ** : " + ParametroValorDato);
                    if (!ParametroValorDato.equals("S")) {
                        break;
                    } else {
                        sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'PORCENTAJEOFAC'";
                        st = con.createStatement();
                        rs = st.executeQuery(sql);
                        while (rs.next()) {
                            ParametroValorDato = rs.getString("ParametroValorDato");
                        }
                        NombresListaVWOFAC_ALL similar2 = new NombresListaVWOFAC_ALL();
//                        System.out.println("************* Busca similitudes en tabla VWOFAC_ALL ************* " + Nombre);
                        deta3 = similar2.BuscarNombreListaVWOFAC_ALL(sequence, Nombre.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud());
//                    respuesta = "Coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud();
                                respuesta = "Coincidencia VWOFAC_ALL nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9989");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWOFAC_ALL");
                                deta3 = similar2.BuscarNombreListaVWOFAC_ALL(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("VWOFAC_ALL Solicitante");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - Coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud());
                                respuesta = "*Autorizo cumplimiento* - Coincidencia VWOFAC_ALL con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                            } else {
                                System.out.println("Sin coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "Sin coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud();
                            }
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWOFAC_ALL");
                            deta3 = similar2.BuscarNombreListaVWOFAC_ALL(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                        }

//============================================ VWOFAC_ALL Beneficiario=========================================================================                
                        now = LocalDateTime.now();
                        formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                        horaInicio = now.format(formatter);

                        similar2 = new NombresListaVWOFAC_ALL();
//                        System.out.println("************* Busca similitudes en tabla VWOFAC_ALL ************* " + NombreBeneficiario);
                        deta3 = similar2.BuscarNombreListaVWOFAC_ALL(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
//                        System.out.println("************* Busca similitudes Valor respuesta : " + deta3.getValorSimilitud());
//                        System.out.println("************* Busca similitudes NombreLista : " + deta3.getNombreLista());
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Coincidencia VWOFAC_ALL nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
//                    respuesta = "Coincidencia nombre : "+deta3.getNombreLista()+ " con valor : "+ deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9988");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWOFAC_ALL");
                                deta3 = similar2.BuscarNombreListaVWOFAC_ALL(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("VWOFAC_ALL Beneficiario");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - 'Beneficiario' Coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud());
                                respuesta = "*Autorizo cumplimiento* - 'Beneficiario' Coincidencia VWOFAC_ALL con un valor de : " + +deta3.getValorSimilitud();
                            } else {
                                System.out.println("Sin coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Sin coincidencia en tabla VWOFAC_ALL con un valor de : " + deta3.getValorSimilitud();
                            }
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWOFAC_ALL");
                            deta3 = similar2.BuscarNombreListaVWOFAC_ALL(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                        }

                    }
                }
//============================================ VWONU_ALL Solicitante =========================================================================                
                now = LocalDateTime.now();
                formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                horaInicio = now.format(formatter);

                sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where parametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'VALIDAONU'";
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    ParametroValorDato = rs.getString("ParametroValorDato");
                    System.out.println("ParametroValorDato ** : " + ParametroValorDato);
                    if (!ParametroValorDato.equals("S")) {
                        break;
                    } else {
                        sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'PARACUMPL' and ParametroValorCodigo = 'PORCENTAJEONU'";
                        st = con.createStatement();
                        rs = st.executeQuery(sql);
                        while (rs.next()) {
                            ParametroValorDato = rs.getString("ParametroValorDato");
                        }
                        NombresListaVWONU_ALL similar3 = new NombresListaVWONU_ALL();
//                        System.out.println("************* Busca similitudes en tabla VWONU_ALL ************* " + Nombre);
                        deta3 = similar3.BuscarNombreListaVWONU_ALL(sequence, Nombre.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
//                        System.out.println("************* Busca similitudes Valor respuesta : " + deta3.getValorSimilitud());
//                        System.out.println("************* Busca similitudes NombreLista : " + deta3.getNombreLista());
//                        if (deta3.getValorSimilitud() >= 0.7083333333333333) {
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "Coincidencia VWONU_ALL nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9987");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWONU_ALL");
                                deta3 = similar3.BuscarNombreListaVWONU_ALL(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("VWONU_ALL Solicitante");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - Coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud());
                                respuesta = "*Autorizo cumplimiento* - Coincidencia VWONU_ALL con un valor de : " + +deta3.getValorSimilitud();
                            } else {
                                System.out.println("Sin coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "Sin coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getValorSimilitud();
                            }
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWONU_ALL");
                            deta3 = similar3.BuscarNombreListaVWONU_ALL(sequence, Nombre.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "REM", ParametroValorDatoPorcentaje);
                        }

//============================================ VWONU_ALL Beneficiario=========================================================================                
                        now = LocalDateTime.now();
                        formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                        horaInicio = now.format(formatter);

                        similar3 = new NombresListaVWONU_ALL();
//                        System.out.println("************* Busca similitudes en tabla VWONU_ALL ************* " + NombreBeneficiario);
                        deta3 = similar3.BuscarNombreListaVWONU_ALL(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "uno", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
//                        System.out.println("************* Busca similitudes Valor respuesta : " + deta3.getValorSimilitud());
//                        System.out.println("************* Busca similitudes NombreLista : " + deta3.getNombreLista());
//                        if (deta3.getValorSimilitud() >= 0.7083333333333333) {
                        if (deta3.getValorSimilitud() >= Double.valueOf(ParametroValorDato)) {
                            if (TranDictamenTipo.equals(true)) {
                                sw = 2;
                                break;
                            } else {
                                System.out.println("Coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Coincidencia VWONU_ALL nombre : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud();
//                    respuesta = "Coincidencia nombre : "+deta3.getNombreLista()+ " con valor : "+ deta3.getValorSimilitud();
                                detalleColeccion = new StatusItem();
                                detalleColeccion.setCodigoRetorno("9986");
                                detalleColeccion.setMensaje(respuesta);
                                detalleColeccion.setReferencia("CN");
                                deta1.add(detalleColeccion);
                                sw = 1;
                                sw00 = 1;
                                sequence = secu.getSecuencia(con);
                                InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWONU_ALL");
                                deta3 = similar3.BuscarNombreListaVWONU_ALL(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                req.setTransaccionEventoAccion("VWONU_ALL Beneficiario");
                                req.setTipoEventoCodigo("INSERT");
                                req.setTransaccionEventoInicio(HoraInicioEvento);
                                req.setTransaccionUsuarioCodigo(usuarioId);
                                req.setTransaccionEventoDispositivo(pantalla);
                                req.setTransaccionEventoIP(terminal);
                                req.setTransaccionEventoSession(llaveSesion);
                                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                                cal.setTime(new java.util.Date());
                                try {
                                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                                            .newXMLGregorianCalendar(cal);
                                } catch (DatatypeConfigurationException ex) {
//                                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                                }
                                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                req.setTransaccionEventoGestion("TIPESTPRO");
                                req.setTransaccionEventoEstado("TIPESTACT");
                                respEvento = insertaEvento(req);
                            }
                        } else {
                            if (sw == 2) {
                                System.out.println("Autorizo cumplimiento - 'Beneficiario' Coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getNombreLista() + " con valor : " + deta3.getValorSimilitud());
                                respuesta = "*Autorizo cumplimiento* - 'Beneficiario' Coincidencia VWONU_ALL con un valor de : " + +deta3.getValorSimilitud();
                            } else {
                                System.out.println("Sin coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getValorSimilitud());
                                respuesta = "'Beneficiario' Sin coincidencia en tabla VWONU_ALL con un valor de : " + deta3.getValorSimilitud();
                            }
                            detalleColeccion = new StatusItem();
                            detalleColeccion.setCodigoRetorno("0000");
                            detalleColeccion.setMensaje(respuesta);
                            detalleColeccion.setReferencia("OK");
                            deta1.add(detalleColeccion);
                            sw = 0;
                            sequence = secu.getSecuencia(con);
                            InsertaResultado = inserta.insertaEvaluacion(sequence, "TIP_NAME", transactionId, detalleColeccion, horaInicio, now, con, "VWONU_ALL");
                            deta3 = similar3.BuscarNombreListaVWONU_ALL(sequence, NombreBeneficiarioCompleto.toUpperCase(), ParametroValorDato, "varios", con, con1, horaInicio, now, "BEN", ParametroValorDatoPorcentaje);
                        }

                    }
                }
                con1.close();

//                if (sw == 1) {
//                    req.setTransaccionEventoComentario("Valor suma de transferencias del día, mayor que la permitida " + Valor);
//                } else {
//                    req.setTransaccionEventoComentario("Valor suma de transferencias del día, *VÁLIDO*");
//                }
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo *9 ********* : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("Eval.Listas * Cumplimiento * ");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario("Fin Evaluacion nombres remitente y beneficiario en Listas * Cumplimiento *");
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("Evaluacion nombres en listas");
                respEvento = insertaEvento(req);
//*****************************************************************************************************************
// LOG de error listas negras  y/o transaccion paso validación
//*****************************************************************************************************************
                if (sw == 1 || sw00 == 1) {
                    System.out.println("************* sw entro  ");
                    System.out.println("Existen errorres, favor revise colecciones");
                    detalleColeccion = new StatusItem();
                    deta0.setCodigoRetornoGlobal("0001");
                    deta0.setMensaje("Existen errorres, favor revise colecciones");
                    deta0.setReferencia("");
                    deta1.add(detalleColeccion);
                    deta2.add(deta0);
//                    deta.setDetalle(deta1);
                    deta.setEstadoGlobal(deta2);
                    deta00.setDetalleItem(deta1);
                    detax.add(deta00);
                    deta.setDetalleColeccion(detax);
                    response.add(deta);
                    System.out.println("*** ejecuta Update 3 *****");
                    String resp = update(con, transactionId, valorCoincidencia, "0", "NO");
//                    sql = "update Transaccion set TransaccionGestionEstado = '" + valorCoincidencia + "' where TransaccionSecuencia = " + transactionId;
////                    sql = "update Transaccion set TransaccionGestionEstado = '" + valorCoincidencia + "', TransaccionPaso = '" + paso + "'  where TransaccionSecuencia = " + transactionId;
//                    stmt = con.createStatement();
//                    int count = stmt.executeUpdate(sql);
//                    System.out.println("transactionId : " + transactionId);
//                    System.out.println("SQl del Update sw = 1: " + sql);
//                    System.out.println("Updated queries: " + count);
                    sw00 = 0;
                    con.close();
                    if (con1 != null) {
                        con1.close();
                    }
                    sw = 0;
                    Accion.clear();
                    FechaHora.clear();
                    Linea.clear();
                    TipoAccion.clear();
                    Campo.clear();
                    Dato.clear();
                    TipoCampo.clear();
                    LineaAdicional.clear();
                    TipoRegistroAdicional.clear();
                    ValorAdicional.clear();
                    ProcesaTransferenciaIn input = new ProcesaTransferenciaIn();
                    input.setTransid(transactionId);
                    input.setUsuario(usuarioId);
                    EnviaNootificacion.enviaCorreo(input);
                    return response;
                }

                System.out.println("************* sw NO entro  ");
                detalleColeccion = new StatusItem();
                deta0.setCodigoRetornoGlobal("0000");
                deta0.setMensaje("Al parecer - todo va bien 👌💲");
                deta0.setReferencia("");
                deta1.add(detalleColeccion);
                deta2.add(deta0);
//                deta.setDetalleColeccion(DetalleColeccion);
                deta.setEstadoGlobal(deta2);
                deta00.setDetalleItem(deta1);
                detax.add(deta00);
                deta.setDetalleColeccion(detax);   //jeton
                response.add(deta);
                System.out.println("*** ejecuta Update 4 *****");
                String resp1 = update(con, transactionId, NovalorCoincidencia, paso, "OK");
//                sql = "update Transaccion set TransaccionGestionEstado = '" + NovalorCoincidencia + "' where TransaccionSecuencia = " + transactionId;
//                sql = "update Transaccion set TransaccionGestionEstado = '" + NovalorCoincidencia + "', TransaccionPaso = " + paso + "  where TransaccionSecuencia = " + transactionId;
//                System.out.println("SQl del Update sw = 0: " + sql);
//                stmt = con.createStatement();
//                int count = stmt.executeUpdate(sql);
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                System.out.println("********** HoraInicioAplicativo *9 ********* : " + HoraInicioAplicativo);
                req.setTransaccionEventoAccion("InvocaValidacion");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioAplicativo);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
                req.setTransaccionEventoComentario("Finaliza InvocaValidacion: " + deta0.getCodigoRetornoGlobal() + "|" + deta0.getMensaje());
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("FIN InvocaValidacion");
                respEvento = insertaEvento(req);
                System.out.println("transactionId : " + transactionId);
//                System.out.println("Updated queries: " + count);
                con.close();
                if (con1 != null) {
                    con1.close();
                }
                sw = 0;
                Accion.clear();
                FechaHora.clear();
                Linea.clear();
                TipoAccion.clear();
                Campo.clear();
                Dato.clear();
                TipoCampo.clear();
                LineaAdicional.clear();
                TipoRegistroAdicional.clear();
                ValorAdicional.clear();
                return response;

            } else {
                System.out.println("*** Estado inactivo ***");
                respuesta = "*** Estado inactivo ***" + Estado;
                detalleColeccion = new StatusItem();
                detalleColeccion.setCodigoRetorno("99");
                detalleColeccion.setMensaje(respuesta);
                detalleColeccion.setReferencia("*** Estado inactivo *** " + Estado);
                deta1.add(detalleColeccion);
                deta2.add(deta0);
                //OJO JETA
                System.out.println("*** ejecuta Update 5 *****");
                String respx = update(con, transactionId, ERROR, "0", "NO");
//                deta.setDetalle(deta1);
                deta.setEstadoGlobal(deta2);
                deta00.setDetalleItem(deta1);
                detax.add(deta00);
                deta.setDetalleColeccion(detax);
                response.add(deta);
                req.setTransaccionSecuencia(Long.valueOf(transactionId));
                req.setTransaccionEventoSistema(HoraInicioAplicativo);
                req.setTransaccionEventoAccion("Consulta cliente / BP");
                req.setTipoEventoCodigo("INSERT");
                req.setTransaccionEventoInicio(HoraInicioEvento);
                req.setTransaccionUsuarioCodigo(usuarioId);
                req.setTransaccionEventoDispositivo(pantalla);
                req.setTransaccionEventoIP(terminal);
                req.setTransaccionEventoSession(llaveSesion);
//                req.setTransaccionEventoComentario("Consulta cliente en el CORE - respuesta No exitosa");
                req.setTransaccionEventoComentario(detalleColeccion.getCodigoRetorno() + "|" + detalleColeccion.getMensaje());
                cal.setTime(new java.util.Date());
                try {
                    HoraFinalizaEvento = DatatypeFactory.newInstance()
                            .newXMLGregorianCalendar(cal);
                } catch (DatatypeConfigurationException ex) {
//                    Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                }
                req.setTransaccionEventoFinal(HoraFinalizaEvento);
                req.setTransaccionEventoGestion("TIPESTPRO");
                req.setTransaccionEventoEstado("TIPESTACT");
                System.out.println("Consulta cliente en el CORE - cliente inactivo");
                respEvento = insertaEvento(req);
                con.close();
                if (con1 != null) {
                    con1.close();
                }
                sw = 0;
                Accion.clear();
                FechaHora.clear();
                Linea.clear();
                TipoAccion.clear();
                Campo.clear();
                Dato.clear();
                TipoCampo.clear();
                LineaAdicional.clear();
                TipoRegistroAdicional.clear();
                ValorAdicional.clear();
                return response;
            }

        } catch (Exception ex) {
            System.out.println("*** ejecuta Update 6 *****");
            String resp = update(con, transactionId, ERROR, "0", "NO");//jeta hoy
            try {
                con.close();
                if (con1 != null) {
                    con1.close();
                }
                sw = 0;
//                ex.printStackTrace();
            } catch (SQLException ex1) {
                deta0.setCodigoRetornoGlobal("9985");
                deta0.setMensaje(ex.getMessage());
                deta0.setReferencia("");
                deta1.add(detalleColeccion);
                deta2.add(deta0);
//                deta.setDetalle(deta1);
                deta.setEstadoGlobal(deta2);
                deta00.setDetalleItem(deta1);
                detax.add(deta00);
                deta.setDetalleColeccion(detax);
                response.add(deta);
                Accion.clear();
                FechaHora.clear();
                Linea.clear();
                TipoAccion.clear();
                Campo.clear();
                Dato.clear();
                TipoCampo.clear();
                LineaAdicional.clear();
                TipoRegistroAdicional.clear();
                ValorAdicional.clear();
                return response;
            }

            System.out.println("************* sw entro dos ");
            deta0.setCodigoRetornoGlobal("9984");
            deta0.setMensaje(ex.getMessage());
            deta0.setReferencia("");
            deta1.add(detalleColeccion);
            deta2.add(deta0);
            deta00.setDetalleItem(deta1);
            detax.add(deta00);
            deta.setDetalleColeccion(detax);
            deta.setEstadoGlobal(deta2);
            response.add(deta);
            sw = 0;
            Accion.clear();
            FechaHora.clear();
            Linea.clear();
            TipoAccion.clear();
            Campo.clear();
            Dato.clear();
            TipoCampo.clear();
            LineaAdicional.clear();
            TipoRegistroAdicional.clear();
            ValorAdicional.clear();
            return response;
        }
//        String result = "Received transferencia: "
//                + "linea: " + transferenciaItem.getLinea()
//                + ", fechaHora: " + transferenciaItem.getFechaHora()
//                + ", tipoAccion: " + transferenciaItem.getTipoAccion()
//                + ", accion: " + transferenciaItem.getAccion()
//                + ", campo: " + campoItem.getCampo()
//                + ", tipoCampo: " + campoItem.getTipoCampo()
//                + ", dato: " + campoItem.getDato();

    }
//</editor-fold>    

//<editor-fold defaultstate="collapsed" desc="public method: Valida WS y Crea archivo MT103 : processTransferenciaMT103">
    @WebMethod(operationName = "procesaTransferenciaMT103")
    public RespuestaTransferenciaMT103 procesaTransferenciaMT103(@WebParam(name = "transactionId") String transactionId,
            @WebParam(name = "aplicationId") String aplicationId,
            @WebParam(name = "paisId") String paisId,
            @WebParam(name = "empresaId") String empresaId,
            @WebParam(name = "regionId") String regionId,
            @WebParam(name = "canalId") String canalId,
            @WebParam(name = "version") String version,
            @WebParam(name = "llaveSesion") String llaveSesion,
            @WebParam(name = "usuarioId") String usuarioId,
            @WebParam(name = "accion") String accion,
            @WebParam(name = "token") String token,
            @WebParam(name = "clienteCoreId") String clienteCoreId,
            @WebParam(name = "TipoIdentificacion") String TipoIdentificacion,
            @WebParam(name = "identificacion") String identificacion,
            @WebParam(name = "terminal") String terminal,
            @WebParam(name = "pantalla") String pantalla,
            @WebParam(name = "paso") String paso,
            @WebParam(name = "Operacion") String Operacion,
            @WebParam(name = "AreaBancaria") String AreaBancaria,
            @WebParam(name = "Cuenta") String Cuenta,
            @WebParam(name = "Comentario") String Comentario,
            @WebParam(name = "ValorTransaccion") String ValorTransaccion,
            @WebParam(name = "ValorComision") String ValorComision,
            //            @WebParam(name = "ValorComisionBANI") String ValorComisionBANI,
            @WebParam(name = "parametroAdicional") parametroAdicionalColeccion parametroAdicional,
            @WebParam(name = "TransferenciaColeccion") TransferenciaColeccion TransferenciaColeccion
    ) {
        RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
        InputStream trama = null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new java.util.Date());
        System.out.println("**** Metodo  *****       procesaTransferenciaMT103        ");//z
        try {
            int Estado = 0;
            String mensaje = null;
            String nombre = null;
            String Nombre = null;
            String ACCOUNT = null;
            String NombreBeneficiario = null;
            String NumCliente = null;
            String Id = null;
            String TipoId = null;
            String TipoPersona = null;
            String CodPaisOrdenante = null;
            String CodPaisBeneficiario = null;
            String DesPais = null;
            String Direccion = null;
            String Telefono = null;
            String Correo = null;
            String SQL = null;
            Statement stmt = null;
            ResultSet rs = null;
            String respuesta = null;
            String ParametroCodigo = null;
            Connection con = null;
            Connection con1 = null;
            String ParametroValorDato = null;
            String ParametroValorDatoPorcentaje = null;
            String Valor = null;
            double totalValor = 0.0;

            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            DecimalFormat df = (DecimalFormat) nf;
            System.out.println("**** PASO1 Entro procesaTransferenciaMT103 ***** ");
            System.out.println("**** transactionId  ***** " + transactionId);
            System.out.println("**** aplicationId  ***** " + aplicationId);
            System.out.println("**** Accion ***** procesaTransferenciaMT103 : " + accion);//
            System.out.println("**** Cuenta ***** " + Cuenta);
            System.out.println("**** ValorTransaccion ***** " + ValorTransaccion);
            System.out.println("**** ValorComision ***** " + ValorComision);
            System.out.println("**** AreaBancaria ***** " + AreaBancaria);
            System.out.println("**** identificacion ***** " + identificacion);
            System.out.println("**** paso ***** " + paso);
            System.out.println("**** Operacion ***** " + Operacion);

//            String coincidencia = "";
//            String valorCoincidencia = "";
//            String Nocoincidencia = "";
//            String NovalorCoincidencia = "";
            //        DBConnectMotor coneccionMotor = new DBConnectMotor();
//        con = coneccionMotor.obtenerConeccion();
            String NUM_TRANSFERENCIA = null;
//            String IDENTIDAD_ORDENANTE = null;
//            String NUM_TRANSFERENCIA = null;
//            String IDENTIDAD_ORDENANTE = null;
            String CUENTA_ORDENANTE = null;
            String NOMBRE_ORDENANTE = "?";
            String DIRECCION_ORDENANTE1 = "";
            String DIRECCION_ORDENANTE2 = "";
            String DIRECCION_ORDENANTE3 = "";
            String BIC_BANCO = null;
            String TIPO_PARTICIPANTE = "?";
            String MONEDA = null;
            String IDENTIDAD_CLIENTE_BENEFICIARIO = "?";
            String MOTIVO1 = "?";
            String MOTIVO2 = "?";
            String MOTIVO3 = "?";
            String MOTIVO4 = "?";
            String CUENTA_BENEFICIARIO = "?";
            String NOMBRE_BENEFICIARIO = "?";
            String DIRECCION_BENEFICIARIO = "?";
            String FECHA = "?";
            String CODIGO_RETORNO = null;
            String DESCRIPCION_ERROR = null;
            String DESCRIPCION_TRANSACCION = null;
            String DESCRIPCION_TRANSACCION_SIN = "?";
            String NUM_TRANSFERENCIA_MT102 = null;
            String TIPO_MENSAGE = null;
            String TIPO_TRANSACCION = "?";
            String ESTADO = null;
            String BIC_BANCO_ORDENANTE = "?";
            String BIC_BANCO_ORDENANTE52A = "?";
            String BIC_BANCO_ORDENANTE52D = "?";
            String BIC_BANCO_INTERMEDIARIO56A = "?";
            String BIC_BANCO_INTERMEDIARIO56D = "?";
            String DIRECCION_BANCO_INTERMEDIARIO1 = "?";
            String DIRECCION_BANCO_INTERMEDIARIO2 = "?";
            String DIRECCION_BANCO_INTERMEDIARIO3 = "?";
            String NOMBRE_BANCO_INTERMEDIARIO56D = "?";
            String REFERENCIA20 = "?";
            String BIC_BANCO_BENEFICIARIO = "?";
            String HEADER3 = "?";
            String BIC_BANCO_BENEFICIARIO57A = "?";
            String BIC_BANCO_BENEFICIARIO57D = "?";
            String NOMBRE_BANCO_BENEFICIARIO57D = "?";
            String BIC_BANCO_RECIBE = null;
            String BIC_BANCO_ENVIA = null;
            String CODIGO_OPERACION = null;
            String FECHA_VALOR = null;
            String MONTO = null;
            String LOGICAL_TERMINAL = "?";
            String LOGICAL_TERMINAL2 = "?";
            int Referencia = 1;
            String NUMERO_CUENTA = "?";
            String NOMBRE_CLIENTE = "?";
            String DIRECCION_CLIENTE = "?";
            String TIPO_CUENTA = "?";
            String FECHA_NACIMIENTO = "?";
            String NUMERO_CUENTA_BENEFICIARIO = "?";
            String NOMBRE_CLIENTE_BENEFICIARIO = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO1 = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO2 = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO3 = "?";
            String DIRECCION_BANCO_BENEFICIARIO1 = "?";
            String DIRECCION_BANCO_BENEFICIARIO2 = "?";
            String DIRECCION_BANCO_BENEFICIARIO3 = "?";
            String NOMBRE_BANCO_ORDENANTE = "?";
            String DIRECCION_BANCO_ORDENANTE1 = "?";
            String DIRECCION_BANCO_ORDENANTE2 = "?";
            String DIRECCION_BANCO_ORDENANTE3 = "?";
            String TIPO_CUENTA_BENEFICIARIO = "?";
            String SHAOUR = "?";
            String VALUEBANI = "";
            String VALUEOUR = "";
            String CODIGO_TTC = "?";
            String SIN = null;
            String NUMERO_IDENTIFICACION = "?";
            String pattern10 = "0000000000";
            String pattern7 = "0000000";
            long numeroControl = 0;
            df.applyPattern(pattern10);
            String numeroControlString = df.format(Referencia);
            df.applyPattern(pattern7);
            String numeroControlString7 = df.format(Referencia);
            String path = "";
            String user = "";
            String user2 = "";
            try {
                HoraInicioAplicativo = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(cal);
                System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
            } catch (DatatypeConfigurationException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("********** DatatypeConfigurationException ********** : " + ex);
            }
//            if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME")) {
//                NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(i);
//                System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO ***** " + NOMBRE_CLIENTE_BENEFICIARIO);
//            }
            ArrayList<String> Campo = new ArrayList<>();
            ArrayList<String> Dato = new ArrayList<>();
            ArrayList<String> TipoCampo = new ArrayList<>();
            System.out.println("********** accion.equals(\"O\") || accion.equals(\"o\" ********** : " + accion.toString());
            if (accion.equals("O") || accion.equals("o") || accion.equals("I") || accion.equals("i")) {
                System.out.println("**** PASO5a ***** " + TransferenciaColeccion.getTransferenciaItems().size());
                for (int i = 0; i < TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size(); i++) {
                    Campo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getCampo());
                    Dato.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getDato());
                    TipoCampo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getTipoCampo());
//            BuscaInfoCuenta busca = new BuscaInfoCuenta();
                    if (Campo.get(i).equals("TIPO_PARTICIPANTE")) {
                        TIPO_PARTICIPANTE = Dato.get(i);
                        System.out.println("**** TIPO_PARTICIPANTE ***** " + TIPO_PARTICIPANTE);
                    }
//                if (Campo.get(i).equals("BIC_BANCO_ENVIA") && TipoCampo.get(i).equals("BIC")) {
//                    BIC_BANCO_ENVIA = Dato.get(i);
//                    System.out.println("**** BIC_BANCO_ENVIA ***** " + BIC_BANCO_ENVIA);
//                }
//                if (Campo.get(i).equals("BIC_BANCO_RECIBE") && TipoCampo.get(i).equals("BIC")) {
//                    BIC_BANCO_RECIBE = Dato.get(i);
//                    System.out.println("**** BIC_BANCO_RECIBE ***** " + BIC_BANCO_RECIBE);
//                }
//=====================================================================
                    try {
                        HoraInicioEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
//                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                    }
                    if (Campo.get(i).equals("1")) {
                        BIC_BANCO_ORDENANTE = Dato.get(i);
                        System.out.println("**** BIC_BANCO_ORDENANTE ***** " + BIC_BANCO_ORDENANTE);
                    }
                    if (Campo.get(i).equals("2")) {
                        BIC_BANCO_BENEFICIARIO = Dato.get(i);
                        System.out.println("**** BIC_BANCO_BENEFICIARIO ***** " + BIC_BANCO_BENEFICIARIO);
                    }
                    if (Campo.get(i).equals("3")) {
                        HEADER3 = Dato.get(i);
                        System.out.println("**** HEADER 3 ***** " + HEADER3);
                    }
                    if (Campo.get(i).equals("20")) {
                        REFERENCIA20 = Dato.get(i);
                        System.out.println("**** REFERENCIA20 ***** " + REFERENCIA20);
                    }
                    if (Campo.get(i).equals("23B")) {
                        // if (Campo.get(i).equals("23B") && TipoCampo.get(i).equals("CRED")) {
                        CODIGO_OPERACION = Dato.get(i);
                        System.out.println("**** CODIGO_OPERACION. ***** " + CODIGO_OPERACION);
                    }
                    if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("DATE")) {
                        FECHA_VALOR = Dato.get(i);
                        System.out.println("**** FECHA_VALOR ***** " + FECHA_VALOR);
                    }

                    if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("CURRENCY")) {
                        MONEDA = Dato.get(i);
                        System.out.println("**** CURRENCY ***** " + MONEDA);
                    }
                    if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("AMOUNT")) {
                        MONTO = Dato.get(i);
                        System.out.println("**** AMOUNT con punto decimal***** " + MONTO);
                        MONTO = MONTO.replace(".", ",");
                        System.out.println("**** AMOUNT con coma decimal***** " + MONTO);
                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ACCOUNT")) {
                        NUMERO_CUENTA = Dato.get(i);
                        System.out.println("**** ACCOUNT ***** " + NUMERO_CUENTA);
                    }

//===================================================
//===================================================
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
//                        NOMBRE_ORDENANTE = Dato.get(i).substring(0, 35);
                        NOMBRE_ORDENANTE = Dato.get(i);
                        int longit = NOMBRE_ORDENANTE.length();
                        System.out.println("**** NOMBRE_ORDENANTE longitud ***** " + longit);
                        if (longit >= 35) {
                            NOMBRE_ORDENANTE = NOMBRE_ORDENANTE.substring(0, 35);
                        } else {
                            NOMBRE_ORDENANTE = NOMBRE_ORDENANTE.substring(0, longit);
                        }
                        System.out.println("**** NOMBRE_ORDENANTE Substrig***** " + NOMBRE_ORDENANTE);
                    }
//================================================Nuevo octubre 2024==================================================================                
//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESS")) {
//                    DIRECCION_ORDENANTE = Dato.get(i);
//                    System.out.println("**** DIRECCION_ORDENANTE ***** " + DIRECCION_ORDENANTE);
//                }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_ORDENANTE1 = Dato.get(i);
                        if (DIRECCION_ORDENANTE1.length() >= 35) {
                            DIRECCION_ORDENANTE1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_ORDENANTE1 = Dato.get(i);
                        }
                        DIRECCION_ORDENANTE1 = validaString.sanitizeString(DIRECCION_ORDENANTE1);
                        System.out.println("**** DIRECCION_ORDENANTE1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_ORDENANTE1);
                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_ORDENANTE2 = Dato.get(i);
                        if (DIRECCION_ORDENANTE2.length() >= 35) {
                            DIRECCION_ORDENANTE2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_ORDENANTE2 = Dato.get(i);
                        }
                        DIRECCION_ORDENANTE2 = validaString.sanitizeString(DIRECCION_ORDENANTE2);
                        System.out.println("**** DIRECCION_ORDENANTE2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_ORDENANTE2);

                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_ORDENANTE3 = Dato.get(i);
                        if (DIRECCION_ORDENANTE3.length() >= 35) {
                            DIRECCION_ORDENANTE3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_ORDENANTE3 = Dato.get(i);
                        }
                        DIRECCION_ORDENANTE3 = validaString.sanitizeString(DIRECCION_ORDENANTE3);
                        System.out.println("**** DIRECCION_ORDENANTE3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_ORDENANTE3);
                    }
//================================================Nuevo octubre 2024==================================================================                                

//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("TC")) {
//                    TIPO_CUENTA = Dato.get(i);
//                    System.out.println("**** TIPO_CUENTA ***** " + TIPO_CUENTA);
//                }
//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("FN")) {
//                    FECHA_NACIMIENTO = Dato.get(i);
//                    System.out.println("**** FECHA_NACIMIENTO ***** " + FECHA_NACIMIENTO);
//                }
//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NI")) {
//                    NUMERO_IDENTIFICACION = Dato.get(i);
//                    System.out.println("**** NUMERO_IDENTIFICACION ***** " + NUMERO_IDENTIFICACION);
//                }
                    if (Campo.get(i).equals("52A") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_ORDENANTE52A = Dato.get(i);
                        System.out.println("**** BIC_BANCO_ORDENANTE52A ***** " + BIC_BANCO_ORDENANTE52A);
                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_ORDENANTE52D = Dato.get(i);
                        System.out.println("**** ABAOCUENTA52D ***** " + BIC_BANCO_ORDENANTE52D);
                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_BANCO_ORDENANTE = Dato.get(i);
                        System.out.println("**** NOMBRE_BANCO_ORDENANTE ***** " + NOMBRE_BANCO_ORDENANTE);
                    }

//================================================Nuevo octubre 2024==================================================================                
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_ORDENANTE1 = Dato.get(i);
                        if (DIRECCION_BANCO_ORDENANTE1.length() >= 35) {
                            DIRECCION_BANCO_ORDENANTE1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_ORDENANTE1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_ORDENANTE1 = validaString.sanitizeString(DIRECCION_BANCO_ORDENANTE1);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_ORDENANTE1);
                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_ORDENANTE2 = Dato.get(i);
                        if (DIRECCION_BANCO_ORDENANTE2.length() >= 35) {
                            DIRECCION_BANCO_ORDENANTE2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_ORDENANTE2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_ORDENANTE2 = validaString.sanitizeString(DIRECCION_BANCO_ORDENANTE2);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_ORDENANTE2);

                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_ORDENANTE3 = Dato.get(i);
                        if (DIRECCION_BANCO_ORDENANTE3.length() >= 35) {
                            DIRECCION_BANCO_ORDENANTE3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_ORDENANTE3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_ORDENANTE3 = validaString.sanitizeString(DIRECCION_BANCO_ORDENANTE3);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_ORDENANTE3);
                    }
//================================================Nuevo octubre 2024 end==================================================================
                    xx++;
                    System.out.println("Si va a preguntar por el 56A : " + xx);
                    if (Campo.get(i).equals("56A") && TipoCampo.get(i).equals("IDENTIFIER")) {
                        BIC_BANCO_INTERMEDIARIO56A = Dato.get(i);
                        System.out.println("**** BIC_BANCO_INTERMEDIARIO56A *****z " + BIC_BANCO_INTERMEDIARIO56A);
                    }
                    if (Campo.get(i).equals("57A") && TipoCampo.get(i).equals("IDENTIFIER")) {
                        BIC_BANCO_BENEFICIARIO57A = Dato.get(i);
                        System.out.println("**** BIC_BANCO_BENEFICIARIO57A ***** " + BIC_BANCO_BENEFICIARIO57A);
                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_INTERMEDIARIO56D = Dato.get(i);
                        System.out.println("**** BIC_BANCO_INTERMEDIARIO1 ***** " + BIC_BANCO_INTERMEDIARIO56D);
                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_BANCO_INTERMEDIARIO56D = Dato.get(i);
                        System.out.println("**** NOMBRE_BANCO_INTERMEDIARIO56D ***** " + NOMBRE_BANCO_INTERMEDIARIO56D);
                    }
                    //     Cambio del dia 13 de noviembre 2024 JETA
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO1.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO1 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO1);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO1);
                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO2.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO2 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO2);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO2);

                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO3.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO3 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO3);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO3);
                    }
                    //     Fin Cambio del dia 13 de noviembre 2024 JETA
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_BENEFICIARIO57D = Dato.get(i);
                        System.out.println("**** BIC_BANCO_BENEFICIARIO57D ***** " + BIC_BANCO_BENEFICIARIO57D);
                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_BANCO_BENEFICIARIO57D = Dato.get(i);
                        System.out.println("**** NOMBRE_BANCO_BENEFICIARIO57D ***** " + NOMBRE_BANCO_BENEFICIARIO57D);
                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO1.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO1 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO1);
                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO2.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO2 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO2);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO2);

                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO3.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO3 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO3);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO3);
                    }
                    //     Fin Cambio del dia 13 de noviembre 2024 JETA
//================================================Nuevo octubre 2024==================================================================                
//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESS")) {
//                    DIRECCION_ORDENANTE = Dato.get(i);
//                    System.out.println("**** DIRECCION_ORDENANTE ***** " + DIRECCION_ORDENANTE);
//                }
/*     Cambio del dia 13 de noviembre 2024 JETA
                    if (Campo.get(i).equals("56A") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO1.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO1 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO1);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO1);
                    }
                    if (Campo.get(i).equals("56A") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO2.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO2 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO2);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO2);

                    }
                    if (Campo.get(i).equals("56A") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO3.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO3 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO3);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO3);
                    }
                     */
//================================================Nuevo octubre 2024 end==================================================================                     
//================================================Nuevo octubre 2024==================================================================                
/*     Cambio del dia 13 de noviembre 2024 JETA
                    if (Campo.get(i).equals("57A") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO1.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO1 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO1);
                    }
                    if (Campo.get(i).equals("57A") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO2.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO2 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO2);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO2);

                    }
                    if (Campo.get(i).equals("57A") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO3.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO3 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO3);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO3);
                    }
                     */
//================================================Nuevo octubre 2024 end================================================================== 		                    
                    if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ACCOUNT")) {
                        NUMERO_CUENTA_BENEFICIARIO = Dato.get(i);
                        System.out.println("**** NUMERO_CUENTA_BENEFICIARIO  ACCOUNT ***** " + NUMERO_CUENTA_BENEFICIARIO);
                    }

                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(i);
                        System.out.println("**** NombreBeneficiario Antes Aprobar***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                        NOMBRE_CLIENTE_BENEFICIARIO = validaString.sanitizeString(NOMBRE_CLIENTE_BENEFICIARIO);
//======================================================
                        int longit = NOMBRE_CLIENTE_BENEFICIARIO.length();
                        System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO longitud ***** " + longit);
                        if (longit >= 35) {
                            NOMBRE_CLIENTE_BENEFICIARIO = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 35);
                        } else {
                            NOMBRE_CLIENTE_BENEFICIARIO = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, longit);
                        }

//======================================================
                        System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO Despues Aprobar***** " + NOMBRE_CLIENTE_BENEFICIARIO);
//                        System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO ***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                    }
//                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESS")) {
//                        DIRECCION_CLIENTE_BENEFICIARIO = Dato.get(i);
//                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO ***** " + DIRECCION_CLIENTE_BENEFICIARIO);
//                    }
//================================================Nuevo octubre 2024 59A==================================================================                
//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESS")) {
//                    DIRECCION_ORDENANTE = Dato.get(i);
//                    System.out.println("**** DIRECCION_ORDENANTE ***** " + DIRECCION_ORDENANTE);
//                }
                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESSLN1") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_CLIENTE_BENEFICIARIO1 = Dato.get(i);
                        if (DIRECCION_CLIENTE_BENEFICIARIO1.length() >= 35) {
                            DIRECCION_CLIENTE_BENEFICIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_CLIENTE_BENEFICIARIO1 = Dato.get(i);
                        }
                        DIRECCION_CLIENTE_BENEFICIARIO1 = validaString.sanitizeString(DIRECCION_CLIENTE_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_CLIENTE_BENEFICIARIO1);
                    }
                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESSLN2") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_CLIENTE_BENEFICIARIO2 = Dato.get(i);
                        if (DIRECCION_CLIENTE_BENEFICIARIO2.length() >= 35) {
                            DIRECCION_CLIENTE_BENEFICIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_CLIENTE_BENEFICIARIO2 = Dato.get(i);
                        }
                        DIRECCION_CLIENTE_BENEFICIARIO2 = validaString.sanitizeString(DIRECCION_CLIENTE_BENEFICIARIO2);
                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_CLIENTE_BENEFICIARIO2);

                    }
                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESSLN3") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_CLIENTE_BENEFICIARIO3 = Dato.get(i);
                        if (DIRECCION_CLIENTE_BENEFICIARIO3.length() >= 35) {
                            DIRECCION_CLIENTE_BENEFICIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_CLIENTE_BENEFICIARIO3 = Dato.get(i);
                        }
                        DIRECCION_CLIENTE_BENEFICIARIO3 = validaString.sanitizeString(DIRECCION_CLIENTE_BENEFICIARIO3);
                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_CLIENTE_BENEFICIARIO3);
                    }
//================================================Nuevo octubre 2024 end 59A==================================================================                     
//                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("TC")) {
//                    TIPO_CUENTA_BENEFICIARIO = Dato.get(i);
//                    System.out.println("**** TIPO_CUENTA_BENEFICIARIO ***** " + TIPO_CUENTA_BENEFICIARIO);
//                }
//                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NI")) {
//                    IDENTIDAD_CLIENTE_BENEFICIARIO = Dato.get(i);
//                    System.out.println("**** IDENTIDAD_CLIENTE_BENEFICIARIO ***** " + IDENTIDAD_CLIENTE_BENEFICIARIO);
//                }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN1")) {
                        MOTIVO1 = Dato.get(i);
                        if (MOTIVO1.length() >= 35) {
                            MOTIVO1 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO1 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO1 ***** " + MOTIVO1);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN2")) {
                        MOTIVO2 = Dato.get(i);
                        if (MOTIVO2.length() >= 35) {
                            MOTIVO2 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO2 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO2 ***** " + MOTIVO2);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN3")) {
                        MOTIVO3 = Dato.get(i);
                        if (MOTIVO3.length() >= 35) {
                            MOTIVO3 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO3 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO3 ***** " + MOTIVO3);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN4")) {
                        MOTIVO4 = Dato.get(i);
                        if (MOTIVO4.length() >= 35) {
                            MOTIVO4 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO4 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO4 ***** " + MOTIVO4);
                    }
                    if (Campo.get(i).equals("71A") && TipoCampo.get(i).equals("DETAIL")) {
                        SHAOUR = Dato.get(i);
                        System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                    }
                    if (Campo.get(i).equals("71A") && TipoCampo.get(i).equals("VALUEBANIUSD")) {
                        VALUEBANI = Dato.get(i);
                        System.out.println("**** 71A - VALUEBANI***** " + VALUEBANI);
                    }
                    if (Campo.get(i).equals("71A") && TipoCampo.get(i).equals("VALUEOURUSD")) {
                        VALUEOUR = Dato.get(i);
                        System.out.println("**** 71A - VALUEOUR***** " + VALUEOUR);
                    }
//                if (Campo.get(i).equals("72") && TipoCampo.get(i).equals("TTC")) {
//                    CODIGO_TTC = Dato.get(i);
//                    System.out.println("**** CODIGO_TTC ***** " + CODIGO_TTC);
//                }
//                if (Campo.get(i).equals("72") && TipoCampo.get(i).equals("SIN")) {
//                    DESCRIPCION_TRANSACCION_SIN = Dato.get(i);
//                    System.out.println("**** DESCRIPCION_TRANSACCION_SIN ***** " + DESCRIPCION_TRANSACCION_SIN);
//                }

                }
                ArrayList<ParametrosAdicionales> detay = new ArrayList<>();
                for (int i = 0; i < parametroAdicional.getParametroAdicionalItem().size(); i++) {
//            System.out.println("**** Accion ***** " + accion);//
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("CON")) {
                        valorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                        System.out.println("**** cuando es CON  ***** " + valorCoincidencia);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("SIN")) {
                        NovalorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                        System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ICI")) {
                        valorCoincidenciaICI = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                        System.out.println("**** cuando es ICI  ***** " + valorCoincidenciaICI);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ECX")) {
                        valorCoincidenciaECX = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                        System.out.println("**** cuando es ECX  ***** " + valorCoincidenciaECX);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ERR")) {
                        ERROR = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                        System.out.println("**** cuando es ERROR  ***** " + ERROR);//
                    }
//                LineaAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getLinea());
//                TipoRegistroAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getTipoRegistro());
//                ValorAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getValor());
                }
                System.out.println("**** DETAIL SHAOUR2***** " + SHAOUR);
//==============================Connect to dataBase=================================================================
                DBConnectMotor coneccionMotor = new DBConnectMotor();
                con = coneccionMotor.obtenerConeccion();
                stmt = con.createStatement();
                String sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'ENTORNO' and ParametroValorCodigo = 'TOSWIFT'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    ParametroValorDato = rs.getString("ParametroValorDato");
                }
//            con.close();
                System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
//===============================================================================================    
                System.out.println("**** pass1 ***** ");
//===============================================================================================        
//            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
//                text = "{1:F01BCEHHNT0AXXX" + numeroControlString + "}{2:I103BCEHHNT0XXXXN}{3:{103:SPH}}{4:\r\n";
//            } else {
                ValidarTags valida = new ValidarTags();
                valida.setConexion(con);
                valida.setLlaveSesion(llaveSesion);
                valida.setPantalla(pantalla);
                valida.setTerminal(terminal);
                valida.setUsuarioId(usuarioId);
                valida.setTransactionId(transactionId);
                System.out.println("**** pasox1 *****  ");
                System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                if (BIC_BANCO_ORDENANTE.equals("?")) {
                    deta = validaTags(valida, "BIC_BANCO_ORDENANTE = ?");
                    return deta;
                }
                System.out.println("**** pasox1 *****  ");
                if (BIC_BANCO_BENEFICIARIO.equals("?")) {
                    deta = validaTags(valida, "BIC_BANCO_BENEFICIARIO = ?");
                    con.close();
                    return deta;
                }
                System.out.println("**** pasox1 *****  ");
                if (HEADER3.equals("?")) {
                    deta = validaTags(valida, "HEADER3 = ?");
                }
                text = "{1:" + BIC_BANCO_ORDENANTE + "}{2:" + BIC_BANCO_BENEFICIARIO + "}{3:" + HEADER3 + "}{4:\r\n";
//                text = "{1:F01" + BIC_BANCO_ORDENANTE + numeroControlString + "}{2:I103" + BIC_BANCO_BENEFICIARIO + "N}{3:{103:SPH}}{4:\r\n";
                //text = "{1:F01" + LOGICAL_TERMINAL + numeroControlString + "}{2:I103" + LOGICAL_TERMINAL2 + "}{3:{103:SPH}}{4:\r\n";
//            }
                System.out.println("**** pasox1a *****  ");
                if (REFERENCIA20.equals("?")) {
                    deta = deta = validaTags(valida, "REFERENCIA20 = ?");
                    con.close();
                    return deta;
                }
                text = text + ":20:" + REFERENCIA20 + "\r\n";
                //text = text + ":20:" + BIC_BANCO_ENVIA.substring(0, 4) + numeroControlString7 + "\r\n";
                System.out.println("**** pasox2 *****  ");
                if (CODIGO_OPERACION.equals("?")) {
                    deta = validaTags(valida, "CODIGO_OPERACION 23B = ?");
                    con.close();
                    return deta;
                }
                text = text + ":23B:" + CODIGO_OPERACION + "\r\n";
                System.out.println("**** pasox3 *****  ");
                if (FECHA_VALOR.equals("?")) {
                    deta = validaTags(valida, "FECHA_VALOR = ?");
                    con.close();
                    return deta;
                }
                if (MONEDA.equals("?")) {
                    deta = validaTags(valida, "MONEDA = ?");
                    con.close();
                    return deta;
                }
                if (MONTO.equals("?")) {
                    deta = validaTags(valida, "MONTO = ?");
                    con.close();
                    return deta;
                }
                text = text + ":32A:" + FECHA_VALOR + MONEDA + MONTO + "\r\n";
                text = text + ":33B:" + MONEDA + MONTO + "\r\n";
                System.out.println("**** pasox4 *****  ");
                if (NUMERO_CUENTA.equals("?")) {
                    deta = validaTags(valida, "NUMERO_CUENTA = ?");
                    con.close();
                    return deta;
                }
                text = text + ":50K:/" + NUMERO_CUENTA.toString().trim() + "\r\n";
                System.out.println("**** pasox5 *****  ");
                if (NOMBRE_ORDENANTE.equals("?")) {
                    deta = validaTags(valida, "NOMBRE_ORDENANTE = ?");
                    con.close();
                    return deta;
                }
                if (NOMBRE_ORDENANTE.length() > 35) {
                    path = NOMBRE_ORDENANTE.substring(0, 35);
                    if (NOMBRE_ORDENANTE.length() > 70) {
                        System.out.println("**** pasox5a - path + user + user2 *****  ");
                        user = NOMBRE_ORDENANTE.substring(35, 70);
                        user2 = NOMBRE_ORDENANTE.substring(70, NOMBRE_ORDENANTE.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                        text = text + user2 + "\r\n";
                    } else {
                        System.out.println("**** pasox5b - path + user *****  ");
                        user = NOMBRE_ORDENANTE.substring(35, NOMBRE_ORDENANTE.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                    }

                } else {
                    System.out.println("**** pasox5c - path/NOMBRE_ORDENANTE  *****  ");
                    text = text + NOMBRE_ORDENANTE + "\r\n";
                }
                System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                path = "";
                user = "";
                user2 = "";
//                DIRECCION_ORDENANTE1 = "Solo para prueba";
                System.out.println("**** pasox6 *****  ");
//                if (DIRECCION_ORDENANTE1.equals("")) {
//                    deta = validaTags(valida, "DIRECCION_ORDENANTE1 = blanco");
//                    return deta;
//                }
//                path = DIRECCION_ORDENANTE1;
//                text = text + path + "\r\n";                
//                if (!DIRECCION_ORDENANTE2.equals("")) {
//                    user = DIRECCION_ORDENANTE2;
//                    text = text + user + "\r\n";
//                }
//                if (!DIRECCION_ORDENANTE3.equals("")) {
//                    user2 = DIRECCION_ORDENANTE3;
//                text = text + user2 + "\r\n";                    
//                }

//=========================================================  begin 50A ======================================                     
                System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                System.out.println("**** pasox6A *****  ");
                if (DIRECCION_ORDENANTE1.equals("") || DIRECCION_ORDENANTE1.equals(" ") || DIRECCION_ORDENANTE1.equals("?")) {
                    deta = validaTags(valida, "DIRECCION_ORDENANTE1 = (b)" + DIRECCION_ORDENANTE1);
//                        return deta;

                } else {
                    path = DIRECCION_ORDENANTE1;
                    text = text + path + "\r\n";
                }

                if (DIRECCION_ORDENANTE2.equals("") || DIRECCION_ORDENANTE2.equals(" ") || DIRECCION_ORDENANTE2.equals("?")) {
                    System.out.println("**** DIRECCION_ORDENANTE2 is = b  *****  " + DIRECCION_ORDENANTE2);
                } else {
                    user = DIRECCION_ORDENANTE2;
                    text = text + user + "\r\n";
                }
                if (DIRECCION_ORDENANTE3.equals("") || DIRECCION_ORDENANTE3.equals(" ") || DIRECCION_ORDENANTE3.equals("?")) {
                    System.out.println("**** DIRECCION_ORDENANTE3 is = b  *****  " + DIRECCION_ORDENANTE3);
                } else {
                    user2 = DIRECCION_ORDENANTE3;
                    text = text + user2 + "\r\n";
                }
//=========================================================  end 50A======================================                   
//                if (DIRECCION_ORDENANTE.length() > 35) {
//                    path = DIRECCION_ORDENANTE.substring(0, 35);
//                    if (DIRECCION_ORDENANTE.length() > 70) {
//                        System.out.println("**** pasox6a - path + user + user2 *****  ");
//                        user = DIRECCION_ORDENANTE.substring(35, 70);
//                        user2 = DIRECCION_ORDENANTE.substring(70, DIRECCION_ORDENANTE.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                        text = text + user2 + "\r\n";
//                    } else {
//                        System.out.println("**** pasox6b - path + user *****  ");
//                        user = DIRECCION_ORDENANTE.substring(35, DIRECCION_ORDENANTE.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                    }
//
//                } else {
//                    System.out.println("**** pasox6c - path/NOMBRE_ORDENANTE  *****  ");
//                    text = text + DIRECCION_ORDENANTE + "\r\n";
//                }NOMBRE_BANCO_ORDENANTE
                System.out.println("**** pasox7 *****  ");
                if (BIC_BANCO_ORDENANTE52A.equals("?")) {
                    System.out.println("BIC_BANCO_ORDENANTE52A.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_ORDENANTE52A = ?");
//                return deta;
                } else {
                    text = text + ":52A:" + BIC_BANCO_ORDENANTE52A.toString().trim() + "\r\n";
                }
                System.out.println("**** pasox7 *****  ");
                System.out.println("**** pasox7a *****  ");  //OJO Cambio hoy 13 de nov 2024 JETA
                if (BIC_BANCO_ORDENANTE52D.equals("?") || BIC_BANCO_ORDENANTE52D.equals("") || BIC_BANCO_ORDENANTE52D.equals(" ")) {
                    System.out.println("BIC_BANCO_ORDENANTE52D.equals(\"?\") = "+BIC_BANCO_ORDENANTE52D);
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                } else {
                    if (BIC_BANCO_ORDENANTE52D.length() == 9) {
                        text = text + ":52D://FW" + BIC_BANCO_ORDENANTE52D.toString().trim() + "\r\n";
                    } else {
                        text = text + ":52D:/" + BIC_BANCO_ORDENANTE52D.toString().trim() + "\r\n";
                    }
                    if (NOMBRE_BANCO_ORDENANTE.length() > 35) {
                        path = NOMBRE_BANCO_ORDENANTE.substring(0, 35);
                        if (NOMBRE_BANCO_ORDENANTE.length() > 70) {
                            System.out.println("**** pasox11a - path + user + user2 *****  ");
                            user = NOMBRE_BANCO_ORDENANTE.substring(35, 70);
                            user2 = NOMBRE_BANCO_ORDENANTE.substring(70, NOMBRE_BANCO_ORDENANTE.length());
                            text = text + path + "\r\n";
                            text = text + user + "\r\n";
                            text = text + user2 + "\r\n";
                        } else {
                            System.out.println("**** pasox12b - path + user *****  ");
                            user = NOMBRE_BANCO_ORDENANTE.substring(35, NOMBRE_BANCO_ORDENANTE.length());
                            text = text + path + "\r\n";
                            text = text + user + "\r\n";
                        }

                    } else {
                        System.out.println("**** pasox13c - path/NOMBRE_ORDENANTE  *****  ");
                        text = text + NOMBRE_BANCO_ORDENANTE + "\r\n";
                    }
//=========================================================  begin 52A ======================================                     
                    System.out.println("**** pasox7A *****  ");
                    if (NOMBRE_BANCO_ORDENANTE.equals("") || NOMBRE_BANCO_ORDENANTE.equals(" ") || NOMBRE_BANCO_ORDENANTE.equals("?")) {
                        deta = validaTags(valida, "NOMBRE_BANCO_ORDENANTE = (b)" + NOMBRE_BANCO_ORDENANTE);
                        System.out.println("**** NOMBRE_BANCO_ORDENANTE is = b  *****  " + NOMBRE_BANCO_ORDENANTE);
//                        return deta;
                    } else {
                        path = NOMBRE_BANCO_ORDENANTE;
                        text = text + NOMBRE_BANCO_ORDENANTE + "\r\n";
                    }
                    if (DIRECCION_BANCO_ORDENANTE1.equals("") || DIRECCION_BANCO_ORDENANTE1.equals(" ") || DIRECCION_BANCO_ORDENANTE1.equals("?")) {
                        deta = validaTags(valida, "DIRECCION_BANCO_ORDENANTE1 = (b)" + DIRECCION_BANCO_ORDENANTE1);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE1 is = b  *****  " + DIRECCION_BANCO_ORDENANTE1);
//                        return deta;
                    } else {
                        path = DIRECCION_BANCO_ORDENANTE1;
                        text = text + DIRECCION_BANCO_ORDENANTE1 + "\r\n";
                    }

                    if (DIRECCION_BANCO_ORDENANTE2.equals("") || DIRECCION_BANCO_ORDENANTE2.equals(" ") || DIRECCION_BANCO_ORDENANTE2.equals("?")) {
                        deta = validaTags(valida, "DIRECCION_BANCO_ORDENANTE2 = (b)" + DIRECCION_BANCO_ORDENANTE2);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE2 is = b  *****  " + DIRECCION_BANCO_ORDENANTE2);
                    } else {
                        user = DIRECCION_BANCO_ORDENANTE2;
                        text = text + user + "\r\n";
                    }
                    if (DIRECCION_BANCO_ORDENANTE3.equals("") || DIRECCION_BANCO_ORDENANTE3.equals(" ") || DIRECCION_BANCO_ORDENANTE3.equals("?")) {
                        deta = validaTags(valida, "DIRECCION_BANCO_ORDENANTE3 = (b)" + DIRECCION_BANCO_ORDENANTE3);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE3 is = b  *****  " + DIRECCION_BANCO_ORDENANTE3);
                    } else {
                        user2 = DIRECCION_BANCO_ORDENANTE3;
                        text = text + user2 + "\r\n";
                    }

//=========================================================  end 52A======================================                       
                }

                //OJO Fin Cambio hoy 13 de nov 2024 JETA
//                if (BIC_BANCO_ORDENANTE52D.equals("?")) {
//                    System.out.println("BIC_BANCO_ORDENANTE52D.equals(\"?\")  ");
//                } else {
//                    text = text + ":52D:" + BIC_BANCO_ORDENANTE52D.toString().trim() + "\r\n";
//                }
                //OJO Fin Cambio hoy 13 de nov 2024 JETA
                System.out.println("**** pasox8 *****  ");
                if (BIC_BANCO_INTERMEDIARIO56A.equals("?") || BIC_BANCO_INTERMEDIARIO56A.equals("") || BIC_BANCO_INTERMEDIARIO56A.equals(" ")) {
                    System.out.println("BIC_BANCO_INTERMEDIARIO56A.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_INTERMEDIARIO56A = ?");
//                return deta;
                } else {
                    text = text + ":56A:" + BIC_BANCO_INTERMEDIARIO56A.toString().trim() + "\r\n";
//=========================================================  begin 56A ======================================                     
                    System.out.println("**** pasox8A *****  ");
//                    if (DIRECCION_BANCO_INTERMEDIARIO1.equals("") || DIRECCION_BANCO_INTERMEDIARIO1.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_INTERMEDIARIO1 = (b)" + DIRECCION_BANCO_INTERMEDIARIO1);
//                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO1);
////                        return deta;
//                    } else {
//                        path = DIRECCION_BANCO_INTERMEDIARIO1;
//                        text = text + path + "\r\n";
//                    }
//
//                    if (DIRECCION_BANCO_INTERMEDIARIO2.equals("") || DIRECCION_BANCO_INTERMEDIARIO2.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO2.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO2);
//                    } else {
//                        user = DIRECCION_BANCO_INTERMEDIARIO2;
//                        text = text + user + "\r\n";
//                    }
//                    if (DIRECCION_BANCO_INTERMEDIARIO3.equals("") || DIRECCION_BANCO_INTERMEDIARIO3.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO3.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO3);
//                    } else {
//                        user2 = DIRECCION_BANCO_INTERMEDIARIO3;
//                        text = text + user2 + "\r\n";
//                    }
//=========================================================  end 56A======================================                       
                }
//=========================================================  end 52A====================================== 
                System.out.println("**** pasox8c *****  ");  //OJO Cambio hoy 13 de nov 2024 JETA
                if (BIC_BANCO_INTERMEDIARIO56D.equals("?") || BIC_BANCO_INTERMEDIARIO56D.equals("") || BIC_BANCO_INTERMEDIARIO56D.equals(" ")) {
                    System.out.println("BIC_BANCO_INTERMEDIARIO56D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                } else {
                    if (BIC_BANCO_INTERMEDIARIO56D.length() == 9) {
                        text = text + ":56D://FW" + BIC_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
                    } else {
                        text = text + ":56D:/" + BIC_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
                    }
//OJO Fin Cambio hoy 13 de nov 2024 JETA                
//AQUI
                    System.out.println("**** pasox9D *****  ");
                    if (NOMBRE_BANCO_INTERMEDIARIO56D.equals("?") || NOMBRE_BANCO_INTERMEDIARIO56D.equals("") || NOMBRE_BANCO_INTERMEDIARIO56D.equals(" ")) {
                        System.out.println("NOMBRE_BANCO_INTERMEDIARIO56D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                    } else {
                        text = text + NOMBRE_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
                    }
                    System.out.println("**** pasox9E *****  ");
                    if (DIRECCION_BANCO_INTERMEDIARIO1.equals("") || DIRECCION_BANCO_INTERMEDIARIO1.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO1);
//                        return deta;
                    } else {
                        path = DIRECCION_BANCO_INTERMEDIARIO1;
                        text = text + path + "\r\n";
                    }

                    if (DIRECCION_BANCO_INTERMEDIARIO2.equals("") || DIRECCION_BANCO_INTERMEDIARIO2.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO2.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO2);
                    } else {
                        user = DIRECCION_BANCO_INTERMEDIARIO2;
                        text = text + user + "\r\n";
                    }
                    if (DIRECCION_BANCO_INTERMEDIARIO3.equals("") || DIRECCION_BANCO_INTERMEDIARIO3.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO3.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO3);
                    } else {
                        user2 = DIRECCION_BANCO_INTERMEDIARIO3;
                        text = text + user2 + "\r\n";
                    }
//=========================================================  begin 56A ======================================  
                }
                System.out.println("**** pasox8D *****  ");
//                if (BIC_BANCO_INTERMEDIARIO56D.equals("?") || BIC_BANCO_INTERMEDIARIO56D.equals("") || BIC_BANCO_INTERMEDIARIO56D.equals(" ")) {
//                    System.out.println("BIC_BANCO_INTERMEDIARIO56D.equals(\"?\")  ");
//                } else {
//                    text = text + ":56D:" + BIC_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
//                }

//OJO REVIZAR
                System.out.println("**** pasox9 *****  ");
                if (BIC_BANCO_BENEFICIARIO57A.equals("?")) {
                    System.out.println("BIC_BANCO_BENEFICIARIO57A.equals(\"?\")  ");
                    deta = validaTags(valida, "BIC_BANCO_BENEFICIARIO57A = ?");
                    //OJO JETA modificacion 13 de noviembre 2024 comentar el return
//                    return deta;
                } else {
                    text = text + ":57A:" + BIC_BANCO_BENEFICIARIO57A.toString().trim() + "\r\n";
//=========================================================  begin 57A ======================================                     
                    System.out.println("**** pasox9A *****  ");
//                    if (DIRECCION_BANCO_BENEFICIARIO1.equals("") || DIRECCION_BANCO_BENEFICIARIO1.equals(" ") || DIRECCION_BANCO_BENEFICIARIO1.equals("?")) {
////                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
//                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO1);
////                        return deta;
//                    } else {
//                        path = DIRECCION_BANCO_BENEFICIARIO1;
//                        text = text + path + "\r\n";
//                    }
//
//                    if (DIRECCION_BANCO_BENEFICIARIO2.equals("") || DIRECCION_BANCO_BENEFICIARIO2.equals(" ") || DIRECCION_BANCO_BENEFICIARIO2.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO2);
//                    } else {
//                        user = DIRECCION_BANCO_BENEFICIARIO2;
//                        text = text + user + "\r\n";
//                    }
//                    if (DIRECCION_BANCO_BENEFICIARIO3.equals("") || DIRECCION_BANCO_BENEFICIARIO3.equals(" ") || DIRECCION_BANCO_BENEFICIARIO3.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO3);
//                    } else {
//                        user2 = DIRECCION_BANCO_BENEFICIARIO3;
//                        text = text + user2 + "\r\n";
//                    }
//=========================================================  end 57A======================================                    
                }
//=========================================================  end 52A======================================   
                System.out.println("**** pasox9D *****  ");
                if (BIC_BANCO_BENEFICIARIO57D.equals("?") || BIC_BANCO_BENEFICIARIO57D.equals("") || BIC_BANCO_BENEFICIARIO57D.equals(" ")) {
                    System.out.println("BIC_BANCO_BENEFICIARIO57D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                } else {
                    if (BIC_BANCO_BENEFICIARIO57D.length() == 9) {
                        text = text + ":57D://FW" + BIC_BANCO_BENEFICIARIO57D.toString().trim() + "\r\n";
                    } else {
                        text = text + ":57D:/" + BIC_BANCO_BENEFICIARIO57D.toString().trim() + "\r\n";
                    }
                    if (NOMBRE_BANCO_BENEFICIARIO57D.equals("") || NOMBRE_BANCO_BENEFICIARIO57D.equals(" ") || NOMBRE_BANCO_BENEFICIARIO57D.equals("?")) {
                        System.out.println("**** NOMBRE_BANCO_BENEFICIARIO57D is = b  *****  " + NOMBRE_BANCO_BENEFICIARIO57D);
                    } else {
                        text = text + NOMBRE_BANCO_BENEFICIARIO57D + "\r\n";
                    }
                    System.out.println("**** pasox9E *****  ");
                    if (DIRECCION_BANCO_BENEFICIARIO1.equals("") || DIRECCION_BANCO_BENEFICIARIO1.equals(" ") || DIRECCION_BANCO_BENEFICIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO1);
//                        return deta;
                    } else {
                        path = DIRECCION_BANCO_BENEFICIARIO1;
                        text = text + path + "\r\n";
                    }

                    if (DIRECCION_BANCO_BENEFICIARIO2.equals("") || DIRECCION_BANCO_BENEFICIARIO2.equals(" ") || DIRECCION_BANCO_BENEFICIARIO2.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO2);
                    } else {
                        user = DIRECCION_BANCO_BENEFICIARIO2;
                        text = text + user + "\r\n";
                    }
                    if (DIRECCION_BANCO_BENEFICIARIO3.equals("") || DIRECCION_BANCO_BENEFICIARIO3.equals(" ") || DIRECCION_BANCO_BENEFICIARIO3.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO3);
                    } else {
                        user2 = DIRECCION_BANCO_BENEFICIARIO3;
                        text = text + user2 + "\r\n";
                    }
                    //=========================================================  begin 56A ======================================     
                }

                System.out.println("**** pasox10 *****  ");
                if (NUMERO_CUENTA_BENEFICIARIO.equals("?")) {
                    deta = validaTags(valida, "NUMERO_CUENTA_BENEFICIARIO = ?");
                    con.close();
                    return deta;
                }
                text = text + ":59:/" + NUMERO_CUENTA_BENEFICIARIO.toString().trim() + "\r\n";
                System.out.println("**** pasox11 *****  ");
                if (NOMBRE_CLIENTE_BENEFICIARIO.equals("?")) {
                    deta = validaTags(valida, "NOMBRE_ORDENANTE = ?");
                    con.close();
                    return deta;
                }
                if (NOMBRE_CLIENTE_BENEFICIARIO.length() > 35) {
                    path = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 35);
                    if (NOMBRE_CLIENTE_BENEFICIARIO.length() > 70) {
                        System.out.println("**** pasox11a - path + user + user2 *****  ");
                        user = NOMBRE_CLIENTE_BENEFICIARIO.substring(35, 70);
                        user2 = NOMBRE_CLIENTE_BENEFICIARIO.substring(70, NOMBRE_CLIENTE_BENEFICIARIO.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                        text = text + user2 + "\r\n";
                    } else {
                        System.out.println("**** pasox12b - path + user *****  ");
                        user = NOMBRE_CLIENTE_BENEFICIARIO.substring(35, NOMBRE_CLIENTE_BENEFICIARIO.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                    }

                } else {
                    System.out.println("**** pasox13c - path/NOMBRE_ORDENANTE  *****  ");
                    text = text + NOMBRE_CLIENTE_BENEFICIARIO + "\r\n";
                }

                path = "";
                user = "";
                user2 = "";
                System.out.println("**** pasox14 *****  ");
//=========================================================  begin 59A ======================================                     
                System.out.println("**** pasox14A *****  ");
                System.out.println("**** pasox14A *****  " + DIRECCION_CLIENTE_BENEFICIARIO1);
                System.out.println("**** pasox14A *****  " + DIRECCION_CLIENTE_BENEFICIARIO2);
                System.out.println("**** pasox14A *****  " + DIRECCION_CLIENTE_BENEFICIARIO3);
                if (DIRECCION_CLIENTE_BENEFICIARIO1.equals("") || DIRECCION_CLIENTE_BENEFICIARIO1.equals(" ") || DIRECCION_CLIENTE_BENEFICIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO1 is = b  *****  " + DIRECCION_CLIENTE_BENEFICIARIO1);
//                        return deta;

                } else {
                    System.out.println("**** pasox14A DIRECCION_CLIENTE_BENEFICIARIO1 *****  " + DIRECCION_CLIENTE_BENEFICIARIO1);
                    path = DIRECCION_CLIENTE_BENEFICIARIO1;
                    System.out.println("**** pasox14A path *****  " + path);
                    text = text + path + "\r\n";
                    System.out.println("**** pasox14A text *****  " + text);
                }

                if (DIRECCION_CLIENTE_BENEFICIARIO2.equals("") || DIRECCION_CLIENTE_BENEFICIARIO2.equals(" ") || DIRECCION_CLIENTE_BENEFICIARIO2.equals("?")) {
                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO2 is = b  *****  " + DIRECCION_CLIENTE_BENEFICIARIO2);
                } else {
                    System.out.println("**** pasox14A DIRECCION_CLIENTE_BENEFICIARIO2 *****  " + DIRECCION_CLIENTE_BENEFICIARIO2);
                    user = DIRECCION_CLIENTE_BENEFICIARIO2;
                    System.out.println("**** pasox14A user *****  " + user);
                    text = text + user + "\r\n";
                    System.out.println("**** pasox14A text *****  " + text);
                }
                if (DIRECCION_CLIENTE_BENEFICIARIO3.equals("") || DIRECCION_CLIENTE_BENEFICIARIO3.equals(" ") || DIRECCION_CLIENTE_BENEFICIARIO3.equals("?")) {
                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO3 is = b  *****  " + DIRECCION_CLIENTE_BENEFICIARIO3);
                } else {
                    user2 = DIRECCION_CLIENTE_BENEFICIARIO3;
                    text = text + user2 + "\r\n";
                    System.out.println("**** pasox14A text *****  " + text);
                }
//=========================================================  end 59A======================================                   
//                if (DIRECCION_CLIENTE_BENEFICIARIO.equals("?")) {
//                    deta = validaTags(valida, "DIRECCION_CLIENTE_BENEFICIARIO = ?");
//                    return deta;
//                }
                System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                System.out.println("**** pasox14 *****  ");
//                if (DIRECCION_CLIENTE_BENEFICIARIO.length() > 35) {
//                    path = DIRECCION_CLIENTE_BENEFICIARIO.substring(0, 35);
//                    if (DIRECCION_CLIENTE_BENEFICIARIO.length() > 70) {
//                        System.out.println("**** pasox14a - path + user + user2 *****  ");
//                        user = DIRECCION_CLIENTE_BENEFICIARIO.substring(35, 70);
//                        user2 = DIRECCION_CLIENTE_BENEFICIARIO.substring(70, DIRECCION_CLIENTE_BENEFICIARIO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                        text = text + user2 + "\r\n";
//                    } else {
//                        System.out.println("**** pasox14b - path + user *****  ");
//                        user = DIRECCION_CLIENTE_BENEFICIARIO.substring(35, DIRECCION_CLIENTE_BENEFICIARIO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                    }
//
//                } else {
//                    System.out.println("**** pasox14c - path/DIRECCION_CLIENTE_BENEFICIARIO  *****  ");
//                    text = text + DIRECCION_CLIENTE_BENEFICIARIO + "\r\n";
//                }
                System.out.println("**** pasox15 *****  ");
                if (MOTIVO1.equals("?")) {
                    deta = validaTags(valida, "MOTIVO = ?");
                    con.close();
                    return deta;
                }
                path = "";
                user = "";
                user2 = "";
                if (MOTIVO1.equals("") || MOTIVO1.equals(" ") || MOTIVO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                    System.out.println("**** MOTIVO1 is = b  *****  " + MOTIVO1);
//                        return deta;
                } else {
                    MOTIVO1 = validaString.sanitizeString(MOTIVO1);
                    path = MOTIVO1;
                    text = text + ":70:" + path.toString().trim() + "\r\n";
//                    text = text + path + "\r\n";
                }

                if (MOTIVO2.equals("") || MOTIVO2.equals(" ") || MOTIVO2.equals("?")) {
                    System.out.println("**** MOTIVO2 is = b  *****  " + MOTIVO2);
                } else {
                    user = MOTIVO2;
                    text = text + user + "\r\n";
                }
                if (MOTIVO3.equals("") || MOTIVO3.equals(" ") || MOTIVO3.equals("?")) {
                    System.out.println("**** MOTIVO3 is = b  *****  " + MOTIVO3);
                } else {
                    user2 = MOTIVO3;
                    text = text + user2 + "\r\n";
                }
                if (MOTIVO4.equals("") || MOTIVO4.equals(" ") || MOTIVO4.equals("?")) {
                    System.out.println("**** MOTIVO4 is = b  *****  " + MOTIVO4);
                } else {
                    text = text + MOTIVO4 + "\r\n";
                }
//                if (MOTIVO1.length() > 35) {
//                    path = ":70:" + MOTIVO1.substring(0, 35);
//                    if (MOTIVO.length() > 70) {
//                        System.out.println("**** pasox15a - path + user + user2 *****  ");
//                        user = MOTIVO.substring(35, 70);
//                        user2 = MOTIVO.substring(70, MOTIVO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                        text = text + user2 + "\r\n";
//                    } else {
//                        System.out.println("**** pasox15b - path + user *****  ");
//                        user = MOTIVO.substring(35, MOTIVO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                    }
//
//                } else {
//                    System.out.println("**** pasox15c - path/DIRECCION_CLIENTE_BENEFICIARIO  *****  ");
//                    text = text + ":70:" + MOTIVO + "\r\n";
//                }
                System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                System.out.println("**** pasox16 *****  " + SHAOUR);
                if (SHAOUR.equals("?")) {
                    System.out.println("**** pasox16a SHAOUR *****  " + SHAOUR);
                    deta = validaTags(valida, "OUR = ?");
                    con.close();
                    return deta;

                }

                text = text + ":71A:" + SHAOUR + "\r\n";
                if (SHAOUR.equals("SHA")) {
                    text = text + ":71F:USD0,00" + "\r\n";
                }
//===============================================================================================
                if (accion.equals("O") || accion.equals("o")) {
                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                    System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                    req.setTransaccionEventoAccion("validaTags");
                    req.setTipoEventoCodigo("INSERT");
                    req.setTransaccionEventoInicio(HoraInicioEvento);
                    System.out.println("********** HoraInicioEvento ********** : " + HoraInicioEvento);
                    req.setTransaccionUsuarioCodigo(usuarioId);
                    req.setTransaccionEventoDispositivo(pantalla);
                    req.setTransaccionEventoIP(terminal);
                    req.setTransaccionEventoSession(llaveSesion);
                    req.setTransaccionEventoComentario("validaTags Todo OK");
                    cal.setTime(new java.util.Date());
                    try {
                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
//                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                    }
                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                    System.out.println("********** HoraFinalizaEvento ********** : " + HoraFinalizaEvento);
                    req.setTransaccionEventoGestion("TIPESTPRO");
                    req.setTransaccionEventoEstado("TIPESTACT");
                    System.out.println("validaTags ** OK **");
                    respEvento = insertaEvento(req);
                }

//            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
//                text = "{1:F01BCEHHNT0AXXX" + numeroControlString + "}{2:I103BCEHHNT0XXXXN}{3:{103:SPH}}{4:\r\n";
//            } else {
//                text = "{1:F01" + BIC_BANCO_ENVIA + numeroControlString + "}{2:I103" + BIC_BANCO_RECIBE + "N}{3:{103:SPH}}{4:\r\n";
//                //text = "{1:F01" + LOGICAL_TERMINAL + numeroControlString + "}{2:I103" + LOGICAL_TERMINAL2 + "}{3:{103:SPH}}{4:\r\n";
//            }
//            System.out.println("**** pass1a ***** ");
//            text = text + ":20:" + REFERENCIA20 + "\r\n";
////            text = text + ":20:" + BIC_BANCO_ENVIA.substring(0, 4) + numeroControlString7 + "\r\n";
//            text = text + ":23B:" + CODIGO_OPERACION + "\r\n";
//            System.out.println("**** pass1b ***** ");
//            
//            text = text + ":32A:" + FECHA_VALOR + MONEDA + MONTO + "\r\n";
//            System.out.println("**** pass1c ***** ");
//            text = text + ":50K:/" + NUMERO_CUENTA.toString().trim() + "\r\n";
//            System.out.println("**** pass1d ***** ");
//            if (NOMBRE_ORDENANTE.length() > 30) {
//                path = NOMBRE_ORDENANTE.substring(0, 30);
//                user = NOMBRE_ORDENANTE.substring(30, NOMBRE_ORDENANTE.length());
//System.out.println("**** pass1e ***** ");
//                text = text + "/NC/" + path + "\r\n";
//                text = text + "//" + user + "\r\n";
//            } else {
//                text = text + "/NC/" + NOMBRE_ORDENANTE + "\r\n";
//                System.out.println("**** pass1f ***** ");
//            }
//            System.out.println("**** pass2 ***** ");
//            if (DIRECCION_ORDENANTE.trim().length() > 30) {
//                path = DIRECCION_ORDENANTE.trim().substring(0, 30);
//                user = DIRECCION_ORDENANTE.trim().substring(30, DIRECCION_ORDENANTE.trim().length());
//                text = text + "/DR/" + path + "\r\n";
//                text = text + "//" + user + "\r\n";
//            } else {
//                text = text + "/DR/" + DIRECCION_ORDENANTE.trim() + "\r\n";
//            }
//            text = text + "/TC/" + TIPO_CUENTA + "/FN/" + FECHA_NACIMIENTO + "\r\n";
//            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
//                text = text + ":52A:" + BIC_BANCO_ORDENANTE + "\r\n";
//            }
//            System.out.println("**** pass3 ***** ");
//            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
//                text = text + ":57A:" + BIC_BANCO_BENEFICIARIO.toString().trim() + "\r\n";
//            }
//            text = text + ":59:/" + NUMERO_CUENTA_BENEFICIARIO.toString().trim() + "\r\n";
//            if (NOMBRE_CLIENTE_BENEFICIARIO.trim().length() > 30) {
//                path = NOMBRE_CLIENTE_BENEFICIARIO.trim().substring(0, 30);
//                user = NOMBRE_CLIENTE_BENEFICIARIO.trim().substring(30, NOMBRE_CLIENTE_BENEFICIARIO.trim().length());
//                text = text + "/NC/" + path + "\r\n";
//                text = text + "//" + user + "\r\n";
//            } else {
//                text = text + "/NC/" + NOMBRE_CLIENTE_BENEFICIARIO.trim() + "\r\n";
//            }
//            System.out.println("**** pass4 ***** ");
//            if (DIRECCION_CLIENTE_BENEFICIARIO.trim().length() > 30) {
//                path = DIRECCION_CLIENTE_BENEFICIARIO.trim().substring(0, 30);
//                user = DIRECCION_CLIENTE_BENEFICIARIO.trim().substring(30, DIRECCION_CLIENTE_BENEFICIARIO.trim().length());
//                text = text + "/DR/" + path + "\r\n";
//                text = text + "//" + user + "\r\n";
//            } else {
//                text = text + "/DR/" + DIRECCION_CLIENTE_BENEFICIARIO.trim() + "\r\n";
//            }
//            text = text + "/TC/" + TIPO_CUENTA_BENEFICIARIO.toString().trim() + "/NI/" + IDENTIDAD_CLIENTE_BENEFICIARIO.toString().trim() + "\r\n";
//            if (TIPO_TRANSACCION.equals("RETURN")) {
//                if (CODIGO_RETORNO.equals("AC01")) {
//                    text = text + ":70:/COR/AC01\r\n";
//                    text = text + "/DER/FORMATO DEL NUMERO DE CUENTA\r\n";
//                    text = text + "// ESPECIFICADO INCORRECTO\r\n";
//                }
//                text = text + ":71A:" + OUR + "\r\n";
//                text = text + ":72:/TTC/ZZZZ/SIN/DEVOLUCION ENTRE\r\n";
//                text = text + "// CLIENTES\r\n";
//            } else {
//                System.out.println("**** pass5 ***** ");
//                text = text + ":71A:" + OUR + "\r\n";
//                int longi = DESCRIPCION_TRANSACCION_SIN.trim().length();
//                System.out.println("**** pass6 ***** ");
//                if (DESCRIPCION_TRANSACCION_SIN.trim().length() > 17) {
//                    String descripcion = DESCRIPCION_TRANSACCION_SIN.trim().substring(0, 17);
//                    String descripcion2 = DESCRIPCION_TRANSACCION_SIN.trim().substring(17, longi);
//                    text = text + ":72:/TTC/803/SIN/" + descripcion + "\r\n";
//                    text = text + "//" + descripcion2.toString().trim() + "\r\n";
//                } else {
//                    text = text + ":72:/TTC/803/SIN/" + DESCRIPCION_TRANSACCION_SIN.trim().toString().trim() + "\r\n";
//                }
//                System.out.println("**** pass7 ***** ");
//            }
            }
            ArrayList<ParametrosAdicionales> detay = new ArrayList<>();
            for (int i = 0; i < parametroAdicional.getParametroAdicionalItem().size(); i++) {
//            System.out.println("**** Accion ***** " + accion);//
                if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("CON")) {
                    valorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                    System.out.println("**** cuando es CON  ***** " + valorCoincidencia);//
                }
                if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("SIN")) {
                    NovalorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                    System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                }
                if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ICI")) {
                    valorCoincidenciaICI = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                    System.out.println("**** cuando es ICI  ***** " + NovalorCoincidencia);//
                }
                if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ECX")) {
                    valorCoincidenciaECX = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                    System.out.println("**** cuando es ECX  ***** " + NovalorCoincidencia);//
                }
                if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ERR")) {
                    ERROR = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
                    System.out.println("**** cuando es ERR  ***** " + NovalorCoincidencia);//
                }
//                LineaAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getLinea());
//                TipoRegistroAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getTipoRegistro());
//                ValorAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getValor());
            }
            System.out.println("accion.equals(\"I\") || accion.equals(\"i\"");
            if (accion.equals("I") || accion.equals("i")) {
                System.out.println("**** PASO5xxx ***** " + TransferenciaColeccion.getTransferenciaItems().size());
                System.out.println("accion.equals(\"I\") || accion.equals(\"i\"  INICIA");
                for (int f = 0; f < TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size(); f++) {
                    Campo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getCampo());
                    Dato.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getDato());
                    TipoCampo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getTipoCampo());
                    System.out.println("****  tamano de el indice f ***** " + f);
                    if (Campo.get(f).equals("59") && TipoCampo.get(f).equals("NAME")) {
                        NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(f);
                        System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO Transferencia entrante***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                    }
//                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
////                        NOMBRE_ORDENANTE = Dato.get(i).substring(0, 35);
//                        NOMBRE_ORDENANTE = Dato.get(i);
//                        int longit = NOMBRE_ORDENANTE.length();
//                        System.out.println("**** NOMBRE_ORDENANTE longitud ***** " + longit);
//                        if (longit >= 35) {
//                            NOMBRE_ORDENANTE = NOMBRE_ORDENANTE.substring(0, 35);
//                        } else {
//                            NOMBRE_ORDENANTE = NOMBRE_ORDENANTE.substring(0, longit);
//                        }
//                        System.out.println("**** NOMBRE_ORDENANTE Substrig***** " + NOMBRE_ORDENANTE);
//                    }
                }
                DBConnectMotor coneccionMotor = new DBConnectMotor();
                con = coneccionMotor.obtenerConeccion();
                if (con == null) {//TIPESTECI    
                    System.out.println("**** la conexion es nula   ***** null ");//
//            sql = "update Transaccion set TransaccionGestionEstado = '" + valorCoincidenciaICI + "', TransaccionPaso = " + paso + "  where TransaccionSecuencia = " + transactionId;
                    deta.setCodigo("9998");
                    deta.setMensaje("Error conexion BD Motor");
                    deta.setTipo("ER");
                    Accion.clear();
                    FechaHora.clear();
                    Linea.clear();
                    TipoAccion.clear();
                    Campo.clear();
                    Dato.clear();
                    TipoCampo.clear();
                    LineaAdicional.clear();
                    TipoRegistroAdicional.clear();
                    ValorAdicional.clear();
                    return deta;
                }
                stmt = con.createStatement();
                String sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'ENTORNO' and ParametroValorCodigo = 'TOSWIFT'";
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    ParametroValorDato = rs.getString("ParametroValorDato");
                }
            }

//===============================================================================================
            System.out.println("PASO 7");
            text = text + "-}";
            System.out.println("**** MOT103 Generado *****  " + text);
            path = null;
//===============================================================================================
            System.out.println("Contabiliza -- webservicestc.WSNICPPSWIFTExecute()  Nuevo");
//===============================================================================================  
//OJO JETA
//            System.out.println("Pregunta si aplicationId.equals(\"X\")  : " + aplicationId);
//            if (!aplicationId.equals("X")) {
            // Ensure the string is at least 5 characters long

            cal = new GregorianCalendar();
            cal.setTime(new java.util.Date());
            HoraInicioAplicativo = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(cal);
            System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
            String algo0 = HoraInicioAplicativo.toString();
            String modifiedDate = algo0.replace("-", "");
            modifiedDate = modifiedDate.substring(0, 8);
            System.out.println("Fecha : " + modifiedDate);
            System.out.println("********** Por llama al servicio de Tasa de cambio ********** : ");
            try {
                System.out.println("WS Contab paso 0 ");
                webservicestc.WSNICTasaCambioExecute parameters = new webservicestc.WSNICTasaCambioExecute();
                System.out.println("WS Contab paso 1 ");
//                URL newEndpoint = new URL("http://niqaappcobros.adbancat.hn//WebServiceSTC/ws_nic_tasa_cambio.aspx");
//                QName qname = new QName("WebServiceSTC", "WS_NIC_Tasa_Cambio");
                System.out.println("WS Contab paso 2 ");
//                webservicestc.WSNICTasaCambio service = new webservicestc.WSNICTasaCambio(newEndpoint, qname);
                webservicestc.WSNICTasaCambio service = new webservicestc.WSNICTasaCambio();
                webservicestc.WSNICTasaCambioSoapPort port = service.getWSNICTasaCambioSoapPort();
                System.out.println("WS Contab paso 3 ");
                SDTWSNICTasaCambioPeticion algo = new SDTWSNICTasaCambioPeticion();
                System.out.println("WS Contab paso 4 ");
                SDTWSNICTasaCambioPeticion algo1 = new SDTWSNICTasaCambioPeticion();
                SDTWSNICTasaCambioRespuestaDetalle algo2 = new SDTWSNICTasaCambioRespuestaDetalle();
                System.out.println("WS Contab paso 5 ");
//                algo.setMonedaOrigen(MONEDA);
                if (MONEDA == null) {
                    MONEDA = "USD";
                    System.out.println("MONEDA = null Entrante " + MONEDA);
                }

                if (MONEDA.equals("USD")) {
                    algo.setMonedaOrigen("840");
                    System.out.println("setMonedaOrigen paso 3 840");
                } else {
                    System.out.println("setMonedaOrigen paso 3 558");
                    algo.setMonedaOrigen("558");
                }
//                System.out.println("setMonedaOrigen paso 3 " + MONEDA);
                algo.setMonedaDestino("558");
                System.out.println("setMonedaDestino paso 3a 558");
                algo.setFecha(modifiedDate);
                System.out.println("modifiedDate paso 3b " + modifiedDate);
                parameters.setPeticion(algo);
                System.out.println("WS Contab paso 3c ");
                webservicestc.WSNICTasaCambioExecuteResponse result = port.execute(parameters);
                System.out.println("WS Contab paso 4 : " + result.getRespuesta().getHeader().getCodigo());
                if (result.getRespuesta().getHeader().getCodigo().equals("0000") && result.getRespuesta().getHeader().getTipo().equals("OK")) {
                    System.out.println("WS Contab paso 4 " + result.getRespuesta().getHeader().getCodigo());
                    for (int ii = 0; ii < 3; ii++) {
                        if (result.getRespuesta().getDetail().getItem().get(ii).getTipoCambio().equals("M")) {
                            TasaCambioM = Double.valueOf(result.getRespuesta().getDetail().getItem().get(ii).getTasaCambio());
                            System.out.println("TasaCambioM : " + TasaCambioM);
                            System.out.println(ii);
                        }
                        if (result.getRespuesta().getDetail().getItem().get(ii).getTipoCambio().equals("V")) {
                            TasaCambioV = Double.valueOf(result.getRespuesta().getDetail().getItem().get(ii).getTasaCambio());
                            System.out.println("TasaCambioV : " + TasaCambioV);
                            System.out.println(ii);
                        }
                        if (result.getRespuesta().getDetail().getItem().get(ii).getTipoCambio().equals("C")) {
                            TasaCambioC = Double.valueOf(result.getRespuesta().getDetail().getItem().get(ii).getTasaCambio());
                            valorCordobas = Double.valueOf(ValorTransaccion) * TasaCambioC;
                            System.out.println("TasaCambioC : " + TasaCambioC);
                            System.out.println("valorCordobas : " + valorCordobas);
//                                System.out.println(ii);
                        }
                    }
                }

            } catch (Exception ex) {
                System.out.println("Exception : " + ex.getMessage());
                deta.setCodigo("9979A");
                deta.setTipo("ER");
                deta.setDetalle("error : " + ex.getMessage());
                return deta;
            }
//===================================== obtiene cuenta Mayor ==================================== begin 
            String CuentaMayor = null;
            stmt = con.createStatement();
            String sql = "SELECT * FROM MotorTransferencia.dbo.ParametroValor WHERE ParametroCodigo='BANCOLOCAL' AND ParametroValorCodigo='CUENTAMAYOR';";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CuentaMayor = rs.getString("ParametroValorDato");
            }
            System.out.println("Select a la tabla ParametroValor para obtener CuentaMayor : " + CuentaMayor);
//===================================== obtiene cuenta Mayor ==================================== end                 
            String MSGTYP = null;
            String MSGID = null;
            String MSGNR = null;
            String MSGV1 = null;
            String MSGV2 = null;
            String MSGV3 = null;
            String MSGV4 = null;
            String FLDNAME = null;

            ArrayList<String> AMSGTYP = new ArrayList<String>();
            ArrayList<String> AMSGID = new ArrayList<String>();
            ArrayList<String> AMSGNR = new ArrayList<String>();
            ArrayList<String> AMSGV1 = new ArrayList<String>();
            ArrayList<String> AMSGV2 = new ArrayList<String>();
            ArrayList<String> AMSGV3 = new ArrayList<String>();
            ArrayList<String> AMSGV4 = new ArrayList<String>();
            ArrayList<String> AFLDNAME = new ArrayList<String>();
            try {
                webservicestc.WSNICPPSWIFTExecute parameters = new webservicestc.WSNICPPSWIFTExecute();
                webservicestc.WSNICPPSWIFT service = new webservicestc.WSNICPPSWIFT();
                webservicestc.WSNICPPSWIFTSoapPort port = service.getWSNICPPSWIFTSoapPort();
                SDTPeticionHeader algo1 = new SDTPeticionHeader();
                SDTWSNICPPSwiftPeticion algo3 = new SDTWSNICPPSwiftPeticion();
                if (transactionId.length() >= 5) {
                    transaccionComentario5 = transactionId.substring(transactionId.length() - 5);
                    System.out.println("Last 5 digits: " + transaccionComentario5);
                } else {
                    transaccionComentario5 = transactionId;
                    System.out.println("The string is too short!  pero ahi va : " + transaccionComentario5);
                }
                if (accion.equals("I") || accion.equals("i")) {
                    System.out.println("NOMBRE_ORDENANTE: " + NOMBRE_ORDENANTE);
                    if (NOMBRE_ORDENANTE.length() <= 40) {
                        nombre40oMenos = NOMBRE_ORDENANTE;
                        System.out.println("nombre40oMenos <= 40: " + nombre40oMenos);
                    } else {
                        nombre40oMenos = NOMBRE_ORDENANTE.substring(0, 40);
                        System.out.println("Else nombre40oMenos 0, 40: " + nombre40oMenos);
                    }
                } else {
                    System.out.println("NOMBRE_CLIENTE_BENEFICIARIO: " + NOMBRE_CLIENTE_BENEFICIARIO);
                    if (NOMBRE_CLIENTE_BENEFICIARIO.length() <= 40) {
                        nombre40oMenos = NOMBRE_CLIENTE_BENEFICIARIO;
                        System.out.println("nombre40oMenos: " + nombre40oMenos);
                    } else {
                        nombre40oMenos = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 40);
                        System.out.println("Else nombre40oMenos: " + nombre40oMenos);
                    }
                }

                if (accion.equals("O") || accion.equals("o")) {
                    System.out.println("Comentario y es salida: " + nombre40oMenos);
                    algo3.setOperacion("0159");
                    Comentario = "W/T-" + transaccionComentario5 + "/" + nombre40oMenos;
                    System.out.println("Comentario y es salida: " + Comentario);
                    algo3.setTasaCambio(String.valueOf(TasaCambioV));
                    System.out.println("Valor TasaCambioV/Venta Cuando transferencia Salienta = " + TasaCambioV);
                } else {
                    System.out.println("Comentario y es entrada: " + nombre40oMenos);
                    algo3.setOperacion("5159");
                    Comentario = "I/T-" + transaccionComentario5 + "/" + nombre40oMenos;
                    System.out.println("Comentario y es entrada: " + Comentario);
                    algo3.setTasaCambio(String.valueOf(TasaCambioC));
                    System.out.println("Valor TasaCambioC/Compra Cuando transferencia Entrante = " + TasaCambioC);
                }
                System.out.println("algo3.setReferenciaExterna() = " + transactionId);
                algo3.setReferenciaExterna(transactionId);
                System.out.println("algo3.getOperacion() = " + algo3.getOperacion());
                algo3.setAreaBancaria(AreaBancaria);
                System.out.println("AreaBancaria = " + AreaBancaria);
                algo3.setCuentaMayor(CuentaMayor);
                System.out.println("Cuenta Mayor = " + CuentaMayor);     // cuenta mayor en duro 4/4/2025           
                algo3.setCuenta(Cuenta);
                System.out.println("Cuenta a contabilizar = " + Cuenta);
                algo3.setComentario(Comentario);
                System.out.println("Comentario = " + Comentario);
                algo3.setValorTransaccion(ValorTransaccion);
                System.out.println("ValorTransaccion ValorTransaccion = " + ValorTransaccion);
                algo3.setValorComision(VALUEOUR);
                System.out.println("ValorComision VALUEOUR = " + VALUEOUR);
                algo3.setValorComisionBANI(VALUEBANI);
                System.out.println("ValorComisionBANI VALUEBANI = " + VALUEBANI);

                parameters.setPeticion(algo3);
                // TODO process result here
                webservicestc.WSNICPPSWIFTExecuteResponse result = port.execute(parameters);
                System.out.println("Result = " + result);
                System.out.println("Resultado getCodigo = " + result.getRespuesta().getHeader().getCodigo());
                System.out.println("Resultado getTipo = " + result.getRespuesta().getHeader().getTipo());
                if (result.getRespuesta().getHeader().getCodigo().equals("0000") && result.getRespuesta().getHeader().getTipo().equals("OK")) {
                    mensaje = result.getRespuesta().getHeader().getMensaje();
//                    TagsContabilidad.add("Volvo");
//                    int j = 0;
                    for (int j = 0; j < result.getRespuesta().getDetail().getItem().size(); j++) {
                        AMSGTYP.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGTYP());
                        AMSGID.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGID());
                        AMSGNR.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGNR());
                        AMSGV1.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGV1());
                        AMSGV2.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGV2());
                        AMSGV3.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGV3());
                        AMSGV4.add(j, result.getRespuesta().getDetail().getItem().get(j).getMSGV4());
                        AFLDNAME.add(j, result.getRespuesta().getDetail().getItem().get(j).getFLDNAME());
                    }

//==============================================================================================================================
//                  Insertar en tabla de Tags.   --- Pendiente 
//==============================================================================================================================
//============================================== Tag 1  
                    String Detail = transactionId;
                    System.out.println("Result Detail/transactionId = " + transactionId);
                    int iTag = 0;
                    URL newEndpoint = new URL("http://Motor/MotorTransferencia/wsinserttransactiontags.aspx?wsdl");//Producción
                    //URL newEndpoint = new URL("http://Motor/MotorTransferencia/wsinserttransactiontags.aspx?wsdl");//Producción
                    QName qname = new QName("MotorTransferencia", "WsInsertTransactionTags");
                    try {
                        motortransferencia.WsInsertTransactionTagsExecute parametersTag = new motortransferencia.WsInsertTransactionTagsExecute();
                        motortransferencia.WsInsertTransactionTags serviceTag = new motortransferencia.WsInsertTransactionTags(newEndpoint, qname);
                        motortransferencia.WsInsertTransactionTagsSoapPort portTag = serviceTag.getWsInsertTransactionTagsSoapPort();
                        ArrayList<WsInsertTransactionTags> algo = new ArrayList<WsInsertTransactionTags>();
                        ArrayOfSDTTransaccionTag algo1Tag = new ArrayOfSDTTransaccionTag();
                        ArrayList<SDTTransaccionTag> detaTag0 = new ArrayList<SDTTransaccionTag>();
                        SDTTransaccionTag detaTag = new SDTTransaccionTag();
                        for (int j = 0; j < result.getRespuesta().getDetail().getItem().size(); j++) {
//============================================== Tag MSGTYP  
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGTYP");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGTYP());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag MSGID
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGID");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGID());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag MSGNR
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGNR");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGNR());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag MSGV1
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGV1");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGV1());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag MSGV2
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGV2");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGV2());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag MSGV3
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGV3");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGV3());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag MSGV4
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("MSGV4");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getMSGV4());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//============================================== Tag FLDNAME
                            detaTag = new SDTTransaccionTag();
                            detaTag.setTransaccionSecuencia(Integer.valueOf(Detail));
                            detaTag.setTransaccionTagLinea(iTag);
                            detaTag.setMensajeCodigo("MT103");
                            detaTag.setTagCodigo("FLDNAME");
                            detaTag.setTransaccionTagNoTransaccion(1);
                            detaTag.setTransaccionTagValor(result.getRespuesta().getDetail().getItem().get(j).getFLDNAME());
                            detaTag.setTransaccionTagEstado("TIPESTACT");
                            detaTag0.add(detaTag);
//=================================================================
                        }
                        algo1Tag.getSDTTransaccionTag().addAll(detaTag0);
                        parametersTag.setListatags(algo1Tag);
                        motortransferencia.WsInsertTransactionTagsExecuteResponse resultTag = portTag.execute(parametersTag);
                        System.out.println("Result = " + result);
                        System.out.println("Result getCode     = " + resultTag.getRespuesta().getCode());
                        System.out.println("Result getDetail   = " + resultTag.getRespuesta().getDetail());
                        System.out.println("Result getMessage  = " + resultTag.getRespuesta().getMessage());
                        System.out.println("Result getType     = " + resultTag.getRespuesta().getType());
                        if (resultTag.getRespuesta().getCode().equals("0000") && resultTag.getRespuesta().getType().equals("OK")) {
                            System.out.println("Tag Contabilidad insertados correctamente " + resultTag.getRespuesta().getCode());
                            System.out.println("Tag Contabilidad insertados correctamente " + resultTag.getRespuesta().getType());
                        } else {
                            System.out.println("Error Tag Contabilidad **NO** insertados  Code : " + resultTag.getRespuesta().getCode());
                            System.out.println("Error Tag Contabilidad **NO** insertados  Type :" + resultTag.getRespuesta().getType());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

//============================================== Tag 32ATASACOMPRA
//==============================================================================================================================                    
// ======================== Graba Evento cuando ws de contabilizacion es OK. Infatlan Mexico Inicia. ===========================
//==============================================================================================================================
                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                    System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                    req.setTransaccionEventoAccion("WSNICPPSWIFTExecuteResponse Contabiliza");
                    req.setTipoEventoCodigo("INSERT");
                    req.setTransaccionEventoInicio(HoraInicioEvento);
                    System.out.println("********** HoraInicioEvento ********** : " + HoraInicioEvento);
                    req.setTransaccionUsuarioCodigo(usuarioId);
                    req.setTransaccionEventoDispositivo(pantalla);
                    req.setTransaccionEventoIP(terminal);
                    req.setTransaccionEventoSession(llaveSesion);
                    req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + mensaje);
                    cal.setTime(new java.util.Date());
                    try {
                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
//                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                    }
                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                    System.out.println("********** HoraFinalizaEvento ********** : " + HoraFinalizaEvento);
                    req.setTransaccionEventoGestion("TIPESTPRO");
                    req.setTransaccionEventoEstado("TIPESTACT");
                    System.out.println("Evento WSNICPPSWIFTExecuteResponse Contabiliza ** OK **");
                    respEvento = insertaEvento(req);
                    trama = new ByteArrayInputStream(text.getBytes("UTF-8"));
                    String MT103Base64 = _getImage64(trama);
                    deta.setCodigo("0000");
                    deta.setTipo("OK");
                    deta.setMT103(MT103Base64);

                    deta.setDetalle(deta.getDetalle());
                    deta.setMensaje(deta.getMensaje());
                    deta.setReferencia(deta.getReferencia());
                    deta.setReferenciaComision(deta.getReferenciaComision());
                    System.out.println("*** ANTES DEL accion.equals(O) || accion.equals(o) *****");
                    if (accion.equals("O") || accion.equals("o")) {
                        System.out.println("*** ENTRO AL IF *****");
                        String ret = creaMT103(ParametroValorDato, transactionId);
                    }
//                            else{
//                                paso = "3";
//                            }
                    System.out.println("*** ejecuta Update 9 *****");
                    System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                    if (deta.getTipo().equals("OK")) {
                        String resp = update(con, transactionId, NovalorCoincidencia, paso, "OK");
                    } else {
                        String resp = update(con, transactionId, ERROR, "0", "NO");
                    }
                    con.close();
                    return deta;
//==============================================================================================================================                    
// ======================== Graba Evento cuando ws de contabilizacion es OK. Infatlan Mexico FIN. ===========================
//==============================================================================================================================                  
                } else {
                    System.out.println("**** result.getRespuesta().getHeader().getTipo()  ***** " + result.getRespuesta().getHeader().getCodigo());
                    deta.setCodigo(result.getRespuesta().getHeader().getCodigo());
                    //deta.setCodigo("9980A");
                    deta.setTipo(result.getRespuesta().getHeader().getTipo());
                    System.out.println("**** result.getRespuesta().getHeader().getTipo()  ***** " + result.getRespuesta().getHeader().getTipo());
                    deta.setDetalle(result.getRespuesta().getHeader().getDetalle());
                    System.out.println("**** result.getRespuesta().getHeader().getDetalle());  ***** " + result.getRespuesta().getHeader().getDetalle());
                    deta.setMensaje(result.getRespuesta().getHeader().getMensaje());
                    System.out.println("**** result.getRespuesta().getHeader().getMensaje());  ***** " + result.getRespuesta().getHeader().getMensaje());
                    deta.setReferencia(result.getRespuesta().getHeader().getReferencia());
                    System.out.println("**** result.getRespuesta().getHeader().getReferencia());  ***** " + result.getRespuesta().getHeader().getReferencia());
                    System.out.println("**** sql ERR  ***** " + ERROR);
                    System.out.println("*** ejecuta Update 10 *****");
                    String resp = update(con, transactionId, ERROR, "0", "NO");
// ======================== Graba Eventocuando ws de contabilizacion es ERROR NOOK. Inicia. ===========================                    
                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                    System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                    req.setTransaccionEventoAccion("ERROR WSNICPPSWIFTExecuteResponse Infatlan Mexico Contabiliza");
                    req.setTipoEventoCodigo("INSERT");
                    req.setTransaccionEventoInicio(HoraInicioEvento);
                    req.setTransaccionUsuarioCodigo(usuarioId);
                    req.setTransaccionEventoDispositivo(pantalla);
                    req.setTransaccionEventoIP(terminal);
                    req.setTransaccionEventoSession(llaveSesion);
//                req.setTransaccionEventoComentario("Consulta cliente en el CORE - respuesta No exitosa");
//errorhoy
                    req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + deta.getMensaje());
                    cal.setTime(new java.util.Date());
                    try {
                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
//                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                    }
                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                    req.setTransaccionEventoGestion("TIPESTPRO");
                    req.setTransaccionEventoEstado("TIPESTACT");
                    System.out.println("Evento WSNICPPSWIFTExecuteResponse Infatlan Mexico Contabiliza ** MAL NOOK **");
                    respEvento = insertaEvento(req);
// ======================== Graba Evento cuando ws de contabilizacion es ERROR NOOK. Fin. ===========================                    
                    con.close();
                    return deta;
                }

            } catch (Exception ex) {
                System.out.println("Exception : " + ex.getMessage());
                deta.setCodigo("9979");
                deta.setTipo("ER");
                deta.setDetalle("error : " + ex.getMessage());
                return deta;
            }
            /*   Comentado el dia 2 de Abril 2025 al incluir el WS contabiliza Infatlan Mexico.
            try {
                webservicestc.WSNICF9I4Execute parameters = new webservicestc.WSNICF9I4Execute();
//                SDTWSNICClientePeticion algo = new SDTWSNICClientePeticion();
                SDTPeticionHeader algo1 = new SDTPeticionHeader();
                SDTPeticion algo2 = new SDTPeticion();
                SDTWSNICF9I4Peticion algo3 = new SDTWSNICF9I4Peticion();
                if (transactionId.length() >= 5) {
                    transaccionComentario5 = transactionId.substring(transactionId.length() - 5);
                    System.out.println("Last 5 digits: " + transaccionComentario5);
                } else {
                    transaccionComentario5 = transactionId;
                    System.out.println("The string is too short!  pero ahi va : " + transaccionComentario5);
                }
                if (NOMBRE_CLIENTE_BENEFICIARIO.length() <= 40) {
                    nombre40oMenos = NOMBRE_CLIENTE_BENEFICIARIO;
                    System.out.println("nombre40oMenos: " + nombre40oMenos);
                } else {
                    nombre40oMenos = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 40);
                    System.out.println("Else nombre40oMenos: " + nombre40oMenos);
                }
                if (accion.equals("I") || accion.equals("i")) {
                    algo3.setOperacion("2");
                    Comentario = "I/T-" + transaccionComentario5 + "/" + nombre40oMenos;
                } else {
                    algo3.setOperacion("1");
                    Comentario = "W/T-" + transaccionComentario5 + "/" + nombre40oMenos;
                }
//                    algo3.setOperacion("0");
                System.out.println("algo3.getOperacion() = " + algo3.getOperacion());
                algo3.setAreaBancaria(AreaBancaria);
                System.out.println("AreaBancaria = " + AreaBancaria);
                algo3.setCuenta(Cuenta);
                System.out.println("Cuenta = " + Cuenta);
                algo3.setComentario(Comentario);
                System.out.println("Comentario = " + Comentario);
                algo3.setValorTransaccion(ValorTransaccion);
                System.out.println("ValorTransaccion = " + ValorTransaccion);
                algo3.setValorComision(ValorComision);
                System.out.println("ValorComision = " + ValorComision);
                parameters.setPeticion(algo3);
                webservicestc.WSNICF9I4 service = new webservicestc.WSNICF9I4();
                webservicestc.WSNICF9I4SoapPort port = service.getWSNICF9I4SoapPort();
                // TODO process result here
                System.out.println("Contabiliza WSNICF9I4ExecuteResponse : ");
                webservicestc.WSNICF9I4ExecuteResponse result = port.execute(parameters);
                if (result.getRespuesta().getHeader().getTipo().equals("EX")) {
                    System.out.println("respuesta 1 WS  9I4: " + result.getRespuesta().getHeader().getTipo());
                    System.out.println("Por ejecutar de nuevo el WS  9I4: ");
                    result = port.execute(parameters);
                    System.out.println("Ejecuto dos veces el WS  9I4: ");
                    System.out.println("respuesta 2 WS  9I4: " + result.getRespuesta().getHeader().getTipo());
                }
                String detalle = null;
                String referencia = null;
                String adicional = null;
                String documentoValor = null;
                String documentoComision = null;
                System.out.println("Contabiliza WSNICF9I4ExecuteResponse getCodigo: " + result.getRespuesta().getHeader().getCodigo());
                System.out.println("Contabiliza WSNICF9I4ExecuteResponse getTipo: " + result.getRespuesta().getHeader().getTipo());
                if (result.getRespuesta().getHeader().getTipo().equals("OK")) {
                    System.out.println("Contabiliza WSNICF9I4ExecuteResponse OK ");
                    detalle = result.getRespuesta().getHeader().getDetalle();
                    mensaje = result.getRespuesta().getHeader().getMensaje();
                    referencia = result.getRespuesta().getHeader().getReferencia();
                    if (result.getRespuesta().getDetail().getItem().size() == 2) {
                        System.out.println("Contabiliza WSNICF9I4ExecuteResponse OK size() == 2");
                        documentoValor = result.getRespuesta().getDetail().getItem().get(0).getMsgv2();
                        System.out.println("documentoValor respuesta WS F9I4: 2: " + documentoValor);
                        documentoComision = result.getRespuesta().getDetail().getItem().get(1).getMsgv2();
                        System.out.println("documentoComision respuesta WS F9I4: 2: " + documentoComision);
                    } else {
                        System.out.println("Contabiliza WSNICF9I4ExecuteResponse OK size() NO = 2");
                        if (result.getRespuesta().getDetail().getItem().size() == 1) {
                            System.out.println("Contabiliza WSNICF9I4ExecuteResponse OK size() == 1");
                            documentoValor = result.getRespuesta().getDetail().getItem().get(0).getMsgv2();
                            System.out.println("documentoValor  respuesta WS F9I4: 1: " + documentoValor);
                            documentoComision = "0.00";
                            System.out.println("documentoComision  respuesta WS F9I4: 1: " + documentoComision);
                        } else {
                            System.out.println("Contabiliza WSNICF9I4ExecuteResponse O9983");
                            deta.setCodigo("9983");
                            deta.setTipo("WS Core : " + result.getRespuesta().getHeader().getTipo());
                            deta.setDetalle("WS Core : " + result.getRespuesta().getHeader().getDetalle());
                            System.out.println("Result = deta.getCodigo " + deta.getCodigo());
                            System.out.println("Result = deta.getDetalle " + deta.getDetalle());
                            deta.setReferencia("WS Core : " + result.getRespuesta().getHeader().getReferencia());
                            //OJO JETA
                            System.out.println("*** ejecuta Update 7 *****");
                            String resp = update(con, transactionId, ERROR, "?", "NO");
                            con.close();
//                    deta.setMensaje(result.getRespuesta().getHeader().getMensaje());
//                    deta.setReferencia(result.getRespuesta().getHeader().getReferencia());
                            return deta;
                        };
                    }
// ======================== Graba Eventocuando ws de contabilizacion es OK. Inicia. ===========================
                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                    System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                    req.setTransaccionEventoAccion("WSNICF9I4ExecuteResponse Contabiliza");
                    req.setTipoEventoCodigo("INSERT");
                    req.setTransaccionEventoInicio(HoraInicioEvento);
                    System.out.println("********** HoraInicioEvento ********** : " + HoraInicioEvento);
                    req.setTransaccionUsuarioCodigo(usuarioId);
                    req.setTransaccionEventoDispositivo(pantalla);
                    req.setTransaccionEventoIP(terminal);
                    req.setTransaccionEventoSession(llaveSesion);
                    req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + mensaje);
                    cal.setTime(new java.util.Date());
                    try {
                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                    System.out.println("********** HoraFinalizaEvento ********** : " + HoraFinalizaEvento);
                    req.setTransaccionEventoGestion("TIPESTPRO");
                    req.setTransaccionEventoEstado("TIPESTACT");
                    System.out.println("Evento WSNICF9I4ExecuteResponse Contabiliza ** OK **");
                    respEvento = insertaEvento(req);
// ======================== Graba Eventocuando ws de contabilizacion es OK. Fin. ===========================
                    System.out.println("***** Llege hasta aqui nuevo nuevo **************");
                    try {
                        int n = 0;
                        SendParseRequestF9I8 detaResp = new SendParseRequestF9I8();
                        RespuestaTransferenciaMT103 deta2 = new RespuestaTransferenciaMT103();
                        System.out.println("detaResp.SendParseRequest documentoValor:" + documentoValor);
//                        deta2 = detaResp.SendParseRequest(AreaBancaria, documentoValor);
                        System.out.println("**** paso 1  : ");
                        while (n < 2) {
                            System.out.println("**** paso 2  : ");
                            System.out.println("**** Ejecuta WS la vez numero : " + n);
                            deta2 = detaResp.SendParseRequest(AreaBancaria, documentoValor);
                            System.out.println("**** paso 3  : ");
                            if (deta2.getTipo().equals("EX")) {
                                System.out.println("**** paso 4  :getTipo = EX ");
                                System.out.println("**** Ejecuta WS la vez numero : " + n);
                                n++;
                            } else {
                                System.out.println("**** paso 5  : ");
                                n = 2;
                            }
                            System.out.println("**** paso 6  : ");
                            System.out.println("**** Ejecuta WS la vez numero : " + n);
                        }
                        System.out.println("antes de  deta.getTipo().equals(\"OK\") :");
                        System.out.println("deta2. :" + deta2.getTipo());
                        if (deta2.getTipo().equals("OK")) {
// ======================== Graba Eventocuando ws de contabilizacion es OK. Inicia. ===========================
                            req.setTransaccionSecuencia(Long.valueOf(transactionId));
                            req.setTransaccionEventoSistema(HoraInicioAplicativo);
                            System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                            req.setTransaccionEventoAccion("WSNICF9I8ExecuteResponse Contabiliza");
                            req.setTipoEventoCodigo("INSERT");
                            req.setTransaccionEventoInicio(HoraInicioEvento);
                            System.out.println("********** HoraInicioEvento ********** : " + HoraInicioEvento);
                            req.setTransaccionUsuarioCodigo(usuarioId);
                            req.setTransaccionEventoDispositivo(pantalla);
                            req.setTransaccionEventoIP(terminal);
                            req.setTransaccionEventoSession(llaveSesion);
                            req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + deta2.getMensaje());
                            cal.setTime(new java.util.Date());
                            try {
                                HoraFinalizaEvento = DatatypeFactory.newInstance()
                                        .newXMLGregorianCalendar(cal);
                            } catch (DatatypeConfigurationException ex) {
                                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            req.setTransaccionEventoFinal(HoraFinalizaEvento);
                            System.out.println("********** HoraFinalizaEvento ********** : " + HoraFinalizaEvento);
                            req.setTransaccionEventoGestion("TIPESTPRO");
                            req.setTransaccionEventoEstado("TIPESTACT");
                            System.out.println("Evento WSNICF9I8ExecuteResponse Contabiliza ** OK **");
                            respEvento = insertaEvento(req);
// ======================== Graba Eventocuando ws de contabilizacion es OK. Fin. ===========================
                            deta.setReferencia(deta2.getReferencia());
                            System.out.println("Contabiliza F9I8 :" + deta.getReferencia());
                            System.out.println("documentoComision :" + documentoComision);
                            if (documentoComision.equals("0") || documentoComision.equals("0.00")) {
                                deta.setCodigo("0000");
                                deta.setTipo("OK");
                                deta.setDetalle(deta2.getDetalle());
                                deta.setMensaje(deta2.getMensaje());
                                deta.setReferencia(deta2.getReferencia());
                                deta.setReferenciaComision("?");
                                System.out.println("********** accion.equals(O) || accion.equals(o) ********** 2 : " + accion.toString());
                                if (accion.equals("O") || accion.equals("o")) {
                                    System.out.println("********** accion.equals(O) || accion.equals(o) ********** Entro : " + accion.toString());
                                    String ret = creaMT103(ParametroValorDato, transactionId);
                                }
                                System.out.println("*** ejecuta Update 8 SALIO*****");
                                String resp = update(con, transactionId, NovalorCoincidencia, paso, "OK");
                                SendParseRequestTags tags = new SendParseRequestTags();
                                deta = tags.SendParseRequest(transactionId, documentoValor, documentoComision);
                                con.close();
//                                return deta;
                            } else {
                                deta2 = detaResp.SendParseRequest(AreaBancaria, documentoComision);
                                System.out.println("detaResp.SendParseRequest documentoComision :" + documentoComision);
//==============================================================================================================
                                n = 0;
                                System.out.println("**** paso 1A de nuez  : ");
//                                while (n < 2) {
//                                    System.out.println("**** paso 2A  : ");
//                                    System.out.println("**** Ejecuta WS la vez numero A : " + n);
//                                    deta2 = detaResp.SendParseRequest(AreaBancaria, documentoComision);
//                                    System.out.println("**** paso 3A  : ");
//                                    if (deta2.getTipo().equals("EX")) {
//                                        System.out.println("**** paso 4A  :getTipo = EX ");
//                                        System.out.println("**** Ejecuta WS la vez numero : " + n);
//                                        n++;
//                                    } else {
//                                        System.out.println("**** paso 5A  : ");
//                                        n = 2;
//                                    }
//                                    System.out.println("**** paso 6A  : ");
//                                    System.out.println("**** Ejecuta WS la vez numero A: " + n);
//                                }
//===============================================================================================================                        
                                if (deta2.getTipo().equals("OK")) {
                                    deta.setCodigo("0000");
                                    deta.setTipo("OK");
                                } else {
                                    deta.setCodigo("9999");
                                    deta.setTipo(deta2.getTipo());
                                }

                                deta.setDetalle(deta2.getDetalle());
                                deta.setMensaje(deta2.getMensaje());
                                deta.setReferencia(deta2.getReferencia());
                                deta.setReferenciaComision(deta2.getReferenciaComision());
                                System.out.println("*** ANTES DEL accion.equals(O) || accion.equals(o) *****");
                                if (accion.equals("O") || accion.equals("o")) {
                                    System.out.println("*** ENTRO AL IF *****");
                                    String ret = creaMT103(ParametroValorDato, transactionId);
                                }
//                            else{
//                                paso = "3";
//                            }
                                System.out.println("*** ejecuta Update 9 *****");
                                System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                                if (deta2.getTipo().equals("OK")) {
                                    String resp = update(con, transactionId, NovalorCoincidencia, paso, "OK");
                                } else {
                                    String resp = update(con, transactionId, ERROR, "?", "NO");
                                }

                                if (con != null) {
                                    con.close();
                                }

//                                return deta;
                            }
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            java.util.Date date = new java.util.Date();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            System.out.println("año    ");
                            String año = String.valueOf(calendar.get(Calendar.YEAR));
                            System.out.println("mesInt    ");
                            int mesInt = calendar.get(Calendar.MONTH) + 1;
                            String pattern2 = "00";
                            df.applyPattern(pattern2);
                            System.out.println("mes dia    ");
                            String mes = df.format(mesInt);
                            System.out.println("mes      ");
                            int diaInt = calendar.get(Calendar.DAY_OF_MONTH);
                            String dia = df.format(diaInt);
                            try {
                                webservicestc.WSNICDebitoCreditoExecute parametersDeb = new webservicestc.WSNICDebitoCreditoExecute();
                                SDTPeticionHeader algo1Deb = new SDTPeticionHeader();
                                SDTPeticion algo2Deb = new SDTPeticion();
                                SDTWSNICDebitoCreditoPeticion algo3Deb = new SDTWSNICDebitoCreditoPeticion();
                                algo3Deb.setItem("?");
                                algo3Deb.setNoTransaccion(transactionId);
                                System.out.println("algo3Deb.setNoTransaccion transactionId: " + transactionId);
                                algo3Deb.setTipoTransaccion("0801");
                                algo3Deb.setTipoMovimiento("DB");
                                algo3Deb.setCodigoAplicacion("001");
                                algo3Deb.setCodigoCanal("?");
                                algo3Deb.setCodigoBanco("2300");
                                algo3Deb.setCodigoCliente("?");
                                algo3Deb.setCuenta(Cuenta);
                                System.out.println("algo3Deb.setCuenta Cuenta: " + Cuenta);
                                algo3Deb.setMoneda(MONEDA);
                                System.out.println("algo3Deb.setMoneda MONEDA: " + MONEDA);
                                algo3Deb.setMonto(VALUEBANI);
                                System.out.println("algo3Deb.setMonto VALUEBANI: " + VALUEBANI);
                                algo3Deb.setSucursal("?");
                                algo3Deb.setDescripcion(Comentario);
                                System.out.println("lgo3Deb.setDescripcion Comentario: " + Comentario);
                                algo3Deb.setUsuario(usuarioId);
                                algo3Deb.setFechaProceso(año + mes + dia);
                                algo3Deb.setFechaValor("?");
                                algo3Deb.setNotaPagoTipo("?");
                                algo3Deb.setNotaPagoTexto("?");
                                algo3Deb.setReferenciaCongelamiento("?");
                                System.out.println("lgo3Deb.setFechaProceso : " + año + mes + dia);
                                algo3Deb.setReferenciaExterna1(transactionId);
                                algo3Deb.setReferenciaExterna2(transactionId);
                                parametersDeb.setPeticion(algo3Deb);
                                webservicestc.WSNICDebitoCredito serviceDeb = new webservicestc.WSNICDebitoCredito();
                                webservicestc.WSNICDebitoCreditoSoapPort portDeb = serviceDeb.getWSNICDebitoCreditoSoapPort();
                                // TODO process result here
                                webservicestc.WSNICDebitoCreditoExecuteResponse resultDeb = portDeb.execute(parametersDeb);
                                System.out.println("Result = " + resultDeb.getRespuesta().getHeader().getCodigo());
                                System.out.println("Result = " + resultDeb.getRespuesta().getHeader().getDetalle());
                                System.out.println("Result = " + resultDeb.getRespuesta().getHeader().getMensaje());
                                System.out.println("Result = " + resultDeb.getRespuesta().getHeader().getReferencia());
                                System.out.println("Result = " + resultDeb.getRespuesta().getHeader().getTipo());
                                System.out.println("Result = " + resultDeb.getRespuesta().getHeader().getAdicional());
                                if (resultDeb.getRespuesta().getHeader().getCodigo().equals("0000") && resultDeb.getRespuesta().getHeader().getTipo().equals("OK") && resultDeb.getRespuesta().getHeader().getMensaje().equals("Procesado Exitosamente")) {
                                    // ======================== Graba Eventocuando ws de DebitoCredito es OK. Inicia. ===========================
                                    System.out.println("Result NumeroReferenciaContable :   " + resultDeb.getRespuesta().getDetail().getItem().get(0).getNumeroReferenciaContable());
                                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                                    System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                                    req.setTransaccionEventoAccion("WSNICDebitoCreditoExecuteResponse DebitoCredito");
                                    req.setTipoEventoCodigo("INSERT");
                                    req.setTransaccionEventoInicio(HoraInicioEvento);
                                    System.out.println("********** HoraInicioEvento ********** : " + HoraInicioEvento);
                                    req.setTransaccionUsuarioCodigo(usuarioId);
                                    req.setTransaccionEventoDispositivo(pantalla);
                                    req.setTransaccionEventoIP(terminal);
                                    req.setTransaccionEventoSession(llaveSesion);
                                    req.setTransaccionEventoComentario("NumeroReferenciaContable : " + resultDeb.getRespuesta().getDetail().getItem().get(0).getNumeroReferenciaContable());
                                    cal.setTime(new java.util.Date());
                                    try {
                                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                                .newXMLGregorianCalendar(cal);
                                    } catch (DatatypeConfigurationException ex) {
                                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                                    System.out.println("********** HoraFinalizaEvento ********** : " + HoraFinalizaEvento);
                                    req.setTransaccionEventoGestion("TIPESTPRO");
                                    req.setTransaccionEventoEstado("TIPESTACT");
                                    System.out.println("WSNICDebitoCreditoExecuteResponse DebitoCredito ** OK **");
                                    respEvento = insertaEvento(req);
// ======================== Graba Eventocuando ws de DebitoCredito es OK. Fin. =========================== 
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                            return deta;
                        }

//                        webservicestc.WSNICF9I8Execute parameters2 = new webservicestc.WSNICF9I8Execute();
//                        SDTWSNICF9I8Peticion algo4 = new SDTWSNICF9I8Peticion();
////                        algo4.setAreaBancaria("2300");
////                        System.out.println("AreaBancaria ******* " + "2300");
//                        algo4.setDocumento("000000025381");
////                        System.out.println("documentoValor ******* " + "000000025315");
//                        algo4.setAreaBancaria(AreaBancaria.trim());
////                        algo4.setDocumento(documentoValor);
//                        System.out.println("algo4.setAreaBancaria ******* " + AreaBancaria.trim());
//                        System.out.println("algo4.getAreaBancaria ******* " + algo4.getAreaBancaria());
//                        System.out.println("algo4.setDocumento ******* " + documentoValor.trim());
//                        System.out.println("algo4.getDocumento ******* " + algo4.getDocumento());
////                        algo4.setDocumento("000000025319");
//                        parameters2.setPeticion(algo4);
//                        webservicestc.WSNICF9I8 service2 = new webservicestc.WSNICF9I8();
//                        webservicestc.WSNICF9I8SoapPort port2 = service2.getWSNICF9I8SoapPort();
//                        System.out.println("parameters2.getPeticion().getAreaBancaria() " + parameters2.getPeticion().getAreaBancaria());
//                        System.out.println("parameters2.getPeticion().getDocumento() " + parameters2.getPeticion().getDocumento());
//                        webservicestc.WSNICF9I8ExecuteResponse result2 = port2.execute(parameters2);
//                        System.out.println("Contabiliza WSNICF9I8ExecuteResponse getCodigo" + result2.getRespuesta().getHeader().getCodigo());
//                        System.out.println("Result = getTipo " + result2.getRespuesta().getHeader().getTipo());
//                        if (result2.getRespuesta().getHeader().getTipo().equals("OK")) {
//                            System.out.println("Contabiliza WSNICF9I8ExecuteResponse equals(\"OK\")");
//                            deta.setReferencia(result2.getRespuesta().getHeader().getReferencia());
//                            algo4.setAreaBancaria(AreaBancaria);
//                            algo4.setDocumento(documentoComision);
//                            parameters2.setPeticion(algo4);
//                            service2 = new webservicestc.WSNICF9I8();
//                            port2 = service2.getWSNICF9I8SoapPort();
//                            // TODO process result here
//                            if (documentoComision.equals("0")) {
//                                deta.setCodigo("0000");
//                                deta.setTipo(result2.getRespuesta().getHeader().getTipo());
//                                deta.setDetalle(result2.getRespuesta().getHeader().getDetalle());
//                                deta.setMensaje(result2.getRespuesta().getHeader().getMensaje());
//                                deta.setReferencia(result2.getRespuesta().getHeader().getReferencia());
//                                deta.setReferenciaComision("?");
//                                String ret = creaMT103(ParametroValorDato);
//                                String resp = update(con, transactionId, NovalorCoincidencia, paso, "OK");
//                                con.close();
//                                return deta;
//                            }
//                            result2 = port2.execute(parameters2);
//                            deta.setCodigo("0000");
//                            deta.setTipo(result2.getRespuesta().getHeader().getTipo());
//                            deta.setDetalle(result2.getRespuesta().getHeader().getDetalle());
//                            deta.setMensaje(result2.getRespuesta().getHeader().getMensaje());
//                            deta.setReferenciaComision(result2.getRespuesta().getHeader().getReferencia());
//                            String ret = creaMT103(ParametroValorDato);
//                            String resp = update(con, transactionId, NovalorCoincidencia, paso, "OK");
//                            con.close();
//                            return deta;
//                        } else {
//                            System.out.println("result2.getRespuesta().getHeader().getTipo().equals(\"NOOK\")");
//                            deta.setCodigo("9982");
//                            deta.setTipo("WS Core : " + result2.getRespuesta().getHeader().getTipo());
//                            deta.setDetalle("WS Core : " + result2.getRespuesta().getHeader().getDetalle());
//                            deta.setMensaje("WS Core : " + result2.getRespuesta().getHeader().getMensaje());
//                            deta.setReferencia("WS Core : " + result2.getRespuesta().getHeader().getReferencia());
//                            System.out.println("Result = deta.getCodigo " + deta.getCodigo());
//                            System.out.println("Result = deta.getDetalle " + deta.getDetalle());
//                            String resp = update(con, transactionId, ERROR, "?", "NO");
//                            con.close();
//                            System.out.println("Contabiliza WSNICF9I4ExecuteResponse O9983");
//                            deta.setCodigo("9983");
//                            deta.setTipo("WS Core : " + result.getRespuesta().getHeader().getTipo());
//                            deta.setDetalle("WS Core : " + result.getRespuesta().getHeader().getDetalle());
//                            System.out.println("Result = deta.getCodigo " + deta.getCodigo());
//                            System.out.println("Result = deta.getDetalle " + deta.getDetalle());
//                            deta.setReferencia("WS Core : " + result.getRespuesta().getHeader().getReferencia());
//                            return deta;
//                        }
                    } catch (Exception ex) {
                        deta.setCodigo("9981");
                        deta.setTipo("ER");
                        deta.setDetalle("error a: " + ex.getMessage());
                        return deta;
                    }

                } else {

                    deta.setCodigo("9980");
                    deta.setTipo(result.getRespuesta().getHeader().getTipo());
                    System.out.println("**** result.getRespuesta().getHeader().getTipo()  ***** " + result.getRespuesta().getHeader().getTipo());
                    deta.setDetalle(result.getRespuesta().getHeader().getDetalle());
                    System.out.println("**** result.getRespuesta().getHeader().getDetalle());  ***** " + result.getRespuesta().getHeader().getDetalle());
                    deta.setMensaje(result.getRespuesta().getHeader().getMensaje());
                    System.out.println("**** result.getRespuesta().getHeader().getMensaje());  ***** " + result.getRespuesta().getHeader().getMensaje());
                    deta.setReferencia(result.getRespuesta().getHeader().getReferencia());
                    System.out.println("**** result.getRespuesta().getHeader().getReferencia());  ***** " + result.getRespuesta().getHeader().getReferencia());
                    System.out.println("**** sql ERR  ***** " + ERROR);
                    System.out.println("*** ejecuta Update 10 *****");
                    String resp = update(con, transactionId, ERROR, "?", "NO");
// ======================== Graba Eventocuando ws de contabilizacion es ERROR NOOK. Inicia. ===========================                    
                    req.setTransaccionSecuencia(Long.valueOf(transactionId));
                    req.setTransaccionEventoSistema(HoraInicioAplicativo);
                    System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
                    req.setTransaccionEventoAccion("ERROR WSNICF9I8ExecuteResponse Contabiliza");
                    req.setTipoEventoCodigo("INSERT");
                    req.setTransaccionEventoInicio(HoraInicioEvento);
                    req.setTransaccionUsuarioCodigo(usuarioId);
                    req.setTransaccionEventoDispositivo(pantalla);
                    req.setTransaccionEventoIP(terminal);
                    req.setTransaccionEventoSession(llaveSesion);
//                req.setTransaccionEventoComentario("Consulta cliente en el CORE - respuesta No exitosa");
//errorhoy
                    req.setTransaccionEventoComentario(result.getRespuesta().getHeader().getCodigo() + "|" + deta.getMensaje());
                    cal.setTime(new java.util.Date());
                    try {
                        HoraFinalizaEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    req.setTransaccionEventoFinal(HoraFinalizaEvento);
                    req.setTransaccionEventoGestion("TIPESTPRO");
                    req.setTransaccionEventoEstado("TIPESTACT");
                    System.out.println("Evento WSNICF9I8ExecuteResponse Contabiliza ** MAL NOOK **");
                    respEvento = insertaEvento(req);
// ======================== Graba Evento cuando ws de contabilizacion es ERROR NOOK. Fin. ===========================                    
                    con.close();
                    return deta;
                }
            } catch (Exception ex) {
                deta.setCodigo("9979");
                deta.setTipo("ER");
                deta.setDetalle("error : " + ex.getMessage());
                return deta;
            }
             */
//            } else {
//                deta.setCodigo("0000");
//                deta.setTipo("OK");
//                deta.setDetalle("Listo sin contabilizar");
//                deta.setMensaje("Listo sin contabilizar");
//                deta.setReferencia("0");
//                deta.setReferenciaComision("0");
//                return deta;
//            }

////===============================================================================================
//            System.out.println("Genera Archivo y graba en directorio");
////===============================================================================================                  
////================================================================================================
//            PrintWriter out = null;
//            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            java.util.Date date = new java.util.Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            System.out.println("año    ");
//            String año = String.valueOf(calendar.get(Calendar.YEAR));
//            System.out.println("mesInt    ");
//            int mesInt = calendar.get(Calendar.MONTH) + 1;
//            String pattern2 = "00";
//            df.applyPattern(pattern2);
//            System.out.println("mes dia    ");
//            String mes = df.format(mesInt);
//            System.out.println("mes      ");
//            int diaInt = calendar.get(Calendar.DAY_OF_MONTH);
//            String dia = df.format(diaInt);
//            System.out.println(" dia    ");
//            String MiliSecods = String.valueOf(calendar.get(Calendar.MILLISECOND));
//            //========================================================================
//            LocalDateTime now = LocalDateTime.now();
//            System.out.println("LocalDateTime      " + now);
//            // Create a formatter with milliseconds
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss-SSS");
//
//            // Format and print the date and time with milliseconds
//            String formattedDateTime = now.format(formatter);
//            System.out.println("Current date and time with milliseconds: " + formattedDateTime);
//            //========================================================================
////        System.out.println("Año : " + calendar.get(Calendar.YEAR) + "   Mes : " + mes + "   Dia : " + calendar.get(Calendar.DAY_OF_MONTH));
//            try {
//                //out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/" + año + mes + dia + MiliSecods + "MT103.txt", true)));
//                out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/MT103-" + formattedDateTime + ".txt", true)));
//                out.println(text);
//                out.close();
//
//            } catch (IOException e) {
//                System.out.println();
//                deta.setCodigo("9999");
//                deta.setTipo("NOOK");
//                deta.setDetalle(e.getMessage());
//                return deta;
//            }
////================================================================================================
//            trama = new ByteArrayInputStream(text.getBytes("UTF-8"));
//            String MT103Base64 = _getImage64(trama);
//            deta.setCodigo("0000");
//            deta.setTipo("OK");
//            deta.setMT103(MT103Base64);
//            return deta;
//        } catch (UnsupportedEncodingException ex) {
//            Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            deta.setCodigo("9993");
            deta.setTipo("ER");
            deta.setDetalle("error : " + ex.getMessage());
            return deta;
        }
//        finally {
//            try {
//                trama.close();
//            } catch (IOException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

//        return null;
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="public method: Crea archivo MT103 : generaTransferenciaMT103">
    @WebMethod(operationName = "generaTransferenciaMT103")
    public RespuestaGeneraTransferenciaMT103 generaTransferenciaMT103(@WebParam(name = "transactionId") String transactionId,
            @WebParam(name = "aplicationId") String aplicationId,
            @WebParam(name = "paisId") String paisId,
            @WebParam(name = "empresaId") String empresaId,
            @WebParam(name = "regionId") String regionId,
            @WebParam(name = "canalId") String canalId,
            @WebParam(name = "version") String version,
            @WebParam(name = "llaveSesion") String llaveSesion,
            @WebParam(name = "usuarioId") String usuarioId,
            @WebParam(name = "accion") String accion,
            @WebParam(name = "token") String token,
            @WebParam(name = "clienteCoreId") String clienteCoreId,
            @WebParam(name = "TipoIdentificacion") String TipoIdentificacion,
            @WebParam(name = "identificacion") String identificacion,
            @WebParam(name = "terminal") String terminal,
            @WebParam(name = "pantalla") String pantalla,
            @WebParam(name = "paso") String paso,
            //            @WebParam(name = "Operacion") String Operacion,
            //            @WebParam(name = "AreaBancaria") String AreaBancaria,
            //            @WebParam(name = "Cuenta") String Cuenta,
            //            @WebParam(name = "Comentario") String Comentario,
            //            @WebParam(name = "ValorTransaccion") String ValorTransaccion,
            //            @WebParam(name = "ValorComision") String ValorComision,
            @WebParam(name = "parametroAdicional") parametroAdicionalColeccion parametroAdicional,
            @WebParam(name = "TransferenciaColeccion") TransferenciaColeccion TransferenciaColeccion
    ) {
        RespuestaGeneraTransferenciaMT103 deta = new RespuestaGeneraTransferenciaMT103();
        InputStream trama = null;
        text = null;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new java.util.Date());
        System.out.println("**** Metodo NUEVO *****       generaTransferenciaMT103       ");//
        try {
            int Estado = 0;
            String mensaje = null;
            String nombre = null;
            String Nombre = null;
            String ACCOUNT = null;
            String NombreBeneficiario = null;
            String NumCliente = null;
            String Id = null;
            String TipoId = null;
            String TipoPersona = null;
            String CodPaisOrdenante = null;
            String CodPaisBeneficiario = null;
            String DesPais = null;
            String Direccion = null;
            String Telefono = null;
            String Correo = null;
            String SQL = null;
            Statement stmt = null;
            ResultSet rs = null;
            String respuesta = null;
            String ParametroCodigo = null;
            Connection con = null;
            Connection con1 = null;
            String ParametroValorDato = null;
            String ParametroValorDatoPorcentaje = null;
            String Valor = null;
            double totalValor = 0.0;

            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            DecimalFormat df = (DecimalFormat) nf;
            System.out.println("**** PASO1 Entro generaTransferenciaMT103 ***** ");
            System.out.println("**** aplicationId  ***** " + aplicationId);
            System.out.println("**** Accion ***** generaTransferenciaMT103 : " + accion);//
//            System.out.println("**** Cuenta ***** " + Cuenta);
//            System.out.println("**** ValorTransaccion ***** " + ValorTransaccion);
//            System.out.println("**** ValorComision ***** " + ValorComision);
//            System.out.println("**** AreaBancaria ***** " + AreaBancaria);
            System.out.println("**** identificacion ***** " + identificacion);
            String NUM_TRANSFERENCIA = null;
            String CUENTA_ORDENANTE = null;
            String NOMBRE_ORDENANTE = "?";
            String DIRECCION_ORDENANTE1 = "";
            String DIRECCION_ORDENANTE2 = "";
            String DIRECCION_ORDENANTE3 = "";
            String BIC_BANCO = null;
            String TIPO_PARTICIPANTE = "?";
            String MONEDA = null;
            String IDENTIDAD_CLIENTE_BENEFICIARIO = "?";
            String MOTIVO1 = "?";
            String MOTIVO2 = "?";
            String MOTIVO3 = "?";
            String MOTIVO4 = "?";
            String CUENTA_BENEFICIARIO = "?";
            String NOMBRE_BENEFICIARIO = "?";
            String DIRECCION_BENEFICIARIO = "?";
            String FECHA = "?";
            String CODIGO_RETORNO = null;
            String DESCRIPCION_ERROR = null;
            String DESCRIPCION_TRANSACCION = null;
            String DESCRIPCION_TRANSACCION_SIN = "?";
            String NUM_TRANSFERENCIA_MT102 = null;
            String TIPO_MENSAGE = null;
            String TIPO_TRANSACCION = "?";
            String ESTADO = null;
            String BIC_BANCO_ORDENANTE = "?";
            String BIC_BANCO_ORDENANTE52A = "?";
            String BIC_BANCO_ORDENANTE52D = "?";
            String BIC_BANCO_INTERMEDIARIO56A = "?";
            String BIC_BANCO_INTERMEDIARIO56D = "?";
            String DIRECCION_BANCO_INTERMEDIARIO1 = "?";
            String DIRECCION_BANCO_INTERMEDIARIO2 = "?";
            String DIRECCION_BANCO_INTERMEDIARIO3 = "?";
            String NOMBRE_BANCO_INTERMEDIARIO56D = "?";
            String REFERENCIA20 = "?";
            String BIC_BANCO_BENEFICIARIO = "?";
            String HEADER3 = "?";
            String BIC_BANCO_BENEFICIARIO57A = "?";
            String BIC_BANCO_BENEFICIARIO57D = "?";
            String NOMBRE_BANCO_BENEFICIARIO57D = "?";
            String BIC_BANCO_RECIBE = null;
            String BIC_BANCO_ENVIA = null;
            String CODIGO_OPERACION = null;
            String FECHA_VALOR = null;
            String MONTO = null;
            String LOGICAL_TERMINAL = "?";
            String LOGICAL_TERMINAL2 = "?";
            int Referencia = 1;
            String NUMERO_CUENTA = "?";
            String NOMBRE_CLIENTE = "?";
            String DIRECCION_CLIENTE = "?";
            String TIPO_CUENTA = "?";
            String FECHA_NACIMIENTO = "?";
            String NUMERO_CUENTA_BENEFICIARIO = "?";
            String NOMBRE_CLIENTE_BENEFICIARIO = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO1 = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO2 = "?";
            String DIRECCION_CLIENTE_BENEFICIARIO3 = "?";
            String DIRECCION_BANCO_BENEFICIARIO1 = "?";
            String DIRECCION_BANCO_BENEFICIARIO2 = "?";
            String DIRECCION_BANCO_BENEFICIARIO3 = "?";
            String NOMBRE_BANCO_ORDENANTE = "?";
            String DIRECCION_BANCO_ORDENANTE1 = "?";
            String DIRECCION_BANCO_ORDENANTE2 = "?";
            String DIRECCION_BANCO_ORDENANTE3 = "?";
            String TIPO_CUENTA_BENEFICIARIO = "?";
            String SHAOUR = "?";
            String CODIGO_TTC = "?";
            String SIN = null;
            String NUMERO_IDENTIFICACION = "?";
            String pattern10 = "0000000000";
            String pattern7 = "0000000";
            long numeroControl = 0;
            df.applyPattern(pattern10);
            String numeroControlString = df.format(Referencia);
            df.applyPattern(pattern7);
            String numeroControlString7 = df.format(Referencia);
            String path = "";
            String user = "";
            String user2 = "";
            try {
                HoraInicioAplicativo = DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(cal);
                System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
            } catch (DatatypeConfigurationException ex) {
                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArrayList<String> Campo = new ArrayList<>();
            ArrayList<String> Dato = new ArrayList<>();
            ArrayList<String> TipoCampo = new ArrayList<>();
            System.out.println("********** accion.equals(\"O\") || accion.equals(\"o\" ********** : " + accion.toString());
            if (accion.equals("O") || accion.equals("o") || accion.equals("I") || accion.equals("i")) {
                System.out.println("**** PASO5a ***** " + TransferenciaColeccion.getTransferenciaItems().size());
                if (TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getCampo() == null) {
                    System.out.println("****  getCampo ***** null ");
                } else {
                    System.out.println("****  getCampo ***** NO null ");
                }
                if (TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getDato() == null) {
                    System.out.println("****  getDato ***** null ");
                } else {
                    System.out.println("****  getDato ***** NO null ");
                }
                if (TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getTipoCampo() == null) {
                    System.out.println("****  getTipoCampo ***** null ");
                } else {
                    System.out.println("****  getTipoCampo ***** NO null ");
                }

                for (int i = 0; i < TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size(); i++) {
                    Campo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getCampo());
                    Dato.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getDato());
                    TipoCampo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getTipoCampo());
                    System.out.println("****  getCampo ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getCampo());
                    System.out.println("****  getDato ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getDato());
                    System.out.println("****  getTipoCampo ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getTipoCampo());

//            BuscaInfoCuenta busca = new BuscaInfoCuenta();
//                    if (Campo.get(i).equals("TIPO_PARTICIPANTE")) {
//                        TIPO_PARTICIPANTE = Dato.get(i);
//                        System.out.println("**** TIPO_PARTICIPANTE ***** " + TIPO_PARTICIPANTE);
//                    }
//=====================================================================
                    try {
                        HoraInicioEvento = DatatypeFactory.newInstance()
                                .newXMLGregorianCalendar(cal);
                    } catch (DatatypeConfigurationException ex) {
//                        Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("********** DatatypeConfigurationException ********** : " + ex);
                    }
                    if (Campo.get(i).equals("1")) {
                        BIC_BANCO_ORDENANTE = Dato.get(i);
                        System.out.println("**** BIC_BANCO_ORDENANTE ***** " + BIC_BANCO_ORDENANTE);
                    }
                    if (Campo.get(i).equals("2")) {
                        BIC_BANCO_BENEFICIARIO = Dato.get(i);
                        System.out.println("**** BIC_BANCO_BENEFICIARIO ***** " + BIC_BANCO_BENEFICIARIO);
                    }
                    if (Campo.get(i).equals("3")) {
                        HEADER3 = Dato.get(i);
                        System.out.println("**** HEADER 3 ***** " + HEADER3);
                    }
                    if (Campo.get(i).equals("20")) {
                        REFERENCIA20 = Dato.get(i);
                        System.out.println("**** REFERENCIA20 ***** " + REFERENCIA20);
                    }
                    if (Campo.get(i).equals("23B")) {
                        // if (Campo.get(i).equals("23B") && TipoCampo.get(i).equals("CRED")) {
                        CODIGO_OPERACION = Dato.get(i);
                        System.out.println("**** CODIGO_OPERACION. ***** " + CODIGO_OPERACION);
                    }
                    if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("DATE")) {
                        FECHA_VALOR = Dato.get(i);
                        System.out.println("**** FECHA_VALOR ***** " + FECHA_VALOR);
                    }

                    if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("CURRENCY")) {
                        MONEDA = Dato.get(i);
                        System.out.println("**** CURRENCY ***** " + MONEDA);
                    }
                    if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("AMOUNT")) {
                        MONTO = Dato.get(i);
                        System.out.println("**** AMOUNT con punto decimal***** " + MONTO);
                        MONTO = MONTO.replace(".", ",");
                        System.out.println("**** AMOUNT con coma decimal***** " + MONTO);
                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ACCOUNT")) {
                        NUMERO_CUENTA = Dato.get(i);
                        System.out.println("**** ACCOUNT ***** " + NUMERO_CUENTA);
                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_ORDENANTE = Dato.get(i);
                        System.out.println("**** NOMBRE_ORDENANTE ***** " + NOMBRE_ORDENANTE);
                    }
//================================================Nuevo octubre 2024==================================================================                
//                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESS")) {
//                    DIRECCION_ORDENANTE = Dato.get(i);
//                    System.out.println("**** DIRECCION_ORDENANTE ***** " + DIRECCION_ORDENANTE);
//                }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_ORDENANTE1 = Dato.get(i);
                        if (DIRECCION_ORDENANTE1.length() >= 35) {
                            DIRECCION_ORDENANTE1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_ORDENANTE1 = Dato.get(i);
                        }
                        DIRECCION_ORDENANTE1 = validaString.sanitizeString(DIRECCION_ORDENANTE1);
                        System.out.println("**** DIRECCION_ORDENANTE1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_ORDENANTE1);
                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_ORDENANTE2 = Dato.get(i);
                        if (DIRECCION_ORDENANTE2.length() >= 35) {
                            DIRECCION_ORDENANTE2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_ORDENANTE2 = Dato.get(i);
                        }
                        DIRECCION_ORDENANTE2 = validaString.sanitizeString(DIRECCION_ORDENANTE2);
                        System.out.println("**** DIRECCION_ORDENANTE2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_ORDENANTE2);

                    }
                    if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_ORDENANTE3 = Dato.get(i);
                        if (DIRECCION_ORDENANTE3.length() >= 35) {
                            DIRECCION_ORDENANTE3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_ORDENANTE3 = Dato.get(i);
                        }
                        DIRECCION_ORDENANTE3 = validaString.sanitizeString(DIRECCION_ORDENANTE3);
                        System.out.println("**** DIRECCION_ORDENANTE3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_ORDENANTE3);
                    }
//================================================Nuevo octubre 2024==================================================================                                

                    if (Campo.get(i).equals("52A") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_ORDENANTE52A = Dato.get(i);
                        System.out.println("**** BIC_BANCO_ORDENANTE52A ***** " + BIC_BANCO_ORDENANTE52A);
                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_ORDENANTE52D = Dato.get(i);
                        System.out.println("**** ABAOCUENTA52D ***** " + BIC_BANCO_ORDENANTE52D);
                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_BANCO_ORDENANTE = Dato.get(i);
                        System.out.println("**** NOMBRE_BANCO_ORDENANTE ***** " + NOMBRE_BANCO_ORDENANTE);
                    }

//================================================Nuevo octubre 2024==================================================================                
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_ORDENANTE1 = Dato.get(i);
                        if (DIRECCION_BANCO_ORDENANTE1.length() >= 35) {
                            DIRECCION_BANCO_ORDENANTE1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_ORDENANTE1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_ORDENANTE1 = validaString.sanitizeString(DIRECCION_BANCO_ORDENANTE1);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_ORDENANTE1);
                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_ORDENANTE2 = Dato.get(i);
                        if (DIRECCION_BANCO_ORDENANTE2.length() >= 35) {
                            DIRECCION_BANCO_ORDENANTE2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_ORDENANTE2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_ORDENANTE2 = validaString.sanitizeString(DIRECCION_BANCO_ORDENANTE2);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_ORDENANTE2);

                    }
                    if (Campo.get(i).equals("52D") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_ORDENANTE3 = Dato.get(i);
                        if (DIRECCION_BANCO_ORDENANTE3.length() >= 35) {
                            DIRECCION_BANCO_ORDENANTE3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_ORDENANTE3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_ORDENANTE3 = validaString.sanitizeString(DIRECCION_BANCO_ORDENANTE3);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_ORDENANTE3);
                    }
//================================================Nuevo octubre 2024 end==================================================================
                    if (Campo.get(i).equals("56A") && TipoCampo.get(i).equals("IDENTIFIER")) {
                        BIC_BANCO_INTERMEDIARIO56A = Dato.get(i);
                        System.out.println("**** BIC_BANCO_INTERMEDIARIO56A ***** " + BIC_BANCO_INTERMEDIARIO56A);
                    }
                    if (Campo.get(i).equals("57A") && TipoCampo.get(i).equals("IDENTIFIER")) {
                        BIC_BANCO_BENEFICIARIO57A = Dato.get(i);
                        System.out.println("**** BIC_BANCO_BENEFICIARIO57A ***** " + BIC_BANCO_BENEFICIARIO57A);
                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_INTERMEDIARIO56D = Dato.get(i);
                        System.out.println("**** BIC_BANCO_INTERMEDIARIO1 ***** " + BIC_BANCO_INTERMEDIARIO56D);
                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_BANCO_INTERMEDIARIO56D = Dato.get(i);
                        System.out.println("**** NOMBRE_BANCO_INTERMEDIARIO56D ***** " + NOMBRE_BANCO_INTERMEDIARIO56D);
                    }
                    //     Cambio del dia 13 de noviembre 2024 JETA
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO1.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO1 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO1);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO1);
                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO2.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO2 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO2);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO2);

                    }
                    if (Campo.get(i).equals("56D") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i);
                        if (DIRECCION_BANCO_INTERMEDIARIO3.length() >= 35) {
                            DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_INTERMEDIARIO3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_INTERMEDIARIO3 = validaString.sanitizeString(DIRECCION_BANCO_INTERMEDIARIO3);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO3);
                    }
                    //     Fin Cambio del dia 13 de noviembre 2024 JETA
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("IDENTIFIER") && Dato.get(i).length() > 0) {
                        BIC_BANCO_BENEFICIARIO57D = Dato.get(i);
                        System.out.println("**** BIC_BANCO_BENEFICIARIO57D ***** " + BIC_BANCO_BENEFICIARIO57D);
                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("NAME")) {
                        NOMBRE_BANCO_BENEFICIARIO57D = Dato.get(i);
                        System.out.println("**** NOMBRE_BANCO_BENEFICIARIO57D ***** " + NOMBRE_BANCO_BENEFICIARIO57D);
                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO1.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO1 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO1 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO1);
                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO2.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO2 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO2 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO2);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_BANCO_BENEFICIARIO2);

                    }
                    if (Campo.get(i).equals("57D") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i);
                        if (DIRECCION_BANCO_BENEFICIARIO3.length() >= 35) {
                            DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_BANCO_BENEFICIARIO3 = Dato.get(i);
                        }
                        DIRECCION_BANCO_BENEFICIARIO3 = validaString.sanitizeString(DIRECCION_BANCO_BENEFICIARIO3);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_BANCO_INTERMEDIARIO3);
                    }

//================================================Nuevo octubre 2024 end================================================================== 		                    
                    if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ACCOUNT")) {
                        NUMERO_CUENTA_BENEFICIARIO = Dato.get(i);
                        System.out.println("**** NUMERO_CUENTA_BENEFICIARIO  ACCOUNT ***** " + NUMERO_CUENTA_BENEFICIARIO);
                    }
                    if (accion.equals("O") || accion.equals("o")) {
                        if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME")) {
                            NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(i);
                            System.out.println("**** NombreBeneficiario Antes Aprobar***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                            NOMBRE_CLIENTE_BENEFICIARIO = validaString.sanitizeString(NOMBRE_CLIENTE_BENEFICIARIO);
//=========================================

                            int longit = NOMBRE_CLIENTE_BENEFICIARIO.length();
                            System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO longitud ***** " + longit);
                            if (longit >= 35) {
                                NOMBRE_CLIENTE_BENEFICIARIO = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 35);
                            } else {
                                NOMBRE_CLIENTE_BENEFICIARIO = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, longit);
                            }

                            System.out.println("**** NombreBeneficiario Despues verifica listas ***** " + NOMBRE_CLIENTE_BENEFICIARIO);

//=========================================
//                            System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO Despues Aprobar***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                        }
                    } else {
                        if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("NAME") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME")) {
                            NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(i);
                            System.out.println("**** NombreBeneficiario Antes Aprobar***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                            NOMBRE_CLIENTE_BENEFICIARIO = validaString.sanitizeString(NOMBRE_CLIENTE_BENEFICIARIO);
                            NOMBRE_CLIENTE_BENEFICIARIO = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 35);
                            System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO Despues Aprobar***** " + NOMBRE_CLIENTE_BENEFICIARIO);
                        }
                    }

//                        if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME")) {
//                            NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(i);
//                            System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO ***** " + NOMBRE_CLIENTE_BENEFICIARIO);
//                        }
                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESSLN1") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESSLN1")) {
                        DIRECCION_CLIENTE_BENEFICIARIO1 = Dato.get(i);
                        if (DIRECCION_CLIENTE_BENEFICIARIO1.length() >= 35) {
                            DIRECCION_CLIENTE_BENEFICIARIO1 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_CLIENTE_BENEFICIARIO1 = Dato.get(i);
                        }
                        DIRECCION_CLIENTE_BENEFICIARIO1 = validaString.sanitizeString(DIRECCION_CLIENTE_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_CLIENTE_BENEFICIARIO1);
                    }
                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESSLN2") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESSLN2")) {
                        DIRECCION_CLIENTE_BENEFICIARIO2 = Dato.get(i);
                        if (DIRECCION_CLIENTE_BENEFICIARIO2.length() >= 35) {
                            DIRECCION_CLIENTE_BENEFICIARIO2 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_CLIENTE_BENEFICIARIO2 = Dato.get(i);
                        }
                        DIRECCION_CLIENTE_BENEFICIARIO2 = validaString.sanitizeString(DIRECCION_CLIENTE_BENEFICIARIO2);
                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_CLIENTE_BENEFICIARIO2);

                    }
                    if (Campo.get(i).equals("59") && TipoCampo.get(i).equals("ADDRESSLN3") || Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESSLN3")) {
                        DIRECCION_CLIENTE_BENEFICIARIO3 = Dato.get(i);
                        if (DIRECCION_CLIENTE_BENEFICIARIO3.length() >= 35) {
                            DIRECCION_CLIENTE_BENEFICIARIO3 = Dato.get(i).substring(0, 35);
                        } else {
                            DIRECCION_CLIENTE_BENEFICIARIO3 = Dato.get(i);
                        }
                        DIRECCION_CLIENTE_BENEFICIARIO3 = validaString.sanitizeString(DIRECCION_CLIENTE_BENEFICIARIO3);
                        System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_CLIENTE_BENEFICIARIO3);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN1")) {
                        MOTIVO1 = Dato.get(i);
                        if (MOTIVO1.length() >= 35) {
                            MOTIVO1 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO1 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO1 ***** " + MOTIVO1);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN2")) {
                        MOTIVO2 = Dato.get(i);
                        if (MOTIVO2.length() >= 35) {
                            MOTIVO2 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO2 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO2 ***** " + MOTIVO2);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN3")) {
                        MOTIVO3 = Dato.get(i);
                        if (MOTIVO3.length() >= 35) {
                            MOTIVO3 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO3 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO3 ***** " + MOTIVO3);
                    }
                    if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVOLN4")) {
                        MOTIVO4 = Dato.get(i);
                        if (MOTIVO4.length() >= 35) {
                            MOTIVO4 = Dato.get(i).substring(0, 35);
                        } else {
                            MOTIVO4 = Dato.get(i);
                        }
                        System.out.println("**** MOTIVO4 ***** " + MOTIVO4);
                    }
                    if (Campo.get(i).equals("71A") && TipoCampo.get(i).equals("DETAIL")) {
                        SHAOUR = Dato.get(i);
                        System.out.println("**** DETAIL SHAOUR***** " + SHAOUR);
                    }
                }
                ArrayList<ParametrosAdicionales> detay = new ArrayList<>();
                for (int i = 0; i < parametroAdicional.getParametroAdicionalItem().size(); i++) {
//            System.out.println("**** Accion ***** " + accion);//
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("CON")) {
                        valorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
//                System.out.println("**** cuando es CON  ***** " + valorCoincidencia);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("SIN")) {
                        NovalorCoincidencia = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
//                System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ICI")) {
                        valorCoincidenciaICI = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
//                System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ECX")) {
                        valorCoincidenciaECX = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
//                System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                    }
                    if (parametroAdicional.getParametroAdicionalItem().get(i).getTipoRegistro().equals("ERR")) {
                        ERROR = parametroAdicional.getParametroAdicionalItem().get(i).getValor();
//                System.out.println("**** cuando es SIN  ***** " + NovalorCoincidencia);//
                    }
//                LineaAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getLinea());
//                TipoRegistroAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getTipoRegistro());
//                ValorAdicional.add(parametroAdicional.getParametroAdicionalItem().get(0).getValor());
                }
//==============================Connect to dataBase=================================================================
//                DBConnectMotor coneccionMotor = new DBConnectMotor();
//                con = coneccionMotor.obtenerConeccion();
//                stmt = con.createStatement();
//                String sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'ENTORNO' and ParametroValorCodigo = 'TOSWIFT'";
//                rs = stmt.executeQuery(sql);
//                while (rs.next()) {
//                    ParametroValorDato = rs.getString("ParametroValorDato");
//                }
//                System.out.println("**** sql  ***** " + ParametroValorDato);
//                con.close();
//===============================================================================================    
                System.out.println("**** pass1 ***** ");
//===============================================================================================        
//            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
//                text = "{1:F01BCEHHNT0AXXX" + numeroControlString + "}{2:I103BCEHHNT0XXXXN}{3:{103:SPH}}{4:\r\n";
//            } else {
                ValidarTags valida = new ValidarTags();
//                valida.setConexion(con);
                valida.setLlaveSesion(llaveSesion);
                valida.setPantalla(pantalla);
                valida.setTerminal(terminal);
                valida.setUsuarioId(usuarioId);
                valida.setTransactionId(transactionId);
                System.out.println("**** pasox1 *****  ");
//                if (BIC_BANCO_ORDENANTE.equals("?")) {
//                    deta = validaTags(valida, "BIC_BANCO_ORDENANTE = ?");
//                    return deta;
//                }
//                System.out.println("**** pasox1 *****  ");
//                if (BIC_BANCO_BENEFICIARIO.equals("?")) {
//                    deta = validaTags(valida, "BIC_BANCO_BENEFICIARIO = ?");
//                    return deta;
//                }
//                System.out.println("**** pasox1 *****  ");
//                if (HEADER3.equals("?")) {
//                    deta = validaTags(valida, "HEADER3 = ?");
//                }
                text = "{1:" + BIC_BANCO_ORDENANTE + "}{2:" + BIC_BANCO_BENEFICIARIO + "}{3:" + HEADER3 + "}{4:\r\n";
//                text = "{1:F01" + BIC_BANCO_ORDENANTE + numeroControlString + "}{2:I103" + BIC_BANCO_BENEFICIARIO + "N}{3:{103:SPH}}{4:\r\n";
                //text = "{1:F01" + LOGICAL_TERMINAL + numeroControlString + "}{2:I103" + LOGICAL_TERMINAL2 + "}{3:{103:SPH}}{4:\r\n";
////            }
                System.out.println("**** pasox1a *****  ");
//                if (REFERENCIA20.equals("?")) {
//                    deta = deta = validaTags(valida, "REFERENCIA20 = ?");
//                    return deta;
//                }
                text = text + ":20:" + REFERENCIA20 + "\r\n";
                //text = text + ":20:" + BIC_BANCO_ENVIA.substring(0, 4) + numeroControlString7 + "\r\n";
                System.out.println("**** pasox2 *****  ");
//                if (CODIGO_OPERACION.equals("?")) {
//                    deta = validaTags(valida, "CODIGO_OPERACION 23B = ?");
//                    return deta;
//                }
                text = text + ":23B:" + CODIGO_OPERACION + "\r\n";
                System.out.println("**** pasox3 *****  ");
//                if (FECHA_VALOR.equals("?")) {
//                    deta = validaTags(valida, "FECHA_VALOR = ?");
//                    return deta;
//                }
//                if (MONEDA.equals("?")) {
//                    deta = validaTags(valida, "MONEDA = ?");
//                    return deta;
//                }
//                if (MONTO.equals("?")) {
//                    deta = validaTags(valida, "MONTO = ?");
//                    return deta;
//                }
                text = text + ":32A:" + FECHA_VALOR + MONEDA + MONTO + "\r\n";
                text = text + ":33B:" + MONEDA + MONTO + "\r\n";
                System.out.println("**** pasox4 *****  ");
//                if (NUMERO_CUENTA.equals("?")) {
//                    deta = validaTags(valida, "NUMERO_CUENTA = ?");
//                    return deta;
//                }
                text = text + ":50K:/" + NUMERO_CUENTA.toString().trim() + "\r\n";
                System.out.println("**** pasox5 *****  ");
//                if (NOMBRE_ORDENANTE.equals("?")) {
//                    deta = validaTags(valida, "NOMBRE_ORDENANTE = ?");
//                    return deta;
//                }
                if (NOMBRE_ORDENANTE.length() > 35) {
                    path = NOMBRE_ORDENANTE.substring(0, 35);
                    if (NOMBRE_ORDENANTE.length() > 70) {
                        System.out.println("**** pasox5a - path + user + user2 *****  ");
                        user = NOMBRE_ORDENANTE.substring(35, 70);
                        user2 = NOMBRE_ORDENANTE.substring(70, NOMBRE_ORDENANTE.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                        text = text + user2 + "\r\n";
                    } else {
                        System.out.println("**** pasox5b - path + user *****  ");
                        user = NOMBRE_ORDENANTE.substring(35, NOMBRE_ORDENANTE.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                    }

                } else {
                    System.out.println("**** pasox5c - path/NOMBRE_ORDENANTE  *****  ");
                    text = text + NOMBRE_ORDENANTE + "\r\n";
                }

                path = "";
                user = "";
                user2 = "";
//                DIRECCION_ORDENANTE1 = "Solo para prueba";
                System.out.println("**** pasox6 *****  ");
//                if (DIRECCION_ORDENANTE1.equals("")) {
//                    deta = validaTags(valida, "DIRECCION_ORDENANTE1 = blanco");
//                    return deta;
//                }
//                path = DIRECCION_ORDENANTE1;
//                text = text + path + "\r\n";                
//                if (!DIRECCION_ORDENANTE2.equals("")) {
//                    user = DIRECCION_ORDENANTE2;
//                    text = text + user + "\r\n";
//                }
//                if (!DIRECCION_ORDENANTE3.equals("")) {
//                    user2 = DIRECCION_ORDENANTE3;
//                text = text + user2 + "\r\n";                    
//                }

//=========================================================  begin 50A ======================================                     
                System.out.println("**** pasox6A *****  ");
                if (DIRECCION_ORDENANTE1.equals("") || DIRECCION_ORDENANTE1.equals(" ") || DIRECCION_ORDENANTE1.equals("?")) {
//                    deta = validaTags(valida, "DIRECCION_ORDENANTE1 = (b)" + DIRECCION_ORDENANTE1);
//                        return deta;
                    System.out.println("**** DIRECCION_ORDENANTE1 is =  *****  " + DIRECCION_ORDENANTE1);
                } else {
                    path = DIRECCION_ORDENANTE1;
                    text = text + path + "\r\n";
                }

                if (DIRECCION_ORDENANTE2.equals("") || DIRECCION_ORDENANTE2.equals(" ") || DIRECCION_ORDENANTE2.equals("?")) {
                    System.out.println("**** DIRECCION_ORDENANTE2 is = b  *****  " + DIRECCION_ORDENANTE2);
                } else {
                    user = DIRECCION_ORDENANTE2;
                    text = text + user + "\r\n";
                }
                if (DIRECCION_ORDENANTE3.equals("") || DIRECCION_ORDENANTE3.equals(" ") || DIRECCION_ORDENANTE3.equals("?")) {
                    System.out.println("**** DIRECCION_ORDENANTE3 is = b  *****  " + DIRECCION_ORDENANTE3);
                } else {
                    user2 = DIRECCION_ORDENANTE3;
                    text = text + user2 + "\r\n";
                }
//=========================================================  end 50A======================================                   
//                if (DIRECCION_ORDENANTE.length() > 35) {
//                    path = DIRECCION_ORDENANTE.substring(0, 35);
//                    if (DIRECCION_ORDENANTE.length() > 70) {
//                        System.out.println("**** pasox6a - path + user + user2 *****  ");
//                        user = DIRECCION_ORDENANTE.substring(35, 70);
//                        user2 = DIRECCION_ORDENANTE.substring(70, DIRECCION_ORDENANTE.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                        text = text + user2 + "\r\n";
//                    } else {
//                        System.out.println("**** pasox6b - path + user *****  ");
//                        user = DIRECCION_ORDENANTE.substring(35, DIRECCION_ORDENANTE.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                    }
//
//                } else {
//                    System.out.println("**** pasox6c - path/NOMBRE_ORDENANTE  *****  ");
//                    text = text + DIRECCION_ORDENANTE + "\r\n";
//                }NOMBRE_BANCO_ORDENANTE
                System.out.println("**** pasox7 *****  ");
                if (BIC_BANCO_ORDENANTE52A.equals("?")) {
                    System.out.println("BIC_BANCO_ORDENANTE52A.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_ORDENANTE52A = ?");
//                return deta;
                } else {
                    text = text + ":52A:" + BIC_BANCO_ORDENANTE52A.toString().trim() + "\r\n";
                }
                System.out.println("**** pasox7 *****  ");
                System.out.println("**** pasox7a *****  ");  //OJO Cambio hoy 13 de nov 2024 JETA
                if (BIC_BANCO_ORDENANTE52D.equals("?") || BIC_BANCO_ORDENANTE52D.equals("") || BIC_BANCO_ORDENANTE52D.equals(" ")) {
                    System.out.println("BIC_BANCO_ORDENANTE52D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                } else {
                    if (BIC_BANCO_ORDENANTE52D.length() == 9) {
                        text = text + ":52D://FW" + BIC_BANCO_ORDENANTE52D.toString().trim() + "\r\n";
                    } else {
                        text = text + ":52D:/" + BIC_BANCO_ORDENANTE52D.toString().trim() + "\r\n";
                    }
                    if (NOMBRE_BANCO_ORDENANTE.length() > 35) {
                        path = NOMBRE_BANCO_ORDENANTE.substring(0, 35);
                        if (NOMBRE_BANCO_ORDENANTE.length() > 70) {
                            System.out.println("**** pasox11a - path + user + user2 *****  ");
                            user = NOMBRE_BANCO_ORDENANTE.substring(35, 70);
                            user2 = NOMBRE_BANCO_ORDENANTE.substring(70, NOMBRE_BANCO_ORDENANTE.length());
                            text = text + path + "\r\n";
                            text = text + user + "\r\n";
                            text = text + user2 + "\r\n";
                        } else {
                            System.out.println("**** pasox12b - path + user *****  ");
                            user = NOMBRE_BANCO_ORDENANTE.substring(35, NOMBRE_BANCO_ORDENANTE.length());
                            text = text + path + "\r\n";
                            text = text + user + "\r\n";
                        }

                    } else {
                        System.out.println("**** pasox13c - path/NOMBRE_ORDENANTE  *****  ");
                        text = text + NOMBRE_BANCO_ORDENANTE + "\r\n";
                    }
//=========================================================  begin 52A ======================================                     
                    System.out.println("**** pasox7A *****  ");
                    if (NOMBRE_BANCO_ORDENANTE.equals("") || NOMBRE_BANCO_ORDENANTE.equals(" ") || NOMBRE_BANCO_ORDENANTE.equals("?")) {
//                        deta = validaTags(valida, "NOMBRE_BANCO_ORDENANTE = (b)" + NOMBRE_BANCO_ORDENANTE);
                        System.out.println("**** NOMBRE_BANCO_ORDENANTE is = b  *****  " + NOMBRE_BANCO_ORDENANTE);
//                        return deta;
                    } else {
                        path = NOMBRE_BANCO_ORDENANTE;
                        text = text + NOMBRE_BANCO_ORDENANTE + "\r\n";
                    }
                    if (DIRECCION_BANCO_ORDENANTE1.equals("") || DIRECCION_BANCO_ORDENANTE1.equals(" ") || DIRECCION_BANCO_ORDENANTE1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_ORDENANTE1 = (b)" + DIRECCION_BANCO_ORDENANTE1);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE1 is = b  *****  " + DIRECCION_BANCO_ORDENANTE1);
//                        return deta;
                    } else {
                        path = DIRECCION_BANCO_ORDENANTE1;
                        text = text + DIRECCION_BANCO_ORDENANTE1 + "\r\n";
                    }

                    if (DIRECCION_BANCO_ORDENANTE2.equals("") || DIRECCION_BANCO_ORDENANTE2.equals(" ") || DIRECCION_BANCO_ORDENANTE2.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_ORDENANTE2 = (b)" + DIRECCION_BANCO_ORDENANTE2);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE2 is = b  *****  " + DIRECCION_BANCO_ORDENANTE2);
                    } else {
                        user = DIRECCION_BANCO_ORDENANTE2;
                        text = text + user + "\r\n";
                    }
                    if (DIRECCION_BANCO_ORDENANTE3.equals("") || DIRECCION_BANCO_ORDENANTE3.equals(" ") || DIRECCION_BANCO_ORDENANTE3.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_ORDENANTE3 = (b)" + DIRECCION_BANCO_ORDENANTE3);
                        System.out.println("**** DIRECCION_BANCO_ORDENANTE3 is = b  *****  " + DIRECCION_BANCO_ORDENANTE3);
                    } else {
                        user2 = DIRECCION_BANCO_ORDENANTE3;
                        text = text + user2 + "\r\n";
                    }

//=========================================================  end 52A======================================                       
                }

                //OJO Fin Cambio hoy 13 de nov 2024 JETA
//                if (BIC_BANCO_ORDENANTE52D.equals("?")) {
//                    System.out.println("BIC_BANCO_ORDENANTE52D.equals(\"?\")  ");
//                } else {
//                    text = text + ":52D:" + BIC_BANCO_ORDENANTE52D.toString().trim() + "\r\n";
//                }
                //OJO Fin Cambio hoy 13 de nov 2024 JETA
                System.out.println("**** pasox8 *****  ");
                if (BIC_BANCO_INTERMEDIARIO56A.equals("?") || BIC_BANCO_INTERMEDIARIO56A.equals("") || BIC_BANCO_INTERMEDIARIO56A.equals(" ")) {
                    System.out.println("BIC_BANCO_INTERMEDIARIO56A.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_INTERMEDIARIO56A = ?");
//                return deta;
                } else {
                    text = text + ":56A:" + BIC_BANCO_INTERMEDIARIO56A.toString().trim() + "\r\n";
//=========================================================  begin 56A ======================================                     
                    System.out.println("**** pasox8A *****  ");
//                    if (DIRECCION_BANCO_INTERMEDIARIO1.equals("") || DIRECCION_BANCO_INTERMEDIARIO1.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_INTERMEDIARIO1 = (b)" + DIRECCION_BANCO_INTERMEDIARIO1);
//                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO1);
////                        return deta;
//                    } else {
//                        path = DIRECCION_BANCO_INTERMEDIARIO1;
//                        text = text + path + "\r\n";
//                    }
//
//                    if (DIRECCION_BANCO_INTERMEDIARIO2.equals("") || DIRECCION_BANCO_INTERMEDIARIO2.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO2.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO2);
//                    } else {
//                        user = DIRECCION_BANCO_INTERMEDIARIO2;
//                        text = text + user + "\r\n";
//                    }
//                    if (DIRECCION_BANCO_INTERMEDIARIO3.equals("") || DIRECCION_BANCO_INTERMEDIARIO3.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO3.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO3);
//                    } else {
//                        user2 = DIRECCION_BANCO_INTERMEDIARIO3;
//                        text = text + user2 + "\r\n";
//                    }
//=========================================================  end 56A======================================                       
                }
//=========================================================  end 52A====================================== 
                System.out.println("**** pasox8c *****  ");  //OJO Cambio hoy 13 de nov 2024 JETA
                if (BIC_BANCO_INTERMEDIARIO56D.equals("?") || BIC_BANCO_INTERMEDIARIO56D.equals("") || BIC_BANCO_INTERMEDIARIO56D.equals(" ")) {
                    System.out.println("BIC_BANCO_INTERMEDIARIO56D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                } else {
                    if (BIC_BANCO_INTERMEDIARIO56D.length() == 9) {
                        text = text + ":56D://FW" + BIC_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
                    } else {
                        text = text + ":56D:/" + BIC_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
                    }
//OJO Fin Cambio hoy 13 de nov 2024 JETA                
//AQUI
                    System.out.println("**** pasox9D *****  ");
                    if (NOMBRE_BANCO_INTERMEDIARIO56D.equals("?") || NOMBRE_BANCO_INTERMEDIARIO56D.equals("") || NOMBRE_BANCO_INTERMEDIARIO56D.equals(" ")) {
                        System.out.println("NOMBRE_BANCO_INTERMEDIARIO56D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                    } else {
                        text = text + NOMBRE_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
                    }
                    System.out.println("**** pasox9E *****  ");
                    if (DIRECCION_BANCO_INTERMEDIARIO1.equals("") || DIRECCION_BANCO_INTERMEDIARIO1.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO1 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO1);
//                        return deta;
                    } else {
                        path = DIRECCION_BANCO_INTERMEDIARIO1;
                        text = text + path + "\r\n";
                    }

                    if (DIRECCION_BANCO_INTERMEDIARIO2.equals("") || DIRECCION_BANCO_INTERMEDIARIO2.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO2.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO2 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO2);
                    } else {
                        user = DIRECCION_BANCO_INTERMEDIARIO2;
                        text = text + user + "\r\n";
                    }
                    if (DIRECCION_BANCO_INTERMEDIARIO3.equals("") || DIRECCION_BANCO_INTERMEDIARIO3.equals(" ") || DIRECCION_BANCO_INTERMEDIARIO3.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_INTERMEDIARIO3 is = b  *****  " + DIRECCION_BANCO_INTERMEDIARIO3);
                    } else {
                        user2 = DIRECCION_BANCO_INTERMEDIARIO3;
                        text = text + user2 + "\r\n";
                    }
//=========================================================  begin 56A ======================================  
                }
                System.out.println("**** pasox8D *****  ");
//                if (BIC_BANCO_INTERMEDIARIO56D.equals("?") || BIC_BANCO_INTERMEDIARIO56D.equals("") || BIC_BANCO_INTERMEDIARIO56D.equals(" ")) {
//                    System.out.println("BIC_BANCO_INTERMEDIARIO56D.equals(\"?\")  ");
//                } else {
//                    text = text + ":56D:" + BIC_BANCO_INTERMEDIARIO56D.toString().trim() + "\r\n";
//                }

//OJO REVIZAR
                System.out.println("**** pasox9 *****  ");
                if (BIC_BANCO_BENEFICIARIO57A.equals("?")) {
                    System.out.println("BIC_BANCO_BENEFICIARIO57A.equals(\"?\")  ");
//                    deta = validaTags(valida, "BIC_BANCO_BENEFICIARIO57A = ?");
                    //OJO JETA modificacion 13 de noviembre 2024 comentar el return
//                    return deta;
                } else {
                    text = text + ":57A:" + BIC_BANCO_BENEFICIARIO57A.toString().trim() + "\r\n";
//=========================================================  begin 57A ======================================                     
                    System.out.println("**** pasox9A *****  ");
//                    if (DIRECCION_BANCO_BENEFICIARIO1.equals("") || DIRECCION_BANCO_BENEFICIARIO1.equals(" ") || DIRECCION_BANCO_BENEFICIARIO1.equals("?")) {
////                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
//                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO1);
////                        return deta;
//                    } else {
//                        path = DIRECCION_BANCO_BENEFICIARIO1;
//                        text = text + path + "\r\n";
//                    }
//
//                    if (DIRECCION_BANCO_BENEFICIARIO2.equals("") || DIRECCION_BANCO_BENEFICIARIO2.equals(" ") || DIRECCION_BANCO_BENEFICIARIO2.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO2);
//                    } else {
//                        user = DIRECCION_BANCO_BENEFICIARIO2;
//                        text = text + user + "\r\n";
//                    }
//                    if (DIRECCION_BANCO_BENEFICIARIO3.equals("") || DIRECCION_BANCO_BENEFICIARIO3.equals(" ") || DIRECCION_BANCO_BENEFICIARIO3.equals("?")) {
//                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO3);
//                    } else {
//                        user2 = DIRECCION_BANCO_BENEFICIARIO3;
//                        text = text + user2 + "\r\n";
//                    }
//=========================================================  end 57A======================================                    
                }
//=========================================================  end 52A======================================   
                System.out.println("**** pasox9D *****  ");
                if (BIC_BANCO_BENEFICIARIO57D.equals("?") || BIC_BANCO_BENEFICIARIO57D.equals("") || BIC_BANCO_BENEFICIARIO57D.equals(" ")) {
                    System.out.println("BIC_BANCO_BENEFICIARIO57D.equals(\"?\")  ");
//                deta = validaTags("BIC_BANCO_BENEFICIARIO57D = ?");
//                return deta;
                } else {
                    if (BIC_BANCO_BENEFICIARIO57D.length() == 9) {
                        text = text + ":57D://FW" + BIC_BANCO_BENEFICIARIO57D.toString().trim() + "\r\n";
                    } else {
                        text = text + ":57D:/" + BIC_BANCO_BENEFICIARIO57D.toString().trim() + "\r\n";
                    }
                    if (NOMBRE_BANCO_BENEFICIARIO57D.equals("") || NOMBRE_BANCO_BENEFICIARIO57D.equals(" ") || NOMBRE_BANCO_BENEFICIARIO57D.equals("?")) {
                        System.out.println("**** NOMBRE_BANCO_BENEFICIARIO57D is = b  *****  " + NOMBRE_BANCO_BENEFICIARIO57D);
                    } else {
                        text = text + NOMBRE_BANCO_BENEFICIARIO57D + "\r\n";
                    }
                    System.out.println("**** pasox9E *****  ");
                    if (DIRECCION_BANCO_BENEFICIARIO1.equals("") || DIRECCION_BANCO_BENEFICIARIO1.equals(" ") || DIRECCION_BANCO_BENEFICIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO1 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO1);
//                        return deta;
                    } else {
                        path = DIRECCION_BANCO_BENEFICIARIO1;
                        text = text + path + "\r\n";
                    }

                    if (DIRECCION_BANCO_BENEFICIARIO2.equals("") || DIRECCION_BANCO_BENEFICIARIO2.equals(" ") || DIRECCION_BANCO_BENEFICIARIO2.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO2 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO2);
                    } else {
                        user = DIRECCION_BANCO_BENEFICIARIO2;
                        text = text + user + "\r\n";
                    }
                    if (DIRECCION_BANCO_BENEFICIARIO3.equals("") || DIRECCION_BANCO_BENEFICIARIO3.equals(" ") || DIRECCION_BANCO_BENEFICIARIO3.equals("?")) {
                        System.out.println("**** DIRECCION_BANCO_BENEFICIARIO3 is = b  *****  " + DIRECCION_BANCO_BENEFICIARIO3);
                    } else {
                        user2 = DIRECCION_BANCO_BENEFICIARIO3;
                        text = text + user2 + "\r\n";
                    }
                    //=========================================================  begin 56A ======================================     
                }

                System.out.println("**** pasox10 *****  ");
//                if (NUMERO_CUENTA_BENEFICIARIO.equals("?")) {
//                    deta = validaTags(valida, "NUMERO_CUENTA_BENEFICIARIO = ?");
//                    return deta;
//                }
                text = text + ":59:/" + NUMERO_CUENTA_BENEFICIARIO.toString().trim() + "\r\n";
                System.out.println("**** pasox11 *****  ");
//                if (NOMBRE_CLIENTE_BENEFICIARIO.equals("?")) {
//                    deta = validaTags(valida, "NOMBRE_ORDENANTE = ?");
//                    return deta;
//                }
                if (NOMBRE_CLIENTE_BENEFICIARIO.length() > 35) {
                    path = NOMBRE_CLIENTE_BENEFICIARIO.substring(0, 35);
                    if (NOMBRE_CLIENTE_BENEFICIARIO.length() > 70) {
                        System.out.println("**** pasox11a - path + user + user2 *****  ");
                        user = NOMBRE_CLIENTE_BENEFICIARIO.substring(35, 70);
                        user2 = NOMBRE_CLIENTE_BENEFICIARIO.substring(70, NOMBRE_CLIENTE_BENEFICIARIO.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                        text = text + user2 + "\r\n";
                    } else {
                        System.out.println("**** pasox12b - path + user *****  ");
                        user = NOMBRE_CLIENTE_BENEFICIARIO.substring(35, NOMBRE_CLIENTE_BENEFICIARIO.length());
                        text = text + path + "\r\n";
                        text = text + user + "\r\n";
                    }

                } else {
                    System.out.println("**** pasox13c - path/NOMBRE_ORDENANTE  *****  ");
                    text = text + NOMBRE_CLIENTE_BENEFICIARIO + "\r\n";
                }

                path = "";
                user = "";
                user2 = "";
                System.out.println("**** pasox14 *****  ");
//=========================================================  begin 59A ======================================                     
                System.out.println("**** pasox14A *****  ");
                if (DIRECCION_CLIENTE_BENEFICIARIO1.equals("") || DIRECCION_CLIENTE_BENEFICIARIO1.equals(" ") || DIRECCION_CLIENTE_BENEFICIARIO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO1 is = b  *****  " + DIRECCION_CLIENTE_BENEFICIARIO1);
//                        return deta;

                } else {
                    path = DIRECCION_CLIENTE_BENEFICIARIO1;
                    text = text + path + "\r\n";
                }

                if (DIRECCION_CLIENTE_BENEFICIARIO2.equals("") || DIRECCION_CLIENTE_BENEFICIARIO2.equals(" ") || DIRECCION_CLIENTE_BENEFICIARIO2.equals("?")) {
                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO2 is = b  *****  " + DIRECCION_CLIENTE_BENEFICIARIO2);
                } else {
                    user = DIRECCION_CLIENTE_BENEFICIARIO2;
                    text = text + user + "\r\n";
                }
                if (DIRECCION_CLIENTE_BENEFICIARIO3.equals("") || DIRECCION_CLIENTE_BENEFICIARIO3.equals(" ") || DIRECCION_CLIENTE_BENEFICIARIO3.equals("?")) {
                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO3 is = b  *****  " + DIRECCION_CLIENTE_BENEFICIARIO3);
                } else {
                    user2 = DIRECCION_CLIENTE_BENEFICIARIO3;
                    text = text + user2 + "\r\n";
                }
//=========================================================  end 59A======================================                   
//                if (DIRECCION_CLIENTE_BENEFICIARIO.equals("?")) {
//                    deta = validaTags(valida, "DIRECCION_CLIENTE_BENEFICIARIO = ?");
//                    return deta;
//                }
                System.out.println("**** pasox14 *****  ");
//                if (DIRECCION_CLIENTE_BENEFICIARIO.length() > 35) {
//                    path = DIRECCION_CLIENTE_BENEFICIARIO.substring(0, 35);
//                    if (DIRECCION_CLIENTE_BENEFICIARIO.length() > 70) {
//                        System.out.println("**** pasox14a - path + user + user2 *****  ");
//                        user = DIRECCION_CLIENTE_BENEFICIARIO.substring(35, 70);
//                        user2 = DIRECCION_CLIENTE_BENEFICIARIO.substring(70, DIRECCION_CLIENTE_BENEFICIARIO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                        text = text + user2 + "\r\n";
//                    } else {
//                        System.out.println("**** pasox14b - path + user *****  ");
//                        user = DIRECCION_CLIENTE_BENEFICIARIO.substring(35, DIRECCION_CLIENTE_BENEFICIARIO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                    }
//
//                } else {
//                    System.out.println("**** pasox14c - path/DIRECCION_CLIENTE_BENEFICIARIO  *****  ");
//                    text = text + DIRECCION_CLIENTE_BENEFICIARIO + "\r\n";
//                }
                System.out.println("**** pasox15 *****  ");
//                if (MOTIVO1.equals("?")) {
//                    deta = validaTags(valida, "MOTIVO = ?");
//                    return deta;
//                }
                path = "";
                user = "";
                user2 = "";
                if (MOTIVO1.equals("") || MOTIVO1.equals(" ") || MOTIVO1.equals("?")) {
//                        deta = validaTags(valida, "DIRECCION_BANCO_BENEFICIARIO1 = (b)"+DIRECCION_BANCO_BENEFICIARIO1);
                    System.out.println("**** MOTIVO1 is = b  *****  " + MOTIVO1);
//                        return deta;
                } else {
                    MOTIVO1 = validaString.sanitizeString(MOTIVO1);
                    path = MOTIVO1;
                    text = text + ":70:" + path.toString().trim() + "\r\n";
//                    text = text + path + "\r\n";
                }

                if (MOTIVO2.equals("") || MOTIVO2.equals(" ") || MOTIVO2.equals("?")) {
                    System.out.println("**** MOTIVO2 is = b  *****  " + MOTIVO2);
                } else {
                    user = MOTIVO2;
                    text = text + user + "\r\n";
                }
                if (MOTIVO3.equals("") || MOTIVO3.equals(" ") || MOTIVO3.equals("?")) {
                    System.out.println("**** MOTIVO3 is = b  *****  " + MOTIVO3);
                } else {
                    user2 = MOTIVO3;
                    text = text + user2 + "\r\n";
                }
                if (MOTIVO4.equals("") || MOTIVO4.equals(" ") || MOTIVO4.equals("?")) {
                    System.out.println("**** MOTIVO4 is = b  *****  " + MOTIVO4);
                } else {
                    text = text + MOTIVO4 + "\r\n";
                }
//                if (MOTIVO1.length() > 35) {
//                    path = ":70:" + MOTIVO1.substring(0, 35);
//                    if (MOTIVO.length() > 70) {
//                        System.out.println("**** pasox15a - path + user + user2 *****  ");
//                        user = MOTIVO.substring(35, 70);
//                        user2 = MOTIVO.substring(70, MOTIVO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                        text = text + user2 + "\r\n";
//                    } else {
//                        System.out.println("**** pasox15b - path + user *****  ");
//                        user = MOTIVO.substring(35, MOTIVO.length());
//                        text = text + path + "\r\n";
//                        text = text + user + "\r\n";
//                    }
//
//                } else {
//                    System.out.println("**** pasox15c - path/DIRECCION_CLIENTE_BENEFICIARIO  *****  ");
//                    text = text + ":70:" + MOTIVO + "\r\n";
//                }
                System.out.println("**** pasox16 *****  " + SHAOUR);
                if (SHAOUR.equals("?")) {
                    System.out.println("**** pasox16a SHAOUR *****  " + SHAOUR);
//                    deta = validaTags(valida, "OUR = ?");
//                    return deta;

                }

                text = text + ":71A:" + SHAOUR + "\r\n";
                if (SHAOUR.equals("SHA")) {
                    text = text + ":71F:USD0,00" + "\r\n";
                }
//===============================================================================================

            }
//            System.out.println("accion.equals(\"I\") || accion.equals(\"i\"");
//            if (accion.equals("I") || accion.equals("i")) {
//                System.out.println("**** PASO5xxx ***** " + TransferenciaColeccion.getTransferenciaItems().size());
//                System.out.println("accion.equals(\"I\") || accion.equals(\"i\"  INICIA");
//                for (int f = 0; f < TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size(); f++) {
//                    Campo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getCampo());
//                    Dato.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getDato());
//                    TipoCampo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getTipoCampo());
//                    System.out.println("****  tamano de el indice f ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getCampo());
//                    System.out.println("****  tamano de el indice f ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getDato());
//                    System.out.println("****  tamano de el indice f ***** " + TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(f).getTipoCampo());
//                    System.out.println("****  tamano de el indice f ***** " + f);
//                    if (Campo.get(f).equals("59") && TipoCampo.get(f).equals("NAME")) {
//                        NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(f);
//                        System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO Transferencia entrante***** " + NOMBRE_CLIENTE_BENEFICIARIO);
//                    }
//                }
////                DBConnectMotor coneccionMotor = new DBConnectMotor();
////                con = coneccionMotor.obtenerConeccion();
////                stmt = con.createStatement();
////                String sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'ENTORNO' and ParametroValorCodigo = 'TOSWIFT'";
////                rs = stmt.executeQuery(sql);
////                while (rs.next()) {
////                    ParametroValorDato = rs.getString("ParametroValorDato");
////                }
//            }

//===============================================================================================
            System.out.println("PASO 7 : " + text);
            text = text + "-}";
            path = null;
//===============================================================================================
//================================================================================================
            PrintWriter out = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            java.util.Date date = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            System.out.println("año    ");
            String año = String.valueOf(calendar.get(Calendar.YEAR));
            System.out.println("mesInt    ");
            int mesInt = calendar.get(Calendar.MONTH) + 1;
            String pattern2 = "00";
            df.applyPattern(pattern2);
            System.out.println("mes dia    ");
            String mes = df.format(mesInt);
            System.out.println("mes      ");
            int diaInt = calendar.get(Calendar.DAY_OF_MONTH);
            String dia = df.format(diaInt);
            System.out.println(" dia    ");
            String MiliSecods = String.valueOf(calendar.get(Calendar.MILLISECOND));
            //========================================================================
            LocalDateTime now = LocalDateTime.now();
            System.out.println("LocalDateTime      " + now);
            // Create a formatter with milliseconds
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss-SSS");

            // Format and print the date and time with milliseconds
            String formattedDateTime = now.format(formatter);
            System.out.println("Current date and time with milliseconds: " + formattedDateTime);
            //                System.out.println("ParametroValorDato : " + ParametroValorDato);
//                System.out.println("File Name          : " + ParametroValorDato + "/MT103-" + transactionId + "-" + formattedDateTime + ".txt");
            System.out.println("MT103 text : " + text);
//out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/" + año + mes + dia + MiliSecods + "MT103.txt", true)));
//                out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/MT103-" + transactionId + "-" + formattedDateTime + ".txt", true)));
//                out.println(text);
//                out.close();
//                con.close();
//================================================================================================
            trama = new ByteArrayInputStream(text.getBytes("UTF-8"));
            System.out.println("Base64 String: " + trama);
            String MT103Base64 = _getImage64(trama);
            System.out.println("Base64 : " + MT103Base64);
            deta.setCodigo("0000");
            deta.setTipo("OK");
            deta.setMT103(MT103Base64);
            return deta;

        } catch (Exception ex) {
            System.out.println("Exception : " + ex.getMessage());
            deta.setCodigo("9993");
            deta.setTipo("ER");
            deta.setDetalle("error : " + ex.getMessage());
            return deta;
        }

    }
    //</editor-fold>

////<editor-fold defaultstate="collapsed" desc="public method: Crea archivo MT103 : generaTransferenciaMT103A">
    //    @WebMethod(operationName = "generaTransferenciaMT103A")
    //    public RespuestaTransferenciaMT103 generaTransferenciaMT103A(@WebParam(name = "transactionId") String transactionId,
    //            @WebParam(name = "aplicationId") String aplicationId,
    //            @WebParam(name = "paisId") String paisId,
    //            @WebParam(name = "empresaId") String empresaId,
    //            @WebParam(name = "regionId") String regionId,
    //            @WebParam(name = "canalId") String canalId,
    //            @WebParam(name = "version") String version,
    //            @WebParam(name = "llaveSesion") String llaveSesion,
    //            @WebParam(name = "usuarioId") String usuarioId,
    //            @WebParam(name = "accion") String accion,
    //            @WebParam(name = "token") String token,
    //            @WebParam(name = "clienteCoreId") String clienteCoreId,
    //            @WebParam(name = "TipoIdentificacion") String TipoIdentificacion,
    //            @WebParam(name = "identificacion") String identificacion,
    //            @WebParam(name = "terminal") String terminal,
    //            @WebParam(name = "pantalla") String pantalla,
    //            @WebParam(name = "paso") String paso,
    //            @WebParam(name = "parametroAdicional") parametroAdicionalColeccion parametroAdicional,
    //            @WebParam(name = "TransferenciaColeccion") TransferenciaColeccion TransferenciaColeccion
    //    ) {
    //        RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
    //        InputStream trama = null;
    //        try {
    //            int Estado = 0;
    //            String mensaje = null;
    //            String nombre = null;
    //            String Nombre = null;
    //            String ACCOUNT = null;
    //            String NombreBeneficiario = null;
    //            String NumCliente = null;
    //            String Id = null;
    //            String TipoId = null;
    //            String TipoPersona = null;
    //            String CodPaisOrdenante = null;
    //            String CodPaisBeneficiario = null;
    //            String DesPais = null;
    //            String Direccion = null;
    //            String Telefono = null;
    //            String Correo = null;
    //            String SQL = null;
    //            Statement stmt = null;
    //            ResultSet rs = null;
    //            String respuesta = null;
    //            String ParametroCodigo = null;
    //            Connection con = null;
    //            Connection con1 = null;
    //            String ParametroValorDato = null;
    //            String ParametroValorDatoPorcentaje = null;
    //            String Valor = null;
    //            double totalValor = 0.0;
    ////            RepuestaTransferencia deta = new RepuestaTransferencia();
    ////            DetalleColeccion deta00 = new DetalleColeccion();
    ////            StatusGlobal deta0 = new StatusGlobal();
    ////            StatusItem detalleColeccion = new StatusItem();
    ////            ArrayList<DetalleColeccion> detax = new ArrayList<>();
    ////            ArrayList<StatusItem> deta1 = new ArrayList<>();
    ////            ArrayList<RepuestaTransferencia> response = new ArrayList<>();
    ////            ArrayList<StatusGlobal> deta2 = new ArrayList<>();
    ////            RespuestaNombresListasNegras deta3 = new RespuestaNombresListasNegras();
    //            NumberFormat nf = NumberFormat.getInstance(Locale.US);
    //            DecimalFormat df = (DecimalFormat) nf;
    //            System.out.println("**** PASO1 Entro generaTransferenciaMT103 ***** ");
    //            System.out.println("**** Accion ***** generaTransferenciaMT103" + accion);//
    //            System.out.println("**** identificacion ***** " + identificacion);
    //            String coincidencia = "";
    //            String valorCoincidencia = "";
    //            String Nocoincidencia = "";
    //            String NovalorCoincidencia = "";
    //            //        DBConnectMotor coneccionMotor = new DBConnectMotor();
    ////        con = coneccionMotor.obtenerConeccion();
    //            String NUM_TRANSFERENCIA = null;
    //            String IDENTIDAD_ORDENANTE = null;
    //            String CUENTA_ORDENANTE = null;
    //            String NOMBRE_ORDENANTE = "?";
    //            String DIRECCION_ORDENANTE = "?";
    //            String DIRECCION_ORDENANTE1 = "?";
    //            String DIRECCION_ORDENANTE2 = "?";
    //            String DIRECCION_ORDENANTE3 = "?";
    //            String BIC_BANCO = null;
    //            String TIPO_PARTICIPANTE = "";
    //            String MONEDA = null;
    //            String IDENTIDAD_CLIENTE_BENEFICIARIO = "?";
    //            String MOTIVO = "?";
    //            String CUENTA_BENEFICIARIO = "?";
    //            String NOMBRE_BENEFICIARIO = "?";
    //            String DIRECCION_BENEFICIARIO = "?";
    //            String FECHA = "?";
    //            String CODIGO_RETORNO = null;
    //            String DESCRIPCION_ERROR = null;
    //            String DESCRIPCION_TRANSACCION = null;
    //            String DESCRIPCION_TRANSACCION_SIN = "?";
    //            String NUM_TRANSFERENCIA_MT102 = null;
    //            String TIPO_MENSAGE = null;
    //            String TIPO_TRANSACCION = "?";
    //            String ESTADO = null;
    //            String BIC_BANCO_ORDENANTE = "?";
    //            String REFERENCIA20 = null;
    //            String BIC_BANCO_BENEFICIARIO = null;
    //            String BIC_BANCO_RECIBE = null;
    //            String BIC_BANCO_ENVIA = null;
    //            String CODIGO_OPERACION = null;
    //            String FECHA_VALOR = null;
    //            String MONTO = null;
    //            String LOGICAL_TERMINAL = "?";
    //            String LOGICAL_TERMINAL2 = "?";
    //            int Referencia = 1;
    //            String NUMERO_CUENTA = "?";
    //            String NOMBRE_CLIENTE = "?";
    //            String DIRECCION_CLIENTE = "?";
    //            String TIPO_CUENTA = "?";
    //            String FECHA_NACIMIENTO = "?";
    //            String NUMERO_CUENTA_BENEFICIARIO = "?";
    //            String NOMBRE_CLIENTE_BENEFICIARIO = "?";
    //            String DIRECCION_CLIENTE_BENEFICIARIO = "?";
    //            String TIPO_CUENTA_BENEFICIARIO = "?";
    //            String OUR = "?";
    //            String CODIGO_TTC = "?";
    //            String SIN = null;
    //            String NUMERO_IDENTIFICACION = "?";
    //            String pattern10 = "0000000000";
    //            String pattern7 = "0000000";
    //            long numeroControl = 0;
    //            df.applyPattern(pattern10);
    //            String numeroControlString = df.format(Referencia);
    //            df.applyPattern(pattern7);
    //            String numeroControlString7 = df.format(Referencia);
    //            String path;
    //            String user;
    //            System.out.println("**** PASO5a ***** " + TransferenciaColeccion.getTransferenciaItems().size());
    //            for (int i = 0; i < TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().size(); i++) {
    //                Campo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getCampo());
    //                Dato.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getDato());
    //                TipoCampo.add(TransferenciaColeccion.getTransferenciaItems().get(0).getCampoColeccion().getCampoItem().get(i).getTipoCampo());
    ////            BuscaInfoCuenta busca = new BuscaInfoCuenta();
    //                if (Campo.get(i).equals("TIPO_PARTICIPANTE")) {
    //                    TIPO_PARTICIPANTE = Dato.get(i);
    //                    System.out.println("**** TIPO_PARTICIPANTE ***** " + TIPO_PARTICIPANTE);
    //                }
    ////                if (Campo.get(i).equals("BIC_BANCO_ENVIA") && TipoCampo.get(i).equals("BIC")) {
    ////                    BIC_BANCO_ENVIA = Dato.get(i);
    ////                    System.out.println("**** BIC_BANCO_ENVIA ***** " + BIC_BANCO_ENVIA);
    ////                }
    ////                if (Campo.get(i).equals("BIC_BANCO_RECIBE") && TipoCampo.get(i).equals("BIC")) {
    ////                    BIC_BANCO_RECIBE = Dato.get(i);
    ////                    System.out.println("**** BIC_BANCO_RECIBE ***** " + BIC_BANCO_RECIBE);
    ////                }
    ////=====================================================================
    //                if (Campo.get(i).equals("20")) {
    //                    REFERENCIA20 = Dato.get(i);
    //                    System.out.println("**** REFERENCIA20 ***** " + REFERENCIA20);
    //                }
    //                if (Campo.get(i).equals("23B")) {
    //                    // if (Campo.get(i).equals("23B") && TipoCampo.get(i).equals("CRED")) {
    //                    CODIGO_OPERACION = Dato.get(i);
    //                    System.out.println("**** CODIGO_OPERACION. ***** " + CODIGO_OPERACION);
    //                }
    //                if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("DATE")) {
    //                    FECHA_VALOR = Dato.get(i);
    //                    System.out.println("**** FECHA_VALOR ***** " + FECHA_VALOR);
    //                }
    //
    //                if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("CURRENCY")) {
    //                    MONEDA = Dato.get(i);
    //                    System.out.println("**** CURRENCY ***** " + MONEDA);
    //                }
    //                if (Campo.get(i).equals("32A") && TipoCampo.get(i).equals("AMOUNT")) {
    //                    MONTO = Dato.get(i);
    //                    System.out.println("**** AMOUNT ***** " + MONTO);
    //                }
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ACCOUNT")) {
    //                    NUMERO_CUENTA = Dato.get(i);
    //                    System.out.println("**** ACCOUNT ***** " + NUMERO_CUENTA);
    //                }
    //
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NAME")) {
    //                    NOMBRE_ORDENANTE = Dato.get(i);
    //                    System.out.println("**** NOMBRE_ORDENANTE ***** " + NOMBRE_ORDENANTE);
    //                }
    ////================================================Nuevo octubre 2024==================================================================                
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESS")) {
    //                    DIRECCION_ORDENANTE = Dato.get(i);
    //                    System.out.println("**** DIRECCION_ORDENANTE ***** " + DIRECCION_ORDENANTE);
    //                }
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN1")) {
    //                    DIRECCION_ORDENANTE1 = Dato.get(i);
    //                    DIRECCION_ORDENANTE1 = validaString.sanitizeString(DIRECCION_ORDENANTE1);
    //                    System.out.println("**** DIRECCION_ORDENANTE1 ADDRESSLN1 despues de valida caracteres ***** " + DIRECCION_ORDENANTE1);
    //                }
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN2")) {
    //                    DIRECCION_ORDENANTE2 = Dato.get(i);
    //                    DIRECCION_ORDENANTE2 = validaString.sanitizeString(DIRECCION_ORDENANTE2);
    //                    System.out.println("**** DIRECCION_ORDENANTE2 ADDRESSLN2 despues de valida caracteres ***** " + DIRECCION_ORDENANTE2);
    //
    //                }
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("ADDRESSLN3")) {
    //                    DIRECCION_ORDENANTE3 = Dato.get(i);
    //                    DIRECCION_ORDENANTE3 = validaString.sanitizeString(DIRECCION_ORDENANTE3);
    //                    System.out.println("**** DIRECCION_ORDENANTE3 ADDRESSLN3 despues de valida caracteres ***** " + DIRECCION_ORDENANTE3);
    //                }
    ////================================================Nuevo octubre 2024==================================================================                                
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("TC")) {
    //                    TIPO_CUENTA = Dato.get(i);
    //                    System.out.println("**** TIPO_CUENTA ***** " + TIPO_CUENTA);
    //                }
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("FN")) {
    //                    FECHA_NACIMIENTO = Dato.get(i);
    //                    System.out.println("**** FECHA_NACIMIENTO ***** " + FECHA_NACIMIENTO);
    //                }
    //                if (Campo.get(i).equals("50K") && TipoCampo.get(i).equals("NI")) {
    //                    NUMERO_IDENTIFICACION = Dato.get(i);
    //                    System.out.println("**** NUMERO_IDENTIFICACION ***** " + NUMERO_IDENTIFICACION);
    //                }
    //                if (Campo.get(i).equals("52A") && TipoCampo.get(i).equals("IDENTIFIER")) {
    //                    BIC_BANCO_ORDENANTE = Dato.get(i);
    //                    System.out.println("**** BIC_BANCO_ORDENANTE ***** " + BIC_BANCO_ORDENANTE);
    //                }
    ////                if (Campo.get(i).equals("1") && TipoCampo.get(i).equals("HEADER1")) {
    ////                    LOGICAL_TERMINAL = Dato.get(i);
    ////                    System.out.println("**** LOGICAL_TERMINAL 1 BIC INPUT ***** " + LOGICAL_TERMINAL);
    ////                }
    ////                if (Campo.get(i).equals("2") && TipoCampo.get(i).equals("HEADER2")) {
    ////                    LOGICAL_TERMINAL2 = Dato.get(i);
    ////                    System.out.println("**** LOGICAL_TERMINAL 2 BIC OUTPUT ***** " + LOGICAL_TERMINAL2);
    ////                }
    //                if (Campo.get(i).equals("57A") && TipoCampo.get(i).equals("IDENTIFIER")) {
    //                    BIC_BANCO_BENEFICIARIO = Dato.get(i);
    //                    System.out.println("**** BIC_BANCO_BENEFICIARIO ***** " + BIC_BANCO_BENEFICIARIO);
    //                }
    //                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ACCOUNT")) {
    //                    NUMERO_CUENTA_BENEFICIARIO = Dato.get(i);
    //                    System.out.println("**** NUMERO_CUENTA_BENEFICIARIO  ACCOUNT ***** " + NUMERO_CUENTA_BENEFICIARIO);
    //                }
    //
    //                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NAME")) {
    //                    NOMBRE_CLIENTE_BENEFICIARIO = Dato.get(i);
    //                    System.out.println("**** NOMBRE_CLIENTE_BENEFICIARIO ***** " + NOMBRE_CLIENTE_BENEFICIARIO);
    //                }
    //                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("ADDRESS")) {
    //                    DIRECCION_CLIENTE_BENEFICIARIO = Dato.get(i);
    //                    System.out.println("**** DIRECCION_CLIENTE_BENEFICIARIO ***** " + DIRECCION_CLIENTE_BENEFICIARIO);
    //                }
    //                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("TC")) {
    //                    TIPO_CUENTA_BENEFICIARIO = Dato.get(i);
    //                    System.out.println("**** TIPO_CUENTA_BENEFICIARIO ***** " + TIPO_CUENTA_BENEFICIARIO);
    //                }
    //                if (Campo.get(i).equals("59A") && TipoCampo.get(i).equals("NI")) {
    //                    IDENTIDAD_CLIENTE_BENEFICIARIO = Dato.get(i);
    //                    System.out.println("**** IDENTIDAD_CLIENTE_BENEFICIARIO ***** " + IDENTIDAD_CLIENTE_BENEFICIARIO);
    //                }
    //                if (Campo.get(i).equals("70") && TipoCampo.get(i).equals("MOTIVO")) {
    //                    MOTIVO = Dato.get(i);
    //                    System.out.println("**** OUR ***** " + OUR);
    //                }
    //                if (Campo.get(i).equals("71A") && TipoCampo.get(i).equals("DETAIL")) {
    //                    OUR = Dato.get(i);
    //                    System.out.println("**** OUR ***** " + OUR);
    //                }
    //                if (Campo.get(i).equals("72") && TipoCampo.get(i).equals("TTC")) {
    //                    CODIGO_TTC = Dato.get(i);
    //                    System.out.println("**** CODIGO_TTC ***** " + CODIGO_TTC);
    //                }
    //                if (Campo.get(i).equals("72") && TipoCampo.get(i).equals("SIN")) {
    //                    DESCRIPCION_TRANSACCION_SIN = Dato.get(i);
    //                    System.out.println("**** DESCRIPCION_TRANSACCION_SIN ***** " + DESCRIPCION_TRANSACCION_SIN);
    //                }
    //
    //            }
    ////==============================Connect to dataBase=================================================================
    //            System.out.println("********** ir all metodo de conexion DB coneccionMotor.obtenerConeccion() **********");
    //            DBConnectMotor coneccionMotor = new DBConnectMotor();
    //            con = coneccionMotor.obtenerConeccion();
    //            System.out.println("**** salio del connect1 ***** ");
    //            stmt = con.createStatement();
    //            String sql = "SELECT ParametroValorDato FROM MotorTransferencia.dbo.ParametroValor where ParametroCodigo = 'ENTORNO' and ParametroValorCodigo = 'TOSWIFT'";
    //            rs = stmt.executeQuery(sql);
    //            System.out.println("**** stmt.executeQuery(sql) ***** ");
    //            while (rs.next()) {
    //                ParametroValorDato = rs.getString("ParametroValorDato");
    //                System.out.println("**** ParametroValorDato *****  " + ParametroValorDato);
    //            }
    //            con.close();
    ////===============================================================================================        
    //            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
    //                text = "{1:F01BCEHHNT0AXXX" + numeroControlString + "}{2:I103BCEHHNT0XXXXN}{3:{103:SPH}}{4:\r\n";
    //            } else {
    //                System.out.println("**** pasox1 *****  ");
    //                text = "{1:F01" + BIC_BANCO_ORDENANTE + numeroControlString + "}{2:I103" + BIC_BANCO_BENEFICIARIO + "N}{3:{103:SPH}}{4:\r\n";
    //                //text = "{1:F01" + LOGICAL_TERMINAL + numeroControlString + "}{2:I103" + LOGICAL_TERMINAL2 + "}{3:{103:SPH}}{4:\r\n";
    //            }
    //            System.out.println("**** pasox1 *****  ");
    //            text = text + ":20:" + REFERENCIA20 + "\r\n";
    //            //text = text + ":20:" + BIC_BANCO_ENVIA.substring(0, 4) + numeroControlString7 + "\r\n";
    //            System.out.println("**** pasox2 *****  ");
    //            text = text + ":23B:" + CODIGO_OPERACION + "\r\n";
    //            System.out.println("**** pasox3 *****  ");
    //            text = text + ":32A:" + FECHA_VALOR + MONEDA + MONTO + "\r\n";
    //            text = text + ":33B:" + MONEDA + MONTO + "\r\n";
    //            System.out.println("**** pasox4 *****  ");
    //            text = text + ":50K:/" + NUMERO_CUENTA.toString().trim() + "\r\n";
    //            System.out.println("**** pasox5 *****  ");
    //            if (NOMBRE_ORDENANTE.length() > 30) {
    //                path = NOMBRE_ORDENANTE.substring(0, 30);
    //                user = NOMBRE_ORDENANTE.substring(30, NOMBRE_ORDENANTE.length());
    //                System.out.println("**** pasox1 *****  ");
    //                text = text + "/NC/" + path + "\r\n";
    //                text = text + "//" + user + "\r\n";
    //            } else {
    //                System.out.println("**** pasox6 *****  ");
    //                text = text + "/NC/" + NOMBRE_ORDENANTE + "\r\n";
    //            }
    //            System.out.println("**** pasox7 *****  ");
    //            if (DIRECCION_ORDENANTE.trim().length() > 30) {
    //                path = DIRECCION_ORDENANTE.trim().substring(0, 30);
    //                user = DIRECCION_ORDENANTE.trim().substring(30, DIRECCION_ORDENANTE.trim().length());
    //                text = text + "/DR/" + path + "\r\n";
    //                text = text + "//" + user + "\r\n";
    //                System.out.println("**** pasox8 *****  ");
    //            } else {
    //                text = text + "/DR/" + DIRECCION_ORDENANTE.trim() + "\r\n";
    //            }
    //            System.out.println("**** pasox9 *****  ");
    //            text = text + "/TC/" + TIPO_CUENTA + "/FN/" + FECHA_NACIMIENTO + "\r\n";
    //            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
    //                text = text + ":52A:" + BIC_BANCO_ORDENANTE + "\r\n";
    //            }
    //
    //            if ((TIPO_PARTICIPANTE.equals("I")) || (TIPO_PARTICIPANTE.equals("i"))) {
    //                text = text + ":57A:" + BIC_BANCO_BENEFICIARIO.toString().trim() + "\r\n";
    //            }
    //            text = text + ":59:/" + NUMERO_CUENTA_BENEFICIARIO.toString().trim() + "\r\n";
    //            if (NOMBRE_CLIENTE_BENEFICIARIO.trim().length() > 30) {
    //                path = NOMBRE_CLIENTE_BENEFICIARIO.trim().substring(0, 30);
    //                user = NOMBRE_CLIENTE_BENEFICIARIO.trim().substring(30, NOMBRE_CLIENTE_BENEFICIARIO.trim().length());
    //                text = text + "/NC/" + path + "\r\n";
    //                text = text + "//" + user + "\r\n";
    //            } else {
    //                text = text + "/NC/" + NOMBRE_CLIENTE_BENEFICIARIO.trim() + "\r\n";
    //            }
    //            System.out.println("**** pasox10 *****  ");
    //            if (DIRECCION_CLIENTE_BENEFICIARIO.trim().length() > 30) {
    //                path = DIRECCION_CLIENTE_BENEFICIARIO.trim().substring(0, 30);
    //                user = DIRECCION_CLIENTE_BENEFICIARIO.trim().substring(30, DIRECCION_CLIENTE_BENEFICIARIO.trim().length());
    //                text = text + "/DR/" + path + "\r\n";
    //                text = text + "//" + user + "\r\n";
    //            } else {
    //                text = text + "/DR/" + DIRECCION_CLIENTE_BENEFICIARIO.trim() + "\r\n";
    //            }
    //            System.out.println("**** pasox11 *****  ");
    //            text = text + "/TC/" + TIPO_CUENTA_BENEFICIARIO.toString().trim() + "/NI/" + IDENTIDAD_CLIENTE_BENEFICIARIO.toString().trim() + "\r\n";
    //            if (TIPO_TRANSACCION.equals("RETURN")) {
    //                if (CODIGO_RETORNO.equals("AC01")) {
    //                    text = text + ":70:/COR/AC01\r\n";
    //                    text = text + "/DER/FORMATO DEL NUMERO DE CUENTA\r\n";
    //                    text = text + "// ESPECIFICADO INCORRECTO\r\n";
    //                }
    //                text = text + ":71A:" + OUR + "\r\n";
    //                text = text + ":72:/TTC/ZZZZ/SIN/DEVOLUCION ENTRE\r\n";
    //                text = text + "// CLIENTES\r\n";
    //            } else {
    //                System.out.println("**** pasox12 *****  ");
    //                text = text + ":71A:" + OUR + "\r\n";
    //                int longi = DESCRIPCION_TRANSACCION_SIN.trim().length();
    //                if (DESCRIPCION_TRANSACCION_SIN.trim().length() > 17) {
    //                    String descripcion = DESCRIPCION_TRANSACCION_SIN.trim().substring(0, 17);
    //                    String descripcion2 = DESCRIPCION_TRANSACCION_SIN.trim().substring(17, longi);
    //                    text = text + ":72:/TTC/803/SIN/" + descripcion + "\r\n";
    //                    text = text + "//" + descripcion2.toString().trim() + "\r\n";
    //                } else {
    //                    text = text + ":72:/TTC/803/SIN/" + DESCRIPCION_TRANSACCION_SIN.trim().toString().trim() + "\r\n";
    //                }
    //            }
    ////===============================================================================================
    //            System.out.println("PASO 7");
    //            text = text + "-}";
    //            path = null;
    ////================================================================================================
    //            PrintWriter out = null;
    //            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //            java.util.Date date = new java.util.Date();
    //            Calendar calendar = Calendar.getInstance();
    //            calendar.setTime(date);
    //            System.out.println("año    ");
    //            String año = String.valueOf(calendar.get(Calendar.YEAR));
    //            System.out.println("mesInt    ");
    //            int mesInt = calendar.get(Calendar.MONTH) + 1;
    //            String pattern2 = "00";
    //            df.applyPattern(pattern2);
    //            System.out.println("mes dia    ");
    //            String mes = df.format(mesInt);
    //            System.out.println("mes      ");
    //            int diaInt = calendar.get(Calendar.DAY_OF_MONTH);
    //            String dia = df.format(diaInt);
    //            System.out.println(" dia    ");
    //            String MiliSecods = String.valueOf(calendar.get(Calendar.MILLISECOND));
    //            //========================================================================
    //            LocalDateTime now = LocalDateTime.now();
    //            System.out.println("LocalDateTime      " + now);
    //            // Create a formatter with milliseconds
    //            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss-SSS");
    //
    //            // Format and print the date and time with milliseconds
    //            String formattedDateTime = now.format(formatter);
    //            System.out.println("Current date and time with milliseconds: " + formattedDateTime);
    //
    //            //========================================================================
    ////        System.out.println("Año : " + calendar.get(Calendar.YEAR) + "   Mes : " + mes + "   Dia : " + calendar.get(Calendar.DAY_OF_MONTH));
    //            try {
    //                //out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/" + año + mes + dia + MiliSecods + "MT103.txt", true)));
    //                out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/MT103-" + transactionId + "-" + formattedDateTime + ".txt", true)));
    //                out.println(text);
    //                out.close();
    //
    //            } catch (IOException e) {
    //                System.out.println();
    //                deta.setCodigo("9978");
    //                deta.setTipo("ER");
    //                deta.setDetalle(e.getMessage());
    //                deta.setMensaje(e.getMessage());
    //                deta.setReferencia("0");
    //                return deta;
    //
    //            }
    //
    ////================================================================================================
    //            trama = new ByteArrayInputStream(text.getBytes("UTF-8"));
    //            String MT103Base64 = _getImage64(trama);
    //            deta.setCodigo("0000");
    //            deta.setTipo("OK");
    //            deta.setMT103(MT103Base64);
    //            return deta;
    //
    //        } catch (UnsupportedEncodingException ex) {
    //            Logger.getLogger(wsTransferencias.class
    //                    .getName()).log(Level.SEVERE, null, ex);
    //
    //        } catch (SQLException ex) {
    //            Logger.getLogger(wsTransferencias.class
    //                    .getName()).log(Level.SEVERE, null, ex);
    //        } finally {
    //            try {
    //                trama.close();
    //
    //            } catch (IOException ex) {
    //                Logger.getLogger(wsTransferencias.class
    //                        .getName()).log(Level.SEVERE, null, ex);
    //            }
    //        }
    //
    //        System.out.println();
    //        deta.setCodigo("9977");
    //        deta.setTipo("ER");
    ////        deta.setDetalle(e.getMessage());
    ////        deta.setMensaje(e.getMessage());
    //        deta.setReferencia("0");
    //        return deta;
    //    }
    //    //</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="public method: _getImage64">    
    private String _getImage64(InputStream stream) {

        String sdata = null;

        byte[] cpBuffer = new byte[40000];
        byte[] buffer = null;

        if (stream != null) {

            try {
                int br = stream.read(cpBuffer);
                while (br != -1) {
                    if (buffer == null) {
                        buffer = new byte[br];
                        System.arraycopy(cpBuffer, 0, buffer, 0, br);
                    } else {
                        byte[] tmp = new byte[buffer.length + br];
                        System.arraycopy(buffer, 0, tmp, 0, buffer.length);
                        System.arraycopy(cpBuffer, 0, tmp, buffer.length, br);
                        buffer = tmp;
                        tmp = null;
                    }
                    br = stream.read(cpBuffer);
                }
            } catch (IOException ex) {
//                Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("IOException : " + ex.getMessage());
            }

            if (buffer != null) {
                buffer = com.sun.mail.util.BASE64EncoderStream.encode(buffer);
                sdata = new String(buffer);
            }

        }

        return sdata;

    }

    //</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="public method: update">    
    private String update(Connection con, String transaccion, String estado, String paso, String tipo) {
        System.out.println("Campos transaccion **** " + transaccion);
        System.out.println("Campos estado ********* " + estado);
        System.out.println("Campos paso ********* " + paso);
        System.out.println("Campos tipo ********* " + tipo);
        System.out.println("update SQl  ENTRO ********* ");
        int transaccionInt = 0;
        int pasoInt = 0;
        if (paso == "?"){
            paso ="0";
        }
        System.out.println("Campos paso update ********* " + paso);
        transaccionInt = Integer.parseInt(transaccion);
        pasoInt = Integer.parseInt(paso);
        try {
            if (tipo.equals("OK")) {
                sql = "update Transaccion set TransaccionGestionEstado = '" + estado + "', TransaccionPaso = " + pasoInt + "  where TransaccionSecuencia = " + transaccionInt;
                System.out.println("update SQl  tipo.equals(\"OK\") ********* " + sql);
            } else {
                sql = "update Transaccion set TransaccionGestionEstado = '" + estado + "' where TransaccionSecuencia = " + transaccionInt;
                System.out.println("update SQl  tipo.equals(\"NO OK\") ********* " + sql);
            }
            System.out.println("update Antes  stmt = con.createStatement() ********* ");
            Statement stmt = con.createStatement();
            System.out.println("update Despues y antes del executestmt = con.createStatement() ********* ");
            int count = stmt.executeUpdate(sql);
            System.out.println("SQl  y count del update : count : " + count + " - " + sql);
            System.out.println("PASO 1 ******************");
            if (count > 0) {
                System.out.println("PASO 3 ******************");
                return "OK";

            }
            System.out.println("PASO 3a ******************");
        } catch (SQLException ex) {
            System.out.println(" Error SQLException " + ex.getMessage());
            return "ER|" + ex.getMessage();
        }
        return "NOOK";
    }
    //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="public method: creaMT103">    
    private String creaMT103(String ParametroValorDato, String transactionId) {
        try {
            RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();

//================================================================================================
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            DecimalFormat df = (DecimalFormat) nf;
            PrintWriter out = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            java.util.Date date = new java.util.Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            System.out.println("año    ");
            String año = String.valueOf(calendar.get(Calendar.YEAR));
            System.out.println("mesInt    ");
            int mesInt = calendar.get(Calendar.MONTH) + 1;
            String pattern2 = "00";
            df.applyPattern(pattern2);
            System.out.println("mes dia    ");
            String mes = df.format(mesInt);
            System.out.println("mes      ");
            int diaInt = calendar.get(Calendar.DAY_OF_MONTH);
            String dia = df.format(diaInt);
            System.out.println(" dia    ");
            String MiliSecods = String.valueOf(calendar.get(Calendar.MILLISECOND));
//========================================================================
            LocalDateTime now = LocalDateTime.now();
            System.out.println("LocalDateTime      " + now);
// Create a formatter with milliseconds
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss-SSS");

// Format and print the date and time with milliseconds
            String formattedDateTime = now.format(formatter);
            System.out.println("Current date and time with milliseconds: " + formattedDateTime);

//out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/" + año + mes + dia + MiliSecods + "MT103.txt", true)));
            out = new PrintWriter(new BufferedWriter(new FileWriter(ParametroValorDato + "/MT103-" + transactionId + "-" + formattedDateTime + ".txt", true)));
            out.println(text);
            out.close();

        } catch (IOException ex) {
            Logger.getLogger(wsTransferencias.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "OK";
    }
    //</editor-fold>  

//<editor-fold defaultstate="collapsed" desc="public method: insertaEvento">    
    private RespuestaEvento insertaEvento(RequestEvento req) {
        
        RespuestaEvento resp = new RespuestaEvento();
        try {
            System.out.println("Entro al metodo de Insertar Evento  ");
            URL newEndpoint = new URL("http://Motor/MotorTransferencia/wsinserttransactionevent.aspx?wsdl");//Producción
            QName qname = new QName("MotorTransferencia", "WsInsertTransactionEvent");
            motortransferencia.WsInsertTransactionEventExecute parameters = new motortransferencia.WsInsertTransactionEventExecute();
            SDTTransaccionEvento deta = new SDTTransaccionEvento();
            deta.setTransaccionSecuencia(req.getTransaccionSecuencia());
            deta.setTransaccionEventoLinea(req.getTransaccionEventoLinea());
            deta.setTransaccionEventoSistema(req.getTransaccionEventoSistema());
            deta.setTransaccionEventoAccion(req.getTransaccionEventoAccion());
            deta.setTipoEventoCodigo(req.getTipoEventoCodigo());
            deta.setTransaccionEventoInicio(req.getTransaccionEventoInicio());
            deta.setTransaccionUsuarioCodigo(req.getTransaccionUsuarioCodigo());
            deta.setTransaccionEventoDispositivo(req.getTransaccionEventoDispositivo());
            deta.setTransaccionEventoIP(req.getTransaccionEventoIP());
            deta.setTransaccionEventoSession(req.getTransaccionEventoSession());
            deta.setTransaccionEventoComentario(req.getTransaccionEventoComentario());
            deta.setTransaccionEventoFinal(req.getTransaccionEventoFinal());
            deta.setTransaccionEventoGestion(req.getTransaccionEventoGestion());
            deta.setTransaccionEventoEstado(req.getTransaccionEventoEstado());
            parameters.setEvento(deta);
            motortransferencia.WsInsertTransactionEvent service = new motortransferencia.WsInsertTransactionEvent(newEndpoint, qname);
            motortransferencia.WsInsertTransactionEventSoapPort port = service.getWsInsertTransactionEventSoapPort();
            System.out.println("Antes de port.execute(parameters) ");
            motortransferencia.WsInsertTransactionEventExecuteResponse result = port.execute(parameters);
            System.out.println("Depues de port.execute(parameters) ");
            resp.setCode(result.getRespuesta().getCode());
            System.out.println("Codigo retorno ");
            resp.setDetail(result.getRespuesta().getDetail());
            System.out.println("Codigo detail ");
            resp.setMessage(result.getRespuesta().getMessage());
            System.out.println("Codigo respuesta ");
            resp.setType(result.getRespuesta().getType());
            System.out.println("Evento getCode = " + result.getRespuesta().getCode());
            System.out.println("Evento getDetail = " + result.getRespuesta().getDetail());
            System.out.println("Evento getMessage = " + result.getRespuesta().getMessage());
            System.out.println("Evento getType = " + result.getRespuesta().getType());

        } catch (Exception ex) {
            System.out.println("Exception de Insertar Evento  "+ex.getMessage());
//            Logger.getLogger(wsTransferencias.class
//                    .getName()).log(Level.SEVERE, null, ex);
            resp.setCode("9999");
            resp.setDetail("Exception");
            resp.setMessage(ex.getMessage());
            resp.setType("Exception encontrada");
            return resp;
        }
        return resp;

    }
    //</editor-fold>  

//<editor-fold defaultstate="collapsed" desc="public method: update">    
    private RespuestaTransferenciaMT103 validaTags(ValidarTags valida, String dato) {
        System.out.println("**** entro al error *****  ");
        RespuestaTransferenciaMT103 deta = new RespuestaTransferenciaMT103();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new java.util.Date());
        deta.setCodigo("9990");
        deta.setTipo("ER");
        deta.setDetalle("error validacion : " + dato);
        deta.setMensaje("error validacion : " + dato);
        System.out.println("**** va a salir del  error *****  " + dato);
        System.out.println("*** ejecuta Update 11 *****");
        String resp = update(valida.getConexion(), valida.getTransactionId(), ERROR, "0", "NO");
// ======================== Graba Eventocuando ws de contabilizacion es ERROR NOOK. Inicia. ===========================                    
        req.setTransaccionSecuencia(Long.valueOf(valida.getTransactionId()));
        req.setTransaccionEventoSistema(HoraInicioAplicativo);
        System.out.println("********** HoraInicioAplicativo ********** : " + HoraInicioAplicativo);
        req.setTransaccionEventoAccion("ERROR validaTags");
        req.setTipoEventoCodigo("INSERT");
        req.setTransaccionEventoInicio(HoraInicioEvento);
        req.setTransaccionUsuarioCodigo(valida.getUsuarioId());
        req.setTransaccionEventoDispositivo(valida.getPantalla());
        req.setTransaccionEventoIP(valida.getTerminal());
        req.setTransaccionEventoSession(valida.getLlaveSesion());
//                req.setTransaccionEventoComentario("Consulta cliente en el CORE - respuesta No exitosa");
        req.setTransaccionEventoComentario("error validacion : " + dato);
        cal.setTime(new java.util.Date());
        try {
            HoraFinalizaEvento = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException ex) {
//            Logger.getLogger(wsTransferencias.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("********** DatatypeConfigurationException ********** : " + ex);
        }
        req.setTransaccionEventoFinal(HoraFinalizaEvento);
        req.setTransaccionEventoGestion("TIPESTPRO");
        req.setTransaccionEventoEstado("TIPESTACT");
        System.out.println("Evento validaTags ** MAL NOOK **");
        respEvento = insertaEvento(req);
        return deta;
    }
    //</editor-fold>    

}
