package model;

public class Portfolio {

    private String symbol;
    private int sharesHeld;
    private double price;
    private double totalValue;
    private double initialTotalValue;

    public Portfolio(String symbol, int sharesHeld, double price, double totalValue, double initialTotalValue){
        this.symbol = symbol;
        this.sharesHeld = sharesHeld;
        this.price = price;
        this.totalValue = totalValue;
        this.initialTotalValue = initialTotalValue;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public int getShares(){
        return this.sharesHeld;
    }

    public double getPrice(){
        return this.price;
    }

    public double getTotalValue(){
        return this.totalValue;
    }

    public double getInitialTotalValue(){
        return this.initialTotalValue;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setShares(int sharesHeld){
        this.sharesHeld = sharesHeld;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setTotalValue(double totalValue){
        this.totalValue = totalValue;
    }

    public void setInitialTotalValue(double initialTotalValue){
        this.initialTotalValue = initialTotalValue;
    }

    @Override
    public String toString(){
        return String.format("%-6s | %-11s | $%.2f | $%.2f", symbol, sharesHeld, price, totalValue);
    }

}
