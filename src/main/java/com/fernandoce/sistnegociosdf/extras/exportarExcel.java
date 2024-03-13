/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.extras;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author lfern
 */
public class exportarExcel {

    public boolean excelReporte(String nameHoja, JTable tabla, String ruta) {
        Workbook libro = new XSSFWorkbook();
        fechaActual fecha = new fechaActual();
        final String nameFile = nameHoja + "_" + fecha.getFechaActual() + ".xlsx";
        Sheet hoja = libro.createSheet(nameHoja);

        Row fila = hoja.createRow(0);

        CellStyle estiloCellHeader = libro.createCellStyle();
        estiloCellHeader.setBorderBottom(BorderStyle.THIN);
        estiloCellHeader.setBorderLeft(BorderStyle.THIN);
        estiloCellHeader.setBorderRight(BorderStyle.THIN);
        estiloCellHeader.setBorderTop(BorderStyle.THIN);

        for (int i = 1; i < tabla.getColumnCount(); i++) {
            Cell celda = fila.createCell(i - 1);
            celda.setCellStyle(estiloCellHeader);
            celda.setCellValue(tabla.getColumnName(i));
            hoja.autoSizeColumn(i);
        }

        for (int j = 0; j < tabla.getRowCount(); j++) {
            Row filaDatos = hoja.createRow(j + 1);
            for (int k = 1; k < tabla.getColumnCount(); k++) {
                Cell celda = filaDatos.createCell(k - 1);
                celda.setCellStyle(estiloCellHeader);
                if (tabla.getValueAt(j, k) != null) {
                    celda.setCellValue(tabla.getValueAt(j, k).toString());
                }
                hoja.autoSizeColumn(k);
            }
        }

        String fileSalida = ruta + nameFile;

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(new File(fileSalida));
            libro.write(outputStream);
            libro.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error al generar excel.\n" + e.getMessage());
            return false;
        }

    }
}
