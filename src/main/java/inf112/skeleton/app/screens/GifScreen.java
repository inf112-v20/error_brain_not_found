package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import inf112.skeleton.app.GifDecoder;
import inf112.skeleton.app.RallyGame;

public class GifScreen extends StandardScreen {

    private Animation<TextureRegion> animation;
    float elapsed;

    public GifScreen(final RallyGame game) {
        super(game);
        this.animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("assets/images/optimusBoi.gif").read());
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        elapsed += Gdx.graphics.getDeltaTime();
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed), 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();
        if (animation.isAnimationFinished(elapsed)) {
            game.setScreen(new YouWinScreen(game));
        }
    }
}
