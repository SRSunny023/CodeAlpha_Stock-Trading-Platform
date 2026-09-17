package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;

import util.Global_Variables;

public class Registration_Service {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(

        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$"

    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(

        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$"

    );

    public Registration_Service(String email, String password, String confirmPassword, String name){

        boolean isValidEmail = validateEmailAddress(email);
        boolean isValidPass = validatePassword(password);
        boolean isPassAndConfirmPassMatching = checkPassAndConfirmPass(password, confirmPassword);
        boolean isDuplicateEmail = checkDuplicateEmail(email);

        if(!isValidEmail){
            System.out.println("Email Not Valid");
            return;
        }

        if(!isValidPass){
            System.out.println("Password Not Valid");
            return;
        }

        if(!isPassAndConfirmPassMatching){
            System.out.println("Password and Confirm Password Not Matched");
            return;
        }

        if(isDuplicateEmail){
            System.out.println("Email Already Exist");
            return;
        }

        if(saveUserToDatabase(email,password,name)){
            System.out.println("Registration Completed");
        } else{
            System.out.println("Unhandled Error Occurred! Registration Failed.");
        }

        return;

    }

    private boolean validateEmailAddress(String email){


        return email != null && EMAIL_PATTERN.matcher(email).matches();

    }

    private boolean validatePassword(String password){

        return password != null && PASSWORD_PATTERN.matcher(password).matches();

    }

    private boolean checkPassAndConfirmPass(String password, String confirmPassword){

        return password.equals(confirmPassword);

    }

    public boolean checkDuplicateEmail(String email){

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
                if(parts.length>=3 && email.trim().equalsIgnoreCase(parts[0].trim())){
                    return true;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    private boolean saveUserToDatabase(String email, String password, String name){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = Paths.get(Global_Variables.USER_LIST_FILE);

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
                System.out.println("Directory created: " + folderPath.toAbsolutePath());
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
                System.out.println("File created: " + filePath.toAbsolutePath());
            }

            Files.writeString(filePath, email + "|" + password + "|" + name + "||" + System.lineSeparator() , StandardOpenOption.APPEND);

            return true;

        } catch(IOException e){
             System.err.println("An error occurred while handling files: " + e.getMessage());
             e.printStackTrace();
        }

        return false;

    }

    public static void main(String[] args){
        new Registration_Service("user2@gmail.com", "User@222", "User@222", "mr.user2");
    }

}
