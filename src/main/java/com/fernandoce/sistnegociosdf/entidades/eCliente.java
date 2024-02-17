/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.entidades;

/**
 *
 * @author lfern
 */
public class eCliente extends ePersona{
    private String ultimaCompra;

    public eCliente() {
    }

    public eCliente(String ultimaCompra, int idPersona, String nombre, String apPaterno, String apMaterno, int tipoDoc, String numDoc, String telefono, String dirección, String fechaCreacion, String estado, int idTipoDoc, String abrevTipoDoc) {
        super(idPersona, nombre, apPaterno, apMaterno, tipoDoc, numDoc, telefono, dirección, fechaCreacion, estado, idTipoDoc, abrevTipoDoc);
        this.ultimaCompra = ultimaCompra;
    }

    public String getUltimaCompra() {
        return ultimaCompra;
    }

    public void setUltimaCompra(String ultimaCompra) {
        this.ultimaCompra = ultimaCompra;
    }
    
}
