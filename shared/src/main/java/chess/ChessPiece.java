package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    // Store color of piece
    ChessGame.TeamColor color;
    // Store type of piece
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    public ChessGame.TeamColor getOppColor() {
        return this.getTeamColor() == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    private boolean checkPosition(ChessBoard board, int row, int col, boolean canTake) {
        if (ChessPosition.inBounds(row, col)) {
            ChessGame.TeamColor color = board.getPieceColor(row, col);
            if (canTake) {
                return ((color == this.getOppColor()) || (color == null));
            }
            return (color == null);
        }
        return false;
    }

    /**
     *
     * @param board      ChessBoard object
     * @param myPosition Starting position
     * @return list of moves a bishop can make
     */
    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition) {
        // Array to store moves in
        Collection<ChessMove> c;
        c = new ArrayList<>();
        // Easier access
        int row = myPosition.getRow(true);
        int col = myPosition.getColumn(true);
        // Generate a possible position
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = 1; ChessPosition.inBounds(row + k * i, col + k * j); k++) {
                    // Check if piece at position
                    ChessGame.TeamColor color = board.getPieceColor(row + k * i, col + k * j);
                    if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                        ChessPosition e = new ChessPosition(row + k * i, col + k * j, true);
                        c.add(new ChessMove(myPosition, e, null));
                    }
                    if (color != null) {
                        break;
                    }
                }
            }
        }
        return c;
    }

    /**
     *
     * @param board      ChessBoard object
     * @param myPosition Starting position
     * @return list of moves a rook can make
     */
    private Collection<ChessMove> orthogonalMoves(ChessBoard board, ChessPosition myPosition) {
        // Array to store moves in
        Collection<ChessMove> c;
        c = new ArrayList<>();
        // Easier access
        int row = myPosition.getRow(true);
        int col = myPosition.getColumn(true);
        // Generate a possible position
        for (int i = -1; i <= 1; i += 2) {
            switch (i) {
                case -1, 1:
                    for (int j = row + i; (ChessPosition.inBounds(j, col)); j += i) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(j, col);
                        if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(j, col, true);
                            c.add(new ChessMove(myPosition, e, null));
                        }
                        if (color != null) {
                            break;
                        }
                    }
                    for (int j = col + i; (ChessPosition.inBounds(row, j)); j += i) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, j);
                        if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, j, true);
                            c.add(new ChessMove(myPosition, e, null));
                        }
                        if (color != null) {
                            break;
                        }
                    }
                    break;
            }
        }
        return c;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        // Create storage container
        Collection<ChessMove> c;
        c = new ArrayList<>();
        // Easier access
        int row = myPosition.getRow(true);
        int col = myPosition.getColumn(true);
        // Generate a possible position
        for (int i = -2; i <= 2; i++) {
            int k = abs(i) == 2 ? 1 : 2;
            for (int j = -k; j <= k; j += (2 * k)) {
                if (i == 0) {
                    break;
                }
                // System.out.println("R: " + row + " | C: " + col);
                if (checkPosition(board, row + i, col + j, true)) {
                    ChessPosition e = new ChessPosition(row + i, col + j, true);
                    c.add(new ChessMove(myPosition, e, null));
                }
            }
        }
        return c;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        // Create storage container
        Collection<ChessMove> c;
        c = new ArrayList<>();
        // Easier access
        int row = myPosition.getRow(true);
        int col = myPosition.getColumn(true);
        // Switch based on PieceColor
        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // Generate and check a possible position
            if (checkPosition(board, row + 1, col, false)) {
                ChessPosition e = new ChessPosition(row + 1, col, true);
                if ((row + 1) == 7) {
                    c.addAll(pawnPromotionMoves(myPosition, e));
                } else {
                    c.add(new ChessMove(myPosition, e, null));
                }
            }
            if ((row == 1) && (board.getPieceColor(row + 1, col) == null) && (checkPosition(board, row + 2, col, false))) {
                ChessPosition e = new ChessPosition(row + 2, col, true);
                c.add(new ChessMove(myPosition, e, null));
            }
            // Generate and check a diagonal position
            if (ChessPosition.inBounds(row + 1, col + 1)) {
                // Check if piece at position
                ChessGame.TeamColor color = board.getPieceColor(row + 1, col + 1);
                if (color == ChessGame.TeamColor.BLACK) { // Add test if piece is at position
                    ChessPosition e = new ChessPosition(row + 1, col + 1, true);
                    if ((row + 1) == 7) {
                        c.addAll(pawnPromotionMoves(myPosition, e));
                    } else {
                        c.add(new ChessMove(myPosition, e, null));
                    }
                }
            }
            // Generate and check a possible position
            if (ChessPosition.inBounds(row + 1, col - 1)) {
                // Check if piece at position
                ChessGame.TeamColor color = board.getPieceColor(row + 1, col - 1);
                if (color == ChessGame.TeamColor.BLACK) { // Add test if piece is at position
                    ChessPosition e = new ChessPosition(row + 1, col - 1, true);
                    if ((row + 1) == 7) {
                        c.addAll(pawnPromotionMoves(myPosition, e));
                    } else {
                        c.add(new ChessMove(myPosition, e, null));
                    }
                }
            }
        } else {
            // Generate and check a possible position
            if (checkPosition(board, row - 1, col, false)) {
                ChessPosition e = new ChessPosition(row - 1, col, true);
                if ((row - 1) == 0) {
                    c.addAll(pawnPromotionMoves(myPosition, e));
                } else {
                    c.add(new ChessMove(myPosition, e, null));
                }
            }
            if ((row == 6) && (board.getPieceColor(row - 1, col) == null) && (checkPosition(board, row - 2, col, false))) {
                ChessPosition e = new ChessPosition(row - 2, col, true);
                c.add(new ChessMove(myPosition, e, null));
            }
            // Generate and check a possible position
            if (ChessPosition.inBounds(row - 1, col + 1)) {
                // Check if piece at position
                ChessGame.TeamColor color = board.getPieceColor(row - 1, col + 1);
                if (color == ChessGame.TeamColor.WHITE) { // Add test if piece is at position
                    ChessPosition e = new ChessPosition(row - 1, col + 1, true);
                    if ((row - 1) == 0) {
                        c.addAll(pawnPromotionMoves(myPosition, e));
                    } else {
                        c.add(new ChessMove(myPosition, e, null));
                    }
                }
            }
            // Generate and check a possible position
            if (ChessPosition.inBounds(row - 1, col - 1)) {
                // Check if piece at position
                ChessGame.TeamColor color = board.getPieceColor(row - 1, col - 1);
                if (color == ChessGame.TeamColor.WHITE) { // Add test if piece is at position
                    ChessPosition e = new ChessPosition(row - 1, col - 1, true);
                    if ((row - 1) == 0) {
                        c.addAll(pawnPromotionMoves(myPosition, e));
                    } else {
                        c.add(new ChessMove(myPosition, e, null));
                    }
                }
            }
        }
        return c;
    }

    private Collection<ChessMove> pawnPromotionMoves(ChessPosition myPos, ChessPosition newPos) {
        // Create storage container
        Collection<ChessMove> c;
        c = new ArrayList<>();
        c.add(new ChessMove(myPos, newPos, PieceType.ROOK));
        c.add(new ChessMove(myPos, newPos, PieceType.BISHOP));
        c.add(new ChessMove(myPos, newPos, PieceType.KNIGHT));
        c.add(new ChessMove(myPos, newPos, PieceType.QUEEN));
        return c;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Create storage container
        Collection<ChessMove> c;
        c = new ArrayList<>();
        // Easy access to position
        int row = myPosition.getRow(true);
        int col = myPosition.getColumn(true);
        switch (type) {
            case KING:
                // Generate a possible position
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (checkPosition(board, row + i, col + j, true)) {
                            ChessPosition e = new ChessPosition(row + i, col + j, true);
                            c.add(new ChessMove(myPosition, e, null));
                        }
                    }
                }
                break;
            case QUEEN:
                c.addAll(diagonalMoves(board, myPosition));
                c.addAll(orthogonalMoves(board, myPosition));
                break;
            case BISHOP:
                c.addAll(diagonalMoves(board, myPosition));
                break;
            case KNIGHT:
                c.addAll(knightMoves(board, myPosition));
                break;
            case ROOK:
                c.addAll(orthogonalMoves(board, myPosition));
                break;
            case PAWN:
                c.addAll(pawnMoves(board, myPosition));
                break;
        }
        return c;
    }
}
