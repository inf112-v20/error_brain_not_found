package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

public class ProgramCard {
    private int priority;
    private int distance;

    private Rotate rotate;

    public ProgramCard(int priority, int distance, Rotate rotate){
        this.priority = priority;
        this.distance = distance;
        this.rotate = rotate;
    }


    public int getPriority() {
        return priority;
    }

    public int getDistance() {
        return distance;
    }

    public Rotate getRotate() {
        return rotate;
    }
}
