package chess.piece;

import chess.board.Board;

import java.util.ArrayList;
import java.util.List;

public class LegalMoves {
    private List<Move> legalMoves;

    public LegalMoves() {
        this.legalMoves = new ArrayList<>();
    }

    public void add(Move move) {
        legalMoves.add(move);
    }

    // add move to the list of legal move
    public void add(int fromX, int fromY, int toX, int toY) {
        legalMoves.add(new Move(fromX, fromY, toX, toY));
    }

    public void add(int fromX, int fromY, int toX, int toY, int flags) {
        legalMoves.add(new Move(fromX, fromY, toX, toY, flags));
    }

    // add list of move to the list of legal move
    public void addAll(List<Move> moves) {
        legalMoves.addAll(moves);
    }

    // add legal moves to legal moves
    public void addAll(LegalMoves legalMoves) {
        this.legalMoves.addAll(legalMoves.getLegalMoves());
    }

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public void clear() {
        legalMoves.clear();
    }

    public boolean isEmpty() {
        return legalMoves.isEmpty();
    }

    // static //
    // return an offset array for the direction
    private static int[] getOffset(int direction) {
        // offsets are 0-top, 1-top right, 2-right, 3-bottom right, 4-bottom, 5-bottom left, 6-left, 7-top left
        // first index is x, second is y
        int[] offset = new int[2];
        switch (direction) {
            case 0:
                offset[0] = 0;
                offset[1] = -1;
                break;
            case 1:
                offset[0] = 1;
                offset[1] = -1;
                break;
            case 2:
                offset[0] = 1;
                offset[1] = 0;
                break;
            case 3:
                offset[0] = 1;
                offset[1] = 1;
                break;
            case 4:
                offset[0] = 0;
                offset[1] = 1;
                break;
            case 5:
                offset[0] = -1;
                offset[1] = 1;
                break;
            case 6:
                offset[0] = -1;
                offset[1] = 0;
                break;
            case 7:
                offset[0] = -1;
                offset[1] = -1;
                break;
        }
        return offset;
    }

    // check if there is a check at the position
    public static boolean isCheck(int x, int y, Piece.Color color, Board board) {
        int destinationX;
        int destinationY;
        Piece pieceAtDestination;

        // check for rooks and queens
        for (int direction = 0; direction < 8; direction += 2) {
            int[] offset = getOffset(direction);

            for (int i = 1; i < board.boardSize; i++) {
                destinationX = x + offset[0] * i;
                destinationY = y + offset[1] * i;
                pieceAtDestination = board.getPiece(destinationX, destinationY);
                if (pieceAtDestination == null) break;

                if (pieceAtDestination.isNone()) continue;
                if (pieceAtDestination.color == color) break;

                if (pieceAtDestination.isRook() || pieceAtDestination.isQueen()) {
                    return true;
                }
                break;
            }
        }

        // check for bishops and queens
        for (int direction = 1; direction < 8; direction += 2) {
            int[] offset = getOffset(direction);

            for (int i = 1; i < board.boardSize; i++) {
                destinationX = x + offset[0] * i;
                destinationY = y + offset[1] * i;
                pieceAtDestination = board.getPiece(destinationX, destinationY);

                if (pieceAtDestination == null) break;

                if (pieceAtDestination.isNone()) continue;
                if (pieceAtDestination.color == color) break;

                if (pieceAtDestination.isBishop() || pieceAtDestination.isQueen()) {
                    return true;
                }
                break;
            }
        }

        // check for knights
        for (int[] offset : KNIGHT_OFFSETS) {
            destinationX = x + offset[0];
            destinationY = y + offset[1];
            pieceAtDestination = board.getPiece(destinationX, destinationY);

            if (pieceAtDestination == null) continue;
            if (pieceAtDestination.isNone()) continue;
            if (pieceAtDestination.color == color) continue;

            if (pieceAtDestination.isKnight()) {
                return true;
            }
        }

        // check for pawns
        for (int direction = 1; direction < 8; direction += 2) {
            int[] offset = getOffset(direction);

            destinationX = x + offset[0];
            destinationY = y + offset[1];
            pieceAtDestination = board.getPiece(destinationX, destinationY);

            if (pieceAtDestination == null) continue;
            if (pieceAtDestination.isNone()) continue;
            if (pieceAtDestination.color == color) break;

            if (pieceAtDestination.isPawn()) {
                if (color == Piece.Color.WHITE) {
                    if (destinationY < y) {
                        return true;
                    }
                } else {
                    if (destinationY > y) {
                        return true;
                    }
                }
            }
        }

        // check for king
        for (int i = 0; i < 8; i++) {
            int[] offset = getOffset(i);
            destinationX = x + offset[0];
            destinationY = y + offset[1];
            pieceAtDestination = board.getPiece(destinationX, destinationY);

            if (pieceAtDestination == null) continue;
            if (pieceAtDestination.isNone()) continue;
            if (pieceAtDestination.color == color) break;

            if (pieceAtDestination.isKing()) {
                return true;
            }
        }

        return false;
    }

