/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.entidades;

/**
 *
 * @author lfern
 */
public class eUsuarios extends ePersona{
    private String cargo;
    private String username;
    private String contrasenia;
    private String ultimo_Acceso;
    private String primerAcceso;

    public eUsuarios() {
    }  

    public eUsuarios(String cargo, String username, String contrasenia, String ultimo_Acceso, String primerAcceso, int idPersona, String nombre, String apPaterno, String apMaterno, int tipoDoc, String numDoc, String telefono, String dirección, String fechaCreacion, String estado, int idTipoDoc, String abrevTipoDoc) {
        super(idPersona, nombre, apPaterno, apMaterno, tipoDoc, numDoc, telefono, dirección, fechaCreacion, estado, idTipoDoc, abrevTipoDoc);
        this.cargo = cargo;
        this.username = username;
        this.contrasenia = contrasenia;
        this.ultimo_Acceso = ultimo_Acceso;
        this.primerAcceso = primerAcceso;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getUltimo_Acceso() {
        return ultimo_Acceso;
    }

    public void setUltimo_Acceso(String ultimo_Acceso) {
        this.ultimo_Acceso = ultimo_Acceso;
    }

    public String getPrimerAcceso() {
        return primerAcceso;
    }

    public void setPrimerAcceso(String primerAcceso) {
        this.primerAcceso = primerAcceso;
    }
    
}
