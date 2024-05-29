import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private Manager manager;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private int id = -1;
    private boolean receivedUsername;


    public ServerThread(Socket clientSocket, Manager manager) {
        this.clientSocket = clientSocket;
        this.manager = manager;
        out = null;
        username = "";
        receivedUsername = false;
        
    }
    
    public void send(String message) {
        out.println(message);
    }
    
    public String getUsername(){
		return username;
	}

    public void setID(int id) {
        this.id = id;
    }
    

    public void run() {
        System.out.println(Thread.currentThread().getName() + ": connection opened.");
    
        String msg = "";
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String userName = "";
            send("ID" + id);
            if (manager.isFirstClient(this)) {
                send("Your Turn");
            }
            while (clientSocket.getInputStream() != null) {
                msg = in.readLine();
                System.out.println("Message received from client = " + msg);
                if(!receivedUsername) {
                    if (msg.startsWith("Username")) {
                        userName = msg.substring(8);
                        receivedUsername = true;
                    }
                }
                processClientMessage(msg);
                
                
            }

        } catch (IOException ex) {
            System.out.println("Error listening for a connection");
            System.out.println(ex.getMessage());    
        }
    }


    private void processClientMessage(String msg) {
        if(msg.equals("play")) {
            manager.startGame();
        } else if(msg.startsWith("Done")) {
            // System.out.println("Next Turn");
            int clientThatJustWent = Integer.parseInt(msg.substring(4));
            System.out.println("ClientThatJustWent: " + clientThatJustWent);
            manager.regulate(clientThatJustWent);
        } else if(msg.equals("DrawCard")) {
            send("FromDraw" + manager.drawCardFromDeck().toString());
        } else if(msg.startsWith("Update")) {
            manager.updateCardInPlay(msg.substring(6));
        }
    }
    
}