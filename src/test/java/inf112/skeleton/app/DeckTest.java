package inf112.skeleton.app;

import inf112.skeleton.app.cards.Deck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeckTest {

    private Deck deck;
    private final int NUMBER_OF_CARDS = 78;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void whenNewDeckIsMadeTheNumberOfProgramCardsInDeckIsNumberOfCardsTest() {
        assertEquals(NUMBER_OF_CARDS, deck.deckSize());
    }

    @Test
    public void whenNewDeckIsMadeButNotShuffledFirstCardIsAMove1CardTest() {
        deck.makeNewDeck();
        assertEquals(3, deck.drawCard().getDistance());
    }

    @Test
    public void whenACardIsDrawnTheSizeIsDecreasedByOneTest() {
        deck.drawCard();
        assertEquals(NUMBER_OF_CARDS-1, deck.deckSize());
    }


}
