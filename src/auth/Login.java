package auth;

public class Login{

    public Login(String email, String password){

        new services.Login_Service(email, password);

    }

}