package Ui.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingUtils{
    public static void animate(JComponent component, Point newPoint, int frames, int interval) {
        Rectangle compBounds = component.getBounds();
    
        Point oldPoint = new Point(compBounds.x, compBounds.y),
              animFrame = new Point((newPoint.x - oldPoint.x) / frames,
                                    (newPoint.y - oldPoint.y) / frames);
    
        new Timer(interval, new ActionListener() {
            int currentFrame = 0;
            public void actionPerformed(ActionEvent e) {
                component.setBounds(oldPoint.x + (animFrame.x * currentFrame),
                                    oldPoint.y + (animFrame.y * currentFrame),
                                    compBounds.width,
                                    compBounds.height);
    
                if (currentFrame != frames)
                    currentFrame++;
                else
                    ((Timer)e.getSource()).stop();
            }
        }).start();
    }

    public static void setButtonProperties(JButton button, Color background, Color foreground, Font font) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(font);
        button.setVisible(true);
    }
}
