package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.gameObjects.Flag;

import java.util.ArrayList;

public class Board extends BoardLayers {

    private final ArrayList<Flag> flags;
    private ArrayList<Player> players;

    public Board() {
        this("assets/testMap.tmx");
    }

    public Board(String mapPath) {
        super(mapPath);

        this.players = new ArrayList<>();
        this.flags = new ArrayList<>();
        findFlags();
        addPlayersToStartPositions(2);
    }

    /**
     * Finds the where the flags are on the board and makes {@link Flag} objects.
     */
    public void findFlags(){
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = flagLayer.getCell(x, y);
                int ID = cell.getTile().getId();
                if (ID == 55){
                    flags.add(new Flag(1, x, y));
                } else if (ID == 63){
                    flags.add(new Flag(2, x, y));
                } else if (ID == 71){
                    flags.add(new Flag(3, x, y));
                } //TODO: Find the last ID to the 4th flag

            }
        }
    }

    /**
     * Check all cells on map for start positions and add a new player to that
     * position based on number of players
     *
     * @param numPlayers number of robots playing, between 1-8
     */
    public void addPlayersToStartPositions(int numPlayers) {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
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
     * @param x coordinate
     * @param y coordinate
     */
    public void addPlayer(int x, int y) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTileSet tileSet = tiledMap.getTileSets().getTileSet("player");
        cell.setTile(tileSet.getTile(137));
        playerLayer.setCell(x, y, cell);
        players.add(new Player(new Vector2(x, y)));
    }

    public boolean canGo(Player player) {
        Vector2 position = player.getPosition();
        Direction direction = player.getDirection();

        if (direction == Direction.EAST) {
            return position.x < boardWidth - 1;
        } else if (direction == Direction.WEST) {
            return position.x > 0;
        } else if (direction == Direction.NORTH) {
            return position.y < boardHeight - 1;
        } else if (direction == Direction.SOUTH) {
            return position.y > 0;
        }
        return false;
    }

    /**
     * Moves the player from current position one tile in the direction it's facing.
     * Removes the cell its currently on and moves the content of that cell to the cell it moves to.
     * Also updates the fields in player
     *
     * @param player that is suppose to move
     */
    public void movePlayer(Player player) {
        if (!canGo(player)) {
            return;
        }
        Vector2 playerPosition = player.getPosition();
        Direction playerDirection = player.getDirection();

        TiledMapTileLayer.Cell cell = playerLayer.getCell((int) playerPosition.x, (int) playerPosition.y);

        playerLayer.setCell((int) playerPosition.x, (int) playerPosition.y, new TiledMapTileLayer.Cell());
        switch (playerDirection) {
            case NORTH:
                playerPosition.set(playerPosition.x, playerPosition.y + 1);
                break;
            case EAST:
                playerPosition.set(playerPosition.x + 1, playerPosition.y);
                break;
            case WEST:
                playerPosition.set(playerPosition.x - 1, playerPosition.y);
                break;
            case SOUTH:
                playerPosition.set(playerPosition.x, playerPosition.y - 1);
                break;
            default:
                return;
        }

        player.setPosition(playerPosition);
        playerLayer.setCell((int) playerPosition.x, (int) playerPosition.y, cell);
    }


    /**
     * @return list of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @return {@link TiledMapTileLayer} of player layer
     */
    public TiledMapTileLayer getPlayerLayer() {
        return playerLayer;
    }

    /**
     * @return {@link TiledMapTileLayer} of flag layer
     */
    public TiledMapTileLayer getFlagLayer() {
        return flagLayer;
    }

    /**
     * @return {@link TiledMapTileLayer} of laser layer
     */
    public TiledMapTileLayer getLaserLayer() {
        return laserLayer;
    }

    /**
     * @return {@link TiledMapTileLayer} of wall layer
     */
    public TiledMapTileLayer getWallLayer() {
        return wallLayer;
    }

    /**
     * @return {@link TiledMapTileLayer} of ground layer
     */
    public TiledMapTileLayer getGroundLayer() {
        return groundLayer;
    }

    /**
     * @return width of the board
     */
    public int getWidth() {
        return boardWidth;
    }

    /**
     * @return height of the board
     */
    public int getHeight() {
        return boardHeight;
    }

    @Override
    public TiledMap getMap() {
        return tiledMap;
    }
}