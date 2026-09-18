package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingUtilities;

import model.User;
import ui.Login_Screen;
import ui.Main_Screen;
import util.Global_Variables;

public class Session {

    public Session(){

        sessionCheck();

    }

    private void sessionCheck(){

        String email = "";
        Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
        Path filePath = folderPath.resolve(Global_Variables.CURRENT_SESSION);
        Path userPath = folderPath.resolve(Global_Variables.USER_LIST_FILE);

        if(Files.notExists(folderPath) || Files.notExists(filePath) || Files.notExists(userPath)){

            SwingUtilities.invokeLater(() -> new Login_Screen());
            return;

        }

        try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

            String line = br.readLine();
            if(line!=null)
                email = line.trim();

        } catch(Exception e){
            e.printStackTrace();
        }

        if(email.isEmpty()){
            SwingUtilities.invokeLater(() -> new Login_Screen());
            return;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(userPath.toFile()))){

            String line;
            while((line=br.readLine())!=null){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split("\\|");
                if(parts.length>0 && parts[0].trim().equalsIgnoreCase(email)){
                    String userEmail = parts[0].trim();
                    String userPassword = parts[1].trim();
                    String userName = parts[2].trim();
                    String userBalance = parts[3].trim();
                    String userCountry = parts.length>4 ? parts[4].trim() : "";
                    String userPhone = parts.length>5 ? parts[5].trim() : "";
                    User user = new User(userEmail, userPassword, userName, userBalance, userCountry, userPhone);
                    SwingUtilities.invokeLater(() -> new Main_Screen(user));
                    return;
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Login_Screen());

    }

}
