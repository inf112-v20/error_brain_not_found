package inf112.skeleton.app;


import com.badlogic.gdx.Game;

import java.util.Scanner;

public class ConnectionThread extends Thread {

    public void run(){
        System.out.println("How many players?");
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = scanner.nextInt();
        GameServer server = new GameServer(numberOfPlayers-1);

        server.connect(9000);
    }

}
