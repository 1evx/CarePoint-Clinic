package Ui.AdminUI;

import Data.Medicine.MedicineData;
import Data.Patient.Patients;
import Data.Payment.Payments;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class InvoiceUI extends JDialog {
    Patients patients = new Patients();
    LocalDateTime currentDate = LocalDateTime.now();
    float sumOfAmount = 0;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
    DecimalFormat df = new DecimalFormat("#0.00");

    String patientIcNum;
    String patientName;
    String appointmentId;
    String appointmentDatetime;
    List<MedicineData> medicineItems;
    PaymentUI paymentFrame;
    int[] quantityList;
    List<String> medicineItemIdList = new ArrayList<String>();
    JDialog parent;

    private JPanel MainPanel;
    private JButton confirmBtn;
    private JLabel totalAmountLabel;
    private JTable itemTable;
    private JLabel patientNameLabel;
    private JLabel patientIcLabel;
    private JLabel paymentForLabel;
    private JLabel invoiceDateLabel;
    private JLabel invoiceTimeLabel;

    public InvoiceUI(String patientIcNum, String appointmentId, String appointmentDatetime, List<MedicineData> medicineItems, int[] quantityList, JDialog parent, PaymentUI paymentFrame) {
        super(null, "Patient Invoice", ModalityType.APPLICATION_MODAL);
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        this.patientIcNum = patientIcNum;
        this.appointmentId = appointmentId;
        this.appointmentDatetime = appointmentDatetime;
        this.medicineItems = medicineItems;
        this.quantityList = quantityList;
        this.parent = parent;
        this.paymentFrame = paymentFrame;

        setSize((int) Math.floor(screenWidth*0.4f), (int) Math.floor(screenHeight*0.7f));
        setOperation();
        setInvoiceData();
        setContentPane(MainPanel);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setInvoiceData() {
        patientName = patients.queryPatient(null, null, patientIcNum, null, null, null).getFirst().getName();
        patientNameLabel.setText("Patient Name: " + patientName);
        patientIcLabel.setText("Patient IC Number: " + patientIcNum);

        if (appointmentId == null) {
            paymentForLabel.setText("Pay For: general sales");
        }
        else {
            paymentForLabel.setText("Pay For: " + appointmentId + " (" + appointmentDatetime + ") ");
        }

        invoiceDateLabel.setText("Invoice Date: " + currentDate.format(dateFormatter));
        invoiceTimeLabel.setText("Invoice Time: " + currentDate.format(timeFormatter));

        String[] header = {"Medicine Code", "Medicine Description", "Unit Price", "Quantity", "Total Amount"};
        String[][] data = new String[medicineItems.size() + 2][header.length];
        String medicineId;
        String unitPrice;
        String quantity;
        float totalAmount = 0;
        for (int i = 0; i < medicineItems.size(); i++) {
            MedicineData medicine = medicineItems.get(i);
            medicineId = medicine.getId();
            medicineItemIdList.add(medicineId);
            data[i][0] = medicineId;
            data[i][1] = medicine.getDescription();
            unitPrice = medicine.getPrice();
            quantity = Integer.toString(quantityList[i]);
            totalAmount = Float.parseFloat(unitPrice)*Integer.parseInt(quantity);
            sumOfAmount += totalAmount;
            data[i][2] = unitPrice;
            data[i][3] = quantity;
            data[i][4] = df.format(totalAmount);
        }
        data[medicineItems.size() + 1][0] = "Payable Amount";
        data[medicineItems.size() + 1][4] = df.format(sumOfAmount);
        TableModel tableModel = new DefaultTableModel(data, header) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable.setModel(tableModel);
        ConvertCurrencyToEnglish currencyConvertor = new ConvertCurrencyToEnglish(sumOfAmount);
        totalAmountLabel.setText("Total amount for the payment is " + currencyConvertor.convertFloatToEnglish() + " only");
    }

    private void setOperation() {
        JDialog dialog = this;
        confirmBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // prompt admin to enter patient paid amount
                String message = "Enter amount";
                int messageType = JOptionPane.QUESTION_MESSAGE;
                float payAmount = 0;
                while (true) {
                    try {
                        // Show the input dialog
                        String input = JOptionPane.showInputDialog(dialog, message, "Confirm Payment", messageType);
                        if (input == null) {
                            // User clicked Cancel or closed the dialog
                            return;
                        }

                        // Parse the input value
                        payAmount = Float.parseFloat(input);
                        if (payAmount < sumOfAmount) {
                            message = "Paid amount is less than the required amount";
                            messageType = JOptionPane.ERROR_MESSAGE;
                        }
                        else {
                            break;
                        }
                    } catch (NumberFormatException err) {
                        // Input is not a valid currency value
                        message = "Invalid input! Please enter a valid currency amount:";
                        messageType = JOptionPane.ERROR_MESSAGE;
                    }
                }
                // calculate changes and display the changes amount to admin
                float changes = payAmount - sumOfAmount;
                JOptionPane.showMessageDialog(dialog, "Changes: " + df.format(changes));

                // add new payment record
                String date = currentDate.format(dateFormatter);
                String time = (currentDate.format(timeFormatter)).split(" ")[0];
                String meridiem = ((currentDate.format(timeFormatter)).split(" ")[1]).replace("am", "a.m.").replace("pm", "p.m.");
                String[] medicineItemIdArray = new String[medicineItemIdList.size()];
                medicineItemIdArray = medicineItemIdList.toArray(medicineItemIdArray);
                String[] quantityArray = new String[medicineItemIdList.size()];
                for (int i = 0; i < medicineItemIdList.size(); i++) {
                    quantityArray[i] = Integer.toString(quantityList[i]);
                }

                Payments payments = new Payments();
                payments.addNewPayment(date, time, meridiem, patientIcNum, patientName, appointmentId, df.format(sumOfAmount), medicineItemIdArray, quantityArray, "paid");

                JOptionPane.showMessageDialog(dialog, "Payment made successful");
                parent.dispose();
                paymentFrame.refreshTable();
                new MakePaymentUI(paymentFrame);

                dialog.dispose();
            }
        });
    }
}

