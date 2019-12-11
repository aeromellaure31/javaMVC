
package model;

public class medicineModel {
    
    public String insertMed(String medType, String medName, String brName, String genName, String price, String quantity){
        return String.format("INSERT INTO tblMedicine(medicineName,brandName,"
                + "genericName,quantity,price,medicineType)"
                + " VALUES ('%s', '%s', '%s', '%d', '%d', '%s')", medName, brName, genName, Integer.parseInt(quantity), Integer.parseInt(price), medType);
    }
    
    public String fetchAllMedicine(){
        return ("Select medicineName from tblmedicine");
    }
    
    public String delMed(String medicineName){
        return String.format("DELETE FROM tblMedicine WHERE medicineName='%s'", medicineName);
    }
    
    public String searchMed(String searched){
        return String.format("SELECT * FROM tblMedicine WHERE medicineName='%s'", searched);
    }
    
    public String outcome(String searched, String medicineType, String medicineName, String brName, String genericName, String price, String quant){
        return String.format("UPDATE tblmedicine SET medicineName='%s' "
                + ",brandName='%s' ,genericName='%s' ,price='%d',quantity='%d' "
                + ",medicineType='%s' WHERE medicineName='%s'"
                + "", medicineName, brName, genericName, Integer.parseInt(price), Integer.parseInt(quant), medicineType, searched);
    }
}
