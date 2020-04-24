package inf112.skeleton.app.screens.gifscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.YouWinScreen;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;

public class GifScreen extends StandardScreen {

    private final Animation<TextureRegion> animation;

    public GifScreen(final RallyGame game) {
        super(game);
        this.animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/images/optimusBoi.gif").read());
    }

    @Override
    public void render(float v) {
        super.render(v);

        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
        if (animation.isAnimationFinished(elapsed)) {
            game.setScreen(new YouWinScreen(game));
        }
    }
}
