package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.Registers;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.lan.GameClientThread;
import inf112.skeleton.app.lan.GameServer;
import inf112.skeleton.app.lan.ServerThread;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.player.Player;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {

    private RallyGame game;
    private Player player;
    private ArrayList<Belt> belts;

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

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky Exchange.tmx");
        Board board = game.getBoard();
        player = new Player(new Vector2(0, 0), 2);
        board.addPlayer(player);
        this.belts = board.getBelts();

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
        int livesBefore = player.getLifeTokens();
        fillUpDamageTokens(player);
        game.decreaseLives();
        assertEquals(livesBefore-1, player.getLifeTokens());
    }

    @Test
    public void respawnIfTenCollectedDamageTokensTest() {
        // Move player out of backupposition
        game.getBoard().movePlayer(player, false);
        fillUpDamageTokens(player);
        game.decreaseLives();
        game.respawnPlayers();
        assertTrue(player.isInBackupState());
    }

    @Test
    public void deadPlayerAreRemovedFromGameTest() {
        // Kill player
        for (int livesTaken = 1; livesTaken <= 3; livesTaken++) {
            fillUpDamageTokens(player);
            player.decrementLifeTokens();
            game.decreaseLives();
        }
        game.removeDeadPlayers();
        assertFalse(game.players.contains(player));
    }

    @Test
    public void playerMovesOnBeltTest() {
        Belt belt = belts.get(0);
        Vector2 beltPosition = belt.getPosition();
        player.setPosition(beltPosition);
        game.activateBelts(false);
        assertNotEquals(beltPosition, player.getPosition());
    }

    @Test
    public void playerMovesInSameDirectionAsBeltTest() {
        Belt belt = belts.get(0);
        Vector2 beltPosition = belt.getPosition();
        Direction beltDirection = belt.getDirection();
        Vector2 newPosition = game.getBoard().getNeighbourPosition(beltPosition, beltDirection);
        player.setPosition(beltPosition);
        game.activateBelts(false);
        assertEquals(newPosition, player.getPosition());
    }

    /**
     * Place player at belt below the corner belt, move belts so player is at the corner belt.
     * The corner belt should rotate player from NORTH to WEST.
     */
    @Test
    public void playerChangesDirectionWhenInACornerOfTheBeltTest() {
        // Found postition in Risky Exhange
        Vector2 fromSouthToNorthBeltPosition = new Vector2(1, 1);
        player.setPosition(fromSouthToNorthBeltPosition);
        player.setDirection(Direction.NORTH);
        // Move player onto corner belt
        game.activateBelts(false);
        // Turn player with corner belt
        game.activateBelts(false);
        assertEquals(Direction.WEST, player.getDirection());
    }

    @Test
    public void moveOneStepWhenOnBeltTest() {
        // Found postition in Risky Exhange. Belt goes east.
        Vector2 startBeltPosition = new Vector2(5, 5);
        player.setPosition(startBeltPosition);
        game.activateBelts(false);
        game.activateBelts(true);
        Vector2 beltMovedToPosition = new Vector2(6, 5);
        assertEquals(beltMovedToPosition, player.getPosition());
    }

    @Test
    public void moveTwoStepOnExpressBeltTest() {
        // Found position in Risky Exhange. Belt goes west
        Vector2 startBeltPosition = new Vector2(8, 6);
        player.setPosition(startBeltPosition);
        game.activateBelts(false);
        game.activateBelts(true);
        Vector2 beltMovedToPosition = new Vector2(6, 6);
        assertEquals(beltMovedToPosition, player.getPosition());
    }

    @Test
    public void repairTileResetsDamageTokensToZeroWhenOneDamageToken() {
        Vector2 repairTilePosition = game.getBoard().getRepairTiles().get(0);
        player.handleDamage();
        player.setPosition(repairTilePosition);
        game.activateRepairTiles();
        assertEquals(0, player.getDamageTokens());
    }

    @Test
    public void repairTileResetsDamageTokenToZeroWhenTenDamageTokens() {
        Vector2 repairTilePosition = game.getBoard().getRepairTiles().get(0);
        for (int i = 0; i < 10; i++) {
            player.handleDamage();
        }
        player.setPosition(repairTilePosition);
        game.activateRepairTiles();
        assertEquals(0, player.getDamageTokens());
    }

    @Test
    public void poweredDownPlayersDoNotGetCardsTest() {
        player.setPoweredDown(true);
        game.dealCards();
        assertTrue(player.getRegisters().getCards().isEmpty());
    }

    @Test
    public void playerPoweringDownBecomesPoweredDownTest() {
        player.setPoweringDown(true);
        game.powerDown();
        assertTrue(player.isPoweredDown());
    }

    @Test
    public void powerDownResetDamageTokensTest() {
        player.handleDamage();
        player.setPoweredDown(true);
        game.powerDown();
        assertEquals(0, player.getDamageTokens());
    }

    @Test
    public void confirmedButtonPoweringDownTest() {
        // TODO: Spiller må sende 5 kort hvis ikke blir det nullpointer
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
        verify(client).sendMessage("2"+ Messages.POWER_UP.toString());
    }

    @Test
    public void sendPowerUpMessageToClient() {
        game.setIsServerToTrue();
        game.setServerThread(serverThread);
        when(mainPlayer.getPlayerNr()).thenReturn(1);
        game.sendPowerUpMessage();
        verify(server).sendToAll("1"+Messages.POWER_UP.toString());

    }

    @Test
    public void poweringUpPlayersHaveNotPressedPowerDownButtonTest() {
        game.addPoweredDownPlayer(mainPlayer);
        // TODO: Denne funksjonen finnes ikke lenger
        //game.powerUpPoweredDownPlayers();
        verify(mainPlayer).setPowerDownNextRound(false);
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
        verify(client).sendMessage("2"+Messages.CONTINUE_POWER_DOWN.toString());
    }


}
