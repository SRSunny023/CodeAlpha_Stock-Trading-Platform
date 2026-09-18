package ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import model.User;

public class Main_Screen extends JFrame implements ActionListener {

    private User loggedInUser;

    public Main_Screen(User loggedInUser){

        this.loggedInUser = loggedInUser;

        initScreen();

        System.out.println("Dashboard");
        System.out.println("Logged in as: " + loggedInUser.getName() + " (" + loggedInUser.getEmail() + ")");

    }

    private void initScreen(){

        getContentPane().setBackground(Color.BLACK);

        setUndecorated(true);
        setSize(1024, 768);

        setLocationRelativeTo(null);
        setLayout(null);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e){



    }

    public static void main(String[] args){
        User mockUser = new User("test@gmail.com", "pass123", "John Doe", "1000", "+123456", "Bangladesh");
        new Main_Screen(mockUser);
    }

}
