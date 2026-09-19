package auth;

import javax.swing.JFrame;

public class Login{

    public Login(String email, String password, JFrame parentFrame){

        new services.Login_Service(email, password, parentFrame);

    }

}