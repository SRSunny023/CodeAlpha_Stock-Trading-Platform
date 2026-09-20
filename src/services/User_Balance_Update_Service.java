package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.User;
import util.Global_Variables;

public class User_Balance_Update_Service {

    private User user;
    private JFrame parentFrame;

    public User_Balance_Update_Service(User user, JFrame parentFrame){

        this.user = user;
        this.parentFrame = parentFrame;

    }

    public boolean updateBalance(){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
            }

            StringBuilder content = new StringBuilder();
            boolean updated = false;

            try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

                String line;
                while((line=br.readLine())!=null){
                    if(line.isEmpty()){
                        continue;
                    }
                    String[] parts = line.split("\\|",-1);
                    if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && parts[0].trim().equalsIgnoreCase(user.getEmail().trim())){

                        String email = parts[0];
                        String password = parts[1];
                        String name = parts[2];
                        String balance = user.getBalance();

                        line = email + "|" + password + "|" + name + "|" + balance;

                        updated = true;

                    }
                    content.append(line).append(System.lineSeparator());
                }

            }

            if (updated) {
                try(FileWriter fw = new FileWriter(filePath.toFile())){
                    fw.write(content.toString());
                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(parentFrame, "User not found in user_list.txt.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(parentFrame, "An error occurred while handling files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }

    }

}
