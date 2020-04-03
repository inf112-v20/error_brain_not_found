package inf112.skeleton.app;

import java.net.Socket;

public class GameClient extends Thread {

    private Socket socket;

    public GameClient(Socket socket) {
        this.socket = socket;
    }

    public void run() {

    }

}
