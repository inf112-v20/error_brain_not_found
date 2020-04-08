package inf112.skeleton.app.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.Board;
import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.enums.Rotate;


public class ShowHand implements Screen {
    private Player player;
    private Board board;
    private SpriteBatch batch;
    private Texture[] texture;
    private Texture cards;
    private RallyGame game;
    private ProgramCard[] card;

    public ShowHand(Player player, Board board){
        this.player = player;
        this.board = board;

       // texture = new Texture[player.getSelectedCards().size()];
        texture = new Texture[player.getAllCards().size()];
        batch = new SpriteBatch();
        creatingCardTextures();
    }

    public void getCards(){
        for (int i = 1; i < texture.length;i++){
//            game.mainPlayer.selectCards();
            printCards(this.player.getSelectedCards().get(i));
        }
    }
   /* public void printCards (ProgramCard card){
        if (card.getDistance() >= 1 ) {
            if (card.getDistance() == 1) {
                cards = new Texture(Gdx.files.internal("assets/programCards/Move1.png"));
            } else if (card.getDistance() == 2){
                cards = new Texture(Gdx.files.internal("assets/programCards/Move2.png"));
            }
            else if (card.getDistance() == 3){
                cards = new Texture(Gdx.files.internal("assets/programCards/Move3.png"));
            }
        }
        else if (card.getRotate() == Rotate.LEFT){
            cards = new Texture(Gdx.files.internal("assets/programCards/LeftTurn.png"));
        }
        else if (card.getRotate() == Rotate.RIGHT){
            cards = new Texture(Gdx.files.internal("assets/programCards/RightTurn.png"));
        }
        else if (card.getRotate() == Rotate.UTURN){
            cards = new Texture(Gdx.files.internal("assets/programCards/U-Turn.png"));
        }
    }*/
    public void printCards(ProgramCard card) {
        switch (card.getRotate()) {
            case RIGHT:
                cards = new Texture(Gdx.files.internal("assets/programCards/RightTurn.png"));
                break;
            case LEFT:
                cards = new Texture(Gdx.files.internal("assets/programCards/LeftTurn.png"));
                break;
            case UTURN:
                cards = new Texture(Gdx.files.internal("assets/programCards/U-Turn.png"));
                break;
            case NONE:
                if (card.getDistance() >= 1 ) {
                    if (card.getDistance() == 1) {
                        cards = new Texture(Gdx.files.internal("assets/programCards/Move1.png"));
                    } else if (card.getDistance() == 2){
                        cards = new Texture(Gdx.files.internal("assets/programCards/Move2.png"));
                    }
                    else if (card.getDistance() == 3){
                        cards = new Texture(Gdx.files.internal("assets/programCards/Move3.png"));
                    }
                }
                break;
            default:
                break;
        }
    }

    public void creatingCardTextures(){
        //System.out.println(player.getSelectedCards().size());
        texture = new Texture[player.getSelectedCards().size()];
        getCards();
    }

    public void drawCards(){
        batch.begin();
        for (int i = 0; i < texture.length; i++ ){
          //  System.out.println(texture[i]);
            batch.draw(texture[i],//i *texture[i].getWidth()
                   10+i ,10,20,20);
        }

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        batch.begin();
        drawCards();
        batch.end();
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
