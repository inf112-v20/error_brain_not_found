package inf112.skeleton.app.board;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.enums.TileID;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.Flag;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.RotatePad;

import java.util.ArrayList;

public abstract class BoardLayers {

    protected final TiledMap tiledMap;

    protected final TiledMapTileLayer playerLayer;
    protected final TiledMapTileLayer flagLayer;
    protected final TiledMapTileLayer wallLayer;
    protected final TiledMapTileLayer laserLayer;
    protected final TiledMapTileLayer groundLayer;

    protected final ArrayList<Laser> lasers;
    protected final ArrayList<Flag> flags;
    protected final ArrayList<RotatePad> rotatePads;
    protected final ArrayList<Vector2> holes;
    protected final ArrayList<Belt> belts;
    protected final ArrayList<Belt> expressBelts;
    protected final ArrayList<Vector2> repairTiles;

    protected final int boardWidth;
    protected final int boardHeight;

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

        this.holes = new ArrayList<>();
        this.rotatePads = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.lasers = new ArrayList<>();
        this.belts = new ArrayList<>();
        this.expressBelts = new ArrayList<>();
        this.repairTiles = new ArrayList<>();

        findFlags();
        findBelts();
        findRotatePads();
        findExpressBelts();
        findHoles();
        findLasers();
        findRepairs();
    }

    public void findBelts() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();

                if (ID == TileID.EAST_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                }
            }
        }
    }

    public void findExpressBelts() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();

                if (ID == TileID.EAST_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, new Vector2(x, y)));
                }
            }
        }
    }

    public void findHoles() {
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
                }
            }
        }
    }

    public void findRotatePads() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();

                if (ID == TileID.ROTATE_PAD_LEFT.getId()) {
                    rotatePads.add(new RotatePad(Rotate.LEFT, new Vector2(x, y)));
                } else if (ID == TileID.ROTATE_PAD_RIGHT.getId()) {
                    rotatePads.add(new RotatePad(Rotate.RIGHT, new Vector2(x, y)));
                }
            }
        }
    }

    /**
     * Finds the where the flags are on the board, makes {@link Flag} objects
     * and puts them in to the flag array.
     */
    public void findFlags() {
        for (int x = 0; x < flagLayer.getWidth(); x++) {
            for (int y = 0; y < flagLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = flagLayer.getCell(x, y);
                if (cell != null) {
                    int ID = cell.getTile().getId();
                    if (ID == TileID.FLAG_1.getId()) {
                        flags.add(new Flag(1, x, y));
                    } else if (ID == TileID.FLAG_2.getId()) {
                        flags.add(new Flag(2, x, y));
                    } else if (ID == TileID.FLAG_3.getId()) {
                        flags.add(new Flag(3, x, y));
                    } else if (ID == TileID.FLAG_4.getId()) {
                        flags.add(new Flag(4, x, y));
                    }
                }
            }
        }
    }

    public void findLasers() {
        for (int x = 0; x < wallLayer.getWidth(); x++) {
            for (int y = 0; y < wallLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
                if (cell != null) {
                    int ID = cell.getTile().getId();
                    if (ID == TileID.EAST_LASER_WALL.getId()) {
                        lasers.add(new Laser(x, y, Direction.WEST));
                    } else if (ID == TileID.WEST_LASER_WALL.getId()) {
                        lasers.add(new Laser(x, y, Direction.EAST));
                    } else if (ID == TileID.NORTH_LASER_WALL.getId()) {
                        lasers.add(new Laser(x, y, Direction.SOUTH));
                    } else if (ID == TileID.SOUTH_LASER_WALL.getId()) {
                        lasers.add(new Laser(x, y, Direction.NORTH));
                    }
                }
            }
        }
    }

    /**
     * Finds all the boards repairs tiles.
     */
    public void findRepairs() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {

                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                if(cell != null){
                    int ID =  cell.getTile().getId();
                    if (ID == TileID.WRENCH.getId()){
                        repairTiles.add(new Vector2(x, y));
                    } else if (ID == TileID.DOUBLE_WRENCH.getId()){
                        repairTiles.add(new Vector2(x, y));
                    }
                }
            }
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * @return {@link TiledMapTileLayer} of laser layer
     */
    public TiledMapTileLayer getLaserLayer() {
        return laserLayer;
    }

    public ArrayList<Laser> getLasers() {
        return lasers;
    }

    public ArrayList<Flag> getFlags() {
        return flags;
    }

    public ArrayList<RotatePad> getRotatePads() {
        return rotatePads;
    }

    public ArrayList<Vector2> getHoles() {
        return holes;
    }

    public ArrayList<Belt> getBelts() {
        return belts;
    }

    public ArrayList<Belt> getExpressBelts() {
        return expressBelts;
    }

    /**
     * Get the {@link ArrayList repairTiles} arraylist.
     * @return the arraylist that contains the location of the repair tiles.
     */
    public ArrayList<Vector2> getRepairTiles() {
        return repairTiles;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }
}
