package Ui.AdminUI;

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
import com.itextpdf.layout.property.TextAlignment;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalesReportDateUI extends JDialog {

    private JPanel MainPanel;
    private JButton generateBtn;
    private JPanel datePanel;

    public SalesReportDateUI(JFrame parent) {
        super(parent, "Generate Sales Report", ModalityType.APPLICATION_MODAL);
        setSize(450, 200);

        datePanel.setLayout(new FormLayout("100px, 200px", "30px, 10px, 30px"));
        JLabel startDateLabel = new JLabel("Start Date: ");
        JDateChooser startDateChooser = new JDateChooser();
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        startDateChooser.setPreferredSize(new Dimension(180, 30));
        datePanel.add(startDateLabel, new CellConstraints().xy(1, 1));
        datePanel.add(startDateChooser, new CellConstraints().xy(2, 1));

        JLabel endDateLabel = new JLabel("End Date: ");
        JDateChooser endDateChooser = new JDateChooser();
        endDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setPreferredSize(new Dimension(180, 30));
        datePanel.add(endDateLabel, new CellConstraints().xy(1, 3));
        datePanel.add(endDateChooser, new CellConstraints().xy(2, 3));

        setContentPane(MainPanel);

        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                if (startDateChooser.getDate() == null || endDateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Please select date range");
                    return;
                }

                String startDate = formatter.format(startDateChooser.getDate());
                String endDate = formatter.format(endDateChooser.getDate());
                generateSalesReport(startDate, endDate);
            }
        });

        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void generateSalesReport(String startDate, String endDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate = LocalDate.parse(startDate, dateFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateFormatter);

        Medicines medicines = new Medicines();
        Payments payments = new Payments();
        // retrieve payment that is between start date and end date
        List<PaymentData> requiredPayment = payments.getPaymentList().stream()
                .filter(payment -> startLocalDate.isBefore(LocalDate.parse(payment.getDate(), dateFormatter)) && endLocalDate.isAfter(LocalDate.parse(payment.getDate(), dateFormatter)))
                .toList();

        if (requiredPayment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Sales Between This Date Range");
            return;
        }

        List<Object[]> medicineList = new ArrayList<>();
        for (PaymentData payment : requiredPayment) {
            String[] items = payment.getItems();
            String[] quantities = payment.getQuantities();
            for (int i=0; i< items.length; i++) {
                itemExistInMedicineList(medicineList, items[i], quantities[i]);
            }
        }

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
        String fileName = "Sales Report " + startDate + " " + endDate + ".pdf";
        String path = Paths.get(downloadDir, "/", fileName).toString();
        DecimalFormat df = new DecimalFormat("#0.00");
        // generate pdf receipt
        try {
            PdfWriter pdfWriter = new PdfWriter(path);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("CarePoint Clinic").setTextAlignment(TextAlignment.CENTER).setFontSize(20f).setBold());
            document.add(new Paragraph("Sales Report from " + startDate + " to " + endDate).setTextAlignment(TextAlignment.CENTER).setFontSize(20f).setBold());
            // set split border
            Border blackBorder = new SolidBorder(ColorConstants.BLACK, 1f/4f);
            Table divider = new Table(new float[]{190f*3});
            divider.setBorder(blackBorder);
            document.add(divider);

            document.add(new Paragraph("\n").setFontSize(12f));

            float[] salesColumns = {500f, 70f};
            Table salesTable = new Table(salesColumns);

            salesTable.addCell(new Cell().add(new Paragraph("Sales")).setFontSize(16f).setUnderline().setBorder(Border.NO_BORDER));
            salesTable.addCell(new Cell().add(new Paragraph("RM")).setFontSize(16f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));

            float sumOfSales = 0;
            for (Object[] medicineInfo : medicineList) {
                String medicineId = medicineInfo[0].toString();
                int quantity = (int) medicineInfo[1];
                MedicineData medicine = medicines.queryMedicine(medicineId, null, null, null).getFirst();
                String unitPrice = medicine.getPrice();
                String medicineDesc = medicine.getDescription();
                float amount = Float.parseFloat(unitPrice) * quantity;
                sumOfSales += amount;

                salesTable.addCell(new Cell().add(new Paragraph(medicineDesc + " (" + medicineId + ")")).setFontSize(12f).setBorder(Border.NO_BORDER));
                salesTable.addCell(new Cell().add(new Paragraph(df.format(amount))).setFontSize(12f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            }

            // set space in the table
            salesTable.addCell(new Cell().add(new Paragraph("-")).setFontSize(12f).setBorder(Border.NO_BORDER).setOpacity(0f));
            salesTable.addCell(new Cell().add(new Paragraph("-")).setFontSize(12f).setBorder(Border.NO_BORDER).setOpacity(0f));
            salesTable.addCell(new Cell().add(new Paragraph("Total Sales")).setFontSize(12f).setBorder(Border.NO_BORDER));

            Cell totalAmountCell = new Cell().add(new Paragraph(df.format(sumOfSales))).setFontSize(12f).setTextAlignment(TextAlignment.CENTER);
            totalAmountCell.setBorderTop((new SolidBorder(1)));
            totalAmountCell.setBorderBottom(Border.NO_BORDER);
            totalAmountCell.setBorderLeft(Border.NO_BORDER);
            totalAmountCell.setBorderRight(Border.NO_BORDER);
            salesTable.addCell(totalAmountCell);

            document.add(salesTable);
            document.close();

            JOptionPane.showMessageDialog(this, "Sales report Generated Successful.", "Sales Report Generation Message", JOptionPane.INFORMATION_MESSAGE);

            File pdfFile = new File(path);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(pdfFile);
                }
            }
        }
        catch (IOException err) {
            JOptionPane.showMessageDialog(this, "Sales report Generated Failed", "Sales Report Generation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void itemExistInMedicineList(List<Object[]> medicineList, String item, String quantity) {
        for (Object[] objArray : medicineList) {
            if (objArray[0].equals(item)) {
                objArray[1] = (int) objArray[1] + Integer.parseInt(quantity);
                return;
            }
        }
        medicineList.add(new Object[]{item, Integer.parseInt(quantity)});
    }
}
