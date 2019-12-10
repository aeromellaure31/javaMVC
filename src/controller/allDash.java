package controller;

import view.login;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

public class allDash {

    List med = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";

    public void cough(String medName, int tab, JTable table) {
        med.clear();
        String medicName = null;
        String brandName = null;
        String genName = null;
        String price = null;

//        String retrieveQuery = String.format("INSERT INTO medic(firstName,age,lastName,userName,password) VALUES ('%s','%d','%s','%s','%s')", firstname, age, lastname,userName,logpass);
        String retrieveQuery = String.format("Select medicineName, brandName, genericName, price from tblMedicine where medicineType='%s'", medName);
        String retrieveMed = String.format("Select COUNT(medicineName) from tblMedicine where medicineType='%s'", medName);
        Connection conn = null;
        Statement stmt = null;

//        String retrieveQuery;
//        retrieveQuery = String.format("SELECT * from `medic`");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(retrieveQuery);
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                brandName = rsAccount.getString("brandName");
                genName = rsAccount.getString("genericName");
                price = rsAccount.getString("price");
//                System.out.println(medicName + " " + brandName + " " + genName + " " + price);
                med.add(medicName);
                med.add(brandName);
                med.add(genName);
                med.add(price);
            }
            ResultSet resultSet = stmt.executeQuery(retrieveMed);
            resultSet.next();
            int rowcount = resultSet.getInt(1);
            int x = 0;
            for (int row = 0; row < rowcount; row++) {
                for (int col = 0; col < tab; col++) {
                    table.setValueAt(med.get(x), row, col);
                    x++;
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void logOut() {
        String delete = ("DELETE FROM tblpurchase");
        String updateStatus = String.format("UPDATE tblPharma SET pharmaIdentity='%s'", 0);
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate(delete);
            stmt.executeUpdate(updateStatus);
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        new login().setVisible(true);
    }
}
