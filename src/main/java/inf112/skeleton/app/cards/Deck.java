package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<ProgramCard> deck;
    private Stack<ProgramCard> discardPile;

    public Deck() {
        makeNewDeck();
        shuffleDeck();
    }

    public void removeCards(ArrayList<ProgramCard> cards) {
        deck.removeAll(cards);
    }

    /**
     * Make all the programCards and put them in the stack
     */
    public void makeNewDeck() {
        deck = new Stack<>();
        discardPile = new Stack<>();
        makeRotateCards();
        makeMoveCards();
    }

    public void addCardsToDiscardPile(ArrayList<ProgramCard> programCards) {
        discardPile.addAll(programCards);
    }

    public void addCardToDiscardPile(ProgramCard programCard) {
        discardPile.push(programCard);
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
        ProgramCard card = deck.pop();
        if (deck.isEmpty()) {
            addDiscardPileToDeck();
        }
        return card;
    }

    public void addDiscardPileToDeck() {
        deck.addAll(discardPile);
        discardPile.clear();
        shuffleDeck();
    }

    /**
     * Make all rotate cards, 18 Rotate right, 18 Rotate left and 6 Rotate U-turn
     * with priority evenly spread between all cards
     */
    private void makeRotateCards() {
        // Make 42 rotate cards
        for (int priority = 1; priority <= 42; priority++) {
            // Every seventh rotate card should be U-turn
            if (priority % 7 == 0) {
                deck.push(new ProgramCard(priority * 10, 0, Rotate.UTURN, "U-turn"));
            } else if (priority % 2 == 0) {
                deck.push(new ProgramCard(priority * 10, 0, Rotate.RIGHT, "Right turn"));
            } else {
                deck.push(new ProgramCard(priority * 10, 0, Rotate.LEFT, "Left turn"));
            }
        }
    }

    /**
     * Make all move cards, 18 Move 1, 12 Move 2 and 6 Move 3
     * with priority evenly spread between all cards
     */
    private void makeMoveCards() {
        int startPriority = 420;
        // Make 36 move cards
        for (int priority = 1; priority <= 36; priority++) {
            if (priority % 6 == 0) {
                // For every sixth card there should be one move 3, two move 2 and three move 1 cards
                deck.push(new ProgramCard(priority * 10 + startPriority, 3, Rotate.NONE, "Move 3"));
            } else if ((priority + 1) % 6 == 0 || (priority + 2) % 6 == 0) {
                deck.push(new ProgramCard(priority * 10 + startPriority, 2, Rotate.NONE, "Move 2"));
            } else {
                deck.push(new ProgramCard(priority * 10 + startPriority, 1, Rotate.NONE, "Move 1"));
            }
        }
    }

    /**
     *
     * @return the stack of cards in the deck
     */
    public Stack<ProgramCard> getDeck() {
        return deck;
    }

    /**
     *
     * @param stack new stack for this deck
     */
    public void setDeck(Stack<ProgramCard> stack) {
        this.deck = stack;
    }
}
