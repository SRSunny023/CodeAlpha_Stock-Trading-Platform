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

public class Login_Screen extends JFrame implements ActionListener {

    String userEmail = "";
    String userPassword = "";

    JLabel[] labels = new JLabel[]{
        new JLabel("Email:"),
        new JLabel("Password:"),
    };

    JTextField field = new JTextField();
    JPasswordField passField = new JPasswordField();

    JButton[] buttons = new JButton[]{
        new JButton("Login"),
        new JButton("Registration"),
        new JButton("Exit"),
    };

    public Login_Screen(){

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
        field.setFont(new Font("Arial", Font.BOLD, 20));
        field.setForeground(Color.BLACK);
        field.setBackground(Color.WHITE);
        field.setBounds(500, 200, 200, 30);
        field.addActionListener(this);
        add(field);

        passField.setFont(new Font("Arial", Font.BOLD, 20));
        passField.setForeground(Color.BLACK);
        passField.setBackground(Color.WHITE);
        passField.setBounds(500, 300, 200, 30);
        passField.addActionListener(this);
        add(passField);
    }

    private void setButtons(){
        for(int i=0; i<buttons.length; i++){
            buttons[i].setFont(new Font("Arial", Font.BOLD, 20));
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBackground(Color.BLACK);
            buttons[i].setFocusPainted(false);
            buttons[i].setBounds(180 + (210*i), 460, 200, 30);
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==field){           // Email
            userEmail = field.getText();
            userPassword = new String(passField.getPassword());
            if(!userEmail.isEmpty() && !userPassword.isEmpty()){
                new auth.Login(userEmail, userPassword);
                return;
            }
            if(userEmail.isEmpty()){
                JOptionPane.showMessageDialog(this, "Email or Password Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            passField.requestFocusInWindow();
        }

        else if(e.getSource()==passField){      // Password
            userEmail = field.getText();
            userPassword = new String(passField.getPassword());
            if(!userEmail.isEmpty() && !userPassword.isEmpty()){
                new services.Login_Service(userEmail, userPassword);
                return;
            }
            if(userPassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "Email or Password Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            field.requestFocusInWindow();
        }

        else if(e.getSource()==buttons[0]){     // Login
            userEmail = field.getText();
            userPassword = new String(passField.getPassword());
            if(!userEmail.isEmpty() && !userPassword.isEmpty()){
                new auth.Login(userEmail, userPassword);
                return;
            }
            JOptionPane.showMessageDialog(this, "Email or Password Field Can Not Be Remain Empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        else if(e.getSource()==buttons[1]){     // Registration
            setVisible(false);
            dispose();
            SwingUtilities.invokeLater(() -> new Registration_Screen());
            return;
        }

        else if(e.getSource()==buttons[2]){     // Exit
            int response = JOptionPane.showConfirmDialog(this, "Want to Exit?", "Exit", JOptionPane.YES_NO_OPTION);
            if(response==JOptionPane.YES_OPTION){
                setVisible(false);
                dispose();
                System.exit(0);
            }
            return;
        }

    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new Login_Screen());
    }

}
