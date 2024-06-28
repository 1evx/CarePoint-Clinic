package Data.Payment;

import Data.Sequence.Sequences;
import File.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Payments {
    private List<String> paymentHeader;
    private final List<PaymentData> paymentList = new ArrayList<>();

    public Payments() {
        File.readFile(this);
    }

    public List<String> getPaymentHeader() {
        return paymentHeader;
    }

    public void addPaymentHeader(String header) {
        this.paymentHeader = Arrays.asList(header.split(","));
    }

    public List<PaymentData> getPaymentList() {
        return paymentList;
    }

    public void addPayment(PaymentData payment) {
        paymentList.add(payment);
    }

    public List<PaymentData> queryPayment(String queryId, String queryDate, String queryTime, String queryMeridiem, String queryPatientIcNumber, String queryAppointmentId, String queryStatus) {
        return paymentList.stream()
                .filter(payment -> queryId == null || payment.getId().equals(queryId))
                .filter(payment -> queryDate == null || payment.getDate().equals(queryDate))
                .filter(payment -> queryTime == null || payment.getTime().equals(queryTime))
                .filter(payment -> queryMeridiem == null || payment.getMeridiem().equals(queryMeridiem))
                .filter(payment -> queryPatientIcNumber == null || payment.getPatientICNum().equals(queryPatientIcNumber))
                .filter(payment -> queryAppointmentId == null || payment.getAppointmentId().equals(queryAppointmentId))
                .filter(payment -> queryStatus == null || payment.getStatus().equals(queryStatus))
                .toList();
    }

    public void cancelPayment(String cancelledId) {
        List<PaymentData> cancelledPayment = queryPayment(cancelledId, null, null, null, null, null, null);
        cancelledPayment.getFirst().setStatus("cancelled");
        updatePayment();
    }

    public void addNewPayment(String date, String time, String meridiem, String patientIcNumber, String patientName, String appointmentId, String amount, String[] items, String[] quantities, String status) {
        Sequences sequences = new Sequences();
        String id = sequences.querySequenceId("payment.txt");
        String newPayment = id + "," + date + "," + time + "," + meridiem + "," + patientIcNumber + "," + patientName + "," + appointmentId + "," + amount + "," + String.join(":",items) + "," + String.join(":",quantities) + "," + status;
        File.appendFile("payment.txt", newPayment);
        sequences.updateId("payment.txt");
    }

    public void updatePayment() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", paymentHeader)).append("\n");
        for (PaymentData payment : paymentList) {
            stringBuilder.append(payment.getId()).append(",")
                    .append(payment.getDate()).append(",")
                    .append(payment.getTime()).append(",")
                    .append(payment.getMeridiem()).append(",")
                    .append(payment.getPatientICNum()).append(",")
                    .append(payment.getPatientName()).append(",")
                    .append(payment.getAppointmentId()).append(",")
                    .append(payment.getAmount()).append(",")
                    .append(String.join(":", payment.getItems())).append(",")
                    .append(String.join(":", payment.getQuantities())).append(",")
                    .append(payment.getStatus())
                    .append("\n");
        }
        File.writeFile("payment.txt", stringBuilder.toString());
    }
}
