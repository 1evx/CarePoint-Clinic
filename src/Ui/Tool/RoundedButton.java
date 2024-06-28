package Ui.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton {
    public static JButton createRoundedButton(String text, int radius, int thickness, Color borderColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
                g2.setColor(borderColor); // Set the color of the border
                g2.setStroke(new BasicStroke(thickness)); // Set the thickness of the border
                g2.draw(new RoundRectangle2D.Double(thickness / 2, thickness / 2, getWidth() - thickness, getHeight() - thickness, radius, radius));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(null); // Set the border to null
        button.setPreferredSize(new Dimension(200, 40)); // Set the preferred size
        return button;
    }
}