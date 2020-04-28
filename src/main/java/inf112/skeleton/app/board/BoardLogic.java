package inf112.skeleton.app.board;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.TileID;
import inf112.skeleton.app.objects.player.Player;

import java.util.ArrayList;

public class BoardLogic {

    public boolean validRespawnPosition(Vector2 position, Direction direction, Board board) {
        Vector2 currPos = position;
        for (int step = 0; step < 3; step++) {
            if (board.hasPlayer(currPos)) {
                return false;
            }
            currPos = board.getNeighbourPosition(currPos, direction);
        }
        return !hasHole(position, board);
    }

    /**
     * Checks if a position is outside the map
     *
     * @param position the position to check
     * @param board    the board that is checked
     * @return true if the position is outside of the board
     */
    public boolean outsideBoard(Vector2 position, Board board) {
        int boardWidth = board.getBoardWidth();
        int boardHeight = board.getBoardHeight();
        return position.x < 0 ||
                position.x >= boardWidth ||
                position.y < 0 ||
                position.y >= boardHeight;
    }

    /**
     * Check if player is outside of board
     *
     * @param player to check
     */
    public boolean outsideBoard(Player player, Board board) {
        return outsideBoard(player.getPosition(), board) || hasHole(player.getPosition(), board);
    }

    /**
     * Checks if player moves on to a hole
     *
     * @param board    the board that are supposed to be checked with
     * @param position that is checked
     * @return true if the position contains a hole
     */
    public boolean hasHole(Vector2 position, Board board) {
        for (Vector2 vector : board.getHoles()) {
            if (vector.equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tells if there is possible to move in the intended {@link Direction} direction
     *
     * @param board     the board that are supposed to be checked with
     * @param position  to go from
     * @param direction to go in
     * @return true if there is no wall blocking the way
     */
    public boolean canGo(Vector2 position, Direction direction, Board board) {
        TiledMapTileLayer.Cell cell = board.getWallLayer().getCell((int) position.x, (int) position.y);
        TiledMapTileLayer.Cell northCell = board.getWallLayer().getCell((int) position.x, (int) position.y + 1);
        TiledMapTileLayer.Cell southCell = board.getWallLayer().getCell((int) position.x, (int) position.y - 1);
        TiledMapTileLayer.Cell eastCell = board.getWallLayer().getCell((int) position.x + 1, (int) position.y);
        TiledMapTileLayer.Cell westCell = board.getWallLayer().getCell((int) position.x - 1, (int) position.y);

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
     * Checks is there is a wall to the west of a given {@link TiledMapTileLayer.Cell} cell}.
     *
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
     * Checks is there is a wall to the east of a given {@link TiledMapTileLayer.Cell cell}.
     *
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
     * Checks is there is a wall to the south of a given {@link TiledMapTileLayer.Cell cell}.
     *
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
     * Checks is there is a wall to the north of a given {@link TiledMapTileLayer.Cell cell}.
     *
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
     * Check if the moving player should try to push another player
     *
     * @param board  the board that are supposed to be checked with
     * @param player trying to move
     * @return true if player should push another player to move
     */
    public boolean shouldPush(Player player, Board board) {
        return board.hasPlayer(board.getNeighbourPosition(player.getPosition(), player.getDirection()));
    }

    /**
     * Push all players in a row
     * Remove player from board
     * Update player position according to direction
     * Add player to cell that corresponds to player position
     *
     * @param board     the board that are supposed to be checked with
     * @param player    that should be pushed
     * @param direction to push player in
     */
    public void pushPlayer(Player player, Direction direction, Board board) {
        if (board.hasPlayer(board.getNeighbourPosition(player.getPosition(), direction))) {
            pushPlayer(board.getPlayer(board.getNeighbourPosition(player.getPosition(), direction)), direction, board);
        }
        board.removePlayerFromBoard(player);
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
        board.addPlayer(player);
        player.setBeltPushDir(null);
    }

    /**
     * Checks if there is possible to push a player.
     *
     * @param board     the board that are supposed to be checked with
     * @param player    that should be pushed
     * @param direction to push in
     * @return true if it is possible to push all enemies in a row.
     */
    public boolean canPush(Player player, Direction direction, Board board) {
        if (board.hasPlayer(board.getNeighbourPosition(player.getPosition(), direction))) {
            return canGo(player.getPosition(), direction, board) &&
                    canPush(board.getPlayer(board.getNeighbourPosition(player.getPosition(), direction)), direction, board);
        }
        return canGo(player.getPosition(), direction, board);
    }

    /**
     * Checks if there is possible to fire in the direction and position
     *
     * @param position  the tile in question.
     * @param direction the way the fire is happening
     * @param board     the board that are supposed to be checked with
     * @return true if {@link #canGo(Vector2, Direction, Board) canGo} is true and if not {@link #outsideBoard(Vector2, Board) outsideBoard} is true.
     */
    public boolean canFire(Vector2 position, Direction direction, Board board) {
        return canGo(position, direction, board) && !outsideBoard(board.getNeighbourPosition(position, direction), board);
    }

    /**
     * Checks for valid player number
     *
     * @param number number of player
     * @return true if player number is valid
     */
    public boolean validPlayerNumber(int number, ArrayList<Player> playersList) {
        for (Player player : playersList) {
            if (number == player.getPlayerNr()) {
                return false;
            }
        }
        return 0 < number && number < 9;
    }

}
