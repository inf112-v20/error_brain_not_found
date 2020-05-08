package inf112.skeleton.app.lan;

import inf112.skeleton.app.cards.ProgramCard;

/**
 * Used by {@link Converter} to give both player and card attached to player.
 */
public class PlayerAndProgramCard {

    private int playerNumber;
    private ProgramCard card;

    public PlayerAndProgramCard(int playerNumber, ProgramCard card) {
            this.playerNumber = playerNumber;
            this.card = card;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public ProgramCard getProgramCard() {
        return card;
    }


}
