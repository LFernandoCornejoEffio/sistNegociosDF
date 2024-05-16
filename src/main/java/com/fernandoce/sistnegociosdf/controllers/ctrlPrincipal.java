/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.entidades.eUsuarios;
import com.fernandoce.sistnegociosdf.extras.controlBotones;
import com.fernandoce.sistnegociosdf.extras.controlLabel;
import com.fernandoce.sistnegociosdf.extras.controlPaneles;
import com.fernandoce.sistnegociosdf.formularios.components.menuAdmin;
import com.fernandoce.sistnegociosdf.formularios.components.menuAlmacen;
import com.fernandoce.sistnegociosdf.formularios.components.menuVendedor;
import com.fernandoce.sistnegociosdf.formularios.frmInicio;
import com.fernandoce.sistnegociosdf.formularios.frmPrincipal;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmUsuarios;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 *
 * @author lfern
 */
public class ctrlPrincipal extends frmPrincipal implements MouseListener {

    ctrlLogin ctrlLogin;
    controlPaneles ctrlPaneles;
    controlLabel ctrlLabel;

    //Paneles Menu
    menuAdmin menuAdmin;
    menuVendedor menuVendedor;
    menuAlmacen menuAlmacen;

    //formularios
    
    String rss = "src/main/resources/imagenes/";

    public ctrlPrincipal(eUsuarios empleado) {
        iniciar(empleado);
    }

    private void iniciar(eUsuarios empleado) {
        this.setSize(1400, 800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("SISTEMA NEGOCIOS D&F - " + empleado.getCargo().toUpperCase());
        ctrlPaneles = new controlPaneles();
        menuAdmin = new menuAdmin();
        menuVendedor = new menuVendedor();
        menuAlmacen = new menuAlmacen();
        switch (empleado.getCargo().toUpperCase()) {
            case "ADMINISTRADOR" -> {
                ctrlPaneles.showPanel(this.panelMenu, menuAdmin, 250, 800);
                menuAdmin.setLocation(0, 0);
                menuAdmin.btnInicio.addMouseListener(this);
                menuAdmin.btnMantenimiento.addMouseListener(this);
                menuAdmin.btnCerrarSesion.addMouseListener(this);
                inicio();
            }
            case "VENDEDOR" -> {
                ctrlPaneles.showPanel(this.panelMenu, menuVendedor, 250, 800);
                menuVendedor.btnInicio.addMouseListener(this);
                menuVendedor.btnCerrarSesion.addMouseListener(this);
                inicio();
            }
            case "ALMACEN" -> {
                ctrlPaneles.showPanel(this.panelMenu, menuAlmacen, 250, 800);
                inicio();

            }
            default ->
                throw new AssertionError();
        }
        String nombreCompleto = empleado.getNombre() + " " + empleado.getApPaterno() + " " + empleado.getApMaterno();
        this.txtuser.setText(nombreCompleto);
        this.txtCargo.setText(empleado.getCargo());
        this.lblId.setText(String.valueOf(empleado.getIdPersona()));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        /* Botones Menu Administrador */
        if (e.getSource() == menuAdmin.btnInicio) {
            inicio();
        }
        
        if (e.getSource() == menuAdmin.btnMantenimiento) {
            ctrlMantenimiento ctrlMantenimiento = new ctrlMantenimiento(this.txtCargo.getText());
            ctrlPaneles.showPanel(this.panelContenido, ctrlMantenimiento, 1105, 670);
        }
        
        if (e.getSource() == menuAdmin.btnCerrarSesion) {
            cerrar();
        }
        /* Botones Menu Vendedor */
        if (e.getSource() == menuVendedor.btnInicio) {
            inicio();
        }

        if (e.getSource() == menuVendedor.btnCerrarSesion) {
            cerrar();
        }
        /* Botones Menu Almacen */
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void inicio() {
        frmInicio frmI = new frmInicio();
        ctrlPaneles.showPanel(this.panelContenido, frmI, 1105, 670);
        setIconBtn();
    }

    private void cerrar() {
        String[] opciones = {"Si", "NO"};
        int rpta = JOptionPane.showOptionDialog(this, "¿Estas seguro de cerrar sesión?", "Cerrar Sesión", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
        if (rpta == 0) {
            ctrlLogin = new ctrlLogin();
            this.dispose();
            ctrlLogin.setVisible(true);
        }
    }
    
    private void setIconBtn(){
        ctrlLabel = new controlLabel();        
        ctrlLabel.mostrarImgLbl(menuAdmin.iconInicio, rss + "home.png", 30, 30);
        ctrlLabel.mostrarImgLbl(menuAdmin.iconVentas, rss+"ventas.png", 30, 30);
        ctrlLabel.mostrarImgLbl(menuAdmin.iconCompras, rss+"compra.png", 30, 30);
        ctrlLabel.mostrarImgLbl(menuAdmin.iconMantenimiento, rss+"mantenimiento.png", 30, 30);
        ctrlLabel.mostrarImgLbl(menuAdmin.iconReportes, rss+"reporte.png", 30, 30);
        ctrlLabel.mostrarImgLbl(menuAdmin.iconMisDatos, rss+"misDatos.png", 30, 30);
        ctrlLabel.mostrarImgLbl(menuAdmin.iconCerrarSesion, rss+"cerrar-sesion.png", 30, 30);
    }
}
