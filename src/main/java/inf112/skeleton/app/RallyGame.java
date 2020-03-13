package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.screens.GifScreen;
import inf112.skeleton.app.screens.LoadingScreen;

public class RallyGame extends Game {

    public Board board;
    public Deck deck;

    public void create() {
        this.setScreen(new LoadingScreen(this));
        startMusic();
    }

    public void setupGame(String mapPath) {
        this.board = new Board(mapPath, 4);
        this.deck = new Deck();
        setInputProcessor();
    }
    public void setInputProcessor() {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                Player player = board.getPlayer1();
                if (keycode == Input.Keys.RIGHT) {
                    player.setDirection(Direction.EAST);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.LEFT) {
                    player.setDirection(Direction.WEST);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.UP) {
                    player.setDirection(Direction.NORTH);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.DOWN) {
                    player.setDirection(Direction.SOUTH);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                }
                return super.keyDown(keycode);
            }
        });
    }

    public void startMusic() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
        music.play();
    }

    public void setWinScreen() {
        this.screen.dispose();
        this.setScreen(new GifScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        this.screen.dispose();
    }

    public Board getBoard() {
        return this.board;
    }
}
