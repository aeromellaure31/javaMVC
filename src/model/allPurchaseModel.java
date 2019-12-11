
package model;

public class allPurchaseModel {
    
    public String retrieveMed(String medicineName){
        return String.format("Select idMedicine, medicineName, quantity from tblMedicine where medicineName='%s'", medicineName);
    }
    
    public String retrieveQueryPharmacist(){
        return String.format("Select pharmaIdentity from tblPharma");
    }
    
    public String updateQuantity(int updateQuantity, String medicineName){
        return "UPDATE tblMedicine SET quantity='" + updateQuantity + "' where medicineName='" + medicineName + "'";
    }
    
    public String insertPurchase(String idMed, int quant){
        return String.format("INSERT INTO tblpurchase(idMedicine,quantity) VALUES ('%s','%d')", idMed, quant);
    }
    
    public String insertAll(String idMed, int quant, String date){
        return String.format("INSERT INTO tblAllPurchase(idMedicine,quantityPurchased,date) VALUES ('%s','%d','%s')", idMed, quant, date);
    }
    
    public String countAllPurchase(){
        return String.format("Select COUNT(*) from tblAllPurchase");
    }
    
    public String countPurchase(){
        return String.format("Select COUNT(*) from tblPurchase");
    }
    
    public String retrievePurchase(){
        return String.format("Select m.medicineName, p.quantityPurchased, m.price, p.date FROM tblAllPurchase p LEFT JOIN tblmedicine m ON p.idMedicine = m.idMedicine");
    }
    
    public String retrieveAllPurchase(){
        return String.format("Select m.medicineName, p.quantity, m.price FROM tblpurchase p LEFT JOIN tblmedicine m ON p.idMedicine = m.idMedicine");
    }
    
    public String sumPurchae(){
        return ("SELECT SUM(p.quantity * m.price) AS totalPrice FROM tblpurchase p LEFT JOIN tblmedicine m ON p.idMedicine = m.idMedicine");
    }
}
