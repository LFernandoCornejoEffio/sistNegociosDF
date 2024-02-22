/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.extras;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author lfern
 */
public class controlTabla {

    public void tabla(JTable tabla, JScrollPane scrollTabla, String[] titulos, String informacion[][], JPopupMenu pom) {
        tabla = new JTable(informacion, titulos);
        scrollTabla.setViewportView(tabla);
        resizeColumnWidth(tabla);
        for (int c = 0; c < tabla.getColumnCount(); c++) {
            Class<?> col_class = tabla.getColumnClass(c);
            tabla.setDefaultEditor(col_class, null);        // remove editor
        }
        popM(pom, tabla);
    }

    private void popM(JPopupMenu pom, JTable tabla) {
        JPopupMenu pM = pom;        
        tabla.setComponentPopupMenu(pM);
    }

    private void resizeColumnWidth(JTable table) {
        //Se obtiene el modelo de la columna
        TableColumnModel columnModel = table.getColumnModel();
        //Se obtiene el total de las columnas
        for (int column = 0; column < table.getColumnCount(); column++) {
            //Establecemos un valor minimo para el ancho de la columna
            int width = 10; //Min Width
            //Obtenemos el numero de filas de la tabla
            for (int row = 0; row < table.getRowCount(); row++) {
                //Obtenemos el renderizador de la tabla
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                //Creamos un objeto para preparar el renderer
                Component comp = table.prepareRenderer(renderer, row, column);
                //Establecemos el width segun el valor maximo del ancho de la columna
                width = Math.max(comp.getPreferredSize().width + 1, width);

            }
            //Se establece una condicion para no sobrepasar el valor de 300
            //Esto es Opcional
//            if (width > 400) {
//                width = 400;
//            }
            //Se establece el ancho de la columna
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
}
