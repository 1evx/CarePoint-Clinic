package Data.Admin;

import Authentication.Cookie;
import File.File;

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
                    .append(admin.getContact())
                    .append("\n");
        }
        File.writeFile("admin.txt", stringBuilder.toString());
    }

    // admin login program
    public void AdminLogin(String username, String password, JFrame frame) {
        for (AdminData admin : adminDataList) {
            if (admin.login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Login Successful. Welcome " + admin.getName());
                //burh
                frame.dispose();
                Cookie.setCookie(admin);
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Username or Password Wrong. Login Failed");
    }
}
