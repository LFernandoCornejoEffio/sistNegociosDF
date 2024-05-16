/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO.DAOImpl;

import com.fernandoce.sistnegociosdf.DAO.usuariosDao;
import com.fernandoce.sistnegociosdf.entidades.eUsuarios;
import com.fernandoce.sistnegociosdf.extras.encriptacionRSA;
import com.fernandoce.sistnegociosdf.extras.exportarExcel;
import com.fernandoce.sistnegociosdf.extras.fechaActual;
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
public class usuariosDaoImpl implements usuariosDao {

    Connection conn = null;
    CallableStatement cst = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    List<eUsuarios> listaUsuarios = null;
    eUsuarios usuarios;
    encriptacionRSA encryptRsa;
    static String rutasalidaPdf;
    static String rutaSalidaExcel;

    @Override
    public eUsuarios login(String username, String password) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        usuarios = new eUsuarios();
        String unsecure;
        try {
            String sql = "SELECT idPersona, nombre, apPaterno, apMaterno, cargo, primerAcceso, contrasenia FROM vista_usuarios WHERE username LIKE ? AND estado LIKE 'Activo'";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            if (rs.next()) {
                String pass = rs.getString("contrasenia");
                unsecure = encryptRsa.decrypt(pass);
                if (unsecure.equals(password)) {
                    usuarios.setIdPersona(rs.getInt("idPersona"));
                    usuarios.setNombre(rs.getString("nombre"));
                    usuarios.setApPaterno(rs.getString("apPaterno"));
                    usuarios.setApMaterno(rs.getString("apMaterno"));
                    usuarios.setCargo(rs.getString("cargo"));
                    usuarios.setPrimerAcceso(rs.getString("primerAcceso"));
                } else {
                    JOptionPane.showMessageDialog(null, "La contraseña ingresada es incorrecta.", "Login - Acceso Denegado", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "El username no se encuentra registrado.", "Login - Acceso Denegado", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al validar las credenciales.\n" + e.getMessage(), "Error Login", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e.getMessage(), "Error Login", JOptionPane.ERROR_MESSAGE);
            }
        }
        return usuarios;
    }

    @Override
    public List<eUsuarios> listar(String campo, String busqueda) {
        conn = Conexion.getConectar();
        listaUsuarios = new ArrayList();
        try {
            String sql = "{CALL buscarUsuarios (?, ?)}";
            cst = conn.prepareCall(sql);
            cst.setString(1, campo);
            cst.setString(2, busqueda);
            rs = cst.executeQuery();
            while (rs.next()) {
                usuarios = new eUsuarios();
                usuarios.setIdPersona(rs.getInt("idPersona"));
                usuarios.setNombre(rs.getString("nombre"));
                usuarios.setApPaterno(rs.getString("apPaterno"));
                usuarios.setApMaterno(rs.getString("apMaterno"));
                usuarios.setAbrevTipoDoc(rs.getString("abrevTipoDoc"));
                usuarios.setNumDoc(rs.getString("numDoc"));
                usuarios.setCargo(rs.getString("cargo"));
                usuarios.setTelefono(rs.getString("telefono"));
                usuarios.setDireccion(rs.getString("direccion"));
                usuarios.setUsername(rs.getString("username"));
                usuarios.setUltimo_Acceso(rs.getString("ultimo_acceso"));
                usuarios.setPrimerAcceso(rs.getString("primerAcceso"));
                listaUsuarios.add(usuarios);
            }
            return listaUsuarios;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al listar usuarios. \nError: " + e.getMessage(), "Error - Metodo Listar", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e.getMessage(), "Error - Metodo Listar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public boolean insertar(eUsuarios objeto) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        boolean rpta = false;
        boolean existeDoc;
        int idUsuario = 0;
        String username;
        try {
            existeDoc = existeDoc(objeto.getNumDoc());
            if (existeDoc == false) {
                String sql = "{CALL insertarUsuario (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
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
                    idUsuario = rs.getInt("_idUsuario");
                }
                rpta = idUsuario > 0;
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el usuario.\nYa se encuentra registrado un usuario con el numero de documento " + objeto.getNumDoc() + ".", "Error al registrar el usuario", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario.\nError: " + e, "Error al registrar el usuario", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cst != null) {
                    cst.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e, "Error al registrar el usuario", JOptionPane.ERROR_MESSAGE);
            }
        }
        return rpta;
    }

