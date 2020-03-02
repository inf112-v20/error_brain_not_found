package inf112.skeleton.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.TileID;
import inf112.skeleton.app.objects.Flag;

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
     * And puts them in to the flag array.
     */
    public void findFlags() {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = flagLayer.getCell(x, y);
                if (cell != null)  {
                    int ID = cell.getTile().getId();
                    if (ID == 55) {
                        flags.add(new Flag(1, x, y));
                    } else if (ID == 63) {
                        flags.add(new Flag(2, x, y));
                    } else if (ID == 71) {
                        flags.add(new Flag(3, x, y));
                    }
                }
            }
        }
    }

    /**
     * Check all cells on map for start positions got by {@link TileID} and add a new player to that
     * position based on number of players
     *
     * @param numPlayers number of robots playing, between 1-8
     */
    public void addPlayersToStartPositions(int numPlayers) {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();
                if (ID == TileID.STARTPOS1.getId()) {
                    addPlayer(x, y, 1);
                } else if (ID == TileID.STARTPOS2.getId() && numPlayers > 1) {
                    addPlayer(x, y, 2);
                } else if (ID == TileID.STARTPOS3.getId() && numPlayers > 2) {
                    addPlayer(x, y, 3);
                } else if (ID == TileID.STARTPOS4.getId() && numPlayers > 3) {
                    addPlayer(x, y, 4);
                } else if (ID == TileID.STARTPOS5.getId() && numPlayers > 4) {
                    addPlayer(x, y, 5);
                } else if (ID == TileID.STARTPOS6.getId() && numPlayers > 5) {
                    addPlayer(x, y, 6);
                } else if (ID == TileID.STARTPOS7.getId() && numPlayers > 6) {
                    addPlayer(x, y, 7);
                } else if (ID == TileID.STARTPOS8.getId() && numPlayers > 7) {
                    addPlayer(x, y, 8);
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
    public void addPlayer(int x, int y, int playerNumber) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        TiledMapTileSet tileSet = tiledMap.getTileSets().getTileSet("player");
        cell.setTile(tileSet.getTile(137));
        playerLayer.setCell(x, y, cell);
        Player player = new Player(new Vector2(x, y), playerNumber);
        players.add(player);
    }

    public Player getPlayer1() {
        for (Player player : players) {
            if (player.getPlayerNr() == 1) {
                return player;
            }
        }
        return players.get(0);
    }

    public boolean canGo(Player player) {
        Vector2 position = player.getPosition();
        Direction direction = player.getDirection();

        shouldPush(position, direction);
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
        Vector2 playerPosition = player.getPosition();
        Direction playerDirection = player.getDirection();

        shouldPush(playerPosition, playerDirection);
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
        updatePlayers();
    }

    /**
     * If player moved outside board method return true
     * @param player current player
     */
    public boolean outsideBoard(Player player) {
        //TODO: Fix constants
        return player.getPosition().x < 0 || player.getPosition().x > 15 || player.getPosition().y < 0 || player.getPosition().y > 11;
    }

    /**
     * Check if the moving player should try to push another player
     * @param position  of the player that wants to move
     * @param direction of the player that wants to move
     */
    private void shouldPush(Vector2 position, Direction direction) {
        for (Player enemyPlayer : players) {
            if (direction == Direction.EAST && enemyPlayer.getPosition().x == position.x + 1 && enemyPlayer.getPosition().y == position.y) {
                pushPlayer(enemyPlayer, direction);
            } else if (direction == Direction.WEST && enemyPlayer.getPosition().x == position.x - 1 && enemyPlayer.getPosition().y == position.y) {
                pushPlayer(enemyPlayer, direction);
            } else if (direction == Direction.NORTH && enemyPlayer.getPosition().x == position.x && enemyPlayer.getPosition().y == position.y + 1) {
                pushPlayer(enemyPlayer, direction);
            } else if (direction == Direction.SOUTH && enemyPlayer.getPosition().x == position.x && enemyPlayer.getPosition().y == position.y - 1) {
                pushPlayer(enemyPlayer, direction);
            }
        }
    }

    /**
     * @param enemyPlayer enemy player that should be pushed
     * @param direction direction to push enemy player in
     */
    private void pushPlayer(Player enemyPlayer, Direction direction) {
        switch (direction) {
            case SOUTH:
                enemyPlayer.setPosition(new Vector2(enemyPlayer.getPosition().x, enemyPlayer.getPosition().y - 1));
                break;
            case WEST:
                enemyPlayer.setPosition(new Vector2(enemyPlayer.getPosition().x - 1, enemyPlayer.getPosition().y));
                break;
            case EAST:
                enemyPlayer.setPosition(new Vector2(enemyPlayer.getPosition().x + 1, enemyPlayer.getPosition().y));
                break;
            case NORTH:
                enemyPlayer.setPosition(new Vector2(enemyPlayer.getPosition().x, enemyPlayer.getPosition().y + 1));
                break;
            default:
                break;
        }
    }

    //TODO: BUG when you are crashing with a player in the corner. Ist possible to only update the specific tile(?).
    public void updatePlayers() {
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                playerLayer.setCell(x, y, null);
            }
        }
        for (Player player : players) {
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            TiledMapTileSet tileSet = tiledMap.getTileSets().getTileSet("player");
            cell.setTile(tileSet.getTile(137));
            if (outsideBoard(player)) {
                respawn(player);
            }
            playerLayer.setCell((int) player.getPosition().x, (int) player.getPosition().y, cell);
        }
    }

    /**
     * Places a player in backup position when player is outside of board.
     * @param player
     */
    private void respawn(Player player) {
        player.setPosition(new Vector2(player.getBackupPosition().x, player.getBackupPosition().y));
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