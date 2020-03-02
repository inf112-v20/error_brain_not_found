package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

public class ProgramCard {
    private int priority;
    private int distance;
    private String name;

    private Rotate rotate;

    public ProgramCard(int priority, int distance, Rotate rotate, String name) {
        this.priority = priority;
        this.distance = distance;
        this.rotate = rotate;
        this.name = name;
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

    public String getName() { return name; }
}
