package chess.board;

import chess.piece.HoldingPiece;
import chess.piece.LegalMoves;
import chess.piece.Move;
import chess.piece.Piece;
import processing.core.PApplet;
import processing.core.PImage;
import util.Color;
import vector.Vector2;

import java.util.HashMap;
import java.util.Map;

public class Board {
    public final PApplet sketch;
    public final int width;
    public final float squareSize;
    public final int boardSize;
    public final Vector2 offset;

    private Piece.Color turn = Piece.Color.WHITE;

    private Color lightSquareColor = new Color(238, 238, 210);
    private Color darkSquareColor = new Color(118, 150, 86);

    private Map<Piece, PImage> pieceImages;
    private Piece[][] pieces;

    private HoldingPiece holdingPiece;
    private LegalMoves legalMoves;

    // constructor that takes a PApplet parameter, a width and a boardSize then calculates the squareSize
    public Board(PApplet sketch, int width, int boardSize, Vector2 offset) {
        this.sketch = sketch;
        this.width = width;
        this.boardSize = boardSize;
        this.squareSize = (float) width / boardSize;
        this.offset = offset;

        loadImages();

        // init all pieces to none
        pieces = new Piece[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                pieces[i][j] = Piece.NONE;
            }
        }
    }

    // load chess position from fen string
    public void loadFen(String fen) {
        String[] fenParts = fen.split(" ");
        String[] rows = fenParts[0].split("/");
        int row = 0;

        pieces = new Piece[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                pieces[i][j] = new Piece(Piece.Color.NONE, Piece.Type.NONE);
            }
        }

        for (String rowStr : rows) {
            int col = 0;
            for (char c : rowStr.toCharArray()) {
                if (Character.isDigit(c)) {
                    col += c - '0';
                } else {
                    pieces[col][row] = Piece.fromFen(c);
                    col++;
                }
            }
            row++;
        }
    }

    // load images to pieceImages hashMap
    public void loadImages() {
        pieceImages = new HashMap<>(12);
        pieceImages.put(new Piece(Piece.Color.WHITE, Piece.Type.PAWN), sketch.loadImage("assets/textures/pw480.png"));
        pieceImages.put(new Piece(Piece.Color.WHITE, Piece.Type.ROOK), sketch.loadImage("assets/textures/rw480.png"));
        pieceImages.put(new Piece(Piece.Color.WHITE, Piece.Type.KNIGHT), sketch.loadImage("assets/textures/nw480.png"));
        pieceImages.put(new Piece(Piece.Color.WHITE, Piece.Type.BISHOP), sketch.loadImage("assets/textures/bw480.png"));
        pieceImages.put(new Piece(Piece.Color.WHITE, Piece.Type.QUEEN), sketch.loadImage("assets/textures/qw480.png"));
        pieceImages.put(new Piece(Piece.Color.WHITE, Piece.Type.KING), sketch.loadImage("assets/textures/kw480.png"));

        pieceImages.put(new Piece(Piece.Color.BLACK, Piece.Type.PAWN), sketch.loadImage("assets/textures/pb480.png"));
        pieceImages.put(new Piece(Piece.Color.BLACK, Piece.Type.ROOK), sketch.loadImage("assets/textures/rb480.png"));
        pieceImages.put(new Piece(Piece.Color.BLACK, Piece.Type.KNIGHT), sketch.loadImage("assets/textures/nb480.png"));
        pieceImages.put(new Piece(Piece.Color.BLACK, Piece.Type.BISHOP), sketch.loadImage("assets/textures/bb480.png"));
        pieceImages.put(new Piece(Piece.Color.BLACK, Piece.Type.QUEEN), sketch.loadImage("assets/textures/qb480.png"));
        pieceImages.put(new Piece(Piece.Color.BLACK, Piece.Type.KING), sketch.loadImage("assets/textures/kb480.png"));
    }

    // get image for piece
    public PImage getImage(Piece piece) {
        for (Piece p : pieceImages.keySet()) {
            if (p.equals(piece)) {
                return pieceImages.get(p);
            }
        }
        return null;
    }

    // show legal moves in form of circles on squares
    public void showLegalMoves() {
        if (legalMoves != null) {
            for (Move move : legalMoves.getLegalMoves()) {
                if (move.hasFlags(Move.CAPTURE)) {
                    sketch.stroke(255, 0, 0, 100);
                    sketch.strokeWeight(squareSize / 10);
                    sketch.noFill();

                    sketch.ellipse(move.toX * squareSize + squareSize / 2 + offset.x,
                            move.toY * squareSize + squareSize / 2 + offset.y, squareSize / 1.5f, squareSize / 1.5f);
                } else {
                    sketch.noStroke();
                    sketch.fill(255, 0, 0, 100);
                    sketch.ellipse(move.toX * squareSize + squareSize / 2 + offset.x,
                            move.toY * squareSize + squareSize / 2 + offset.y, squareSize / 3, squareSize / 3);
                }
            }
        }
    }

    // show pieces on the board
    public void showPieces() {
        sketch.imageMode(sketch.CORNER);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (getPiece(i, j) != null && getPiece(i, j).color != Piece.Color.NONE && getPiece(i, j).type != Piece.Type.NONE && getPiece(i, j).showOnBoard) {
                    sketch.image(getImage(getPiece(i, j)), i * squareSize + offset.x, j * squareSize + offset.y, squareSize, squareSize);
                }
            }
        }
    }

    // draw the board
    public void draw() {
        sketch.noStroke();
        sketch.strokeWeight(1);
        sketch.fill(lightSquareColor.r, lightSquareColor.g, lightSquareColor.b);
        sketch.rect(offset.x, offset.y, width, width);
        sketch.fill(darkSquareColor.r, darkSquareColor.g, darkSquareColor.b);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if ((i + j) % 2 != 0) {
                    sketch.rect(i * squareSize + offset.x, j * squareSize + offset.y, squareSize, squareSize);
                }
            }
        }

        showPieces();
        showLegalMoves();
        if (holdingPiece != null) {
            sketch.imageMode(sketch.CENTER);
            sketch.image(getImage(holdingPiece.piece), sketch.mouseX, sketch.mouseY, squareSize, squareSize);
        }
    }

    // get piece from board at position x, y if x and y is not in the board return null
    public Piece getPiece(int x, int y) {
        if (x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
            return null;
        }
        return pieces[x][y];
    }

    // set piece on board at position x, y
    public void setPiece(Piece piece, int x, int y) {
        if (x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
            return;
        }
        pieces[x][y] = piece;
    }

    // find first pieces of type and color on the board and return its position
    public int[] findPiece(Piece.Type type, Piece.Color color) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (getPiece(i, j) != null && getPiece(i, j).type == type && getPiece(i, j).color == color) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    // check if x , y is in the board, include offset
    public boolean inBoard(int x, int y) {
        return x >= offset.x && x <= offset.x + width && y >= offset.y && y <= offset.y + width;
    }

    private boolean isCheckMate(Piece.Color forColor) {
        boolean isCheckMate = true;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Piece piece = getPiece(i, j);
                if (piece != null && piece.color == forColor) {
                    if (LegalMoves.getLegalMoves(piece, i, j, this) != null && LegalMoves.getLegalMoves(piece, i, j,
                            this).getLegalMoves().size() > 0) {
                        isCheckMate = false;
                    }
                }
            }
        }

        return isCheckMate;
    }

    private void movePiece(Move move) {
        if (move.hasFlags(Move.CAPTURE)) {
        }

        if (move.hasFlags(Move.EN_PASSANT)) {
            setPiece(Piece.NONE, move.referenceX, move.referenceY);
        }

        if (move.hasFlags(Move.CASTLE)) {
            if (move.referenceX < move.fromX) {
                setPiece(getPiece(move.referenceX, move.referenceY), move.toX + 1, move.fromY);
                setPiece(Piece.NONE, move.referenceX, move.referenceY);
            } else if (move.referenceX > move.fromX) {
                setPiece(getPiece(move.referenceX, move.referenceY), move.toX - 1, move.fromY);
                setPiece(Piece.NONE, move.referenceX, move.referenceY);
            }
        }

        pieces[move.fromX][move.fromY].pawnPushedTwice = move.hasFlags(Move.DOUBLE_PAWN_PUSH);

        pieces[move.toX][move.toY] = pieces[move.fromX][move.fromY];
        pieces[move.toX][move.toY].moved = true;
        pieces[move.toX][move.toY].showOnBoard = true;
        pieces[move.fromX][move.fromY] = Piece.NONE;

        turn = turn == Piece.Color.WHITE ? Piece.Color.BLACK : Piece.Color.WHITE;

        if (isCheckMate(turn)) {
            System.out.println("Checkmate " + turn + " lost");
        }
    }

    private Move validMove(int x, int y) {
        if (legalMoves == null) {
            return null;
        }

        for (Move move : legalMoves.getLegalMoves()) {
            if (move.toX == x && move.toY == y) {
                return move;
            }
        }
        return null;
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (!inBoard(mouseX, mouseY)) {
            return;
        }

        int boardX = (int) ((mouseX - offset.x) / squareSize);
        int boardY = (int) ((mouseY - offset.y) / squareSize);
        Piece piece = getPiece(boardX, boardY);
        if (piece != null && !piece.isNone() && piece.color == turn) {
            if (piece != null && !piece.isNone()) {
                holdingPiece = new HoldingPiece(piece, mouseX, mouseY, boardX, boardY);
                legalMoves = LegalMoves.getLegalMoves(piece, boardX, boardY, this);
                getPiece(boardX, boardY).showOnBoard = false;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        if (!inBoard(mouseX, mouseY)) {
            if (holdingPiece != null) {
                getPiece(holdingPiece.startPosX, holdingPiece.startPosY).showOnBoard = true;
                holdingPiece = null;
                legalMoves = null;
            }
            return;
        }

        int boardX = (int) ((mouseX - offset.x) / squareSize);
        int boardY = (int) ((mouseY - offset.y) / squareSize);

        if (holdingPiece != null) {
            if (holdingPiece.startPosX == boardX && holdingPiece.startPosY == boardY) {
                holdingPiece.piece.showOnBoard = true;
                holdingPiece = null;
                legalMoves = null;
                return;
            }

            Move move = validMove(boardX, boardY);
            if (move != null) {
                movePiece(move);
            } else {
                getPiece(holdingPiece.startPosX, holdingPiece.startPosY).showOnBoard = true;
            }
            holdingPiece = null;
            legalMoves = null;
        }
    }

    // getters and setters for squares colors
    public Color getLightSquareColor() {
        return lightSquareColor;
    }

    public void setLightSquareColor(Color lightSquareColor) {
        this.lightSquareColor = lightSquareColor;
    }

    public Color getDarkSquareColor() {
        return darkSquareColor;
    }

    public void setDarkSquareColor(Color darkSquareColor) {
        this.darkSquareColor = darkSquareColor;
    }

    // get fen from board
    public String getFen() {
        String fen = "";
        int emptySquares = 0;

        for (int j = 0; j < boardSize; j++) {
            for (int i = 0; i < boardSize; i++) {
                if (getPiece(i, j).type == Piece.Type.NONE) {
                    emptySquares++;
                } else {
                    if (emptySquares > 0) {
                        fen += emptySquares;
                        emptySquares = 0;
                    }
                    fen += getPiece(i, j).toFen();
                }
            }
            if (emptySquares > 0) {
                fen += emptySquares;
                emptySquares = 0;
            }
            if (j < boardSize - 1) {
                fen += "/";
            }
        }
//        fen += " " + turn.toString().toLowerCase();
        return fen;
    }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                String piece = pieces[j][i].toString();
                // string should not be shorter than 6 chars
                while (piece.length() < 6) {
                    piece += " ";
                }

                sb.append(piece).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
