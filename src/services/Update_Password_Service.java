package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.User;
import util.Global_Variables;

public class Update_Password_Service {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(

        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$"

    );

    JFrame parentFrame;
    User user;

    public Update_Password_Service(String password, String confirmPassword, JFrame parentFrame, User user){

        this.parentFrame = parentFrame;
        this.user = user;

        boolean isValidPass = validatePassword(password);
        boolean isPassAndConfirmPassMatching = checkPassAndConfirmPass(password, confirmPassword);

        if(!isValidPass){
            JOptionPane.showMessageDialog(parentFrame, "Password is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(!isPassAndConfirmPassMatching){
            JOptionPane.showMessageDialog(parentFrame, "Password Didn't Matched!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(updatePassword(password)){
            user.setPassword(password);
            JOptionPane.showMessageDialog(parentFrame, "Password Successfully Updated!", "Successfull", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Failed to update password. Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }

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

    private boolean updatePassword(String password){

        Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
        Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(folderPath) || Files.notExists(filePath)){
            return false;
        }

        StringBuilder content = new StringBuilder();
        boolean updated = false;

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|");
                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && user.getEmail().trim().equalsIgnoreCase(parts[0].trim())){
                    line = parts[0] + "|" + password + "|" + parts[2] + "|" + parts[3];
                    updated = true;
                }
                content.append(line).append(System.lineSeparator());
            }

            if(updated){
                try(FileWriter fw = new FileWriter(filePath.toFile())){
                    fw.write(content.toString());
                    return true;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

}
