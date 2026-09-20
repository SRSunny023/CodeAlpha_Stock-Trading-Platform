package services;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import model.Transaction;
import model.User;
import util.Global_Variables;

public class Transaction_History_Service {

    User user;

    ArrayList<Transaction> transactions = new ArrayList<>();

    int totalBuys = 0;
    int totalSells = 0;
    double totalMoney = 0;
    double totalBoughtMoney = 0;
    double totalSoldMoney = 0;

    public Transaction_History_Service(User user, DefaultTableModel model, JLabel[] labels){

        this.user = user;

        loadTransaction();

        if(!transactions.isEmpty()){
            for(Transaction transaction : transactions){
                model.addRow(new Object[]{transaction.getSymbol(), transaction.shares(), String.format("%.2f$", transaction.totalCost()), transaction.action()});
            }
            labels[3].setText(transactions.size() + " ( " + totalBuys + " Buys / " + totalSells + " Sells)");
            labels[4].setText(String.format("%.2f$ ( Bought: %.2f$ / Sold: %.2f$ )", totalMoney,totalBoughtMoney,totalSoldMoney));
            labels[5].setText(String.format("%.2f$", Math.abs(totalSoldMoney-totalBoughtMoney)));
            if((totalSoldMoney-totalBoughtMoney) > 0){
                labels[5].setForeground(Color.GREEN);
            } else if((totalSoldMoney-totalBoughtMoney) < 0){
                labels[5].setForeground(Color.RED);
            }
        }

    }

    private void loadTransaction(){

        try{

            String name = user.getEmail().replaceAll("@.*", "");
            Path parentFolderPath = Paths.get(Global_Variables.PORTFOLIO_FOLDER);
            Path folderPath = parentFolderPath.resolve(name);
            Path transactionPath = folderPath.resolve("transaction.txt");

            if(Files.notExists(parentFolderPath) || Files.notExists(folderPath) || Files.notExists(transactionPath)){
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
                            totalBoughtMoney+=totalCost;
                        } else if(action.equals("Sold")){
                            totalSells++;
                            totalSoldMoney+=totalCost;
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

}
