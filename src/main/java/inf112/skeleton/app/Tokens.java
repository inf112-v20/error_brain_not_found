package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tokens {
    private Player player;
    private RallyGame game;
    private Board board;
    private int tokens;

    public Texture lifeTokens = new Texture("assets/images/lifeToken.png");
    public Texture damageTokens = new Texture("assets/images/damageToken.png");

    SpriteBatch batch = new SpriteBatch();

    public Tokens (Player player){
        this.player = player;
        //this.tokens = player.getLifeTokens();
        //this.tokens = tokens;
//        System.out.println(player.getLifeTokens());

    }
    public void render(){
        batch.begin();
        for (int i =1; i <= tokens; i++){
            batch.draw(lifeTokens,i*15,i*2);
        }
        batch.end();
    }
}


