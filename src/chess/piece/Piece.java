package chess.piece;

import processing.core.PImage;
import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    public enum Color {
        NONE,
        WHITE, BLACK
    }

    public enum Type {
        NONE,
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    }

    public static final Piece NONE = new Piece(Color.NONE, Type.NONE);

    public final Color color;
    public final Type type;
    public boolean moved;
    public boolean showOnBoard = true;
    public boolean pawnPushedTwice = true;
    private List<String> flags = new ArrayList<>();

    public Piece(Color color, Type type) {
        this.color = color;
        this.type = type;
        this.moved = false;
    }

    // add, remove and check if has custom flags
    public void addFlag(String flag) {
        flags.add(flag);
    }

    public void removeFlag(String flag) {
        flags.remove(flag);
    }

    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    // methods returning booleans if the piece is every color
    public boolean isWhite() {
        return this.color == Color.WHITE;
    }

    public boolean isBlack() {
        return this.color == Color.BLACK;
    }

    // methods returning booleans if the piece is every type
    public boolean isPawn() {
        return this.type == Type.PAWN;
    }

    public boolean isKnight() {
        return this.type == Type.KNIGHT;
    }

    public boolean isBishop() {
        return this.type == Type.BISHOP;
    }

    public boolean isRook() {
        return this.type == Type.ROOK;
    }

    public boolean isQueen() {
        return this.type == Type.QUEEN;
    }

    public boolean isKing() {
        return this.type == Type.KING;
    }

    // check if piece is sliding
    public boolean isSliding() {
        return this.isRook() || this.isBishop() || this.isQueen();
    }

    // static method that returns the piece from fen char
    public static Piece fromFen(char c) {
        switch (c) {
            case 'P':
                return new Piece(Color.WHITE, Type.PAWN);
            case 'p':
                return new Piece(Color.BLACK, Type.PAWN);
            case 'N':
                return new Piece(Color.WHITE, Type.KNIGHT);
            case 'n':
                return new Piece(Color.BLACK, Type.KNIGHT);
            case 'B':
                return new Piece(Color.WHITE, Type.BISHOP);
            case 'b':
                return new Piece(Color.BLACK, Type.BISHOP);
            case 'R':
                return new Piece(Color.WHITE, Type.ROOK);
            case 'r':
                return new Piece(Color.BLACK, Type.ROOK);
            case 'Q':
                return new Piece(Color.WHITE, Type.QUEEN);
            case 'q':
                return new Piece(Color.BLACK, Type.QUEEN);
            case 'K':
                return new Piece(Color.WHITE, Type.KING);
            case 'k':
                return new Piece(Color.BLACK, Type.KING);
            default:
                return null;
        }
    }

    // check if piece is the same type and color
    public boolean isSame(Piece piece) {
        return this.color == piece.color && this.type == piece.type;
    }

    // check if piece is the same color
    public boolean isSameColor(Piece piece) {
        return this.color == piece.color;
    }

    // check if piece is the same type
    public boolean isSameType(Piece piece) {
        return this.type == piece.type;
    }

    // check if piece is none
    public boolean isNone() {
        return this.type == Type.NONE || this.color == Color.NONE;
    }

    // equals method
    public boolean equals(Object o) {
        if (o instanceof Piece) {
            Piece piece = (Piece) o;
            return isSame(piece);
        }
        return false;
    }

    // toFen char
    public char toFen() {
        switch (this.type) {
            case PAWN:
                return this.color == Color.WHITE ? 'P' : 'p';
            case KNIGHT:
                return this.color == Color.WHITE ? 'N' : 'n';
            case BISHOP:
                return this.color == Color.WHITE ? 'B' : 'b';
            case ROOK:
                return this.color == Color.WHITE ? 'R' : 'r';
            case QUEEN:
                return this.color == Color.WHITE ? 'Q' : 'q';
            case KING:
                return this.color == Color.WHITE ? 'K' : 'k';
            default:
                return ' ';
        }
    }

    // toString method
    public String toString() {
        if (this.isNone()) {
            return "NONE";
        }
        if (this.isWhite()) {
            return this.type.toString().toUpperCase();
        }
        return this.type.toString().toLowerCase();
    }
}
