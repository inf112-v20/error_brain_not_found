package inf112.skeleton.app;


import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;

public class Player {

    private final int playerNr;
    private Vector2 backupPosition;
    private Direction backupDirection;
    private Vector2 alternativeBackupPosition;
    private Direction alternativeBackupDirection;
    private Vector2 position;
    private Direction direction;
    private ArrayList<Flag> flagsCollected;
    private ArrayList<ProgramCard> selectedCards;
    private ArrayList<ProgramCard> allCards;

    private int damageTokens;
    private int lifeTokens;

    public Player(Vector2 position, int playerNr) {
        this.position = position;
        this.direction = Direction.EAST;
        this.playerNr = playerNr;
        this.flagsCollected = new ArrayList<>();
        this.selectedCards = new ArrayList<>();
        this.allCards = new ArrayList<>();
        this.damageTokens = 0;
        this.lifeTokens = 3;

        setBackup(position, Direction.EAST);
    }

    public ArrayList<ProgramCard> getAllCards() {
        return allCards;
    }

    public ArrayList<ProgramCard> getSelectedCards() {
        System.out.println(selectedCards);
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
     * a int on how many damageTokens
     *
     * @return your damageTokens
     */
    public int getDamageTokens() {
        return damageTokens;
    }

    public int resetDamageTokens() {
        return this.damageTokens = 0;
    }

    public int getLifeTokens() {
        return lifeTokens;
    }

    public void decrementLifeTokens() {
        this.lifeTokens--;
    }

    public boolean isDead() {
        return lifeTokens <= 0;
    }

    public void handleDamage(RallyGame game) {
        this.damageTokens++;
        if (damageTokens >= 10) {
            lifeTokens--;
            damageTokens = 0;
            game.getBoard().respawn(this);
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

    public void setAlternativeBackup(Vector2 alternativeBackupPosition, Direction alternativeBackupDirection) {
        if (this.alternativeBackupPosition == null) {
            this.alternativeBackupPosition = new Vector2(alternativeBackupPosition);
        } else {
            this.alternativeBackupPosition.set(alternativeBackupPosition.x, alternativeBackupPosition.y);
        }
        this.alternativeBackupDirection = alternativeBackupDirection;
    }

    /**
     * @return alternative backup position
     */
    public Vector2 getAlternativeBackupPosition() {
        return alternativeBackupPosition;
    }

    /**
     * @return alternative backup direction
     */
    public Direction getAlternativeBackupDirection() {
        return alternativeBackupDirection;
    }


    public void chooseAlternativeBackupPosition(Board board, Vector2 position) {
        ArrayList<Vector2> possiblePositions = board.getNeighbourhood(position);
        Collections.shuffle(possiblePositions);
        for (Vector2 pos : possiblePositions) {
            for (Direction dir : board.getDirectionRandomOrder()) {
                if (board.validRespawnPosition(pos, dir)) {
                    setAlternativeBackup(pos, dir);
                    return;
                }
            }
        }
        setAlternativeBackup(board.getStartPosition(getPlayerNr()), Direction.EAST);
        if (board.hasPlayer(board.getStartPosition(getPlayerNr()))) {
            chooseAlternativeBackupPosition(board, alternativeBackupPosition);
        }
    }

    /**
     * @return number of player
     */
    public int getPlayerNr() {
        return playerNr;
    }

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

    /**
     * Update the selected cards for this player.
     * Used for testing, so that we can decide what card the player is going to play, and then test that the player
     * does what the card says.
     *
     * @param cards one or more cards (separated by comma) or a list of cards.
     */
    public void setSelectedCards(ProgramCard... cards) {
        this.selectedCards = new ArrayList<>(Arrays.asList(cards));
    }

    public ArrayList<Flag> getFlagsCollected() {
        return flagsCollected;
    }

    public boolean hasAllFlags(int numberOfFlags) {
        return flagsCollected.size() == numberOfFlags;
    }

    public boolean equals(Player other) {
        return this.getPlayerNr() == other.getPlayerNr();
    }

    public String toString() {
        return "Player " + getPlayerNr();
    }
}
