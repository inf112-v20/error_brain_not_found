package inf112.skeleton.app.enums;

import inf112.skeleton.app.cards.ProgramCard;

/**
 * Messages to communicate between Host and Client.
 */
public enum Messages {
    QUIT, HOST_LEAVES, CLOSED,
    DECK_BEGIN, DECK_END, HERE_IS_MAP,
    START_TURN, CONTINUE_TURN, CONFIRM,
    POWERING_DOWN, POWER_UP, CONTINUE_POWER_DOWN,
    STOP_THREAD;
}
