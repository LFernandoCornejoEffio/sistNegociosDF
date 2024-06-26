/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.DAO.DAOImpl.tipoDocDaoImpl;
import com.fernandoce.sistnegociosdf.DAO.DAOImpl.usuariosDaoImpl;
import com.fernandoce.sistnegociosdf.entidades.eUsuarios;
import com.fernandoce.sistnegociosdf.entidades.eTipoDoc;
import com.fernandoce.sistnegociosdf.extras.controlBotones;
import com.fernandoce.sistnegociosdf.extras.controlItemMenu;
import com.fernandoce.sistnegociosdf.extras.controlValidaciones;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmEditarUsuario;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmNuevoUsuario;
import com.fernandoce.sistnegociosdf.formularios.frmPrincipal;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmReporteUsuarios;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmUsuarios;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author lfern
 */
public final class ctrlUsuarios extends frmUsuarios implements ActionListener, KeyListener {

    controlValidaciones ctrlValidaciones;
    controlBotones ctrlBotones;
    controlItemMenu ctrlItemMenu;
    usuariosDaoImpl usuariosDaoImpl;
    frmNuevoUsuario frmNuevoUsuario;
    frmEditarUsuario frmEditarUsuario;
    frmPrincipal frmPrincipal;
    frmReporteUsuarios frmReporteUsuarios;
    tipoDocDaoImpl tipoDocDaoImpl;
    eUsuarios empleado;

    String rss = "src/main/resources/imagenes/";

