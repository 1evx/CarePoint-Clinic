package Ui.PatientUI;

import Data.Appointment.AppointmentData;
import Data.TimeSlot.TimeSlotData;
import Ui.Tool.RoundedButton;
import Ui.Tool.SwingUtils;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientUpcomingUI extends JPanel {
    private final PatientUI parent;
    JTable upcomingTable = new JTable();

    public PatientUpcomingUI(PatientUI parent) {
        this.parent = parent;
    }

    public void printUpcoming() {
        // Layer One
        JPanel layerOne = new JPanel();
        layerOne.setBorder(BorderFactory.createEmptyBorder(5, 30, 0, 30));
        layerOne.setLayout(new BorderLayout());
        layerOne.setOpaque(false);

        //  Title
        JLabel patientAppointmentTitle = new JLabel("Your Appointment");
        patientAppointmentTitle.setFont(new Font("Arial", Font.BOLD, 20));
        patientAppointmentTitle.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 30));
        patientAppointmentTitle.setForeground(Color.WHITE);
        patientAppointmentTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        layerOne.add(patientAppointmentTitle, BorderLayout.NORTH);

        // Patient Appointment Table
        setAppointmentTableById();
        JScrollPane scrollPane = new JScrollPane(upcomingTable);
        scrollPane.getViewport().setBackground(new Color(31,36,66));
        scrollPane.setPreferredSize(new Dimension(935,170));
        scrollPane.createHorizontalScrollBar();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        layerOne.add(scrollPane, BorderLayout.CENTER);

        // Button Cancel Appointment
        JPanel buttonHolder = new JPanel();
        buttonHolder.setPreferredSize(new Dimension(300,50));
        buttonHolder.setBackground(parent.background);
        JButton cancelAppointmentButton = RoundedButton.createRoundedButton("Cancel Appointment", 40, 3, new Color(255, 15, 101));
        SwingUtils.setButtonProperties(cancelAppointmentButton, new Color(255, 15, 101), new Color(246,246,246), new Font("Arial", Font.BOLD, 18));
        cancelAppointmentButton.setPreferredSize(new Dimension(300, 45));
        cancelAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display confirmation dialog
                int response = JOptionPane.showConfirmDialog(
                        parent,
                        "Are you sure you want to cancel this appointment?",
                        "Appointment Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION && parent.appointmentID != null) {
                    String queryTimeSlotID = parent.appointments.queryAppointment(parent.appointmentID, null, null, null, parent.patientIcNumber, null, null, null, null).getFirst().getTimeSlotId();

                    // Change TimeSlot To reserved
                    java.util.List<TimeSlotData> editedTimeSlot = parent.timeSlots.queryTimeSlot(queryTimeSlotID, parent.date, parent.time, parent.meridiem, null, null, null, "reserved");
                    editedTimeSlot.getFirst().setStatus("available");
                    parent.timeSlots.updateTimeSlots();

                    // Delete Appointment (Reservation)
                    parent.appointments.deleteReservedAppointment(parent.appointmentID,queryTimeSlotID);

                    JOptionPane.showMessageDialog(parent, "Appointment delete successfully.");

                    // Refresh the Tables
                    parent.dispose();
                    new PatientUI(parent.userID);

                } else {
                    JOptionPane.showMessageDialog(parent, "Delete failed.");
                }
            }
        });
        buttonHolder.add(cancelAppointmentButton);
        layerOne.add(buttonHolder, BorderLayout.SOUTH);

        parent.whitePanel.setBounds(parent.whitePanelX, parent.whitePanelY, parent.whitePanelWidth, parent.screenSizeHeight-35);
        parent.whitePanel.setLayout(new BorderLayout());
        parent.whitePanel.setOpaque(false);

        parent.whitePanel.add(layerOne, BorderLayout.CENTER);

        setVisible(true);
    }

    public void setAppointmentTableById() {
        java.util.List<String> appointmentHeaders = parent.appointments.getAppointmentHeaders().stream().toList();

        Object[] header = appointmentHeaders.toArray();

        java.util.List<AppointmentData> appointmentList = parent.appointments.getAppointmentList();
        List<Object[]> matchingRows = new ArrayList<>();

        for (AppointmentData appointmentData : appointmentList) {
            if (appointmentData.getPatientIcNumber().equals(parent.patientIcNumber)) {
                LocalDate appointmentDate = LocalDate.parse(appointmentData.getDate());
                if(appointmentDate.isAfter(parent.currentDate)){
                    matchingRows.add(new Object[]{
                            appointmentData.getId(),
                            appointmentData.getDate(),
                            appointmentData.getTime(),
                            appointmentData.getMeridiem(),
                            appointmentData.getPatientIcNumber(),
                            appointmentData.getSpecialization(),
                            appointmentData.getType(),
                            appointmentData.getStatus(),
                            appointmentData.getTimeSlotId()
                    });
                }
            }
        }

        TableModel tableModel = new DefaultTableModel(matchingRows.toArray(new Object[0][0]), header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        upcomingTable.setModel(tableModel);

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
        JTableHeader tableHeader = upcomingTable.getTableHeader();
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
        TableColumnModel columnModel = upcomingTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(new CustomCellRenderer());
        }

        upcomingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = upcomingTable.getSelectedRow();

                if (selectedRow != -1) {
                    TableModel model = upcomingTable.getModel();

                    parent.appointmentID = model.getValueAt(selectedRow, 0).toString();
                    parent.date = model.getValueAt(selectedRow, 1).toString();
                    parent.time = model.getValueAt(selectedRow, 2).toString();
                    parent.meridiem = model.getValueAt(selectedRow, 3).toString();
                }

            }
        });

        upcomingTable.setMaximumSize(new Dimension(935, 100));
        upcomingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        upcomingTable.getTableHeader().setReorderingAllowed(false);
        upcomingTable.setRowHeight(40);
        upcomingTable.setShowGrid(false);
        upcomingTable.setIntercellSpacing(new Dimension(0,0));
    }
}
