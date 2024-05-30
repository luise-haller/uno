public class Manager {
    private Game game;
    private MyArrayList<ServerThread> threads;
    
    public Manager() {
        threads = new MyArrayList<ServerThread>();
        game = new Game();
    }

    public void add(ServerThread st) {
        st.setID(threads.size());
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

    public void broadcast(String msg) {
        System.out.println("broadcasting " + msg + " on thread size: " + threads.size());
        for (int i = 0; i<threads.size();i++) {
            threads.get(i).send(msg);
        }
    }

    public DLList<Card> getDeck() {
        return game.getDeckPile();
    }

    public void startGame() {
        System.out.println("startGame() has been called");
        System.out.println("Thread size = " + threads.size());
        // later fix: 4 players
        if (threads.size() == 4) {
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

    public void regulate(int currentIndex) {
        int nextClient = -1;
        if (currentIndex != 3) {
            nextClient = currentIndex + 1;
        } else {
            nextClient = 0;
        }
        broadcast("NextClient" + nextClient);
    }
    public void reverseRegulate(int currentIndex) {
        int nextClient = 4;
        if (currentIndex != 0) {
            nextClient = currentIndex - 1;
        } else {
            nextClient = 3;
        }
        broadcast("NextClientReverse" + nextClient);
    }
    //Called in ServerThread which takes and removes top card from deck and adds to one client's hand
    public Card drawCardFromDeck() {
        Card draw = game.getDeckPile().get(0);
        game.getDeckPile().remove(0);
        return draw;
    }

    //Broadcast the new top card to ALL Clients
    public void updateCardInPlay(String newCard) {
        broadcast("Top" + newCard);
    }

    //Logic for draw 2 action card
    public DLList<Card> draw2() {
        DLList<Card> newCards = new DLList<Card>();
        Card c1 = game.getDeckPile().get(0);
        Card c2 = game.getDeckPile().get(1);
        game.getDeckPile().remove(0);game.getDeckPile().remove(0);
        newCards.add(c1);newCards.add(c2);
        return newCards;
    }
    public DLList<Card> draw4() {
        DLList<Card> newCards = new DLList<Card>();
        Card c1 = game.getDeckPile().get(0);
        Card c2 = game.getDeckPile().get(1);
        Card c3 = game.getDeckPile().get(2);
        Card c4 = game.getDeckPile().get(3);
        game.getDeckPile().remove(0);game.getDeckPile().remove(1);game.getDeckPile().remove(2);game.getDeckPile().remove(3);
        newCards.add(c1); newCards.add(c2); newCards.add(c3); newCards.add(c4);
        return newCards;
    }

}