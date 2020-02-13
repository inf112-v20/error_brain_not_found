package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;


public class Board {

    private TiledMap map;

    private TiledMapTileLayer startLayer;
    private TiledMapTileLayer flagLayer;
    private TiledMapTileLayer holeLayer;
    private TiledMapTileLayer wallLayer;
    private TiledMapTileLayer groundLayer;

    private ArrayList<Player> startPos; // Need to be changed

    private OrthogonalTiledMapRenderer mapRenderer;


    public void createBoard(String mapPath){
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters();
        parameters.flipY = true;

        this.map = new TmxMapLoader().load(mapPath, parameters);
        this.startLayer = (TiledMapTileLayer) map.getLayers().get("Start");
        this.flagLayer =(TiledMapTileLayer) map.getLayers().get("Flag");
        this.holeLayer =(TiledMapTileLayer) map.getLayers().get("Hole");
        this.wallLayer =(TiledMapTileLayer) map.getLayers().get("Wall");
        this.groundLayer = (TiledMapTileLayer) map.getLayers().get("Ground");

        mapRenderer = new OrthogonalTiledMapRenderer(map);

    }
    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void findStartLayer(){
        for (int x = 0; x < startLayer.getWidth(); x++) {
            for (int y = 0; y < startLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = startLayer.getCell(x,y);
                if (cell != null){
                    startPos.add(new Player(new Position(x,y)));
                }
            }
        }
    }

    public TiledMapTileLayer getStartLayer() {
        return startLayer;
    }

    public TiledMapTileLayer getFlagLayer() {
        return flagLayer;
    }

    public TiledMapTileLayer getHoleLayer() {
        return holeLayer;
    }

    public TiledMapTileLayer getWallLayer() {
        return wallLayer;
    }

    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }
}