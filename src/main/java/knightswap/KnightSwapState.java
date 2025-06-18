package knightswap;

import puzzle.State;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import knightswap.utils.PieceType;
import knightswap.utils.Position;
import knightswap.utils.ParsedMove;

/**
 * Represents the current state of the Knight Swap puzzle on a 4x3 board.
 * Dark knights ({@code 'D'}) start on row 0, and light knights ({@code 'L'}) start on row 3.
 * The game's objective is to swap their initial positions.
 * Players (Light then Dark) take turns moving their knights. A move is valid if it's a standard
 * knight's move to an empty square that is not attacked by an opposing knight.
 */
public class KnightSwapState implements State<String> {
    /**
     * The board representation, where 'D' is a dark knight, 'L' is a light knight, and '.' is an empty square.
     */
    final char[][] board;

    /**
     * The {@link PieceType} of the player whose turn it is to move next.
     */
    private PieceType currentPlayer;

    /**
     * Creates a new {@code KnightSwapState} with the initial setup for the Knight Swap puzzle.
     * The board is initialized with dark knights in row 0 and light knights in row 3.
     * The {@link #currentPlayer} is set to {@link PieceType#LIGHT}.
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
        Logger.info("KnightSwap puzzle initial state created. Current player: {}.", currentPlayer);
        Logger.debug("Initial board state:\n{}", this.toString());
    }

    /**
     * Creates a new {@code KnightSwapState} by performing a deep copy of another {@code KnightSwapState} instance.
     * This constructor is essential for state-space search algorithms (like BFS) to explore different paths
     * without altering the original state.
     *
     * @param other The {@code KnightSwapState} instance to be copied.
     */
    public KnightSwapState(KnightSwapState other) {
        this.board = new char[4][3];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, 3);
        }
        this.currentPlayer = other.currentPlayer;
        Logger.debug("KnightSwapState deep copy created. Current player: {}.", currentPlayer);
    }

    /**
     * Returns the character symbol representing the piece at the specified board position.
     *
     * @param row The {@code int} row index (0-3).
     * @param col The {@code int} column index (0-2).
     * @return The {@code char} symbol of the piece ('D', 'L', or '.').
     * @throws IllegalArgumentException If the provided row or column is out of the board's bounds.
     */
    public char getPieceAt(int row, int col) {
        if (row < 0 || row >= 4 || col < 0 || col >= 3) {
            Logger.error("Attempted to access out of bounds board position: ({}, {}).", row, col);
            throw new IllegalArgumentException("Position out of bounds: (" + row + ", " + col + ")");
        }
        return board[row][col];
    }

    /**
     * {@inheritDoc}
     * Determines if the puzzle has reached its solved state.
     * The puzzle is solved when all dark knights are in row 3 and all light knights are in row 0.
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise.
     */
    @Override
    public boolean isSolved() {
        boolean darkKnightsAtBottom = (getPieceAt(3, 0) == PieceType.DARK.getSymbol() &&
                getPieceAt(3, 1) == PieceType.DARK.getSymbol() &&
                getPieceAt(3, 2) == PieceType.DARK.getSymbol());

        boolean lightKnightsAtTop = (getPieceAt(0, 0) == PieceType.LIGHT.getSymbol() &&
                getPieceAt(0, 1) == PieceType.LIGHT.getSymbol() &&
                getPieceAt(0, 2) == PieceType.LIGHT.getSymbol());

        boolean solved = darkKnightsAtBottom && lightKnightsAtTop;
        Logger.debug("Checking if solved. Result: {}. Dark at bottom: {}, Light at top: {}.", solved, darkKnightsAtBottom, lightKnightsAtTop);
        return solved;
    }

    /**
     * {@inheritDoc}
     * Generates a set of all legal moves available to the {@link #currentPlayer} from the current board state.
     * Each move is represented as a {@link String} in the format "startRow startCol endRow endCol".
     *
     * @return A {@link Set} of {@link String}s, where each string describes a legal move.
     */
    @Override
    public Set<String> getLegalMoves() {
        Set<String> legalMoves = new HashSet<>();
        Logger.debug("Generating legal moves for current player: {}.", currentPlayer);
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 3; c++) {
                char piece = getPieceAt(r, c);
                if ((currentPlayer == PieceType.LIGHT && piece == PieceType.LIGHT.getSymbol()) ||
                        (currentPlayer == PieceType.DARK && piece == PieceType.DARK.getSymbol())) {

                    Position start = new Position(r, c);
                    int[][] knightMoves = {
                            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                            {1, -2}, {1, 2}, {2, -1}, {2, 1}
                    };

                    for (int[] move : knightMoves) {
                        int endR = start.row() + move[0];
                        int endC = start.col() + move[1];

                        if (endR >= 0 && endR < 4 && endC >= 0 && endC < 3) {
                            Position end = new Position(endR, endC);
                            String moveString = String.format("%d %d %d %d", start.row(), start.col(), end.row(), end.col());
                            if (isLegalMove(moveString)) {
                                legalMoves.add(moveString);
                                Logger.trace("Found legal move: {} (from {} to {}).", moveString, start, end);
                            } else {
                                Logger.trace("Move {} (from {} to {}) is not legal.", moveString, start, end);
                            }
                        }
                    }
                }
            }
        }
        Logger.debug("Generated {} legal moves for current player {}.", legalMoves.size(), currentPlayer);
        return legalMoves;
    }

    /**
     * Parses a string representation of a move into a {@link ParsedMove} object.
     * The expected format is "{@code startRow startCol endRow endCol}".
     *
     * @param moveString The {@link String} representing the move.
     * @return A {@link ParsedMove} object if parsing is successful and coordinates are valid,
     * otherwise {@code null}.
     */
    private ParsedMove parseMoveString(String moveString) {
        Logger.debug("Attempting to parse move string: '{}'.", moveString);
        try {
            String[] parts = moveString.split(" ");
            if (parts.length != 4) {
                Logger.warn("Invalid move format: {}. Expected 'startRow startCol endRow endCol'.", moveString);
                return null;
            }

            int startRow = Integer.parseInt(parts[0]);
            int startCol = Integer.parseInt(parts[1]);
            int endRow = Integer.parseInt(parts[2]);
            int endCol = Integer.parseInt(parts[3]);

            Position start = new Position(startRow, startCol);
            Position end = new Position(endRow, endCol);

            Logger.debug("Move string '{}' parsed successfully to start: {}, end: {}.", moveString, start, end);
            return new ParsedMove(start, end);
        } catch (NumberFormatException e) {
            Logger.error("Error parsing move coordinates for move '{}': {}", moveString, e.getMessage(), e);
            return null;
        } catch (IllegalArgumentException e) {
            Logger.warn("Move string contains out-of-bounds coordinates: {}. Error: {}", moveString, e.getMessage());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * Checks if a given move, specified as a string, is legal according to the game rules.
     * A move is legal if:
     * <ul>
     * <li>It corresponds to the {@link #currentPlayer}'s piece.</li>
     * <li>The target square is empty.</li>
     * <li>It is a valid knight's move.</li>
     * <li>The target square is not attacked by an opposing knight.</li>
     * </ul>
     *
     * @param moveString A {@link String} representing the move (e.g., "0 0 1 2").
     * @return {@code true} if the move is legal, {@code false} otherwise.
     */
    @Override
    public boolean isLegalMove(String moveString) {
        Logger.debug("Checking legality of move: '{}'. Current player: {}.", moveString, currentPlayer);
        ParsedMove parsedMove = parseMoveString(moveString);
        if (parsedMove == null) {
            Logger.debug("Move string '{}' could not be parsed, not a legal move.", moveString);
            return false;
        }

        Position start = parsedMove.start();
        Position end = parsedMove.end();

        char piece = getPieceAt(start.row(), start.col());
        char targetPiece = getPieceAt(end.row(), end.col());

        if ((currentPlayer == PieceType.LIGHT && piece != PieceType.LIGHT.getSymbol()) ||
                (currentPlayer == PieceType.DARK && piece != PieceType.DARK.getSymbol())) {
            Logger.debug("Move from {} (piece: {}) rejected: not current player's piece or empty square. Current player: {}.", start, piece, currentPlayer);
            return false;
        }

        if (targetPiece != '.') {
            Logger.debug("Move from {} to {} rejected: target square is not empty (contains {}).", start, end, targetPiece);
            return false;
        }

        if (!end.isValidKnightMove(start)) {
            Logger.debug("Move from {} to {} rejected: not a valid knight move.", start, end);
            return false;
        }

        PieceType attackingPieceType = currentPlayer.opponent();
        if (isAttacked(end, attackingPieceType)) {
            Logger.debug("Move from {} to {} rejected: target square {} is attacked by an opposing {} piece.", start, end, end, attackingPieceType);
            return false;
        }

        Logger.debug("Move '{}' from {} to {} is legal for player {}.", moveString, start, end, currentPlayer);
        return true;
    }

    /**
     * Checks if a specific {@link Position} on the board is currently under attack by an opposing knight.
     * An attack is defined by a knight of {@code attackingPieceType} being able to move to {@code position}.
     *
     * @param position The {@link Position} to check for attacks.
     * @param attackingPieceType The {@link PieceType} of the attacking knight (e.g., {@link PieceType#DARK}).
     * @return {@code true} if the position is attacked, {@code false} otherwise.
     */
    private boolean isAttacked(Position position, PieceType attackingPieceType) {
        char attackerSymbol = attackingPieceType.getSymbol();
        Logger.trace("Checking if position {} is attacked by {} pieces.", position, attackingPieceType);

        int[][] knightMoves = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            int attackerRow = position.row() + move[0];
            int attackerCol = position.col() + move[1];

            if (attackerRow >= 0 && attackerRow < 4 && attackerCol >= 0 && attackerCol < 3) {
                if (getPieceAt(attackerRow, attackerCol) == attackerSymbol) {
                    Logger.debug("Position {} attacked by {} piece at ({}, {}).", position, attackerSymbol, attackerRow, attackerCol);
                    return true;
                }
            }
        }
        Logger.trace("Position {} is NOT attacked by {} pieces.", position, attackingPieceType);
        return false;
    }

    /**
     * {@inheritDoc}
     * Applies the specified move to the current board state.
     * This method assumes the move has already been validated as legal by {@link #isLegalMove(String)}.
     * After the move, the {@link #currentPlayer} is switched to the opponent.
     *
     * @param moveString A {@link String} representing the move (e.g., "0 0 1 2").
     * @throws IllegalArgumentException If the move string is illegal or cannot be parsed.
     */
    @Override
    public void makeMove(String moveString) {
        Logger.info("Attempting to make move: '{}' for player {}.", moveString, currentPlayer);
        if (!isLegalMove(moveString)) {
            Logger.error("Cannot make move '{}' as it is not legal. Throwing IllegalArgumentException.", moveString);
            throw new IllegalArgumentException("Illegal move: " + moveString);
        }

        ParsedMove parsedMove = parseMoveString(moveString);
        if (parsedMove == null) {
            Logger.error("Cannot make move '{}' as it could not be parsed. This should not happen if isLegalMove was called first. Throwing IllegalArgumentException.", moveString);
            throw new IllegalArgumentException("Invalid move string provided to makeMove: " + moveString);
        }

        Position start = parsedMove.start();
        Position end = parsedMove.end();

        char piece = board[start.row()][start.col()];
        board[start.row()][start.col()] = '.';
        board[end.row()][end.col()] = piece;

        Logger.info("Move successfully made: {} {} -> {} {} (Piece: {}).", start.row(), start.col(), end.row(), end.col(), piece);
        currentPlayer = currentPlayer.opponent();
        Logger.debug("Current player switched to {}.", currentPlayer);
    }

    /**
     * Returns the {@link PieceType} of the player whose turn it is to make a move.
     *
     * @return The {@link PieceType} of the current player.
     */
    public PieceType getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * {@inheritDoc}
     * Creates and returns a deep copy of this {@code KnightSwapState} instance.
     *
     * @return A new {@link State} object that is an independent copy of the current state.
     */
    @Override
    public State<String> clone() {
        Logger.debug("Cloning KnightSwapState.");
        return new KnightSwapState(this);
    }

    /**
     * {@inheritDoc}
     * Compares this {@code KnightSwapState} object to another object for equality.
     * Two states are considered equal if their board configurations and current players are identical.
     *
     * @param o The {@link Object} to compare with this state.
     * @return {@code true} if the specified object is equal to this state, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnightSwapState that = (KnightSwapState) o;
        boolean isEqual = Arrays.deepEquals(board, that.board) && currentPlayer == that.currentPlayer;
        Logger.trace("Comparing states. Result: {}. This hash: {}, Other hash: {}.", isEqual, this.hashCode(), that.hashCode());
        return isEqual;
    }

    /**
     * {@inheritDoc}
     * Computes a hash code for this {@code KnightSwapState} object.
     * The hash code is based on the board configuration and the current player.
     *
     * @return An {@code int} hash code value for this state.
     */
    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(board);
        result = 31 * result + Objects.hash(currentPlayer);
        Logger.trace("Calculated hash code: {}.", result);
        return result;
    }

    /**
     * Returns a multi-line string representation of the current board state,
     * including the current player whose turn it is.
     *
     * @return A {@link String} representation of the board and current player.
     */
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
}