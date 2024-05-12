import javax.swing.JFrame;
import java.io.*;

public class Client {

    public static void main(String args[]) throws IOException{

        JFrame frame = new JFrame("Client");

        ClientScreen sc = new ClientScreen();
        frame.add(sc);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        while (true) {

            boolean gameStarted = sc.getGameStarted();
            if (gameStarted) {
                sc.connect();
            }
            try { // To avoid high CPU usage
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}