package Data.Admin;

import Authentication.Cookie;
import Data.Patient.PatientData;
import File.File;
import Ui.AdminUI.AdminMainUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Admins {
    private List<String> adminHeaders;
    private final List<AdminData> adminDataList = new ArrayList<>();

    // read admin.txt and add all AdminData into adminDataList. This related addAdminHeaders method and addAdminData method
    public Admins() {
        File.readFile(this);
    }

    public void addAdminHeaders (String headers) {
        adminHeaders = Arrays.asList(headers.split(","));
    }

    public void addAdminData (AdminData adminData) {
        adminDataList.add(adminData);
    }

    public List<String> getAdminHeaders() {
        return adminHeaders;
    }

    public List<AdminData> getAdminsList() {
        return adminDataList;
    }

    // find specific admin that fulfill the requirement, no selected for null
    public List<AdminData> queryAdmin(String queryId, String queryName, String queryIcNumber, String queryContact) {
        return adminDataList.stream()
                .filter(admin -> queryId == null || admin.getId().equals(queryId))
                .filter(admin -> queryName == null || admin.getName().equals(queryName))
                .filter(admin -> queryIcNumber == null || admin.getIcNumber().equals(queryIcNumber))
                .filter(admin -> queryContact == null || admin.getContact().equals(queryContact))
                .toList();
    }

    public void deleteAdmin (String deletedId) {
        List<AdminData> deletedAdmin = queryAdmin(deletedId, null, null, null);
        adminDataList.removeAll(deletedAdmin);
        updateAdmin();
    }

    // overwrite admin.txt with the edited admin info
    public void updateAdmin() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.join(",", adminHeaders)).append("\n");
        for (AdminData admin : adminDataList) {
            stringBuilder.append(admin.getId()).append(",")
                    .append(admin.getName()).append(",")
                    .append(admin.getIcNumber()).append(",")
                    .append(admin.getPass()).append(",")
                    .append(admin.getContact())
                    .append("\n");
        }
        File.writeFile("admin.txt", stringBuilder.toString());
    }

    public Object[] checkAdminIc(String icNumber) {
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

        List<AdminData> queriedAdmin = queryAdmin(null, null, icNumber, null);
        if (queriedAdmin.isEmpty()) {
            statusMessage[0] = "Admin IC Number does not exist in the system.";
        }
        else {
            statusMessage[1] = queriedAdmin.getFirst();
        }
        return statusMessage;
    }

    // admin login program
    public void AdminLogin(String username, String password, JFrame frame) {
        for (AdminData admin : adminDataList) {
            if (admin.login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful. Welcome " + admin.getName());
                Cookie.setCookie(admin);
                new AdminMainUI();
                frame.dispose();
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Username or Password Wrong. Login Failed");
    }
}