    public ctrlUsuarios() {
        this.btnNuevo.addActionListener(this);
        this.btnBuscar.addActionListener(this);
        this.btnLimpiar.addActionListener(this);
        this.btnExcel.addActionListener(this);
        this.menuEditar.addActionListener(this);
        this.menuEliminar.addActionListener(this);
        this.menuReset.addActionListener(this);
        this.btnReporte.addActionListener(this);
        Listar(tblUsuarios, "", "");
        iconBtn();
        this.setVisible(true);

        frmPrincipal = new frmPrincipal();
        frmNuevoUsuario = new frmNuevoUsuario(frmPrincipal, true);
        frmEditarUsuario = new frmEditarUsuario(frmPrincipal, true);
        frmReporteUsuarios = new frmReporteUsuarios(frmPrincipal, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int fila = tblUsuarios.getSelectedRow();
        int idPersona;
        if (e.getSource() == this.btnNuevo) {
            nuevoUsuario();
        }

        if (e.getSource() == this.btnExcel) {
            excel();
        }

        if (e.getSource() == this.btnBuscar) {
            String campo = cbBuscar.getSelectedItem().toString();
            String busqueda = txtBuscar.getText();
            Listar(tblUsuarios, campo, busqueda);
        }

        if (e.getSource() == this.btnLimpiar) {
            limpiarBusqueda();
        }

        if (e.getSource() == this.btnReporte) {
            reporte();
        }

        if (e.getSource() == this.menuEditar) {
            idPersona = Integer.parseInt(String.valueOf(tblUsuarios.getValueAt(fila, 0)));
            showEditar(idPersona);
        }

        if (e.getSource() == this.menuReset) {
            String[] opciones = {"Si", "NO"};
            idPersona = Integer.parseInt(String.valueOf(tblUsuarios.getValueAt(fila, 0)));
            String user = String.valueOf(tblUsuarios.getValueAt(fila, 2));
            int rpta = JOptionPane.showOptionDialog(this, "¿Estas seguro de resetear la contraseña del usuario " + user + "?", "Resetear Contraseña", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
            if (rpta == 0) {
                resetPass(idPersona);
            }
        }

        if (e.getSource() == this.menuEliminar) {
            String[] opciones = {"Si", "NO"};
            String user = String.valueOf(tblUsuarios.getValueAt(fila, 2));
            idPersona = Integer.parseInt(String.valueOf(tblUsuarios.getValueAt(fila, 0)));
            int rpta = JOptionPane.showOptionDialog(this, "¿Estas seguro de eliminar al usuario " + user + "?", "Eliminar usuario", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
            if (rpta == 0) {
                eliminar(idPersona);
            }
        }

        if (e.getSource() == frmNuevoUsuario.btnGuardar) {
            String name = frmNuevoUsuario.txtNombres.getText();
            String apPat = frmNuevoUsuario.txtPaterno.getText();
            String apMat = frmNuevoUsuario.txtMaterno.getText();
            int tipoD = frmNuevoUsuario.cbTipoDoc.getItemAt(frmNuevoUsuario.cbTipoDoc.getSelectedIndex()).getIdTipoDoc();
            String numDoc = frmNuevoUsuario.txtNumDoc.getText();
            String tel = frmNuevoUsuario.txtTelefono.getText();
            String direcion = frmNuevoUsuario.txtDireccion.getText();
            String cargo = String.valueOf(frmNuevoUsuario.cbCargo.getSelectedItem());
            empleado = new eUsuarios();
            empleado.setNombre(name);
            empleado.setApPaterno(apPat);
            empleado.setApMaterno(apMat);
            empleado.setTipoDoc(tipoD);
            empleado.setNumDoc(numDoc);
            empleado.setTelefono(tel);
            empleado.setDireccion(direcion);
            empleado.setCargo(cargo);
            btnGuardar(empleado);
        }

        if (e.getSource() == frmNuevoUsuario.btnLimpiar) {
            btnLimpiarNuevo();
        }

        if (e.getSource() == frmNuevoUsuario.btnCancelar) {
            frmNuevoUsuario.dispose();
        }

        if (e.getSource() == frmEditarUsuario.btnGuardar) {
            int idEmpleado = Integer.parseInt(frmEditarUsuario.idPersona.getText());
            String name = frmEditarUsuario.txtNombre.getText();
            String apPat = frmEditarUsuario.txtPaterno.getText();
            String apMat = frmEditarUsuario.txtMaterno.getText();
            int tipoD = frmEditarUsuario.cbTipoDoc.getItemAt(frmEditarUsuario.cbTipoDoc.getSelectedIndex()).getIdTipoDoc();
            String numDoc = frmEditarUsuario.txtNumDoc.getText();
            String tel = frmEditarUsuario.txtTelefono.getText();
            String direcion = frmEditarUsuario.txtDireccion.getText();
            String cargo = String.valueOf(frmEditarUsuario.cbCargo.getSelectedItem());
            empleado = new eUsuarios();
            empleado.setIdPersona(idEmpleado);
            empleado.setNombre(name);
            empleado.setApPaterno(apPat);
            empleado.setApMaterno(apMat);
            empleado.setTipoDoc(tipoD);
            empleado.setNumDoc(numDoc);
            empleado.setTelefono(tel);
            empleado.setDireccion(direcion);
            empleado.setCargo(cargo);
            btnEditar(empleado);
        }

        if (e.getSource() == frmEditarUsuario.btnCancelar) {
            frmEditarUsuario.dispose();
        }

        if (e.getSource() == frmReporteUsuarios.btnGenerar) {
            reportes();
            limpiarReporte();
        }

        if (e.getSource() == frmReporteUsuarios.btnLimpiar) {
            limpiarReporte();
        }

        if (e.getSource() == frmReporteUsuarios.btnRegresar) {
            btnRegresar();
        }
    }

    private void limpiarBusqueda() {
        cbBuscar.setSelectedIndex(0);
        txtBuscar.setText("");
        Listar(tblUsuarios, "", "");
    }

    private void reporte() {
        ctrlBotones = new controlBotones();
        frmPrincipal = new frmPrincipal();
        frmReporteUsuarios = new frmReporteUsuarios(frmPrincipal, true);
        frmReporteUsuarios.setTitle("Reporte de Usuarios");
        frmReporteUsuarios.setSize(550, 320);
        frmReporteUsuarios.setResizable(false);
        frmReporteUsuarios.setLocationRelativeTo(null);
        ctrlBotones.iconoBtn(frmReporteUsuarios.btnGenerar, rss + "pdf.png", 25, 25);
        ctrlBotones.iconoBtn(frmReporteUsuarios.btnLimpiar, rss + "limpiar.png", 25, 25);
        ctrlBotones.iconoBtn(frmReporteUsuarios.btnRegresar, rss + "volver.png", 30, 30);
        frmReporteUsuarios.btnGenerar.addActionListener(this);
        frmReporteUsuarios.btnLimpiar.addActionListener(this);
        frmReporteUsuarios.btnRegresar.addActionListener(this);
        frmReporteUsuarios.setVisible(true);
    }

    private void nuevoUsuario() {
        ctrlBotones = new controlBotones();
        frmPrincipal = new frmPrincipal();
        frmNuevoUsuario = new frmNuevoUsuario(frmPrincipal, true);
        frmNuevoUsuario.setSize(810, 460);
        frmNuevoUsuario.setTitle("Nuevo Usuario");
        frmNuevoUsuario.setResizable(false);
        frmNuevoUsuario.setLocationRelativeTo(null);
        ctrlBotones.iconoBtn(frmNuevoUsuario.btnGuardar, rss + "salvar.png", 30, 30);
        ctrlBotones.iconoBtn(frmNuevoUsuario.btnLimpiar, rss + "limpiar.png", 30, 30);
        ctrlBotones.iconoBtn(frmNuevoUsuario.btnCancelar, rss + "return.png", 30, 30);
        frmNuevoUsuario.btnGuardar.addActionListener(this);
        frmNuevoUsuario.btnLimpiar.addActionListener(this);
        frmNuevoUsuario.btnCancelar.addActionListener(this);
        //Eventos para tipear
        frmNuevoUsuario.txtNombres.addKeyListener(this);
        frmNuevoUsuario.txtPaterno.addKeyListener(this);
        frmNuevoUsuario.txtMaterno.addKeyListener(this);
        frmNuevoUsuario.txtNumDoc.addKeyListener(this);
        frmNuevoUsuario.txtDireccion.addKeyListener(this);
        frmNuevoUsuario.txtTelefono.addKeyListener(this);
        comboTipoDoc(frmNuevoUsuario.cbTipoDoc);
        frmNuevoUsuario.setVisible(true);
    }

    private void btnGuardar(eUsuarios empleado) {
        usuariosDaoImpl = new usuariosDaoImpl();
        usuariosDaoImpl.insertar(empleado);
        boolean update = usuariosDaoImpl.insertar(empleado);
        if (update == true) {
            JOptionPane.showMessageDialog(frmNuevoUsuario, "El usuario se registro con exito.");
            btnLimpiarNuevo();
        }
        Listar(tblUsuarios, "", "");
        frmNuevoUsuario.dispose();
    }

    private void btnEditar(eUsuarios empleado) {
        usuariosDaoImpl = new usuariosDaoImpl();
        boolean update = usuariosDaoImpl.editar(empleado);
        if (update == true) {
            JOptionPane.showMessageDialog(frmEditarUsuario, "El usuario se actualizo con exito.");
            btnLimpiarEdit();
        }
        Listar(tblUsuarios, "", "");
        frmEditarUsuario.dispose();
    }

    private void eliminar(int idPersona) {
        usuariosDaoImpl = new usuariosDaoImpl();
        boolean delete = usuariosDaoImpl.eliminar(idPersona);
        if (delete == true) {
            JOptionPane.showMessageDialog(null, "El usuario se elimino con exito.");
            Listar(tblUsuarios, "", "");
        }
    }

    private void btnLimpiarNuevo() {
        frmNuevoUsuario.txtNombres.setText("");
        frmNuevoUsuario.txtPaterno.setText("");
        frmNuevoUsuario.txtMaterno.setText("");
        frmNuevoUsuario.cbTipoDoc.setSelectedIndex(0);
        frmNuevoUsuario.txtNumDoc.setText("");
        frmNuevoUsuario.txtTelefono.setText("");
        frmNuevoUsuario.txtDireccion.setText("");
        frmNuevoUsuario.cbCargo.setSelectedIndex(0);
    }

    private void btnLimpiarEdit() {
        frmEditarUsuario.txtNombre.setText("");
        frmEditarUsuario.txtPaterno.setText("");
        frmEditarUsuario.txtMaterno.setText("");
        frmEditarUsuario.cbTipoDoc.setSelectedIndex(0);
        frmEditarUsuario.txtNumDoc.setText("");
        frmEditarUsuario.txtTelefono.setText("");
        frmEditarUsuario.txtDireccion.setText("");
        frmEditarUsuario.cbCargo.setSelectedIndex(0);
    }

    private void showEditar(int idPersona) {
        ctrlBotones = new controlBotones();
        frmPrincipal = new frmPrincipal();
        frmEditarUsuario = new frmEditarUsuario(frmPrincipal, true);
        comboTipoDoc(frmEditarUsuario.cbTipoDoc);
        frmEditarUsuario.setSize(810, 460);
        frmEditarUsuario.setTitle("Nuevo Usuario");
        frmEditarUsuario.setResizable(false);
        frmEditarUsuario.setLocationRelativeTo(null);
        ctrlBotones.iconoBtn(frmEditarUsuario.btnGuardar, rss + "salvar.png", 30, 30);
        ctrlBotones.iconoBtn(frmEditarUsuario.btnCancelar, rss + "return.png", 30, 30);
        empleado = usuariosDaoImpl.obtenerObjetoPorId(idPersona);
        frmEditarUsuario.idPersona.setText(String.valueOf(empleado.getIdPersona()));
        frmEditarUsuario.idPersona.setVisible(true);
        frmEditarUsuario.txtNombre.setText(empleado.getNombre().toUpperCase());
        frmEditarUsuario.txtPaterno.setText(empleado.getApPaterno().toUpperCase());
        frmEditarUsuario.txtMaterno.setText(empleado.getApMaterno().toUpperCase());
        int idTipo = empleado.getTipoDoc();
        frmEditarUsuario.cbTipoDoc.setSelectedItem(new eTipoDoc(idTipo));
        frmEditarUsuario.txtNumDoc.setText(empleado.getNumDoc().toUpperCase());
        frmEditarUsuario.txtTelefono.setText(empleado.getTelefono().toUpperCase());
        frmEditarUsuario.txtDireccion.setText(empleado.getDireccion().toUpperCase());
        frmEditarUsuario.cbCargo.setSelectedItem(empleado.getCargo().toUpperCase());
        frmEditarUsuario.btnCancelar.addActionListener(this);
        frmEditarUsuario.btnGuardar.addActionListener(this);
        //Eventos para tipear
        frmEditarUsuario.txtNombre.addKeyListener(this);
        frmEditarUsuario.txtPaterno.addKeyListener(this);
        frmEditarUsuario.txtMaterno.addKeyListener(this);
        frmEditarUsuario.txtNumDoc.addKeyListener(this);
        frmEditarUsuario.txtDireccion.addKeyListener(this);
        frmEditarUsuario.txtTelefono.addKeyListener(this);
        frmEditarUsuario.setVisible(true);
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
        ctrlItemMenu.iconoBtn(menuReset, rss + "reiniciarPass.png", 20, 20);
    }

    private void Listar(JTable tbl, String campo, String busqueda) {
        usuariosDaoImpl = new usuariosDaoImpl();
        List<eUsuarios> lista = usuariosDaoImpl.listar(campo, busqueda);
        DefaultTableModel dtm = new DefaultTableModel();
        String titulos[] = {"ID", "N°", "Nombre Completo", "Tipo Doc.", "Numero Doc.", "Cargo", "Telefono", "Dirección", "Usuario", "Ultimo Acceso", "Primer Acceso"};
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
            filaTbl[5] = lista.get(i).getCargo();
            filaTbl[6] = lista.get(i).getTelefono();
            filaTbl[7] = lista.get(i).getDireccion();
            filaTbl[8] = lista.get(i).getUsername();
            filaTbl[9] = lista.get(i).getUltimo_Acceso();
            filaTbl[10] = lista.get(i).getPrimerAcceso();
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

    private void comboTipoDoc(JComboBox comboBox) {
        tipoDocDaoImpl = new tipoDocDaoImpl();
        List<eTipoDoc> listaTipoDoc = tipoDocDaoImpl.listar();
        comboBox.removeAllItems();
        for (int i = 0; i < listaTipoDoc.size(); i++) {
            comboBox.addItem(new eTipoDoc(listaTipoDoc.get(i).getIdTipoDoc(), listaTipoDoc.get(i).getAbrevTipoDoc().toUpperCase()));
        }
    }

    private void resetPass(int idPersona) {

        usuariosDaoImpl = new usuariosDaoImpl();
        boolean resetPass = usuariosDaoImpl.resetContrasenia(idPersona);
        if (resetPass == true) {
            JOptionPane.showMessageDialog(frmEditarUsuario, "El reseteo la contraseña con exito.");
            Listar(tblUsuarios, "", "");
        } else {
            JOptionPane.showMessageDialog(frmEditarUsuario, "Ocurrio un error al resetear la contraseña.");
        }
    }

    private void btnRegresar() {
        frmReporteUsuarios.dispose();
    }

    private void excel() {
        usuariosDaoImpl = new usuariosDaoImpl();
        usuariosDaoImpl.reporteUsuariosExcel(tblUsuarios);
    }

    private void limpiarReporte() {
        frmReporteUsuarios.cbCargo.setSelectedIndex(0);
        frmReporteUsuarios.fechaInicio.setDate(null);
        frmReporteUsuarios.fechaFin.setDate(null);
    }

    private JasperPrint reportes() {
        String cargo;
        SimpleDateFormat sdfInicio = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdfFin = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String fechaInicio;
        String fechaFin;
        usuariosDaoImpl = new usuariosDaoImpl();
        if (frmReporteUsuarios.cbCargo.getSelectedIndex() == 0) {
            cargo = "";
        } else {
            cargo = frmReporteUsuarios.cbCargo.getSelectedItem().toString();
        }

        if (frmReporteUsuarios.fechaInicio.getDate() == null) {
            fechaInicio = "";
        } else {
            fechaInicio = sdfInicio.format(frmReporteUsuarios.fechaInicio.getDate());
        }
        if (frmReporteUsuarios.fechaFin.getDate() == null) {
            fechaFin = "";
        } else {
            fechaFin = sdfFin.format(frmReporteUsuarios.fechaFin.getDate());
        }

        if (cargo.equals("") && fechaInicio.equals("") && fechaFin.equals("")) {
            return usuariosDaoImpl.reporteUsuariosSinFiltro();
        } else if (!cargo.equals("") && (fechaInicio.equals("") || fechaFin.equals(""))) {
            if (fechaInicio.equals("") && fechaFin.equals("")) {
                return usuariosDaoImpl.reporteUsuariosCargo(cargo);
            } else {
                JOptionPane.showMessageDialog(frmReporteUsuarios, "Por favor seleccione la fecha faltante");
                return null;
            }
        } else if (cargo.equals("") && (!fechaInicio.equals("") || !fechaFin.equals(""))) {
            return usuariosDaoImpl.reporteUsuariosFecha(fechaInicio, fechaFin);
        } else {
            return usuariosDaoImpl.reporteUsuariosCargoFecha(cargo, fechaInicio, fechaFin);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        ctrlValidaciones = new controlValidaciones();
        char key = e.getKeyChar();
        if (Character.isLowerCase(key)) {
            String cad = ("" + key).toUpperCase();
            key = cad.charAt(0);
            e.setKeyChar(key);
        }

        if (e.getSource() == frmNuevoUsuario.txtNombres || e.getSource() == frmNuevoUsuario.txtPaterno || e.getSource() == frmNuevoUsuario.txtMaterno || e.getSource() == frmEditarUsuario.txtNombre || e.getSource() == frmEditarUsuario.txtPaterno || e.getSource() == frmEditarUsuario.txtMaterno) {
            if (ctrlValidaciones.ingresarLetras(key) == false && ctrlValidaciones.espacio(key) == false) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Solo se permiten letras", "Validación", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (e.getSource() == frmNuevoUsuario.txtTelefono || e.getSource() == frmNuevoUsuario.txtNumDoc || e.getSource() == frmEditarUsuario.txtTelefono || e.getSource() == frmEditarUsuario.txtNumDoc) {
            if (ctrlValidaciones.ingresarNumeros(key) == false) {
                e.consume();
                JOptionPane.showMessageDialog(null, "Solo se permiten numeros", "Validación", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
