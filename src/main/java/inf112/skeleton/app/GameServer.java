package inf112.skeleton.app;

import com.badlogic.gdx.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server for handling connection between players
 */
public class GameServer {

    private int numberOfClients;
    private ServerSocket serverSocket;

    public GameServer(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void connect(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            //this.sockets = new ArrayList<>();
            // Connect to several clients
            int connected = 0;
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientThreads(socket).start();
                connected++;
                if (connected == this.numberOfClients) {
                    System.out.print("Connected! :)");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
