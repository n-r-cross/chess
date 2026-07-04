package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;

    // Store whether pieces have moved to track castling
    private boolean whiteKingMoved = false;
    private boolean whiteLeftRookMoved = false;
    private boolean whiteRightRookMoved = false;
    private boolean blackKingMoved = false;
    private boolean blackLeftRookMoved = false;
    private boolean blackRightRookMoved = false;


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
        if (getTeamTurn() == TeamColor.WHITE) {
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

    private ChessMove checkCastlingMove(ChessPosition kingPos, ChessPosition rookPos) {
        int direction = rookPos.getColumn(true) - kingPos.getColumn(true);
        direction = direction / abs(direction);
        // Is there a piece in between the king and the rook?
        for (int i = kingPos.getColumn(true) + direction;
             i != rookPos.getColumn(true); i += direction) {
            if (board.getPiece(kingPos.getRow(true), i) != null) {
                return null;
            }
        }
        // Is king in check?
        if (testInCheck(board.getPieceColor(kingPos))) {
            return null;
        }
        // Would king be in check one space over?
        ChessPosition oneOver = new ChessPosition(kingPos.getRow(), kingPos.getColumn() + direction);
        if (!testNotInCheck(board.getPieceColor(kingPos), new ChessMove(kingPos, oneOver, null))) {
            return null;
        }
        // Would king be in check two spaces over?
        ChessPosition twoOver = new ChessPosition(kingPos.getRow(), kingPos.getColumn() + 2 * direction);
        if (!testNotInCheck(board.getPieceColor(kingPos), new ChessMove(kingPos, twoOver, null))) {
            return null;
        }
        return new ChessMove(kingPos, twoOver, null);
    }

    private Collection<ChessMove> validCastlingMoves(ChessPosition startPosition) {
        Collection<ChessMove> c = new ArrayList<>();
        // Figure out the color of the king
        if (board.getPiece(startPosition).getTeamColor() == TeamColor.WHITE) {
            // Check if king has already moved
            if (whiteKingMoved) {
                return c;
            }
            // Check if left rook moved
            if (!whiteLeftRookMoved) {
                ChessMove move = checkCastlingMove(startPosition, new ChessPosition(0, 0, true));
                if (move != null) {
                    c.add(move);
                }
            }
            // Check if right rook moved
            if (!whiteRightRookMoved) {
                ChessMove move = checkCastlingMove(startPosition, new ChessPosition(0, 7, true));
                if (move != null) {
                    c.add(move);
                }
            }
        } else {
            // Check if king has already moved
            if (blackKingMoved) {
                return c;
            }
            // Check if left rook moved
            if (!blackLeftRookMoved) {
                ChessMove move = checkCastlingMove(startPosition, new ChessPosition(7, 0, true));
                if (move != null) {
                    c.add(move);
                }
            }
            // Check if right rook moved
            if (!blackRightRookMoved) {
                ChessMove move = checkCastlingMove(startPosition, new ChessPosition(7, 7, true));
                if (move != null) {
                    c.add(move);
                }
            }
        }
        // Return castling moves
        return c;
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> c;
        c = board.getPieceMoves(startPosition);
        ChessPiece.PieceType type = board.getPiece(startPosition).getPieceType();
        // If piece is a king and is at one of the king starting positions, add castling moves
        if ((type.equals(ChessPiece.PieceType.KING)) &&
                ((startPosition.equals(new ChessPosition(0, 4, true)))
                        || (startPosition.equals(new ChessPosition(7, 4, true))))) {
            c.addAll(validCastlingMoves(startPosition));
        }
        // TODO: if piece is a pawn and en passant was triggered, add en passant moves
        Collection<ChessMove> valid = new ArrayList<>();
        // Filter moves that put/leave king in check
        for (ChessMove move : c) {
            if (testNotInCheck(board.getPieceColor(startPosition), move)) {
                valid.add(move);
            }
        }
        // Return valid moves
        return valid;
    }

    /**
     * Gets all possible moves from a position (meant to see if piece at position
     * could capture king if it was that team's turn)
     *
     * @param startPosition the piece to get valid moves for
     * @param testBoard     the board to get valid moves from
     * @return all possible moves from a position (excluding castling, en passant,
     * and including any moves that would be illegal for exposing king)
     */
    private Collection<ChessMove> validTestMoves(ChessPosition startPosition, ChessBoard testBoard) {
        if (testBoard.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> c;
        c = testBoard.getPieceMoves(startPosition);
        return c;
    }

    /**
     * Gets all valid moves that a given team color can make
     *
     * @param color Team color to get moves for
     * @return All valid moves that the team can make
     */
    public Collection<ChessMove> allValidMoves(TeamColor color) {
        Collection<ChessMove> c = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board.getPieceColor(i, j) == color) {
                    c.addAll(validMoves(new ChessPosition(i, j, true)));
                }
            }
        }
        return c;
    }

    /**
     * Gets all valid moves that a given team color can make
     * on a test board, excluding castling and en passant, and
     * including any moves that would expose king.
     *
     * @param color     Team color
     * @param testBoard Board being tested on
     * @return All valid moves that the team can make
     */
    public Collection<ChessMove> allValidTestMoves(TeamColor color, ChessBoard testBoard) {
        Collection<ChessMove> c = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (testBoard.getPieceColor(i, j) == color) {
                    Collection<ChessMove> valid = validTestMoves(new ChessPosition(i, j, true), testBoard);
                    if (valid != null) {
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
        TeamColor pieceColor = board.getPieceColor(row, col);
        if (pieceColor != getTeamTurn()) {
            String msg = "Tried to move " + pieceColor + " colored piece on " + getTeamTurn() + " turn";
            throw new InvalidMoveException(msg);
        }
        boolean validated = false;
        Collection<ChessMove> c = validMoves(move.getStartPosition());
        for (ChessMove i : c) {
            if (move.equals(i)) {
                validated = true;
                break;
            }
        }
        if (!validated) {
            String msg = move + " is invalid!";
            throw new InvalidMoveException(msg);
        }
        board.removePiece(move.getEndPosition());
        ChessPiece.PieceType type = move.getPromotionPiece();
        if (type != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(pieceColor, type));
        } else {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        board.removePiece(move.getStartPosition());
        nextTurn();
        // Check if rook or king moved, update castling monitor, do castling if necessary
        if (!whiteKingMoved && (row == 0) && (col == 4)) {
            whiteKingMoved = true;
            if (abs(col - move.getEndPosition().getColumn(true)) == 2) {
                if (move.getEndPosition().getColumn(true) > col) {
                    ChessPosition rookStartPosition = new ChessPosition(0, 7, true);
                    ChessPosition rookEndPosition = new ChessPosition(move.getEndPosition().getRow(false),
                            move.getEndPosition().getColumn(false) - 1);
                    board.addPiece(rookEndPosition, board.getPiece(rookStartPosition));
                    board.removePiece(rookStartPosition);
                    whiteRightRookMoved = true;
                } else {
                    ChessPosition rookStartPosition = new ChessPosition(0, 0, true);
                    ChessPosition rookEndPosition = new ChessPosition(move.getEndPosition().getRow(false),
                            move.getEndPosition().getColumn(false) + 1);
                    board.addPiece(rookEndPosition, board.getPiece(rookStartPosition));
                    board.removePiece(rookStartPosition);
                    whiteLeftRookMoved = true;
                }
            }
        }
        if (!blackKingMoved && (row == 7) && (col == 4)) {
            blackKingMoved = true;
            if (abs(col - move.getEndPosition().getColumn(true)) == 2) {
                if (move.getEndPosition().getColumn(true) > col) {
                    ChessPosition rookStartPosition = new ChessPosition(7, 7, true);
                    ChessPosition rookEndPosition = new ChessPosition(move.getEndPosition().getRow(false),
                            move.getEndPosition().getColumn(false) - 1);
                    board.addPiece(rookEndPosition, board.getPiece(rookStartPosition));
                    board.removePiece(rookStartPosition);
                    blackRightRookMoved = true;
                } else {
                    ChessPosition rookStartPosition = new ChessPosition(7, 0, true);
                    ChessPosition rookEndPosition = new ChessPosition(move.getEndPosition().getRow(false),
                            move.getEndPosition().getColumn(false) + 1);
                    board.addPiece(rookEndPosition, board.getPiece(rookStartPosition));
                    board.removePiece(rookStartPosition);
                    blackLeftRookMoved = true;
                }
            }
        }
        if (!whiteLeftRookMoved && (row == 0) && (col == 0)) {
            whiteLeftRookMoved = true;
        }
        if (!whiteRightRookMoved && (row == 0) && (col == 7)) {
            whiteRightRookMoved = true;
        }
        if (!blackLeftRookMoved && (row == 7) && (col == 0)) {
            blackLeftRookMoved = true;
        }
        if (!blackRightRookMoved && (row == 7) && (col == 7)) {
            blackRightRookMoved = true;
        }
    }

    /**
     * Makes a move on a separate test board
     *
     * @param move      chess move to perform
     * @param testBoard testing board to perform move on
     */
    public void makeTestMove(ChessMove move, ChessBoard testBoard) {
        int row = move.getStartPosition().getRow(true);
        int col = move.getStartPosition().getColumn(true);
        TeamColor pieceColor = testBoard.getPieceColor(row, col);
        testBoard.removePiece(move.getEndPosition());
        ChessPiece.PieceType type = move.getPromotionPiece();
        if (type != null) {
            testBoard.addPiece(move.getEndPosition(), new ChessPiece(pieceColor, type));
        } else {
            testBoard.addPiece(move.getEndPosition(), testBoard.getPiece(move.getStartPosition()));
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
        return testInCheck(teamColor);
    }

    /**
     * Determines if the given team would be in check
     * if a given move were completed
     *
     * @param teamColor which team to check for check
     * @param testMove  which move to check for check
     * @return True if the specified team would be in
     * check if move were completed (and move is invalid)
     */
    private boolean testNotInCheck(TeamColor teamColor, ChessMove testMove) {
        TeamColor oppColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessBoard testBoard = new ChessBoard(board);
        makeTestMove(testMove, testBoard);
        Collection<ChessMove> c = allValidTestMoves(oppColor, testBoard);
        ChessPosition kingPos = testBoard.getKingPosition(teamColor);
        for (ChessMove move : c) {
            if (move.getEndPosition().equals(kingPos)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team would be in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team would be in
     * check
     */
    private boolean testInCheck(TeamColor teamColor) {
        TeamColor oppColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessBoard testBoard = new ChessBoard(board);
        Collection<ChessMove> c = allValidTestMoves(oppColor, testBoard);
        ChessPosition kingPos = testBoard.getKingPosition(teamColor);
        for (ChessMove move : c) {
            if (move.getEndPosition().equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    private boolean testNoValidMove(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = allValidMoves(teamColor);
        for (ChessMove move : teamMoves) {
            if (testNotInCheck(teamColor, move)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return (testNoValidMove(teamColor) && isInCheck(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return (testNoValidMove(teamColor) && !isInCheck(teamColor));
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
