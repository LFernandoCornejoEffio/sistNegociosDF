/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO;

import com.fernandoce.sistnegociosdf.entidades.eUsuarios;
import java.util.List;

/**
 *
 * @author lfern
 */
public interface empleadoDao extends crudDAO<eUsuarios>{
    public eUsuarios login(String username, String password);
    public List<eUsuarios> listar(String campo, String busqueda);
}
