package inf112.skeleton.app.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.TileID;
import inf112.skeleton.app.objects.Flag;
import inf112.skeleton.app.objects.Player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board extends BoardLayers {

    private final ArrayList<Player> players;

    private final Sound scream = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/WilhelmScream.mp3"));
    private final Sound wallImpact = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/ImpactWall.mp3"));


    public Board(String mapPath, int numberOfPlayers) {
        super(mapPath);

        this.players = new ArrayList<>();

        addPlayersToStartPositions(numberOfPlayers);
    }

    private TiledMapTile getRobotTile(Player player) {
        TiledMapTileSet tileSet = this.tiledMap.getTileSets().getTileSet("robots");
        switch (player.getDirection()) {
            case SOUTH:
                return tileSet.getTile(TileID.PLAYER_SOUTH.getId());
            case NORTH:
                return tileSet.getTile(TileID.PLAYER_NORTH.getId());
            case EAST:
                return tileSet.getTile(TileID.PLAYER_EAST.getId());
            case WEST:
                return tileSet.getTile(TileID.PLAYER_WEST.getId());
            default:
                return null;
        }
    }

    /**
     * @param number number of player
     * @return true if player number is valid
     */
    public boolean validPlayerNumber(int number) {
        for (Player player : players) {
            if (number == player.getPlayerNr()) {
                return false;
            }
        }
        return 0 < number && number < 9;
    }

    /**
     * Make new player and add player to game and board
     *
     * @param x            coordinate
     * @param y            coordinate
     * @param playerNumber of player
     */
    public void addPlayer(int x, int y, int playerNumber) {
        if (!validPlayerNumber(playerNumber)) {
            return;
        }
        Player player = new Player(new Vector2(x, y), playerNumber);
        addPlayer(player);
    }

    /**
     * Add a player to the player layer in coordinate (x, y) and
     * add that player to the list of players
     *
     * @param player to add to game and board
     */
    public void addPlayer(Player player) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(getRobotTile(player));
        playerLayer.setCell((int) player.getPosition().x, (int) player.getPosition().y, cell);
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public Vector2 getStartPosition(int number) {
        for (int x = 0; x < groundLayer.getWidth(); x++) {
            for (int y = 0; y < groundLayer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = groundLayer.getCell(x, y);
                int ID = cell.getTile().getId();
                if (number == 1 && ID == TileID.START_POS1.getId()) {
                    return new Vector2(x, y);
                } else if (number == 2 && ID == TileID.START_POS2.getId()) {
                    return new Vector2(x, y);
                } else if (number == 3 && ID == TileID.START_POS3.getId()) {
                    return new Vector2(x, y);
                } else if (number == 4 && ID == TileID.START_POS4.getId()) {
                    return new Vector2(x, y);
                } else if (number == 5 && ID == TileID.START_POS5.getId()) {
                    return new Vector2(x, y);
                } else if (number == 6 && ID == TileID.START_POS6.getId()) {
                    return new Vector2(x, y);
                } else if (number == 7 && ID == TileID.START_POS7.getId()) {
                    return new Vector2(x, y);
                } else if (number == 8 && ID == TileID.START_POS8.getId()) {
                    return new Vector2(x, y);
                }
            }
        }
        return null;
    }

    /**
     * Check all cells on map for start positions with {@link TileID} and add a new player to that
     * position based on number of players
     *
     * @param numPlayers number of robots playing, between 1-8
     */
    public void addPlayersToStartPositions(int numPlayers) {
        if (numPlayers == 0) {
            return;
        }
        for (int playerNr = 1; playerNr <= numPlayers; playerNr++) {
            Vector2 position = getStartPosition(playerNr);
            addPlayer((int) position.x, (int) position.y, playerNr);
        }
    }

    /**
     * Add laser in position in the right direction
     *
     * @param position  to add laser
     * @param direction of laser
     */
    public void addLaser(Vector2 position, Direction direction) {
        TiledMapTileLayer.Cell cell = laserLayer.getCell((int) position.x, (int) position.y);
        TiledMapTileSet tileSet = tiledMap.getTileSets().getTileSet("tiles");
        if (cell == null) {
            cell = new TiledMapTileLayer.Cell();
        }
        if (direction == Direction.NORTH || direction == Direction.SOUTH) {
            if (cell.getTile() == null) {
                cell.setTile(tileSet.getTile(TileID.VERTICAL_LASER.getId()));
            } else if (cell.getTile().getId() == TileID.HORIZONTAL_LASER.getId()) {
                cell.setTile(tileSet.getTile(TileID.CROSSED_LASER.getId()));
            }
        } else {
            if (cell.getTile() == null) {
                cell.setTile(tileSet.getTile(TileID.HORIZONTAL_LASER.getId()));
            } else if (cell.getTile().getId() == TileID.VERTICAL_LASER.getId()) {
                cell.setTile(tileSet.getTile(TileID.CROSSED_LASER.getId()));
            }
        }
        laserLayer.setCell((int) position.x, (int) position.y, cell);
    }

    /**
     * Checks if player moves on to a hole
     *
     * @param position that is checked
     * @return true if the position contains a hole
     */
    public boolean hasHole(Vector2 position) {
        for (Vector2 vector : holes) {
            if (vector.equals(position)) {
                scream.play(0.2f);
                return true;
            }
        }
        return false;
    }

    /**
     * Check if player is outside of board
     *
     * @param player to check
     */
    public boolean outsideBoard(Player player) {
        return player.getPosition().x < 0 ||
                player.getPosition().x >= this.boardWidth ||
                player.getPosition().y < 0 ||
                player.getPosition().y >= this.boardHeight ||
                hasHole(player.getPosition());
    }

    /**
     * Places a player in backup position or alternative position
     *
     * @param player to respawn
     */
    public void respawn(Player player) {
        removePlayerFromBoard(player);
        if (hasPlayer(player.getBackupPosition())) {
            player.chooseAlternativeBackupPosition(this, player.getBackupPosition());
            player.setPosition(new Vector2(player.getAlternativeBackupPosition().x, player.getAlternativeBackupPosition().y));
            player.setDirection(player.getAlternativeBackupDirection());
        } else {
            player.setPosition(new Vector2(player.getBackupPosition().x, player.getBackupPosition().y));
            player.setDirection(player.getBackupDirection());
        }
        addPlayer(player);
    }

    public void respawnPlayers() {
        for (Player player : players) {
            if (outsideBoard(player)) {
                player.decrementLifeTokens();
                respawn(player);
            }
        }
    }

    public boolean validRespawnPosition(Vector2 position, Direction direction) {
        Vector2 currPos = position;
        for (int step = 0; step < 3; step++) {
            if (hasPlayer(currPos)) {
                return false;
            }
            currPos = getNeighbourPosition(currPos, direction);
        }
        return !hasHole(position);
    }

    public List<Direction> getDirectionRandomOrder() {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);
        return directions;
    }

    /**
     * @return player that should be moved with arrows
     */
    public Player getPlayer1() {
        for (Player player : players) {
            if (player.getPlayerNr() == 1) {
                return player;
            }
        }
        return null;
    }

    /**
     * @param position  to go from
     * @param direction to go in
     * @return true if there is no wall blocking the way
     */
    public boolean canGo(Vector2 position, Direction direction) {
        TiledMapTileLayer.Cell cell = wallLayer.getCell((int) position.x, (int) position.y);
        TiledMapTileLayer.Cell northCell = wallLayer.getCell((int) position.x, (int) position.y + 1);
        TiledMapTileLayer.Cell southCell = wallLayer.getCell((int) position.x, (int) position.y - 1);
        TiledMapTileLayer.Cell eastCell = wallLayer.getCell((int) position.x + 1, (int) position.y);
        TiledMapTileLayer.Cell westCell = wallLayer.getCell((int) position.x - 1, (int) position.y);

        switch (direction) {
            case NORTH:
                if (hasNorthWall(cell) || hasSouthWall(northCell)) {
                    return false;
                }
                break;
            case SOUTH:
                if (hasSouthWall(cell) || hasNorthWall(southCell)) {
                    return false;
                }
                break;
            case EAST:
                if (hasEastWall(cell) || hasWestWall(eastCell)) {
                    return false;
                }
                break;
            case WEST:
                if (hasWestWall(cell) || hasEastWall(westCell)) {
                    return false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * @param cell to check for wall
     * @return true if cell has a wall on west side
     */
    public boolean hasWestWall(TiledMapTileLayer.Cell cell) {
        if (cell != null) {
            int tileID = cell.getTile().getId();
            return tileID == TileID.WEST_WALL.getId() ||
                    tileID == TileID.NORTHWEST_WALL.getId() ||
                    tileID == TileID.SOUTHWEST_WALL.getId() ||
                    tileID == TileID.WEST_LASER_WALL.getId();
        }
        return false;
    }

    /**
     * @param cell to check for wall
     * @return true if cell has a wall on east side
     */
    public boolean hasEastWall(TiledMapTileLayer.Cell cell) {
        if (cell != null) {
            int tileID = cell.getTile().getId();
            return tileID == TileID.EAST_WALL.getId() ||
                    tileID == TileID.NORTHEAST_WALL.getId() ||
                    tileID == TileID.SOUTHEAST_WALL.getId() ||
                    tileID == TileID.EAST_LASER_WALL.getId();
        }
        return false;
    }

    /**
     * @param cell to check for wall
     * @return true if cell has a wall on south side
     */
    public boolean hasSouthWall(TiledMapTileLayer.Cell cell) {
        if (cell != null) {
            int tileID = cell.getTile().getId();
            return tileID == TileID.SOUTH_WALL.getId() ||
                    tileID == TileID.SOUTHWEST_WALL.getId() ||
                    tileID == TileID.SOUTHEAST_WALL.getId() ||
                    tileID == TileID.SOUTH_LASER_WALL.getId();
        }
        return false;
    }

    /**
     * @param cell to check for wall
     * @return true if cell has a wall on north side
     */
    public boolean hasNorthWall(TiledMapTileLayer.Cell cell) {
        if (cell != null) {
            int tileID = cell.getTile().getId();
            return tileID == TileID.NORTH_WALL.getId() ||
                    tileID == TileID.NORTHWEST_WALL.getId() ||
                    tileID == TileID.NORTHEAST_WALL.getId() ||
                    tileID == TileID.NORTH_LASER_WALL.getId();
        }
        return false;
    }


    /**
     * Add player to the board, so the direction is correct
     * @param player that should be rotated
     *
     */
    public void rotatePlayer(Player player) {
        addPlayer(player);
    }

    /**
     * Check if it is possible to move in the direction the player are facing.
     * Check if player should and can push another player, if not return
     * Remove player from board
     * Update player position according to direction
     * Add player to cell that corresponds to player position
     *
     * @param player that is suppose to move
     * @param direction the {@link Direction} the player should be moved in, if set to null use the player direction.
     */
    public void movePlayer(Player player, Direction direction) {
        Vector2 position = player.getPosition();
        if (direction == null){
            direction = player.getDirection();
        }

        if (!canGo(position, direction)) {
            wallImpact.play(0.2f);
            addPlayer(player);
            return;
        }
        if (shouldPush(player)) {
            Player enemyPlayer = getPlayer(getNeighbourPosition(player.getPosition(), direction));
            if (canPush(enemyPlayer, direction)) {
                pushPlayer(enemyPlayer, direction);
            } else {
                addPlayer(player);
                return;
            }
        }

        removePlayerFromBoard(player);

        switch (direction) {
            case NORTH:
                position.y++;
                break;
            case EAST:
                position.x++;
                break;
            case WEST:
                position.x--;
                break;
            case SOUTH:
                position.y--;
                break;
            default:
                break;
        }

        player.setPosition(position);
        addPlayer(player);

        if (hasFlag(player.getPosition())) {
            pickUpFlag(player);
        }
    }

    public ArrayList<Vector2> getNeighbourhood(Vector2 position) {
        ArrayList<Vector2> positions = new ArrayList<>();
        for (int yi = -1; yi <= 1; yi++) {
            for (int xi = -1; xi <= 1; xi++) {
                int x = (int) (position.x + xi);
                int y = (int) (position.y + yi);
                if (x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) {
                    positions.add(new Vector2(x, y));
                }
            }
        }
        return positions;
    }

    /**
     * @param position  to go from
     * @param direction to go in
     * @return neighbour position in direction from position
     */
    public Vector2 getNeighbourPosition(Vector2 position, Direction direction) {
        Vector2 neighbourPosition = new Vector2(position);
        switch (direction) {
            case EAST:
                neighbourPosition.x++;
                break;
            case WEST:
                neighbourPosition.x--;
                break;
            case NORTH:
                neighbourPosition.y++;
                break;
            case SOUTH:
                neighbourPosition.y--;
                break;
            default:
                break;
        }
        return neighbourPosition;
    }

    public boolean hasPlayer(Vector2 position) {
        for (Player enemyPlayer : players) {
            if (enemyPlayer.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return player if there is a player in that position
     *
     * @param position to check
     * @return player in position
     */
    public Player getPlayer(Vector2 position) {
        for (Player enemyPlayer : players) {
            if (enemyPlayer.getPosition().equals(position)) {
                return enemyPlayer;
            }
        }
        return null;
    }

    /**
     * Check if the moving player should try to push another player
     *
     * @param player trying to move
     * @return true if player should push another player to move
     */
    public boolean shouldPush(Player player) {
        return hasPlayer(getNeighbourPosition(player.getPosition(), player.getDirection()));
    }

    /**
     * @param player    that should be pushed
     * @param direction to push in
     * @return true if it is possible to push all enemies in a row
     */
    private boolean canPush(Player player, Direction direction) {
        if (hasPlayer(getNeighbourPosition(player.getPosition(), direction))) {
            return canGo(player.getPosition(), direction) &&
                    canPush(getPlayer(getNeighbourPosition(player.getPosition(), direction)), direction);
        }
        return canGo(player.getPosition(), direction);
    }

    /**
     * Push all players in a row
     * Remove player from board
     * Update player position according to direction
     * Add player to cell that corresponds to player position
     *
     * @param player    that should be pushed
     * @param direction to push player in
     */
    private void pushPlayer(Player player, Direction direction) {
        if (hasPlayer(getNeighbourPosition(player.getPosition(), direction))) {
            pushPlayer(getPlayer(getNeighbourPosition(player.getPosition(), direction)), direction);
        }
        removePlayerFromBoard(player);
        switch (direction) {
            case SOUTH:
                player.setPosition(new Vector2(player.getPosition().x, player.getPosition().y - 1));
                break;
            case WEST:
                player.setPosition(new Vector2(player.getPosition().x - 1, player.getPosition().y));
                break;
            case EAST:
                player.setPosition(new Vector2(player.getPosition().x + 1, player.getPosition().y));
                break;
            case NORTH:
                player.setPosition(new Vector2(player.getPosition().x, player.getPosition().y + 1));
                break;
            default:
                break;
        }
        addPlayer(player);
    }

    /**
     * Remove player from board
     *
     * @param player to remove from board
     */
    public void removePlayerFromBoard(Player player) {
        playerLayer.setCell((int) player.getPosition().x, (int) player.getPosition().y, null);
    }

    public boolean hasFlag(Vector2 position) {
        return flagLayer.getCell((int) position.x, (int) position.y) != null;
    }

    public Flag getFlag(Vector2 position) {
        for (Flag flag : flags) {
            if (flag.getPosition().equals(position)) {
                return flag;
            }
        }
        return null;
    }

    public void pickUpFlag(Player player) {
        Flag flag = getFlag(player.getPosition());
        player.pickUpFlag(getFlag(player.getPosition()), flag.getFlagnr());
    }

    public ArrayList<Flag> getFlags(){
        return flags;
    }

    /**
     * @return list of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
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