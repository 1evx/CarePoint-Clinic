package Data.Medicine;

import Data.Sequence.Sequences;
import File.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Medicines {
    private List<String> medicineHeader;
    private final List<MedicineData> medicineList = new ArrayList<>();

    public Medicines() {
        File.readFile(this);
    }

    public List<String> getMedicineHeader() {
        return medicineHeader;
    }

    public void addMedicineHeader(String header) {
        medicineHeader = Arrays.asList(header.split(","));
    }

    public List<MedicineData> getMedicineList() {
        return medicineList;
    }

    public void addMedicineList(MedicineData medicine) {
        medicineList.add(medicine);
    }

    public List<MedicineData> queryMedicine(String queryId, String queryDescription, String queryPrice, String querySupplierName) {
        return medicineList.stream()
                .filter(medicine -> queryId == null || medicine.getId().equals(queryId))
                .filter(medicine -> queryDescription == null || medicine.getDescription().equals(queryDescription))
                .filter(medicine -> queryPrice == null || medicine.getPrice().equals(queryPrice))
                .filter(medicine -> querySupplierName == null || medicine.getSupplierName().equals(querySupplierName))
                .toList();
    }

    public void deleteMedicine(String deletedId) {
        List<MedicineData> deletedMedicine = queryMedicine(deletedId, null, null, null);
        medicineList.removeAll(deletedMedicine);
        updateMedicine();
    }

    public String editMedicine(String id, String description, String price, String supplierName) {
        if (description == null || description.isBlank()) {
            return "Description must be filled up.";
        }
        if (supplierName == null || supplierName.isBlank()) {
            supplierName = "Null";
        }

        MedicineData queriedMedicine = queryMedicine(id, null, null, null).getFirst();
        queriedMedicine.setDescription(description);
        queriedMedicine.setPrice(price);
        queriedMedicine.setSupplierName(supplierName);
        updateMedicine();
        return "Medicine updated successful.";
    }

    public String addNewMedicine(String id, String description, String price, String supplierName) {
        if (description == null || description.isBlank()) {
            return "Description must be filled up.";
        }
        if (supplierName == null || supplierName.isBlank()) {
            supplierName = "Null";
        }
        Sequences sequences = new Sequences();
        id = sequences.querySequenceId("medicine.txt");
        String newMedicalRecord = id + "," + description + "," + price + "," + supplierName;
        File.appendFile("medicine.txt", newMedicalRecord);
        sequences.updateId("medicine.txt");

        return "Medicine Information add successful.";
    }

    public void updateMedicine() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", medicineHeader)).append("\n");
        for (MedicineData medicine : medicineList) {
            stringBuilder.append(medicine.getId()).append(",")
                    .append(medicine.getDescription()).append(",")
                    .append(medicine.getPrice()).append(",")
                    .append(medicine.getSupplierName()).append("\n");
        }
        File.writeFile("medicine.txt", stringBuilder.toString());
    }
}
