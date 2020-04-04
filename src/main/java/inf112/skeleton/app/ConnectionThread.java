package inf112.skeleton.app;

/**
 * Make a new thread to make server connect to clients so that game.create()
 * can finish, and the menu-screen is shown.
 * @author  Jenny
 */
public class ConnectionThread extends Thread {

    private int numberOfPlayers;
    private GameServer server;

    public ConnectionThread(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * What the thread is doing when it is started.
     */
    public void run(){
        this.server = new GameServer();
        int numberOfClients = this.numberOfPlayers-1;
        server.connect(9000, numberOfClients);
    }

    /**
     *
     * @return the gameserver handling the connections.
     */
    public GameServer getServer() {
        return server;
    }
}
