package model;

public class User {

    private String email;
    private String password;
    private String name;
    private String country;
    private String phoneNumber;
    private String balance;

    public User(
        String email,
        String password,
        String name,
        String balance,
        String country,
        String phoneNumber
    ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.balance = balance;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public String getName(){
        return this.name;
    }

    public String getBalance(){
        return this.balance;
    }

    public String getCountry(){
        return this.country;
    }

    public String getPhoneNumber(){
        return this.phoneNumber;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setBalance(String balance){
        this.balance = balance;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

}
