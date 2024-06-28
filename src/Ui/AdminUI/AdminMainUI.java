package Ui.AdminUI;

import Data.Appointment.AppointmentData;
import Data.Appointment.Appointments;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminMainUI extends JFrame {
    Appointments appointments = new Appointments();
    Object[] header;
    Object[][] data;
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String todayDate = today.format(formatter);

    private JButton doctorAccountButton;
    private JButton patientAccountButton;
    private JLabel Title;
    private JPanel MainPanel;
    private JTable Table;
    private JLabel TableLabel;
    private JScrollPane TableScrollPane;
    private JPanel TablePane;
    private JPanel UserAccPanel;
    private JPanel AppointmentPanel;
    private JButton timeSlotsButton;
    private JButton walkInAppointmentButton;
    private JPanel PaymentPanel;
    private JPanel MedicalPanel;
    private JButton refreshButton;
    private JButton patientMedicalRecordButton;
    private JButton MedicineButton;
    private JButton PaymentButton;

    private PatientAccUI patientAccUI;
    private DoctorAccUI doctorAccUI;
    private TimeSlotUI timeSlotUI;
    private WalkInAppoinmentUI walkInAppointmentUI;
    private PatientMedicalRecordUI patientMedicalRecordUI;
    private MedicineUI medicineUI;
    private PaymentUI paymentUI;

    public static void main(String[] args) {
        new AdminMainUI();
    }

    public AdminMainUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        setContentPane(MainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin Menu");
        setSize(screenWidth, (int) Math.floor(screenHeight*0.98));
        TablePane.setPreferredSize(new Dimension(getSize().width, getSize().height/4));
        setTable();
        setResizable(false);
        setLocationRelativeTo(null);
        addButtonListener();
        setVisible(true);
    }

    private void addButtonListener() {
        patientAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (patientAccUI != null) {
                    patientAccUI.dispose();
                }
                patientAccUI = new PatientAccUI();
            }
        });

        doctorAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (doctorAccUI != null) {
                    doctorAccUI.dispose();
                }
                doctorAccUI = new DoctorAccUI();
            }
        });

        timeSlotsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeSlotUI != null) {
                    timeSlotUI.dispose();
                }
                timeSlotUI = new TimeSlotUI();
            }
        });

        walkInAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (walkInAppointmentUI != null) {
                    walkInAppointmentUI.dispose();
                }
                walkInAppointmentUI = new WalkInAppoinmentUI();
            }
        });

        patientMedicalRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (patientMedicalRecordUI != null) {
                    patientMedicalRecordUI.dispose();
                }
                patientMedicalRecordUI = new PatientMedicalRecordUI();
            }
        });

        MedicineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (medicineUI != null) {
                    medicineUI.dispose();
                }
                medicineUI = new MedicineUI();
            }
        });

        PaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paymentUI != null) {
                    paymentUI.dispose();
                }
                paymentUI = new PaymentUI();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                appointments = new Appointments();
                setTable();
            }
        });
    }

    private void setTable() {
        List<String> headerList = appointments.getAppointmentHeaders();
        List<String> doctorHeader = headerList.stream()
                .filter(header -> !header.equals("ID") && !header.equals("Time Slot ID"))
                .toList();
        header = doctorHeader.toArray();

        List<AppointmentData> appointmentList = appointments.queryAppointment(null, todayDate, null, null, null, null, null, null, null);
        data = new Object[appointmentList.size()][header.length];

        for (int i = 0; i < appointmentList.size(); i++) {
            AppointmentData appointmentData = appointmentList.get(i);
            data[i][0] = appointmentData.getDate();
            data[i][1] = appointmentData.getTime();
            data[i][2] = appointmentData.getMeridiem();
            data[i][3] = appointmentData.getPatientIcNumber();
            data[i][4] = appointmentData.getSpecialization();
            data[i][5] = appointmentData.getType();
            data[i][6] = appointmentData.getStatus();
        }

        TableModel tableModel = new DefaultTableModel(data, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        Table.setModel(tableModel);
        Table.setRowSorter(sorter);

        Table.getTableHeader().setReorderingAllowed(false);
    }

}
