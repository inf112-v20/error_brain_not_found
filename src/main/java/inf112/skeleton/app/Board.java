package inf112.skeleton.app;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

    private ArrayList<Player> startPos; // Need to be changed

    public Board(String mapPath) {

        this.tiledMap = new TmxMapLoader().load("assets/kart.tmx");

        this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        this.flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Flag");
        this.laserLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Laser");
        this.wallLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
        this.groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        MapProperties properties = tiledMap.getProperties();
        boardWidth = properties.get("width", Integer.class);
        boardHeight = properties.get("height", Integer.class);
    }

    public void findStartLayer() {
        for (int x = 0; x < playerLayer.getWidth(); x++) {
            for (int y = 0; y < playerLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = playerLayer.getCell(x,y);
                if (cell != null) {
                    startPos.add(new Player(new Position(x,y)));
                }
            }
        }
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