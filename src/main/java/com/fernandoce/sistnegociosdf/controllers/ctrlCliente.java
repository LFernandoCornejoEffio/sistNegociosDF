/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.DAO.DAOImpl.clienteDaoImpl;
import com.fernandoce.sistnegociosdf.DAO.DAOImpl.tipoDocDaoImpl;
import com.fernandoce.sistnegociosdf.entidades.eCliente;
import com.fernandoce.sistnegociosdf.entidades.eTipoDoc;
import com.fernandoce.sistnegociosdf.extras.controlBotones;
import com.fernandoce.sistnegociosdf.extras.controlItemMenu;
import com.fernandoce.sistnegociosdf.formularios.frmPrincipal;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmClientes;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmEditarCliente;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmNuevoCliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author lfern
 */
public class ctrlCliente extends frmClientes implements ActionListener, KeyListener{
        
    clienteDaoImpl clienteDaoImpl;
    controlBotones ctrlBotones;
    controlItemMenu ctrlItemMenu;
    frmPrincipal frmPrincipal;
    frmNuevoCliente frmNuevoCliente;
    frmEditarCliente frmEditarCliente;
    tipoDocDaoImpl tipoDocDaoImpl;
    eCliente cliente;
    
    String rss = "src/main/resources/imagenes/";
    
