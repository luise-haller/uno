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
    public void remove(ServerThread st) {
        threads.remove(st);
    }
    public boolean isFirstClient(ServerThread st) {
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i) == st) {
                return i == 0;
            }
        }
        return false;
    }

    public void regulate(boolean reversed) {
        if(!reversed) {
            for (int i = 0; i<threads.size();i++) {
                //Apply isFirstClient() logic here
            }
        } else {
            for (int i = threads.size()-1; i>=0;i--) {
                //reverse the logic above when turns are reversed
            }
        }
        
    }
    public Card drawCardFromDeck() {
        Card draw = game.getDeckPile().get(0);
        game.getDeckPile().remove(0);
        return draw;
    }

    public void updateCardInPlay(String newCard) {
        broadcast("Top" + newCard);
        //broadcast the new card to ALL clients
    }

    public DLList<Card> getDeck() {
        return game.getDeckPile();
    }
    public void startGame() {
        System.out.println("Thread size = " + threads.size());
        // later fix: 4 players
        if (threads.size() == 2) {
            // Deal hands to players
            for (int i = 0; i < threads.size(); i++) {
                ServerThread thread = threads.get(i);
                DLList<Card> hand = game.dealHand();
                thread.send("Hand" + hand.toString());    
            }
            DLList<Card> deck = game.getDeckPile();
            Card topCard = deck.get(0);
            broadcast("Top" + topCard.toString());
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
