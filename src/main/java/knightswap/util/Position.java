package knightswap.util;

/**
 * Represents a position on the chessboard with row and column coordinates.
 */
public record Position(int row, int col) {

    /**
     * Creates a {@code Position} instance.
     *
     * @param row the row coordinate
     * @param col the column coordinate
     */
    public Position {
        if (row < 0 || row >= 4 || col < 0 || col >= 3) {
            throw new IllegalArgumentException("Invalid row or column for 4x3 board.");
        }
    }

    /**
     * Checks if this position is a valid knight move from another position.
     *
     * @param from the starting position
     * @return true if this position is a valid knight move from {@code from}, false otherwise
     */
    public boolean isValidKnightMove(Position from) {
        int dRow = Math.abs(this.row - from.row);
        int dCol = Math.abs(this.col - from.col);
        return (dRow == 2 && dCol == 1) || (dRow == 1 && dCol == 2);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", row, col);
    }
}