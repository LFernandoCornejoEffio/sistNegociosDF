/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO;

import java.util.List;

/**
 *
 * @author lfern
 */
public interface crudDAO <T>{
    public List<T> listar(String campo, String busqueda);
    public boolean insertar(T objeto);
    public boolean editar(T objeto);
    public boolean eliminar(int idObjeto);
    public T obtenerObjetoPorId(int idObjeto);    
}
