package inf112.skeleton.app.enums;

public enum Direction {
    WEST,
    EAST,
    NORTH,
    SOUTH;

    /**
     * Turns the {@link Direction} left.
     *
     * @return {@link Direction} after left transformation.
     */
    public Direction turnLeft() {
        return getDirection(NORTH, WEST, SOUTH, EAST);
    }

    /**
     * Turns the {@link Direction} right.
     *
     * @return {@link Direction} after right transformation.
     */
    public Direction turnRight() {
        return getDirection(SOUTH, EAST, NORTH, WEST);
    }

    /**
     * Help function instead of duplicate code
     *
     * @param baseCaseEAST  The direction it's supposed to be if the case is EAST.
     * @param baseCaseNORTH The direction it's supposed to be if the case is NORTH.
     * @param baseCaseWEST  The direction it's supposed to be if the case is WEST.
     * @param baseCaseSOUT  The direction it's supposed to be if the case is SOUTH.
     * @return The right facing after a given transformation.
     */
    private Direction getDirection(Direction baseCaseEAST, Direction baseCaseNORTH, Direction baseCaseWEST, Direction baseCaseSOUT) {
        switch (this) {
            case EAST:
                return baseCaseEAST;
            case NORTH:
                return baseCaseNORTH;
            case WEST:
                return baseCaseWEST;
            case SOUTH:
                return baseCaseSOUT;
            default:
                return null;
        }
    }
}

