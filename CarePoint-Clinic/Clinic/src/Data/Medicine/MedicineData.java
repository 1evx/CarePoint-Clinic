package Data.Medicine;

import Data.Data;

public class MedicineData extends Data{
    private String description;
    private String price;
    private String supplierName;

    public MedicineData(String id, String description, String price, String supplierName) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.supplierName = supplierName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
