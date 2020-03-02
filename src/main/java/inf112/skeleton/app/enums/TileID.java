package inf112.skeleton.app.enums;

public enum TileID {
    START_POS1(121),
    START_POS2(122),
    START_POS3(123),
    START_POS4(124),
    START_POS5(129),
    START_POS6(130),
    START_POS7(131),
    START_POS8(133),

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

    PLAYER(137),

    FLAG_1(55),
    FLAG_2(63),
    FLAG_3(71);

    private final int id;

    TileID(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }
}


