package Ui.AdminUI;

import Data.TimeSlot.TimeSlotData;
import Data.TimeSlot.TimeSlots;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TimeSlotUI extends JFrame{
    TimeSlots timeSlots = new TimeSlots();
    Object[] header;
    Object[][] data;
    int selectedRow = -1;
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JLabel Title;
    private JToolBar TB1;
    private JToolBar.Separator TB1TBS1;
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
    private JButton searchButton;

    public TimeSlotUI(){
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.8f), (int) Math.floor(screenHeight*0.9f));
        setOperation();
        List<TimeSlotData> queriedTimeSlot = timeSlots.queryTimeSlot(null, null, null, null, null, null, null, "available");
        List<TimeSlotData> timeSlotList = queriedTimeSlot.stream()
                .filter(timeSlot -> !currentDate.isAfter(LocalDate.parse(timeSlot.getDate(), dateTimeFormatter)))
                .toList();
        setTable(timeSlots.getTimeSlotHeaders(), timeSlotList);
        setComponents();
        P1.setPreferredSize(new Dimension(getSize().width/4, getSize().height));
        P2.setPreferredSize(new Dimension(getSize().width/2, getSize().height));
        setContentPane(MainPanel);
        setTitle("Check Available Time Slots");

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setOperation() {
        JFrame frame = this;
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataInputTitle.setText("Search Available Time Slot");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> comboBox) {
                        comboBox.setSelectedItem("- none -");
                        comboBox.setEnabled(true);
                        continue;
                    }
                    else if (dataInput instanceof JDateChooser dateChooser) {
                        dateChooser.setDate(null);
                        dateChooser.setEnabled(true);
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
                            if (dataInputPanel.getComponent(i) instanceof JComboBox<?> comboBox) {
                                value = (String) comboBox.getSelectedItem();
                                valueArray[i] = (value.equals("- none -")) ? null : value;
                                continue;
                            } else if (dataInputPanel.getComponent(i) instanceof JDateChooser dateChooser) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                valueArray[i] = (dateChooser.getDate() == null) ? null : formatter.format(dateChooser.getDate());
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            value = inputField.getText();
                            valueArray[i] = (value.isBlank()) ? null : value;
                        }
                        valueArray[1] = (currentDate.isBefore(LocalDate.parse(valueArray[1], dateTimeFormatter)) ||
                                currentDate.equals(LocalDate.parse(valueArray[1], dateTimeFormatter)))
                                ? valueArray[1]
                                : "";
                        List<TimeSlotData> queriedTimeSlotList = timeSlots.queryTimeSlot(valueArray[0], valueArray[1], valueArray[2],valueArray[3], null, valueArray[4], valueArray[5], "available");
                        JOptionPane.showMessageDialog(frame, String.format("%d result found", queriedTimeSlotList.size()));
                        updateQueryTable(queriedTimeSlotList);
                    }
                });
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Time Slots");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setSelectedItem("- none -");
                        genderComboBox.setEnabled(false);
                        continue;
                    }
                    else if (dataInput instanceof JDateChooser dateChooser) {
                        dateChooser.setDate(null);
                        dateChooser.setEnabled(false);
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
            else if (dataInputPanel.getComponents()[i] instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) dataInputPanel.getComponent(i);
                try {
                    dateChooser.setDate(new SimpleDateFormat("yyy-MM-dd").parse((data[selectedRow][i]).toString()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                dateChooser.setEnabled(false);
            }
        }
    }

    public void updateQueryTable(List<TimeSlotData> queriedTimeSlotList) {
        setTable(timeSlots.getTimeSlotHeaders(), queriedTimeSlotList);
        Table.revalidate();
        Table.repaint();
    }

    public void updateTable() {
        timeSlots = new TimeSlots();
        List<TimeSlotData> queriedTimeSlot = timeSlots.queryTimeSlot(null, null, null, null, null, null, null, "available");
        List<TimeSlotData> timeSlotList = queriedTimeSlot.stream()
                .filter(timeSlot -> !currentDate.isAfter(LocalDate.parse(timeSlot.getDate(), dateTimeFormatter)))
                .toList();
        setTable(timeSlots.getTimeSlotHeaders(), timeSlotList);
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
            if (header[j].equals("Date")) {
                JDateChooser dateChooser = new JDateChooser();
                dateChooser.setDateFormatString("yyyy-MM-dd");
                dateChooser.setEnabled(false);
                JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) dateChooser.getDateEditor().getUiComponent();
                dateEditor.setDisabledTextColor(Color.BLACK);
                dateEditor.setEditable(false);
                comp = dateChooser;
            }
            else if (header[j].equals("Time") || header[j].equals("Meridiem")) {
                String combo = (String) header[j];
                String[] list = switch (combo) {
                    case "Time" -> generateTimeSlots();
                    case "Meridiem" -> new String[]{"- none -", "a.m.", "p.m."};
                    default -> throw new IllegalStateException("Unexpected value: " + combo);
                };
                JComboBox<String> comboBox = new JComboBox<>(list);
                comboBox.setEnabled(false);
                comboBox.setEditable(false);
                comboBox.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public void paint(Graphics g) {
                        setForeground(Color.BLACK);
                        super.paint(g);
                    }
                });
                comp = comboBox;
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

    public void setTable(List<String> headerList, List<TimeSlotData> timeSlotDataList) {
        List<String> doctorHeader = headerList.stream()
                .filter(header -> !header.equals("Status") && !header.equals("Doctor ID"))
                .toList();
        header = doctorHeader.toArray();

        data = new Object[timeSlotDataList.size()][header.length];

        for (int i = 0; i < timeSlotDataList.size(); i++) {
            TimeSlotData timeSlotData = timeSlotDataList.get(i);
            data[i][0] = timeSlotData.getId();
            data[i][1] = timeSlotData.getDate();
            data[i][2] = timeSlotData.getTime();
            data[i][3] = timeSlotData.getMeridiem();
            data[i][4] = timeSlotData.getDoctorName();
            data[i][5] = timeSlotData.getDoctorSpecialization();
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
                    dataInputTitle.setText("View Time Slot");
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

    public String[] generateTimeSlots() {
        String[] timeSlots = new String[25];
        timeSlots[0] = "- none -";
        int slotIndex = 1;
        int hour12 = 0;
        for (int hour = 9; hour < 21; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                hour12 = hour%12;
                if (hour12 == 0) {
                    hour12 = 12;
                }
                timeSlots[slotIndex++] = String.format("%02d:%02d", hour12, minute);
            }
        }

        return timeSlots;
    }
}
