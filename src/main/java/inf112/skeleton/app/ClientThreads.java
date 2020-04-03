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
    private String name;

    public ClientThreads(Socket client, String name) {
        this.client = client;
        this.name = name;
    }

    @Override
    public void run() {
        try {

            // Let the player know what the playernumber is
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(name);

            // Get incoming messages
            InputStream input = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            System.out.print(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
