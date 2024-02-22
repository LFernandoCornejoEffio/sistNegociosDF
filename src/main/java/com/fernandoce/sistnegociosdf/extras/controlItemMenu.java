/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.extras;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 *
 * @author lfern
 */
public class controlItemMenu {
    public void iconoBtn(JMenuItem menuItem, String ruta, int ancho, int alto){
        ImageIcon img = new ImageIcon(ruta);
        Icon icon = new ImageIcon(img.getImage().getScaledInstance(ancho, alto, Image.SCALE_DEFAULT));
        menuItem.setIcon(icon);
    }
}
