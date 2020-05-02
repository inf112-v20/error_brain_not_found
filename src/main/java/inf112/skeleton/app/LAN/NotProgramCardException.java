package inf112.skeleton.app.LAN;

/**
 * Handling when input is not a card
 */
public class NotProgramCardException extends Exception {
    public NotProgramCardException(String message) {
        super(message);
    }
}
