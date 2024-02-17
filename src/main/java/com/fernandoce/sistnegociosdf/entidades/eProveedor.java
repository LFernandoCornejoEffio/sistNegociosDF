/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.entidades;

/**
 *
 * @author lfern
 */
public class eProveedor extends eTipoDoc{
    private int idProveedor;
    private int tipoDoc;
    private String numDocProveedor;
    private String razonSocial;
    private String direccion;
    private String telefono;
    private String email;
    private String estado;

    public eProveedor() {
    }

    public eProveedor(int idProveedor, int tipoDoc, String numDocProveedor, String razonSocial, String direccion, String telefono, String email, String estado, int idTipoDoc, String abrevTipoDoc) {
        super(idTipoDoc, abrevTipoDoc);
        this.idProveedor = idProveedor;
        this.tipoDoc = tipoDoc;
        this.numDocProveedor = numDocProveedor;
        this.razonSocial = razonSocial;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.estado = estado;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(int tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDocProveedor() {
        return numDocProveedor;
    }

    public void setNumDocProveedor(String numDocProveedor) {
        this.numDocProveedor = numDocProveedor;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
