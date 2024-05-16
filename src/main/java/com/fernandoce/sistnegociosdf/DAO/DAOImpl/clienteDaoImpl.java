/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO.DAOImpl;

import com.fernandoce.sistnegociosdf.DAO.clienteDao;
import com.fernandoce.sistnegociosdf.entidades.eCliente;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author lfern
 */
public class clienteDaoImpl implements clienteDao{
    
    Connection conn = null;
    CallableStatement cst = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    eCliente cliente = null;
    List<eCliente> listaCliente = null;
    static String rutasalidaPdf;
    static String rutaSalidaExcel;
    
    @Override
    public List<eCliente> listar(String campo, String busqueda) {
        conn = Conexion.getConectar();
        listaCliente = new ArrayList();
        try {
            String sql = "{CALL buscarClientes (?, ?)}";
            cst = conn.prepareCall(sql);
            cst.setString(1, campo);
            cst.setString(2, busqueda);
            rs = cst.executeQuery();
            while (rs.next()) {                
                cliente = new eCliente();
                cliente.setIdPersona(rs.getInt("idPersona"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApPaterno(rs.getString("apPaterno"));
                cliente.setApMaterno(rs.getString("apMaterno"));
                cliente.setAbrevTipoDoc(rs.getString("abrevTipoDoc"));
                cliente.setNumDoc(rs.getString("numDoc"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setUltimaCompra(rs.getString("ultimaCompra"));
                listaCliente.add(cliente);
            }
            return listaCliente;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al listar clientes. \nError: " + e.getMessage());
            return null;
        }finally{
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e.getMessage(), "Error - Metodo Listar", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public boolean insertar(eCliente objeto) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        boolean existeDoc;
        int _idCliente = 0;
        try {
            existeDoc = existeDoc(objeto.getNumDoc());
            if (existeDoc == false) {
                String sql = "{CALL insertarcliente (?, ?, ?, ?, ?, ?, ?)}";
                cst = conn.prepareCall(sql);
                cst.setString(1, objeto.getNombre());
                cst.setString(2, objeto.getApPaterno());
                cst.setString(3, objeto.getApMaterno());
                cst.setInt(4, objeto.getTipoDoc());
                cst.setString(5, objeto.getNumDoc());
                cst.setString(6, objeto.getTelefono());
                cst.setString(7, objeto.getDireccion());
                rs = cst.executeQuery();
                while (rs.next()) {
                    _idCliente = rs.getInt("_idCliente");
                }
                rpta = _idCliente > 0;
            } else {
                JOptionPane.showMessageDialog(null, "Error al registrar el cliente.\nYa se encuentra registrado un Cliente con el numero de documento " + objeto.getNumDoc() + ".", "Error al registrar el cliente", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el cliente.\nError: " + e, "Error al registrar el cliente", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "Error al cerrar conexion: " + e, "Error al registrar el cliente", JOptionPane.ERROR_MESSAGE);
            }
        }
        return rpta;
    }

    @Override
    public boolean editar(eCliente objeto) {
        return true;
    }

    @Override
    public boolean eliminar(int idObjeto) {
        return true;
    }

    @Override
    public eCliente obtenerObjetoPorId(int idObjeto) {
        return null;
    }
    
    public boolean existeDoc(String doc) {
        conn = Conexion.getConectar();
        boolean rpta = false;
        int t = 0;
        try {
            String sql = "SELECT COUNT(numDoc) AS tNumDoc FROM vista_clientes WHERE numDoc LIKE ? AND estado LIKE 'Activo'";
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
}
