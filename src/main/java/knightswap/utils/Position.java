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
     * Constructs a {@code Position} instance, validating that the coordinates
     * are within the bounds of a 4x3 board (row 0-3, col 0-2).
     *
     * @param row The {@code int} row coordinate.
     * @param col The {@code int} column coordinate.
     * @throws IllegalArgumentException If {@code row} or {@code col} are out of valid bounds.
     */
    public Position {
        if (row < 0 || row >= 4 || col < 0 || col >= 3) {
            throw new IllegalArgumentException("Invalid row or column for 4x3 board.");
        }
    }

    /**
     * Checks if this {@code Position} represents a valid knight's move target
     * from a given starting {@code Position}. A move is valid if the absolute
     * difference in rows is 2 and columns is 1, or vice-versa.
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