package knightswap;

import knightswap.util.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class KnightSwapStateTest {
    private KnightSwapState initialState;

    @BeforeEach
    void setUp() {
        initialState = new KnightSwapState();
    }

    @Test
    void getPieceAt() {
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 0), "Dark knight at (0,0)");
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 1), "Dark knight at (0,1)");
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 2), "Dark knight at (0,2)");

        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 0), "Light knight at (3,0)");
        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 1), "Light knight at (3,1)");
        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 2), "Light knight at (3,2)");

        assertEquals('.', initialState.getPieceAt(1, 0), "Empty at (1,0)");
        assertEquals('.', initialState.getPieceAt(2, 2), "Empty at (2,2)");

        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(-1, 0), "Should throw for negative row");
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(4, 0), "Should throw for row too high");
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(0, -1), "Should throw for negative col");
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(0, 3), "Should throw for col too high");
    }

    @Test
    void isSolved() {
        assertFalse(initialState.isSolved(), "Initial state should not be solved");

        KnightSwapState solvedState = new KnightSwapState();
        for (int i = 0; i < 4; i++) {
            Arrays.fill(solvedState.board[i], '.');
        }
        solvedState.board[0][0] = PieceType.LIGHT.getSymbol();
        solvedState.board[0][1] = PieceType.LIGHT.getSymbol();
        solvedState.board[0][2] = PieceType.LIGHT.getSymbol();
        solvedState.board[3][0] = PieceType.DARK.getSymbol();
        solvedState.board[3][1] = PieceType.DARK.getSymbol();
        solvedState.board[3][2] = PieceType.DARK.getSymbol();

        assertTrue(solvedState.isSolved(), "Manually created solved state should be identified as solved");

        KnightSwapState partialSolvedState = new KnightSwapState();
        for (int i = 0; i < 4; i++) {
            Arrays.fill(partialSolvedState.board[i], '.');
        }
        partialSolvedState.board[3][0] = PieceType.DARK.getSymbol();
        partialSolvedState.board[3][1] = PieceType.DARK.getSymbol();
        partialSolvedState.board[3][2] = PieceType.DARK.getSymbol();
        partialSolvedState.board[0][0] = PieceType.DARK.getSymbol();
        assertFalse(partialSolvedState.isSolved(), "Partially solved state should not be solved");
    }

    @Test
    void getLegalMoves() {
        assertTrue(initialState.getLegalMoves().isEmpty(), "getLegalMoves should return an empty set (placeholder)");
    }

    @Test
    void testClone() {
        KnightSwapState clonedState = (KnightSwapState) initialState.clone();

        assertNotSame(initialState, clonedState, "Cloned object should not be the same instance");
        assertEquals(initialState.toString(), clonedState.toString(), "Cloned state's string representation should be identical");
        assertEquals(initialState.getCurrentPlayer(), clonedState.getCurrentPlayer(), "Cloned state should have the same current player");

        clonedState.board[0][0] = '.';
        assertNotEquals(clonedState.getPieceAt(0,0), initialState.getPieceAt(0,0), "Modifying clone should not affect original board");
    }

    @Test
    void isLegalMove() {
        assertFalse(initialState.isLegalMove("3 0 1 1"), "isLegalMove should return false (placeholder)");
        assertFalse(initialState.isLegalMove("0 0 1 2"), "isLegalMove should return false (placeholder)");
    }

    @Test
    void makeMove() {
        String initialBoardString = initialState.toString();
        initialState.makeMove("3 0 1 1");

        assertEquals(initialBoardString, initialState.toString(), "makeMove should not change state if unimplemented");
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer(), "Current player should not change if makeMove is unimplemented");
    }

    @Test
    void testEquals() {
        KnightSwapState otherState = new KnightSwapState();
        assertEquals(initialState, otherState, "Initial state should be equal to another freshly created state");
        assertEquals(initialState.hashCode(), otherState.hashCode(), "Equal objects must have equal hash codes");

        initialState.board[0][0] = '.';
        assertNotEquals(initialState, otherState, "States should be unequal after modification");
        assertNotEquals(initialState.hashCode(), otherState.hashCode(), "Unequal objects should have different hash codes");

        assertNotEquals(null, initialState, "State should not be equal to null");

        assertNotEquals(new Object(), initialState, "State should not be equal to an object of different class");
    }

    @Test
    void testHashCode() {
        KnightSwapState otherState = new KnightSwapState();
        assertEquals(initialState.hashCode(), otherState.hashCode(), "Fresh states should have same hash code");

        initialState.board[0][0] = '.';
        assertNotEquals(initialState.hashCode(), otherState.hashCode(), "States with different boards should have different hash codes");

        KnightSwapState clonedState = (KnightSwapState) otherState.clone();
        clonedState.board[0][0] = '.';
        assertEquals(initialState.hashCode(), clonedState.hashCode(), "States with identical configurations should have same hash code");
    }

    @Test
    void testToString() {
        String expectedInitialString = """
                Current turn: LIGHT
                Board:
                D D D\s
                . . .\s
                . . .\s
                L L L\s
                """;
        assertEquals(expectedInitialString, initialState.toString(), "Initial state toString should match expected");
    }

    @Test
    void getCurrentPlayer() {
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer(), "Initial player should be LIGHT");
        initialState.makeMove("some move");
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer(), "Player should remain LIGHT as makeMove is unimplemented");
    }
}