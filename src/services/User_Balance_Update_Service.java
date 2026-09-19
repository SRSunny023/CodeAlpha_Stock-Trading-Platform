package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.User;
import util.Global_Variables;

public class User_Balance_Update_Service {

    User user;

    public User_Balance_Update_Service(User user){

        this.user = user;

        updateBalance();

    }

    private void updateBalance(){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
            }

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
                }
            } else {
                System.out.println("User not found in user_list.txt.");
            }

        } catch(Exception e){
            System.err.println("An error occurred while handling files: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
