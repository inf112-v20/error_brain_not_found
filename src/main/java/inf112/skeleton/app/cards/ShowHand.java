package inf112.skeleton.app.cards;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Board;
import inf112.skeleton.app.Player;

public class ShowHand implements Screen {
    private Player player;
    private Board board;
    private SpriteBatch batch;
    private Texture[] texture;
    private Texture cards;

    public ShowHand(Player player, Board board){
        this.player = player;
        this.board = board;

        texture = new Texture[player.getSelectedCards().size()];
        batch = new SpriteBatch();
    }
    public void printCards(){
        System.out.println(player.getSelectedCards());
        for (int i = 0; i < texture.length;i++){

        }
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
