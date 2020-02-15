package inf112.skeleton.app.enums;

public enum Direction {
    WEST,
    EAST,
    NORTH,
    SOUTH;

    public Direction turnLeft(){
        return getDirection(NORTH, WEST, SOUTH, EAST);
    }

    public Direction turnRight(){
        return getDirection(SOUTH, EAST, NORTH, WEST);
    }

    /**
     * Help function instead of duplicate code
     * @param baseCaseEAST The direction its supposed to be if the case is EAST.
     * @param baseCaseNORTH The direction its supposed to be if the case is NORTH.
     * @param baseCaseWEST The direction its supposed to be if the case is WEST.
     * @param baseCaseSOUT The direction its supposed to be if the case is SOUTH.
     * @return The right facing after a given turn.
     *
     */
    private Direction getDirection(Direction baseCaseEAST, Direction baseCaseNORTH, Direction baseCaseWEST, Direction baseCaseSOUT) {
        switch (this){
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

