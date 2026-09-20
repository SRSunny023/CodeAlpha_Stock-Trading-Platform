package model;

public class User {

    private String email;
    private String password;
    private String name;
    private String balance;

    public User(
        String email,
        String password,
        String name,
        String balance
    ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.balance = balance;
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

}
