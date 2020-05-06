package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.Registers;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.GameClientThread;
import inf112.skeleton.app.lan.GameServer;
import inf112.skeleton.app.lan.ServerThread;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.player.Player;
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    private RallyGame game;
    private Player player1;
    private ArrayList<Belt> belts;
    private Converter converter;
    private Player player2;
    private Player player3;

    @Mock
    private Player mainPlayer;

    @Mock
    private GameClientThread client;

    @Mock
    private Registers registers;

    @Mock
    private ServerThread serverThread;

    @Mock
    private GameServer server;

    @Mock
    private GameScreen screen;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky Exchange.tmx");
        this.game.setDeck(new Deck().getDeck());
        this.game.setScreen(screen);
        Board board = game.getBoard();
        player1 = new Player(new Vector2(0, 0), 1);
        player2 = new Player(new Vector2(0, 1), 2);
        player3 = new Player(new Vector2(0, 2), 3);
        game.setPlayers(player1, player2, player3);
        board.addPlayer(player1);
        this.belts = board.getBelts();
        this.converter = new Converter();

        game.setMainPlayer(mainPlayer);
        when(mainPlayer.getRegisters()).thenReturn(registers);
        ProgramCard card = new ProgramCard(1,1, Rotate.NONE, "Card");
        when(registers.getCards()).thenReturn(new ArrayList<>(Arrays.asList(card, card, card, card, card)));


        when(serverThread.getServer()).thenReturn(server);
    }

    /**
     * Give the player ten damage tokens.
     * @param player
     */
    private void fillUpDamageTokens(Player player) {
        for (int takeDamage = 1; takeDamage <= 10; takeDamage++) {
            player.handleDamage();
        }
    }

    @Test
    public void lifeDecreasedWhenCollectedTenDamageTokensTest() {
        int livesBefore = player1.getLifeTokens();
        fillUpDamageTokens(player1);
        game.decreaseLives();
        assertEquals(livesBefore-1, player1.getLifeTokens());
    }

    @Test
    public void respawnIfTenCollectedDamageTokensTest() {
        // Move player out of backupposition
        game.getBoard().movePlayer(player1, false);
        fillUpDamageTokens(player1);
        game.decreaseLives();
        game.respawnPlayers();
        assertTrue(player1.isInBackupState());
    }

    @Test
    public void deadPlayerAreRemovedFromGameTest() {
        // Kill player
        for (int livesTaken = 1; livesTaken <= 3; livesTaken++) {
            fillUpDamageTokens(player1);
            player1.decrementLifeTokens();
            game.decreaseLives();
        }
        game.removeDeadPlayers();
        assertFalse(game.players.contains(player1));
    }

    @Test
    public void playerMovesOnBeltTest() {
        Belt belt = belts.get(0);
        Vector2 beltPosition = belt.getPosition();
        player1.setPosition(beltPosition);
        game.activateBelts(false);
        assertNotEquals(beltPosition, player1.getPosition());
    }

    @Test
    public void playerMovesInSameDirectionAsBeltTest() {
        Belt belt = belts.get(0);
        Vector2 beltPosition = belt.getPosition();
        Direction beltDirection = belt.getDirection();
        Vector2 newPosition = game.getBoard().getNeighbourPosition(beltPosition, beltDirection);
        player1.setPosition(beltPosition);
        game.activateBelts(false);
        assertEquals(newPosition, player1.getPosition());
    }

    /**
     * Place player at belt below the corner belt, move belts so player is at the corner belt.
     * The corner belt should rotate player from NORTH to WEST.
     */
    @Test
    public void playerChangesDirectionWhenInACornerOfTheBeltTest() {
        // Found postition in Risky Exhange
        Vector2 fromSouthToNorthBeltPosition = new Vector2(1, 1);
        player1.setPosition(fromSouthToNorthBeltPosition);
        player1.setDirection(Direction.NORTH);
        // Move player onto corner belt
        game.activateBelts(false);
        // Turn player with corner belt
        game.activateBelts(false);
        assertEquals(Direction.WEST, player1.getDirection());
    }

    @Test
    public void moveOneStepWhenOnBeltTest() {
        // Found postition in Risky Exhange. Belt goes east.
        Vector2 startBeltPosition = new Vector2(5, 5);
        player1.setPosition(startBeltPosition);
        game.activateBelts(false);
        game.activateBelts(true);
        Vector2 beltMovedToPosition = new Vector2(6, 5);
        assertEquals(beltMovedToPosition, player1.getPosition());
    }

    @Test
    public void moveTwoStepOnExpressBeltTest() {
        // Found position in Risky Exhange. Belt goes west
        Vector2 startBeltPosition = new Vector2(8, 6);
        player1.setPosition(startBeltPosition);
        game.activateBelts(false);
        game.activateBelts(true);
        Vector2 beltMovedToPosition = new Vector2(6, 6);
        assertEquals(beltMovedToPosition, player1.getPosition());
    }

    @Test
    public void repairTileResetsDamageTokensToZeroWhenOneDamageToken() {
        Vector2 repairTilePosition = game.getBoard().getRepairTiles().get(0);
        player1.handleDamage();
        player1.setPosition(repairTilePosition);
        game.activateRepairTiles();
        assertEquals(0, player1.getDamageTokens());
    }

    @Test
    public void repairTileResetsDamageTokenToZeroWhenTenDamageTokens() {
        Vector2 repairTilePosition = game.getBoard().getRepairTiles().get(0);
        for (int i = 0; i < 10; i++) {
            player1.handleDamage();
        }
        player1.setPosition(repairTilePosition);
        game.activateRepairTiles();
        assertEquals(0, player1.getDamageTokens());
    }

    @Test
    public void poweredDownPlayersDoNotGetCardsTest() {
        player1.setPoweredDown(true);
        game.dealCards();
        assertTrue(player1.getRegisters().getCards().isEmpty());
    }

    @Test
    public void playerPoweringDownBecomesPoweredDownTest() {
        player1.setPoweringDown(true);
        game.powerDown();
        assertTrue(player1.isPoweredDown());
    }

    @Test
    public void powerDownResetDamageTokensTest() {
        player1.handleDamage();
        player1.setPoweredDown(true);
        game.powerDown();
        assertEquals(0, player1.getDamageTokens());
    }

    @Test
    public void confirmedButtonPoweringDownTest() {
        game.setIsServer(false);
        game.setClient(client);
        when(mainPlayer.getRegisters().hasRegistersWithoutCard()).thenReturn(true);
        when(mainPlayer.getPowerDownNextRound()).thenReturn(true);
        game.confirm();
        verify(mainPlayer).setPoweringDown(true);
    }

    @Test
    public void confirmingCardsSendToServerTest() {
        game.setClient(client);
        when(mainPlayer.getRegisters().hasRegistersWithoutCard()).thenReturn(false);
        game.confirm();
        verify(client, times(5)).sendMessage(anyString());
    }

    @Test
    public void confirmingCardsSendToClientsTest() {
        game.setServerThread(serverThread);
        game.setIsServerToTrue();
        when(mainPlayer.getRegisters().hasRegistersWithoutCard()).thenReturn(false);
        when(server.allClientsHaveSelectedCardsOrIsPoweredDown()).thenReturn(true);
        game.confirm();
        verify(server).sendSelectedCardsToAll();
    }

    @Test
    public void pressedPowerDownButtonAndSelectedCardsTest() {
        game.setClient(client);
        when(mainPlayer.getPowerDownNextRound()).thenReturn(true);
        when(mainPlayer.getRegisters().hasRegistersWithoutCard()).thenReturn(false);
        game.confirm();
        verify(client, times(6)).sendMessage(anyString());
        verify(mainPlayer).setPoweringDown(true);
    }

    @Test
    public void sendPowerUpMessageToServer() {
        game.setClient(client);
        when(mainPlayer.getPlayerNr()).thenReturn(2);
        game.sendPowerUpMessage();
        verify(client).sendMessage(converter.createMessageFromPlayer(2, Messages.POWER_UP));
    }

    @Test
    public void sendPowerUpMessageToClient() {
        game.setIsServerToTrue();
        game.setServerThread(serverThread);
        when(mainPlayer.getPlayerNr()).thenReturn(1);
        game.sendPowerUpMessage();
        verify(server).sendToAll(converter.createMessageFromPlayer(1, Messages.POWER_UP));

    }

    @Test
    public void confirmingPowerUpTest() {
        game.setClient(client);
        game.addPoweredDownPlayer(mainPlayer);
        game.setWaitingForCards(false);
        when(mainPlayer.isPoweredDown()).thenReturn(true);
        when(mainPlayer.getRegisters().hasRegistersWithoutCard()).thenReturn(true);
        when(mainPlayer.getPowerUpNextRound()).thenReturn(true);
        game.confirm();
        verify(mainPlayer).setPoweredDown(false);
    }

    @Test
    public void confirmingStayInPowerDownTest() {
        game.setClient(client);
        game.setWaitingForCards(false);
        game.addPoweredDownPlayer(mainPlayer);
        when(mainPlayer.isPoweredDown()).thenReturn(true);
        when(mainPlayer.getRegisters().hasRegistersWithoutCard()).thenReturn(true);
        when(mainPlayer.getPowerUpNextRound()).thenReturn(false);
        when(mainPlayer.getPlayerNr()).thenReturn(2);
        game.confirm();
        verify(client).sendMessage(converter.createMessageFromPlayer(2, Messages.CONTINUE_POWER_DOWN));
    }

    @Test
    public void everyOneInPowerDownAtEndOfTurnNextTurnStartsRightAwayTest() {
        game.setIsServer(true);
        game.setServerThread(serverThread);
        player1.setPoweringDown(true);
        player2.setPoweringDown(true);
        player3.setPoweringDown(true);
        game.powerDown();
        game.serverGetReadyForNextRound();
        verify(server).sendToAll(Messages.START_TURN.toString());
    }

    @Test
    public void onlyServerInPowerDownThenServerHasConfirmedTest() {
        game.setIsServer(true);
        game.setServerThread(serverThread);
        player1.setPoweringDown(true);
        game.powerDown();
        game.serverGetReadyForNextRound();
        verify(server).setServerHasConfirmed(true);
    }

    @Test
    public void onlyServerIsPoweredUpThenAllClientsHaveConfirmedTest() {
        game.setIsServer(true);
        game.setServerThread(serverThread);
        player2.setPoweringDown(true);
        player3.setPoweringDown(true);
        game.powerDown();
        game.serverGetReadyForNextRound();
        verify(server).setAllClientsHaveSelectedCardsOrIsPoweredDown(true);
    }

}
