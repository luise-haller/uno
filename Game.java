public class Game {

    DLList<Card> deck;

    public Game() {
        deck = new DLList<Card>();
        cardBox();
        shuffle();

    }

    public DLList<Card> dealHand() {
        DLList<Card> hand = new DLList<Card>();
        for (int i = 0; i<7;i++) {
            Card card = deck.remove(0);
            hand.add(card);
        }
        return hand;
    }
    public DLList<Card> getDeckPile() {
        return deck;
    }
    
    public void cardBox() { /* Makes Deck with 112 Cards */
        //Red 0 - 9
        deck.add(new Card("Red", "0"));
        deck.add(new Card("Red", "0"));
        deck.add(new Card("Red", "1"));
        deck.add(new Card("Red", "1"));
        deck.add(new Card("Red", "2"));
        deck.add(new Card("Red", "2"));
        deck.add(new Card("Red", "3"));
        deck.add(new Card("Red", "3"));
        deck.add(new Card("Red", "4"));
        deck.add(new Card("Red", "4"));
        deck.add(new Card("Red", "5"));
        deck.add(new Card("Red", "5"));
        deck.add(new Card("Red", "6"));
        deck.add(new Card("Red", "6"));
        deck.add(new Card("Red", "7"));
        deck.add(new Card("Red", "7"));
        deck.add(new Card("Red", "8"));
        deck.add(new Card("Red", "8"));
        deck.add(new Card("Red", "9"));
        deck.add(new Card("Red", "9")); 
        //Red 6 Action Cards
        deck.add(new Card("Red", "Reverse"));
        deck.add(new Card("Red", "Skip"));
        deck.add(new Card("Red", "DrawTwo"));
        deck.add(new Card("Red", "Reverse"));
        deck.add(new Card("Red", "Skip"));
        deck.add(new Card("Red", "DrawTwo"));

        //Green 0-9
        deck.add(new Card("Green", "0"));
        deck.add(new Card("Green", "0"));
        deck.add(new Card("Green", "1"));
        deck.add(new Card("Green", "1"));
        deck.add(new Card("Green", "2"));
        deck.add(new Card("Green", "2"));
        deck.add(new Card("Green", "3"));
        deck.add(new Card("Green", "3"));
        deck.add(new Card("Green", "4"));
        deck.add(new Card("Green", "4"));
        deck.add(new Card("Green", "5"));
        deck.add(new Card("Green", "5"));
        deck.add(new Card("Green", "6"));
        deck.add(new Card("Green", "6"));
        deck.add(new Card("Green", "7"));
        deck.add(new Card("Green", "7"));
        deck.add(new Card("Green", "8"));
        deck.add(new Card("Green", "8"));
        deck.add(new Card("Green", "9"));
        deck.add(new Card("Green", "9"));
        //Green 6 Action Cards
        deck.add(new Card("Green", "Reverse"));
        deck.add(new Card("Green", "Skip"));
        deck.add(new Card("Green", "DrawTwo"));
        deck.add(new Card("Green", "Reverse"));
        deck.add(new Card("Green", "Skip"));
        deck.add(new Card("Green", "DrawTwo"));

        //Blue 0 - 9
        deck.add(new Card("Blue", "0"));
        deck.add(new Card("Blue", "0"));
        deck.add(new Card("Blue", "1"));
        deck.add(new Card("Blue", "1"));
        deck.add(new Card("Blue", "2"));
        deck.add(new Card("Blue", "2"));
        deck.add(new Card("Blue", "3"));
        deck.add(new Card("Blue", "3"));
        deck.add(new Card("Blue", "4"));
        deck.add(new Card("Blue", "4"));
        deck.add(new Card("Blue", "5"));
        deck.add(new Card("Blue", "5"));
        deck.add(new Card("Blue", "6"));
        deck.add(new Card("Blue", "6"));
        deck.add(new Card("Blue", "7"));
        deck.add(new Card("Blue", "7"));
        deck.add(new Card("Blue", "8"));
        deck.add(new Card("Blue", "8"));
        deck.add(new Card("Blue", "9"));
        deck.add(new Card("Blue", "9"));
        //Green 6 Action Cards
        deck.add(new Card("Blue", "Reverse"));
        deck.add(new Card("Blue", "Skip"));
        deck.add(new Card("Blue", "DrawTwo"));
        deck.add(new Card("Blue", "Reverse"));
        deck.add(new Card("Blue", "Skip"));
        deck.add(new Card("Blue", "DrawTwo"));

        //Yellow 0 - 9 
        deck.add(new Card("Yellow", "0"));
        deck.add(new Card("Yellow", "0"));
        deck.add(new Card("Yellow", "1"));
        deck.add(new Card("Yellow", "1"));
        deck.add(new Card("Yellow", "2"));
        deck.add(new Card("Yellow", "2"));
        deck.add(new Card("Yellow", "3"));
        deck.add(new Card("Yellow", "3"));
        deck.add(new Card("Yellow", "4"));
        deck.add(new Card("Yellow", "4"));
        deck.add(new Card("Yellow", "5"));
        deck.add(new Card("Yellow", "5"));
        deck.add(new Card("Yellow", "6"));
        deck.add(new Card("Yellow", "6"));
        deck.add(new Card("Yellow", "7"));
        deck.add(new Card("Yellow", "7"));
        deck.add(new Card("Yellow", "8"));
        deck.add(new Card("Yellow", "8"));
        deck.add(new Card("Yellow", "9"));
        deck.add(new Card("Yellow", "9"));
        //Yellow 6 Action Cards
        deck.add(new Card("Yellow", "Reverse"));
        deck.add(new Card("Yellow", "Skip"));
        deck.add(new Card("Yellow", "DrawTwo"));
        deck.add(new Card("Yellow", "Reverse"));
        deck.add(new Card("Yellow", "Skip"));
        deck.add(new Card("Yellow", "DrawTwo"));

        //8 Wild Cards
        deck.add(new Card("Black", "DrawFourWild"));
        deck.add(new Card("Black", "DrawFourWild"));
        deck.add(new Card("Black", "DrawFourWild"));
        deck.add(new Card("Black", "DrawFourWild"));
        deck.add(new Card("Black", "WildCard"));
        deck.add(new Card("Black", "WildCard"));
        deck.add(new Card("Black", "WildCard"));
        deck.add(new Card("Black", "WildCard"));

    }

    private void shuffle() { /* Shuffles the Deck */
		Card temp;
		Card temp2;
		int s;		
		for(int i=0; i<deck.size(); i++){
			temp = deck.get(i);
			s = (int)(Math.random()*112);
			temp2 = deck.get(s);
			deck.set(i, temp2);
			deck.set(s, temp);		
		}
	}
}
