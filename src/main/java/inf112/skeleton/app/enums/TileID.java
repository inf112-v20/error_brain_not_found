package inf112.skeleton.app.enums;

public enum TileID {
    STARTPOS1(121),
    STARTPOS2(122),
    STARTPOS3(123),
    STARTPOS4(124),
    STARTPOS5(129),
    STARTPOS6(130),
    STARTPOS7(131),
    STARTPOS8(133);

    private int id;

    public int getId() {
        return this.id;
    }
    private TileID(int id) {
        this.id = id;
    }
}


