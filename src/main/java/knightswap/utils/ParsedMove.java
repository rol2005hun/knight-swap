package knightswap.utils;

/**
 * A record class representing a parsed move in the Knight Swap game,
 * consisting of a starting {@link Position} and an ending {@link Position}.
 * This record provides an immutable way to store move data.
 *
 * @param start The {@link Position} from which a piece moves.
 * @param end The {@link Position} to which a piece moves.
 */
public record ParsedMove(Position start, Position end) {}