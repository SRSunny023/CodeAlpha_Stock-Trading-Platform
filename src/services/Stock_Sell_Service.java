package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Stock;
import model.User;
import util.Global_Variables;

public class Stock_Sell_Service {

    private Stock stock;
    private User user;
    private int amount;
    private double totalPrice;
    private double userBalance;
    private JFrame parentFrame;

    public Stock_Sell_Service(Stock stock, User user, int amount, JFrame parentFrame){
        this.stock = stock;
        this.user = user;
        this.amount = amount;
        this.parentFrame = parentFrame;
    }

    public boolean sellStock(){

        totalPrice = amount * stock.getPrice();
        userBalance = Double.parseDouble(user.getBalance());

        if(!haveEnoughShares()){
            JOptionPane.showMessageDialog(parentFrame, "You have not enough shares to sell", "Transaction Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double newBalance = userBalance+totalPrice;

        user.setBalance(Double.toString(newBalance));

        if(!updatePortfolio()){
            user.setBalance(Double.toString(userBalance));
            JOptionPane.showMessageDialog(parentFrame, "Couldn't Sell Shares!", "Transaction Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        User_Balance_Update_Service balanceUpdate = new User_Balance_Update_Service(user, parentFrame);

        boolean success = balanceUpdate.updateBalance();

        if(success){
            JOptionPane.showMessageDialog(parentFrame, "Shares Sold Successfully", "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else{
            user.setBalance(Double.toString(userBalance));
            JOptionPane.showMessageDialog(parentFrame,"Failed to update balance","Transaction Failed",JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    private boolean haveEnoughShares(){

        try{

            String name = user.getEmail().replaceAll("@.*", "");
            Path parentFolderPath = Paths.get(Global_Variables.PORTFOLIO_FOLDER);
            Path folderPath = parentFolderPath.resolve(name);
            Path filePath = folderPath.resolve("portfolio.txt");

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
                    if(parts.length>=2 && parts[0].equals(stock.getSymbol()) && Integer.parseInt(parts[1]) >= amount ){
                        return true;
                    }
                }

            }

        } catch(Exception e){
            JOptionPane.showMessageDialog(parentFrame, "An error occurred while handling files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return false;

    }

    private boolean updatePortfolio(){

        try{

            String name = user.getEmail().replaceAll("@.*", "");
            Path parentFolderPath = Paths.get(Global_Variables.PORTFOLIO_FOLDER);
            Path folderPath = parentFolderPath.resolve(name);
            Path transactionPath = folderPath.resolve("transaction.txt");
            Path filePath = folderPath.resolve("portfolio.txt");

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
            }

            if(Files.notExists(transactionPath)){
                Files.createFile(transactionPath);
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
            }

            StringBuilder content = new StringBuilder();

            try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

                String line;

                while((line=br.readLine())!=null){
                    if(line.trim().isEmpty()){
                        continue;
                    }
                    String[] parts = line.split("\\|");
                    if(parts.length>=3 && parts[0].equals(stock.getSymbol())){
                        int oldAmount = Integer.parseInt(parts[1]);
                        int newAmount = oldAmount - amount;
                        double oldTotalPrice = Double.parseDouble(parts[2]);
                        double newTotalPrice = oldTotalPrice - totalPrice;
                        if(newAmount>0){
                            line = parts[0] + "|" + newAmount + "|" + newTotalPrice;
                            content.append(line);
                            content.append(System.lineSeparator());
                        }
                        continue;
                    }
                    content.append(line);
                    content.append(System.lineSeparator());

                }

            }

            try(FileWriter fw = new FileWriter(filePath.toFile())){
                fw.write(content.toString());
            }

            Files.writeString(
                transactionPath,
                stock.getSymbol() + "|" + amount + "|" + totalPrice + "|" + "Sold" + System.lineSeparator(),
                StandardOpenOption.APPEND
            );

            return true;

        } catch(Exception e){
            JOptionPane.showMessageDialog(parentFrame, "An error occurred while handling files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return false;

    }

}
