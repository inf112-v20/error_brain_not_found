package inf112.skeleton.app.objects.player;

import java.util.Comparator;

public class PlayerSorter implements Comparator<Player> {

    private final int cardNumber;

    public PlayerSorter(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public int compare(Player player1, Player player2) {
        return player2.getRegisters().getCard(cardNumber).getPriority() -
                player1.getRegisters().getCard(cardNumber).getPriority();
    }
}
