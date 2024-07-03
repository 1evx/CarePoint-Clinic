package Data.MedicalRecord;

import Data.Patient.PatientData;
import Data.Patient.Patients;
import Data.Sequence.Sequences;
import File.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicalRecords {
    private List<String> medicalRecordHeader;
    private final List<MedicalRecordData> medicalRecordList = new ArrayList<>();

    public MedicalRecords() {
        File.readFile(this);
    }

    public void addMedicalRecordHeader(String header) {
        medicalRecordHeader = Arrays.asList(header.split(","));
    }

    public void addMedicalRecord(MedicalRecordData medicalRecord) {
        medicalRecordList.add(medicalRecord);
    }

    public List<String> getMedicalRecordHeader() {
        return medicalRecordHeader;
    }

    public List<MedicalRecordData> getMedicalRecordList() {
        return medicalRecordList;
    }

    public List<MedicalRecordData> queryMedicalRecord(String queryId, String queryDate, String queryTime, String queryMeridiem, String queryDoctorId, String queryPatientIcNumber, String querySpecialization) {
        return medicalRecordList.stream()
                .filter(medicalRecord -> queryId == null || medicalRecord.getId().equals(queryId))
                .filter(medicalRecord -> queryDate == null || medicalRecord.getDate().equals(queryDate))
                .filter(medicalRecord -> queryTime == null || medicalRecord.getTime().equals(queryTime))
                .filter(medicalRecord -> queryMeridiem == null || medicalRecord.getMeridiem().equals(queryMeridiem))
                .filter(medicalRecord -> queryDoctorId == null || medicalRecord.getDoctorId().equals(queryDoctorId))
                .filter(medicalRecord -> queryPatientIcNumber == null || medicalRecord.getPatientIcNumber().equals(queryPatientIcNumber))
                .filter(medicalRecord -> querySpecialization == null || medicalRecord.getSpecialization().equals(querySpecialization))
                .toList();
    }

    public void deleteMedicalRecords(String deletedId) {
        List<MedicalRecordData> deletedMedicalRecords = queryMedicalRecord(deletedId, null, null, null, null, null, null);
        medicalRecordList.removeAll(deletedMedicalRecords);
        updateMedicalRecord();
    }

    public String editMedicalRecord(String id, String date, String time, String meridiem, String description, String dose, String patientIcNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        String patientName = null;

        if (time.equals("- none -")) {
            stringBuilder.append("Please select time");
        }

        if (meridiem.equals("-none -")) {
            stringBuilder.append("Please select meridiem");
        }

        if (patientIcNumber.length() != 12) {
            stringBuilder.append("Invalid IC number.").append("\n");
        }
        else {
            for (int i=0; i < patientIcNumber.length(); i++) {
                if (!Character.isDigit(patientIcNumber.charAt(i))) {
                    stringBuilder.append("Invalid IC number.").append("\n");
                    break;
                }
            }
            List<PatientData> queriedPatient = new Patients().queryPatient(null, null, patientIcNumber, null, null, null);
            if (queriedPatient.isEmpty()) {
                stringBuilder.append("Patient Ic Number does not exist in the system.").append("\n");
            }
            else {
                patientName = queriedPatient.getFirst().getName();
            }
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }

        description = description.replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace(",", "\\c");

        dose = dose.replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace(",", "\\c");

        MedicalRecordData editedMedicalRecord = queryMedicalRecord(id, null, null, null, null, null, null).getFirst();
        editedMedicalRecord.setDate(date);
        editedMedicalRecord.setTime(time);
        editedMedicalRecord.setMeridiem(meridiem);
        editedMedicalRecord.setDescription(description);
        editedMedicalRecord.setDose(dose);
        editedMedicalRecord.setPatientIcNumber(patientIcNumber);
        editedMedicalRecord.setPatientName(patientName);
        updateMedicalRecord();
        return "Medical Record updated successfully";
    }

    public String addNewMedicalRecord(String id, String date, String time, String meridiem, String description, String dose, String doctorId, String doctorName, String patientIcNumber, String specialization) {
        StringBuilder stringBuilder = new StringBuilder();
        String patientName = null;
        if (time.equals("- none -")) {
            stringBuilder.append("Please select time");
        }

        if (meridiem.equals("-none -")) {
            stringBuilder.append("Please select meridiem");
        }

        if (patientIcNumber.length() != 12) {
            stringBuilder.append("Invalid IC number.").append("\n");
        }
        else {
            for (int i=0; i < patientIcNumber.length(); i++) {
                if (!Character.isDigit(patientIcNumber.charAt(i))) {
                    stringBuilder.append("Invalid IC number.").append("\n");
                    break;
                }
            }
            List<PatientData> queriedPatient = new Patients().queryPatient(null, null, patientIcNumber, null, null, null);
            if (queriedPatient.isEmpty()) {
                stringBuilder.append("Patient Ic Number does not exist in the system.").append("\n");
            }
            else {
                patientName = queriedPatient.getFirst().getName();
            }
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }

        description = description.replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace(",", "\\c");

        dose = dose.replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace(",", "\\c");

        Sequences sequences = new Sequences();
        id = sequences.querySequenceId("medicalRecord.txt");
        String newMedicalRecord = id + "," + date + "," + time + "," + meridiem + "," + description + "," + dose + "," + doctorId + "," + doctorName + "," + patientIcNumber + "," + patientName + "," + specialization;
        File.appendFile("medicalRecord.txt", newMedicalRecord);
        sequences.updateId("medicalRecord.txt");

        return "Medical Record add successful";
    }

    public void updateMedicalRecord() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", medicalRecordHeader)).append("\n");
        for (MedicalRecordData medicalRecord : medicalRecordList) {
            stringBuilder.append(medicalRecord.getId()).append(",")
                    .append(medicalRecord.getDate()).append(",")
                    .append(medicalRecord.getTime()).append(",")
                    .append(medicalRecord.getMeridiem()).append(",")
                    .append(medicalRecord.getDescription()).append(",")
                    .append(medicalRecord.getDose()).append(",")
                    .append(medicalRecord.getDoctorId()).append(",")
                    .append(medicalRecord.getDoctorName()).append(",")
                    .append(medicalRecord.getPatientIcNumber()).append(",")
                    .append(medicalRecord.getPatientName()).append(",")
                    .append(medicalRecord.getSpecialization()).append("\n");
        }
        File.writeFile("medicalRecord.txt", stringBuilder.toString());
    }
}
