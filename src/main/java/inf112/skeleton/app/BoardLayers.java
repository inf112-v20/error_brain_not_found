package inf112.skeleton.app;

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

    public final TiledMap tiledMap;

    public final TiledMapTileLayer playerLayer;
    public final TiledMapTileLayer flagLayer;
    public final TiledMapTileLayer wallLayer;
    public final TiledMapTileLayer laserLayer;
    public final TiledMapTileLayer groundLayer;

    public final ArrayList<Laser> lasers;
    public final ArrayList<Flag> flags;
    public final ArrayList<RotatePad> rotatePads;
    public final ArrayList<Vector2> holes;
    public final ArrayList<Belt> belts;
    public final ArrayList<Belt> expressBelts;

    public final int boardWidth;
    public final int boardHeight;

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

        findFlags();
        findRotatePadsAndHolesAndBelts();
        findLasers();
    }


    /**
     * Finds where there are {@link RotatePad} and Holes on the map. In the case of RotatePad adds a RotatePad object to
     * the rotatePad list, in the case of a hole adds a {@link Vector2} to the holes list.
     * <p>
     * For better runtime the method finds the {@link RotatePad}'s, holes and {@link Belt}'s these are all on the same
     * layer. Could have been three different methods but then the run time would have been three times higher.
     */
    public void findRotatePadsAndHolesAndBelts() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();
                // Add holes
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
                    // Add Rotate Pads
                } else if (ID == TileID.ROTATE_PAD_LEFT.getId()) {
                    rotatePads.add(new RotatePad(Rotate.LEFT, new Vector2(x, y)));
                } else if (ID == TileID.ROTATE_PAD_RIGHT.getId()) {
                    rotatePads.add(new RotatePad(Rotate.RIGHT, new Vector2(x, y)));
                    // Add normal Belts
                } else if (ID == TileID.EAST_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.NONE, 1, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.NONE, 1, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.NONE, 1, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.NONE, 1, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_NORTH_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_SOUTH_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_EAST_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.LEFT, 1, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.RIGHT, 1, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_WEST_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.LEFT, 1, new Vector2(x, y)));
                    // Add express Belts
                } else if (ID == TileID.EAST_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.EAST_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.NONE, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, Rotate.NONE, 2, new Vector2(x, y)));
                } else if (ID == TileID.NORTH_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.NONE, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, Rotate.NONE, 2, new Vector2(x, y)));
                } else if (ID == TileID.WEST_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.NONE, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, Rotate.NONE, 2, new Vector2(x, y)));
                } else if (ID == TileID.SOUTH_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.NONE, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, Rotate.NONE, 2, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_NORTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.NORTH, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.NORTH, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WESTEAST_TO_SOUTH_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.SOUTH, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WESTSOUTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.WESTNORTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_EAST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.EAST, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.EASTSOUTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, Rotate.LEFT, 2, new Vector2(x, y)));
                } else if (ID == TileID.EASTNORTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.RIGHT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, Rotate.RIGHT, 2, new Vector2(x, y)));
                } else if (ID == TileID.NORTHSOUTH_TO_WEST_EXPRESS_BELT.getId()) {
                    belts.add(new Belt(Direction.WEST, Rotate.LEFT, 2, new Vector2(x, y)));
                    expressBelts.add(new Belt(Direction.WEST, Rotate.LEFT, 2, new Vector2(x, y)));
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

    public abstract TiledMap getMap();

    public abstract TiledMapTileLayer getLaserLayer();
}
