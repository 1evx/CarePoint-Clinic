package Ui.AdminUI;

import Data.MedicalRecord.MedicalRecordData;
import Data.MedicalRecord.MedicalRecords;
import Data.Patient.*;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientMedicalRecordUI extends JFrame {
    JLabel IcNumberLabel = new JLabel("Patient Ic Number");
    JLabel dateLabel = new JLabel("Date");
    JTextField IcNumberInputField = new JTextField();
    JDateChooser dateChooser = new JDateChooser();
    String date;
    SimpleDateFormat formatter = new SimpleDateFormat("yyy-MM-dd");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JLabel Title;
    private JPanel P2;
    private JPanel P1;
    private JLabel dataInputTitle;
    private JSeparator P1S1;
    private JScrollPane P1ScrollPanel;
    private JPanel dataPanel;
    private JPanel dataInputPanel;
    private JPanel dataDescriptionPanel;
    private JPanel controlPanel;
    private JPanel MainPanel;
    private JButton Search;
    private JLabel genderLabel;
    private JLabel dobLabel;
    private JLabel ageLabel;
    private JLabel nameLabel;
    private JPanel PMRDetailPanel;
    private String icNumber;

    public static void main(String[] args) {
        new PatientMedicalRecordUI();
    }

    public PatientMedicalRecordUI(){
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        setSize((int) Math.floor(screenWidth*0.4f), (int) Math.floor(screenHeight*0.5f));
        setComponents();

        Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                icNumber = IcNumberInputField.getText();
                date = (dateChooser.getDate() == null) ? null: formatter.format(dateChooser.getDate());
                List<PatientData> searchedPatient = new Patients().queryPatient(null, null, icNumber, null, null, null);
                if (searchedPatient.isEmpty()) {
                    JOptionPane.showMessageDialog( MainPanel, "Patient does not exist");
                    return;
                }

                List<MedicalRecordData> searchedMedicalRecord = new MedicalRecords().queryMedicalRecord(null, date, null, null, null, icNumber, null);
                if (searchedMedicalRecord.isEmpty()) {
                    JOptionPane.showMessageDialog( MainPanel, "Medical Record does not exist");
                    return;
                }

                PatientData patient = searchedPatient.getFirst();
                String name = patient.getName();
                String gender = patient.getGender();
                String dob = patient.getDob();

                LocalDate dobDate = LocalDate.parse(dob, dateTimeFormatter);
                LocalDate today = LocalDate.now();
                Period period = Period.between(dobDate, today);
                int age = period.getYears();


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
                String fileName = "Medical Record " + icNumber + " " + date + ".pdf";
                String path = Paths.get(downloadDir, "/",  fileName).toString();
                // generate pdf receipt
                try {
                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                    pdfDocument.setDefaultPageSize(PageSize.A4);
                    Document document = new Document(pdfDocument);

                    document.add(new Paragraph("Patient Name: " + name).setFontSize(14f).setBold());
                    document.add(new Paragraph("Gender: " + gender).setFontSize(14f).setBold());
                    document.add(new Paragraph("DOB: " + dob).setFontSize(14f).setBold());
                    document.add(new Paragraph("Age: " + age).setFontSize(14f).setBold());
                    document.add(new Paragraph("\n").setFontSize(20f));

                    // set split border
                    Border blackBorder = new SolidBorder(ColorConstants.BLACK, 1f/2f);
                    Table divider = new Table(new float[]{190f*3});
                    divider.setBorder(blackBorder);
                    document.add(divider);

                    for (int i=0; i<searchedMedicalRecord.size(); i++) {
                        MedicalRecordData medicalRecord = searchedMedicalRecord.get(i);
                        document.add(new Paragraph("Medical Record " + i+1).setFontSize(14f).setBold().setUnderline());
                        Table medicalRecordsTable = new Table(new float[]{50f, 520f});
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph("Datetime: ")).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph(medicalRecord.getDate() + " " + medicalRecord.getTime() + medicalRecord.getMeridiem())).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph("Doctor: ")).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph(medicalRecord.getDoctorId() + " " + medicalRecord.getDoctorName() + " (" + medicalRecord.getSpecialization() + ")")).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph("Description: ")).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph(medicalRecord.getDescription())).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph("Dose: ")).setFontSize(14f).setBorder(Border.NO_BORDER));
                        medicalRecordsTable.addCell(new Cell().add(new Paragraph(medicalRecord.getDose())).setFontSize(14f).setBorder(Border.NO_BORDER));

                        document.add(medicalRecordsTable);
                    }
                    document.close();

                    JOptionPane.showMessageDialog(MainPanel, "Patient Medical Record Generated Successful.", "Patient Medical record Generation Message", JOptionPane.INFORMATION_MESSAGE);

                    File pdfFile = new File(path);
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.OPEN)) {
                            desktop.open(pdfFile);
                        }
                    }

                }
                catch (IOException err) {
                    throw new RuntimeException(err);
                }
            }
        });

        setContentPane(MainPanel);
        setTitle("Track Patient Medical Record");

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setComponents() {
        FormLayout layout = new FormLayout("p", "50px, 10px, 50px, 10px");
        dataDescriptionPanel.setLayout(layout);
        dataInputPanel.setLayout(layout);

        // add IC Number Label and date label into dataDescriptionPanel
        IcNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        IcNumberLabel.setPreferredSize(new Dimension(200, 30));
        IcNumberLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dataDescriptionPanel.add(IcNumberLabel, new CellConstraints().xy(1, 1));

        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setPreferredSize(new Dimension(200, 30));
        dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        dataDescriptionPanel.add(dateLabel, new CellConstraints().xy(1, 3));

        IcNumberInputField.setFont(new Font("Arial", Font.PLAIN, 14));
        IcNumberInputField.setPreferredSize(new Dimension(100, 30));
        dataInputPanel.add(IcNumberInputField, new CellConstraints().xy(1, 1));

        dateChooser.setDateFormatString("yyyy-MM-dd");
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) dateChooser.getDateEditor().getUiComponent();
        dateEditor.setDisabledTextColor(Color.BLACK);
        dateEditor.setEditable(false);
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 14));
        dateChooser.setPreferredSize(new Dimension(100, 30));
        dataInputPanel.add(dateChooser, new CellConstraints().xy(1, 3));
    }
}
