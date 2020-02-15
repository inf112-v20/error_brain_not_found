package inf112.skeleton.app;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

public class Board {

    private TiledMap tiledMap;

    private TiledMapTileLayer playerLayer;
    private TiledMapTileLayer flagLayer;
    private TiledMapTileLayer wallLayer;
    private TiledMapTileLayer laserLayer;
    private TiledMapTileLayer groundLayer;

    private int boardWidth;
    private int boardHeight;

    private ArrayList<Player> players; // Need to be changed

    public Board(String mapPath) {

        this.players = new ArrayList<>();

        this.tiledMap = new TmxMapLoader().load(mapPath);

        this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        this.flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Flag");
        this.laserLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Laser");
        this.wallLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
        this.groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        MapProperties properties = tiledMap.getProperties();
        boardWidth = properties.get("width", Integer.class);
        boardHeight = properties.get("height", Integer.class);

        addPlayersToStartPositions(2);
    }

    /**
     * Check all cells on map for start positions and add a new player to that
     * position based on number of players
     *
     * @param numPlayers number of robots playing
     */
    public void addPlayersToStartPositions(int numPlayers) {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x,y);
                int ID = cell.getTile().getId();
                if (ID == 121) {
                    addPlayer(x, y);
                } else if (ID == 122 && numPlayers > 1) {
                    addPlayer(x, y);
                } else if (ID == 123 && numPlayers > 2) {
                    addPlayer(x, y);
                } else if (ID == 124 && numPlayers > 3) {
                    addPlayer(x, y);
                } else if (ID == 129 && numPlayers > 4) {
                    addPlayer(x, y);
                } else if (ID == 130 && numPlayers > 5) {
                    addPlayer(x, y);
                } else if (ID == 131 && numPlayers > 6) {
                    addPlayer(x, y);
                } else if (ID == 132 && numPlayers > 7) {
                    addPlayer(x, y);
                }
            }
        }
    }

    /**
     * Add a player to the player layer in coordinate (x, y) and
     * add that player to the list of players
     *
     * @param x x coordinate
     * @param y y coordinte
     */
    public void addPlayer(int x, int y) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTileSet tileSet = tiledMap.getTileSets().getTileSet("player");
        cell.setTile(tileSet.getTile(137));
        playerLayer.setCell(x, y, cell);
        players.add(new Player(new Position(x,y)));
    }

    /**
     * @return list of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }


    public TiledMapTileLayer getPlayerLayer() {
        return playerLayer;
    }

    public TiledMapTileLayer getFlagLayer() {
        return flagLayer;
    }

    public TiledMapTileLayer getLaserLayer() {
        return laserLayer;
    }

    public TiledMapTileLayer getWallLayer() {
        return wallLayer;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    public TiledMap getMap() { return tiledMap; }

    public int getWidth() { return boardWidth; }

    public int getHeight() { return boardHeight; }
}