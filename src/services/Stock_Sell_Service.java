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

public class Stock_Sell_Service {

    Stock stock;
    User user;
    int amount;
    double totalPrice;
    double userBalance;

    public Stock_Sell_Service(Stock stock, User user, int amount){
        this.stock = stock;
        this.user = user;
        this.amount = amount;
        sellStock();
    }

    private void sellStock(){

        totalPrice = amount * stock.getPrice();
        userBalance = Double.parseDouble(user.getBalance());

        if(!haveEnoughShares()){
            System.out.println("You have not enough shares to sell");
            return;
        }

        if(!updatePortfolio()){
            System.out.println("Sell Failed!");
            return;
        }

        System.out.println("Sell Successfull");
        userBalance+=totalPrice;
        user.setBalance(Double.toString(userBalance));

        new User_Balance_Update_Service(user);

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
            System.err.println("An error occurred while handling files: " + e.getMessage());
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
                stock.getSymbol() + "|" + amount + "|" + totalPrice + "|" + "Sold" + System.lineSeparator(),
                StandardOpenOption.APPEND
            );

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
                        int newAmount = oldAmount - amount;
                        if(newAmount>0){
                            line = parts[0] + "|" + newAmount;
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

            return true;

        } catch(Exception e){
            System.err.println("An error occurred while handling files: " + e.getMessage());
            e.printStackTrace();
        }

        return false;

    }

    public static void main(String[] args){

        new Stock_Sell_Service(
            new Stock("APPL", "Abble Inc.", 182.50),
            new User("mock@gmail.com", "Mock@111", "mr.mock", "3000"),
            5
        );

    }

}
