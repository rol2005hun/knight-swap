package knightswap.utils;

/**
 * Represents the two types of knight pieces in the Knight Swap game: Light and Dark.
 * Each piece type is associated with a unique character symbol.
 */
public enum PieceType {
    /**
     * Represents a Light knight, typically initialized at the bottom of the game board.
     * Its symbol is 'L'.
     */
    LIGHT('L'),
    /**
     * Represents a Dark knight, typically initialized at the top of the game board.
     * Its symbol is 'D'.
     */
    DARK('D');

    private final char symbol;

    /**
     * Constructs a {@code PieceType} with the specified character symbol.
     *
     * @param symbol The {@code char} symbol representing the piece type ('L' or 'D').
     */
    PieceType(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the character symbol associated with this piece type.
     *
     * @return The {@code char} symbol ('L' for Light, 'D' for Dark).
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns the opposing {@code PieceType}.
     *
     * @return {@link #DARK} if this instance is {@link #LIGHT}, otherwise {@link #LIGHT}.
     */
    public PieceType opponent() {
        return this == LIGHT ? DARK : LIGHT;
    }
}