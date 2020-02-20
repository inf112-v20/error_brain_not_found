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
     * Makes all the programCards and puts them on the stack
     */
    public void makeNewDeck() {
        deck = new Stack<>();
        int priority = 0;

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
        for (int i = 0; i < 36; i++) {
            ProgramCard card;
            startPri += 10;
            if (i % 2 == 0) {
                card = new ProgramCard(startPri, 0, Rotate.LEFT, "Rotate Left");
            } else {
                card = new ProgramCard(startPri, 0, Rotate.RIGHT, "Rotate Right");
            }
            deck.push(card);
        }
        for (int i = 0; i < 6; i++) {
            startPri += 10;
            ProgramCard card = new ProgramCard(startPri, 0, Rotate.UTURN, "U-Turn");
            deck.push(card);
        }
        return startPri;
    }

    /**
     * Makes all the move cards, Move 1 (18), Move 2 (12) and Move 3 (6).
     * @param starter is the priority number of the last card made had.
     */
    private void makeMoveCards(int starter){
        boolean notGoodVariableName = true;
        for (int i = 0; i < 30; i++) {
            starter += 10;
            ProgramCard card;

            // This is so the move tre cards not all get in the same range of priority.
            if (i % 3 == 0 && i < 18) {
                card = new ProgramCard(starter, 3, Rotate.NONE, "Move 3");
            } else {
                if (notGoodVariableName) {
                    card = new ProgramCard(starter, 2, Rotate.NONE, "Move 2");
                    notGoodVariableName = false;
                } else {
                    card = new ProgramCard(starter, 1, Rotate.NONE, "Move 1");
                    notGoodVariableName = true;
                }
            }
            deck.push(card);
        }
        // Making the last 6 Move 1 cards
        for (int i = 0; i < 6; i++) {
            starter += 10;
            ProgramCard card = new ProgramCard(starter, 1, Rotate.NONE, "Move 1");
            deck.push(card);
        }
    }
}
