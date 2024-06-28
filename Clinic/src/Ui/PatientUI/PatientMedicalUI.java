package Ui.PatientUI;

import Data.MedicalRecord.MedicalRecordData;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PatientMedicalUI extends JPanel {
    private PatientUI parent;
    JTable medicalTable = new JTable();

    public PatientMedicalUI(PatientUI parent) {
        this.parent = parent;
    }

    public void printRecord() {
        // Layer One
        JPanel layerOne = new JPanel();
        layerOne.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
        layerOne.setLayout(new BorderLayout());
        layerOne.setOpaque(false);

        //  Title
        JLabel patientAppointmentTitle = new JLabel("Medical Record");
        patientAppointmentTitle.setFont(new Font("Arial", Font.BOLD, 20));
        patientAppointmentTitle.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 30));
        patientAppointmentTitle.setForeground(Color.WHITE);
        patientAppointmentTitle.setAlignmentX(Component.RIGHT_ALIGNMENT);
        layerOne.add(patientAppointmentTitle, BorderLayout.NORTH);

        // Patient Appointment Table
        setMedicalRecordTableById();
        JScrollPane scrollPane = new JScrollPane(medicalTable);
        scrollPane.getViewport().setBackground(new Color(31,36,66));
        scrollPane.setPreferredSize(new Dimension(935,170));
        scrollPane.createHorizontalScrollBar();
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        layerOne.add(scrollPane, BorderLayout.CENTER);

        parent.whitePanel.setBounds(parent.whitePanelX, parent.whitePanelY, parent.whitePanelWidth, parent.screenSizeHeight-35);
        parent.whitePanel.setLayout(new BorderLayout());
        parent.whitePanel.setOpaque(false);
        parent.whitePanel.add(layerOne, BorderLayout.CENTER);

        setVisible(true);
    }

    public void setMedicalRecordTableById() {
        java.util.List<String> medicalRecordHeaders = parent.medicalRecords.getMedicalRecordHeader().stream().toList();

        Object[] header = medicalRecordHeaders.toArray();

        java.util.List<MedicalRecordData> medicalRecordList = parent.medicalRecords.getMedicalRecordList();
        List<Object[]> matchingRows = new ArrayList<>();

        for (MedicalRecordData medicalRecordData : medicalRecordList) {
            if (medicalRecordData.getPatientIcNumber().equals(parent.patientIcNumber)) {
                matchingRows.add(new Object[]{
                        medicalRecordData.getId(),
                        medicalRecordData.getDate(),
                        medicalRecordData.getTime(),
                        medicalRecordData.getMeridiem(),
                        medicalRecordData.getDescription(),
                        medicalRecordData.getDose(),
                        medicalRecordData.getDoctorId(),
                        medicalRecordData.getDoctorName(),
                        medicalRecordData.getPatientIcNumber(),
                        medicalRecordData.getPatientName(),
                        medicalRecordData.getSpecialization()
                });
            }
        }

        TableModel tableModel = new DefaultTableModel(matchingRows.toArray(new Object[0][0]), header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        medicalTable.setModel(tableModel);

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
        JTableHeader tableHeader = medicalTable.getTableHeader();
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
                    setBackground(new Color(32, 38, 70));
                    setForeground(Color.WHITE); // Font
                }
                setHorizontalAlignment(CENTER); // Center align text
                return this;
            }
        }

        // Apply custom cell renderer to specific columns or all columns
        TableColumnModel columnModel = medicalTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(new CustomCellRenderer());
        }

        medicalTable.setMaximumSize(new Dimension(935, 100));
        medicalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicalTable.getTableHeader().setReorderingAllowed(false);
        medicalTable.setRowHeight(40);
        medicalTable.setShowGrid(false);
        medicalTable.setIntercellSpacing(new Dimension(0,0));
    }
}
