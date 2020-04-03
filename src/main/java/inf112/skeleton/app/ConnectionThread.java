package inf112.skeleton.app;


public class ConnectionThread extends Thread {

    private int numberOfPlayers;

    public ConnectionThread(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    @Override
    public void run(){
        GameServer server = new GameServer(this.numberOfPlayers-1);
        server.connect(9000);
        server.sendToAll(numberOfPlayers+"");
    }


}
