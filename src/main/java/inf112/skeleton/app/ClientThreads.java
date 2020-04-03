package inf112.skeleton.app;

import com.badlogic.gdx.net.SocketHints;
import org.lwjgl.Sys;

import java.io.*;
import java.net.Socket;

/**
 * Make a thread for each client connecting
 */
public class ClientThreads extends Thread {

    private Socket client;

    public ClientThreads(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            // Get incoming messages
            InputStream input = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            System.out.print(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
