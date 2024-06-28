package Ui.LoginUI;

import Ui.Tool.RoundedButton;
import Ui.Tool.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartUI extends JFrame implements ActionListener {
    private final JButton confirmButton = RoundedButton.createRoundedButton("Healthcare That's Personalized for You", 60, 2, new Color(217, 217, 217));

    public StartUI() {
        int screenSizeWidth = 1280;
        int screenSizeHeight = 800;

        // Main Frame
        setTitle("CarePoint Clinic");
        setSize(screenSizeWidth, screenSizeHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background Image //
        ImageIcon backgroundImageIcon = new ImageIcon(StartUI.class.getResource( "/image/StartMenu.png"));
        backgroundImageIcon = new ImageIcon(backgroundImageIcon.getImage().getScaledInstance(screenSizeWidth, screenSizeHeight, Image.SCALE_SMOOTH));
        JLabel backgroundImage = new JLabel(backgroundImageIcon);
        backgroundImage.setBounds(0, 0, backgroundImageIcon.getIconWidth(), backgroundImageIcon.getIconHeight());

        // Title Container
        JPanel titleContainer = new JPanel();
        titleContainer.setBounds(150,150,1000,380);
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS)); 
        titleContainer.setOpaque(false);

        JLabel greetTitle = new JLabel("Welcome To CarePoint Clinic");
        greetTitle.setFont(new Font("Inter", Font.BOLD, 64));
        greetTitle.setForeground(new Color(255, 255, 255));
        greetTitle.setPreferredSize(new Dimension(989, 68));
        greetTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subTitle = new JLabel("<html><div style='text-align: center;'><br>Welcome to CarePoint, where compassionate care meets excellence in medicine.<br>Our dedicated team of healthcare professionals is committed to providing personalized treatment, fostering wellness, and empowering you on your journey to better health.</div></html>");
        subTitle.setFont(new Font("Inter", Font.PLAIN, 20));
        subTitle.setForeground(new Color(255, 255, 255));
        subTitle.setPreferredSize(new Dimension(989, 87));
        subTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        SwingUtils.setButtonProperties(confirmButton, new Color(217, 217, 217), new Color(17,17,17), new Font("Arial", Font.BOLD, 18));
        confirmButton.setPreferredSize(new Dimension(600, 200));
        confirmButton.setBorder(BorderFactory.createEmptyBorder(0, 250, 0, 250));
        confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmButton.addActionListener(this);
        
        titleContainer.add(greetTitle);
        titleContainer.add(subTitle);
        titleContainer.add(confirmButton);
        titleContainer.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        // Add components to the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.add(backgroundImage, Integer.valueOf(0));
        layeredPane.add(titleContainer, Integer.valueOf(1));

        // Frame Setting
        add(layeredPane);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmButton){
            // Create an instance of LoginUI
            LoginUI loginUI = new LoginUI();
            
            // Set the LoginUI frame visible
            loginUI.setVisible(true);
            
            // Close the current frame
            setVisible(false);
        }
    }
}
