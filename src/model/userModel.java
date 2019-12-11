
package model;

public class userModel {
    
    public String fetch(){
       return ("SELECT userName FROM medic");
    }
    
    public String insertQuery(String f, String a, String l, String u, String lp){
        return String.format("INSERT INTO medic(firstName,age,lastName,userName,password,status) VALUES ('%s','%d','%s','%s','%s','0')", f, Integer.parseInt(a), l, u, lp);
    }
    
    public String retrieveQuery(String u){
        return String.format("Select userName, password, age from medic where userName='%s'", u);
    }
    
    public String retrieveQueryPharmacist(String u){
        return String.format("Select userName, password from tblPharma where userName='%s'", u);
    }
    
    public String updateStatus(){
        return String.format("UPDATE tblPharma SET pharmaIdentity='%s'", 1);
    }
    
    public String status(String u){
        return String.format("UPDATE medic SET status='%s' where username='%s'", 1, u);
    }
}
