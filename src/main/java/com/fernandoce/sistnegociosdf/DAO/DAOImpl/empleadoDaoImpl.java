/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO.DAOImpl;

import com.fernandoce.sistnegociosdf.DAO.empleadoDao;
import com.fernandoce.sistnegociosdf.entidades.eEmpleado;
import com.fernandoce.sistnegociosdf.entidades.eTipoDoc;
import com.fernandoce.sistnegociosdf.extras.encriptacionRSA;
import java.awt.HeadlessException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

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
    eTipoDoc tipodoc;
    encriptacionRSA encryptRsa;

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
            while (rs.next()) {
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
            }
            return null;

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
    public List<eEmpleado> listar() {
        return null;
    }

    @Override
    public boolean insertar(eEmpleado objeto) {
        conn = Conexion.getConectar();
        encryptRsa = new encriptacionRSA();
        boolean rpta = false;
        int rpt = 0;
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
            cst.setString(9, generarUser(nameAll));
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
                JOptionPane.showMessageDialog(null, "El usuario se registro con exito");
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
        return false;
    }

    @Override
    public boolean eliminar(int idObjeto) {
        return false;
    }

    @Override
    public eEmpleado obtenerObjetoPorId(int idObjeto) {
        conn = Conexion.getConectar();
        empleado = new eEmpleado();
        encryptRsa = new encriptacionRSA();
        String unsecure;

        System.out.println("idObjeto: " + idObjeto);
        try {
            String sql = "SELECT contrasenia FROM empleado WHERE idEmpleado = ?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, idObjeto);
            rs = pst.executeQuery();

            while (rs.next()) {
                String pass = rs.getString("contrasenia");
                System.out.println("Pass_: " + pass);
                unsecure = encryptRsa.decrypt(pass);
                empleado.setContrasenia(unsecure);
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
            username += i.substring(0, 1);//            
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
        String passReset = "";
        try {
            passReset = encryptRsa.encrypt(getPassDefault());
            String sql = "UPDATE empleado SET contrasenia = ?, primerAcceso = 'SI' WHERE idEmpleado = ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, passReset);
            pst.setInt(2, idEmpleado);
            rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Se reseteo la contraseña con exito.");
                return true;
            } else {
                return false;
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al resetear la contraseña");
            return false;
        }finally {
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
            JOptionPane.showMessageDialog(null, "Ocurrio un error al cambiar la contraseña.\n"+e);
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

    public static void main(String[] args) {
        String name = "Luis Pa";
        String apPat = "Cor";
        String apMat = "Eff";
        int tipoD = 1;
        String numDoc = "111a1145";
        String tel = "000";
        String direcion = "ds";
        String cargo = "Vendedor";
        empleadoDaoImpl eI = new empleadoDaoImpl();
        eEmpleado e = new eEmpleado();
        e.setNombre(name);
        e.setApPaterno(apPat);
        e.setApMaterno(apMat);
        e.setTipoDoc(tipoD);
        e.setNumDoc(numDoc);
        e.setTelefono(tel);
        e.setDireccion(direcion);
        e.setCargo(cargo);
//        System.out.println(eI.insertar(e));
//        System.out.println(eI.obtenerObjetoPorId(15).getContrasenia());
//        System.out.println(eI.login("LPCE1", "negociosDF24").getNombre());
//        System.out.println(eI.getPass(11));
//        System.out.println(eI.cambiarContrasenia(15, "12345"));
    }
}
