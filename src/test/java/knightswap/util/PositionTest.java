package knightswap.util;

import knightswap.utils.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    void testIsValidKnightMoveValidMoves() {
        Position from11 = new Position(1, 1);

        assertTrue(new Position(3, 0).isValidKnightMove(from11), "Move (1,1) -> (3,0) should be valid.");
        assertTrue(new Position(3, 2).isValidKnightMove(from11), "Move (1,1) -> (3,2) should be valid.");
        assertTrue(new Position(3, 0).isValidKnightMove(from11), "Move (1,1) -> (3,0) should be valid.");
        assertTrue(new Position(3, 2).isValidKnightMove(from11), "Move (1,1) -> (3,2) should be valid.");

        Position from00 = new Position(0, 0);

        assertTrue(new Position(2, 1).isValidKnightMove(from00), "Move (0,0) -> (2,1) should be valid.");
        assertTrue(new Position(1, 2).isValidKnightMove(from00), "Move (0,0) -> (1,2) should be valid.");

        Position from32 = new Position(3, 2);

        assertTrue(new Position(1, 1).isValidKnightMove(from32), "Move (3,2) -> (1,1) should be valid.");
        assertTrue(new Position(2, 0).isValidKnightMove(from32), "Move (3,2) -> (2,0) should be valid.");
    }

    @Test
    void testIsValidKnightMoveInvalidMoves() {
        Position from = new Position(1, 1);

        assertFalse(new Position(1, 1).isValidKnightMove(from), "Move to self should be invalid.");
        assertFalse(new Position(1, 0).isValidKnightMove(from), "Move to adjacent square should be invalid.");
        assertFalse(new Position(2, 1).isValidKnightMove(from), "Move to adjacent square (vertical) should be invalid.");
        assertFalse(new Position(0, 1).isValidKnightMove(from), "Move to adjacent square (vertical) should be invalid.");
        assertFalse(new Position(1, 2).isValidKnightMove(from), "Move to adjacent square (horizontal) should be invalid.");
        assertFalse(new Position(0, 1).isValidKnightMove(from), "Move (1,1) -> (0,1) should be invalid (not L-shaped).");
        assertFalse(new Position(2, 0).isValidKnightMove(from), "Move (1,1) -> (2,0) should be invalid (not L-shaped).");
        assertFalse(new Position(0, 1).isValidKnightMove(from), "Move (1,1) -> (0,1) should be invalid (not L-shaped).");
    }

    @Test
    void testIsValidKnightMoveInvalidTargetPositions() {
        Position from = new Position(1, 1);
        assertFalse(new Position(1, 0).isValidKnightMove(from), "Move (1,1) -> (1,0) should be invalid (not a knight move).");
        assertFalse(new Position(2, 1).isValidKnightMove(from), "Move (1,1) -> (2,1) should be invalid (not a knight move).");
        assertFalse(new Position(0, 1).isValidKnightMove(from), "Move (1,1) -> (0,1) should be invalid (not a knight move).");
    }

    @Test
    void testToString() {
        Position pos = new Position(2, 1);
        assertEquals("(2, 1)", pos.toString(), "toString should format correctly.");

        Position pos2 = new Position(0, 0);
        assertEquals("(0, 0)", pos2.toString(), "toString should format correctly for (0,0).");
    }
}