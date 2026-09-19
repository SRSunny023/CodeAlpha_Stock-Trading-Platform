package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import util.Global_Functions;

public class Registration_Screen extends JFrame implements ActionListener {

    String userEmail = "";
    String userPassword = "";
    String userConfirmPassword = "";
    String userName = "";

    JLabel[] labels = new JLabel[]{
        new JLabel("Email:"),
        new JLabel("Password:"),
        new JLabel("Confirm Password:"),
        new JLabel("Full Name:"),
    };

    JTextField fields[] = new JTextField[]{
        new JTextField(),
        new JTextField(),
    };

    JPasswordField passFields[] = new JPasswordField[]{
        new JPasswordField(),
        new JPasswordField(),
    };

    JButton[] buttons = new JButton[]{
        new JButton("Registration"),
        new JButton("Back"),
        new JButton("Exit"),
    };

    public Registration_Screen(){

        initScreen();

    }

    private void initScreen(){

        getContentPane().setBackground(Color.BLACK);

        setUndecorated(true);
        setSize(1024, 768);

        setLocationRelativeTo(null);
        setLayout(null);

        setLabels();
        setFields();
        setButtons();

        setVisible(true);

    }

    private void setLabels(){
        for(int i=0; i<labels.length; i++){
            labels[i].setFont(new Font("Arial", Font.BOLD, 20));
            labels[i].setForeground(Color.WHITE);
            labels[i].setBounds(300, 200 + (100*i), 200, 30);
            add(labels[i]);
        }
    }

    private void setFields(){
        for(int i=0; i<fields.length; i++){
            fields[i].setFont(new Font("Arial", Font.BOLD, 20));
            fields[i].setForeground(Color.BLACK);
            fields[i].setBackground(Color.WHITE);
            fields[i].setBounds(500, 200 + (300*i), 200, 30);
            fields[i].addActionListener(this);
            add(fields[i]);
        }

        for(int i=0; i<passFields.length; i++){
            passFields[i].setFont(new Font("Arial", Font.BOLD, 20));
            passFields[i].setForeground(Color.BLACK);
            passFields[i].setBackground(Color.WHITE);
            passFields[i].setBounds(500, 300 + (100*i), 200, 30);
            passFields[i].addActionListener(this);
            add(passFields[i]);
        }

    }

    private void setButtons(){
        for(int i=0; i<buttons.length; i++){
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setFocusPainted(false);
            buttons[i].setBounds(180 + (210*i), 600, 200, 30);
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }
    }

    private void getResult(){
        userEmail = fields[0].getText();
        userPassword = new String(passFields[0].getPassword());
        userConfirmPassword = new String(passFields[1].getPassword());
        userName = fields[1].getText();
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==fields[0]){           // Email
            getResult();
            if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty() && !userName.isEmpty()){
                new auth.Registration(userEmail, userPassword, userConfirmPassword, userName, this);
                return;
            }
            if(userEmail.isEmpty()){
                JOptionPane.showMessageDialog(this, "Email Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            passFields[0].requestFocusInWindow();
        }

        else if(e.getSource()==fields[1]){      // Name
            getResult();
            if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty() && !userName.isEmpty()){
                new auth.Registration(userEmail, userPassword, userConfirmPassword, userName, this);
                return;
            }
            if(userName.isEmpty()){
                JOptionPane.showMessageDialog(this, "Name Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fields[0].requestFocusInWindow();
        }

        else if(e.getSource()==passFields[0]){      // Password
            getResult();
            if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty() && !userName.isEmpty()){
                new auth.Registration(userEmail, userPassword, userConfirmPassword, userName, this);
                return;
            }
            if(userPassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Password Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            passFields[1].requestFocusInWindow();
        }

        else if(e.getSource()==passFields[1]){      // Confirm Password
            getResult();
            if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty() && !userName.isEmpty()){
                new auth.Registration(userEmail, userPassword, userConfirmPassword, userName, this);
                return;
            }
            if(userConfirmPassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Confirm Password Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            fields[1].requestFocusInWindow();
        }

        else if(e.getSource()==buttons[0]){     // Registration
            getResult();
            if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty() && !userName.isEmpty()){
                new auth.Registration(userEmail, userPassword, userConfirmPassword, userName, this);
                return;
            }
            JOptionPane.showMessageDialog(this, "All Fields Must Be Filled!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        else if(e.getSource()==buttons[1]){     // Back
            setVisible(false);
            dispose();
            SwingUtilities.invokeLater(() -> new Login_Screen());
            return;
        }

        else if(e.getSource()==buttons[2]){     // Exit
            new Global_Functions().exitApp(this);
            return;
        }

    }

}
