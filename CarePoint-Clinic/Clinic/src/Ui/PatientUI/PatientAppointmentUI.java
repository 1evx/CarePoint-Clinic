package Ui.PatientUI;

import Data.TimeSlot.TimeSlotData;
import Data.TimeSlot.TimeSlots;
import Ui.Tool.RoundedButton;
import Ui.Tool.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PatientAppointmentUI extends JPanel {
    private final PatientUI parent;
    private JTable appointmentTable = new JTable();
    private JTextField textField = new JTextField();

    public PatientAppointmentUI(PatientUI parent) {
        this.parent = parent;
    }

    public void printAppointment(){
        parent.setAllTimeSlotTable(null,null, appointmentTable);

        // Layer Two //
        JPanel layerOne = new JPanel();
        layerOne.setMaximumSize(new Dimension(935, 250));
        layerOne.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        layerOne.setLayout(new BorderLayout());
        layerOne.setOpaque(false);

        // Search Container //
        JPanel searchContainer = new JPanel();
        searchContainer.setPreferredSize(new Dimension(935,50));
        searchContainer.setOpaque(false);
        searchContainer.setLayout(new BoxLayout(searchContainer, BoxLayout.X_AXIS));
        layerOne.add(searchContainer, BorderLayout.NORTH);

        //  Title
        JLabel appointment = new JLabel("Available Appointment");
        appointment.setFont(new Font("Arial", Font.BOLD, 20));
        appointment.setForeground(Color.WHITE);
        appointment.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Table
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.getViewport().setBackground(new Color(31,36,66));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        layerOne.add(scrollPane, BorderLayout.CENTER);

        // Search Combo Box
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

        // Button To Add Appointment
        JPanel buttonHolder = new JPanel();
        buttonHolder.setPreferredSize(new Dimension(300,50));
        buttonHolder.setBackground(parent.background);
        JButton addAppointmentButton = RoundedButton.createRoundedButton("Add Appointment", 40, 3, new Color(112, 195, 167));
        SwingUtils.setButtonProperties(addAppointmentButton, new Color(112, 195, 167), new Color(246,246,246), new Font("Arial", Font.BOLD, 18));
        addAppointmentButton.setPreferredSize(new Dimension(300, 45));
        addAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display confirmation dialog
                int response = JOptionPane.showConfirmDialog(
                        parent,
                        "Are you sure you want to reserve this appointment?",
                        "Appointment Confirmation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                // Check the user's response
                if (response == JOptionPane.YES_OPTION && parent.slotID != null) {
                    String status = parent.timeSlots.queryTimeSlot(parent.slotID, null, null, null, null, null, null, null).getFirst().getStatus();
                    if(status.equals("available")){
                        String specialization = parent.timeSlots.queryTimeSlot(parent.slotID, null, null, null, null, null, null, "available").getFirst().getDoctorSpecialization();

                        // Change TimeSlot To reserved
                        List<TimeSlotData> editedTimeSlot = parent.timeSlots.queryTimeSlot(parent.slotID, null, null, null, null, null, null, null);
                        editedTimeSlot.getFirst().setStatus("reserved");
                        parent.timeSlots.updateTimeSlots();

                        // Add Appointment (Reservation)
                        parent.appointments.addReservedAppointment(parent.date, parent.time, parent.meridiem, parent.patientIcNumber, specialization, "reservation", "active",parent.slotID);

                        JOptionPane.showMessageDialog(parent, "Appointment booked successfully.");

                        // refresh the tables
                        parent.dispose();
                        new PatientUI(parent.userID);

                    } else {
                        JOptionPane.showMessageDialog(parent, "Someone already reserve the appointment.");
                    }
                } else {
                    JOptionPane.showMessageDialog(parent, "Reserved failed.");
                }
            }
        });
        buttonHolder.add(addAppointmentButton);
        layerOne.add(buttonHolder, BorderLayout.SOUTH);

        parent.whitePanel.setBounds(parent.whitePanelX, parent.whitePanelY, parent.whitePanelWidth, parent.screenSizeHeight-35);
        parent.whitePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        parent.whitePanel.setLayout(new BorderLayout());
        parent.whitePanel.setOpaque(false);

        parent.whitePanel.add(layerOne, BorderLayout.CENTER);

        setVisible(true);
    }
}
