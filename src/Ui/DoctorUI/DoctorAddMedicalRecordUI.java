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

public class DoctorAddMedicalRecordUI extends JDialog {
    String icNumber;
    String desc;
    String dose;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    private JPanel MainPanel;
    private JTextField IcNumberInputField;
    private JTextArea descField;
    private JButton addButton;
    private JTextArea doseField;

    public DoctorAddMedicalRecordUI(JFrame parent) {
        super(parent, "Add Patient Medical Record", ModalityType.APPLICATION_MODAL);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        setSize((int) Math.floor(screenWidth*0.4f), (int) Math.floor(screenHeight*0.5f));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                icNumber = IcNumberInputField.getText();
                List<PatientData> searchedPatient = new Patients().queryPatient(null, null, icNumber, null, null, null);
                if (searchedPatient.isEmpty()) {
                    JOptionPane.showMessageDialog( MainPanel, "Patient does not exist");
                    return;
                }
                LocalDateTime currentDate = LocalDateTime.now();
                String date = currentDate.format(dateFormatter);
                String time = (currentDate.format(timeFormatter)).split(" ")[0];
                String meridiem = ((currentDate.format(timeFormatter)).split(" ")[1]).replace("am", "a.m.").replace("pm", "p.m.");
                desc = descField.getText();
                dose = doseField.getText();
                String doctorId = Cookie.getDoctorCookie().getId();
                String doctorName = Cookie.getDoctorCookie().getName();
                String specialization = Cookie.getDoctorCookie().getSpecialization();
                MedicalRecords medicalRecords = new MedicalRecords();
                String addMessage = medicalRecords.addNewMedicalRecord(null, date, time, meridiem, desc, dose, doctorId, doctorName, icNumber, specialization);
                JOptionPane.showMessageDialog(MainPanel, addMessage);
                if (addMessage.equals("Medical Record add successful")) {
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
