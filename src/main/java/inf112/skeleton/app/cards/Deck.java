package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<ProgramCard> deck;

    private ProgramCard card;
    private int priority = 0;

    public Deck() {
        makeNewDeck();
        shuffleDeck();
    }

    /**
     * Makes all the programCards and puts them on the stack
     */
    public void makeNewDeck() {
        deck = new Stack<>();


        priority = makeRotateCards(priority);
        makeMoveCards(priority);
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
     * Makes all the rotating cards, RotateR (18), RotateL (18) and RotateU (6) cards.
     * @param startPri is the priority number the last card made had.
     * @return the priority number of the last card made
     */
    private int makeRotateCards(int startPri){
        int priority = startPri;
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
            card = new ProgramCard(priority, 0, Rotate.UTURN, "U-Turn");
            deck.push(card);
        }
        return priority;
    }

    /**
     * Makes all the move cards, Move 1 (18), Move 2 (12) and Move 3 (6).
     * @param startPri is the priority number of the last card made had.
     */
    private void makeMoveCards(int startPri){
        int priority = startPri;
        boolean checker = true;
        for (int i = 0; i < 30; i++) {
            priority += 10;

            // This is so the move tre cards not all get in the same range of priority.
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
}
