package inf112.skeleton.app.cards;

import inf112.skeleton.app.enums.Rotate;

public class ProgramCard {
    private final int priority;
    private final int distance;
    private final String name;

    private final Rotate rotate;

    public ProgramCard(int priority, int distance, Rotate rotate, String name) {
        this.priority = priority;
        this.distance = distance;
        this.rotate = rotate;
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public boolean equals(ProgramCard other) {
        return this.priority == other.priority &&
                this.distance == other.distance &&
                this.rotate == other.rotate;
    }

    public int getDistance() {
        return distance;
    }

    public Rotate getRotate() {
        return rotate;
    }

    public String toString() {
        return name + ": " + priority;
    }

    public String getName(){return name;}
}