    // check if is in check after move
    public static boolean isCheckAfterMove(int x, int y, Piece.Color color, Move move, Board board) {
        Piece referencePiece = null;
        if (move.hasFlags(Move.EN_PASSANT)) {
            referencePiece = board.getPiece(move.referenceX, move.referenceY);
            board.setPiece(Piece.NONE, move.referenceX, move.referenceY);
        }

        Piece pieceAtDestination = board.getPiece(move.toX, move.toY);
        Piece pieceAtSource = board.getPiece(move.fromX, move.fromY);

        board.setPiece(pieceAtSource, move.toX, move.toY);
        board.setPiece(Piece.NONE, move.fromX, move.fromY);

        boolean isCheck = isCheck(x, y, color, board);

        board.setPiece(pieceAtSource, move.fromX, move.fromY);
        board.setPiece(pieceAtDestination, move.toX, move.toY);

        if (move.hasFlags(Move.EN_PASSANT)) {
            board.setPiece(referencePiece, move.referenceX, move.referenceY);
        }

        return isCheck;
    }

    private static LegalMoves getLegalMovesForSlidingPiece(Piece piece, int x, int y, Board board) {
        LegalMoves legalMoves = new LegalMoves();
        Piece pieceAtDestination;
        int destinationX;
        int destinationY;

        int startDirection = piece.isBishop() ? 1 : 0;
        int directionStep = (piece.isBishop() || piece.isRook()) ? 2 : 1;

        for (int direction = startDirection; direction < 8; direction += directionStep) {

            for (int j = 1; j < board.boardSize; j++) {
                int[] offset = getOffset(direction);
                destinationX = x + offset[0] * j;
                destinationY = y + offset[1] * j;
                pieceAtDestination = board.getPiece(destinationX, destinationY);

                if (pieceAtDestination == null || pieceAtDestination.isSameColor(piece)) {
                    break;
                }

                Move move = new Move(x, y, destinationX, destinationY);

                int[] kingPos = board.findPiece(Piece.Type.KING, piece.color);
                boolean isInCheck = isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board);

                if (!pieceAtDestination.isSameColor(piece) && pieceAtDestination.color != Piece.Color.NONE && !isInCheck) {
                    move.setReferance(destinationX, destinationY);
                    move.addFlags(Move.CAPTURE);
                    legalMoves.add(move);
                    break;
                } else if (pieceAtDestination.isSameColor(piece) && pieceAtDestination.color != Piece.Color.NONE) {
                    break;
                }

                if (isInCheck) continue;

                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    // array of offsets for knight
    private static final int[][] KNIGHT_OFFSETS = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

    private static LegalMoves getLegalMovesForKnight(Piece piece, int x, int y, Board board) {
        LegalMoves legalMoves = new LegalMoves();
        Piece pieceAtDestination;
        int destinationX;
        int destinationY;

        for (int[] offset : KNIGHT_OFFSETS) {
            destinationX = x + offset[0];
            destinationY = y + offset[1];
            pieceAtDestination = board.getPiece(destinationX, destinationY);
            if (pieceAtDestination == null || pieceAtDestination.isSameColor(piece)) continue;

            Move move = new Move(x, y, destinationX, destinationY);
            int[] kingPos = board.findPiece(Piece.Type.KING, piece.color);
            if (pieceAtDestination.isNone()) {
                if (!isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board)) {
                    legalMoves.add(move);
                }
            } else {
                move.setReferance(destinationX, destinationY);
                move.addFlags(Move.CAPTURE);
                if (!isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board)) {
                    legalMoves.add(move);
                }
            }
        }
        return legalMoves;
    }

    private static LegalMoves getLegalMovesForKing(Piece piece, int x, int y, Board board) {
        LegalMoves legalMoves = new LegalMoves();
        Piece pieceAtDestination;
        int destinationX;
        int destinationY;

        for (int i = 0; i < 8; i++) {
            destinationX = x + getOffset(i)[0];
            destinationY = y + getOffset(i)[1];
            pieceAtDestination = board.getPiece(destinationX, destinationY);
            if (pieceAtDestination == null) continue;

            Move move = new Move(x, y, destinationX, destinationY);

            if (isCheck(destinationX, destinationY, piece.color, board) || isCheckAfterMove(destinationX, destinationY, piece.color, move, board))
                continue;


            if (pieceAtDestination.isNone()) {
                legalMoves.add(move);
            } else if (!pieceAtDestination.isSameColor(piece)) {
                move.addFlags(Move.CAPTURE);
                legalMoves.add(move);
            }
        }

        legalMoves.addAll(getLegalMovesForCastling(piece, x, y, board));

        return legalMoves;
    }

