
package controller;

import view.dashboardPharmacist;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import model.*;

public class medicineController {
    List allMed = new ArrayList();
    List med = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";
    
    public void addMed(String medType, String medName, String brName, String genName, String price, String quantity){
        medicineModel medicine = new medicineModel();
        String fetchMedName = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            if (medType.equals("Cough") || medType.equals("Allergies") || medType.equals("BodyPain") || medType.equals("Headache")) {
                ResultSet rsAccount = stmt.executeQuery(medicine.fetchAllMedicine());
                while (rsAccount.next()) {
                    fetchMedName = rsAccount.getString("medicineName");
                    med.add(fetchMedName);
                }
                if (!med.contains(medName)) {
                    stmt.executeUpdate(medicine.insertMed(medType, medName, brName, genName, price, quantity));
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
        medicineModel medicine = new medicineModel();
        String medicName = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(medicine.fetchAllMedicine());
            while (rs.next()) {
                medicName = rs.getString("medicineName");
                med.add(medicName);
            }
            if (med.contains(medicineName)) {
                stmt.executeQuery(medicine.delMed(medicineName));
            } else {
                JOptionPane.showMessageDialog(null, "No Medicine Name!!!");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void search(String searched, JTextField medType, JTextField medName, JTextField brandName, JTextField genName, JTextField price, JTextField quantity){
        medicineModel medicine = new medicineModel();
        String data = null;
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(medicine.fetchAllMedicine());
            while (rs.next()) {
                data = rs.getString("medicineName");
                allMed.add(data.toLowerCase());
            }
            if (allMed.contains(searched.toLowerCase())) {
                ResultSet rsAccount = stmt.executeQuery(medicine.searchMed(searched));
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
        medicineModel medicine = new medicineModel();
        String fetchMedName = null;
        Connection conn = null;
        Statement stmt = null;
//        String retrieveQuery;
//        retrieveQuery = String.format("SELECT * from `medic`");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            if (medicineType.equals("Cough") || medicineType.equals("Allergies") || medicineType.equals("BodyPain") || medicineType.equals("Headache")) {
                ResultSet rsAccount = stmt.executeQuery(medicine.fetchAllMedicine());
                while (rsAccount.next()) {
                    fetchMedName = rsAccount.getString("medicineName");
                    med.add(fetchMedName);
                }
                if (searched.equalsIgnoreCase(medicineName)) {
                    stmt.executeUpdate(medicine.outcome(searched, medicineType, medicineName, brName, genericName, price, quant));
                    new dashboardPharmacist().setVisible(true);
                    conn.close();
                    med.clear();
                } else if (!med.contains(medicineName)) {
                    stmt.executeUpdate(medicine.outcome(searched, medicineType, medicineName, brName, genericName, price, quant));
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
