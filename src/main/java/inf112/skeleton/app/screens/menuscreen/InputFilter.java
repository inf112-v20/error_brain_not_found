package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

public class InputFilter implements TextFieldFilter {

    private final char[] accepted;

    public InputFilter() {
        accepted = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
    }

    @Override
    public boolean acceptChar(TextField textField, char c) {
        for (char a : accepted) {
            if (a == c) { return true; }
        }
        return false;
    }
}
