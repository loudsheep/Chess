package chess.piece;

public class HoldingPiece {
    public Piece piece;
    public int x;
    public int y;
    public int startPosX, startPosY;
    public int releasePosX, releasePosY;

    public HoldingPiece(Piece piece, int x, int y, int startPosX, int startPosY) {
        this.piece = piece;
        this.x = x;
        this.y = y;
        this.startPosX = startPosX;
        this.startPosY = startPosY;
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
