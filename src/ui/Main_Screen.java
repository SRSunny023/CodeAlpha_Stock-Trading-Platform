package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import model.User;
import services.Update_Password_Service;
import util.Global_Functions;

public class Main_Screen extends JFrame implements ActionListener {

    private User loggedInUser;

    JButton[] buttons = new JButton[]{
        new JButton("Update Password"),
        new JButton("Logout"),
        new JButton("Exit"),
    };

    public Main_Screen(User loggedInUser){

        this.loggedInUser = loggedInUser;

        initScreen();

    }

    private void initScreen(){

        getContentPane().setBackground(Color.BLACK);

        setUndecorated(true);
        setSize(1024, 768);

        setLocationRelativeTo(null);
        setLayout(null);

        setHeading();

        setVisible(true);

    }

    private void setHeading(){

        JLabel[] labels = new JLabel[]{
            new JLabel("NAME:"),
            new JLabel("BALANCE:"),
            new JLabel(),
            new JLabel(),
        };

        JPanel header = new JPanel();
        header.setLayout(null);
        header.setBackground(Color.GRAY);
        header.setBounds(0, 0, 1024, 76);
        add(header);

        JPanel footer = new JPanel();
        footer.setLayout(null);
        footer.setBackground(Color.GRAY);
        footer.setBounds(0, 692, 1024, 76);
        add(footer);

        for(int i=0; i<labels.length; i++){
            labels[i].setForeground(Color.BLUE);
            labels[i].setFont(new Font("Arial", Font.BOLD, 16));
            if(i<2)
                labels[i].setBounds(50 + (700*i), 20, 250, 30);
            else
                labels[i].setBounds(130 + (720*(i-2)), 20, 350, 30);

            header.add(labels[i]);
        }

        labels[2].setText(loggedInUser.getName());
        labels[3].setText(loggedInUser.getBalance() + "$");

        for(int i=0; i<buttons.length; i++){
            buttons[i].setForeground(Color.BLUE);
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(this);
            buttons[i].setBounds(180 + (210*i), 20, 200, 30);
            footer.add(buttons[i]);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==buttons[0]){          // Update Password

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

            JLabel newPassLabel = new JLabel("New Password:");
            JLabel newConfirmPassLabel = new JLabel("Confirm Password:");

            JPasswordField newPassField = new JPasswordField(15);
            JPasswordField confirmPassField = new JPasswordField(15);

            panel.add(newPassLabel);
            panel.add(newPassField);
            panel.add(newConfirmPassLabel);
            panel.add(confirmPassField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Password!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(result==JOptionPane.OK_OPTION){

                String newPass = new String(newPassField.getPassword());
                String newConfirmPass = new String(confirmPassField.getPassword());

                if(newPass.isEmpty() || newConfirmPass.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Password Field Can Not Be Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                new Update_Password_Service(newPass, newConfirmPass, this, loggedInUser);

            }

        }

        else if(e.getSource()==buttons[1]){     // Logout
            new Global_Functions().logout(this, loggedInUser);
            return;
        }

        else if(e.getSource()==buttons[2]){     // Exit
            new Global_Functions().exitApp(this);
            return;
        }

    }

    public static void main(String[] args){
        User mockUser = new User("mock@gmail.com", "Mock@111", "Mr. Mock", "1000");
        new Main_Screen(mockUser);
    }

}
