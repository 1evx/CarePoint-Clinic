package Data.Doctor;

import Authentication.Authentication;
import Authentication.Cookie;
import Data.Sequence.Sequences;
import File.File;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Doctors {
    private List<String> doctorHeaders;
    private final List<DoctorData> doctorDataList = new ArrayList<>();

    public Doctors() {
        File.readFile(this);
    }

    public void addDoctorHeaders(String headers) {
        doctorHeaders = Arrays.asList(headers.split(","));
    }

    public void addDoctorData (DoctorData doctorData) {
        doctorDataList.add(doctorData);
    }

    // changed wh
    public List<String> getDoctorHeaders() {
        return doctorHeaders;
    }
    // changed wh

    public List<DoctorData> getDoctorList() {
        return doctorDataList;
    }

    public List<DoctorData> queryDoctor(String queryId, String queryName, String queryIcNumber, String queryGender, String queryContact, String querySpecialization, String queryEducation, String queryExperienceYear) {
        return doctorDataList.stream()
                .filter(doctor -> queryId == null || doctor.getId().equals(queryId))
                .filter(doctor -> queryName == null || doctor.getName().equals(queryName))
                .filter(doctor -> queryIcNumber == null || doctor.getIcNumber().equals(queryIcNumber))
                .filter(doctor -> queryGender == null || doctor.getGender().equals(queryGender))
                .filter(doctor -> queryContact == null || doctor.getContact().equals(queryContact))
                .filter(doctor -> querySpecialization == null || doctor.getSpecialization().equals(querySpecialization))
                .filter(doctor -> queryEducation == null || doctor.getEducation().equals(queryEducation))
                .filter(doctor -> queryExperienceYear == null || doctor.getExperienceYear().equals(queryExperienceYear))
                .toList();
    }

    // changed wh
    public String editDoctor(String id, String name, String icNumber, String gender, String contact, String specialization, String education, String experienceYear) {
        StringBuilder stringBuilder = new StringBuilder();
        DoctorData editedDoctor = queryDoctor(id, null, null, null, null, null, null, null).getFirst();
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
            String queriedIcNumber = new DoctorData(null, null, icNumber, null, null, null, null, null, null).checkIcNumber();
            if (queriedIcNumber != null) {
                if (!editedDoctor.getIcNumber().equals(queriedIcNumber)) {
                    stringBuilder.append("IC Number already exist in the system").append("\n");
                }
            }
        }

        if (!gender.equals("male") && !gender.equals("female")) {
            stringBuilder.append("Please select gender").append("\n");
        }

        if (specialization == null || specialization.isBlank()) {
            stringBuilder.append("Specialization cannot be blank.").append("\n");
        }

        if (education == null || education.isBlank()) {
            stringBuilder.append("Education cannot be blank.").append("\n");
        }

        try {
            Integer.parseInt(experienceYear);
        } catch (NumberFormatException e) {
            stringBuilder.append("Invalid Experience year format.").append("\n");
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }
        stringBuilder.append("Doctor data edit successful.");
        editedDoctor.setName(name);
        editedDoctor.setIcNumber(icNumber);
        editedDoctor.setGender(gender);
        editedDoctor.setContact(contact);
        editedDoctor.setSpecialization(specialization);
        editedDoctor.setEducation(education);
        editedDoctor.setExperienceYear(experienceYear);
        updateDoctor();
        return stringBuilder.toString();
    }

    public String registerDoctor(String id, String name, String icNumber, String pass, String gender, String contact, String specialization, String education, String experienceYear) {
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
            String queriedIcNumber = new DoctorData(null, null, icNumber, null, null, null, null, null, null).checkIcNumber();
            if (queriedIcNumber != null) {
                stringBuilder.append("IC Number already exist in the system").append("\n");
            }
        }

        if (!gender.equals("male") && !gender.equals("female")) {
            stringBuilder.append("Please select gender").append("\n");
        }

        try {
            Integer.parseInt(contact);
        } catch (NumberFormatException e) {
            stringBuilder.append("Invalid Contact number format.").append("\n");
        }

        if (specialization == null || specialization.isBlank()) {
            stringBuilder.append("Specialization cannot be blank.").append("\n");
        }

        if (education == null || education.isBlank()) {
            stringBuilder.append("Education cannot be blank.").append("\n");
        }

        try {
            Integer.parseInt(experienceYear);
        } catch (NumberFormatException e) {
            stringBuilder.append("Invalid Experience year format.").append("\n");
        }

        if (!stringBuilder.isEmpty()) {
            return stringBuilder.toString();
        }
        stringBuilder.append("Doctor data register successful.");
        Sequences sequences = new Sequences();
        id = sequences.querySequenceId("doctor.txt");
        pass = Authentication.hashPassword(pass);
        String newUserInfo = id + "," + name + "," + icNumber + "," + pass + "," +gender + "," + contact + "," + specialization + "," + education + "," + experienceYear;
        File.appendFile("doctor.txt", newUserInfo);
        sequences.updateId("doctor.txt");
        return stringBuilder.toString();
    }
    // changed wh

    public void deleteDoctor (String deletedId) {
        List<DoctorData> deletedDoctor = queryDoctor(deletedId, null, null, null, null, null, null, null);
        doctorDataList.removeAll(deletedDoctor);
        updateDoctor();
    }

    public void updateDoctor() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", doctorHeaders)).append("\n");
        for (DoctorData doctor : doctorDataList) {
            stringBuilder.append(doctor.getId()).append(",")
                    .append(doctor.getName()).append(",")
                    .append(doctor.getIcNumber()).append(",")
                    .append(doctor.getPass()).append(",")
                    .append(doctor.getGender()).append(",")
                    .append(doctor.getContact()).append(",")
                    .append(doctor.getSpecialization()).append(",")
                    .append(doctor.getEducation()).append(",")
                    .append(doctor.getExperienceYear())
                    .append("\n");
        }
        File.writeFile("doctor.txt", stringBuilder.toString());
    }

    public void deleteDoctor() {

    }

    public void DoctorLogin(String username, String password, JFrame frame) {
        for (DoctorData doctor : doctorDataList) {
            if (doctor.login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful. Welcome " + doctor.getName());
                // render doctor main page
                frame.dispose();
                Cookie.setCookie(doctor);
            }
        }
        JOptionPane.showMessageDialog(frame, "Username or Password Wrong. Login Failed");
    }
}
