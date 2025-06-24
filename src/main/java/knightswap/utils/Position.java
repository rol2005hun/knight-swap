package knightswap.utils;

/**
 * Represents a position on a 4x3 chessboard using immutable row and column coordinates.
 * Provides validation for coordinates and a method to check for valid knight moves.
 *
 * @param row The {@code int} row coordinate (0-3).
 * @param col The {@code int} column coordinate (0-2).
 */
public record Position(int row, int col) {
    /**
     * Checks if this position's coordinates are within the specified board boundaries.
     * This method does not validate the existence of a board, only the coordinate values themselves
     * relative to the provided board dimensions.
     *
     * @param boardRows The total number of rows on the board (exclusive upper bound for row coordinate).
     * @param boardCols The total number of columns on the board (exclusive upper bound for col coordinate).
     * @return {@code true} if the row and column are within the valid range [0, boardRows-1] and [0, boardCols-1] respectively, {@code false} otherwise.
     */
    public boolean isValidForBoard(int boardRows, int boardCols) {
        return row >= 0 && row < boardRows && col >= 0 && col < boardCols;
    }

    /**
     * Checks if this {@code Position} represents a valid knight's move target
     * from a given starting {@code Position}. A move is valid if the absolute
     * difference in rows is 2 and columns are 1, or vice versa.
     *
     * @param from The starting {@link Position} of the knight.
     * @return {@code true} if this position is a valid knight move from {@code from}, {@code false} otherwise.
     */
    public boolean isValidKnightMove(Position from) {
        int dRow = Math.abs(this.row - from.row);
        int dCol = Math.abs(this.col - from.col);
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }

    /**
     * Returns a string representation of this position in the format "(row, col)".
     *
     * @return A {@link String} representing the position.
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }
}