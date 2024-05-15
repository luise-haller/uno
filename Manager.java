public class Manager {
    private Game game;
    
    private MyArrayList<ServerThread> threads;
    
    public Manager() {
        threads = new MyArrayList<ServerThread>();
        game = new Game();
    }

    public void add(ServerThread st) {
        threads.add(st);
    }

    // Hopefully checks if a given ServerThread instance is the first client
    public boolean isFirstClient(ServerThread st) {
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i) == st) {
                return i == 0;
            }
        }
        return false;
    }

    public void startGame() {
        System.out.println("Thread size = " + threads.size());
        // later fix: max of 4 players
        if (threads.size() >= 1) {
            // broadcast("Starting the Game!");

            // Deal hands to players
            for (int i = 0; i < threads.size(); i++) {
                ServerThread thread = threads.get(i);
                DLList<Card> hand = game.dealHand();

                thread.send("Your hand: " + hand.toString()); 
                
            }

            DLList<Card> deck = game.getDeckPile();
            Card topCard = deck.get(0);
            broadcast(topCard.toString());
        } else {
            System.out.println("Game requires 3 to 4 players to start.");
        }
    }

    public void broadcast(String msg) {
        System.out.println("broadcasting " + msg + " on thread size: " + threads.size());
        for (int i = 0; i<threads.size();i++) {
            threads.get(i).send(msg);
        }
    }
}
