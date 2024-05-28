import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private Manager manager;
    private PrintWriter out;
    private String username;


    public ServerThread(Socket clientSocket, Manager manager) {
        this.clientSocket = clientSocket;
        this.manager = manager;
        out = null;
        username = "";
    }
    
    public void send(String message) {
        out.println(message);
    }
    
    public String getUsername(){
		return username;
	}

    public void run() {
        System.out.println(Thread.currentThread().getName() + ": connection opened.");
        

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
            String msg = in.readLine();
            System.out.println("Msg received from client: " + msg);
            if (msg.startsWith("Username")) {
                username = msg.substring(8);
                send("Username" + username.toString());
                if (manager.isFirstClient(this)) {
                    send("Your Turn");
                }
            } else if(msg.equals("DrawCard")) { // receive update for deck from client screen
                send("CardDrawn" + manager.drawCardFromDeck());
            } else if(msg.startsWith("ChangeCard")) { //Check if msgs has like change card in play or smth like that call a manager method and broadcast to all clients
                //Test
                System.out.println("New Card In Play = " + msg.substring(10));
                //Actual Code:
                //manager.updateCardInPlay(msg.subString(10));

            }
            //If message = reverse - > manager.regulate(true) vice versa manager.regulate(false);


            
            manager.startGame();
            // send("DeckFromGame" + manager.getDeck());

            // System.out.println("test:" + manager.isFirstClient(this));
            // // Send "Your turn" message to the first client that joins
            

            while (clientSocket.getInputStream() != null) { 
                String msg2 = in.readLine();
                
            }

            out.flush();
            out.close();
            System.out.println(Thread.currentThread().getName() + ": connection closed.");
        } catch (IOException ex) {
            System.out.println("Error listening for a connection");
            System.out.println(ex.getMessage());
        }
    }
    
}
