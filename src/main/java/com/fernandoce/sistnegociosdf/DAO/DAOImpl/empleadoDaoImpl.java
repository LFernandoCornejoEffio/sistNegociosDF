/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO.DAOImpl;

import com.fernandoce.sistnegociosdf.DAO.empleadoDao;
import com.fernandoce.sistnegociosdf.entidades.eEmpleado;
import com.fernandoce.sistnegociosdf.extras.encriptacionRSA;
import com.fernandoce.sistnegociosdf.extras.exportarExcel;
import com.fernandoce.sistnegociosdf.extras.fechaActual;
import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author lfern
 */
public class empleadoDaoImpl implements empleadoDao {

    Connection conn = null;
    CallableStatement cst = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    List<eEmpleado> listaEmpleados = null;
    eEmpleado empleado;
    encriptacionRSA encryptRsa;
    static String rutasalidaPdf;
    static String rutaSalidaExcel;

    @Override
    public eEmpleado login(String username, String password) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        empleado = new eEmpleado();
        String unsecure;
        try {
            String sql = "SELECT e.idEmpleado, p.nombre, p.apPaterno, p.apMaterno, e.cargo, e.primerAcceso, e.contrasenia FROM empleado e INNER JOIN persona p ON p.idPersona = e.idEmpleado WHERE e.username LIKE ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("e.contrasenia");
                unsecure = encryptRsa.decrypt(pass);
                if (unsecure.equals(password)) {
                    empleado.setIdPersona(rs.getInt("idEmpleado"));
                    empleado.setNombre(rs.getString("nombre"));
                    empleado.setApPaterno(rs.getString("apPaterno"));
                    empleado.setApMaterno(rs.getString("apMaterno"));
                    empleado.setCargo(rs.getString("cargo"));
                    empleado.setPrimerAcceso(rs.getString("primerAcceso"));
                } else {
                    JOptionPane.showMessageDialog(null, "La contraseña ingresada es incorrecta o no se encuentra registrada", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                return empleado;
            } else {
                JOptionPane.showMessageDialog(null, "El username es incorrecto o no se encuentra registrado", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                return null;
            }

        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error " + e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    @Override
    public List<eEmpleado> listar(String campo, String busqueda) {
        conn = Conexion.getConectar();
        listaEmpleados = new ArrayList();
        try {
            String sql = "{CALL buscarUsuarios (?, ?)}";
            cst = conn.prepareCall(sql);
            cst.setString(1, campo);
            cst.setString(2, busqueda);
            rs = cst.executeQuery();
            while (rs.next()) {
                empleado = new eEmpleado();
                empleado.setIdPersona(rs.getInt("idEmpleado"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApPaterno(rs.getString("apPaterno"));
                empleado.setApMaterno(rs.getString("apMaterno"));
                empleado.setAbrevTipoDoc(rs.getString("abrevTipoDoc"));
                empleado.setNumDoc(rs.getString("numDoc"));
                empleado.setCargo(rs.getString("cargo"));
                empleado.setTelefono(rs.getString("telefono"));
                empleado.setDireccion(rs.getString("direccion"));
                empleado.setUltimo_Acceso(rs.getString("ultimo_acceso"));
                empleado.setUsername(rs.getString("username"));
                listaEmpleados.add(empleado);
            }
            return listaEmpleados;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al Listar. \nError: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cst != null) {
                    cst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    @Override
    public List<eEmpleado> listar() {
        return null;
    }

    @Override
    public boolean insertar(eEmpleado objeto) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        boolean rpta = false;
        int rpt = 0;
        String username;
        try {
            String sql = "{CALL insertarEmpleado (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cst = conn.prepareCall(sql);
            cst.setString(1, objeto.getNombre());
            cst.setString(2, objeto.getApPaterno());
            cst.setString(3, objeto.getApMaterno());
            cst.setInt(4, objeto.getTipoDoc());
            cst.setString(5, objeto.getNumDoc());
            cst.setString(6, objeto.getTelefono());
            cst.setString(7, objeto.getDireccion());
            cst.setString(8, objeto.getCargo());
            String nameAll = objeto.getNombre() + " " + objeto.getApPaterno() + " " + objeto.getApMaterno();
            username = generarUser(nameAll.toUpperCase());
            cst.setString(9, username);
            String secure = encryptRsa.encrypt(getPassDefault());
            cst.setString(10, secure);
            rs = cst.executeQuery();
            while (rs.next()) {
                rpt = rs.getInt("_idEmpleado");
                System.out.println("IdEmpleado: " + rpt);
            }
            if (rpt == 0) {
                rpta = false;
                JOptionPane.showMessageDialog(null, "El usuario ya se encuentra registrado");
            } else if (rpt > 0) {
                rpta = true;
                JOptionPane.showMessageDialog(null, "El usuario se registro con exito\n\nEl usuario se registro con las credenciales:\nUsername: " + username + "\nContraseña: " + getPassDefault());
            } else {
                rpta = false;
                JOptionPane.showMessageDialog(null, "ocurrio un error, no se logro registrar " + rpta);
            }
            return rpta;
        } catch (HeadlessException | SQLException e) {
            return rpta;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cst != null) {
                    cst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    @Override
    public boolean editar(eEmpleado objeto) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        try {
            String sql = "{CALL editarEmpleado (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cst = conn.prepareCall(sql);
            cst.setInt(1, objeto.getIdPersona());
            cst.setString(2, objeto.getNombre());
            cst.setString(3, objeto.getApPaterno());
            cst.setString(4, objeto.getApMaterno());
            cst.setInt(5, objeto.getTipoDoc());
            cst.setString(6, objeto.getNumDoc());
            cst.setString(7, objeto.getTelefono());
            cst.setString(8, objeto.getDireccion());
            cst.setString(9, objeto.getCargo());
            cst.execute();
            rpta = true;
            return rpta;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al editar " + e);
            return rpta;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cst != null) {
                    cst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    @Override
    public boolean eliminar(int idObjeto) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        try {
            String sql = "DELETE FROM persona WHERE idPersona = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idObjeto);
            int N = pst.executeUpdate();
            if (N != 0) {
                rpta = true;
                return rpta;
            } else {
                return rpta;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el usuario " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexiones " + e);
            }
        }
    }

    @Override
    public eEmpleado obtenerObjetoPorId(int idObjeto) {
        conn = Conexion.getConectar();
        empleado = new eEmpleado();

        System.out.println("idObjeto: " + idObjeto);
        try {
            String sql = "SELECT * FROM vista_empleados WHERE idEmpleado = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idObjeto);
            rs = pst.executeQuery();
            while (rs.next()) {
                empleado.setIdPersona(rs.getInt("idEmpleado"));
                empleado.setNombre(rs.getString("nombre"));
                empleado.setApPaterno(rs.getString("apPaterno"));
                empleado.setApMaterno(rs.getString("apMaterno"));
                empleado.setTipoDoc(rs.getInt("tipoDocId"));
                empleado.setAbrevTipoDoc(rs.getString("abrevTipoDoc"));
                empleado.setNumDoc(rs.getString("numDoc"));
                empleado.setCargo(rs.getString("cargo"));
                empleado.setTelefono(rs.getString("telefono"));
                empleado.setDireccion(rs.getString("direccion"));
                return empleado;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
        return null;
    }

    public String generarUser(String nameCom) {
        String username = "";
        System.out.println(nameCom);
        String[] names = nameCom.split(" ");
        for (String i : names) {
            username += i.substring(0, 1);
        }
        int i = usernameExiste(username);
        String uL;
        int j = 1;
        if (i > 0 || username.length() < 4) {
            uL = nameCom.substring(nameCom.length() - 1, nameCom.length());
            if (isNumeric(uL)) {
                j = Integer.parseInt(uL) + 1;
                int indice = nameCom.length();
                nameCom = nameCom.substring(0, indice - 1) + j;
            } else {
                nameCom = nameCom + " " + j;
            }
            return generarUser(nameCom);
        } else {
            return username;
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int usernameExiste(String username) {
        conn = Conexion.getConectar();
        empleado = new eEmpleado();
        int rpta = 0;
        String sql = "SELECT COUNT(1) AS tUser FROM empleado WHERE username LIKE ?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            while (rs.next()) {
                rpta = rs.getInt("tUser");
            }
            return rpta;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return rpta;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    private String getPassDefault() {
        String pass = "negociosDF";
        pass += getYear();
        System.out.println(pass);
        return pass;
    }

    private String getYear() {
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 4);
        System.out.println("Year: " + year);
        return year;
    }

    public boolean ultimoAcceso(int idEmpleado) {
        conn = Conexion.getConectar();
        try {
            empleado = new eEmpleado();
            String sql = "{CALL ultimoAcceso (?)}";
            cst = conn.prepareCall(sql);
            cst.setInt(1, idEmpleado);
            rs = cst.executeQuery();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cst != null) {
                    cst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    public boolean resetContrasenia(int idEmpleado) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        String passReset;
        try {
            passReset = encryptRsa.encrypt(getPassDefault());
            String sql = "UPDATE empleado SET contrasenia = ?, primerAcceso = 'SI' WHERE idEmpleado = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, passReset);
            pst.setInt(2, idEmpleado);
            int N = pst.executeUpdate();
            if (N != 0) {
                JOptionPane.showMessageDialog(null, "Se reseteo la contraseña con exito.");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al resetear.");
                return false;
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al resetear la contraseña " + e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    public boolean cambiarContrasenia(int idEmpleado, String pass) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        try {
            String sql = "UPDATE empleado SET contrasenia = ?, primerAcceso = 'NO' WHERE idEmpleado = ?";

            String passOld = getPass(idEmpleado);
            if (pass.toLowerCase().contains("negociosdf") == true) {
                JOptionPane.showMessageDialog(null, "Estimado usuario, la contraseña no puede contener \"NEGOCIOSDF\".\nPor favor intenta con una contraseña diferente.");
                return false;
            } else if (pass.equals(passOld)) {
                JOptionPane.showMessageDialog(null, "Estimado usuario, la nueva contraseña no puede ser igual a la contraseña anterior.");
                return false;
            } else {
                pass = encryptRsa.encrypt(pass);

                pst = conn.prepareStatement(sql);
                pst.setString(1, pass);
                pst.setInt(2, idEmpleado);
                int N = pst.executeUpdate();
                if (N != 0) {
                    JOptionPane.showMessageDialog(null, "Se actualizo la contraseña con exito.");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al cambiar la contraseña.\n" + e);
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e);
            }
        }
    }

    public String getPass(int idEmpleado) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        String pass = null;
        try {
            String sql = "SELECT contrasenia FROM empleado WHERE idEmpleado = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idEmpleado);
            rs = pst.executeQuery();
            while (rs.next()) {
                pass = encryptRsa.decrypt(rs.getString("contrasenia"));
                return pass;

            }
            return pass;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
            return null;
        }
    }

    public void reporteUsuariosExcel(JTable tabla) {
        Properties prop = new Properties();
        String fileProperties = "application.properties";
        InputStream setting = Conexion.class.getClassLoader().getResourceAsStream(fileProperties);
        exportarExcel excel = new exportarExcel();
        try {
            prop.load(setting);
            rutaSalidaExcel = prop.get("excelUsuarios").toString();
            boolean reportExcel = excel.excelReporte("usuarios", tabla, rutaSalidaExcel);
            if (reportExcel == true) {
                JOptionPane.showMessageDialog(null, "Se creo el archivo excel.");
            }
            setting.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException. \n" + ex);
        }
    }

    public JasperPrint reporteUsuariosSinFiltro() {
        conn = Conexion.getConectar();
        String url = "src/main/resources/reportes/usuarios/";
        Properties prop = new Properties();
        String fileProperties = "application.properties";
        InputStream setting = Conexion.class.getClassLoader().getResourceAsStream(fileProperties);
        try {
            prop.load(setting);
            rutasalidaPdf = prop.get("pdfUsuarios").toString();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException. \n" + ex);
        }
        File reporte = new File(url + "reporteUsuarios.jasper");
        fechaActual fecha = new fechaActual();
        final String nameFile = "reporte_" + fecha.getFechaActual() + ".pdf";

        if (!reporte.exists()) {
            JOptionPane.showMessageDialog(null, "El reporte no se encuentra disponible");
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(reporte.getAbsoluteFile()));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(inputStream);
            JasperPrint print = JasperFillManager.fillReport(jasper, null, conn);
            String fileSalida = rutasalidaPdf + nameFile;
            try (FileOutputStream outputStream = new FileOutputStream(new File(fileSalida))) {
                JasperExportManager.exportReportToPdfStream(print, outputStream);
                JOptionPane.showMessageDialog(null, "Se genero el reporte correctamente.");
                outputStream.close();
            }
            setting.close();
            inputStream.close();
            return print;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error FileNotFoundException.\n" + ex);
            return null;
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Error JRException.\n" + ex);
            System.out.println("Error JRE: " + ex);
            return null;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException.\n" + ex);
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión.\n" + ex);
                return null;
            }
        }
    }

    public JasperPrint reporteUsuariosCargo(String cargo) {
        conn = Conexion.getConectar();
        String url = "src/main/resources/reportes/usuarios/";
        File reporte = new File(url + "reporteUsuariosCargo.jasper");
        fechaActual fecha = new fechaActual();
        final String nameFile = "reporte_" + fecha.getFechaActual() + ".pdf";
        Map parametro = new HashMap();
        parametro.put("filtroCargo", cargo);
        Properties prop = new Properties();
        String fileProperties = "application.properties";
        InputStream setting = Conexion.class.getClassLoader().getResourceAsStream(fileProperties);
        try {
            prop.load(setting);
            rutasalidaPdf = prop.get("pdfUsuarios").toString();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException. \n" + ex);
        }
        if (!reporte.exists()) {
            JOptionPane.showMessageDialog(null, "El reporte no se encuentra disponible");
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(reporte.getAbsoluteFile()));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(inputStream);

            JasperPrint print = JasperFillManager.fillReport(jasper, parametro, conn);

            String fileSalida = rutasalidaPdf + nameFile;

            try (FileOutputStream outputStream = new FileOutputStream(new File(fileSalida))) {
                JasperExportManager.exportReportToPdfStream(print, outputStream);
                JOptionPane.showMessageDialog(null, "Se genero el reporte correctamente.");
                outputStream.close();
            }
            setting.close();
            inputStream.close();
            return print;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error FileNotFoundException.\n" + ex);
            return null;
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Error JRException.\n" + ex);
            System.out.println("Error JRE: " + ex);
            return null;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException.\n" + ex);
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión.\n" + ex);
                return null;
            }
        }
    }
    
    public JasperPrint reporteUsuariosFecha(String fechaInicio, String fechaFin) {
        conn = Conexion.getConectar();
        String url = "src/main/resources/reportes/usuarios/";
        File reporte = new File(url + "reporteUsuariosFecha.jasper");
        fechaActual fecha = new fechaActual();
        final String nameFile = "reporte_" + fecha.getFechaActual() + ".pdf";
        Map parametro = new HashMap();
        parametro.put("fechaInicio", fechaInicio);
        parametro.put("fechaFin", fechaFin);
        Properties prop = new Properties();
        String fileProperties = "application.properties";
        InputStream setting = Conexion.class.getClassLoader().getResourceAsStream(fileProperties);
        try {
            prop.load(setting);
            rutasalidaPdf = prop.get("pdfUsuarios").toString();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException. \n" + ex);
        }
        if (!reporte.exists()) {
            JOptionPane.showMessageDialog(null, "El reporte no se encuentra disponible");
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(reporte.getAbsoluteFile()));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(inputStream);

            JasperPrint print = JasperFillManager.fillReport(jasper, parametro, conn);

            String fileSalida = rutasalidaPdf + nameFile;

            try (FileOutputStream outputStream = new FileOutputStream(new File(fileSalida))) {
                JasperExportManager.exportReportToPdfStream(print, outputStream);
                JOptionPane.showMessageDialog(null, "Se genero el reporte correctamente.");
                outputStream.close();
            }
            setting.close();
            inputStream.close();
            return print;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error FileNotFoundException.\n" + ex);
            return null;
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Error JRException.\n" + ex);
            System.out.println("Error JRE: " + ex);
            return null;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException.\n" + ex);
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión.\n" + ex);
                return null;
            }
        }
    }
    
    public JasperPrint reporteUsuariosCargoFecha(String cargo, String fechaInicio, String fechaFin) {
        conn = Conexion.getConectar();
        String url = "src/main/resources/reportes/usuarios/";
        File reporte = new File(url + "reporteUsuariosCargoFecha.jasper");
        fechaActual fecha = new fechaActual();
        final String nameFile = "reporte_" + fecha.getFechaActual() + ".pdf";
        Map parametro = new HashMap();
        parametro.put("cargo", cargo);
        parametro.put("fechaInicio", fechaInicio);
        parametro.put("fechaFin", fechaFin);
        Properties prop = new Properties();
        String fileProperties = "application.properties";
        InputStream setting = Conexion.class.getClassLoader().getResourceAsStream(fileProperties);
        try {
            prop.load(setting);
            rutasalidaPdf = prop.get("pdfUsuarios").toString();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException. \n" + ex);
        }
        if (!reporte.exists()) {
            JOptionPane.showMessageDialog(null, "El reporte no se encuentra disponible");
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(reporte.getAbsoluteFile()));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(inputStream);

            JasperPrint print = JasperFillManager.fillReport(jasper, parametro, conn);

            String fileSalida = rutasalidaPdf + nameFile;

            try (FileOutputStream outputStream = new FileOutputStream(new File(fileSalida))) {
                JasperExportManager.exportReportToPdfStream(print, outputStream);
                JOptionPane.showMessageDialog(null, "Se genero el reporte correctamente.");
                outputStream.close();
            }
            setting.close();
            inputStream.close();
            return print;
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error FileNotFoundException.\n" + ex);
            return null;
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Error JRException.\n" + ex);
            System.out.println("Error JRE: " + ex);
            return null;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error IOException.\n" + ex);
            return null;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexión.\n" + ex);
                return null;
            }
        }
    }
}
