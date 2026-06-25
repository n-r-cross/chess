package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    // Store position row
    private int p_row;
    // Store position column
    private int p_col;

    public ChessPosition(int row, int col) {
        p_row = row-1;
        p_col = col-1;
    }

    /**
     *
     * @param row row index
     * @param col column index
     * @param zero_index whether row and
     *                   col are 0-indexed
     */
    public ChessPosition(int row, int col, boolean zero_index) {
        if(zero_index){
            p_row = row;
            p_col = col;
        } else {
            p_row = row-1;
            p_col = col-1;
        }
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return p_row+1;
    }

    /**
     *
     * @param zero_index whether to return
     *                 1-indexed or
     *                 0-indexed
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow(boolean zero_index) {
        if(!zero_index) {
            return getRow();
        }
        return p_row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return p_col+1;
    }

    /**
     *
     * @param zero_index whether to return
     *                   1-indexed or
     *                   0-indexed
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getColumn(boolean zero_index) {
        if(!zero_index) {
            return getColumn();
        }
        return p_col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return p_row == that.p_row && p_col == that.p_col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(p_row, p_col);
    }
}
