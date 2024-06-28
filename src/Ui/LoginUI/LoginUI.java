package Ui.LoginUI;

import Data.Admin.Admins;
import Data.Doctor.Doctors;
import Data.Patient.Patients;
import Ui.Tool.RoundedButton;
import Ui.Tool.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame implements ActionListener {
    String role;

    private final JLayeredPane layeredPane = new JLayeredPane();
    private final JPanel sidePanel = new JPanel();
    private final JPanel whitePanel = new JPanel();
    private final JLabel loginTitle = new JLabel("Welcome To CarePoint Clinic");
    private final JLabel loginSubtitle = new JLabel("Proceed By Logging Into Your Account");
    private final JPanel divisionLine = new JPanel();
    private final JTextField userIc;
    private final JPasswordField userPass;
    private final JPanel buttonsPanel = new JPanel();
    private final JPanel blackPanel = new JPanel();
    private final JButton loginPatientButton = RoundedButton.createRoundedButton("Patient Login", 60, 3, new Color(246, 246, 246));
    private final JButton loginDoctorButton = RoundedButton.createRoundedButton("Doctor Login", 60, 3, new Color(246, 246, 246));
    private final JButton loginAdminButton = RoundedButton.createRoundedButton("Admin Login", 60, 3, new Color(246, 246, 246));
    private final JButton clinicButton = RoundedButton.createRoundedButton("Clinic CarePoint", 60, 2, new Color(246, 246, 246));
    private JButton loginButton = RoundedButton.createRoundedButton("Login", 60, 2, new Color(255, 15, 100));
    private JButton forgetPassButton = RoundedButton.createRoundedButton("Show Password", 60, 2, new Color(255, 15, 100));

    JPanel sideContainer = new JPanel();

    public LoginUI() {
        int screenSizeWidth = 1280;
        int screenSizeHeight = 800;

        // Set up ActionListener for buttons
        loginPatientButton.addActionListener(this);
        loginDoctorButton.addActionListener(this);
        loginAdminButton.addActionListener(this);
        loginButton.addActionListener(this);
        forgetPassButton.addActionListener(this);
        clinicButton.addActionListener(this);

        // Set button properties
        SwingUtils.setButtonProperties(loginPatientButton, new Color(17,17,17), new Color(246,246,246), new Font("Arial", Font.BOLD, 18));
        SwingUtils.setButtonProperties(loginDoctorButton, new Color(17,17,17), new Color(246,246,246), new Font("Arial", Font.BOLD, 18));
        SwingUtils.setButtonProperties(loginAdminButton, new Color(17,17,17), new Color(246,246,246), new Font("Arial", Font.BOLD, 18));

        // Create a JLayeredPane to hold the components
        layeredPane.setPreferredSize(new Dimension(screenSizeWidth, screenSizeHeight));

        // Main Frame
        setTitle("Patient Application");
        setSize(screenSizeWidth, screenSizeHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background Image
        ImageIcon backgroundImageIcon = new ImageIcon(LoginUI.class.getResource("/image/LoginBackground.png"));
        backgroundImageIcon = new ImageIcon(backgroundImageIcon.getImage().getScaledInstance(screenSizeWidth, screenSizeHeight, Image.SCALE_AREA_AVERAGING));
        JLabel backgroundImage = new JLabel(backgroundImageIcon);
        backgroundImage.setBounds(0, 0, backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());

        // Side Panel //
        int sidePanelWidth = 300;
        int sidePanelHeight = 600;
        int sidePanelX = 780;
        int sidePanelY = (screenSizeHeight - sidePanelHeight) / 2;
        sidePanel.setBounds(sidePanelX, sidePanelY, sidePanelWidth, sidePanelHeight);
        sidePanel.setBackground(new Color(22, 17, 17));
        sidePanel.setLayout(new BorderLayout());

        // Side Image
        ImageIcon logoImageIcon = new ImageIcon(LoginUI.class.getResource("/image/ClinicLogo.png"));
        logoImageIcon = new ImageIcon(logoImageIcon.getImage().getScaledInstance(198, 176, Image.SCALE_AREA_AVERAGING));
        JLabel logoImage = new JLabel(logoImageIcon);
        logoImage.setPreferredSize(new Dimension(logoImageIcon.getIconWidth(), 240));
        logoImage.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        // Side Container
        sideContainer.setBackground(new Color(22, 17, 17));
        sideContainer.setPreferredSize(new Dimension(500, 400));
        sideContainer.setLayout(new GridLayout(5, 1, 0, 20));
        sideContainer.setBorder(BorderFactory.createEmptyBorder(0, 45, 0, 45));
        JLabel sideDescription = new JLabel();
        sideDescription.setText("<html>A Secure Gateway To<br>&nbsp;Health and Wellness<html>");
        sideDescription.setFont(new Font("Monospace", Font.BOLD, 20));
        sideDescription.setForeground(new Color(246, 246, 246));
        sideContainer.add(sideDescription);
        sideContainer.add(loginPatientButton);
        sideContainer.add(loginDoctorButton);
        sideContainer.add(loginAdminButton);
        sidePanel.add(logoImage, BorderLayout.NORTH);
        sidePanel.add(sideContainer, BorderLayout.CENTER);

        // White Panel //
        int whitePanelWidth = 600;
        int whitePanelHeight = 600;
        int whitePanelX = 180;
        int whitePanelY = (screenSizeHeight - whitePanelHeight) / 2;
        whitePanel.setBounds(whitePanelX, whitePanelY, whitePanelWidth, whitePanelHeight);
        whitePanel.setBackground(new Color(246, 246, 246));
        whitePanel.setLayout(new BorderLayout());

        // Title Container
        JPanel titleContainer = new JPanel();
        titleContainer.setBackground(new Color(246, 246, 246));
        titleContainer.setLayout(new GridLayout(2, 1, 0, 10));

        loginTitle.setFont(new Font("Arial", Font.BOLD, 34));
        loginTitle.setForeground(new Color(0, 255, 117));
        loginTitle.setBorder(BorderFactory.createEmptyBorder(0, 55, 0, 0));

        loginSubtitle.setFont(new Font("Arial", Font.BOLD, 24));
        loginSubtitle.setForeground(new Color(22, 17, 17));
        loginSubtitle.setBorder(BorderFactory.createEmptyBorder(0, 65, 0, 0));

        titleContainer.add(loginTitle);
        titleContainer.add(loginSubtitle);
        titleContainer.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
        whitePanel.add(titleContainer, BorderLayout.NORTH);

        // Input Container
        JPanel inputContainer = new JPanel();
        inputContainer.setBackground(new Color(246, 246, 246));
        inputContainer.setLayout(new GridLayout(4, 1, 0, 0));
        inputContainer.setBorder(BorderFactory.createEmptyBorder(30, 70, 40, 70));
        JLabel icTextHolder = new JLabel();
        icTextHolder.setText("Identify Card/IC: ");
        icTextHolder.setFont(new Font("Monospace", Font.BOLD, 14));
        JLabel passwordTextHolder = new JLabel();
        passwordTextHolder.setText("Password: ");
        passwordTextHolder.setFont(new Font("Monospace", Font.BOLD, 14));
        userIc = new JTextField();
        userPass = new JPasswordField();
        inputContainer.add(icTextHolder);
        inputContainer.add(userIc);
        inputContainer.add(passwordTextHolder);
        inputContainer.add(userPass);
        whitePanel.add(inputContainer, BorderLayout.CENTER);

        // Create Division Line //
        divisionLine.setBounds(240,510,480,2); // Division Line Coordinates and Size
        divisionLine.setBackground(Color.BLACK);

        // Buttons Panel
        buttonsPanel.setBackground(new Color(246, 246, 246));
        buttonsPanel.setPreferredSize(new Dimension(300, 180));
        buttonsPanel.setForeground(new Color(0, 255, 117));
        buttonsPanel.setLayout(new GridLayout(1, 2, 20, 5));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 110, 80));
        whitePanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Black Panel //
        int blackPanelWidth = 600;
        int blackPanelHeight = 600;
        int blackPanelX = 180;
        int blackPanelY = (screenSizeHeight - blackPanelHeight) / 2;
        blackPanel.setBounds(blackPanelX, blackPanelY, blackPanelWidth, blackPanelHeight);
        blackPanel.setBackground(new Color(30, 30, 30));
        blackPanel.setLayout(new BorderLayout());

        // Image Container
        JPanel infoContainer = new JPanel();
        infoContainer.setBackground(new Color(246, 246, 246));
        infoContainer.setLayout(new GridLayout(3, 2, -120, 0));
        infoContainer.setBackground(new Color(30, 30, 30));
        infoContainer.setBorder(BorderFactory.createEmptyBorder(100, -100, 100, 0));

        ImageIcon phoneImageIcon = new ImageIcon(LoginUI.class.getResource("/image/Phone.png"));
        JLabel phoneImage = new JLabel(new ImageIcon(phoneImageIcon.getImage().getScaledInstance(96, 96, Image.SCALE_AREA_AVERAGING)));

        ImageIcon mailImageIcon = new ImageIcon(LoginUI.class.getResource("/image/Mail.png"));
        JLabel mailImage = new JLabel(new ImageIcon(mailImageIcon.getImage().getScaledInstance(96, 96, Image.SCALE_AREA_AVERAGING)));

        ImageIcon locationImageIcon = new ImageIcon(LoginUI.class.getResource("/image/Location.png"));
        JLabel locationImage = new JLabel(new ImageIcon(locationImageIcon.getImage().getScaledInstance(96, 96, Image.SCALE_AREA_AVERAGING)));

        JLabel phoneNumber = new JLabel("<html><font color='red'>Official Phone Number</font><br>+016 846 6392</html>");
        phoneNumber.setFont(new Font("Arial", Font.BOLD, 22));
        phoneNumber.setForeground(new Color(255, 255, 255));
        JLabel emailAddress = new JLabel("<html><font color='red'>Email Address</font><br>carepoint@gmail.com</html>");
        emailAddress.setFont(new Font("Arial", Font.BOLD, 22));
        emailAddress.setForeground(new Color(255, 255, 255));
        JLabel geoLocation = new JLabel("<html><font color='red'>Geo-Location</font><br>Suite 19.07, Wisma Zelan Bandar Sri<br>Pemaisuri, Taman Sri Pemaisuri</html>");
        geoLocation.setFont(new Font("Arial", Font.BOLD, 22));
        geoLocation.setForeground(new Color(255, 255, 255));
        infoContainer.add(phoneImage);
        infoContainer.add(phoneNumber);
        infoContainer.add(mailImage);
        infoContainer.add(emailAddress);
        infoContainer.add(locationImage);
        infoContainer.add(geoLocation);
        blackPanel.add(infoContainer, BorderLayout.CENTER);

        // Add components to the layered pane
        layeredPane.add(backgroundImage, Integer.valueOf(0));
        layeredPane.add(blackPanel, Integer.valueOf(1));
        layeredPane.add(sidePanel, Integer.valueOf(1));

        // Frame Setting
        add(layeredPane);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginPatientButton || e.getSource() == loginDoctorButton || e.getSource() == loginAdminButton) {
            animateComponents();
            updateUIForLoginType(e.getSource());
        } else if (e.getSource() == clinicButton) {
            resetAnimateComponents();
            updateUIForClinicButton();
        } else if (e.getSource() == loginButton) {
            String username = userIc.getText();
            String password = String.valueOf(userPass.getPassword());
            switch (role) {
                case "patient":
                    Patients patients = new Patients();
                    patients.PatientLogin(username, password, this);
                    break;
                case "doctor":
                    Doctors doctors = new Doctors();
                    doctors.DoctorLogin(username, password, this);
                    break;
                case "admin":
                    Admins admins = new Admins();
                    admins.AdminLogin(username, password, this);
                    break;
            }
        } else if (e.getSource() == forgetPassButton) {
            new ForgetPassUI(this, role);
        }
    }

    private void animateComponents() {
        SwingUtils.animate(sidePanel, new Point(180, 100), 30, 5);
        SwingUtils.animate(whitePanel, new Point(500, 100), 30, 5);
        SwingUtils.animate(divisionLine, new Point(540, 510), 30, 5);
    }

    private void updateUIForLoginType(Object source) {
        Color textColor;
        if (source == loginPatientButton) {
            role = "patient";
            textColor = new Color(3, 227, 160);
        } else if (source == loginDoctorButton) {
            role = "doctor";
            textColor = new Color(100, 106, 255);
        } else { // Admin
            role = "admin";
            textColor = new Color(255, 15, 100);
        }

        whitePanel.setVisible(true);
        divisionLine.setVisible(true);
        blackPanel.setVisible(false);

        layeredPane.add(whitePanel, Integer.valueOf(1));
        layeredPane.remove(blackPanel);
        layeredPane.add(divisionLine, Integer.valueOf(2));

        loginTitle.setForeground(textColor);
        loginSubtitle.setForeground(textColor);
        sideContainer.setBackground(textColor);
        sidePanel.setBackground(textColor);

        loginButton = RoundedButton.createRoundedButton("Login", 60, 2, textColor);
        loginButton.setForeground(textColor);
        loginButton.setBackground(new Color(246, 246, 246));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.addActionListener(this);

        forgetPassButton = RoundedButton.createRoundedButton("Forget Password", 60, 2, textColor);
        forgetPassButton.setForeground(textColor);
        forgetPassButton.setBackground(new Color(246, 246, 246));
        forgetPassButton.setFont(new Font("Arial", Font.BOLD, 16));
        forgetPassButton.addActionListener(this);

        buttonsPanel.add(loginButton);
        buttonsPanel.add(forgetPassButton);

        sideContainer.remove(loginPatientButton);
        sideContainer.remove(loginDoctorButton);
        sideContainer.remove(loginAdminButton);

        clinicButton.setForeground(new Color(246, 246, 246));
        clinicButton.setBackground(textColor);
        clinicButton.setFont(new Font("Arial", Font.BOLD, 18));

        sideContainer.add(clinicButton);
    }

    private void resetAnimateComponents() {
        SwingUtils.animate(sidePanel, new Point(780, 100), 30, 5);
    }

    private void updateUIForClinicButton() {
        sideContainer.setBackground(new Color(22, 17, 17));
        sidePanel.setBackground(new Color(22, 17, 17));

        whitePanel.setVisible(false);
        divisionLine.setVisible(false);
        blackPanel.setVisible(true);

        layeredPane.remove(whitePanel);
        layeredPane.remove(divisionLine);
        layeredPane.add(blackPanel, Integer.valueOf(1));

        buttonsPanel.remove(loginButton);
        buttonsPanel.remove(forgetPassButton);

        sideContainer.remove(clinicButton);
        sideContainer.add(loginPatientButton);
        sideContainer.add(loginDoctorButton);
        sideContainer.add(loginAdminButton);
    }
}