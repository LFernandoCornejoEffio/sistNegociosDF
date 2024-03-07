/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.extras;

/**
 *
 * @author lfern
 */
public class controlValidaciones {

    public boolean ingresarLetras(int key) {
        boolean mayusculas = key >= 65 && key <= 90 || key == 209;
        boolean minusculas = key >= 97 && key <= 122 || key == 241;        
        boolean borrar = key == 8;
        return minusculas || mayusculas || borrar;
    }
    
    public boolean ingresarLetrasSinNcontilde(int key) {
        boolean mayusculas = key >= 65 && key <= 90;
        boolean minusculas = key >= 97 && key <= 122;
        boolean borrar = key == 8;
        return minusculas || mayusculas || borrar;
    }
    
    public boolean  ingresarCaracteresEspeciales(int key){
        //33 = !, 35 = #, 36 = $, 38 = &, 42 = *, 43 = +, 45 = -, 46 = ., 47 = /, 64 = @, 95 = _, 124 = |
        boolean caracteres = key == 33 || key == 35 || key == 36 || key == 38 || key == 42 || key == 43 || key == 64 || key == 95 || key == 124 || key >= 45 && key <= 47;
        boolean borrar = key == 8;
        return caracteres || borrar;
    }
    
    public boolean ingresarNumeros(int key){
        boolean numeros = key >= 48 && key <= 57;
        boolean borrar = key == 8;
        return numeros || borrar;
    }
    
    public boolean espacio(int key){
        boolean espacio = key == 32;
        return espacio;
    }
}
