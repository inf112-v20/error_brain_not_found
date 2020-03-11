package inf112.skeleton.app.cards;

import java.util.ArrayList;

public class CardHand {

    private ArrayList<ProgramCard> hand;

    public CardHand() {
        hand = new ArrayList<>();
    }

    public void dealCards(Deck deck, int slots) {
        while (hand.size() < slots) {
            hand.add(deck.drawCard());
        }
    }
}
