/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.entidades;

/**
 *
 * @author lfern
 */
public class eTipoDoc {
    private int idTipoDoc;
    private String nombreTipoDoc;
    private String abrevTipoDoc;
    private String estadoTipoDoc;

    public eTipoDoc() {
    }

    public eTipoDoc(int idTipoDoc, String nombreTipoDoc, String abrevTipoDoc, String estadoTipoDoc) {
        this.idTipoDoc = idTipoDoc;
        this.nombreTipoDoc = nombreTipoDoc;
        this.abrevTipoDoc = abrevTipoDoc;
        this.estadoTipoDoc = estadoTipoDoc;
    }

    public eTipoDoc(int idTipoDoc, String abrevTipoDoc) {
        this.idTipoDoc = idTipoDoc;
        this.abrevTipoDoc = abrevTipoDoc;
    }

    public int getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(int idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    public String getNombreTipoDoc() {
        return nombreTipoDoc;
    }

    public void setNombreTipoDoc(String nombreTipoDoc) {
        this.nombreTipoDoc = nombreTipoDoc;
    }

    public String getAbrevTipoDoc() {
        return abrevTipoDoc;
    }

    public void setAbrevTipoDoc(String abrevTipoDoc) {
        this.abrevTipoDoc = abrevTipoDoc;
    }

    public String getEstadoTipoDoc() {
        return estadoTipoDoc;
    }

    public void setEstadoTipoDoc(String estadoTipoDoc) {
        this.estadoTipoDoc = estadoTipoDoc;
    }
    
}
