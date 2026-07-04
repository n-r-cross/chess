package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard copy) {
        board = new ChessPiece[8][];
        for (int i = 0; i <= 7; i++) {
            board[i] = Arrays.copyOf(copy.getBoard()[i], copy.getBoard()[i].length);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow(true)][position.getColumn(true)] = piece;
    }

    public void removePiece(ChessPosition pos) {
        int row = pos.getRow(true);
        int col = pos.getColumn(true);
        board[row][col] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow(true)][position.getColumn(true)];
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param row The row to get the piece from (zero-indexed)
     * @param col The column to get the piece from (zero-indexed)
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(int row, int col) {
        return board[row][col];
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    /**
     *
     * @param position The position to get the piece's color from
     * @return The color of the piece at the position on the board, or null if
     * no piece is at that position
     */
    public ChessGame.TeamColor getPieceColor(ChessPosition position) {
        if (board[position.getRow(true)][position.getColumn(true)] == null) {
            return null;
        }
        return board[position.getRow(true)][position.getColumn(true)].getTeamColor();
    }

    /**
     *
     * @param row Row position of piece
     * @param col Column position of piece
     * @return The color of the piece at the position on the board, or null if
     * no piece is at that position
     */
    public ChessGame.TeamColor getPieceColor(int row, int col) {
        if (board[row][col] == null) {
            return null;
        }
        return board[row][col].getTeamColor();
    }

    /**
     * Get the moves that the piece can make
     *
     * @param pos Position of piece to get moves from
     * @return The moves that the piece at the given positions
     * can make
     */
    public Collection<ChessMove> getPieceMoves(ChessPosition pos) {
        return getPiece(pos).pieceMoves(this, pos);
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if ((getPiece(i, j) != null) && (getPiece(i, j).getPieceType() == ChessPiece.PieceType.KING)
                        && (getPiece(i, j).getTeamColor() == color)) {
                    return new ChessPosition(i, j, true);
                }
            }
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Create white pawns
        for (int i = 0; i <= 7; i++) {
            this.addPiece(new ChessPosition(1, i, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        }
        // Create other white pieces
        this.addPiece(new ChessPosition(0, 0, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(0, 7, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(0, 1, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(0, 6, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(0, 2, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(0, 5, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(0, 3, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(0, 4, true), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        // Create black pawns
        for (int i = 0; i <= 7; i++) {
            this.addPiece(new ChessPosition(6, i, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        // Create other black pieces
        this.addPiece(new ChessPosition(7, 0, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7, 7, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        this.addPiece(new ChessPosition(7, 1, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(7, 6, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        this.addPiece(new ChessPosition(7, 2, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(7, 5, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        this.addPiece(new ChessPosition(7, 3, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        this.addPiece(new ChessPosition(7, 4, true), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
