package inf112.skeleton.app;

import java.io.*;
import java.net.Socket;

/**
 * Get continuos updates from server.
 */
public class GameClient extends Thread {

    private Socket clientSocket;

    public GameClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        try {
            // Get incoming messages
            while (true) {
                InputStream input = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                System.out.print(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
