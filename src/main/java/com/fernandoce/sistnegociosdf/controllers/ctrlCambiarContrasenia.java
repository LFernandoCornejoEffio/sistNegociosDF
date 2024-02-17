/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.DAO.DAOImpl.empleadoDaoImpl;
import com.fernandoce.sistnegociosdf.extras.controlValidaciones;
import com.fernandoce.sistnegociosdf.formularios.frmCambiarContrasenia;
import com.fernandoce.sistnegociosdf.formularios.frmPrincipal;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;

/**
 *
 * @author lfern
 */
public class ctrlCambiarContrasenia extends frmCambiarContrasenia implements ActionListener, KeyListener {

    empleadoDaoImpl emDaoImpl;
    controlValidaciones ctrlValidaciones;
    frmPrincipal frmP;
    ctrlLogin ctrlL;

    public ctrlCambiarContrasenia(Frame parent, boolean modal, int idPersona) {
        super(parent, modal);
        this.setTitle("Cambiar Contraseña");
        this.setSize(400, 400);
        this.lblIdEmpleado.setText(String.valueOf(idPersona));
        this.setLocationRelativeTo(null);
        this.btnGuardar.addActionListener(this);
        this.btnCancelar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        emDaoImpl = new empleadoDaoImpl();
        ctrlL = new ctrlLogin();
        int id = Integer.parseInt(lblIdEmpleado.getText());
        String nuevaPass = this.txtNuevaPass.getText();
        String confirmarPass = this.txtConfirmarPass.getText();
        if (e.getSource() == this.btnGuardar) {
            if (nuevaPass.equals("") || confirmarPass.equals("")) {
                JOptionPane.showMessageDialog(this, "Estimado usuario los campos nueva contraseña y confirmar contraseña son obligatorios", "Validación de Campos", JOptionPane.INFORMATION_MESSAGE);
            } else if (nuevaPass.equals(confirmarPass)) {
                boolean update = emDaoImpl.cambiarContrasenia(id, nuevaPass);
                if (update == true) {
                    this.setVisible(false);
                    this.dispose();

                    ctrlL.setVisible(false);
                    ctrlL.dispose();
                    frmP = new frmPrincipal();
                    frmP.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Estimado usuario los campos nueva contraseña y confirmar contraseña no coinciden", "Validación de Campos", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (e.getSource() == this.btnCancelar) {
            this.setVisible(false);
            ctrlL.setVisible(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        ctrlValidaciones = new controlValidaciones();
        int key = e.getKeyChar();
        if (e.getSource() == txtNuevaPass || e.getSource() == txtConfirmarPass) {
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
