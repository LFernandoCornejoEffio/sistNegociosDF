/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.controllers;

import com.fernandoce.sistnegociosdf.formularios.frmMantenimiento;
import com.fernandoce.sistnegociosdf.formularios.mantenimiento.frmProveedor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

/**
 *
 * @author lfern
 */
public class ctrlMantenimiento extends frmMantenimiento implements ActionListener {

    ctrlUsuarios ctrlUsuarios = new ctrlUsuarios();
    ctrlCliente ctrlCliente = new ctrlCliente();
    frmProveedor frmP = new frmProveedor();
    
    String rss = "src/main/resources/imagenes/";
    
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public ctrlMantenimiento(String cargo) {
        if (cargo.equals("ADMINISTRADOR")) {
            ImageIcon imgUsuarios = new ImageIcon(rss+"users.png");
            ImageIcon imgClientes = new ImageIcon(rss+"clientes.png");
            ImageIcon imgProveedores = new ImageIcon(rss+"proveedor.png");
            this.tabPanel.addTab("USUARIOS", new ImageIcon(imgUsuarios.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)) , ctrlUsuarios);
            this.tabPanel.addTab("CLIENTES", new ImageIcon(imgClientes.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)) , ctrlCliente);
            this.tabPanel.addTab("PROVEEDORES", new ImageIcon(imgProveedores.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)) , frmP);
        }else{
            
        }
        
        this.setVisible(true);
    }

}
