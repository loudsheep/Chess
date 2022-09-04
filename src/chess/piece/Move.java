package chess.piece;

public class Move {
    public static final int CAPTURE = 1;
    public static final int CASTLE = 2;
    public static final int DOUBLE_PAWN_PUSH = 4;
    public static final int EN_PASSANT = 8;
    public static final int PROMOTION = 16;

    public int fromX;
    public int fromY;
    public int toX;
    public int toY;
    public int flags;
    public int referenceX;
    public int referenceY;

    public Move(int fromX, int fromY, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    public Move(int fromX, int fromY, int toX, int toY, int flags) {
        this(fromX, fromY, toX, toY);
        this.flags = flags;
    }

    // set the reference square
    public void setReferance(int x, int y) {
        referenceX = x;
        referenceY = y;
    }

    // set flags
    public void setFlags(int flags) {
        this.flags = flags;
    }

    // add flags
    public void addFlags(int flags) {
        this.flags |= flags;
    }

    // check if move has flags
    public boolean hasFlags(int flags) {
        return (this.flags & flags) != 0;
    }

    // toString
    public String toString() {
        return "Move from (" + fromX + ", " + fromY + ") to (" + toX + ", " + toY + ")";
    }
}
