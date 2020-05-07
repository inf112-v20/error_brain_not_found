package inf112.skeleton.app.lan;

import inf112.skeleton.app.RallyGame;

/**
 * Make a new thread to make server connect to clients so that game.setUo()
 * can finish, and the game-screen is shown.
 */
public class ServerThread extends Thread {

    private int numberOfPlayers;
    private GameServer server;
    private RallyGame game;
    private int portNumber;

    public ServerThread(RallyGame game, int numberOfPlayers, int portNumber) {
        this.numberOfPlayers = numberOfPlayers;
        this.game = game;
        this.portNumber = portNumber;
    }

    /**
     * What the thread is doing when it is started.
     */
    public void run(){
        this.server = new GameServer(game);
        int numberOfClients = this.numberOfPlayers-1;
        server.createServerSocket(portNumber);
        //server.connect(numberOfClients);
        server.connect(7);
    }

    /**
     *
     * @return the gameserver handling the connections.
     */
    public GameServer getServer() {
        return server;
    }
}
