package controller;

import view.login;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import model.*;
import view.purchase;

public class allDashController {

    List med = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";

    public void cough(String medName, int tab, JTable table) {
        allDashModel dash = new allDashModel();
        med.clear();
        String medicName = null;
        String brandName = null;
        String genName = null;
        String price = null;

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(dash.retrieveQuery(medName));
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                brandName = rsAccount.getString("brandName");
                genName = rsAccount.getString("genericName");
                price = rsAccount.getString("price");
                med.add(medicName);
                med.add(brandName);
                med.add(genName);
                med.add(price);
            }
            ResultSet resultSet = stmt.executeQuery(dash.retrieveMed(medName));
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
        allDashModel dash = new allDashModel();
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            stmt.executeUpdate(dash.delete());
            stmt.executeUpdate(dash.updateStatus());
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        new login().setVisible(true);
    }
    
    public void status() {
        allDashModel dash = new allDashModel();
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(dash.status());
            rs.next();
            if(rs.getInt("age") < 18){
                JOptionPane.showMessageDialog(null, "You are not allowed to Purchase");
            }else{
                new purchase().setVisible(true);
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
