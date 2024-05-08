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

    public void startGame() {
        if (threads.size() >= 3 || threads.size() <= 4) {
            //Turn Regulation Code Goes Here
            //Use dealHand() method from Game.java which takes the top 7 cards from the shuffled deck
        }
    }

    public void broadcast(String msg) {
        System.out.println("broadcasting " + msg + " on thread size: " + threads.size());
        for (int i = 0; i<threads.size();i++) {
            threads.get(i).send(msg);
        }
    }
}
