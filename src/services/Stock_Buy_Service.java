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

public class Stock_Buy_Service {

    private Stock stock;
    private User user;
    private int amount;
    private double totalPrice;
    private double userBalance;
    private JFrame parentFrame;

    public Stock_Buy_Service(Stock stock, User user, int amount, JFrame parentFrame){

        this.stock = stock;
        this.user = user;
        this.amount = amount;
        this.parentFrame = parentFrame;

    }

    public boolean purchaseStock(){
        totalPrice = amount * stock.getPrice();
        userBalance = Double.parseDouble(user.getBalance());

        if(totalPrice>userBalance){
            JOptionPane.showMessageDialog(parentFrame,
                "You don't have enough money to buy. You need " + String.format("%.2f$", totalPrice-userBalance) + " more money to buy",
                "Transaction Failed",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        double newBalance = userBalance-totalPrice;

        user.setBalance(Double.toString(newBalance));

        if(!updatePortfolio()){
            user.setBalance(Double.toString(userBalance));
            JOptionPane.showMessageDialog(parentFrame, "Stock Purchase Failed", "Transaction Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        User_Balance_Update_Service balanceUpdate = new User_Balance_Update_Service(user, parentFrame);

        boolean success = balanceUpdate.updateBalance();

        if(success){
            JOptionPane.showMessageDialog(parentFrame, "Stock Purchase Successfully", "Transaction Successful", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else{
            user.setBalance(Double.toString(userBalance));
            JOptionPane.showMessageDialog(parentFrame, "Stock Purchase Failed", "Transaction Failed", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    private boolean updatePortfolio(){

        try{

            String name = user.getEmail().replaceAll("@.*", "");
            Path parentFolderPath = Paths.get(Global_Variables.PORTFOLIO_FOLDER);
            Path folderPath = parentFolderPath.resolve(name);
            Path transactionPath = folderPath.resolve("transaction.txt");
            Path filePath = folderPath.resolve("portfolio.txt");

            if(Files.notExists(parentFolderPath)){
                Files.createDirectories(parentFolderPath);
                System.out.println("Directory created: " + parentFolderPath.toAbsolutePath());
            }

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
                System.out.println("Directory created: " + folderPath.toAbsolutePath());
            }

            if(Files.notExists(transactionPath)){
                Files.createFile(transactionPath);
                System.out.println("File created: " + transactionPath.toAbsolutePath());
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
                System.out.println("File created: " + filePath.toAbsolutePath());
            }

            Boolean updated = false;
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
                        int newAmount = oldAmount + amount;
                        double oldTotalPrice = Double.parseDouble(parts[2]);
                        double newTotalPrice = oldTotalPrice + totalPrice;
                        line = parts[0] + "|" + newAmount + "|" + newTotalPrice;
                        updated = true;
                    }
                    content.append(line);
                    content.append(System.lineSeparator());

                }

            }

            if(updated){

                try(FileWriter fw = new FileWriter(filePath.toFile())){
                    fw.write(content.toString());
                }

            } else{
                Files.writeString(filePath,
                    stock.getSymbol() + "|" + amount + "|" + totalPrice + System.lineSeparator(),
                    StandardOpenOption.APPEND
                );
            }

            Files.writeString(
                transactionPath,
                stock.getSymbol() + "|" + amount + "|" + totalPrice + "|" + "Bought" + System.lineSeparator(),
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
