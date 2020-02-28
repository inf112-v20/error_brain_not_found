package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class CardHand {

    private ArrayList<ProgramCard> hand = new ArrayList<>();

    public CardHand(Deck deck, int slots) {
        for (int i = 0; i < slots; i++) {
            hand.add(deck.drawCard());
        }
    }

}
