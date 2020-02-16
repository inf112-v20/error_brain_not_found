package inf112.skeleton.app.cards;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<ProgramCard> deck;

    public Deck(){
        //TODO: The deck can be make in the constructor or the constructor can call a func (?)
    }

    /** @return the size of the deck */
    public int deckSize(){
        return deck.size();
    }
    /** Shuffles the deck */
    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    /** @return the next {@link ProgramCard} in the stack / deck */
    public ProgramCard drawCard(){
        return deck.pop();
    }

}
