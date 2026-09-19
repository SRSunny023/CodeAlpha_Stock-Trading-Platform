package auth;

import javax.swing.JFrame;

public class Registration{

    public Registration(String email, String password, String confirmPassword, String name, JFrame parentFrame){

        new services.Registration_Service(email, password, confirmPassword, name, parentFrame);

    }

}