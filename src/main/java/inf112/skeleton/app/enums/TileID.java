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






    private int id;

    public int getId() {
        return this.id;
    }
    private TileID(int id) {
        this.id = id;
    }
}


