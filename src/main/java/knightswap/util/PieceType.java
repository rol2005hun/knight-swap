package knightswap.util;

/**
 * Represents the type of knight piece.
 */
public enum PieceType {
    /**
     * Represents a Light knight, typically starting at the bottom of the board.
     */
    LIGHT('L'),
    /**
     * Represents a Dark knight, typically starting at the top of the board.
     */
    DARK('D');

    private final char symbol;

    PieceType(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the character symbol for the piece type.
     * @return the symbol ('L' for Light, 'D' for Dark)
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the opposing piece type.
     * @return {@link #DARK} if this is {@link #LIGHT}, otherwise {@link #LIGHT}
     */
    public PieceType opponent() {
        return this == LIGHT ? DARK : LIGHT;
    }
}