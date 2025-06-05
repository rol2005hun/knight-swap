package knightswap;

import puzzle.State;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.Objects;

import knightswap.util.PieceType;
import knightswap.util.Position;

/**
 * Represents the state of the KnightSwap puzzle game.
 * The board is 4 rows x 3 columns. Dark knights start in row 0, light knights in row 3.
 * The objective is to swap the positions of the knights.
 * Players take turns moving a knight: Light ({@link PieceType#LIGHT}) starts.
 * A knight move is valid if it's a standard knight move and the target square is not attacked by an opposing piece.
 */
public class KnightSwapState implements State<String> {
    /**
     * The board representation, where {@code 'D'} is a dark knight, {@code 'L'} is a light knight, and {@code '.'} is an empty square.
     */
    final char[][] board;

    /**
     * The type of piece whose turn it is to move next.
     */
    PieceType currentPlayer;

    /**
     * Creates a new {@code KnightSwapState} with the initial board setup.
     * The board is 4x3. Dark knights are placed in row 0, light knights in row 3.
     * The current player is set to {@link PieceType#LIGHT}.
     */
    public KnightSwapState() {
        this.board = new char[4][3];

        for (int i = 0; i < 4; i++) {
            Arrays.fill(board[i], '.');
        }

        board[0][0] = PieceType.DARK.getSymbol();
        board[0][1] = PieceType.DARK.getSymbol();
        board[0][2] = PieceType.DARK.getSymbol();

        board[3][0] = PieceType.LIGHT.getSymbol();
        board[3][1] = PieceType.LIGHT.getSymbol();
        board[3][2] = PieceType.LIGHT.getSymbol();

        this.currentPlayer = PieceType.LIGHT;
        Logger.info("KnightSwap puzzle initialized.");
    }

    /**
     * Creates a new {@code KnightSwapState} as a deep copy of an existing state.
     * This is crucial for the puzzle solver to explore different paths without modifying the original state.
     *
     * @param other the state to copy
     */
    public KnightSwapState(KnightSwapState other) {
        this.board = new char[4][3];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, 3);
        }
        this.currentPlayer = other.currentPlayer;
    }

    /**
     * Returns the character representing the piece at the given position.
     *
     * @param row the row
     * @param col the column
     * @return the character representing the piece ('D', 'L', or '.')
     * @throws IllegalArgumentException if the position is out of bounds (should not happen with {@link Position} record)
     */
    public char getPieceAt(int row, int col) {
        if (row < 0 || row >= 4 || col < 0 || col >= 3) {
            Logger.error("Attempted to access out of bounds position: ({}, {})", row, col);
            throw new IllegalArgumentException("Position out of bounds: (" + row + ", " + col + ")");
        }
        return board[row][col];
    }

    @Override
    public boolean isSolved() {
        boolean darkKnightsAtBottom = (getPieceAt(3, 0) == PieceType.DARK.getSymbol() &&
                getPieceAt(3, 1) == PieceType.DARK.getSymbol() &&
                getPieceAt(3, 2) == PieceType.DARK.getSymbol());

        boolean lightKnightsAtTop = (getPieceAt(0, 0) == PieceType.LIGHT.getSymbol() &&
                getPieceAt(0, 1) == PieceType.LIGHT.getSymbol() &&
                getPieceAt(0, 2) == PieceType.LIGHT.getSymbol());

        return darkKnightsAtBottom && lightKnightsAtTop;
    }

    @Override
    public java.util.Set<String> getLegalMoves() {
        return java.util.Set.of();
    }

    @Override
    public State<String> clone() {
        return new KnightSwapState(this);
    }

    @Override
    public boolean isLegalMove(String s) {
        return false;
    }

    @Override
    public void makeMove(String s) {
        // TODO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnightSwapState that = (KnightSwapState) o;
        return Arrays.deepEquals(board, that.board) && currentPlayer == that.currentPlayer;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + Objects.hash(currentPlayer);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current turn: ").append(currentPlayer).append("\n");
        sb.append("Board:\n");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(board[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the current player whose turn it is.
     *
     * @return the {@link PieceType} of the current player
     */
    public PieceType getCurrentPlayer() {
        return currentPlayer;
    }
}