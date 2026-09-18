package auth;

public class Registration{

    public Registration(String email, String password, String confirmPassword, String name){

        new services.Registration_Service(email, password, confirmPassword, name);

    }

}