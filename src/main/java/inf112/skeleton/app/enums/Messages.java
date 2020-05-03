package inf112.skeleton.app.enums;

import inf112.skeleton.app.cards.ProgramCard;

/**
 * Messages to communicate between Host and Client.
 */
public enum Messages {
    QUIT, HOST_LEAVES, CLOSED,
    DECK_BEGIN, DECK_END, HERE_IS_MAP,
    START_TURN,
    POWER_DOWN, POWER_UP,
    STOP_THREAD;
}
