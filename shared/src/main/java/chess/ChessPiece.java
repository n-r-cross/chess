package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    // Store color of piece
    ChessGame.TeamColor p_color;
    // Store type of piece
    ChessPiece.PieceType p_type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        p_color = pieceColor;
        p_type = type;
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
        return p_color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return p_type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return p_color == that.p_color && p_type == that.p_type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(p_color, p_type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //
        Collection<ChessMove> c;
        c = new ArrayList<ChessMove>();
        switch(p_type){
            case KING:
                // Generate a possible position
                // Check if position is inBounds
                // Check if piece at position
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        if (ChessPosition.inBounds(myPosition.getRow(true) + i, myPosition.getColumn(true) + j)) {
                            if (true) { // Add test if piece is at position
                                ChessPosition e = new ChessPosition(myPosition.getRow(true) + i, myPosition.getColumn(true) + j);
                                c.add(new ChessMove(myPosition, e, null));
                            }
                        }
                    }
                }
                break;
            case QUEEN:
                // Generate a possible position
                // Check if position is inBounds
                // Check if piece at position
                break;
            case BISHOP:
                // Generate a possible position
                // Check if position is inBounds
                // Check if piece at position
                break;
            case KNIGHT:
                // Generate a possible position
                // Check if position is inBounds
                // Check if piece at position
                break;
            case ROOK:
                // Generate a possible position
                // Check if position is inBounds
                // Check if piece at position
                break;
            case PAWN:
                // Generate a possible position
                // Check if position is inBounds
                // Check if piece at position
                break;
        }
        throw new RuntimeException("Not implemented");

    }
}
