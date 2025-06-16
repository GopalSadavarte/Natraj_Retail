package com.app.config;

import java.sql.*;

public class DBConnection {
    public static Connection con = null;

    public static void createConnection() {
        try {
            con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/natraj_retail", "postgres",
                    "grs2004311");
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at DBConnection.java 12");
        }
    }

    public static ResultSet executeQuery(String query) throws Exception {
        Statement st = con.createStatement();
        return st.executeQuery(query);
    }
}
