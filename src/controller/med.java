
package controller;

import view.dashboardPharmacist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class med {
    List allMed = new ArrayList();
    List med = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";
    
    public void addMed(String medType, String medName, String brName, String genName, String price, String quantity){
        String fetchMedName = null;

        String insertQuery = String.format("INSERT INTO tblMedicine(medicineName,brandName,"
                + "genericName,quantity,price,medicineType)"
                + " VALUES ('%s', '%s', '%s', '%d', '%d', '%s')", medName, brName, genName, Integer.parseInt(quantity), Integer.parseInt(price), medType);
        String fetchQuery = ("Select medicineName from tblmedicine");
        Connection conn = null;
        Statement stmt = null;
//        String retrieveQuery;
//        retrieveQuery = String.format("SELECT * from `medic`");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            if (medType.equals("Cough") || medType.equals("Allergies") || medType.equals("BodyPain") || medType.equals("Headache")) {
                ResultSet rsAccount = stmt.executeQuery(fetchQuery);
                while (rsAccount.next()) {
                    fetchMedName = rsAccount.getString("medicineName");
                    med.add(fetchMedName);
                }
                if (!med.contains(medName)) {
                    stmt.executeUpdate(insertQuery);
                    new dashboardPharmacist().setVisible(true);
                    conn.close();
                    med.clear();
                } else {
                    JOptionPane.showMessageDialog(null, "Medicine name already exist!!!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No type medicine available!!!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void deleteMed(String medicineName){
        String medicName = null;

        String deleteMed = String.format("DELETE FROM tblMedicine WHERE medicineName='%s'", medicineName);
        String allMed = ("SELECT medicineName FROM tblMedicine");
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(allMed);
            while (rs.next()) {
                medicName = rs.getString("medicineName");
                med.add(medicName);
            }
            if (med.contains(medicineName)) {
                stmt.executeQuery(deleteMed);
            } else {
                JOptionPane.showMessageDialog(null, "No Medicine Name!!!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void search(String searched, JTextField medType, JTextField medName, JTextField brandName, JTextField genName, JTextField price, JTextField quantity){
        String fetchMed = String.format("SELECT * FROM tblMedicine WHERE medicineName='%s'", searched);
        String fetchAll = String.format("SELECT medicineName FROM tblMedicine");
        String data = null;
        Connection conn = null;
        Statement stmt = null;
//        String retrieveQuery;
//        retrieveQuery = String.format("SELECT * from `medic`");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(fetchAll);
            while (rs.next()) {
                data = rs.getString("medicineName");
                allMed.add(data.toLowerCase());
            }
            if (allMed.contains(searched.toLowerCase())) {
                ResultSet rsAccount = stmt.executeQuery(fetchMed);
                rsAccount.next();
                medType.setText(rsAccount.getString("medicineType"));
                medName.setText(rsAccount.getString("medicineName"));
                brandName.setText(rsAccount.getString("brandName"));
                genName.setText(rsAccount.getString("genericName"));
                price.setText(rsAccount.getString("price"));
                quantity.setText(rsAccount.getString("quantity"));
            } else {
                JOptionPane.showMessageDialog(null, "No Medicine Match your Search!!!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void update(String searched, String medicineType, String medicineName, String brName, String genericName, String price, String quant){
        String fetchMedName = null;
        String updateQuery = String.format("UPDATE tblmedicine SET medicineName='%s' "
                + ",brandName='%s' ,genericName='%s' ,price='%d',quantity='%d' "
                + ",medicineType='%s' WHERE medicineName='%s'"
                + "", medicineName, brName, genericName, Integer.parseInt(price), Integer.parseInt(quant), medicineType, searched);
        String fetchQuery = ("Select medicineName from tblmedicine");
        Connection conn = null;
        Statement stmt = null;
//        String retrieveQuery;
//        retrieveQuery = String.format("SELECT * from `medic`");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            if (medicineType.equals("Cough") || medicineType.equals("Allergies") || medicineType.equals("BodyPain") || medicineType.equals("Headache")) {
                ResultSet rsAccount = stmt.executeQuery(fetchQuery);
                while (rsAccount.next()) {
                    fetchMedName = rsAccount.getString("medicineName");
                    med.add(fetchMedName);
                }
                if (searched.equalsIgnoreCase(medicineName)) {
                    stmt.executeUpdate(updateQuery);
                    new dashboardPharmacist().setVisible(true);
                    conn.close();
                    med.clear();
                } else if (!med.contains(medicineName)) {
                    stmt.executeUpdate(updateQuery);
                    new dashboardPharmacist().setVisible(true);
                    conn.close();
                    med.clear();
                } else {
                    JOptionPane.showMessageDialog(null, "Medicine name already exist!!!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No type medicine available!!!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
