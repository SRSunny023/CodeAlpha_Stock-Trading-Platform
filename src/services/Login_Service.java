package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import model.User;
import util.Global_Variables;

public class Login_Service {

    private User loggedInUser;

    public Login_Service(String email, String password){

        boolean emailMatched = checkDuplicateEmail(email);
        boolean passwordMatched = checkPasswordMatch(email,password);

        if(!emailMatched){
            System.out.println("Invalid Email Address");
            return;
        }

        if(!passwordMatched){
            System.out.println("Invalid Password");
            return;
        }

        if(updateCurrentSession(email)){
            this.loggedInUser = fetchUserProfile(email);
            System.out.println("Welcome back, " + loggedInUser.getName() + "!");
        } else{
            System.out.println("Unhandled Error Occurred! Login Failed.");
        }

        return;

    }

    private boolean checkDuplicateEmail(String email){

        File file = new File(Global_Variables.USER_LIST_FILE);

        if(!file.exists()){
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|",-1);
                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && email.trim().equalsIgnoreCase(parts[0].trim())){
                    return true;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    private boolean checkPasswordMatch(String email, String password){

        File file = new File(Global_Variables.USER_LIST_FILE);

        if(!file.exists()){
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|",-1);
                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && parts[0].trim().equalsIgnoreCase(email) && password.trim().equals(parts[1].trim())){
                    return true;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    private boolean updateCurrentSession(String email){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = Paths.get(Global_Variables.CURRENT_SESSION);

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
                System.out.println("Directory created: " + folderPath.toAbsolutePath());
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
                System.out.println("File created: " + filePath.toAbsolutePath());
            }

            Files.writeString(filePath, email, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            return true;

        } catch(IOException e){
             System.err.println("An error occurred while handling files: " + e.getMessage());
             e.printStackTrace();
        }

        return false;

    }

    private User fetchUserProfile(String email){

        Path filePath = Paths.get(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(filePath)){
            return null;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|",-1);

                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && parts[0].trim().equalsIgnoreCase(email.trim())){

                    String userEmail = parts[0].trim();
                    String userPassword = parts[1].trim();
                    String userName = parts[2].trim();
                    String userBalance = parts[3].trim();
                    String userCountry = parts[4].trim();
                    String userPhone = parts[5].trim();

                    return new User(userEmail,userPassword,userName,userBalance,userCountry,userPhone);

                }
            }

        } catch(Exception e){
            System.err.println("Error reading user profile: " + e.getMessage());
            e.printStackTrace();
        }

        return null;

    }

    public static void main(String[] args){
        new Login_Service("user2@gmail.com", "User@222");
    }

}
