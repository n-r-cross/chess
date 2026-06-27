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
    final private int row;
    // Store position column
    final private int col;

    public ChessPosition(int row, int col) {
        this.row = row - 1;
        this.col = col - 1;
    }

    /**
     *
     * @param row        row index
     * @param col        column index
     * @param zeroIndex whether row and
     *                   col are 0-indexed
     */
    public ChessPosition(int row, int col, boolean zeroIndex) {
        if (zeroIndex) {
            this.row = row;
            this.col = col;
        } else {
            this.row = row - 1;
            this.col = col - 1;
        }
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row + 1;
    }

    /**
     *
     * @param zeroIndex whether to return
     *                   1-indexed or
     *                   0-indexed
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow(boolean zeroIndex) {
        if (!zeroIndex) {
            return getRow();
        }
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col + 1;
    }

    /**
     *
     * @param zeroIndex whether to return
     *                   1-indexed or
     *                   0-indexed
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getColumn(boolean zeroIndex) {
        if (!zeroIndex) {
            return getColumn();
        }
        return col;
    }

    /**
     *
     * @param row row of point to test (assumes zero-indexed)
     * @param col column of point to test (assumes zero-indexed)
     * @return whether point is inside board boundaries
     */
    static public boolean inBounds(int row, int col) {
        return (((row >= 0) && (row <= 7)) && ((col >= 0) && (col <= 7)));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
