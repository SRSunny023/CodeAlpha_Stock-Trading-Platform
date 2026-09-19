package ui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

import util.Global_Variables;

public class Splash_Screen extends JFrame {

    public Splash_Screen(){

        splashScreen();

        Timer timer = new Timer(2000, e -> {
            try {

                setVisible(false);
                dispose();

                new auth.Session();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        timer.setRepeats(false);
        timer.start();

    }

    private void splashScreen(){

        setUndecorated(true);
        setSize(1024, 768);

        setLocationRelativeTo(null);
        setLayout(null);

        Image scaledImage = new ImageIcon(Global_Variables.SPLASH_ICON).getImage().getScaledInstance(1024, 768, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(scaledImage);
        JLabel label = new JLabel(image);
        label.setBounds(0, 0, 1024, 768);
        add(label);

        setVisible(true);

    }

}
