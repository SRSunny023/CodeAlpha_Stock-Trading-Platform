package util;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.User;
import services.Market_Simulation_Service;
import ui.Login_Screen;

public class Global_Functions {

    public void exitApp(JFrame parentFrame, Market_Simulation_Service marketSimulator){
        int response = JOptionPane.showConfirmDialog(parentFrame, "Want to Exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if(response==JOptionPane.YES_OPTION){
            if(marketSimulator != null){
                marketSimulator.stopSimulation();
            }
            parentFrame.setVisible(false);
            parentFrame.dispose();
            System.exit(0);
        }
        return;
    }

    public void logout(JFrame parenFrame, User loggedInUser, Market_Simulation_Service marketSimulator){
        int respone = JOptionPane.showConfirmDialog(parenFrame, "Want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if(respone==JOptionPane.YES_OPTION){
            if(logoutService()){
                JOptionPane.showMessageDialog(parenFrame, "Logout Successfull!", "Logout", JOptionPane.INFORMATION_MESSAGE);
                if(marketSimulator != null){
                    marketSimulator.stopSimulation();
                }
                parenFrame.setVisible(false);
                parenFrame.dispose();
                SwingUtilities.invokeLater(() -> new Login_Screen());
            } else{
                JOptionPane.showMessageDialog(parenFrame, "Logout Failed Due To Some Error!", "Logout", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean logoutService(){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = folderPath.resolve(Global_Variables.CURRENT_SESSION);

            if(Files.notExists(folderPath) || Files.notExists(filePath)){
                return true;
            }

            try(FileWriter fw = new FileWriter(filePath.toFile())){
                fw.write("");
            }

            return true;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

}
