package knightswap;

import puzzle.State;
import org.tinylog.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import knightswap.util.PieceType;
import knightswap.util.Position;
import knightswap.util.ParsedMove;

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
    private PieceType currentPlayer;

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
     * @throws IllegalArgumentException if the position is out of bounds
     */
    public char getPieceAt(int row, int col) {
        if (row < 0 || row >= 4 || col < 0 || col >= 3) {
            Logger.error("Attempted to access out of bounds position: ({}, {})", row, col);
            throw new IllegalArgumentException("Position out of bounds: (" + row + ", " + col + ")");
        }
        return board[row][col];
    }

    /**
     * {@inheritDoc}
     * Checks if the puzzle is solved. The puzzle is solved when all light knights
     * are in the top row (row 0) and all dark knights are in the bottom row (row 3).
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise
     */
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

    /**
     * {@inheritDoc}
     * Returns a set of all legal moves for the {@link #currentPlayer}.
     * A move is represented as a string "startRow startCol endRow endCol".
     *
     * @return a {@link Set} of legal moves
     */
    @Override
    public Set<String> getLegalMoves() {
        Set<String> legalMoves = new HashSet<>();
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
     * Parses a move string into a {@link ParsedMove} object.
     * A move string is expected in the format "startRow startCol endRow endCol".
     *
     * @param moveString the string representation of the move
     * @return a {@link ParsedMove} object if parsing is successful, or {@code null} if the string format is invalid or coordinates are out of bounds.
     */
    private ParsedMove parseMoveString(String moveString) {
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

            return new ParsedMove(start, end);
        } catch (NumberFormatException e) {
            Logger.error("Error parsing move coordinates for move '{}': {}", moveString, e.getMessage());
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * Checks if a specific move is legal.
     * A move string should be in the format "startRow startCol endRow endCol".
     *
     * @param moveString a string representing the move (e.g., "0 0 1 2")
     * @return {@code true} if the move is legal, {@code false} otherwise
     */
    @Override
    public boolean isLegalMove(String moveString) {
        ParsedMove parsedMove = parseMoveString(moveString);
        if (parsedMove == null) {
            return false;
        }

        Position start = parsedMove.start();
        Position end = parsedMove.end();

        char piece = getPieceAt(start.row(), start.col());
        char targetPiece = getPieceAt(end.row(), end.col());

        if ((currentPlayer == PieceType.LIGHT && piece != PieceType.LIGHT.getSymbol()) ||
                (currentPlayer == PieceType.DARK && piece != PieceType.DARK.getSymbol())) {
            Logger.debug("Attempted to move opponent's piece ({}) or empty square ({}) from {}. Current player: {}.", piece, '.', start);
            return false;
        }

        if (targetPiece != '.') {
            Logger.debug("Target square {} is not empty. Contains: {}", end, targetPiece);
            return false;
        }

        if (!end.isValidKnightMove(start)) {
            Logger.debug("Move from {} to {} is not a valid knight move.", start, end);
            return false;
        }

        PieceType attackingPieceType = currentPlayer.opponent();
        if (isAttacked(end, attackingPieceType)) {
            Logger.debug("Target square {} is attacked by an opposing {} piece.", end, attackingPieceType);
            return false;
        }

        Logger.debug("Move {} is legal for {}.", moveString, currentPlayer);
        return true;
    }

    /**
     * Checks if a given position is attacked by a piece of a specific type.
     * This method looks for any knight of {@code attackingPieceType} that can move to {@code position}.
     *
     * @param position the position to check for attacks
     * @param attackingPieceType the type of piece (e.g., {@link PieceType#DARK}) that would attack
     * @return {@code true} if the position is attacked by a knight of the specified type, {@code false} otherwise
     */
    private boolean isAttacked(Position position, PieceType attackingPieceType) {
        char attackerSymbol = attackingPieceType.getSymbol();

        int[][] knightMoves = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] move : knightMoves) {
            // Calculate a potential attacker's position
            int attackerRow = position.row() + move[0];
            int attackerCol = position.col() + move[1];

            // Check if potential attacker position is within bounds
            if (attackerRow >= 0 && attackerRow < 4 && attackerCol >= 0 && attackerCol < 3) {
                if (getPieceAt(attackerRow, attackerCol) == attackerSymbol) {
                    Logger.debug("Position {} attacked by {} at ({}, {}).", position, attackerSymbol, attackerRow, attackerCol);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * Applies the specified move to the current state.
     * Assumes the move is legal (should be checked with {@link #isLegalMove(String)} beforehand).
     *
     * @param moveString a string representing the move (e.g., "0 0 1 2")
     */
    @Override
    public void makeMove(String moveString) {
        if (!isLegalMove(moveString)) {
            Logger.error("Cannot make move '{}' as it is not legal.", moveString);
            throw new IllegalArgumentException("Illegal move: " + moveString);
        }

        ParsedMove parsedMove = parseMoveString(moveString);
        if (parsedMove == null) {
            Logger.error("Cannot make move '{}' as it could not be parsed. This should not happen if isLegalMove was called first.", moveString);
            throw new IllegalArgumentException("Invalid move string provided to makeMove: " + moveString);
        }

        Position start = parsedMove.start();
        Position end = parsedMove.end();

        char piece = board[start.row()][start.col()];
        board[start.row()][start.col()] = '.';
        board[end.row()][end.col()] = piece;

        Logger.info("Move made: {} {} -> {} {} (Piece: {}).", start.row(), start.col(), end.row(), end.col(), piece);
        currentPlayer = currentPlayer.opponent();
        Logger.debug("Current player switched to {}.", currentPlayer);
    }

    /**
     * Returns the current player whose turn it is.
     *
     * @return the {@link PieceType} of the current player
     */
    public PieceType getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * {@inheritDoc}
     * Creates and returns a deep copy of the current state.
     *
     * @return a new {@code KnightSwapState} object that is a deep copy of this instance
     */
    @Override
    public State<String> clone() {
        return new KnightSwapState(this);
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

    /**
     * Returns a string representation of the current board state.
     *
     * @return a string representing the board, including the current player
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