package Data.Appointment;

import Data.Patient.PatientData;
import Data.Patient.Patients;
import Data.Sequence.Sequences;
import Data.TimeSlot.TimeSlotData;
import Data.TimeSlot.TimeSlots;
import File.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// changed wh
public class Appointments {
    private List<String> appointmentHeaders;
    private final List<AppointmentData> appointmentList = new ArrayList<>();

    public static  void main(String[] args) {
        Appointments appointments = new Appointments();
        System.out.println(appointments.getAppointmentList().getFirst().getDate());
    }

    public Appointments() {
        File.readFile(this);
    }

    public void addAppointmentHeader(String header) {
        appointmentHeaders = Arrays.asList(header.split(","));
    }

    public void addAppointment(AppointmentData appointment) {
        appointmentList.add(appointment);
    }

    public List<String> getAppointmentHeaders() {
        return appointmentHeaders;
    }

    public List<AppointmentData> getAppointmentList() {
        return appointmentList;
    }

    public List<AppointmentData> queryAppointment(String queryId, String queryDate, String queryTime, String queryMeridiem, String queryPatientIcNumber, String querySpecialization, String queryType, String queryStatus, String queryTimeSlotId) {

        return appointmentList.stream()
                .filter(appointment -> queryId == null || appointment.getId().equals(queryId))
                .filter(appointment -> queryDate == null || appointment.getDate().equals(queryDate))
                .filter(appointment -> queryTime == null || appointment.getTime().equals(queryTime))
                .filter(appointment -> queryMeridiem == null || appointment.getMeridiem().equals(queryMeridiem))
                .filter(appointment -> queryPatientIcNumber == null || appointment.getPatientIcNumber().equals(queryPatientIcNumber))
                .filter(appointment -> querySpecialization == null || appointment.getSpecialization().equals(querySpecialization))
                .filter(appointment -> queryType == null || appointment.getType().equals(queryType))
                .filter(appointment -> queryStatus == null || appointment.getStatus().equals(queryStatus))
                .filter(appointment -> queryTimeSlotId == null || appointment.getTimeSlotId().equals(queryTimeSlotId))
                .toList();
    }

    public void cancelAppointment(String cancelledId, String cancelledTimeSlotId) {
        List<AppointmentData> deletedAppointment = queryAppointment(cancelledId, null, null, null, null, null, null, null, null);
        deletedAppointment.getFirst().setStatus("cancelled");
        TimeSlots timeSlots = new TimeSlots();
        TimeSlotData queriedTimeSlot = timeSlots.queryTimeSlot(cancelledTimeSlotId, null, null, null, null, null, null, null).getFirst();
        queriedTimeSlot.setStatus("available");
        timeSlots.updateTimeSlots();
        updateAppointment();
    }

    public void deleteAppointment (String deletedId, String deletedTimeSlotId) {
        List<AppointmentData> deletedAppointment = queryAppointment(deletedId, null, null, null, null, null, null, null, null);
        appointmentList.removeAll(deletedAppointment);
        TimeSlots timeSlots = new TimeSlots();
        TimeSlotData queriedTimeSlot = timeSlots.queryTimeSlot(deletedTimeSlotId, null, null, null, null, null, null, null).getFirst();
        queriedTimeSlot.setStatus("available");
        timeSlots.updateTimeSlots();
        updateAppointment();
    }

    public String editAppointment (String id, String date, String time, String meridiem, String patientIcNumber, String specialization, String type, String timeSlotId) {
        StringBuilder stringBuilder = new StringBuilder();

        if (date == null) {
            stringBuilder.append("Please select a date");
        }

        if (time.equals("- none -")) {
            stringBuilder.append("Please select time.").append("\n");
        }

        if (meridiem.equals("none -")) {
            stringBuilder.append("Please select meridiem.").append("\n");
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
        }

        if (specialization.equals("- none -")) {
            stringBuilder.append("Please select specialization.").append("\n");
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }

        TimeSlots timeSlots = new TimeSlots();
        List<TimeSlotData> queriedTimeSlot = timeSlots.queryTimeSlot(null, date, time, meridiem, null, null, specialization, "available");
        if (queriedTimeSlot.isEmpty()) {
            return "Selected time slot is not available.\n";
        }
        List<TimeSlotData> currentTimeSlot = timeSlots.queryTimeSlot(timeSlotId, null, null, null, null, null, null, null);
        currentTimeSlot.getFirst().setStatus("available");
        String editedTimeSlotId = queriedTimeSlot.getFirst().getId();
        queriedTimeSlot.getFirst().setStatus("reserved");
        timeSlots.updateTimeSlots();

        AppointmentData editedAppointment = queryAppointment(id, null, null, null, null, null, null, null, null).getFirst();
        editedAppointment.setDate(date);
        editedAppointment.setTime(time);
        editedAppointment.setMeridiem(meridiem);
        editedAppointment.setPatientIcNumber(patientIcNumber);
        editedAppointment.setSpecialization(specialization);
        editedAppointment.setType(type);
        editedAppointment.setTimeSlotId(editedTimeSlotId);
        updateAppointment();
        return "Walk-In Appointment data edit successful.";
    }

    public String addNewAppointment(String id, String date, String time, String meridiem, String patientIcNumber, String specialization, String type) {
        StringBuilder stringBuilder = new StringBuilder();
        String timeSlotId;

        if (time.equals("- none -")) {
            stringBuilder.append("Please select time").append("\n");
        }

        if (meridiem.equals("- none -")) {
            stringBuilder.append("Please select meridiem").append("\n");
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
        }

        if (specialization.equals("- none -")) {
            stringBuilder.append("Please select specialization").append("\n");
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }

        TimeSlots timeSlots = new TimeSlots();
        List<TimeSlotData> queriedTimeSlot = timeSlots.queryTimeSlot(null, date, time, meridiem, null, null, specialization, "available");
        if (queriedTimeSlot.isEmpty()) {
            return "Selected time slot is not available.";
        }
        timeSlotId = queriedTimeSlot.getFirst().getId();
        queriedTimeSlot.getFirst().setStatus("reserved");
        timeSlots.updateTimeSlots();

        addReservedAppointment(date, time, meridiem, patientIcNumber, specialization, type, "active", timeSlotId);

        return "Walk-In Appointment add successful.";
    }

    public void updateAppointment() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", appointmentHeaders)).append("\n");
        for (AppointmentData appointment : appointmentList) {
            stringBuilder.append(appointment.getId()).append(",")
                    .append(appointment.getDate()).append(",")
                    .append(appointment.getTime()).append(",")
                    .append(appointment.getMeridiem()).append(",")
                    .append(appointment.getPatientIcNumber()).append(",")
                    .append(appointment.getSpecialization()).append(",")
                    .append(appointment.getType()).append(",")
                    .append(appointment.getStatus()).append(",")
                    .append(appointment.getTimeSlotId())
                    .append("\n");
        }
        File.writeFile("appointment.txt", stringBuilder.toString());
    }

    public void addReservedAppointment(String date, String time, String meridiem, String patientIcNumber, String specialization, String type, String status, String timeSlotId) {
        String id;
        Sequences sequences = new Sequences();
        id = sequences.querySequenceId("appointment.txt");
        String newAppointment = id + "," + date + "," + time + "," + meridiem + "," + patientIcNumber + "," + specialization + "," + type + "," + status + "," + timeSlotId;
        File.appendFile("appointment.txt", newAppointment);
        sequences.updateId("appointment.txt");
    }

}

//changed wh
