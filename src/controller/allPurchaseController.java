package controller;

import view.dashboard;
import view.dashboardPharmacist;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import model.*;
import view.purchase;

public class allPurchaseController {

    List med = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";

    public void purchase(String medicineName, String quantity) {
        allPurchaseModel purchase = new allPurchaseModel();
        int quant = Integer.parseInt(quantity);
        String medicName = null;
        int updateQuantity = 0;
        String totalQuant = null;
        String idMed = null;
        String identity = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(purchase.retrieveMed(medicineName));
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                totalQuant = rsAccount.getString("quantity");
                idMed = rsAccount.getString("idMedicine");
                System.out.println(idMed);
            }
            if (medicineName.equalsIgnoreCase(medicName)) {
                updateQuantity = (Integer.parseInt(totalQuant) - quant);
                if (Integer.parseInt(totalQuant) > quant) {
                    System.out.println(updateQuantity);
                    stmt.executeUpdate(purchase.updateQuantity(updateQuantity, medicineName));
                    stmt.executeUpdate(purchase.insertPurchase(idMed, quant));
                    stmt.executeUpdate(purchase.insertAll(idMed, quant, dtf.format(now)));

                    //not final...
                    ResultSet rs = stmt.executeQuery(purchase.retrieveQueryPharmacist());
                    rs.next();
                    identity = rs.getString("pharmaIdentity");
                    if (identity.equals("1")) {
                        new dashboardPharmacist().setVisible(true);
                    } else {
                        new dashboard().setVisible(true);
                    }
                    conn.close();
                } else {
                    JOptionPane.showMessageDialog(null, "Insufficient Medicine!!!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No Medicine Available!!!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void goBackDashboard() {
        allPurchaseModel purchase = new allPurchaseModel();
        String identity = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(purchase.retrieveQueryPharmacist());
            rsAccount.next();
            identity = rsAccount.getString("pharmaIdentity");
            if (identity.equals("1")) {
                new dashboardPharmacist().setVisible(true);
            } else {
                new dashboard().setVisible(true);
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void viewAllPurchase(JTable table) {
        allPurchaseModel purchase = new allPurchaseModel();
        String medicName = null;
        String quantity = null;
        String price = null;
        String date = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(purchase.retrievePurchase());
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                quantity = rsAccount.getString("quantityPurchased");
                price = rsAccount.getString("price");
                date = rsAccount.getString("date");
                med.add(medicName);
                med.add(quantity);
                med.add(price);
                med.add(date);
            }
            ResultSet resultSet = stmt.executeQuery(purchase.countAllPurchase());
            resultSet.next();
            int rowcount = resultSet.getInt(1);
            System.out.println(rowcount);
            int x = 0;
            for (int row = 0; row < rowcount; row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    table.setValueAt(med.get(x), row, col);
                    x++;
                }
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void viewPurchase(JTable table, JLabel totalPrice, int tab, JLabel discount) {
        allDashModel dash = new allDashModel();
        allPurchaseModel purchase = new allPurchaseModel();
        String medicName = null;
        String quantity = null;
        String price = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(purchase.retrieveAllPurchase());
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                quantity = rsAccount.getString("quantity");
                price = rsAccount.getString("price");
                med.add(medicName);
                med.add(quantity);
                med.add(price);
            }
            ResultSet resultSet = stmt.executeQuery(purchase.countPurchase());
            resultSet.next();
            int rowcount = resultSet.getInt(1);
            int x = 0;
            for (int row = 0; row < rowcount; row++) {
                for (int col = 0; col < tab; col++) {
                    table.setValueAt(med.get(x), row, col);
                    x++;
                }
            }
            ResultSet resultSetCount = stmt.executeQuery(purchase.sumPurchae());
            resultSetCount.next();
            totalPrice.setText(resultSetCount.getString("totalPrice"));
            ResultSet rs = stmt.executeQuery(dash.status());
            rs.next();
            System.out.println("jasldkjf " + rs.getInt("age"));
            if (rs.getInt("age") >= 65) {
                ResultSet rsDiscounted = stmt.executeQuery(purchase.sumPurchae());
                rsDiscounted.next();
                discount.setText(Double.toString((rsDiscounted.getInt("totalPrice")) - ((rsDiscounted.getInt("totalPrice")) * 0.20)));
            } else {
                ResultSet rsPrice = stmt.executeQuery(purchase.sumPurchae());
                rsPrice.next();
                discount.setText(rsPrice.getString("totalPrice"));
            }
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
