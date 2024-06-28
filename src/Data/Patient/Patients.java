package Data.Patient;

import Authentication.Authentication;
import Authentication.Cookie;
import Data.Sequence.Sequences;
import File.File;
import Ui.PatientUI.PatientUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Patients {
    private List<String> patientHeaders;
    private final List<PatientData> patientDataList = new ArrayList<>();

    public Patients () {
        File.readFile(this);
    }

    public void addPatientHeaders(String headers) {
        patientHeaders = Arrays.asList(headers.split(","));
    }

    public void addPatientData (PatientData patientData) {
        patientDataList.add(patientData);
    }

    public List<String> getPatientHeaders() {
        return patientHeaders;
    }

    public List<PatientData> getPatientList() {
        return patientDataList;
    }

    public List<PatientData> queryPatient(String queryId, String queryName, String queryIcNumber, String queryGender, String queryDob, String queryContact) {
        return patientDataList.stream()
                .filter(patient -> queryId == null || patient.getId().equals(queryId))
                .filter(patient -> queryName == null || patient.getName().equals(queryName))
                .filter(patient -> queryIcNumber == null || patient.getIcNumber().equals(queryIcNumber))
                .filter(patient -> queryGender == null || patient.getGender().equals(queryGender))
                .filter(patient -> queryDob == null || patient.getDob().equals(queryDob))
                .filter(patient -> queryContact == null || patient.getContact().equals(queryContact))
                .toList();
    }

    public void deletePatient (String deletedId) {
        List<PatientData> deletedPatient = queryPatient(deletedId, null, null, null, null, null);
        patientDataList.removeAll(deletedPatient);
        updatePatient();
    }

    // changed wh
    public String editPatient(String id, String name, String icNumber, String gender, String dob, String contact) {
        StringBuilder stringBuilder = new StringBuilder();
        PatientData editedPatient = queryPatient(id, null, null, null, null, null).getFirst();
        if (name == null || name.isBlank()) {
            stringBuilder.append("Name cannot be blank.").append("\n");
        }

        if (icNumber.length() != 12) {
            stringBuilder.append("Invalid IC number.").append("\n");
        }
        else {
            for (int i=0; i < icNumber.length(); i++) {
                if (!Character.isDigit(icNumber.charAt(i))) {
                    stringBuilder.append("Invalid IC number.").append("\n");
                    break;
                }
            }
            String queriedIcNumber = new PatientData(null, null, icNumber, null, null, null, null).checkIcNumber();
            if (queriedIcNumber != null) {
                if (!editedPatient.getIcNumber().equals(icNumber)) {
                    stringBuilder.append("IC Number already exist in the system").append("\n");
                }
            }
        }

        if (!gender.equals("male") && !gender.equals("female")) {
            stringBuilder.append("Please select gende.r").append("\n");
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }
        stringBuilder.append("Patient data edit successful.");
        editedPatient.setName(name);
        editedPatient.setIcNumber(icNumber);
        editedPatient.setGender(gender);
        editedPatient.setDob(dob);
        editedPatient.setContact(contact);
        updatePatient();
        return stringBuilder.toString();
    }

    public String registerPatient(String id, String name, String icNumber, String pass, String gender, String dob, String contact) {
        StringBuilder stringBuilder = new StringBuilder();
        if (name == null || name.isBlank()) {
            stringBuilder.append("Name cannot be blank.").append("\n");
        }

        if (icNumber.length() != 12) {
            stringBuilder.append("Invalid IC number.").append("\n");
        }
        else {
            for (int i=0; i < icNumber.length(); i++) {
                if (!Character.isDigit(icNumber.charAt(i))) {
                    stringBuilder.append("Invalid IC number.").append("\n");
                    break;
                }
            }
            String queriedIcNumber = new PatientData(null, null, icNumber, null, null, null, null).checkIcNumber();
            if (queriedIcNumber != null) {
                stringBuilder.append("IC Number already exist in the system").append("\n");
            }
        }

        if (!gender.equals("male") && !gender.equals("female")) {
            stringBuilder.append("Please select gender").append("\n");
        }

        if (dob == null) {
            stringBuilder.append("Please select date of birth").append("\n");
        }

        try {
            Integer.parseInt(contact);
        } catch (NumberFormatException e) {
            stringBuilder.append("Invalid Contact number format.").append("\n");
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }
        stringBuilder.append("Patient data register successful.");
        Sequences sequences = new Sequences();
        id = sequences.querySequenceId("patient.txt");
        pass = Authentication.hashPassword(pass);
        String newUserInfo = id + "," + name + "," + icNumber + "," + pass + "," +gender + "," + dob+ "," + contact;
        File.appendFile("patient.txt", newUserInfo);
        sequences.updateId("patient.txt");
        return stringBuilder.toString();
    }
    // changed wh

    public void updatePatient() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", patientHeaders)).append("\n");
        for (PatientData patient : patientDataList) {
            stringBuilder.append(patient.getId()).append(",")
                    .append(patient.getName()).append(",")
                    .append(patient.getIcNumber()).append(",")
                    .append(patient.getPass()).append(",")
                    .append(patient.getGender()).append(",")
                    .append(patient.getDob()).append(",")
                    .append(patient.getContact())
                    .append("\n");
        }
        File.writeFile("patient.txt", stringBuilder.toString());
    }

    public Object[] checkPatientIc(String icNumber) {
        Object[] statusMessage = new Object[2];
        if (icNumber.length() != 12) {
            statusMessage[0] = "Invalid IC number.";
        }
        for (int i=0; i < icNumber.length(); i++) {
            if (!Character.isDigit(icNumber.charAt(i))) {
                statusMessage[0] = "Invalid IC number.\n";
                break;
            }
        }

        if (statusMessage[0] != null) {
            return statusMessage;
        }

        List<PatientData> queriedPatient = queryPatient(null, null, icNumber, null, null, null);
        if (queriedPatient.isEmpty()) {
            statusMessage[0] = "Patient IC Number does not exist in the system.";
        }
        else {
            statusMessage[1] = queriedPatient.getFirst();
        }
        return statusMessage;
    }

    public void PatientLogin(String username, String password, JFrame frame) {
        for (PatientData patient : patientDataList) {
            if (patient.login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful. Welcome " + patient.getName());
                Cookie.setCookie(patient);
                new PatientUI();
                frame.dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Username or Password Wrong. Login Failed");
    }
}
