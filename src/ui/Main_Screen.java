package ui;

import model.User;

public class Main_Screen {

    private User loggedInUser;

    public Main_Screen(User loggedInUser){

        this.loggedInUser = loggedInUser;

        System.out.println("Dashboard");
        System.out.println("Logged in as: " + loggedInUser.getName() + " (" + loggedInUser.getEmail() + ")");

    }

    public static void main(String[] args){
        User mockUser = new User("test@gmail.com", "pass123", "John Doe", "1000", "+123456", "Bangladesh");
        new Main_Screen(mockUser);
    }

}
