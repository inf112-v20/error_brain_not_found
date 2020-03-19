package inf112.skeleton.app;


import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;

import java.util.ArrayList;

public class Player {

    private final int playerNr;
    private Vector2 backupPosition;
    private Direction backupDirection;
    private Vector2 position;
    private Direction direction;
    private ArrayList<Flag> flagsCollected;
    private ArrayList<ProgramCard> selectedCards;
    private ArrayList<ProgramCard> allCards;

    public Player(Vector2 position, int playerNr) {
        this.position = position;
        this.direction = Direction.EAST;
        this.playerNr = playerNr;
        this.flagsCollected = new ArrayList<>();
        this.selectedCards = new ArrayList<>();
        this.allCards = new ArrayList<>();
        setBackup(position, Direction.EAST);
    }

    public ArrayList<ProgramCard> getAllCards() {
        return allCards;
    }

    public ArrayList<ProgramCard> getSelectedCards() {
        return selectedCards;
    }

    public void selectCards() {
        while (selectedCards.size() < 5) {
            selectedCards.add(allCards.remove(0));
        }
    }

    public void drawCards(Deck deck) {
        while (allCards.size() < 9) {
            allCards.add(deck.drawCard());
        }
    }

    /**
     * Set new backup position and direction
     *
     * @param backupPosition  respawn position when damaged
     * @param backupDirection respawn direction when damaged
     */
    public void setBackup(Vector2 backupPosition, Direction backupDirection) {
        if (this.backupPosition == null) {
            this.backupPosition = new Vector2(backupPosition);
        } else {
            this.backupPosition.set(backupPosition.x, backupPosition.y);
        }
        this.backupDirection = backupDirection;
    }

    /**
     * @return backup position
     */
    public Vector2 getBackupPosition() {
        return backupPosition;
    }

    /**
     * @return backup direction
     */
    public Direction getBackupDirection() {
        return backupDirection;
    }

    /**
     * @return number of player
     */
    public int getPlayerNr() {return playerNr; }

    /**
     * @return the {@link Vector2} to the player
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set's the position to the player
     */
    public void setPosition(Vector2 pos) {
        this.position.set(pos.x, pos.y);
    }

    /**
     * @return the direction the player are facing
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Set's the direction the player are suppose to face
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void pickUpFlag(Flag flag, int flagNr) {
        switch (flagNr) {
            case 1:
                if (!flagsCollected.contains(flag)) {
                    flagsCollected.add(flag);
                }
                break;
            case 2:
                if (!flagsCollected.contains(flag) && flagsCollected.size() == 1) {
                    flagsCollected.add(flag);
                }
                break;
            case 3:
                if (!flagsCollected.contains(flag) && flagsCollected.size() == 2) {
                    flagsCollected.add(flag);
                }
                break;
            case 4:
                if (!flagsCollected.contains(flag) && flagsCollected.size() == 3) {
                    flagsCollected.add(flag);
                }
                break;
            default:
                break;
        }
    }

    public ArrayList<Flag> getFlagsCollected() {
        return flagsCollected;
    }

    public boolean hasAllFlags(int numberOfFlags) {
        return flagsCollected.size() == numberOfFlags;
    }

    public String toString() {
        return "Player " + getPlayerNr();
    }
}
