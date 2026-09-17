package model;

public class Stock {

    private String symbol;
    private String company;
    private double price;

    public Stock(String symbol, String company, double price){
        this.symbol = symbol;
        this.company = company;
        this.price = price;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public String getCompany(){
        return this.company;
    }

    public double getPrice(){
        return this.price;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public void setPrice(double price){
        this.price = price;
    }

    @Override
    public String toString(){
        return String.format("%-6s | %-20s | $%.2f", symbol, company, price);
    }

}
