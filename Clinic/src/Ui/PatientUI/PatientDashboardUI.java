package Ui.PatientUI;

import Data.Appointment.AppointmentData;
import Data.TimeSlot.TimeSlotData;
import Ui.Tool.RoundedComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class PatientDashboardUI extends JPanel {
    private PatientUI parent;
    private JTable appointmentTable = new JTable();
    private JTextField textField = new JTextField();

    public PatientDashboardUI(PatientUI parent) {
        this.parent = parent;
    }

    protected void printDashboard() {
        parent.setAllTimeSlotTable(null,null, appointmentTable);
        int radius = 10;

        // Layer One //
        JPanel layerOne = new JPanel();
        int layerOneWidth = 1000;
        int layerOneHeight = 150;
        layerOne.setBounds(0, 0, layerOneWidth, layerOneHeight);
        layerOne.setLayout(new BoxLayout(layerOne, BoxLayout.X_AXIS));
        layerOne.setBorder(BorderFactory.createEmptyBorder(30, 0, 25, 0));
        layerOne.setOpaque(false);

        // Incoming Appointment panel
        RoundedComponent incomingPanel = new RoundedComponent(radius,radius,radius,radius);
        incomingPanel.setPreferredSize(new Dimension(300,150));
        incomingPanel.setBackground(new Color(122, 63, 243));
        incomingPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        incomingPanel.setLayout(new BorderLayout());

        ImageIcon hospital = new ImageIcon(PatientUI.class.getResource("/image/Hospital.png"));
        hospital = new ImageIcon(hospital.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        JLabel hospitalImage = new JLabel(hospital);
        hospitalImage.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        incomingPanel.add(hospitalImage,BorderLayout.WEST);

        JPanel incomingContainer = new JPanel();
        incomingContainer.setLayout(new BoxLayout(incomingContainer, BoxLayout.Y_AXIS));
        incomingContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 15));
        incomingContainer.setOpaque(false);
        incomingContainer.setBackground(new Color(122, 63, 243));
        JLabel incomingTitle1 = new JLabel("Incoming");
        incomingTitle1.setFont(new Font("Arial", Font.BOLD, 18));
        incomingTitle1.setBackground(new Color(122, 63, 243));
        incomingTitle1.setForeground(Color.WHITE);
        incomingTitle1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel incomingTitle2 = new JLabel("Appointment:");
        incomingTitle2.setFont(new Font("Arial", Font.BOLD, 18));
        incomingTitle2.setBackground(new Color(122, 63, 243));
        incomingTitle2.setForeground(Color.WHITE);
        incomingTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
        int incomingAppointment = getIncomingAppointment();
        JLabel incomingNumber = new JLabel(String.valueOf(incomingAppointment));
        incomingNumber.setFont(new Font("Arial", Font.BOLD, 60));
        incomingNumber.setBackground(new Color(122, 63, 243));
        incomingNumber.setForeground(Color.WHITE);
        incomingNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        incomingContainer.add(incomingTitle1);
        incomingContainer.add(incomingTitle2);
        incomingContainer.add(incomingNumber);
        incomingPanel.add(incomingContainer,BorderLayout.CENTER);

        // Available Appointment Panel
        RoundedComponent availablePanel = new RoundedComponent(radius,radius,radius,radius);
        availablePanel.setPreferredSize(new Dimension(300,150));
        availablePanel.setBackground(new Color(92, 126, 234));
        availablePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        availablePanel.setLayout(new BorderLayout());

        ImageIcon doctor = new ImageIcon(PatientUI.class.getResource("/image/Doctor.png"));
        doctor = new ImageIcon(doctor.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel doctorImage = new JLabel(doctor);
        doctorImage.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        availablePanel.add(doctorImage,BorderLayout.WEST);

        JPanel availableContainer = new JPanel();
        availableContainer.setLayout(new BoxLayout(availableContainer, BoxLayout.Y_AXIS));
        availableContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 15));
        availableContainer.setOpaque(false);
        availableContainer.setBackground(new Color(122, 63, 243));
        JLabel availableTitle1 = new JLabel("Available");
        availableTitle1.setFont(new Font("Arial", Font.BOLD, 18));
        availableTitle1.setBackground(new Color(122, 63, 243));
        availableTitle1.setForeground(Color.WHITE);
        availableTitle1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel availableTitle2 = new JLabel("Appointment:");
        availableTitle2.setFont(new Font("Arial", Font.BOLD, 18));
        availableTitle2.setBackground(new Color(122, 63, 243));
        availableTitle2.setForeground(Color.WHITE);
        availableTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
        int availableAppointment = getAvailableAppointment();
        JLabel availableNumber = new JLabel(String.valueOf(availableAppointment));
        availableNumber.setFont(new Font("Arial", Font.BOLD, 60));
        availableNumber.setBackground(new Color(122, 63, 243));
        availableNumber.setForeground(Color.WHITE);
        availableNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        availableContainer.add(availableTitle1);
        availableContainer.add(availableTitle2);
        availableContainer.add(availableNumber);
        availablePanel.add(availableContainer,BorderLayout.CENTER);

        // History Review Panel
        RoundedComponent historyPanel = new RoundedComponent(radius,radius,radius,radius);
        historyPanel.setPreferredSize(new Dimension(300,150));
        historyPanel.setBackground(new Color(113, 193, 166));
        historyPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        historyPanel.setLayout(new BorderLayout());

        ImageIcon history2 = new ImageIcon(PatientUI.class.getResource("/image/History2.png"));
        history2 = new ImageIcon(history2.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel historyImage2 = new JLabel(history2);
        historyImage2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        historyPanel.add(historyImage2,BorderLayout.WEST);

        JPanel historyContainer = new JPanel();
        historyContainer.setLayout(new BoxLayout(historyContainer, BoxLayout.Y_AXIS));
        historyContainer.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 15));
        historyContainer.setOpaque(false);
        historyContainer.setBackground(new Color(122, 63, 243));
        JLabel historyTitle1 = new JLabel("History");
        historyTitle1.setFont(new Font("Arial", Font.BOLD, 18));
        historyTitle1.setBackground(new Color(122, 63, 243));
        historyTitle1.setForeground(Color.WHITE);
        historyTitle1.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel historyTitle2 = new JLabel("Appointment:");
        historyTitle2.setFont(new Font("Arial", Font.BOLD, 18));
        historyTitle2.setBackground(new Color(122, 63, 243));
        historyTitle2.setForeground(Color.WHITE);
        historyTitle2.setAlignmentX(Component.CENTER_ALIGNMENT);
        int historyAppointment = getHistoryAppointment();
        JLabel historyNumber = new JLabel(String.valueOf(historyAppointment));
        historyNumber.setFont(new Font("Arial", Font.BOLD, 60));
        historyNumber.setBackground(new Color(122, 63, 243));
        historyNumber.setForeground(Color.WHITE);
        historyNumber.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyContainer.add(historyTitle1);
        historyContainer.add(historyTitle2);
        historyContainer.add(historyNumber);
        historyPanel.add(historyContainer,BorderLayout.CENTER);

        // Add components to layerOne
        layerOne.add(Box.createHorizontalStrut(30));
        layerOne.add(incomingPanel);
        layerOne.add(Box.createHorizontalStrut(20));
        layerOne.add(availablePanel);
        layerOne.add(Box.createHorizontalStrut(20));
        layerOne.add(historyPanel);
        layerOne.add(Box.createHorizontalStrut(30));

        // Layer Two //
        JPanel layerTwo = new JPanel();
        layerTwo.setBounds(0, 0, 935, 100); // Overall Layer Two Size
        layerTwo.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        layerTwo.setLayout(new BorderLayout());
        layerTwo.setOpaque(false);

        // Search Container //
        JPanel searchContainer = new JPanel();
        searchContainer.setOpaque(false);
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.X_AXIS));
        searchContainer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        layerTwo.add(searchContainer, BorderLayout.NORTH);

        //  Title
        JLabel appointment = new JLabel("Available Appointment");
        appointment.setFont(new Font("Arial", Font.BOLD, 20));
        appointment.setForeground(Color.WHITE);
        appointment.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Spicy Chicken Combo Box
        JToolBar toolbar = new JToolBar();
        JComboBox searchComboBox = parent.setSearchComboBox();
        searchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doctorName = (String) searchComboBox.getSelectedItem();
                parent.setAllTimeSlotTable(doctorName, null, appointmentTable);
            }
        });
        toolbar.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        toolbar.setFloatable(false);
        toolbar.setRollover(false);
        toolbar.add(searchComboBox);
        toolbar.setMaximumSize(new Dimension(140, 30));
        toolbar.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Search Icon
        ImageIcon searchImageIcon = new ImageIcon(PatientUI.class.getResource("/image/Search.png"));
        searchImageIcon = new ImageIcon(searchImageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        JLabel search = new JLabel();
        search.setIcon(searchImageIcon);
        search.setAlignmentX(Component.RIGHT_ALIGNMENT);
        search.setFont(new Font("Comic Sans", Font.BOLD, 20));

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.getViewport().setBackground(new Color(31,36,66));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.createVerticalScrollBar();
        layerTwo.add(scrollPane, BorderLayout.CENTER);

        // Search Text Field
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String specialize = textField.getText();
                parent.setAllTimeSlotTable(null ,String.valueOf(specialize), appointmentTable);
            }
        });
        textField.setMaximumSize(new Dimension(200,30));
        textField.setBorder(BorderFactory.createLineBorder(new Color(92, 126, 234), 2));
        textField.setAlignmentX(Component.RIGHT_ALIGNMENT);
        textField.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to Search Container
        searchContainer.add(Box.createHorizontalStrut(10));
        searchContainer.add(appointment);
        searchContainer.add(Box.createHorizontalStrut(10));
        searchContainer.add(toolbar);
        searchContainer.add(Box.createHorizontalStrut(330));
        searchContainer.add(search);
        searchContainer.add(Box.createHorizontalStrut(10));
        searchContainer.add(textField);

        parent.whitePanel.setBounds(parent.whitePanelX, parent.whitePanelY, parent.whitePanelWidth, parent.screenSizeHeight-35);
        parent.whitePanel.setLayout(new BorderLayout());
        parent.whitePanel.setOpaque(false);

        parent.whitePanel.add(layerOne, BorderLayout.NORTH);
        parent.whitePanel.add(layerTwo, BorderLayout.CENTER);

        setVisible(true);
    }

    private int getAvailableAppointment(){
        java.util.List<TimeSlotData> timeSlotList = parent.timeSlots.getTimeSlotList();
        return timeSlotList.size();
    }

    private int getIncomingAppointment(){
        int incomingAppointment = 0;
        java.util.List<AppointmentData> appointmentDataList = parent.appointments.getAppointmentList();
        for (AppointmentData appointmentData : appointmentDataList) {
            if (appointmentData.getPatientIcNumber().equals(parent.patientIcNumber) && appointmentData.getStatus().equals("active")) {
                LocalDate appointmentDate = LocalDate.parse(appointmentData.getDate());
                if(appointmentDate.isAfter(parent.currentDate)) {
                    incomingAppointment += 1;
                }
            }
        }
        return incomingAppointment;
    }

    private int getHistoryAppointment(){
        int historyAppointment = 0;
        List<AppointmentData> appointmentDataList = parent.appointments.getAppointmentList();
        for (AppointmentData appointmentData : appointmentDataList) {
            if (appointmentData.getPatientIcNumber().equals(parent.patientIcNumber)) {
                LocalDate appointmentDate = LocalDate.parse(appointmentData.getDate());
                if(appointmentDate.isBefore(parent.currentDate)) {
                    historyAppointment += 1;
                }
            }
        }
        return historyAppointment;
    }
}
