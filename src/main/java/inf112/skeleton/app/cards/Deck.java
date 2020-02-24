package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<ProgramCard> deck;
    private int priority;
    private ProgramCard card;
    private boolean checker = true;

    public Deck() {
        makeNewDeck();
        shuffleDeck();
    }

    /**
     * Makes all the programCards and puts them on the stack
     */
    public void makeNewDeck() {
        deck = new Stack<>();


        // Making RotateR (18) and RotateL (18) cards
        for (int i = 0; i < 36; i++) {
            priority += 10;
            if (i % 2 == 0) {
                card = new ProgramCard(priority, 0, Rotate.LEFT, "Rotate Left");
            } else {
                card = new ProgramCard(priority, 0, Rotate.RIGHT, "Rotate Right");
            }
            deck.push(card);
        }
        for (int i = 0; i < 6; i++) {
            priority += 10;
            ProgramCard card = new ProgramCard(priority, 0, Rotate.UTURN, "U-Turn");
            deck.push(card);
        }
        // Making Move 2 (12) and Move 3 (6) and Move 1 (12) cards
        for (int i = 0; i < 30; i++) {
            priority += 10;
            if (i % 3 == 0 && i < 18) {
                card = new ProgramCard(priority, 3, Rotate.NONE, "Move 3");
            } else {
                if (checker) {
                    card = new ProgramCard(priority, 2, Rotate.NONE, "Move 2");
                    checker = false;
                } else {
                    card = new ProgramCard(priority, 1, Rotate.NONE, "Move 1");
                    checker = true;
                }
            }
            deck.push(card);
        }
        // Making the last 6 Move 1 cards
        for (int i = 0; i < 6; i++) {
            priority += 10;
            card = new ProgramCard(priority, 1, Rotate.NONE, "Move 1");
            deck.push(card);
        }
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

}
