package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ui.Login_Screen;
import util.Global_Variables;

public class Registration_Service {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(

        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$"

    );

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(

        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$"

    );

    private static final String INITIAL_BALANCE = "1000";

    JFrame parentFrame;

    public Registration_Service(String email, String password, String confirmPassword, String name, JFrame parentFrame){

        this.parentFrame = parentFrame;

        boolean isValidEmail = validateEmailAddress(email);
        boolean isValidPass = validatePassword(password);
        boolean isPassAndConfirmPassMatching = checkPassAndConfirmPass(password, confirmPassword);
        boolean isDuplicateEmail = checkDuplicateEmail(email);

        if(!isValidEmail){
            JOptionPane.showMessageDialog(parentFrame, "Email Address is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!isValidPass){
            JOptionPane.showMessageDialog(parentFrame, "Password is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!isPassAndConfirmPassMatching){
            JOptionPane.showMessageDialog(parentFrame, "Password Didn't Matched!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(isDuplicateEmail){
            JOptionPane.showMessageDialog(parentFrame, "Email Already Exist!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(saveUserToDatabase(email,password,name)){
            JOptionPane.showMessageDialog(parentFrame, "Registration Completed", "Registration", JOptionPane.INFORMATION_MESSAGE);
            parentFrame.setVisible(false);
            parentFrame.dispose();
            SwingUtilities.invokeLater(() -> new Login_Screen());
        } else{
            JOptionPane.showMessageDialog(parentFrame, "Unhandled Error Occurred! Registration Failed.", "Error", JOptionPane.ERROR_MESSAGE);
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

        if(password == null || confirmPassword == null){
            return false;
        }

        return password.equals(confirmPassword);

    }

    public boolean checkDuplicateEmail(String email){

        Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
        Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(filePath)){
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|");
                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && email.trim().equalsIgnoreCase(parts[0].trim())){
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
            Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

            Files.writeString(
                filePath,
                email + "|" + password + "|" + name + "|" + INITIAL_BALANCE + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );

            return true;

        } catch(IOException e){
            JOptionPane.showMessageDialog(parentFrame, "An error occurred while handling files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;

    }

}
