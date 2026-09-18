package auth;

public class Registration{

    public Registration(){

        System.out.println("Email: user@gmail.com");
        System.out.println("Password: user");
        System.out.println("Confirm Password: user");
        System.out.println("Name: mr.user");
        System.out.println("Registration");
        System.out.println("Back");
        System.out.println("Exit");

        System.out.println("Clicked on Registration");

        new services.Registration_Service("mock@gmail.com", "Mock@111", "Mock@111", "mr.mock");

    }

    public static void main(String[] args){
        new Registration();
    }

}