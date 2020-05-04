package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class NumOfPlayersInputFilter implements TextField.TextFieldFilter {
    private final char[] accepted;

    public NumOfPlayersInputFilter() {
        accepted = new char[] {'2', '3', '4', '5', '6', '7', '8'};
    }

    @Override
    public boolean acceptChar(TextField textField, char c) {
        for (char a : accepted) {
            if (a == c) { return true; }
        }
        return false;
    }
}
