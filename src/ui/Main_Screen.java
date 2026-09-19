package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Stock;
import model.User;
import services.Portfolio_Viewer_Service;
import services.Stock_Buy_Service;
import services.Stock_Sell_Service;
import services.Transaction_History_Service;
import services.Update_Password_Service;
import services.View_Market_Service;
import util.Global_Functions;

public class Main_Screen extends JFrame implements ActionListener,MouseListener {

    private User loggedInUser;

    private boolean isListenerAdded = false;

    JButton[] buttons = new JButton[]{
        new JButton("Update Password"),
        new JButton("Logout"),
        new JButton("Exit"),
    };

    JTable marketTable, portfolioTable, transactionTable;

    public Main_Screen(User loggedInUser){

        this.loggedInUser = loggedInUser;

        initScreen();

    }

    private void initScreen(){

        getContentPane().setBackground(Color.BLACK);

        setUndecorated(true);
        setSize(1024, 768);

        setLocationRelativeTo(null);
        setLayout(null);

        setHeaderFooter();

        setViewMarketPanel();
        setPortfolioViewPanel();
        setTransactionViewPanel();

        setVisible(true);

    }

    private void setHeaderFooter(){

        JLabel[] labels = new JLabel[]{
            new JLabel("NAME:"),
            new JLabel("BALANCE:"),
            new JLabel(),
            new JLabel(),
        };

        JPanel header = new JPanel();
        header.setLayout(null);
        header.setBackground(Color.GRAY);
        header.setBounds(0, 0, 1024, 76);
        add(header);

        JPanel footer = new JPanel();
        footer.setLayout(null);
        footer.setBackground(Color.GRAY);
        footer.setBounds(0, 692, 1024, 76);
        add(footer);

        for(int i=0; i<labels.length; i++){
            labels[i].setForeground(Color.BLUE);
            labels[i].setFont(new Font("Arial", Font.BOLD, 16));
            if(i<2)
                labels[i].setBounds(50 + (700*i), 20, 250, 30);
            else
                labels[i].setBounds(130 + (720*(i-2)), 20, 350, 30);

            header.add(labels[i]);
        }

        labels[2].setText(loggedInUser.getName());
        labels[3].setText(String.format("%.2f$", Double.parseDouble(loggedInUser.getBalance())));

        for(int i=0; i<buttons.length; i++){
            buttons[i].setForeground(Color.BLUE);
            buttons[i].setBackground(Color.GRAY);
            buttons[i].setFont(new Font("Arial", Font.BOLD, 16));
            buttons[i].setFocusPainted(false);
            if(!isListenerAdded){
                buttons[i].addActionListener(this);
            }
            buttons[i].setBounds(180 + (210*i), 20, 200, 30);
            footer.add(buttons[i]);
        }
        isListenerAdded = true;

    }

    private void setViewMarketPanel(){

        JScrollPane scrollPane;
        DefaultTableModel model;

        String[] columns = new String[]{
            "Symbol",
            "Company",
            "Price",
            "Action"
        };

        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        marketTable = new JTable(model);
        marketTable.setFont(new Font("Arial", Font.BOLD, 16));
        marketTable.setRowHeight(30);
        marketTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        marketTable.getTableHeader().setBackground(Color.BLACK);
        marketTable.getTableHeader().setForeground(Color.WHITE);
        marketTable.setBackground(Color.BLACK);
        marketTable.setForeground(Color.WHITE);
        marketTable.addMouseListener(this);

        scrollPane = new JScrollPane(marketTable);
        scrollPane.setBounds(0, 77, 512, 613);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.getViewport().setForeground(Color.WHITE);

        add(scrollPane);

        new View_Market_Service(model);

    }

    private void setPortfolioViewPanel(){

        JScrollPane scrollPane;
        DefaultTableModel model;

        String[] columns = new String[]{
            "Symbol",
            "Shares Owned",
            "Price",
            "Total Value",
            "Action"
        };

        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        portfolioTable = new JTable(model);
        portfolioTable.setFont(new Font("Arial", Font.BOLD, 16));
        portfolioTable.setRowHeight(30);
        portfolioTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        portfolioTable.getTableHeader().setBackground(Color.BLACK);
        portfolioTable.getTableHeader().setForeground(Color.WHITE);
        portfolioTable.setBackground(Color.BLACK);
        portfolioTable.setForeground(Color.WHITE);
        portfolioTable.addMouseListener(this);

        scrollPane = new JScrollPane(portfolioTable);
        scrollPane.setBounds(514, 77, 510, 306);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.getViewport().setForeground(Color.WHITE);

        add(scrollPane);

        new Portfolio_Viewer_Service(loggedInUser, model);

    }

    private void setTransactionViewPanel(){
        JScrollPane scrollPane;
        DefaultTableModel model;

        String[] columns = new String[]{
            "Symbol",
            "Shares",
            "Amount",
            "Status"
        };

        model = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        transactionTable = new JTable(model);
        transactionTable.setFont(new Font("Arial", Font.BOLD, 16));
        transactionTable.setRowHeight(30);
        transactionTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        transactionTable.getTableHeader().setBackground(Color.BLACK);
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.setBackground(Color.BLACK);
        transactionTable.setForeground(Color.WHITE);
        transactionTable.addMouseListener(this);

        scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(514, 425, 510, 164);
        scrollPane.getViewport().setBackground(Color.BLACK);
        scrollPane.getViewport().setForeground(Color.WHITE);

        JLabel label = new JLabel("Transaction History");
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.ORANGE);
        label.setBounds(700, 395, 510, 20);

