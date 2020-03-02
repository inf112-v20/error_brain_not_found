package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<ProgramCard> deck;

    public Deck() {
        makeNewDeck();
        shuffleDeck();
    }

    /**
     * Make all the programCards and put them in the stack
     */
    public void makeNewDeck() {
        deck = new Stack<>();
        makeRotateCards();
        makeMoveCards();
    }

    /**
     * @return the size of the deck
     */
    public int deckSize() {
        return deck.size();
    }

    /**
     * Shuffles the deck
     */
    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    /**
     * @return the next {@link ProgramCard} in the stack / deck
     */
    public ProgramCard drawCard() {
        return deck.pop();
    }

    /**
     * Make all rotate cards, 18 Rotate right, 18 Rotate left and 6 Rotate U-turn
     * with priority evenly spread between all cards
     */
    private void makeRotateCards() {
        int ROTATE_CARDS = 42;
        for (int priority = 1; priority <= ROTATE_CARDS; priority++) {
            // Every seventh rotate card should be U-turn
            if (priority % 7 == 0) {
                deck.push(new ProgramCard(priority * 10, 0, Rotate.UTURN));
            } else if (priority % 2 == 0) {
                deck.push(new ProgramCard(priority * 10, 0, Rotate.RIGHT));
            } else {
                deck.push(new ProgramCard(priority * 10, 0, Rotate.LEFT));
            }
        }
    }

    /**
     * Make all move cards, 18 Move 1, 12 Move 2 and 6 Move 3
     * with priority evenly spread between all cards
     */
    private void makeMoveCards() {
        int MOVE_CARDS = 36;
        int startPriority = 420;
        for (int priority = 1; priority <= MOVE_CARDS; priority++) {
            if (priority % 6 == 0) {
                // For every sixth card there should be one move 3, two move 2 and three move 1 cards
                deck.push(new ProgramCard(priority + startPriority, 3, Rotate.NONE));
            } else if ((priority + 1) % 6 == 0 || (priority + 2) % 6 == 0) {
                deck.push(new ProgramCard(priority + startPriority, 2, Rotate.NONE));
            } else {
                deck.push(new ProgramCard(priority + startPriority, 1, Rotate.NONE));
            }
        }
    }
}