    // get legal move for castling
    private static LegalMoves getLegalMovesForCastling(Piece piece, int x, int y, Board board) {
        LegalMoves legalMoves = new LegalMoves();
        if (piece.type != Piece.Type.KING) return legalMoves;
        if (piece.moved) return legalMoves;
        if (isCheck(x, y, piece.color, board)) return legalMoves;

        int rook1X = 0;
        int rook2X = 7;

        for (int i = x - 1; i >= 0; i--) {
            if (board.getPiece(i, y) != null) {
                if (!board.getPiece(i, y).isNone() || isCheck(i, y, piece.color, board)) {
                    rook1X = i;
                    break;
                }
            }
        }
        for (int i = x + 1; i < board.width; i++) {
            if (board.getPiece(i, y) != null) {
                if (!board.getPiece(i, y).isNone() || isCheck(i, y, piece.color, board)) {
                    rook2X = i;
                    break;
                }
            }
        }

        if (rook1X == 0 && board.getPiece(rook1X, y).isRook() && !board.getPiece(rook1X, y).moved && board.getPiece(rook1X, y).isSameColor(piece)) {
            Move move = new Move(x, y, x - 2, y, Move.CASTLE);
            move.setReferance(rook1X, y);
            legalMoves.add(move);
        }

        if (rook2X == 7 && board.getPiece(rook2X, y).isRook() && !board.getPiece(rook2X, y).moved && board.getPiece(rook2X, y).isSameColor(piece)) {
            Move move = new Move(x, y, x + 2, y, Move.CASTLE);
            move.setReferance(rook2X, y);
            legalMoves.add(move);
        }

        return legalMoves;
    }

    private static LegalMoves getLegalMovesForPawn(Piece piece, int x, int y, Board board) {
        LegalMoves legalMoves = new LegalMoves();
        Piece pieceAtDestination;
        int destinationX;
        int destinationY;

        int direction = piece.color == Piece.Color.WHITE ? -1 : 1;

        // check for one step forward
        destinationX = x;
        destinationY = y + direction;
        pieceAtDestination = board.getPiece(destinationX, destinationY);
        if (pieceAtDestination != null && pieceAtDestination.isNone()) {
            Move move = new Move(x, y, destinationX, destinationY);
            int[] kingPos = board.findPiece(Piece.Type.KING, piece.color);
            if (!isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board)) {
                legalMoves.add(move);
            }
        }

        // check for two-step forward
        if (!board.getPiece(x, y).moved) {
            destinationX = x;
            destinationY = y + direction;
            pieceAtDestination = board.getPiece(destinationX, destinationY);
            if (pieceAtDestination != null && pieceAtDestination.isNone()) {
                destinationX = x;
                destinationY = y + 2 * direction;
                pieceAtDestination = board.getPiece(destinationX, destinationY);
                if (pieceAtDestination != null && pieceAtDestination.isNone()) {
                    Move move = new Move(x, y, destinationX, destinationY, Move.DOUBLE_PAWN_PUSH);
                    int[] kingPos = board.findPiece(Piece.Type.KING, piece.color);
                    if (!isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board)) {
                        legalMoves.add(move);
                    }
                }
            }
        }

        // check for capture
        for (int i = -1; i <= 1; i += 2) {
            destinationX = x + i;
            destinationY = y + direction;
            pieceAtDestination = board.getPiece(destinationX, destinationY);
            if (pieceAtDestination != null && !pieceAtDestination.isNone() && !pieceAtDestination.isSameColor(piece)) {
                Move move = new Move(x, y, destinationX, destinationY, Move.CAPTURE);
                int[] kingPos = board.findPiece(Piece.Type.KING, piece.color);
                if (!isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board)) {
                    legalMoves.add(move);
                }
            }
        }

        // check for en passant
        if (board.getPiece(x, y).moved) {
            for (int i = -1; i <= 1; i += 2) {
                destinationX = x + i;
                destinationY = y;
                pieceAtDestination = board.getPiece(destinationX, destinationY);
                if (pieceAtDestination != null && !pieceAtDestination.isSameColor(piece)) {
                    if (pieceAtDestination.isPawn() && pieceAtDestination.pawnPushedTwice) {
                        if (board.getPiece(destinationX, destinationY + direction) == null || board.getPiece(destinationX, destinationY + direction).isNone()) {
                            Move move = new Move(x, y, destinationX, destinationY + direction, Move.EN_PASSANT);
                            move.setReferance(destinationX, destinationY);
                            int[] kingPos = board.findPiece(Piece.Type.KING, piece.color);
                            if (!isCheckAfterMove(kingPos[0], kingPos[1], piece.color, move, board)) {
                                legalMoves.add(move);
                            }
                        }
                    }
                }
            }
        }

        return legalMoves;
    }

    public static LegalMoves getLegalMoves(Piece piece, int x, int y, Board board) {
        if (piece.isSliding()) {
            return getLegalMovesForSlidingPiece(piece, x, y, board);
        } else if (piece.isKnight()) {
            return getLegalMovesForKnight(piece, x, y, board);
        } else if (piece.isKing()) {
            return getLegalMovesForKing(piece, x, y, board);
        } else if (piece.isPawn()) {
            return getLegalMovesForPawn(piece, x, y, board);
        }

        return null;
    }
}
