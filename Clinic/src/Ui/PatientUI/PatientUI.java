package Ui.PatientUI;

import Data.Appointment.Appointments;
import Data.Doctor.DoctorData;
import Data.Doctor.Doctors;
import Data.MedicalRecord.MedicalRecords;
import Data.Patient.Patients;
import Data.TimeSlot.TimeSlotData;
import Data.TimeSlot.TimeSlots;
import Ui.LoginUI.StartUI;
import Ui.Tool.RoundedComponent;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientUI extends JFrame implements ActionListener{
    PatientDashboardUI patientDashboardUI = new PatientDashboardUI(this);
    PatientAppointmentUI patientAppointmentUI = new PatientAppointmentUI(this);
    PatientUpcomingUI patientUpcomingUI = new PatientUpcomingUI(this);
    PatientMedicalUI patientMedicalUI = new PatientMedicalUI(this);
    PatientHistoryUI patientHistoryUI = new PatientHistoryUI(this);

    TimeSlots timeSlots = new TimeSlots();
    Appointments appointments = new Appointments();
    Patients patients = new Patients();
    MedicalRecords medicalRecords = new MedicalRecords();

    protected int screenSizeHeight = 800;
    protected final JButton dashboard = new JButton();
    protected final JButton booking = new JButton();
    protected final JButton edit = new JButton();
    protected final JButton record = new JButton();
    protected final JButton history = new JButton();
    protected final JButton exit = new JButton();
    // White Panel;
    protected final JPanel whitePanel = new JPanel();
    protected int whitePanelWidth = 990;
    protected int whitePanelX = 1280;
    protected int whitePanelY = 0;
    // Global Variable
    protected String userID, slotID, date, time, meridiem, appointmentID;
    protected String patientIcNumber = patients.queryPatient(userID, null, null, null, null, null).getFirst().getIcNumber();
    protected LocalDate currentDate = LocalDate.now();
    protected Doctors doctors = new Doctors();
    protected Color side = new Color(29, 32, 47);
    protected Color background = new Color(18,19,37);

    public PatientUI(String patientID){
        userID = patientID;
        patientDashboardUI.printDashboard();

        // Main Frame //
        setTitle("CarePoint");
        setSize(1280, 835);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Side Panel //
        int sidePanelWidth = 290;
        RoundedComponent sidePanel = new RoundedComponent(0,40,0,40);
        sidePanel.setBackground(side);
        sidePanel.setBounds(0,0,sidePanelWidth,screenSizeHeight-35);
        sidePanel.setLayout(new BorderLayout());

        // Side Container
        JPanel sideContainer = new JPanel();
        sideContainer.setOpaque(false);
        sideContainer.setLayout(new BoxLayout(sideContainer, BoxLayout.Y_AXIS));

        JLabel clinicName = new JLabel("CarePoint Clinic");
        clinicName.setFont(new Font("Arial", Font.BOLD, 26));
        clinicName.setForeground(new Color(232, 246, 244));
        clinicName.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        clinicName.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon userProfile = new ImageIcon(PatientUI.class.getResource("/image/ClinicLogo.png"));
        userProfile = new ImageIcon(userProfile.getImage().getScaledInstance(150, 134, Image.SCALE_DEFAULT));
        JLabel profileImage = new JLabel(userProfile);
        profileImage.setBounds(0, 0, userProfile.getIconWidth(), userProfile.getIconHeight());
        profileImage.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        profileImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        String patientName = patients.queryPatient(userID, null, null, null, null, null).getFirst().getName();
        JLabel userName = new JLabel(patientName);
        userName.setFont(new Font("Arial", Font.BOLD, 20));
        userName.setForeground(new Color(255, 255, 255));
        userName.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel icNumber = new JLabel("IC: " + patientIcNumber);
        icNumber.setFont(new Font("Arial", Font.BOLD, 20));
        icNumber.setForeground(new Color(255, 255, 255));
        icNumber.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        icNumber.setAlignmentX(Component.CENTER_ALIGNMENT);

        int space = 8;

        ImageIcon dashboardImageIcon = new ImageIcon(PatientUI.class.getResource("/image/Dashboard.png"));
        dashboardImageIcon = new ImageIcon(dashboardImageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        dashboard.setIcon(dashboardImageIcon);
        dashboard.setText("Dashboard");
        dashboard.setFont(new Font("Comic Sans", Font.BOLD, 20));
        dashboard.setForeground(new Color(255, 255, 255));
        dashboard.setBackground(side);
        dashboard.setFocusPainted(false);
        dashboard.setBorder(BorderFactory.createEmptyBorder(space, 35, space, 75));
        dashboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        dashboard.addActionListener(this);

        ImageIcon bookingImageIcon = new ImageIcon(PatientUI.class.getResource("/image/Calendar.png"));
        bookingImageIcon = new ImageIcon(bookingImageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING));
        booking.setIcon(bookingImageIcon);
        booking.setText("Appointment");
        booking.setFont(new Font("Comic Sans", Font.BOLD, 20));
        booking.setForeground(new Color(255, 255, 255));
        booking.setBackground(side);
        booking.setFocusPainted(false);
        booking.setBorder(BorderFactory.createEmptyBorder(space, 35, space, 60));
        booking.setAlignmentX(Component.CENTER_ALIGNMENT);
        booking.addActionListener(this);

        ImageIcon editImageIcon = new ImageIcon(PatientUI.class.getResource("/image/Upcoming.png"));
        editImageIcon = new ImageIcon(editImageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_AREA_AVERAGING));
        edit.setIcon(editImageIcon);
        edit.setText("Upcoming");
        edit.setFont(new Font("Comic Sans", Font.BOLD, 20));
        edit.setForeground(new Color(255, 255, 255));
        edit.setBackground(side);
        edit.setFocusPainted(false);
        edit.setBorder(BorderFactory.createEmptyBorder(space, 35, space, 85));
        edit.setAlignmentX(Component.CENTER_ALIGNMENT);
        edit.addActionListener(this);

        ImageIcon recordImageIcon = new ImageIcon(PatientUI.class.getResource("/image/Book.png"));
        recordImageIcon = new ImageIcon(recordImageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        record.setIcon(recordImageIcon);
        record.setText("Medical Record");
        record.setFont(new Font("Comic Sans", Font.BOLD, 20));
        record.setForeground(new Color(255, 255, 255));
        record.setBackground(side);
        record.setFocusPainted(false);
        record.setBorder(BorderFactory.createEmptyBorder(space, 35, space, 35));
        record.setAlignmentX(Component.CENTER_ALIGNMENT);
        record.addActionListener(this);

        ImageIcon historyImageIcon = new ImageIcon(PatientUI.class.getResource("/image/History.png"));
        historyImageIcon = new ImageIcon(historyImageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        history.setIcon(historyImageIcon);
        history.setText("History Review");
        history.setFont(new Font("Comic Sans", Font.BOLD, 20));
        history.setForeground(new Color(255, 255, 255));
        history.setBackground(side);
        history.setFocusPainted(false);
        history.setBorder(BorderFactory.createEmptyBorder(space, 35, space, 35));
        history.setAlignmentX(Component.CENTER_ALIGNMENT);
        history.addActionListener(this);

        ImageIcon exitImageIcon = new ImageIcon(PatientUI.class.getResource("/image/Logout.png"));
        exitImageIcon = new ImageIcon(exitImageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
        exit.setIcon(exitImageIcon);
        exit.setText("Log Out");
        exit.setFont(new Font("Comic Sans", Font.BOLD, 20));
        exit.setForeground(new Color(255, 255, 255));
        exit.setBackground(side);
        exit.setFocusPainted(false);
        exit.setBorder(BorderFactory.createEmptyBorder(space+5, 35, space+5, 95));
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.addActionListener(this);

        sideContainer.add(clinicName);
        sideContainer.add(profileImage);
        sideContainer.add(userName);
        sideContainer.add(icNumber);
        sideContainer.add(dashboard);
        sideContainer.add(booking);
        sideContainer.add(edit);
        sideContainer.add(record);
        sideContainer.add(history);
        sideContainer.add(exit);
        sidePanel.add(sideContainer);

        // Create Division Line //
        RoundedComponent divisionLine = new RoundedComponent(space,space,space,space);
        divisionLine.setBounds(13,300,250,4); // Division Line Coordinates and Size
        divisionLine.setBackground(new Color(255,255,255));
        divisionLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Background Panel //
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBounds(0,0,1270,screenSizeHeight);
        backgroundPanel.setBackground(background);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(sidePanel, BorderLayout.WEST);
        backgroundPanel.add(whitePanel, BorderLayout.CENTER);

        // Add components to the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(backgroundPanel, Integer.valueOf(0));
        layeredPane.add(divisionLine, Integer.valueOf(1));

        add(layeredPane);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == dashboard){
            resetWhitePanelContent();
            patientDashboardUI.printDashboard();
        }else if(e.getSource() == booking){
            resetWhitePanelContent();
            patientAppointmentUI.printAppointment();
        }else if(e.getSource() == edit){
            resetWhitePanelContent();
            patientUpcomingUI.printUpcoming();
        }else if(e.getSource() == record){
            resetWhitePanelContent();
            patientMedicalUI.printRecord();
        }else if(e.getSource() == history){
            resetWhitePanelContent();
            patientHistoryUI.printHistory();
        }else if(e.getSource() == exit){
            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to sign out the account?",
                    "Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Sign Out Now...");
                this.dispose();
                new StartUI();
            }
        }
    }

    // Common Functionality //
    public JComboBox setSearchComboBox(){
        List<String> doctorSlotHeaders = doctors.getDoctorHeaders();
        Object[] header = doctorSlotHeaders.toArray();
        List<DoctorData> doctorDataList = doctors.getDoctorList();
        Object[][] data = new Object[doctorDataList.size()][header.length];
        for (int i = 0; i < doctorDataList.size(); i++) {
            DoctorData doctorData = doctorDataList.get(i);
            data[i][5] = doctorData.getDoctorName();
        }
        List<String> comboBoxItems = new ArrayList<>();
        comboBoxItems.add("All");
        for (int i = 0; i < data.length; i++) {
            comboBoxItems.add((String) data[i][5]);
        }
        String[] comboBoxArray = comboBoxItems.toArray(new String[0]);
        JComboBox<String> comboBox = new JComboBox<>(comboBoxArray);
        comboBox.setBackground(new Color(92, 126, 234));
        comboBox.setForeground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));

        class CustomComboBoxUI extends BasicComboBoxUI{
            @Override
            public JButton createArrowButton() {
                // Create a custom button for the arrow
                JButton button = new JButton("▼");
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setBackground(new Color(92, 126, 234));
                button.setForeground(Color.WHITE);
                return button;
            }

            @Override
            public void installUI(JComponent comboBox) {
                super.installUI(comboBox);
                comboBox.setBorder(BorderFactory.createLineBorder(new Color(92, 126, 234), 0));
            }
        }

        comboBox.setUI(new CustomComboBoxUI());

        return comboBox;
    }

    public void setAllTimeSlotTable(String doctorName, String specialize, JTable table) {
        java.util.List<String> timeSlotHeaders = timeSlots.getTimeSlotHeaders().stream().toList();

        Object[] header = timeSlotHeaders.toArray();

        List<TimeSlotData> timeSlotList = timeSlots.getTimeSlotList();
        List<Object[]> matchingRows = new ArrayList<>();
        boolean matchFound = false;

        for (TimeSlotData timeSlotData : timeSlotList) {
            if (timeSlotData.getDoctorName().equals(doctorName) || timeSlotData.getDoctorSpecialization().equals(specialize)) {
                matchingRows.add(new Object[]{
                        timeSlotData.getId(),
                        timeSlotData.getDate(),
                        timeSlotData.getTime(),
                        timeSlotData.getMeridiem(),
                        timeSlotData.getDoctorId(),
                        timeSlotData.getDoctorName(),
                        timeSlotData.getDoctorSpecialization(),
                        timeSlotData.getStatus()
                });
                matchFound = true;
            }
        }

        if (!matchFound) {
            for (TimeSlotData timeSlotData : timeSlotList) {
                matchingRows.add(new Object[]{
                        timeSlotData.getId(),
                        timeSlotData.getDate(),
                        timeSlotData.getTime(),
                        timeSlotData.getMeridiem(),
                        timeSlotData.getDoctorId(),
                        timeSlotData.getDoctorName(),
                        timeSlotData.getDoctorSpecialization(),
                        timeSlotData.getStatus()
                });
            }
        }

        TableModel tableModel = new DefaultTableModel(matchingRows.toArray(new Object[0][0]), header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(tableModel);

        // Custom header renderer to set header row height
        class CustomHeaderRenderer extends JLabel implements TableCellRenderer {
            public CustomHeaderRenderer() {
                setOpaque(true);
                setBackground(new Color(24, 26, 51));
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 12));
                setHorizontalAlignment(CENTER);
                setVerticalAlignment(CENTER);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText(value.toString());
                setPreferredSize(new Dimension(table.getColumnModel().getColumn(column).getWidth(), 50));
                return this;
            }
        }

        // Apply custom header renderer
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setDefaultRenderer(new CustomHeaderRenderer());

        // Custom cell renderer
        class CustomCellRenderer extends JLabel implements TableCellRenderer {
            public CustomCellRenderer() {
                setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText(value.toString());
                if (isSelected) {
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());
                } else {
                    //setBackground(row % 2 == 0 ? new Color(32, 38, 70) : new Color(65, 65, 119));
                    setBackground(new Color(32, 38, 70));
                    setForeground(Color.WHITE); // Font
                }
                setHorizontalAlignment(CENTER); // Center align text
                return this;
            }
        }

        // Apply custom cell renderer to specific columns or all columns
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(new CustomCellRenderer());
        }

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    TableModel model = table.getModel();

                    slotID = model.getValueAt(selectedRow, 0).toString();
                    date = model.getValueAt(selectedRow, 1).toString();
                    time = model.getValueAt(selectedRow, 2).toString();
                    meridiem = model.getValueAt(selectedRow, 3).toString();
                }
            }
        });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
    }

    public void resetWhitePanelContent(){
        whitePanel.removeAll();
        whitePanel.revalidate();
        whitePanel.repaint();
    }

    public static void main(String[] args) {
        new PatientUI("pt1");
    }
}