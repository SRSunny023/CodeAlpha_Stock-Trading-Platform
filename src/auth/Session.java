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
                if(parts.length>=Global_Variables.USER_DETAILS_LENGTH && parts[0].trim().equalsIgnoreCase(email)){
                    User user = new User(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
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
