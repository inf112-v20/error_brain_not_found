package inf112.skeleton.app.lan;

/**
 * Handling when input is not a card
 */
public class NotProgramCardException extends Exception {
    public NotProgramCardException(String message) {
        super(message);
    }
}
