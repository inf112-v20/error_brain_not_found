package inf112.skeleton.app.screens.gamescreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ProgramCardSkin {

    private final Skin programCardSkin;

    public ProgramCardSkin() {
        TextureAtlas atlas = new TextureAtlas();

        TextureRegion move1 = new TextureRegion(new Texture("assets/programCards/Move1.jpg"));
        TextureRegion move2 = new TextureRegion(new Texture("assets/programCards/Move2.jpg"));
        TextureRegion move3 = new TextureRegion(new Texture("assets/programCards/Move3.jpg"));
        TextureRegion leftTurn = new TextureRegion(new Texture("assets/programCards/LeftTurn.jpg"));
        TextureRegion rightTurn = new TextureRegion(new Texture("assets/programCards/RightTurn.jpg"));
        TextureRegion UTurn = new TextureRegion(new Texture("assets/programCards/U-turn.jpg"));
        TextureRegion backup = new TextureRegion(new Texture("assets/programCards/BackUp.jpg"));
        TextureRegion powerDown = new TextureRegion(new Texture("assets/programCards/PowerDown.jpg"));

        atlas.addRegion("Move 1", move1);
        atlas.addRegion("Move 2", move2);
        atlas.addRegion("Move 3", move3);
        atlas.addRegion("Left turn", leftTurn);
        atlas.addRegion("Right turn", rightTurn);
        atlas.addRegion("U-turn", UTurn);
        atlas.addRegion("Back up", backup);
        atlas.addRegion("Power down", powerDown);

        this.programCardSkin = new Skin(atlas);
    }

    public Skin getSkins() {
        return programCardSkin;
    }
}
