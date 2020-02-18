package inf112.skeleton.app;

import inf112.skeleton.app.cards.CardHand;
import inf112.skeleton.app.cards.Deck;
import org.junit.Before;
import org.junit.Test;

public class CardHandTest {

    private final int NUMBERS_OF_CARDS = 78;
    // Random number
    private final int NUMER_OF_SLOTS = 6;

    private Deck deck;

    private CardHand hand;

    @Before
    public void setUp() {
        deck = new Deck();
        // Do not shuffle deck
        deck.makeNewDeck();
        hand = new CardHand(deck, NUMER_OF_SLOTS);
    }

    @Test
    public void whenHandIsMade
}
