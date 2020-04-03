package inf112.skeleton.app;


public class ConnectionThread extends Thread {

    private int numberOfPlayers;
    private GameServer server;

    public ConnectionThread(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void run(){
        this.server = new GameServer(this.numberOfPlayers-1);
        server.connect(9000);
        //server.sendToAll(numberOfPlayers+"");
    }

    /**
     * Tell server to close all connections
     */
    public void closeAll() {
        this.server.closeAll();
    }


}
