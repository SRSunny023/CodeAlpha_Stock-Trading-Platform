package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.Portfolio;
import model.Stock;
import model.User;
import util.Global_Variables;

public class Portfolio_Viewer_Service {

    User user;

    ArrayList<Stock> stocks = new ArrayList<>();

    ArrayList<Portfolio> portfolios = new ArrayList<>();

    public Portfolio_Viewer_Service(User user){

        this.user = user;

        loadStocks();

        loadPortfolio();

        if(portfolios.size()>0){
            portfolioDashboard();
            viewPortfolio();
        } else{
            System.out.println("No Portfolio to Show");
        }

    }

    private void portfolioDashboard(){

        System.out.println("DASHBOARD");
        System.out.println("Client Name: " + user.getName());
        System.out.println("Client Balance: $" + user.getBalance());

        System.out.printf("%-6s | %-11s | %-10s | %-10s\n", "Symbol", "Shares HELD", "Live Price", "Total Value");

    }

    private void loadStocks(){

        try{

            Path filePath = Paths.get(Global_Variables.STOCK_LIST_FILE);

            if(Files.notExists(filePath)){
                System.out.println("market_stocks.txt not found. So couldn't load stocks");
                return;
            }

            try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

                String line;

                while((line=br.readLine())!=null){
                    if(line.trim().isEmpty()){
                        continue;
                    }
                    String[] parts = line.split("\\|");
                    if(parts.length>=3){
                        String symbol = parts[0];
                        String company = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        Stock stock = new Stock(symbol, company, price);
                        stocks.add(stock);
                    }
                }

            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("An error occurred during file operation or parsing: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void loadPortfolio(){

        try{

            String name = user.getEmail().replaceAll("@.*", "");
            Path parentFolderPath = Paths.get(Global_Variables.PORTFOLIO_FOLDER);
            Path folderPath = parentFolderPath.resolve(name);
            Path filePath = folderPath.resolve("portfolio.txt");

            if(Files.notExists(parentFolderPath) || Files.notExists(folderPath) || Files.notExists(filePath)){
                return;
            }

            try(BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))){

                String line;
                while((line=br.readLine())!=null){
                    if(line.isEmpty()){
                        continue;
                    }
                    String[] parts = line.split("\\|");
                    if(parts.length>=2){
                        String symbol = parts[0];
                        int sharesHeld = Integer.parseInt(parts[1]);
                        double price = 0;
                        for(Stock stock : stocks){
                            if(stock.getSymbol().equals(symbol)){
                                price = stock.getPrice();
                                break;
                            }
                        }
                        double totalValue = sharesHeld*price;
                        Portfolio portfolio = new Portfolio(symbol, sharesHeld, price, totalValue);
                        portfolios.add(portfolio);
                    }
                }

            }

        } catch(Exception e){
            System.err.println("An error occurred while handling files: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void viewPortfolio(){

        for(Portfolio portfolio : portfolios){
            System.out.println(portfolio);
        }

    }

    public static void main(String[] args){

        new Portfolio_Viewer_Service(
            new User("mock@gmail.com", "Mock@111", "mr.mock", "3000", "Bangladesh", "+880")
        );

    }

}
