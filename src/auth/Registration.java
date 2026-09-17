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

        new services.Registration_Service("user@gmail.com", "User@111", "User@111", "mr.user");

    }

    public static void main(String[] args){
        new Registration();
    }

}