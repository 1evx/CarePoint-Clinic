package Data.MedicalRecord;

import Data.Data;

public class MedicalRecordData extends Data {
    private String date;
    private String time;
    private String meridiem;
    private String description;
    private String dose;
    private String doctorId;
    private String doctorName;
    private String patientIcNumber;
    private String patientName;
    private String specialization;

    public MedicalRecordData(String id, String date, String time, String meridiem, String description, String dose, String doctorId, String doctorName, String patientIcNumber, String patientName, String specialization) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.meridiem = meridiem;
        description = description.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\c", ",");
        this.description = description;
        dose = dose.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\c", ",");
        this.dose = dose;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientIcNumber = patientIcNumber;
        this.patientName = patientName;
        this.specialization = specialization;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMeridiem() {
        return meridiem;
    }

    public void setMeridiem(String meridiem) {
        this.meridiem = meridiem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientIcNumber() {
        return patientIcNumber;
    }

    public void setPatientIcNumber(String patientIcNumber) {
        this.patientIcNumber = patientIcNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
