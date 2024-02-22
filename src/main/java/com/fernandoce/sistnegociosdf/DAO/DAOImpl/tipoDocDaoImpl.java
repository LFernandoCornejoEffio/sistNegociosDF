/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO.DAOImpl;

import com.fernandoce.sistnegociosdf.DAO.tipoDocDao;
import com.fernandoce.sistnegociosdf.entidades.eTipoDoc;
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
public class tipoDocDaoImpl implements tipoDocDao {
    Connection conn = null;
    PreparedStatement pst;
    ResultSet rs;
    eTipoDoc tipoDoc;
    
    @Override
    public List<eTipoDoc> listar() {
        ArrayList<eTipoDoc> listaTipoDoc = new ArrayList();
        try {
            conn = Conexion.getConectar();
            String sql = "SELECT * FROM vista_tipodoccombo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                tipoDoc = new eTipoDoc();
                tipoDoc.setIdTipoDoc(rs.getInt("idTipoDoc"));
                tipoDoc.setAbrevTipoDoc(rs.getString("abrevTipoDoc"));
                listaTipoDoc.add(tipoDoc);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e);
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
        return listaTipoDoc;
    }
    
    public static void main(String[] args) {
        tipoDocDao t = new tipoDocDaoImpl();
        List<eTipoDoc> lista = t.listar();
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(lista.get(i).getNombreTipoDoc());
        }
    }
}
