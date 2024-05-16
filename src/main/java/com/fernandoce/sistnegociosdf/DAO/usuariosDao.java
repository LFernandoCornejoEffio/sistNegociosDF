/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO;

import com.fernandoce.sistnegociosdf.entidades.eUsuarios;

/**
 *
 * @author lfern
 */
public interface usuariosDao extends crudDAO<eUsuarios>{    
    public eUsuarios login(String username, String password);
    public boolean existeDoc(String doc);
    public String generarUser(String nameCom);
    public boolean usernameExiste(String username);
    public boolean ultimoAcceso(int idEmpleado);
    public boolean resetContrasenia(int idEmpleado);
    public boolean cambiarContrasenia(int idEmpleado, String pass);
    public String getPass(int idEmpleado);
}
