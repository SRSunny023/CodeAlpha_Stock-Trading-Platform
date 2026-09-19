package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.Transaction;
import model.User;
import util.Global_Variables;

public class Transaction_History_Service {

    User user;

    ArrayList<Transaction> transactions = new ArrayList<>();

    int totalBuys = 0;
    int totalSells = 0;
    double totalMoney = 0;

    public Transaction_History_Service(User user){

        this.user = user;

        loadTransaction();

        if(!transactions.isEmpty()){
            transactionDashboard();
            viewTransactions();
            transactionSummaryDashboard();
        } else{
            System.out.println("TRANSACTION HISTORY REPORT");
            System.out.println("Account Owner: " + user.getName());
            System.out.println(" [!] No trade history found for this account.");
            System.out.println("=========================");
        }


    }

    private void transactionDashboard(){

        System.out.println("TRANSACTION HISTORY REPORT");
        System.out.println("=================================");
        System.out.println("Account Owner: " + user.getName());
        System.out.println("Current Balance: $" + user.getBalance());
        System.out.println("=================================");
        System.out.println("Symbol\tShares\tTotal Cost\tAction");
        System.out.println("=================================");

    }

    private void transactionSummaryDashboard(){

        System.out.println("=================================");
        System.out.println("Total Operations Handled: " + transactions.size() + " ( " + totalBuys + " Buys / " + totalSells + " Sells)");
        System.out.println("Cumulative Money Exchanged: " + totalMoney);
        System.out.println("=================================");

    }

    private void loadTransaction(){

        try{

            String name = user.getEmail().replaceAll("@.*", "");
            Path parentFolderPath = Paths.get(Global_Variables.PORTFOLIO_FOLDER);
            Path folderPath = parentFolderPath.resolve(name);
            Path transactionPath = folderPath.resolve("transaction.txt");

            if(Files.notExists(parentFolderPath) || Files.notExists(folderPath) || Files.notExists(transactionPath)){
                System.out.println("Transaction History Not Found");
                return;
            }

            try(BufferedReader br = new BufferedReader(new FileReader(transactionPath.toFile()))){

                String line;

                while((line=br.readLine())!=null){
                    if(line.trim().isEmpty()){
                        continue;
                    }
                    String[] parts = line.split("\\|");
                    if(parts.length>=4){
                        String symbol = parts[0];
                        int shares = Integer.parseInt(parts[1]);
                        double totalCost = Double.parseDouble(parts[2]);
                        String action = parts[3];
                        if(action.equals("Bought")){
                            totalBuys++;
                        } else if(action.equals("Sold")){
                            totalSells++;
                        }
                        totalMoney+=totalCost;
                        Transaction transaction = new Transaction(symbol, shares, totalCost, action);
                        transactions.add(transaction);
                    }

                }

            }

        } catch(Exception e){
            System.err.println("An error occurred while handling files: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void viewTransactions(){
        for(Transaction transaction : transactions){
            System.out.println(transaction);
        }
    }

    public static void main(String[] args){
        new Transaction_History_Service(
            new User("mock@gmail.com","Mock@111","mr.mock","3000")
        );
    }

}
