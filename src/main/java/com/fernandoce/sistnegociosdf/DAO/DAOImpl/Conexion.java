/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fernandoce.sistnegociosdf.DAO.DAOImpl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author lfern
 */
public class Conexion {

    private static Connection cn;
    static String driver;
    static String url;
    static String user;
    static String password;

    public static Connection getConectar() {

        Properties prop = new Properties();
        String fileProperties = "application.properties";
        try (InputStream setting = Conexion.class.getClassLoader().getResourceAsStream(fileProperties)) {
            prop.load(setting);
            driver = prop.get("db.driverClassName").toString();
            url = prop.get("db.url").toString();
            user = prop.get("db.username").toString();
            password = prop.get("db.password").toString();

            Class.forName(driver);
            cn = DriverManager.getConnection(url, user, password);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            String[] options = {"Aceptar"};
            int conectar = JOptionPane.showOptionDialog(null, "Error al conectar con la base de datos\n\nPor favor contactar a soporte tecnico\n\n" + e, "Conexion con Base de Datos", JOptionPane.ERROR_MESSAGE, 0, null, options, options[0]);
            if (conectar >= 0) {
                System.exit(0);
            }
        }
        return cn;
    }

}
