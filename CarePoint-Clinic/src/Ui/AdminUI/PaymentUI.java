package Ui.AdminUI;

import Authentication.Authentication;
import Authentication.Cookie;
import Data.Appointment.AppointmentData;
import Data.Appointment.Appointments;
import Data.Medicine.MedicineData;
import Data.Medicine.Medicines;
import Data.Payment.PaymentData;
import Data.Payment.Payments;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.File;

public class PaymentUI extends JFrame {
    Payments payments = new Payments();
    Object[] header;
    Object[][] data;
    int selectedRow = -1;
    String[] patientNameList;
    String[][] paymentItemList;
    String[][] paymentItemQuantityList;
    DecimalFormat df = new DecimalFormat("#0.00");
    // Arrays for English representation of numbers

    private JLabel Title;
    private JToolBar TB1;
    private JToolBar.Separator TB1TBS1;
    private JButton CANCELButton;
    private JToolBar.Separator TB1TBS3;
    private JButton NEWButton;
    private JToolBar.Separator TB1TBS4;
    private JButton searchButton;
    private JButton refreshButton;
    private JPanel P1;
    private JLabel dataInputTitle;
    private JSeparator P1S1;
    private JScrollPane P1ScrollPanel;
    private JPanel dataPanel;
    private JPanel dataInputPanel;
    private JPanel dataDescriptionPanel;
    private JPanel controlPanel;
    private JPanel P2;
    private JLabel P2L1;
    private JScrollPane P2SP1;
    private JTable Table;
    private JPanel MainPanel;
    private JButton generateReciptButton;
    private JButton generateSalesReportBtn;

