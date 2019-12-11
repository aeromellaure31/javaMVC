
package model;

public class allDashModel {
    
    public String retrieveQuery(String medName){
        return String.format("Select medicineName, brandName, genericName, price from tblMedicine where medicineType='%s'", medName);
    }
    
    public String retrieveMed(String medName){
        return String.format("Select COUNT(medicineName) from tblMedicine where medicineType='%s'", medName);
    }
    
    public String delete(){
        return ("DELETE FROM tblpurchase");
    }
    
    public String updateStatus(){
        return String.format("UPDATE tblPharma SET pharmaIdentity='%s'", 0);
    }
    
    public String status(){
        return String.format("SELECT * from medic where status='1'");
    }
}
