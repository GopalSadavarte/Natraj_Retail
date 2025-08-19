package com.app.config;

import java.sql.*;

public class DBConnection {

    public static final Connection con;

    static {
        Connection temp = null;
        try {
            temp = DriverManager.getConnection("jdbc:postgresql://localhost:5432/natraj_retail",
                    "postgres",
                    "grs2004311");
            ;
        } catch (Exception e) {
            System.out.println(e.getMessage() + " at DBConnection.createConnection() 12");
        }
        con = temp;
    }

    public static ResultSet executeQuery(String query) throws Exception {
        Statement st = con.createStatement();
        return st.executeQuery(query);
    }

    
}