    public PaymentUI() {
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.8f), (int) Math.floor(screenHeight*0.9f));
        setOperation();
        setTable(payments.getPaymentHeader(), payments.queryPayment(null, null, null, null, null, null, "paid"));
        setComponents();
        P1.setPreferredSize(new Dimension(getSize().width/4, getSize().height));
        P2.setPreferredSize(new Dimension(getSize().width/2, getSize().height));
        setContentPane(MainPanel);
        setTitle("Manage Payment");

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setOperation() {
        PaymentUI frame = this;

        CANCELButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select data for cancelling");
                    return;
                }
                String cancelPaymentId = data[selectedRow][0].toString();
                String message = "Please enter your password to confirm cancel Payment " + cancelPaymentId;
                int messageType = JOptionPane.QUESTION_MESSAGE;
                while (true) {
                    String input = JOptionPane.showInputDialog(frame, message, "Deleting Confirmation", messageType);
                    if (input == null) {
                        return;
                    }
                    if (Authentication.matchPassword(input, Cookie.getAdminCookie().getPass())) {
                        break;
                    }
                    message = "Wrong password, please try again";
                    messageType = JOptionPane.ERROR_MESSAGE;
                }
                payments.cancelPayment(cancelPaymentId);
                JOptionPane.showMessageDialog(frame, "Payment cancelled successful.");
                updateTable();
                selectedRow = -1;
                dataInputTitle.setText("View Payment");
                for (Component dataInput: dataInputPanel.getComponents()) {
                    if (dataInput instanceof JComboBox<?> comboBox) {
                        comboBox.setSelectedItem("- none -");
                        comboBox.setEnabled(false);
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

        NEWButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MakePaymentUI(frame);
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataInputTitle.setText("Search Payment");
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
                        List<PaymentData> queriedPaymentlist = payments.queryPayment(valueArray[0], valueArray[1], valueArray[2],valueArray[3], valueArray[4], valueArray[5], "paid");
                        JOptionPane.showMessageDialog(frame, String.format("%d result found", queriedPaymentlist.size()));
                        updateQueryTable(queriedPaymentlist);
                    }
                });
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        generateReciptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select payment to generate receipt");
                    return;
                }
                generateReceipt();
            }
        });

        generateSalesReportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SalesReportDateUI(frame);
            }
        });
    }

    public void refreshTable() {
        updateTable();
        selectedRow = -1;
        dataInputTitle.setText("View Payment");
        for (Component dataInput: dataInputPanel.getComponents()) {
            if (dataInput instanceof JComboBox<?> comboBox) {
                comboBox.setSelectedItem("- none -");
                comboBox.setEnabled(false);
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

    private void updateDataInput() {
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

    private void updateQueryTable(List<PaymentData> queriedPaymentList) {
        payments = new Payments();
        setTable(payments.getPaymentHeader(), queriedPaymentList);
        Table.revalidate();
        Table.repaint();
    }

    private void updateTable() {
        payments = new Payments();
        setTable(payments.getPaymentHeader(), payments.queryPayment(null, null, null, null, null, null, "paid"));
        Table.revalidate();
        Table.repaint();
    }

    private void setComponents() {
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
            else if (header[j].equals("Meridiem")) {
                String[] list = new String[]{"- none -", "a.m.", "p.m."};
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
            else if (header[j].equals("Time")) {
                try {
                    MaskFormatter timeFormatter = new MaskFormatter("##:##");
                    JFormattedTextField timeField = new JFormattedTextField(timeFormatter);
                    timeField.setColumns(5);
                    timeField.setEditable(false);
                    comp = timeField;
                }
                catch (ParseException e) {
                    throw new RuntimeException(e);
                }
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

    private void setTable(List<String> headerList, List<PaymentData> paymentList) {
        // header
        List<String> paymentHeader = headerList.stream()
                .filter(header -> !header.equals("Patient Name") && !header.equals("Items") && !header.equals("Quantities") && !header.equals("Status"))
                .toList();
        header = paymentHeader.toArray();

        // data
        data = new Object[paymentList.size()][header.length];
        patientNameList = new String[paymentList.size()];
        paymentItemList = new String[paymentList.size()][];
        paymentItemQuantityList = new String[paymentList.size()][];

        for (int i = 0; i < paymentList.size(); i++) {
            PaymentData paymentData = paymentList.get(i);
            data[i][0] = paymentData.getId();
            data[i][1] = paymentData.getDate();
            data[i][2] = paymentData.getTime();
            data[i][3] = paymentData.getMeridiem();
            data[i][4] = paymentData.getPatientICNum();
            data[i][5] = paymentData.getAppointmentId();
            data[i][6] = paymentData.getAmount();
            patientNameList[i] = paymentData.getPatientName();
            paymentItemList[i] = paymentData.getItems();
            paymentItemQuantityList[i] = paymentData.getQuantities();
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
                    dataInputTitle.setText("View Payment");
                    selectedRow = Table.getSelectedRow();
                    updateDataInput();
                }
            }
        });

        Table.getTableHeader().setReorderingAllowed(false);
    }

    private JButton createControlButton(String buttonText) {
        JButton controlButton = new JButton(buttonText);
        controlButton.setPreferredSize(new Dimension(100, 30));
        controlPanel.add(controlButton, new CellConstraints().xy(1, 1));
        validate();
        repaint();
        return controlButton;
    }

    private void removeControlButton() {
        if (controlPanel.getComponentCount() > 0) {
            controlPanel.removeAll();
            validate();
            repaint();
        }
    }

    private void generateReceipt() {
        Medicines medicines = new Medicines();
        // get all data that may use for generating receipt
        String patientName = patientNameList[selectedRow];
        String[] medicineItem = paymentItemList[selectedRow];
        String[] quantityItem = paymentItemQuantityList[selectedRow];
        String paymentId = data[selectedRow][0].toString();
        String date = data[selectedRow][1].toString();
        String patientIcNum = data[selectedRow][4].toString();
        String appointmentId = data[selectedRow][5].toString();
        String payFor;
        if (appointmentId.equals("null")) {
            payFor = "general sales";
        }
        else {
            AppointmentData appointment = new Appointments().queryAppointment(appointmentId, null, null, null, null, null, null, null, null).getFirst();
            payFor = appointmentId + " (" + appointment.getDate() + " " + appointment.getTime() + appointment.getMeridiem() + ")";
        }
        String amount = data[selectedRow][6].toString();

        // set the downloaded path
        String os = System.getProperty("os.name").toLowerCase();
        String downloadDir = null;

        if (os.contains("win")) {
            // Windows OS
            downloadDir = System.getenv("USERPROFILE") + "\\Downloads";
        } else if (os.contains("mac")) {
            // macOS
            downloadDir = System.getProperty("user.home") + "/Downloads";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            // Unix/Linux OS
            downloadDir = System.getProperty("user.home") + "/Downloads";
        } else {
            // Unknown OS
            downloadDir = System.getProperty("user.home") + "/Downloads";
        }
        String fileName = patientIcNum + " " + date + " " + paymentId + ".pdf";
        String path = Paths.get(downloadDir, "/", fileName).toString();
        // generate pdf receipt
        try {
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            float headerSecondColumn = 285f;
            float headerFirstColumn = headerSecondColumn + 150f;
            float[] headerColumns = {headerFirstColumn, headerSecondColumn};

            // set receipt header
            Table headerTable = new Table(headerColumns);
            headerTable.addCell(new Cell().add(new Paragraph("Receipt")).setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
            Table nestedHeaderTable = new Table(new float[]{headerSecondColumn/2, headerSecondColumn/2});
            nestedHeaderTable.addCell(new Cell().add(new Paragraph("Receipt ID: ")).setBold().setBorder(Border.NO_BORDER));
            nestedHeaderTable.addCell((new Cell().add(new Paragraph(paymentId))).setBold().setBorder(Border.NO_BORDER));
            nestedHeaderTable.addCell(new Cell().add(new Paragraph("Receipt Date: ")).setBold().setBold().setBorder(Border.NO_BORDER));
            nestedHeaderTable.addCell(new Cell().add(new Paragraph(date)).setBold().setBorder(Border.NO_BORDER));
            headerTable.addCell(new Cell().add(nestedHeaderTable).setBorder(Border.NO_BORDER));
            document.add(headerTable);

            // set split border
            Border greyBorder = new SolidBorder(ColorConstants.GRAY, 1f/2f);
            Table divider = new Table(new float[]{190f*3});
            divider.setBorder(greyBorder);
            document.add(divider);

            Table infoTable = new Table(headerColumns);
            Table nestedInfoTable1 = new Table(new float[]{headerFirstColumn/2});
            nestedInfoTable1.addCell(new Cell().add(new Paragraph("Billing Information")).setFontSize(16f).setBorder(Border.NO_BORDER).setBold().setUnderline());
            nestedInfoTable1.addCell(new Cell().add(new Paragraph("Patient Name")).setFontSize(10f).setBorder(Border.NO_BORDER).setBold());
            nestedInfoTable1.addCell(new Cell().add(new Paragraph(patientName)).setFontSize(10f).setBorder(Border.NO_BORDER));
            nestedInfoTable1.addCell(new Cell().add(new Paragraph("Patient IC Number")).setFontSize(10f).setBorder(Border.NO_BORDER).setBold());
            nestedInfoTable1.addCell(new Cell().add(new Paragraph(patientIcNum)).setFontSize(10f).setBorder(Border.NO_BORDER));
            nestedInfoTable1.addCell(new Cell().add(new Paragraph("Pay For")).setFontSize(10f).setBorder(Border.NO_BORDER).setBold());
            nestedInfoTable1.addCell(new Cell().add(new Paragraph(payFor)).setFontSize(10f).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(nestedInfoTable1).setBorder(Border.NO_BORDER));

            Table nestedInfoTable2 = new Table(new float[]{headerSecondColumn/2});
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("Clinic Information")).setFontSize(16f).setBorder(Border.NO_BORDER).setBold().setUnderline());
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("Clinic Name")).setFontSize(10f).setBorder(Border.NO_BORDER).setBold());
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("CarePoint Clinic")).setFontSize(10f).setBorder(Border.NO_BORDER));
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("Email")).setFontSize(10f).setBorder(Border.NO_BORDER).setBold());
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("carepoint@gmail.com")).setFontSize(10f).setBorder(Border.NO_BORDER));
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("Phone Number")).setFontSize(10f).setBorder(Border.NO_BORDER).setBold());
            nestedInfoTable2.addCell(new Cell().add(new Paragraph("016-846 6392")).setFontSize(10f).setBorder(Border.NO_BORDER));
            infoTable.addCell(new Cell().add(nestedInfoTable2).setBorder(Border.NO_BORDER));
            document.add(infoTable);

            document.add(new Paragraph("\n").setFontSize(50f));

            float[] tableColumn = {100f, 200f, 85f, 85f, 100f};
            document.add(new Paragraph("Item: ").setFontSize(14f).setBold());
            Table itemTable = new Table(tableColumn);
            itemTable.addCell(new Cell().add(new Paragraph("Item Code")).setFontSize(10f).setBold());
            itemTable.addCell(new Cell().add(new Paragraph("Description")).setFontSize(10f).setBold());
            itemTable.addCell(new Cell().add(new Paragraph("Unit Price (RM)")).setFontSize(10f).setBold());
            itemTable.addCell(new Cell().add(new Paragraph("Quantity")).setFontSize(10f).setBold());
            itemTable.addCell(new Cell().add(new Paragraph("Total Amount (RM)")).setFontSize(10f).setBold());

            String price;
            String quantity;
            float totalAmount = 0;
            float sum = 0;
            for (int i=0; i< medicineItem.length; i++) {
                MedicineData medicine = medicines.queryMedicine(medicineItem[i], null, null, null).getFirst();
                price = medicine.getPrice();
                quantity = quantityItem[i];
                totalAmount = Float.parseFloat(price) * Integer.parseInt(quantity);
                sum += totalAmount;

                itemTable.addCell(new Cell().add(new Paragraph(medicine.getId())).setFontSize(12f));
                itemTable.addCell(new Cell().add(new Paragraph(medicine.getDescription())).setFontSize(12f));
                itemTable.addCell(new Cell().add(new Paragraph(price)).setFontSize(12f));
                itemTable.addCell(new Cell().add(new Paragraph(quantity)).setFontSize(12f));
                itemTable.addCell(new Cell().add(new Paragraph(df.format(totalAmount))).setFontSize(12f));
            }

            // skip a row
            for (int j = 0; j < tableColumn.length; j++) {
                itemTable.addCell(new Cell().add(new Paragraph("-")).setFontSize(12f).setOpacity(0f));
            }

            // place payable amount
            itemTable.addCell(new Cell().add(new Paragraph("Payable Amount")).setFontSize(12f));
            for (int j = 0; j < tableColumn.length-2; j++) {
                itemTable.addCell(new Cell().add(new Paragraph("")).setFontSize(12f));
            }
            itemTable.addCell(new Cell().add(new Paragraph(df.format(sum))).setFontSize(12f));
            document.add(itemTable);

            document.add(new Paragraph("\n").setFontSize(50f));

            ConvertCurrencyToEnglish currencyConvertor = new ConvertCurrencyToEnglish(sum);
            document.add(new Paragraph("Paid Amount is " + currencyConvertor.convertFloatToEnglish() + " only.").setFontSize(18f));

            document.close();

            JOptionPane.showMessageDialog(this, "Receipt Generated Successful.", "Receipt Generation Message", JOptionPane.INFORMATION_MESSAGE);

            File pdfFile = new File(path);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(pdfFile);
                }
            }
        }
        catch (IOException err) {
            JOptionPane.showMessageDialog(this, "Receipt Generated Failed", "Receipt Generation Error", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(err);
        }

    }
}
