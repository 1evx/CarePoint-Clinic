package Data.Payment;

import Data.Data;

public class PaymentData extends Data{
    private String date;
    private String time;
    private String meridiem;
    private String patientICNum;
    private String patientName;
    private String appointmentId;
    private String amount;
    private String[] items;
    private String[] quantities;
    private String status;

    public PaymentData(String id, String date, String time, String meridiem, String patientICNum, String patientName, String appointmentId, String amount, String items, String quantities, String status){
        this.id = id;
        this.date = date;
        this.time = time;
        this.meridiem = meridiem;
        this.patientICNum = patientICNum;
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.items = items.split(":");
        this.quantities = quantities.split(":");
        this.status = status;
    }

    public static void main(String[] args) {
        new Payments();
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

    public String getPatientICNum() {
        return patientICNum;
    }

    public void setPatientICNum(String patientICNum) {
        this.patientICNum = patientICNum;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items.split(":");
    }

    public String[] getQuantities() {
        return quantities;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities.split(":");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
