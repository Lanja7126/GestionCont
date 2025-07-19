package com.example.gestioncont;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/GestionContacts","root","");
            System.out.println("Connextion établie !");
            return connection;
        }catch (ClassNotFoundException | SQLException e){
            System.out.println("La connection a échoué ! "+ e.getMessage());
            return null;
        }
    }
}
