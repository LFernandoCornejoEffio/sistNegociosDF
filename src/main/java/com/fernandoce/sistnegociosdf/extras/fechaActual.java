/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.extras;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author lfern
 */
public class fechaActual {
    public String getFechaActual(){
        String pattern = "YYYY-MM-dd_HH-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String fecha = simpleDateFormat.format(new Date());

        return fecha;
    }
    
}
