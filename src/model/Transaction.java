package model;

public class Transaction {

    private String symbol;
    private int shares;
    private double totalCost;
    private String action;

    public Transaction(String symbol, int shares, double totalCost, String action){

        this.symbol = symbol;
        this.shares = shares;
        this.totalCost = totalCost;
        this.action = action;

    }

    public String getSymbol(){
        return this.symbol;
    }

    public int shares(){
        return this.shares;
    }

    public double totalCost(){
        return this.totalCost;
    }

    public String action(){
        return this.action;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public void setShares(int shares){
        this.shares = shares;
    }

    public void setTotalCost(double totalCost){
        this.totalCost = totalCost;
    }

    public void setAction(String action){
        this.action = action;
    }

    @Override
    public String toString(){
        return String.format("%-6s | %-11s | $%.2f | %-10s", symbol, shares, totalCost, action);
    }

}