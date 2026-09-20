package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import model.Stock;
import model.User;
import ui.Main_Screen;
import util.Global_Variables;

public class Market_Simulation_Service {

    private final ScheduledExecutorService scheduler;
    private final Random random;
    private Main_Screen mainScreen;

    User user;

    public Market_Simulation_Service(User user, Main_Screen mainScreen){

        this.user = user;
        this.mainScreen = mainScreen;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.random = new Random();

    }

    public void startSimulation(){
        scheduler.scheduleAtFixedRate(this::updateMarketPrices, 0, 10, TimeUnit.SECONDS);
    }

    public void stopSimulation(){
        scheduler.shutdown();
    }

    private void updateMarketPrices(){

        try{

            Path folderPath = Paths.get(Global_Variables.DATA_FOLDER);
            Path filePath = folderPath.resolve(Global_Variables.STOCK_LIST_FILE);

            if(Files.notExists(filePath)){
                return;
            }

            List<Stock> temporaryStockPool = new ArrayList<>();

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
                        double oldPrice = Double.parseDouble(parts[2]);

                        double changePercent = (random.nextDouble() * 0.04) - 0.02;
                        double newPrice = oldPrice*(1+changePercent);
                        newPrice = Math.round(newPrice*100.0)/100.0;

                        if(newPrice < 10.00){
                            newPrice = 10.00;
                        }
                        temporaryStockPool.add(new Stock(symbol,company,newPrice));
                    }
                }
            }

            StringBuilder content = new StringBuilder();
            for(Stock stock : temporaryStockPool){
                content.append(stock.getSymbol()).append("|")
                       .append(stock.getCompany()).append("|")
                       .append(stock.getPrice()).append(System.lineSeparator());
            }
            try(FileWriter fw = new FileWriter(filePath.toFile())){
                fw.write(content.toString());
            }
            SwingUtilities.invokeLater(() -> mainScreen.refreshScreen());

        } catch(Exception e){
            System.err.println("File update failure occurred: " + e.getMessage());
        }

    }

}
