package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.User;
import ui.Main_Screen;
import util.Global_Variables;

public class Login_Service {

    private User loggedInUser;

    JFrame parentFrame;

    public Login_Service(String email, String password, JFrame parentFrame){

        this.parentFrame = parentFrame;

        boolean emailMatched = checkDuplicateEmail(email);
        boolean passwordMatched = checkPasswordMatch(email,password);

        if(!emailMatched){
            JOptionPane.showMessageDialog(parentFrame, "Invalid Email Address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!passwordMatched){
            JOptionPane.showMessageDialog(parentFrame, "Invalid Password!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(updateCurrentSession(email)){
            loggedInUser = fetchUserProfile(email);
            if(loggedInUser!=null){
                JOptionPane.showMessageDialog(parentFrame, "Logging in Successful!", "Logged In", JOptionPane.INFORMATION_MESSAGE);
                parentFrame.setVisible(false);
                parentFrame.dispose();
                SwingUtilities.invokeLater(() -> new Main_Screen(loggedInUser));
            } else {
                JOptionPane.showMessageDialog(parentFrame, "User profile could not be loaded. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else{
            JOptionPane.showMessageDialog(parentFrame, "Unhandled Error Occurred! Login Failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return;

    }

    private boolean checkDuplicateEmail(String email){

        Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
        Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(folderPath) || Files.notExists(filePath)){
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|");
                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && parts[0].trim().equalsIgnoreCase(email.trim())){
                    return true;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    private boolean checkPasswordMatch(String email, String password){

        Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
        Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(folderPath) || Files.notExists(filePath)){
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|");
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
            Path filePath = folderPath.resolve(Global_Variables.CURRENT_SESSION);

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
            }

            Files.writeString(filePath, email, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            return true;

        } catch(IOException e){
            JOptionPane.showMessageDialog(parentFrame, "An error occurred while handling files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;

    }

    private User fetchUserProfile(String email){

        Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
        Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(folderPath) || Files.notExists(filePath)){
            return null;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|");

                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && parts[0].trim().equalsIgnoreCase(email.trim())){

                    String userEmail = parts[0].trim();
                    String userPassword = parts[1].trim();
                    String userName = parts[2].trim();
                    String userBalance = parts[3].trim();

                    return new User(userEmail,userPassword,userName,userBalance);

                }
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(parentFrame, "Error reading user profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return null;

    }

}
