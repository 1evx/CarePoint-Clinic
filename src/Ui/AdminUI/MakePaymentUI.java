package Ui.AdminUI;

import Data.Appointment.AppointmentData;
import Data.Appointment.Appointments;
import Data.Medicine.MedicineData;
import Data.Medicine.Medicines;
import Data.Patient.Patients;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class MakePaymentUI extends JDialog{
    Patients patients = new Patients();
    PaymentUI paymentFrame;
    List<MedicineData> medicineList = new Medicines().getMedicineList();
    List<AppointmentData> appointmentList;
    String patientIc;
    AppointmentData selectedAppointment;
    int maxItemNum = 5;
    String[] itemList = new String[maxItemNum];
    int[] itemIndexList = new int[maxItemNum];
    int[] quantityList = new int[maxItemNum];

    private JLabel Title;
    private JPanel PaymentInput;
    private JComboBox AppointmentCombo;
    private JLabel AppointmentLabel;
    private JPanel MainPanel;
    private JPanel Gap1;
    private JPanel ItemPanel;
    private JPanel AppointmentPanel;
    private JPanel PatientIcNumPanel;
    private JTextField PatientIcNumInput;
    private JLabel PatientIcNumLabel;
    private JButton NextButton;
    private JPanel Gap2;
    private JScrollPane ItemScrollPane;
    private JPanel generateInvoicePanel;
    private JPanel MedicineItemPanel;
    private JComboBox itemComboBox1;
    private JPanel itemGap1;
    private JButton addItemBtn1;
    private JComboBox itemComboBox2;
    private JPanel itemGap2;
    private JButton removeItemBtn1;
    private JButton addItemBtn2;
    private JComboBox itemComboBox3;
    private JPanel itemGap3;
    private JButton addItemBtn3;
    private JButton removeItemBtn2;
    private JComboBox itemComboBox4;
    private JPanel itemGap4;
    private JButton addItemBtn4;
    private JButton removeItemBtn4;
    private JComboBox itemComboBox5;
    private JPanel itemGap5;
    private JButton removeItemBtn5;
    private JLabel medicinePaymentList;
    private JButton generateInvoiceBtn;
    private JSpinner spinner1;
    private JButton resetBtn;

    public MakePaymentUI(PaymentUI paymentFrame) {
        super(null, "Make Patient Payment", ModalityType.APPLICATION_MODAL);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.5f), (int) Math.floor(screenHeight*0.9f));
        this.paymentFrame = paymentFrame;
        setOperation();
        setItem();
        setContentPane(MainPanel);
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setOperation() {
        JDialog dialog = this;
        NextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String icNum = PatientIcNumInput.getText();
                Object[] checkStatus = patients.checkPatientIc(icNum);
                if (checkStatus[0] != null) {
                    JOptionPane.showMessageDialog(dialog, checkStatus[0]);
                    return;
                }
                patientIc = icNum;
                AppointmentPanel.setVisible(true);
                NextButton.setVisible(false);
                PatientIcNumInput.setEditable(false);

                Appointments patientAppointments = new Appointments();
                appointmentList = patientAppointments.queryAppointment(null, null, null, null, icNum, null, null, null, null).reversed();
                AppointmentCombo.addItem("- none -");
                AppointmentCombo.addItem("No Appointment");
                for (AppointmentData appointment: appointmentList) {
                    AppointmentCombo.addItem(String.format("%s (%s %s%s)", appointment.getId(), appointment.getDate(), appointment.getTime(), appointment.getMeridiem()));
                }
                AppointmentCombo.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public void paint(Graphics g) {
                        setForeground(Color.BLACK);
                        super.paint(g);
                    }
                });
            }
        });

        AppointmentCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedItem = e.getItem().toString();
                    if (selectedItem.equals("- none -")) {
                        return;
                    }
                    if (!selectedItem.equals("No Appointment")) {
                        selectedAppointment = appointmentList.get(AppointmentCombo.getSelectedIndex() - 2);
                    }
                    AppointmentCombo.setEnabled(false);
                    generateInvoiceBtn.setVisible(true);
                    ItemScrollPane.setVisible(true);
                    itemList[0] = "- none -";
                    Arrays.fill(quantityList, 1);
                }
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                new MakePaymentUI(paymentFrame);
            }
        });

        generateInvoiceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check all item is selected, no anyone is empty
                if (Arrays.asList(itemList).contains("- none -")) {
                    JOptionPane.showMessageDialog(dialog, "Please ensure all medicine item is selected.");
                    return;
                }
                String appointmentId = null;
                String appointmentDatetime = null;
                if (selectedAppointment != null) {
                    appointmentId = selectedAppointment.getId();
                    appointmentDatetime = selectedAppointment.getDate() + " " + selectedAppointment.getTime() + selectedAppointment.getMeridiem();
                }
                List<MedicineData> selectedMedicines = Arrays.stream(itemIndexList)
                        .filter(index -> index > 0) // Filter out invalid indices
                        .mapToObj(index -> medicineList.get(index - 1)) // Map valid indices to MedicineData objects
                        .toList();
                new InvoiceUI(patientIc, appointmentId, appointmentDatetime, selectedMedicines, quantityList, dialog, paymentFrame);
            }
        });
    }

    private void setItem() {
        // create array that store medicine selection
        Object[] medicineArray = Stream.concat(
                    Stream.of("- none -"),
                    medicineList.stream().map(medicine -> medicine.getDescription() + " (" + medicine.getId() + ")")
                ).toArray();

        JPanel subPanel;
        int itemIndex;
        JComboBox<Object> itemComboBox = null;
        JSpinner quantityField = null;
        // set component for each panel
        for (int i=0; i<ItemPanel.getComponentCount(); i++) {
            subPanel = (JPanel) ItemPanel.getComponent(i);
            // the panel is add button and remove button
            if (i%2 != 0) {
                setItemButton(subPanel, i, itemComboBox, quantityField);
                continue;
            }
            // insert medicine array into combo box
            itemComboBox = (JComboBox<Object>) subPanel.getComponent(1);
            itemComboBox.setModel(new DefaultComboBoxModel<>(medicineArray));
            itemComboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public void paint(Graphics g) {
                    setForeground(Color.BLACK);
                    super.paint(g);
                }
            });
            // when medicine selected, add the medicine into itemList
            itemIndex = i/2;
            int index = itemIndex;
            JComboBox<Object> currentItemComboBox = itemComboBox;
            itemComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    itemList[index] = (String) currentItemComboBox.getSelectedItem();
                    itemIndexList[index] = currentItemComboBox.getSelectedIndex();
                }
            });

            quantityField = (JSpinner) subPanel.getComponent(3);
            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
            quantityField.setModel(spinnerModel);
            JSpinner currentQuantityField = quantityField;
            // set JSpinner font always black color
            ((JSpinner.DefaultEditor) quantityField.getEditor()).getTextField().setDisabledTextColor(Color.BLACK);
            quantityField.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    quantityList[index] = (int) currentQuantityField.getValue();
                }
            });
        }
    }

    private void setItemButton(JPanel subPanel, int currentPanelIndex, JComboBox<Object> itemComboBox, JSpinner quantityField) {
        JButton itemButton;
        final JPanel[] nextSubPanel = new JPanel[2];
        final JPanel[] lastSubPanel = new JPanel[2];
        String buttonName;
        for (Component comp: subPanel.getComponents()) {
            if (!(comp instanceof JButton)) {
                continue;
            }
            itemButton = (JButton) comp;
            buttonName = itemButton.getName();
            if (buttonName.equals("remove")) {
                lastSubPanel[0] = (JPanel) ItemPanel.getComponent(currentPanelIndex-1);
                lastSubPanel[1] = (JPanel) ItemPanel.getComponent(currentPanelIndex-2);
                itemButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        itemComboBox.setSelectedItem("- none -");
                        quantityField.setValue(1);
                        lastSubPanel[0].setVisible(false);
                        lastSubPanel[1].setVisible(true);
                        subPanel.setVisible(false);
                        itemList[currentPanelIndex/2] = null;
                        itemIndexList[currentPanelIndex/2] = 0;
                    }
                });
                continue;
            }
            nextSubPanel[0] = (JPanel) ItemPanel.getComponent(currentPanelIndex+1);
            nextSubPanel[1] = (JPanel) ItemPanel.getComponent(currentPanelIndex+2);
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nextSubPanel[0].setVisible(true);
                    nextSubPanel[1].setVisible(true);
                    subPanel.setVisible(false);
                    itemList[currentPanelIndex/2 + 1] = "- none -";
                    itemIndexList[currentPanelIndex/2 + 1] = -1;
                }
            });
        }
    }
}

