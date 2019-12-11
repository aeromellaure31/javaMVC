package controller;

import view.dashboard;
import view.dashboardPharmacist;
import view.login;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.*;

public class userController {
    List user = new ArrayList();
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/medicine";
    static final String USER = "root";
    static final String PASS = "";

    public void register(String f, String a, String l, String u, String lp, String r) {
        userModel reg = new userModel();
        String name = null;
        if (l.equals("") || f.equals("") || u.equals("") || lp.equals("") || r.equals("") || a.equals("")) {
            JOptionPane.showMessageDialog(null, "All fields are required!!!");
        } else {
//            String insertQuery = String.format("INSERT INTO medic(firstName,age,lastName,userName,password) VALUES ('%s','%d','%s','%s','%s')", f, Integer.parseInt(a), l, u, lp);
//            String fetchData = ("SELECT userName FROM medic");
            Connection conn = null;
            Statement stmt = null;

            try {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                stmt = conn.createStatement();
                ResultSet rsAccount = stmt.executeQuery(reg.fetch());
                while (rsAccount.next()) {
                    name = rsAccount.getString("userName");
                    user.add(name);
                }
                if (user.contains(u)) {
                    JOptionPane.showMessageDialog(null, "UserName already exist!!!");
                } else if (lp.length() < 6) {
                    JOptionPane.showMessageDialog(null, "Password is weak!!!");
                } else if ((lp.equals(r))) {
                    stmt.executeUpdate(reg.insertQuery(f, a, l, u, lp));
                    conn.close();
                    new login().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Password doesn't match!Re-type password");
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public void login(String u, String p){
        userModel reg = new userModel();
        String password = null;
        String userName = null;
        String age = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            if (u.equals("admin")) {
                ResultSet rs = stmt.executeQuery(reg.retrieveQueryPharmacist(u));
                while (rs.next()) {
                    password = rs.getString("password");
                    userName = rs.getString("userName");
                }
                if ((p.equals(password) && (u.equals(userName)))) {
                    stmt.executeUpdate(reg.updateStatus());
                    new dashboardPharmacist().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Username or Password is incorrect!!!");
                }
            } else {
                ResultSet rsAccount = stmt.executeQuery(reg.retrieveQuery(u));
                while (rsAccount.next()) {
                    password = rsAccount.getString("password");
                    userName = rsAccount.getString("userName");
                    age = rsAccount.getString("age");
                }
                if ((p.equals(password) && (u.equals(userName)))) {
                    stmt.executeUpdate(reg.status(u));
                    new dashboard().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Username or Password is incorrect!!!");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
