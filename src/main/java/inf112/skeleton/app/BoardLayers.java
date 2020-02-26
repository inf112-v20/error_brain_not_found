package inf112.skeleton.app;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;

public abstract class BoardLayers {

    public TiledMap tiledMap;

    public TiledMapTileLayer playerLayer;
    public TiledMapTileLayer flagLayer;
    public TiledMapTileLayer wallLayer;
    public TiledMapTileLayer laserLayer;
    public TiledMapTileLayer groundLayer;

    public int boardWidth;
    public int boardHeight;

    private ArrayList<Player> players;

    public BoardLayers(String mapPath){
        this.tiledMap = new TmxMapLoader().load(mapPath);

        this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        this.flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Flag");
        this.laserLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Laser");
        this.wallLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
        this.groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        MapProperties properties = tiledMap.getProperties();
        boardWidth = properties.get("width", Integer.class);
        boardHeight = properties.get("height", Integer.class);

    }


    public abstract TiledMap getMap();
    public abstract TiledMapTileLayer getPlayerLayer();
    public abstract TiledMapTileLayer getFlagLayer();
    public abstract TiledMapTileLayer getLaserLayer();
}
