package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import model.Stock;
import model.User;
import util.Global_Variables;

public class Stock_Buy_Service {

    Stock stock;
    User user;
    int amount;
    double totalPrice;
    double userBalance;

    public Stock_Buy_Service(Stock stock, User user, int amount){

        this.stock = stock;
        this.user = user;
        this.amount = amount;

        purchaseStock();

    }

    private void purchaseStock(){
        totalPrice = amount * stock.getPrice();
        userBalance = Double.parseDouble(user.getBalance());

        if(totalPrice>userBalance){
            System.out.println("You don't have enough money to buy. You need " + (totalPrice-userBalance) + " more money to buy");
            return;
        }

        userBalance-=totalPrice;

        user.setBalance(Double.toString(userBalance));

        if(updatePortfolio()){

            System.out.println("Stock Purchase Successfully");

            new User_Balance_Update_Service(user);

        } else{

            System.out.println("Stock Purchase Failed");

        }

        return;
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

            Files.writeString(
                transactionPath,
                stock.getSymbol() + "|" + amount + "|" + totalPrice + "|" + "Bought" + System.lineSeparator(),
                StandardOpenOption.APPEND
            );

            Boolean updated = false;
            StringBuilder content = new StringBuilder();

            try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

                String line;

                while((line=br.readLine())!=null){
                    if(line.trim().isEmpty()){
                        continue;
                    }
                    String[] parts = line.split("\\|");
                    if(parts.length>=2 && parts[0].equals(stock.getSymbol())){
                        int oldAmount = Integer.parseInt(parts[1]);
                        int newAmount = oldAmount + amount;
                        line = parts[0] + "|" + newAmount;
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
                    stock.getSymbol() + "|" + amount + System.lineSeparator(),
                    StandardOpenOption.APPEND
                );
            }

            return true;

        } catch(Exception e){
            System.err.println("An error occurred while handling files: " + e.getMessage());
            e.printStackTrace();
        }

        return false;

    }

    public static void main(String[] args){

        new Stock_Buy_Service(
            new Stock("APPL", "Abble Inc.", 182.50),
            new User("mock@gmail.com", "Mock@111", "mr.mock", "3000"),
            5
        );

    }

}
