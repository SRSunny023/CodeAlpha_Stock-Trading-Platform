package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.Stock;
import util.Global_Variables;

public class View_Market_Service {

    public ArrayList<Stock> stocks = new ArrayList<>();

    public View_Market_Service(){

        loadStocks();

        for(Stock stock : stocks){
            System.out.println(stock);
        }

    }

    public void loadStocks(){

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

    public static void main(String[] args){
        new View_Market_Service();
    }

}
