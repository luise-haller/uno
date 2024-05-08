import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private Manager m;
    private PrintWriter out = null;


    
    public void send(String message) {
        out.println(message);
    }

    public void run() {
        
        try {
            // System.out.println(Thread.currentThread().getName() + ": connection
            // opened.");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            

            while (true) {
                
            }
        } catch (IOException ex) {
            System.out.println("Error listening for a connection");
            System.out.println(ex.getMessage());
        }
    }
    
}
