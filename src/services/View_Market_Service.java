package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import model.Stock;
import util.Global_Variables;

public class View_Market_Service {

    private String[] stocksSymbol = new String[]{
        "APPL", "GOGL", "MSFT", "AMZN", "TSLA", "META", "NVDA", "NFLX", "DISN",
        "SBUX", "NKE", "MCD", "COCA", "PEPS", "FORD", "BOEG", "AMEX", "JPM",
        "WMT", "COST",
    };

    private String[] stocksCompany = new String[]{
        "Abble Inc.", "Alphabit Inc.", "Microsift Corp.", "Amazone.com Inc.",
        "Tessla Motors", "Mata Platforms", "Nvidiaa Corp.", "Netfliz Inc.",
        "Disnay Co.", "Starbux Coffee", "Nikey Inc.", "McDonolds Corp.", "Coka-Cola Co.",
        "Pepsico Corp.", "Fourd Motor Co.", "Boeingy Aerospace", "Amex Express",
        "JPMorzan Chase", "Walmurt Inc.", "Costko Wholesale",
    };

    private String[] stocksPrice = new String[]{
        "190.91", "133.18", "433.04", "211.21", "186.17", "513.82", "933.12", "585.03", "116.71",
        "112.2", "106.98", "261.32", "49.41", "183.99", "10.68", "190.81", "190.32", "218.29",
        "49.71", "783.14",
    };

    public ArrayList<Stock> stocks = new ArrayList<>();

    public View_Market_Service(DefaultTableModel model){

        loadStocks();

        for(Stock stock : stocks){
            model.addRow(new Object[]{stock.getSymbol(), stock.getCompany(), stock.getPrice() + "$", "Buy"});
        }

    }

    public void loadStocks(){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = folderPath.resolve(Global_Variables.STOCK_LIST_FILE);

            if(Files.notExists(folderPath)){
                Files.createDirectories(folderPath);
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
                writeStocks(filePath);
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

    private void writeStocks(Path filePath){

        try(FileWriter fw = new FileWriter(filePath.toFile())){

            for(int i=0; i<stocksSymbol.length; i++){
                String line = stocksSymbol[i] + "|" + stocksCompany[i] + "|" + stocksPrice[i] + "\n";
                fw.write(line);
            }

        } catch(Exception e){
            System.err.println("An error occurred during file operation or parsing: " + e.getMessage());
            e.printStackTrace();
        }

    }

}
