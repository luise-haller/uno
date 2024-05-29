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
        if (threads.size() >= 1) {
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


    // idk if what i wrote here makes sense LOL - > created new one below!
    // public void regulate(boolean reversed) {
    //     if(!reversed) {
    //         for (int i = 0; i<threads.size();i++) {
    //             if (isFirstClient(threads.get(i))) {
    //                 threads.get(i).send("YourTurn");
    //             } else {
    //                 threads.get(i).send("NotYourTurn");
    //             }
    //         }
    //     } else {
    //         for (int i = threads.size()-1; i>=0;i--) {
    //             if (isFirstClient(threads.get(i))) {
    //                 threads.get(i).send("YourTurn");
    //             } else {
    //                 threads.get(i).send("NotYourTurn");
    //             }
    //         }
    //     }
        
    // }

    

    public void regulate(boolean reversed) {
        // if reversed is true - > set currentIndex to last index
        // if reversed is false - > set currentIndex to 0 (first index)
        int currentIndex = reversed ? threads.size() - 1 : 0;
        // if reversed - > step is -1
        // if not reversed - > step is 1
        int step = reversed ? -1 : 1;

        // for each thread, check if index matches currentIndex
        // if it matches - > send turn msg (otherwise send not turn msg)
        for (int i = 0; i < threads.size(); i++) {
            if (i == currentIndex) {
                threads.get(i).send("YourTurn");
            } else {
                threads.get(i).send("NotYourTurn");
            }
        }
        // update currentIndex for the next turn
        // add step to currentIndex (% ensures that index wraps around when it reaches end of list)
        currentIndex = (currentIndex + step + threads.size()) % threads.size();
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
}
