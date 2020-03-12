package inf112.skeleton.app.objects;

import inf112.skeleton.app.enums.Rotate;

public class RotatePad {
    private Rotate rotate;

    public RotatePad(Rotate rotate){
        this.rotate = rotate;
    }

    public Rotate getRotate() {
        return rotate;
    }
}
