import javax.swing.SwingUtilities;

public class Main{

    public Main(){
        SwingUtilities.invokeLater(() -> new ui.Splash_Screen());
    }

    public static void main(String[] args){
        new Main();
    }

}