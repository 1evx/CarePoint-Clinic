package Ui.DoctorUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorMainUI extends JFrame {
    private JButton uploadDailyScheduleButton;
    private JButton managePatientsMedicalRecordButton;
    private JButton manageIndividualAppointmentButton;
    private JPanel Main;

    DoctorUpdateScheduleUI doctorUpdateScheduleUI;
    DoctorAppoinmentUI doctorAppoinmentUI;
    DoctorManageMedicalRecordUI doctorManageMedicalRecordUI;

    public DoctorMainUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        setContentPane(Main);
        setSize(screenWidth/2, screenHeight/2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        uploadDailyScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (doctorUpdateScheduleUI != null) {
                    doctorUpdateScheduleUI.dispose();
                }
                doctorUpdateScheduleUI = new DoctorUpdateScheduleUI();
            }
        });
        manageIndividualAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (doctorAppoinmentUI != null) {
                    doctorAppoinmentUI.dispose();
                }
                doctorAppoinmentUI = new DoctorAppoinmentUI();
            }
        });
        managePatientsMedicalRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (doctorManageMedicalRecordUI != null) {
                    doctorManageMedicalRecordUI.dispose();
                }
                doctorManageMedicalRecordUI = new DoctorManageMedicalRecordUI();
            }
        });
    }

}
