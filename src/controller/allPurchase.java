
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

public class allPurchase {
    List med = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";
    
    public void purchase(String medicineName, String quantity){
        int quant = Integer.parseInt(quantity);
        System.out.println(quant);
        String medicName = null;
        int updateQuantity = 0;
        String totalQuant = null;
        String idMed = null;
        String identity = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String retrieveMed = String.format("Select idMedicine, medicineName, quantity from tblMedicine where medicineName='%s'", medicineName);
        String retrieveQueryPharmacist = String.format("Select pharmaIdentity from tblPharma");
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(retrieveMed);
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
                    stmt.executeUpdate("UPDATE tblMedicine SET quantity='" + updateQuantity + "' where medicineName='" + medicineName + "'");
                    stmt.executeUpdate(String.format("INSERT INTO tblpurchase(idMedicine,quantity) VALUES ('%s','%d')", idMed, quant));
                    stmt.executeUpdate(String.format("INSERT INTO tblAllPurchase(idMedicine,quantityPurchased,date) VALUES ('%s','%d','%s')", idMed, quant, dtf.format(now)));

                    //not final...
                    ResultSet rs = stmt.executeQuery(retrieveQueryPharmacist);
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
    
    public void clickAppName(){
        String identity = null;
        String retrieveQueryPharmacist = String.format("Select pharmaIdentity from tblPharma");
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(retrieveQueryPharmacist);
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
    
    public void cancel(){
        String retrieveQueryPharmacist = String.format("Select pharmaIdentity from tblPharma");
        String identity = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(retrieveQueryPharmacist);
            rs.next();
            identity = rs.getString("pharmaIdentity");
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
    
    public void viewAllPurchase(JTable table){
        String medicName = null;
        String quantity = null;
        String price = null;
        String date = null;

        String retrieveMed = String.format("Select COUNT(idAllPurchase) from tblAllPurchase");
        String retrievePurchased = String.format("Select m.medicineName, p.quantityPurchased, m.price, p.date FROM tblAllPurchase p LEFT JOIN tblmedicine m ON p.idMedicine = m.idMedicine");
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(retrievePurchased);
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                quantity = rsAccount.getString("quantityPurchased");
                price = rsAccount.getString("price");
                date = rsAccount.getString("date");
//                System.out.println(medicName + " " + brandName + " " + genName + " " + price);
                med.add(medicName);
                med.add(quantity);
                med.add(price);
                med.add(date);
            }
            ResultSet resultSet = stmt.executeQuery(retrieveMed);
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
    
    public void viewPurchase(JTable table, JLabel totalPrice){
        String medicName = null;
        String quantity = null;
        String price = null;

        String count = ("SELECT SUM(p.quantity * m.price) AS totalPrice FROM tblpurchase p LEFT JOIN tblmedicine m ON p.idMedicine = m.idMedicine");
        String retrieveMed = String.format("Select COUNT(idPurchase) from tblpurchase");
        String retrievePurchased = String.format("Select m.medicineName, p.quantity, m.price FROM tblpurchase p LEFT JOIN tblmedicine m ON p.idMedicine = m.idMedicine");
        Connection conn = null;
        Statement stmt = null;
//        String retrieveQuery;
//        retrieveQuery = String.format("SELECT * from `medic`");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rsAccount = stmt.executeQuery(retrievePurchased);
            while (rsAccount.next()) {
                medicName = rsAccount.getString("medicineName");
                quantity = rsAccount.getString("quantity");
                price = rsAccount.getString("price");
//                System.out.println(medicName + " " + brandName + " " + genName + " " + price);
                med.add(medicName);
                med.add(quantity);
                med.add(price);
            }
            ResultSet resultSet = stmt.executeQuery(retrieveMed);
            resultSet.next();
            int rowcount = resultSet.getInt(1);
            System.out.println(rowcount);
            int x = 0;
//            medTable.addRowSelectionInterval(0, rowcount);
//            System.out.println(medTable.getRowCount());
            System.out.println("asdf");
            for (int row = 0; row < rowcount; row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
//                    System.out.println(medTable.getValueAt(row, col));
                    table.setValueAt(med.get(x), row, col);
                    x++;
                }
            }
            ResultSet resultSetCount = stmt.executeQuery(count);
            resultSetCount.next();
            totalPrice.setText(resultSetCount.getString("totalPrice"));
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
