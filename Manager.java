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
}
