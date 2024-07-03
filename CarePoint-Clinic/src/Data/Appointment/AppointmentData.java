package Data.Appointment;

import Data.Data;

//changed wh
public class AppointmentData extends Data {
    private String date;
    private String time;
    private String meridiem;
    private String patientIcNumber;
    private String specialization;
    private String type;
    private String status;
    private String timeSlotId;

    public AppointmentData(String id, String date, String time, String meridiem, String patientIcNumber, String specialization, String type, String status, String timeSlotId) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.meridiem = meridiem;
        this.patientIcNumber = patientIcNumber;
        this.specialization = specialization;
        this.type = type;
        this.status = status;
        this.timeSlotId = timeSlotId;
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

    public String getPatientIcNumber() {
        return patientIcNumber;
    }

    public void setPatientIcNumber(String patientIcNumber) {
        this.patientIcNumber = patientIcNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }
}
// changed wh
