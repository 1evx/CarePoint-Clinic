package Ui.DoctorUI;

import Authentication.Cookie;
import Data.MedicalRecord.MedicalRecords;
import Data.Patient.PatientData;
import Data.Patient.Patients;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DoctorEditMedicalRecordUI extends JDialog {
    String icNumber;
    String desc;
    String dose;

    private JPanel MainPanel;
    private JTextField IcNumberInputField;
    private JTextArea descField;
    private JTextArea doseField;
    private JButton editButton;

    public DoctorEditMedicalRecordUI(JFrame parent, String medicalRecordId, String medicalRecordDesc, String medicalRecordDose, String icNum) {
        super(parent, "Edit Patient Medical Record", Dialog.ModalityType.APPLICATION_MODAL);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        setSize((int) Math.floor(screenWidth * 0.4f), (int) Math.floor(screenHeight * 0.5f));

        IcNumberInputField.setText(icNum);
        descField.setText(medicalRecordDesc);
        doseField.setText(medicalRecordDose);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                icNumber = IcNumberInputField.getText();
                List<PatientData> searchedPatient = new Patients().queryPatient(null, null, icNumber, null, null, null);
                if (searchedPatient.isEmpty()) {
                    JOptionPane.showMessageDialog(MainPanel, "Patient does not exist");
                    return;
                }
                desc = descField.getText();
                dose = doseField.getText();
                MedicalRecords medicalRecords = new MedicalRecords();
                String addMessage = medicalRecords.editMedicalRecord(medicalRecordId, desc, dose, icNumber);
                JOptionPane.showMessageDialog(MainPanel, addMessage);
                if (addMessage.equals("Medical Record updated successfully")) {
                    ((DoctorManageMedicalRecordUI) parent).updateTable();
                    dispose();
                }
            }
        });

        setContentPane(MainPanel);

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
