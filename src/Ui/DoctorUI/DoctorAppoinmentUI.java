package Ui.DoctorUI;

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Authentication.Cookie;
import Data.Appointment.AppointmentData;
import Data.Appointment.Appointments;
import Data.TimeSlot.TimeSlotData;
import Data.TimeSlot.TimeSlots;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

public class DoctorAppoinmentUI extends JFrame{
    TimeSlots timeSlots = new TimeSlots();
    Appointments appointments = new Appointments();
    Object[] header;
    Object[][] data;
    String[] timeSlotIdList;
    int selectedRow = -1;
    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JPanel Main3;
    private JPanel MainPanel;
    private JLabel Title;
    private JToolBar TB1;
    private JToolBar.Separator TB1TBS1;
    private JToolBar.Separator TB1TBS3;
    private JButton DELETEButton;
    private JToolBar.Separator TB1TBS4;
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

    public static void main(String[] args) {
        new DoctorAppoinmentUI();
    }


    public DoctorAppoinmentUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.8f), (int) Math.floor(screenHeight*0.9f));
        // get this doctor's time slot
        List<TimeSlotData> queriedTimeSlot = timeSlots.queryTimeSlot(null, null, null, null, Cookie.getDoctorCookie().getId(), null, null, null);
        List<TimeSlotData> doctorTimeSlotList = queriedTimeSlot.stream()
                .filter(timeSlot -> !currentDate.isAfter(LocalDate.parse(timeSlot.getDate(), dateTimeFormatter)))
                .toList();
        List<AppointmentData> doctorAppointment;
        List<AppointmentData> doctorAppointmentList = new ArrayList<>();
        // based on the queried time slot id, track all this doctor's appointment
        // iterate doctorTimeSlotList and set each time slot into doctorTimeSlot
        for (TimeSlotData doctorTimeSlot: doctorTimeSlotList) {
            // query appointment that the time slot id is the doctorTimeSlot's id
            doctorAppointment = appointments.queryAppointment(null, null, null, null, null, null, null, "active", doctorTimeSlot.getId());
            // if appointment for this time slot is available, then add the appointment into doctorAppointmentList
            if (!doctorAppointment.isEmpty()) {
                doctorAppointmentList.add(doctorAppointment.getFirst());
            }
        }
        setOperation();
        setTable(appointments.getAppointmentHeaders(), doctorAppointmentList);
        setComponents();
        P1.setPreferredSize(new Dimension(getSize().width/4, getSize().height));
        P2.setPreferredSize(new Dimension(getSize().width/2, getSize().height));
        setContentPane(MainPanel);
        setTitle("View Individual Appointment");

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setOperation() {
        JFrame frame = this;

        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select data for canceling");
                    return;
                }
                String deletedAppointmentId = data[selectedRow][0].toString();
                String deletedAppointmentSlotTimeId = timeSlotIdList[selectedRow];
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure to cancel Appointment "  + deletedAppointmentId +"?", "Deleting Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                    return;
                }
                appointments.cancelAppointment(deletedAppointmentId, deletedAppointmentSlotTimeId);
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Individual Appointment");
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

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataInputTitle.setText("Search Individual Appointment");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> genderComboBox) {
                        genderComboBox.setSelectedItem("- none -");
                        genderComboBox.setEnabled(true);
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
                            if (dataInputPanel.getComponent(i) instanceof JComboBox<?> genderComboBox) {
                                value = (String) genderComboBox.getSelectedItem();
                                valueArray[i] = (value.equals("- none -")) ? null : value;
                                continue;
                            }
                            else if (dataInputPanel.getComponent(i) instanceof JDateChooser dateChooser) {
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                valueArray[i] = (dateChooser.getDate() == null)? null: formatter.format(dateChooser.getDate());
                                continue;
                            }
                            JTextField inputField = (JTextField) dataInputPanel.getComponents()[i];
                            value = inputField.getText();
                            valueArray[i] = (value.isBlank())? null: value;
                        }
                        List<AppointmentData> queriedAppointmentlist = appointments.queryAppointment(valueArray[0], valueArray[1], valueArray[2],valueArray[3], valueArray[4], valueArray[5], valueArray[6], "active", null);
                        JOptionPane.showMessageDialog(frame, String.format("%d result found", queriedAppointmentlist.size()));
                        updateQueryTable(queriedAppointmentlist);
                    }
                });
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Individual Appointment");
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
                removeControlButton();
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

    public void updateQueryTable(List<AppointmentData> queriedAppointmentList) {
        setTable(appointments.getAppointmentHeaders(), queriedAppointmentList);
        Table.revalidate();
        Table.repaint();
    }

    public void updateTable() {
        timeSlots = new TimeSlots();
        appointments = new Appointments();
        List<TimeSlotData> queriedTimeSlot = timeSlots.queryTimeSlot(null, null, null, null, Cookie.getDoctorCookie().getId(), null, null, null);
        List<TimeSlotData> doctorTimeSlotList = queriedTimeSlot.stream()
                .filter(timeSlot -> !currentDate.isAfter(LocalDate.parse(timeSlot.getDate(), dateTimeFormatter)))
                .toList();
        List<AppointmentData> doctorAppointment;
        List<AppointmentData> doctorAppointmentList = new ArrayList<>();
        for (TimeSlotData doctorTimeSlot: doctorTimeSlotList) {
            doctorAppointment = appointments.queryAppointment(null, null, null, null, null, null, null, "active", doctorTimeSlot.getId());
            if (!doctorAppointment.isEmpty()) {
                doctorAppointmentList.add(doctorAppointment.getFirst());
            }
        }
        setTable(appointments.getAppointmentHeaders(), doctorAppointmentList);
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
            else if (header[j].equals("Time") || header[j].equals("Meridiem") || header[j].equals("Specialization")) {
                String combo = (String) header[j];
                String[] list = switch (combo) {
                    case "Time" -> generateTimeSlots();
                    case "Meridiem" -> new String[]{"- none -", "a.m.", "p.m."};
                    case "Specialization" -> new String[]{"- none -", "general"};
                    default -> throw new IllegalStateException("Unexpected value: " + combo);
                };
                JComboBox<String> comboBox = new JComboBox<>(list);
                comboBox.setEditable(false);
                comboBox.setEnabled(false);
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
            dataInputPanel.add(comp, new CellConstraints().xy(1, j * 2 + 1));
        }
        controlPanel.setLayout(new FormLayout("p", "50px"));
    }

    public void setTable(List<String> headerList, List<AppointmentData> appointmentList) {
        List<String> appointmentHeader = headerList.stream()
                .filter(header -> !header.equals("Status") && !header.equals("Time Slot ID"))
                .toList();
        header = appointmentHeader.toArray();

        data = new Object[appointmentList.size()][header.length];
        timeSlotIdList = new String[appointmentList.size()];

        for (int i = 0; i < appointmentList.size(); i++) {
            AppointmentData appointmentData = appointmentList.get(i);
            data[i][0] = appointmentData.getId();
            data[i][1] = appointmentData.getDate();
            data[i][2] = appointmentData.getTime();
            data[i][3] = appointmentData.getMeridiem();
            data[i][4] = appointmentData.getPatientIcNumber();
            data[i][5] = appointmentData.getSpecialization();
            data[i][6] = appointmentData.getType();
            timeSlotIdList[i] = appointmentData.getTimeSlotId();
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
                    dataInputTitle.setText("View Individual Appointment");
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
