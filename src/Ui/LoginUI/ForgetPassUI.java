package Ui.LoginUI;

import Data.Admin.Admins;
import Data.Doctor.Doctors;
import Data.Patient.PatientData;
import Data.Patient.Patients;
import Data.UserData;
import Ui.Tool.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgetPassUI extends JDialog {

    private JPanel MainPanel;
    private JTextField icField;
    private JTextField contactField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JPanel resetPassBtnPanel;

    public ForgetPassUI(JFrame parent, String role) {
        super(parent, "Forget Password", ModalityType.APPLICATION_MODAL);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.3f), (int) Math.floor(screenHeight*0.4f));
        setButton(role);
        setContentPane(MainPanel);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setButton(String role) {
        JButton resetPassBtn = RoundedButton.createRoundedButton("Reset Password", 30, 2, new Color(50, 15, 150));
        resetPassBtnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        resetPassBtnPanel.add(resetPassBtn);
        JDialog dialog = this;
        resetPassBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String icNum = icField.getText().trim();
               String contactNum = contactField.getText().trim();
               String newPassword = new String(newPasswordField.getPassword()).trim();
               String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

               Object users = null;
               Object[] checkICStatus = switch (role) {
                   case "patient" -> {
                       Patients patients = new Patients();
                       users = patients;
                       yield patients.checkPatientIc(icNum);
                   }
                   case "doctor" -> {
                       Doctors doctors = new Doctors();
                       users = doctors;
                       yield doctors.checkDoctorIc(icNum);
                   }
                   case "admin" -> {
                       Admins admins = new Admins();
                       users = admins;
                       yield admins.checkAdminIc(icNum);
                   }
                   default -> new Object[2];
               };
               if (checkICStatus[1] == null) {
                   JOptionPane.showMessageDialog(null, checkICStatus[0]);
                   return;
               }
               UserData queriedUser = (UserData) checkICStatus[1];
               if (!queriedUser.getContact().equals(contactNum)) {
                   JOptionPane.showMessageDialog(null, "Contact number does not match");
                   return;
               }
               String changePassMessage = queriedUser.setPass(newPassword, confirmPassword);
               if (changePassMessage != null) {
                   JOptionPane.showMessageDialog(null, changePassMessage);
                   return;
               }
               if (users instanceof Patients patients) {
                   patients.updatePatient();
               }
               else if (users instanceof Doctors doctors) {
                   doctors.updateDoctor();
               }
               else if (users instanceof Admins admins) {
                   admins.updateAdmin();
               }
               JOptionPane.showMessageDialog(null, "Password changed successfully");
               dialog.dispose();
           }
        });
    }
}
