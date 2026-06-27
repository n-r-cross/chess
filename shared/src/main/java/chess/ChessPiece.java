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

    public ChessGame.TeamColor getOppColor() {
        return this.getTeamColor() == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
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

    private Collection<ChessMove> pawnPromotionMoves(ChessPosition myPos, ChessPosition newPos) {
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
        //
        Collection<ChessMove> c;
        c = new ArrayList<>();
        // Easy access to position
        int row;
        int col;
        switch(p_type){
            case KING:
                // Generate a possible position
                for(int i = -1; i <= 1; i++) {
                    for(int j = -1; j <= 1; j++) {
                        row = myPosition.getRow(true) + i;
                        col = myPosition.getColumn(true) + j;
                        // System.out.println("R: " + row + " | C: " + col);
                        // Check if position is inBounds
                        if (ChessPosition.inBounds(row,col)) {
                            // Check if piece at position
                            ChessGame.TeamColor color = board.getPieceColor(row,col);
                            if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                                ChessPosition e = new ChessPosition(myPosition.getRow(true) + i, myPosition.getColumn(true) + j, true);
                                c.add(new ChessMove(myPosition, e, null));
                            }
                        }
                    }
                }
                break;
            case QUEEN:
                row = myPosition.getRow(true);
                col = myPosition.getColumn(true);
                // Generate a possible position
                for(int i = -1; i <= 1; i += 2){
                    for(int j = -1; j <= 1; j += 2){
                        for(int k = 1; ChessPosition.inBounds(row+k*i,col+k*j); k++){
                            // System.out.println("R: " + row + " | C: " + col);
                            // Check if piece at position
                            ChessGame.TeamColor color = board.getPieceColor(row+k*i,col+k*j);
                            if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                                ChessPosition e = new ChessPosition(row+k*i,col+k*j, true);
                                c.add(new ChessMove(myPosition, e, null));
                            }
                            if (color != null) {
                                break;
                            }
                        }
                    }
                }
                // Generate a possible position
                for (int i = -1; i <= 1; i++) {
                    switch (i) {
                        case -1,1:
                            for (int j = row + i; (ChessPosition.inBounds(j,col)); j += i) {
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
                            break;
                        case 0:
                            for (int j = col+1; (ChessPosition.inBounds(row,j)); j += 1) {
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
                            for (int j = col-1; (ChessPosition.inBounds(row,j)); j -= 1) {
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
                break;
            case BISHOP:
                row = myPosition.getRow(true);
                col = myPosition.getColumn(true);
                // Generate a possible position
                for(int i = -1; i <= 1; i += 2){
                    for(int j = -1; j <= 1; j += 2){
                        for(int k = 1; ChessPosition.inBounds(row+k*i,col+k*j); k++){
                            // System.out.println("R: " + row + " | C: " + col);
                            // Check if piece at position
                            ChessGame.TeamColor color = board.getPieceColor(row+k*i,col+k*j);
                            if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                                ChessPosition e = new ChessPosition(row+k*i,col+k*j, true);
                                c.add(new ChessMove(myPosition, e, null));
                            }
                            if (color != null) {
                                break;
                            }
                        }
                    }
                }
                break;
            case KNIGHT:
                // Generate a possible position
                for(int i = -2; i <= 2; i++) {
                    switch(i) {
                        case -2, 2:
                            for (int j = -1; j <= 1; j += 2) {
                                row = myPosition.getRow(true) + i;
                                col = myPosition.getColumn(true) + j;
                                // System.out.println("R: " + row + " | C: " + col);
                                // Check if position is inBounds
                                if (ChessPosition.inBounds(row, col)) {
                                    // Check if piece at position
                                    ChessGame.TeamColor color = board.getPieceColor(row, col);
                                    if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                                        ChessPosition e = new ChessPosition(myPosition.getRow(true) + i, myPosition.getColumn(true) + j, true);
                                        c.add(new ChessMove(myPosition, e, null));
                                    }
                                }
                            }
                            break;
                        case -1, 1:
                            for (int j = -2; j <= 2; j += 4) {
                                row = myPosition.getRow(true) + i;
                                col = myPosition.getColumn(true) + j;
                                // System.out.println("R: " + row + " | C: " + col);
                                // Check if position is inBounds
                                if (ChessPosition.inBounds(row, col)) {
                                    // Check if piece at position
                                    ChessGame.TeamColor color = board.getPieceColor(row, col);
                                    if ((color == this.getOppColor()) || (color == null)) { // Add test if piece is at position
                                        ChessPosition e = new ChessPosition(myPosition.getRow(true) + i, myPosition.getColumn(true) + j, true);
                                        c.add(new ChessMove(myPosition, e, null));
                                    }
                                }
                            }
                            break;
                        case 0:
                            break;
                    }
                }
            case ROOK:
                row = myPosition.getRow(true);
                col = myPosition.getColumn(true);
                // Generate a possible position
                for (int i = -1; i <= 1; i++) {
                    switch (i) {
                        case -1,1:
                            for (int j = row + i; (ChessPosition.inBounds(j,col)); j += i) {
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
                            break;
                        case 0:
                            for (int j = col+1; (ChessPosition.inBounds(row,j)); j += 1) {
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
                            for (int j = col-1; (ChessPosition.inBounds(row,j)); j -= 1) {
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
                break;
            case PAWN:
                // Switch based on PieceColor
                if(this.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    // Generate a possible position
                    row = myPosition.getRow(true) + 1;
                    col = myPosition.getColumn(true);
                    // Check if position is inBounds
                    if (ChessPosition.inBounds(row, col)) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, col);
                        if (color == null) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, col, true);
                            if (row == 7) {
                                c.addAll(pawnPromotionMoves(myPosition, e));
                            } else {
                                c.add(new ChessMove(myPosition, e, null));
                            }
                            // No piece impeding next space, so check advance 2 spaces
                            if(myPosition.getRow(true) == 1) {
                                row++;
                                if (ChessPosition.inBounds(row, col)) {
                                    // Check if piece at position
                                    color = board.getPieceColor(row, col);
                                    if (color == null) { // Add test if piece is at position
                                        e = new ChessPosition(row, col, true);
                                        c.add(new ChessMove(myPosition, e, null));
                                    }
                                }
                            }
                        }
                    }
                    // Generate a possible position
                    row = myPosition.getRow(true) + 1;
                    col = myPosition.getColumn(true) + 1;
                    // Check if position is inBounds
                    if (ChessPosition.inBounds(row, col)) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, col);
                        if (color == ChessGame.TeamColor.BLACK) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, col, true);
                            if (row == 7) {
                                c.addAll(pawnPromotionMoves(myPosition, e));
                            } else {
                                c.add(new ChessMove(myPosition, e, null));
                            }
                        }
                    }
                    // Generate a possible position
                    row = myPosition.getRow(true) + 1;
                    col = myPosition.getColumn(true) - 1;
                    // Check if position is inBounds
                    if (ChessPosition.inBounds(row, col)) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, col);
                        if (color == ChessGame.TeamColor.BLACK) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, col, true);
                            if (row == 7) {
                                c.addAll(pawnPromotionMoves(myPosition, e));
                            } else {
                                c.add(new ChessMove(myPosition, e, null));
                            }
                        }
                    }
                } else {
                    // Generate a possible position
                    row = myPosition.getRow(true) - 1;
                    col = myPosition.getColumn(true);
                    // Check if position is inBounds
                    if (ChessPosition.inBounds(row, col)) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, col);
                        if (color == null) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, col, true);
                            if (row == 0) {
                                c.addAll(pawnPromotionMoves(myPosition, e));
                            } else {
                                c.add(new ChessMove(myPosition, e, null));
                            }
                            // No piece impeding next space, so check advance 2 spaces
                            if(myPosition.getRow(true) == 6) {
                                row--;
                                if (ChessPosition.inBounds(row, col)) {
                                    // Check if piece at position
                                    color = board.getPieceColor(row, col);
                                    if (color == null) { // Add test if piece is at position
                                        e = new ChessPosition(row, col, true);
                                        c.add(new ChessMove(myPosition, e, null));
                                    }
                                }
                            }
                        }
                    }
                    // Generate a possible position
                    row = myPosition.getRow(true) - 1;
                    col = myPosition.getColumn(true) + 1;
                    // Check if position is inBounds
                    if (ChessPosition.inBounds(row, col)) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, col);
                        if (color == ChessGame.TeamColor.WHITE) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, col, true);
                            if (row == 0) {
                                c.addAll(pawnPromotionMoves(myPosition, e));
                            } else {
                                c.add(new ChessMove(myPosition, e, null));
                            }
                        }
                    }
                    // Generate a possible position
                    row = myPosition.getRow(true) - 1;
                    col = myPosition.getColumn(true) - 1;
                    // Check if position is inBounds
                    if (ChessPosition.inBounds(row, col)) {
                        // Check if piece at position
                        ChessGame.TeamColor color = board.getPieceColor(row, col);
                        if (color == ChessGame.TeamColor.WHITE) { // Add test if piece is at position
                            ChessPosition e = new ChessPosition(row, col, true);
                            if (row == 0) {
                                c.addAll(pawnPromotionMoves(myPosition, e));
                            } else {
                                c.add(new ChessMove(myPosition, e, null));
                            }
                        }
                    }
                }
                break;
        }
        for(ChessMove i : c) {
            System.out.println(i);
        }
        return c;
    }
}