    @Override
    public boolean editar(eUsuarios objeto) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        boolean existeDoc = false;
        try {
            boolean difDoc = diferenteDoc(objeto.getIdPersona(), objeto.getNumDoc());
            if (difDoc == true) {
                existeDoc = existeDoc(objeto.getNumDoc());
            }
            if (existeDoc == false) {
                String sql = "{CALL editarUsuario (?, ?, ?, ?, ?, ?, ?, ?, ?)}";
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
            } else {
                JOptionPane.showMessageDialog(null, "Error al editar el usuario.\nEl nuevo numero de documento ya se encuentra registrado para un usuario diferente.", "Error al editar el usuario", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al editar " + e);
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
        return rpta;
    }

    @Override
    public boolean eliminar(int idObjeto) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        try {
            String sql = "UPDATE persona AS p SET p.estado = ? WHERE p.idPersona = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, "Inactivo");
            pst.setInt(2, idObjeto);
            int N = pst.executeUpdate();
            if (N != 0) {
                rpta = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario " + e);
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
        return rpta;
    }

    @Override
    public eUsuarios obtenerObjetoPorId(int idObjeto) {
        conn = Conexion.getConectar();
        usuarios = new eUsuarios();
        try {
            String sql = "SELECT * FROM vista_usuarios WHERE idPersona = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idObjeto);
            rs = pst.executeQuery();
            while (rs.next()) {
                usuarios.setIdPersona(rs.getInt("idPersona"));
                usuarios.setNombre(rs.getString("nombre"));
                usuarios.setApPaterno(rs.getString("apPaterno"));
                usuarios.setApMaterno(rs.getString("apMaterno"));
                usuarios.setTipoDoc(rs.getInt("tipoDocId"));
                usuarios.setAbrevTipoDoc(rs.getString("abrevTipoDoc"));
                usuarios.setNumDoc(rs.getString("numDoc"));
                usuarios.setCargo(rs.getString("cargo"));
                usuarios.setTelefono(rs.getString("telefono"));
                usuarios.setDireccion(rs.getString("direccion"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener registro por id.\n" + e.getMessage(), "Error Obtener Registro por Id", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e, "Error Obtener Registro por Id", JOptionPane.ERROR_MESSAGE);
            }
        }

        return usuarios;
    }

    @Override
    public boolean existeDoc(String doc) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        int t = 0;
        try {
            String sql = "SELECT COUNT(numDoc) AS tNumDoc FROM vista_usuarios WHERE numDoc LIKE ? AND estado LIKE 'Activo'";
            pst = conn.prepareStatement(sql);
            pst.setString(1, doc);
            rs = pst.executeQuery();
            while (rs.next()) {
                t = rs.getInt("tNumDoc");
            }
            rpta = t > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar si existe el numero de documento.\n" + e.getMessage(), "Error Numero Documento", JOptionPane.ERROR_MESSAGE);
        }
        return rpta;
    }

    @Override
    public String generarUser(String nameCom) {
        String username = "";
        System.out.println(nameCom);
        String[] names = nameCom.split(" ");
        for (String i : names) {
            username += i.substring(0, 1);
        }
        boolean i = usernameExiste(username);
        String uL;
        int j = 1;
        if (i == true || username.length() < 4) {
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

    @Override
    public boolean usernameExiste(String username) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        int t = 0;
        try {
            String sql = "SELECT COUNT(username) AS tUsername FROM vista_usuarios WHERE username LIKE ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            rs = pst.executeQuery();
            while (rs.next()) {
                t = rs.getInt("tUsername");
            }
            rpta = t > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar si existe el username.\n" + e.getMessage(), "Error Username", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e.getMessage(), "Error Username", JOptionPane.ERROR_MESSAGE);
            }
        }
        return rpta;
    }

    @Override
    public boolean ultimoAcceso(int idEmpleado) {
        conn = Conexion.getConectar();
        try {
            usuarios = new eUsuarios();
            String sql = "{CALL ultimoAcceso (?)}";
            cst = conn.prepareCall(sql);
            cst.setInt(1, idEmpleado);
            rs = cst.executeQuery();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el ultimo acceso: " + e, "Error Ultimo Acceso", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e, "Error Ultimo Acceso", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public boolean resetContrasenia(int idEmpleado) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        String passReset;
        boolean rpta = false;
        try {
            passReset = encryptRsa.encrypt(getPassDefault());
            String sql = "UPDATE usuarios SET contrasenia = ?, primerAcceso = 'SI' WHERE idUsuario = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, passReset);
            pst.setInt(2, idEmpleado);
            int N = pst.executeUpdate();
            if (N != 0) {
                rpta = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al resetear la contraseña " + e);
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
        return rpta;
    }

    @Override
    public boolean cambiarContrasenia(int idEmpleado, String pass) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        boolean rpta = false;
        try {
            String sql = "UPDATE usuarios SET contrasenia = ?, primerAcceso = 'NO' WHERE idUsuario = ?";
            String passOld = getPass(idEmpleado);
            if (pass.toLowerCase().contains("negociosdf") == true) {
                JOptionPane.showMessageDialog(null, "Estimado usuario, la contraseña no puede contener \"NEGOCIOSDF\".\nPor favor intenta con una contraseña diferente.");
            } else if (pass.equals(passOld)) {
                JOptionPane.showMessageDialog(null, "Estimado usuario, la nueva contraseña no puede ser igual a la contraseña anterior.");
            } else {
                pass = encryptRsa.encrypt(pass);
                pst = conn.prepareStatement(sql);
                pst.setString(1, pass);
                pst.setInt(2, idEmpleado);
                int N = pst.executeUpdate();
                if (N != 0) {
                    rpta = true;
                    JOptionPane.showMessageDialog(null, "Se actualizo la contraseña con exito.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al cambiar la contraseña.\n" + e);
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
        return rpta;
    }

    @Override
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
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
        }
        return pass;
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

    public static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean diferenteDoc(int id, String numDoc) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        String doc = "";
        try {
            String sql = "SELECT numDoc FROM vista_usuarios WHERE idPersona = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                doc = rs.getString("numDoc");
            }
            rpta = !doc.equals(numDoc);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar si coinciden los numeros de documentos.\n" + e.getMessage(), "Error Numero Documento", JOptionPane.ERROR_MESSAGE);
        }
        return rpta;
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

    public static void main(String[] args) {
//        int idP = 27;
//        String nombre = "Lidia";
//        String apPat = "Corrr";
//        String apMat = "Efffff";
//        int tipoD = 1;
//        String numD = "12345678";
//        String tel = "123456";
//        String direc = "aaaaa";
//        String cargo = "Almacen";
        String user = "LFCE1";
//        String pass = "12345";
        String pass = "negociosDF24";
        usuariosDaoImpl u = new usuariosDaoImpl();
        eUsuarios objeto = new eUsuarios();
//        objeto.setIdPersona(idP);
//        objeto.setNombre(nombre);
//        objeto.setApPaterno(apPat);
//        objeto.setApMaterno(apMat);
//        objeto.setTipoDoc(tipoD);
//        objeto.setNumDoc(numD);
//        objeto.setTelefono(tel);
//        objeto.setDireccion(direc);
//        objeto.setCargo(cargo);
//        System.out.println("InsertarUser: " + u.insertar(objeto));
//        System.out.println("diferenteDoc: " + u.diferenteDoc(27, numD));
//        System.out.println("EditarUser: "+u.editar(objeto));
//        objeto = u.login(user, pass);
//        System.out.println("Login: " + objeto.getApPaterno());
//        List<eUsuarios> lista = u.listar("", "");
//        if (lista.isEmpty()) {
//            System.out.println("Ningun Registro");
//        } else {
//            for (int i = 0; i < lista.size(); i++) {
//                System.out.println("Nombre: " + lista.get(i).getNombre());
//            }
//        }
//        System.out.println("ExisteDni: " + u.existeDoc(numD));
//
//        System.out.println("ExisteUsername: " + u.usernameExiste("LPCE22"));
//        System.out.println("Eliminar: "+u.eliminar(idP));
//        System.out.println("obtenerObjetoPorId: "+u.obtenerObjetoPorId(idP).getNombre());
        System.out.println("ResetPass: "+u.resetContrasenia(17));
    }
}
