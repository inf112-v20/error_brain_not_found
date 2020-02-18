package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class CardHand {

    // TODO: se if it's better to have ProgramCard array with fixed size.
    private ArrayList<ProgramCard> hand = new ArrayList<>();

    // TODO: Check rulebook for for what decide how many card a player can have.
    public CardHand(Deck deck, int slots) {
        for (int i = 0; i < slots; i++) {
            hand.add(deck.drawCard());
        }
    }

}