        JLabel[] labels = new JLabel[]{
            new JLabel("Total Operations:"),
            new JLabel("Total Money Exchanged:"),
            new JLabel(),
            new JLabel(),
        };

        add(label);
        add(scrollPane);

        for(int i=0; i<labels.length; i++){
            labels[i].setFont(new Font("Arial", Font.BOLD, 16));
            labels[i].setForeground(Color.ORANGE);
            if(i<2){
                labels[i].setBounds(514, 595 + (50*i), 200, 30);
            } else{
                labels[i].setBounds(720, 595 + (50*(i-2)), 200, 30);
            }
            add(labels[i]);
        }

        new Transaction_History_Service(loggedInUser, model, labels);
    }

    private void refreshScreen(){

        getContentPane().removeAll();

        setHeaderFooter();
        setViewMarketPanel();
        setPortfolioViewPanel();
        setTransactionViewPanel();

        revalidate();
        repaint();
    }











    @Override
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==buttons[0]){          // Update Password

            JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));

            JLabel newPassLabel = new JLabel("New Password:");
            JLabel newConfirmPassLabel = new JLabel("Confirm Password:");

            JPasswordField newPassField = new JPasswordField(15);
            JPasswordField confirmPassField = new JPasswordField(15);

            panel.add(newPassLabel);
            panel.add(newPassField);
            panel.add(newConfirmPassLabel);
            panel.add(confirmPassField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Update Password!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(result==JOptionPane.OK_OPTION){

                String newPass = new String(newPassField.getPassword());
                String newConfirmPass = new String(confirmPassField.getPassword());

                if(newPass.isEmpty() || newConfirmPass.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Password Field Can Not Be Empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                new Update_Password_Service(newPass, newConfirmPass, this, loggedInUser);

            }

        }

        else if(e.getSource()==buttons[1]){     // Logout
            new Global_Functions().logout(this, loggedInUser);
            return;
        }

        else if(e.getSource()==buttons[2]){     // Exit
            new Global_Functions().exitApp(this);
            return;
        }

    }

    public void mouseClicked(MouseEvent e){

        JTable clickedTable = (JTable) e.getSource();

        int row = clickedTable.rowAtPoint(e.getPoint());
        int column = clickedTable.columnAtPoint(e.getPoint());

        if(row==-1){
            return;
        }

        if(clickedTable==marketTable && column==3){

            String symbol = (String) clickedTable.getValueAt(row, 0);
            String company = (String) clickedTable.getValueAt(row, 1);
            String tempPrice = (String) clickedTable.getValueAt(row, 2);
            double price = Double.parseDouble(tempPrice.replace("$", ""));
            Stock stock = new Stock(symbol, company, price);

            JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
            JLabel amountLabel = new JLabel("Share Amount:");
            JTextField amountField = new JTextField();

            panel.add(amountLabel);
            panel.add(amountField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Shares to Buy", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(result==JOptionPane.OK_OPTION){

                try{

                    int amount = Integer.parseInt(amountField.getText());

                    if(amount <= 0){
                        JOptionPane.showMessageDialog(this, "Share Amount Must Be Greater Than 0", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Stock_Buy_Service buyService = new Stock_Buy_Service(stock, loggedInUser, amount, this);

                    boolean success = buyService.purchaseStock();

                    if(success){
                        refreshScreen();
                    }

                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Amount Must Be In Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }

        }

        if(clickedTable==portfolioTable && column==4){

            String symbol = (String) clickedTable.getValueAt(row, 0);
            String tempSharesOwned = (String) clickedTable.getValueAt(row, 1).toString();
            int sharesOwned = Integer.parseInt(tempSharesOwned.trim());
            String tempPrice = (String) clickedTable.getValueAt(row, 2);
            double price = Double.parseDouble(tempPrice.replace("$", ""));
            Stock stock = new Stock(symbol, symbol, price);

            JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
            JLabel amountLabel = new JLabel("Share Amount:");
            JTextField amountField = new JTextField();

            panel.add(amountLabel);
            panel.add(amountField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Shares to Sell", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if(result==JOptionPane.OK_OPTION){

                try{

                    int amount = Integer.parseInt(amountField.getText());

                    if(amount <= 0){
                        JOptionPane.showMessageDialog(this,"Share Amount Must Be Greater Than 0","Invalid Amount",JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if(amount > sharesOwned){
                        JOptionPane.showMessageDialog(this, "You Have Not Enough Shares To Sell", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Stock_Sell_Service sellService = new Stock_Sell_Service(stock, loggedInUser, amount, this);

                    boolean success = sellService.sellStock();

                    if(success){
                        refreshScreen();
                    }

                } catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "Amount Must Be In Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }

        }

    }

    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    public static void main(String[] args){
        User mockUser = new User("mock@gmail.com", "Mock@111", "Mr. Mock", "1000");
        new Main_Screen(mockUser);
    }

}
