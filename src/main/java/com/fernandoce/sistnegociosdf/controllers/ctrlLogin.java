/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.DAO.DAOImpl.empleadoDaoImpl;
import com.fernandoce.sistnegociosdf.entidades.eEmpleado;
import com.fernandoce.sistnegociosdf.extras.controlBotones;
import com.fernandoce.sistnegociosdf.extras.controlLabel;
import com.fernandoce.sistnegociosdf.extras.controlPaneles;
import com.fernandoce.sistnegociosdf.extras.controlValidaciones;
import com.fernandoce.sistnegociosdf.formularios.components.logo;
import com.fernandoce.sistnegociosdf.formularios.frmLogin;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author lfern
 */
public class ctrlLogin extends frmLogin implements MouseListener, KeyListener {

    controlValidaciones ctrlValidaciones;
    controlBotones ctrlBotones;
    controlLabel ctrlLabel;
    controlPaneles ctrlPaneles;

    empleadoDaoImpl empleadoDAOImpl;
    
    logo log = new logo();

    public ctrlLogin() {
        Iniciar();
    }

    private void Iniciar() {
        this.setSize(650, 450);
        this.setLocationRelativeTo(null);
        this.setTitle("SISTEMA NEGOCIOS D&F - INICIO DE SESIÓN");
        setFondos();
      
        setImgBtn(btnIngresar, "src/main/resources/imagenes/iconAcceso.png");
        setImgBtn(btnCancelar, "src/main/resources/imagenes/cerrar-sesion.png");

        this.txtUser.addKeyListener(this);
        this.txtPass.addKeyListener(this);

        this.btnCancelar.addMouseListener(this);
        this.btnIngresar.addMouseListener(this);
        this.setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent evt) {
        if (evt.getSource() == btnCancelar) {
            System.exit(0);
        }

        if (evt.getSource() == btnIngresar) {
            if (txtUser.getText().equals("") || String.valueOf(txtPass.getPassword()).equals("")) {
                JOptionPane.showMessageDialog(this, "Estimado usuario los campos usuario y contraseña son obligatorios", "Validación de Campos", JOptionPane.INFORMATION_MESSAGE);
            } else {
                eEmpleado eEmpleado = login();
                if (eEmpleado != null) {
                    if (login().getPrimerAcceso().equals("SI")) {
                        JOptionPane.showMessageDialog(rootPane, "BINVENIDO AL SISTEMA: \n" + eEmpleado.getNombre().toUpperCase() + " " + eEmpleado.getApPaterno().toUpperCase() + " " + eEmpleado.getApMaterno().toUpperCase() + "\n"
                                + "<html><p style=\"color:red\">Al ser su Primer Acceso debe cambiar su contraseña</p></html>");
                        ctrlCambiarContrasenia ctrlCambiarPass = new ctrlCambiarContrasenia(this, true, eEmpleado);
                        ctrlCambiarPass.setVisible(true);
                        this.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Bienvenido al sistema: \n" + eEmpleado.getNombre().toUpperCase() + " " + eEmpleado.getApPaterno().toUpperCase() + " " + eEmpleado.getApMaterno().toUpperCase());
                        ctrlPrincipal ctrlPrincipal = new ctrlPrincipal(eEmpleado);
                        ctrlPrincipal.setVisible(true);
                        empleadoDAOImpl.ultimoAcceso(eEmpleado.getIdPersona());
                        this.dispose();
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent evt) {

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
    
    private void setFondos(){
        ctrlPaneles = new controlPaneles();
        ctrlLabel = new controlLabel();
        log = new logo();
        ctrlPaneles.showPanel(this.fondoLogo, log, 250, 450);
        log.setLocation(0, 0);
        ctrlLabel.mostrarImgLbl(log.lblIconNegocio, "src/main/resources/imagenes/iconVent.png", 150, 150);
    }

    private void setImgBtn(JButton button, String root) {
        ctrlBotones = new controlBotones();
        int ancho = 25;
        int alto = 25;
        ctrlBotones.iconoBtn(button, root, ancho, alto);
    }

    private eEmpleado login() {
        empleadoDAOImpl = new empleadoDaoImpl();
        String username = this.txtUser.getText();
        String password = String.valueOf(txtPass.getPassword());
        eEmpleado empleado = empleadoDAOImpl.login(username, password);
        return empleado;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        ctrlValidaciones = new controlValidaciones();
        char key = e.getKeyChar();
        if (e.getSource() == txtUser) {
            if (Character.isLowerCase(key)) {
                String cad = ("" + key).toUpperCase();
                key = cad.charAt(0);
                e.setKeyChar(key);
            }
            if (ctrlValidaciones.ingresarLetras(key) == false && ctrlValidaciones.ingresarNumeros(key) == false) {
                e.consume();
                JOptionPane.showMessageDialog(rootPane, "Solo se permiten letras y numeros", "Validación", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (e.getSource() == txtPass) {
            if (ctrlValidaciones.ingresarCaracteresEspeciales(key) == false && ctrlValidaciones.ingresarLetrasSinNcontilde(key) == false && ctrlValidaciones.ingresarNumeros(key) == false) {
                e.consume();
                JOptionPane.showMessageDialog(rootPane, "Caracter ingresado no permitido\nCaracteres Permitidos: \nLetras\nNumeros\n! # $ & * + - . / @ _ |", "Validación", JOptionPane.INFORMATION_MESSAGE);
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
