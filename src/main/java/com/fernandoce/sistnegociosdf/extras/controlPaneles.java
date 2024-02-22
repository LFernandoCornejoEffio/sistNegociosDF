/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.extras;

import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 *
 * @author lfern
 */
public class controlPaneles {
    public void showPanel(JPanel panelPadre, JPanel panelHijo, int ancho, int alto){
        panelHijo.setSize(ancho, alto);
        panelHijo.setLocation(0, 0);
        
        panelHijo.setOpaque(false);
        
        panelPadre.removeAll();
        panelPadre.add(panelHijo, BorderLayout.CENTER);
        panelPadre.revalidate();
        panelPadre.repaint();
    }
}
