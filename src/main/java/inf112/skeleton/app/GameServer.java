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
    private ArrayList<Socket> sockets;

    public GameServer(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void connect(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.sockets = new ArrayList<>();
            // Connect to several clients
            int clients = 0;
            while (clients < numberOfClients) {
                Socket socket = serverSocket.accept();
                sockets.add(socket);
                clients++;
            }
        } catch (IOException ex) {
            System.out.println("Could not create server.");
        }
    }

    public Socket getPlayerSocket(int playerNumber) {
        return sockets.get(playerNumber-1);
    }

    public String getMessage(int playerNumber) {
        try {
            InputStream input = sockets.get(playerNumber-1).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void broadcastMessage(String message) {
        for (Socket socket : sockets) {
            try {
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
