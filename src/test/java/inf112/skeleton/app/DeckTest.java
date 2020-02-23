package inf112.skeleton.app;

import inf112.skeleton.app.cards.Deck;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeckTest {

    private Deck deck;
    private final int NUMBER_OF_MOVE_ONE_CARDS = 6;
    private final int NUMBER_OF_CARDS = 78;

    @Before
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void whenNewDeckIsMadeTheNumberOfProgramCardsInDeckIsNumberOfCards() {
        assertEquals(NUMBER_OF_CARDS, deck.deckSize());
    }

    @Test
    public void whenNewDeckIsMadeButNotShuffledFirstCardIsAMove1Card() {
        deck.makeNewDeck();
        assertEquals(1, deck.drawCard().getDistance());
    }

    @Test
    public void whenDeckIsNotShuffledTheFirstCardsAreOfTypeMoveOneCard() {
        deck.makeNewDeck();
        for (int i = 0; i < NUMBER_OF_MOVE_ONE_CARDS; i++) {
            assertEquals(1, deck.drawCard().getDistance());
        }
    }

    @Test
    public void whenACardIsDrawnTheSizeIsDecreasedByOne() {
        deck.drawCard();
        assertEquals(NUMBER_OF_CARDS-1, deck.deckSize());
    }


}
