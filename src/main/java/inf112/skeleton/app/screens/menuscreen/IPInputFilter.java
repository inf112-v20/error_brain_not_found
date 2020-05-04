package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

public class IPInputFilter implements TextFieldFilter {

    private final char[] accepted;

    public IPInputFilter() {
        accepted = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.', 'l', 'o', 'c', 'a', 'h', 's', 't'};
    }

    @Override
    public boolean acceptChar(TextField textField, char c) {
        for (char a : accepted) {
            if (a == c) { return true; }
        }
        return false;
    }
}
