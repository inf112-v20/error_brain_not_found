package inf112.skeleton.app;

import inf112.skeleton.app.cards.Deck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeckTest {

    private Deck deck;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void whenNewDeckIsMadeTheNumberOfProgramCardsInDeckIs78() {
        assertEquals(78, deck.deckSize());
    }


}
