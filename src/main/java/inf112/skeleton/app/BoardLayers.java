package inf112.skeleton.app;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.enums.TileID;
import inf112.skeleton.app.objects.RotatePad;

import java.util.ArrayList;

public abstract class BoardLayers {

    public TiledMap tiledMap;

    public TiledMapTileLayer playerLayer;
    public TiledMapTileLayer flagLayer;
    public TiledMapTileLayer wallLayer;
    public TiledMapTileLayer laserLayer;
    public TiledMapTileLayer groundLayer;

    public final ArrayList<RotatePad> rotatePads;
    public final ArrayList<Vector2> holes;

    public int boardWidth;
    public int boardHeight;

    public BoardLayers(String mapPath) {
        this.tiledMap = new TmxMapLoader().load(mapPath);

        this.playerLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Player");
        this.flagLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Flag");
        this.laserLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Laser");
        this.wallLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Wall");
        this.groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Ground");

        MapProperties properties = tiledMap.getProperties();
        boardWidth = properties.get("width", Integer.class);
        boardHeight = properties.get("height", Integer.class);

        this.holes = new ArrayList<Vector2>();
        this.rotatePads = new ArrayList<RotatePad>();
    }


    /**
     * Finds where there are {@link RotatePad} and Holes on the map. In the case of RotatePad adds a RotatePad object to
     * the rotatePad list, in the case of a hole adds a {@link Vector2} to the holes list.
     */
    public void findRotatePadsAndHoles() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();
                if (ID == TileID.NORMAL_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORMAL_HOLE2.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORTHWEST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORTH_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORTHEAST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.EAST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORTH_EAST_SOUTH_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.WEST_EAST_SOUTH_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.SOUTHWEST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.SOUTH_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.SOUTHEAST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.WEST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORTH_WEST_SOUTH_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.NORTH_WEST_EAST_HOLE.getId()) {
                    holes.add(new Vector2(x, y));
                } else if (ID == TileID.ROTATE_PAD_LEFT.getId()) {
                    rotatePads.add(new RotatePad(Rotate.LEFT, new Vector2(x, y)));
                } else if (ID == TileID.ROTATE_PAD_RIGHT.getId()) {
                    rotatePads.add(new RotatePad(Rotate.RIGHT, new Vector2(x, y)));
                }
            }
        }
    }

    public abstract TiledMap getMap();

    public abstract TiledMapTileLayer getPlayerLayer();

    public abstract TiledMapTileLayer getFlagLayer();

    public abstract TiledMapTileLayer getLaserLayer();
}
