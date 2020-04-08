package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.Board;
import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.ShowHand;

public class GameScreen extends StandardScreen {

<<<<<<< HEAD
    private TiledMapRenderer mapRenderer;
    private Texture lifeTokens;
    private Texture damageTokens;
    private Texture confirmProgramCardsButton = new Texture(Gdx.files.internal("assets/images/ConfirmButton.png"));
    private Texture confirmProgramCardsNotReadyButton = new Texture(Gdx.files.internal("assets/images/ConfirmButtonNotReady.png"));
=======
    private final TiledMapRenderer mapRenderer;
    private final Texture lifeTokens;
    private final Texture damageTokens;
>>>>>>> 75f226310cb9276c7a6924766dffacb08fdac1c3
    private float tokensX;
    private float lifeTokensY;
    private float damageTokensY;
    private float tokensSize;
    private ShowHand showHand;
    private Board board;
    private Player player;
    private Texture[] texture;
    private Texture cards;

    public GameScreen(final RallyGame game) {
        super(game);

        lifeTokens = new Texture("assets/images/lifeToken.png");
        damageTokens = new Texture("assets/images/damageToken.png");

        super.camera.setToOrtho(false, game.board.getWidth() * 400, game.board.getHeight() * 400);
        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getMap());
        mapRenderer.setView(camera);
        updateTokens();
        showHand = new ShowHand(game.currentPlayer,board);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderSettings(v);

        updateTokens();
        mapRenderer.render();
        batch.begin();
        renderLifeTokens();
        renderDamageTokens();
        if (chooseStartingProgrammingCards()){
            renderStartProgramCards();
            // TODO: draw(confirmProgramCardsNotReadyButton) if cards are chosen, else draw(confirmProgramCardsButton)
            // TODO: if confirmProgramCardsButton are clicked, dispose/make chooseStartingProgramCards false and give player the cards
        }
        batch.draw(confirmProgramCardsButton, (float) (Gdx.graphics.getWidth() / 1.2),10,tokensSize,tokensSize);

        batch.end();
    }
    private boolean chooseStartingProgrammingCards() {
        return game.gameIsRunning;
    }

    public void updateTokens() {
        tokensSize = camera.viewportHeight / 8;
        tokensX = 10;
        lifeTokensY = camera.viewportHeight - tokensSize;
        damageTokensY = lifeTokensY - tokensSize;
    }

    public void renderDamageTokens() {
        for (int i = 0; i < game.mainPlayer.getDamageTokens(); i++) {
            batch.draw(damageTokens, tokensX + i * tokensSize, damageTokensY, tokensSize, tokensSize);
        }
    }

    public void renderLifeTokens() {
        for (int i = 0; i < game.mainPlayer.getLifeTokens(); i++) {
            batch.draw(lifeTokens, tokensX + i * tokensSize, lifeTokensY, tokensSize, tokensSize);
        }
    }
    private void drawConfirmButton(){

    }

    public void renderStartProgramCards(){

        for (int i = 0; i <9; i++){
            printAllCards(game.mainPlayer.getAllCards().get(i));
            //texture = new Texture[player.getAllCards().size()];
            //System.out.println(i);
            batch.draw(this.cards,i * camera.viewportHeight/5 , camera.viewportHeight-140,  (tokensSize*2)-30,tokensSize*2);
                    //110*i+180,camera.viewportHeight-camera.viewportHeight/4,camera.viewportHeight/5,camera.viewportHeight/4);
        }

    }

    public void printAllCards(ProgramCard card) {
        switch (card.getRotate()) {
            case RIGHT:
                cards = new Texture(Gdx.files.internal("assets/programCards/RightTurn.jpg"));
                break;
            case LEFT:
                cards = new Texture(Gdx.files.internal("assets/programCards/LeftTurn.jpg"));
                break;
            case UTURN:
                cards = new Texture(Gdx.files.internal("assets/programCards/U-Turn.jpg"));
                break;
            case NONE:
                if (card.getDistance() >= 1 ) {
                    if (card.getDistance() == 1) {
                        cards = new Texture(Gdx.files.internal("assets/programCards/Move1.jpg"));
                    } else if (card.getDistance() == 2){
                        cards = new Texture(Gdx.files.internal("assets/programCards/Move2.jpg"));
                    }
                    else if (card.getDistance() == 3){
                        cards = new Texture(Gdx.files.internal("assets/programCards/Move3.jpg"));
                    }
                }
                break;
            default:
                break;
        }
    }
}
