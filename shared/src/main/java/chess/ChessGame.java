package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Sets turn to the next teams turn
     *
     * @return Which teams turn it now is
     */
    public TeamColor nextTurn() {
        if(getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
            return TeamColor.BLACK;
        }
        setTeamTurn(TeamColor.WHITE);
        return TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, getBoard());
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(board.getPiece(startPosition) != null){
            Collection<ChessMove> c;
            c = board.getPieceMoves(startPosition);
            // TODO: Filter invalid moves
            return c;
        }
        return null;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        int row = move.getStartPosition().getRow(true);
        int col = move.getStartPosition().getColumn(true);
        TeamColor pieceColor = board.getPieceColor(row,col);
        if(pieceColor != getTeamTurn()) {
            String msg = "Tried to move " + pieceColor + " colored piece on " + getTeamTurn() + " turn";
            throw new InvalidMoveException(msg);
        }
        boolean validated = false;
        Collection<ChessMove> c = validMoves(move.getStartPosition());
        for(ChessMove i : c) {
            if (move.equals(i)) {
                validated = true;
                break;
            }
        }
        if(!validated) {
            String msg = move + " is invalid!";
            throw new InvalidMoveException(msg);
        }
        board.removePiece(move.getEndPosition());
        ChessPiece.PieceType type = move.getPromotionPiece();
        if(type != null){
            board.addPiece(move.getEndPosition(),new ChessPiece(pieceColor,type));
        } else {
            board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
