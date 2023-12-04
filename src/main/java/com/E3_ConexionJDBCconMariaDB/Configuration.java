/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.E3_ConexionJDBCconMariaDB;

/**
 *
 * @author Luis Quintano
 */
public class Configuration {
    public static final String DRIVER = "org.mariadb.jdbc.Driver";  //el driver de MySQL pero se podría poner otro driver.
    public static final String URLCONNECTION = "jdbc:mariadb://localhost:3306/Instituto";  //para conectarnos a la BBDD.
    public static final String USER = "root";
    public static final String PASSW = "root";
    
}
