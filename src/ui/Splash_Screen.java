package ui;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Splash_Screen extends JFrame {

    public Splash_Screen() {

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

    private void splashScreen() {

        setUndecorated(true);
        setSize(1024, 768);

        setLocationRelativeTo(null);
        setLayout(null);

        java.net.URL imageUrl = getClass().getResource("/Welcome.png");

        if (imageUrl == null) {
            java.io.File physicalFile = new java.io.File("resources/Welcome.png");
            if (physicalFile.exists()) {
                try {
                    imageUrl = physicalFile.toURI().toURL();
                } catch (java.net.MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (imageUrl != null) {
            Image scaledImage = new ImageIcon(imageUrl).getImage().getScaledInstance(1024, 768, Image.SCALE_SMOOTH);
            ImageIcon image = new ImageIcon(scaledImage);
            JLabel label = new JLabel(image);
            label.setBounds(0, 0, 1024, 768);
            add(label);
        } else {
            JOptionPane.showMessageDialog(this, "Could not find splash icon resource: Welcome.png", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);

    }

}