    public ctrlCliente() {
        this.btnNuevo.addActionListener(this);
        this.btnBuscar.addActionListener(this);
        this.btnLimpiar.addActionListener(this);
        this.btnExcel.addActionListener(this);
        this.menuEditar.addActionListener(this);
        this.menuEliminar.addActionListener(this);
        this.btnReporte.addActionListener(this);
        Listar(tblClientes, "", "");
        iconBtn();
        frmPrincipal = new frmPrincipal();
        frmNuevoCliente = new frmNuevoCliente(frmPrincipal, true);
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        int fila = tblClientes.getSelectedRow();
        int idPersona;
        
        if (e.getSource() == this.btnNuevo) {
            nuevoCliente();
        }

        if (e.getSource() == this.btnExcel) {
//            excel();
        }

        if (e.getSource() == this.btnBuscar) {
            String campo = cbBuscar.getSelectedItem().toString();
            String busqueda = txtBuscar.getText();
            Listar(tblClientes, campo, busqueda);
        }

        if (e.getSource() == this.btnLimpiar) {
            limpiarBusqueda();
        }
        
        if (e.getSource() == this.menuEditar) {
            idPersona = Integer.parseInt(String.valueOf(tblClientes.getValueAt(fila, 0)));
//            showEditar(idPersona);
        }

        if (e.getSource() == this.menuEliminar) {
            String[] opciones = {"Si", "NO"};
            String user = String.valueOf(tblClientes.getValueAt(fila, 2));
            idPersona = Integer.parseInt(String.valueOf(tblClientes.getValueAt(fila, 0)));
            int rpta = JOptionPane.showOptionDialog(this, "¿Estas seguro de eliminar al usuario " + user + "?", "Eliminar usuario", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
//            if (rpta == 0) {
//                eliminar(idPersona);
//            }
        }
        
        if (e.getSource() == frmNuevoCliente.btnGuardar) {
            String name = frmNuevoCliente.txtNombres.getText();
            String apPat = frmNuevoCliente.txtPaterno.getText();
            String apMat = frmNuevoCliente.txtMaterno.getText();
            int tipoD = frmNuevoCliente.cbTipoDoc.getItemAt(frmNuevoCliente.cbTipoDoc.getSelectedIndex()).getIdTipoDoc();
            String numDoc = frmNuevoCliente.txtNumDoc.getText();
            String tel = frmNuevoCliente.txtTelefono.getText();
            String direcion = frmNuevoCliente.txtDireccion.getText();
            cliente = new eCliente();
            cliente.setNombre(name);
            cliente.setApPaterno(apPat);
            cliente.setApMaterno(apMat);
            cliente.setTipoDoc(tipoD);
            cliente.setNumDoc(numDoc);
            cliente.setTelefono(tel);
            cliente.setDireccion(direcion);
            btnGuardar(cliente);            
        }
        
        if (e.getSource() == frmNuevoCliente.btnLimpiar) {
            btnLimpiarNuevo();
        }
        
        if (e.getSource() == frmNuevoCliente.btnCancelar) {
            frmNuevoCliente.dispose();
        }
    }
    
    private void iconBtn() {
        ctrlBotones = new controlBotones();
        ctrlBotones.iconoBtn(btnBuscar, rss + "lupa.png", 20, 20);
        ctrlBotones.iconoBtn(btnNuevo, rss + "agregaruser.png", 30, 30);
        ctrlBotones.iconoBtn(btnLimpiar, rss + "limpiar.png", 20, 20);
        ctrlBotones.iconoBtn(btnExcel, rss + "excel.png", 20, 20);
        ctrlBotones.iconoBtn(btnReporte, rss + "pdf.png", 20, 20);
    }
    
    private void iconItemMenu() {
        ctrlItemMenu = new controlItemMenu();
        ctrlItemMenu.iconoBtn(menuEditar, rss + "editar.png", 20, 20);
        ctrlItemMenu.iconoBtn(menuEliminar, rss + "eliminarUser.png", 20, 20);
    }
    
    
    private void Listar(JTable tbl, String campo, String busqueda) {
        clienteDaoImpl = new clienteDaoImpl();
        List<eCliente> lista = clienteDaoImpl.listar(campo, busqueda);
        DefaultTableModel dtm = new DefaultTableModel();
        String titulos[] = {"ID", "N°", "Nombre Completo", "Tipo Doc.", "Numero Doc.", "Telefono", "Dirección", "Ultima Compra"};
        for (String titulo : titulos) {
            dtm.addColumn(titulo);
        }

        for (int i = 0; i < lista.size(); i++) {
            Object[] filaTbl = new Object[titulos.length];
            filaTbl[0] = lista.get(i).getIdPersona();
            filaTbl[1] = (i + 1);
            String nomcomp = lista.get(i).getNombre() + " " + lista.get(i).getApPaterno() + " " + lista.get(i).getApMaterno();
            filaTbl[2] = nomcomp;
            filaTbl[3] = lista.get(i).getAbrevTipoDoc();
            filaTbl[4] = lista.get(i).getNumDoc();
            filaTbl[5] = lista.get(i).getTelefono();
            filaTbl[6] = lista.get(i).getDireccion();
            filaTbl[7] = lista.get(i).getUltimaCompra();
            dtm.addRow(filaTbl);
        }
        
        tbl.setModel(dtm);
        TableColumn colId = tbl.getColumnModel().getColumn(0);
        TableColumn colNum = tbl.getColumnModel().getColumn(1);
        TableColumn colAbrevTipoDoc = tbl.getColumnModel().getColumn(3);
        colNum.setPreferredWidth(20);
        colNum.setMaxWidth(25);
        colNum.setMinWidth(20);
        colId.setResizable(false);
        colId.setMinWidth(0);
        colId.setMaxWidth(0);
        colId.setPreferredWidth(0);
        colId.setResizable(false);
        colAbrevTipoDoc.setMinWidth(60);
        colAbrevTipoDoc.setMaxWidth(60);
        colAbrevTipoDoc.setPreferredWidth(60);
        iconItemMenu();
    }
    
    private void limpiarBusqueda() {
        cbBuscar.setSelectedIndex(0);
        txtBuscar.setText("");
        Listar(tblClientes, "", "");
    }
    
    private void nuevoCliente() {
        ctrlBotones = new controlBotones();
        frmPrincipal = new frmPrincipal();
        frmNuevoCliente = new frmNuevoCliente(frmPrincipal, true);
        frmNuevoCliente.setSize(810, 410);
        frmNuevoCliente.setTitle("Nuevo Cliente");
        frmNuevoCliente.setResizable(false);
        frmNuevoCliente.setLocationRelativeTo(null);
        ctrlBotones.iconoBtn(frmNuevoCliente.btnGuardar, rss + "salvar.png", 30, 30);
        ctrlBotones.iconoBtn(frmNuevoCliente.btnLimpiar, rss + "limpiar.png", 30, 30);
        ctrlBotones.iconoBtn(frmNuevoCliente.btnCancelar, rss + "return.png", 30, 30);
        frmNuevoCliente.btnGuardar.addActionListener(this);
        frmNuevoCliente.btnLimpiar.addActionListener(this);
        frmNuevoCliente.btnCancelar.addActionListener(this);
        //Eventos para tipear
        frmNuevoCliente.txtNombres.addKeyListener(this);
        frmNuevoCliente.txtPaterno.addKeyListener(this);
        frmNuevoCliente.txtMaterno.addKeyListener(this);
        frmNuevoCliente.txtNumDoc.addKeyListener(this);
        frmNuevoCliente.txtDireccion.addKeyListener(this);
        frmNuevoCliente.txtTelefono.addKeyListener(this);
        comboTipoDoc(frmNuevoCliente.cbTipoDoc);
        frmNuevoCliente.setVisible(true);
    }
    
    private void comboTipoDoc(JComboBox comboBox) {
        tipoDocDaoImpl = new tipoDocDaoImpl();
        List<eTipoDoc> listaTipoDoc = tipoDocDaoImpl.listar();
        comboBox.removeAllItems();
        for (int i = 0; i < listaTipoDoc.size(); i++) {
            comboBox.addItem(new eTipoDoc(listaTipoDoc.get(i).getIdTipoDoc(), listaTipoDoc.get(i).getAbrevTipoDoc().toUpperCase()));
        }
    }
    
    private void btnGuardar(eCliente cliente){
        clienteDaoImpl = new clienteDaoImpl();
        boolean update = clienteDaoImpl.insertar(cliente);
        if (update == true) {
            JOptionPane.showMessageDialog(frmNuevoCliente, "El cliente se registro con exito.");
            btnLimpiarNuevo();
        }
        Listar(tblClientes, "", "");
        frmNuevoCliente.dispose();
    }
    
    private void btnLimpiarNuevo() {
        frmNuevoCliente.txtNombres.setText("");
        frmNuevoCliente.txtPaterno.setText("");
        frmNuevoCliente.txtMaterno.setText("");
        frmNuevoCliente.cbTipoDoc.setSelectedIndex(0);
        frmNuevoCliente.txtNumDoc.setText("");
        frmNuevoCliente.txtTelefono.setText("");
        frmNuevoCliente.txtDireccion.setText("");
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
