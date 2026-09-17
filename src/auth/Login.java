package auth;

public class Login{

    public Login(){

        System.out.println("Email: user@gmail.com");
        System.out.println("Pass: User@111");

        System.out.println("Login Clicked");

        new services.Login_Service("user@gmail.com", "User@111");

    }

    public static void main(String[] args){
        new Login();
    }

}