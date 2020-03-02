package inf112.skeleton.app.enums;

public enum TileID {
    STARTPOS1(121),
    STARTPOS2(122),
    STARTPOS3(123),
    STARTPOS4(124),
    STARTPOS5(129),
    STARTPOS6(130),
    STARTPOS7(131),
    STARTPOS8(133),

    EAST_WALL(23),
    SOUTH_WALL(29),
    WEST_WALL(30),
    NORTH_WALL(31),

    EAST_LASER_WALL(46),
    SOUTH_LASER_WALL(37),
    WEST_LASER_WALL(38),
    NORTH_LASER_WALL(45),

    SOUTHEAST_WALL(8),
    NORTHEAST_WALL(16),
    NORTHWEST_WALL(24),
    SOUTHWEST_WALL(32),

    VERTICAL_LASER(47),
    HORIZONTAL_LASER(39),

    WRENCH(14),
    DOUBLE_WRENCH(7),

    PLAYER(137);

    private int id;

    TileID(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
}


