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

        // boolean connected = false;

        // while (!connected) {

        //     boolean gameStarted = sc.getGameStarted();
        //     if (gameStarted) {
        //         sc.connect();
        //         connected = true;
        //     }
        //     try { // To avoid high CPU usage
        //         Thread.sleep(1000); 
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
        while(!sc.getGameStarted()) {
            try {
                Thread.sleep(100); // Adjust sleep interval to avoid busy waiting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sc.connect();
       
    }
}