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

    RIGHT_WALL(23),
    DOWN_WALL(29),
    LEFT_WALL(30),
    UP_WALL(31),

    RIGHT_LASER_WALL(46),
    DOWN_LASER_WALL(37),
    LEFT_LASER_WALL(38),
    UP_LASER_WALL(45),

    SOUTHEASTWALL(8),
    NORTHEASTWALL(16),
    NORTHWESTWALL(24),
    SOUTHWESTWALL(32),

    VERTICAL_LASER(47),
    HORIZONTAL_LASER(39),

    WRENCH(14),
    DOUBLE_WRENCH(7);

    private final int id;

    public int getId() {
        return this.id;
    }
    private TileID(int id) {
        this.id = id;
    }
}


