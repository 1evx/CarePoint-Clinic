package Ui.AdminUI;

import Data.Medicine.MedicineData;
import Data.Medicine.Medicines;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

public class MedicineUI extends JFrame {
    Medicines medicines = new Medicines();
    Object[] header;
    Object[][] data;
    int selectedRow = -1;
    DecimalFormat df = new DecimalFormat("#0.00");

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
    private JPanel MainPanel;

    public MedicineUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.8f), (int) Math.floor(screenHeight*0.9f));
        setOperation();
        setTable(medicines.getMedicineHeader(), medicines.getMedicineList());
        setComponents();
        P1.setPreferredSize(new Dimension(getSize().width/4, getSize().height));
        P2.setPreferredSize(new Dimension(getSize().width/2, getSize().height));
        setContentPane(MainPanel);
        setTitle("Manage Patient Account");

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
                dataInputTitle.setText("Edit Medicine");
                removeControlButton();
                updateDataInput();
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JFormattedTextField priceField) {
                        priceField.setEditable(true);
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
                            if (dataInputPanel.getComponent(i) instanceof JFormattedTextField priceField) {
                                valueArray[i] = df.format(priceField.getValue());
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            String value = inputField.getText();
                            valueArray[i] = value.trim();
                        }
                        String editMessage = medicines.editMedicine(valueArray[0], valueArray[1], valueArray[2], valueArray[3]);
                        JOptionPane.showMessageDialog(frame, editMessage);
                        if (editMessage.equals("Medicine updated successful.")) {
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
                String deletedMedicineId = data[selectedRow][0].toString();
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure to delete Medicine "  + deletedMedicineId +"?", "Deleting Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                boolean deleteStatus = medicines.deleteMedicine(deletedMedicineId);
                if (!deleteStatus) {
                    JOptionPane.showMessageDialog(frame, "Medicine deleted fail.\nThis medicine has been sold.");
                    return;
                }
                JOptionPane.showMessageDialog(frame, "Medicine deleted successful.");
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Medicine Details");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JFormattedTextField priceField) {
                        priceField.setValue(null);
                        priceField.setEditable(false);
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
                dataInputTitle.setText("Add New Medicine");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JFormattedTextField priceField) {
                        priceField.setValue(0);
                        priceField.setEditable(true);
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
                            if (dataInputPanel.getComponent(i) instanceof JFormattedTextField priceField) {
                                valueArray[i] = df.format(priceField.getValue());
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            String value = inputField.getText();
                            valueArray[i] = value.trim();
                        }
                        String addMessage = medicines.addNewMedicine(valueArray[0], valueArray[1], valueArray[2], valueArray[3]);
                        JOptionPane.showMessageDialog(frame, addMessage);
                        if (addMessage.equals("Medicine Information add successful.")) {
                            updateTable();
                        }
                    }
                });
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataInputTitle.setText("Search Medicine");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JFormattedTextField priceField) {
                        priceField.setValue(0);
                        priceField.setEditable(true);
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
                            if (dataInputPanel.getComponent(i) instanceof JFormattedTextField priceField) {
                                String amount = df.format(priceField.getValue());
                                valueArray[i] = (amount.equals("0.00")) ? null : amount;
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            value = inputField.getText();
                            valueArray[i] = (value.isBlank())? null: value;
                        }
                        List<MedicineData> queriedMedicineList = medicines.queryMedicine(valueArray[0], valueArray[1], valueArray[2],valueArray[3]);
                        JOptionPane.showMessageDialog(frame, String.format("%d result found", queriedMedicineList.size()));
                        updateQueryTable(queriedMedicineList);
                    }
                });
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Medicine Details");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JFormattedTextField priceField) {
                        priceField.setValue(null);
                        priceField.setEditable(false);
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
            if (dataInputPanel.getComponents()[i] instanceof JFormattedTextField priceField) {
                Float amount = Float.parseFloat((String) data[selectedRow][i]);
                priceField.setValue(amount);
                priceField.setEditable(false);
                continue;
            }
            JTextField inputField = (JTextField) dataInputPanel.getComponent(i);
            inputField.setText((String) data[selectedRow][i]);
            inputField.setEditable(false);
        }
    }

    public void updateQueryTable(List<MedicineData> queriedMedicineList) {
        medicines = new Medicines();
        setTable(medicines.getMedicineHeader(), queriedMedicineList);
        Table.revalidate();
        Table.repaint();
    }

    public void updateTable() {
        medicines = new Medicines();
        setTable(medicines.getMedicineHeader(), medicines.getMedicineList());
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
            if (header[j].equals("Price")) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ms", "MY"));
                format.setMinimumFractionDigits(2);
                format.setMaximumFractionDigits(2);
                NumberFormatter formatter = new NumberFormatter(format) {
                    @Override
                    public Object stringToValue(String text) throws ParseException {
                        if (text == null || text.trim().isEmpty()) {
                            return null;
                        }
                        return super.stringToValue(text);
                    }
                };
                formatter.setMinimum(0.0);
                formatter.setAllowsInvalid(false);
                JFormattedTextField priceField = new JFormattedTextField(formatter);
                priceField.setColumns(10);
                priceField.setEditable(false);
                comp = priceField;
            }
            else {
                JTextField inputField = new JTextField();
                inputField.setEditable(false);
                comp = inputField;
            }
            comp.setFocusable(true);
            comp.setFont(new Font("Arial", Font.PLAIN, 14));
            comp.setPreferredSize(new Dimension(100, 30));
            dataInputPanel.add(comp, new CellConstraints().xy(1, j*2+1));
        }
        controlPanel.setLayout(new FormLayout("p", "50px"));
    }

    public void setTable(List<String> headerList, List<MedicineData> medicineList) {
        header = headerList.toArray();

        data = new Object[medicineList.size()][header.length];

        for (int i = 0; i < medicineList.size(); i++) {
            MedicineData medicineData = medicineList.get(i);
            data[i][0] = medicineData.getId();
            data[i][1] = medicineData.getDescription();
            data[i][2] = medicineData.getPrice();
            data[i][3] = medicineData.getSupplierName();
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
                    dataInputTitle.setText("View Medicine Details");
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
