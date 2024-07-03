package Ui.PatientUI;

import Data.Appointment.AppointmentData;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientHistoryUI {
    private final PatientUI parent;
    JTable historyTable = new JTable();

    public PatientHistoryUI(PatientUI parent) {
        this.parent = parent;
    }

    public void printHistory(){
        // Layer One
        JPanel layerOne = new JPanel();
        layerOne.setBorder(BorderFactory.createEmptyBorder(5, 30, 10, 30));
        layerOne.setLayout(new BorderLayout());
        layerOne.setOpaque(false);

        //  Title
        JLabel patientAppointmentTitle = new JLabel("Historical Record");
        patientAppointmentTitle.setFont(new Font("Arial", Font.BOLD, 20));
        patientAppointmentTitle.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 30));
        patientAppointmentTitle.setForeground(Color.WHITE);
        patientAppointmentTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        layerOne.add(patientAppointmentTitle, BorderLayout.NORTH);

        // Patient Appointment Table
        setHistoryTableById();
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.getViewport().setBackground(new Color(31,36,66));
        scrollPane.setPreferredSize(new Dimension(935,170));
        scrollPane.createHorizontalScrollBar();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        layerOne.add(scrollPane, BorderLayout.CENTER);

        parent.whitePanel.setBounds(parent.whitePanelX, parent.whitePanelY, parent.whitePanelWidth, parent.screenSizeHeight-35);
        parent.whitePanel.setLayout(new BorderLayout());
        parent.whitePanel.setOpaque(false);
        parent.whitePanel.add(layerOne, BorderLayout.CENTER);

        parent.setVisible(true);
    }

    public void setHistoryTableById() {
        java.util.List<String> appointmentHeaders = parent.appointments.getAppointmentHeaders().stream().toList();

        Object[] header = appointmentHeaders.toArray();

        java.util.List<AppointmentData> appointmentList = parent.appointments.getAppointmentList();
        List<Object[]> matchingRows = new ArrayList<>();

        for (AppointmentData appointmentData : appointmentList) {
            if (appointmentData.getPatientIcNumber().equals(parent.patientIcNumber)) {
                LocalDate appointmentDate = LocalDate.parse(appointmentData.getDate());
                if(appointmentDate.isBefore(parent.currentDate)){
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

        historyTable.setModel(tableModel);

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
        JTableHeader tableHeader = historyTable.getTableHeader();
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
        TableColumnModel columnModel = historyTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(new CustomCellRenderer());
        }

        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = historyTable.getSelectedRow();

                if (selectedRow != -1) {
                    TableModel model = historyTable.getModel();

                    parent.appointmentID = model.getValueAt(selectedRow, 0).toString();
                    parent.date = model.getValueAt(selectedRow, 1).toString();
                    parent.time = model.getValueAt(selectedRow, 2).toString();
                    parent.meridiem = model.getValueAt(selectedRow, 3).toString();
                }

            }
        });

        historyTable.setMaximumSize(new Dimension(935, 100));
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyTable.getTableHeader().setReorderingAllowed(false);
        historyTable.setRowHeight(40);
        historyTable.setShowGrid(false);
        historyTable.setIntercellSpacing(new Dimension(0,0));
    }
}
