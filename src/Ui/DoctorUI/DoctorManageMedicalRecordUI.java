package Ui.DoctorUI;

import Authentication.Cookie;
import Data.MedicalRecord.MedicalRecordData;
import Data.MedicalRecord.MedicalRecords;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



public class DoctorManageMedicalRecordUI extends JFrame {
    MedicalRecords medicalRecords = new MedicalRecords();
    Object[] header;
    Object[][] data;
    int selectedRow = -1;


    private JPanel MainPanel;
    private JLabel Title;
    private JButton refreshButton;
    private JToolBar TB1;
    private JToolBar.Separator TB1TBS1;
    private JButton EDITButton1;
    private JToolBar.Separator TB1TBS3;
    private JButton DELETEButton;
    private JToolBar.Separator TB1TBS4;
    private JButton NEWButton;
    private JPanel P2;
    private JTable Table;
    private JPanel P1;
    private JLabel dataInputTitle;
    private JPanel dataInputPanel;
    private JPanel dataDescriptionPanel;
    private JPanel controlPanel;
    private JSeparator P1S1;
    private JScrollPane P1ScrollPanel;
    private JPanel dataPanel;
    private JLabel P2L1;
    private JScrollPane P2SP1;
    private JButton trackButton;

    public DoctorManageMedicalRecordUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.8f), (int) Math.floor(screenHeight*0.9f));
        setOperation();
        setTable(medicalRecords.getMedicalRecordHeader(), medicalRecords.queryMedicalRecord(null, null, null, null, Cookie.getDoctorCookie().getId(), null, null));
        setComponents();
        P1.setPreferredSize(new Dimension(getSize().width/4, getSize().height));
        P2.setPreferredSize(new Dimension(getSize().width/2, getSize().height));
        setContentPane(MainPanel);
        setTitle("Manage Walk-In Appointment");

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setOperation() {
        JFrame frame = this;

        EDITButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select data for editing");
                    return;
                }
                String editedMedicalRecordId = data[selectedRow][0].toString();
                String editedDesc = data[selectedRow][4].toString();
                String editedDose = data[selectedRow][5].toString();
                String editedPatientICNum = data[selectedRow][6].toString();
                new DoctorEditMedicalRecordUI(frame, editedMedicalRecordId, editedDesc, editedDose, editedPatientICNum);
            }
        });

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select data for cancelling");
                    return;
                }
                String deleteMedicalRecordId = data[selectedRow][0].toString();
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure to cancel Appointment "  + deleteMedicalRecordId +"?", "Deleting Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                medicalRecords.deleteMedicalRecords(deleteMedicalRecordId);
                JOptionPane.showMessageDialog(frame, "Medical Record delete successful.");
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Medical Records");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    JTextField inputField = (JTextField) dataInput;
                    inputField.setText("");
                    inputField.setEditable(false);
                }
            }
        });

        NEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorAddMedicalRecordUI(frame);
            }
        });

        trackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DoctorTrackRecordUI(frame);
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Patient Medical Record");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    JTextField inputField = (JTextField) dataInput;
                    inputField.setText("");
                    inputField.setEditable(false);
                }
                removeControlButton();
            }
        });
    }

    public void updateDataInput() {
        for (int i = 0; i < dataInputPanel.getComponentCount(); i++) {
            JTextField inputField = (JTextField) dataInputPanel.getComponent(i);
            inputField.setText((String) data[selectedRow][i]);
            inputField.setEditable(false);
        }
    }

    public void updateQueryTable(List<MedicalRecordData> queriedMedicalRecordList) {
        medicalRecords = new MedicalRecords();
        setTable(medicalRecords.getMedicalRecordHeader(), queriedMedicalRecordList);
        Table.revalidate();
        Table.repaint();
    }

    public void updateTable() {
        medicalRecords = new MedicalRecords();
        setTable(medicalRecords.getMedicalRecordHeader(), medicalRecords.queryMedicalRecord(null, null, null, null, Cookie.getDoctorCookie().getId(), null, null));
        Table.revalidate();
        Table.repaint();
    }

    public void setComponents() {
        // set layout of data description panel and data input panel
        int descNum = header.length;
        String[] rowSpec = new String[descNum*2 + 1];
        for (int i = 0; i < descNum*2 + 1; i++) {
            rowSpec[i] = (i%2 == 0) ? "50px": "10px";
        }
        String row = String.join(",", rowSpec);
        FormLayout layout = new FormLayout("p", row);
        dataDescriptionPanel.setLayout(layout);
        dataInputPanel.setLayout(layout);

        // insert description in description panel
        for (int i = 0; i < descNum; i++) {
            JLabel descLabel = new JLabel(header[i] + ": ");
            descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            descLabel.setPreferredSize(new Dimension(200, 30));
            descLabel.setHorizontalAlignment(SwingConstants.LEFT);
            dataDescriptionPanel.add(descLabel, new CellConstraints().xy(1, i*2+1));
        }

        // insert input field into input panel
        for (int j = 0; j < header.length; j++) {
            JTextField inputField = new JTextField();
            inputField.setEditable(false);
            inputField.setFont(new Font("Arial", Font.PLAIN, 14));
            inputField.setPreferredSize(new Dimension(100, 30));
            dataInputPanel.add(inputField, new CellConstraints().xy(1, j*2+1));
        }
        controlPanel.setLayout(new FormLayout("p", "50px"));
    }

    public void setTable(List<String> headerList, List<MedicalRecordData> medicalRecordList) {
        // header
        List<String> appointmentHeader = headerList.stream()
                .filter(header -> !header.equals("Doctor ID") && !header.equals("Doctor Name") && !header.equals("Specialization"))
                .toList();
        header = appointmentHeader.toArray();

        // data
        data = new Object[medicalRecordList.size()][header.length];

        for (int i = 0; i < medicalRecordList.size(); i++) {
            MedicalRecordData medicalRecordData = medicalRecordList.get(i);
            data[i][0] = medicalRecordData.getId();
            data[i][1] = medicalRecordData.getDate();
            data[i][2] = medicalRecordData.getTime();
            data[i][3] = medicalRecordData.getMeridiem();
            data[i][4] = medicalRecordData.getDescription();
            data[i][5] = medicalRecordData.getDose();
            data[i][6] = medicalRecordData.getPatientIcNumber();
            data[i][7] = medicalRecordData.getPatientName();
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

        Table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    dataInputTitle.setText("View Walk-In Appointment");
                    selectedRow = Table.getSelectedRow();
                    updateDataInput();
                }
            }
        });

        Table.getTableHeader().setReorderingAllowed(false);
    }

    public JButton createControlButton(String buttonText) {
        JButton controlButton = new JButton(buttonText);
        controlButton.setPreferredSize(new Dimension(100, 30));
        controlPanel.add(controlButton, new CellConstraints().xy(1, 1));
        validate();
        repaint();
        return controlButton;
    }

    public void removeControlButton() {
        if (controlPanel.getComponentCount() > 0) {
            controlPanel.removeAll();
            validate();
            repaint();
        }
    }
}






