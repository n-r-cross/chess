package chess;

import java.util.ArrayList;
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
     */
    public void nextTurn() {
        if(getTeamTurn() == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
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
        if(board.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> c;
        c = board.getPieceMoves(startPosition);
        Collection<ChessMove> valid = new ArrayList<>();
        // Filter moves that put/leave king in check
        for(ChessMove move : c) {
            if(testNotInCheck(board.getPieceColor(startPosition), move)) {
                valid.add(move);
            }
        }

        return valid;
    }

    private Collection<ChessMove> validTestMoves(ChessPosition startPosition, ChessBoard testBoard) {
        if(testBoard.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> c;
        c = testBoard.getPieceMoves(startPosition);
        return c;
    }

    public Collection<ChessMove> allValidMoves(TeamColor color) {
        Collection<ChessMove> c = new ArrayList<>();
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++) {
                if (board.getPieceColor(i, j) == color) {
                    c.addAll(validMoves(new ChessPosition(i, j, true)));
                }
            }
        }
        return c;
    }

    public Collection<ChessMove> allValidTestMoves(TeamColor color, ChessBoard testBoard) {
        Collection<ChessMove> c = new ArrayList<>();
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++) {
                if (testBoard.getPieceColor(i, j) == color) {
                    Collection<ChessMove> valid = validTestMoves(new ChessPosition(i, j, true),testBoard);
                    if(valid != null) {
                        c.addAll(valid);
                    }
                }
            }
        }
        return c;
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
        board.removePiece(move.getStartPosition());
        nextTurn();
    }

    public void makeTestMove(ChessMove move, ChessBoard testBoard) {
        int row = move.getStartPosition().getRow(true);
        int col = move.getStartPosition().getColumn(true);
        TeamColor pieceColor = testBoard.getPieceColor(row,col);
        testBoard.removePiece(move.getEndPosition());
        ChessPiece.PieceType type = move.getPromotionPiece();
        if(type != null){
            testBoard.addPiece(move.getEndPosition(),new ChessPiece(pieceColor,type));
        } else {
            testBoard.addPiece(move.getEndPosition(),testBoard.getPiece(move.getStartPosition()));
        }
        testBoard.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor oppColor = teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> c = allValidMoves(oppColor);
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        for (ChessMove move : c){
            if(move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team would be in check
     * if a given move were completed
     *
     * @param teamColor which team to check for check
     * @param testMove which move to check for check
     * @return True if the specified team would be in
     * check if move were completed (and move is invalid)
     */
    private boolean testNotInCheck(TeamColor teamColor, ChessMove testMove) {
        TeamColor oppColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessBoard testBoard = new ChessBoard(board);
        makeTestMove(testMove,testBoard);
        Collection<ChessMove> c = allValidTestMoves(oppColor,testBoard);
        for (ChessMove move : c){
            if(move.getEndPosition().equals(testBoard.getKingPosition(teamColor))) {
                return false;
            }
        }
        return true;
    }

    private boolean testForValidMove(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = allValidMoves(teamColor);
        for (ChessMove move : teamMoves) {
            if(testNotInCheck(teamColor, move)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return (!testForValidMove(teamColor) && isInCheck(teamColor));
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
