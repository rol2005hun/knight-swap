package knightswap.util;

/**
 * A helper class that stores the start and end positions of a move.
 * @param start the starting position of the move
 * @param end the ending position of the move
 */
public record ParsedMove(Position start, Position end) {
}