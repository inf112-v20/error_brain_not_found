package inf112.skeleton.app;

import java.io.*;
import java.net.Socket;

/**
 * Own thread for a client so client can get continous updates from server.
 */
public class GameClientThread extends Thread {

    private Socket clientSocket;

    public GameClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * What the thread is doing when it is started.
     */
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
