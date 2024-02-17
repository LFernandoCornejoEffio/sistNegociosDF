/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.DAO.DAOImpl.empleadoDaoImpl;
import com.fernandoce.sistnegociosdf.entidades.eEmpleado;
import com.fernandoce.sistnegociosdf.extras.controlBotones;
import com.fernandoce.sistnegociosdf.extras.controlLabel;
import com.fernandoce.sistnegociosdf.extras.controlValidaciones;
import com.fernandoce.sistnegociosdf.formularios.frmCambiarContrasenia;
import com.fernandoce.sistnegociosdf.formularios.frmLogin;
import com.fernandoce.sistnegociosdf.formularios.frmPrincipal;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author lfern
 */
public class ctrlLogin extends frmLogin implements MouseListener, KeyListener {

    controlValidaciones ctrlValidaciones;
    controlBotones ctrlBotones;
    controlLabel ctrlLabel;
    empleadoDaoImpl empleadoDAOImpl;

    public ctrlLogin() {
        Iniciar();
    }

    private void Iniciar() {
        this.setVisible(true);
        this.setSize(650, 450);
        this.setLocationRelativeTo(null);
        setImgLbl(this.lblIconNegocio, "src/main/resources/imagenes/iconVent.png");
        setImgBtn(btnIngresar, "src/main/resources/imagenes/iconAcceso.png");
        setImgBtn(btnCancelar, "src/main/resources/imagenes/cerrar-sesion.png");

        this.txtUser.addKeyListener(this);
        this.txtPass.addKeyListener(this);

        this.btnCancelar.addMouseListener(this);
        this.btnIngresar.addMouseListener(this);
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
                if (login() != null) {
                    if (login().getPrimerAcceso().equals("SI")) {
                        JOptionPane.showMessageDialog(rootPane, "Bienvenido al sistema: \n" + login().getNombre().toUpperCase() + " " + login().getApPaterno().toUpperCase() + " " + login().getApMaterno().toUpperCase());                  
                        ctrlCambiarContrasenia ctrlCambiarPass = new ctrlCambiarContrasenia(this, true, login().getIdPersona());
                        ctrlCambiarPass.setVisible(true);
                        this.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Bienvenido al sistema: \n" + login().getNombre().toUpperCase() + " " + login().getApPaterno().toUpperCase() + " " + login().getApMaterno().toUpperCase());
                        frmPrincipal frmPrincipal = new frmPrincipal();
                        frmPrincipal.setVisible(true);
                    }
                }

//                if (login().getIdUsuario() > 0) {
//                    usuario usuario = login();
//                    if(usuario.getPersonaId().getEstado().equals("Activo") && loginDAOImpl.ultimoAcceso(usuario.getIdUsuario()) == true){
//                        this.dispose();
//                        JOptionPane.showMessageDialog(rootPane, "Bienvenido al sistema " + usuario.getNomCompleto(), "Acceso al sistema", JOptionPane.PLAIN_MESSAGE);
//                        ctrlPrincipal = new controlPrincipal(usuario.getPersonaId().getCargoId().getIdCargo(), usuario.getPersonaId().getIdPersona()); 
//                    }else{
//                        JOptionPane.showMessageDialog(this, "El usuario se encuentra inactivo\nPor favor contactar al administrador del sistema", "Acceso al Sistema", JOptionPane.INFORMATION_MESSAGE);
//                    }
//                } else {
//                    JOptionPane.showMessageDialog(rootPane, "Las credenciales ingresadas son incorrectas o no se encuentra registrado", "Acceso Denegado", JOptionPane.ERROR_MESSAGE);
//                }
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

    private void setImgLbl(JLabel jLabel, String root) {
        ctrlLabel = new controlLabel();
        int ancho = jLabel.getWidth();
        int alto = jLabel.getHeight();
        ctrlLabel.mostrarImgLbl(jLabel, root, ancho, alto);
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
        int key = e.getKeyChar();
        if (e.getSource() == txtUser) {
            if (ctrlValidaciones.ingresarLetras(key) == false && ctrlValidaciones.ingresarNumeros(key) == false) {
                e.consume();
                JOptionPane.showMessageDialog(rootPane, "Solo se permiten letras", "Validación", JOptionPane.INFORMATION_MESSAGE);
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
