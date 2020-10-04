package com.example.longqifeng.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtils {
    public static int getConnection() throws ClassNotFoundException {
        final String CLS = "com.mysql.jdbc.Driver";
        final String URL = "jdbc:mysql://121.41.230.43:3306/appUser";
        final String USER = "administrator";
        final String PASSWORD = "45683759";

        int count = 0;

        try {
            Class.forName(CLS);
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "Select count(1) as s1 from user_sheet";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count = rs.getInt("s1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
