package Ui.AdminUI;

import Data.Doctor.DoctorData;
import Data.Doctor.Doctors;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DoctorAccUI extends JFrame {
    Doctors doctors = new Doctors();
    Object[] header;
    Object[][] data;
    int selectedRow = -1;

    private JPanel MainPanel;
    private JLabel Title;
    private JToolBar TB1;
    private JToolBar.Separator TB1TBS1;
    private JButton EDITButton1;
    private JToolBar.Separator TB1TBS3;
    private JButton DELETEButton;
    private JToolBar.Separator TB1TBS4;
    private JButton NEWButton;
    private JButton searchButton;
    private JButton refreshButton;
    private JPanel P2;
    private JLabel P2L1;
    private JScrollPane P2SP1;
    private JTable Table;
    private JPanel P1;
    private JLabel dataInputTitle;
    private JSeparator P1S1;
    private JScrollPane P1ScrollPanel;
    private JPanel dataPanel;
    private JPanel dataInputPanel;
    private JPanel dataDescriptionPanel;
    private JPanel controlPanel;

    public DoctorAccUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.8f), (int) Math.floor(screenHeight*0.9f));
        setOperation();
        setTable(doctors.getDoctorHeaders(), doctors.getDoctorList());
        setComponents();
        P1.setPreferredSize(new Dimension(getSize().width/4, getSize().height));
        P2.setPreferredSize(new Dimension(getSize().width/2, getSize().height));
        setContentPane(MainPanel);
        setTitle("Manage Doctor Account");

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
                dataInputTitle.setText("Edit Doctor");
                removeControlButton();
                updateDataInput();
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setEnabled(true);
                        continue;
                    }
                    JTextField inputField = (JTextField) dataInput;
                    if (dataInput != dataInputPanel.getComponents()[0]) {
                        inputField.setEditable(true);
                    }
                }
                JButton controlButton = createControlButton("Save");
                controlButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int choice = JOptionPane.showConfirmDialog(frame, "Are you sure to edit the data?", "Editing Confirmation", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                            return;
                        }
                        String[] valueArray = new String[dataInputPanel.getComponents().length];
                        for (int i = 0; i < dataInputPanel.getComponents().length; i++) {
                            if (dataInputPanel.getComponent(i) instanceof JComboBox<?> genderComboBox) {
                                valueArray[i] = (String) genderComboBox.getSelectedItem();
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            String value = inputField.getText();
                            valueArray[i] = value.trim();
                        }
                        String editMessage = doctors.editDoctor(valueArray[0], valueArray[1], valueArray[2], valueArray[3], valueArray[4], valueArray[5], valueArray[6], valueArray[7]);
                        JOptionPane.showMessageDialog(frame, editMessage);
                        if (editMessage.equals("Doctor data edit successful.")) {
                            updateTable();
                        }
                    }
                });
            }
        });

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select data for deleting");
                    return;
                }
                String deletedDoctorId = data[selectedRow][0].toString();
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure to delete Doctor " + deletedDoctorId + "?", "Deleting Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                doctors.deleteDoctor(deletedDoctorId);
                JOptionPane.showMessageDialog(frame, "Doctor Account deleted successful.");
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Doctor");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setSelectedItem("- none -");
                        genderComboBox.setEnabled(false);
                        continue;
                    }
                    JTextField inputField = (JTextField) dataInput;
                    inputField.setText("");
                    inputField.setEditable(false);
                }
            }
        });

        NEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataInputTitle.setText("Register Doctor");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setSelectedItem("- none -");
                        genderComboBox.setEnabled(true);
                        continue;
                    }
                    JTextField inputField = (JTextField) dataInput;
                    if (dataInput != dataInputPanel.getComponents()[0]) {
                        inputField.setText("");
                        inputField.setEditable(true);
                        continue;
                    }
                    inputField.setText("Auto Generated");
                }

                removeControlButton();

                JButton controlButton = createControlButton("Save");
                controlButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String[] valueArray = new String[dataInputPanel.getComponents().length];
                        valueArray[0] = null;
                        for (int i = 1; i < dataInputPanel.getComponents().length; i++) {
                            if (dataInputPanel.getComponent(i) instanceof JComboBox<?> genderComboBox) {
                                valueArray[i] = (String) genderComboBox.getSelectedItem();
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            String value = inputField.getText();
                            valueArray[i] = value.trim();
                        }
                        String registerMessage = doctors.registerDoctor(valueArray[0], valueArray[1], valueArray[2], valueArray[1],valueArray[3], valueArray[4], valueArray[5], valueArray[6], valueArray[7]);
                        JOptionPane.showMessageDialog(frame, registerMessage);
                        if (registerMessage.equals("Doctor data register successful.")) {
                            updateTable();
                        }
                    }
                });
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataInputTitle.setText("Search Doctor");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setSelectedItem("- none -");
                        genderComboBox.setEnabled(true);
                        continue;
                    }
                    JTextField inputField = (JTextField) dataInput;
                    inputField.setText("");
                    inputField.setEditable(true);
                }
                removeControlButton();

                JButton controlButton = createControlButton("Search");
                controlButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        selectedRow = -1;
                        String[] valueArray = new String[dataInputPanel.getComponents().length];
                        String value;
                        for (int i = 0; i < dataInputPanel.getComponents().length; i++) {
                            if (dataInputPanel.getComponent(i) instanceof JComboBox<?> genderComboBox) {
                                value = (String) genderComboBox.getSelectedItem();
                                valueArray[i] = (value.equals("- none -")) ? null : value;
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            value = inputField.getText();
                            valueArray[i] = (value.isBlank())? null: value;
                        }
                        List<DoctorData> queriedDoctorList = doctors.queryDoctor(valueArray[0], valueArray[1], valueArray[2],valueArray[3], valueArray[4], valueArray[5], valueArray[6], valueArray[7]);
                        JOptionPane.showMessageDialog(frame, String.format("%d result found", queriedDoctorList.size()));
                        updateQueryTable(queriedDoctorList);
                    }
                });
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Doctors");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setSelectedItem("- none -");
                        genderComboBox.setEnabled(false);
                        continue;
                    }
                    JTextField inputField = (JTextField) dataInput;
                    inputField.setText("");
                    inputField.setEditable(false);
                }
            }
        });
    }

    public void updateDataInput() {
        for (int i = 0; i < dataInputPanel.getComponentCount(); i++) {
            if (dataInputPanel.getComponents()[i] instanceof JTextField) {
                JTextField inputField = (JTextField) dataInputPanel.getComponent(i);
                inputField.setText((String) data[selectedRow][i]);
                inputField.setEditable(false);
            }
            else if (dataInputPanel.getComponents()[i] instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) dataInputPanel.getComponent(i);
                comboBox.setSelectedItem(data[selectedRow][i]);
                comboBox.setEnabled(false);
            }
        }
    }

    public void updateQueryTable(List<DoctorData> queriedDoctorList) {
        doctors = new Doctors();
        setTable(doctors.getDoctorHeaders(), queriedDoctorList);
        Table.revalidate();
        Table.repaint();
    }

    public void updateTable() {
        doctors = new Doctors();
        setTable(doctors.getDoctorHeaders(), doctors.getDoctorList());
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

        // insert description and input field into both panel
        for (int i = 0; i < descNum; i++) {
            JLabel descLabel = new JLabel(header[i] + ": ");
            descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            descLabel.setPreferredSize(new Dimension(200, 30));
            descLabel.setHorizontalAlignment(SwingConstants.LEFT);
            dataDescriptionPanel.add(descLabel, new CellConstraints().xy(1, i*2+1));
        }
        Component comp;
        for (int j = 0; j < header.length; j++) {
            if (header[j].equals("Gender")) {
                String[] genderList = {"- none -", "male", "female"};
                JComboBox<String> genderComboBox = new JComboBox<>(genderList);
                genderComboBox.setEditable(false);
                genderComboBox.setEnabled(false);
                genderComboBox.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public void paint(Graphics g) {
                        setForeground(Color.BLACK);
                        super.paint(g);
                    }
                });
                comp = genderComboBox;
            }
            else {
                JTextField inputField = new JTextField();
                inputField.setEditable(false);
                comp = inputField;
            }
            comp.setFont(new Font("Arial", Font.PLAIN, 14));
            comp.setPreferredSize(new Dimension(100, 30));
            dataInputPanel.add(comp, new CellConstraints().xy(1, j*2+1));
        }
        controlPanel.setLayout(new FormLayout("p", "50px"));
    }

    public void setTable(List<String> headerList, List<DoctorData> doctorList) {
        List<String> doctorHeader = headerList.stream()
                .filter(header -> !header.equals("Password"))
                .toList();
        header = doctorHeader.toArray();

        data = new Object[doctorList.size()][header.length];

        for (int i = 0; i < doctorList.size(); i++) {
            DoctorData doctorData = doctorList.get(i);
            data[i][0] = doctorData.getId();
            data[i][1] = doctorData.getName();
            data[i][2] = doctorData.getIcNumber();
            data[i][3] = doctorData.getGender();
            data[i][4] = doctorData.getContact();
            data[i][5] = doctorData.getSpecialization();
            data[i][6] = doctorData.getEducation();
            data[i][7] = doctorData.getExperienceYear();
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
                    dataInputTitle.setText("View Doctor");
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
