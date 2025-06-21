package knightswap;

import knightswap.utils.PieceType;
import knightswap.utils.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KnightSwapStateTest {
    private KnightSwapState initialState;

    @BeforeEach
    void setUp() {
        initialState = new KnightSwapState();
    }

    @Test
    void testInitialBoardSetup() {
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 0));
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 1));
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 2));

        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 0));
        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 1));
        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 2));

        assertEquals('.', initialState.getPieceAt(1, 0));
        assertEquals('.', initialState.getPieceAt(1, 1));
        assertEquals('.', initialState.getPieceAt(1, 2));
        assertEquals('.', initialState.getPieceAt(2, 0));
        assertEquals('.', initialState.getPieceAt(2, 1));
        assertEquals('.', initialState.getPieceAt(2, 2));
    }

    @Test
    void testInitialCurrentPlayer() {
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer());
    }

    @Test
    void testGetPieceAtValidCoordinates() {
        assertEquals(PieceType.DARK.getSymbol(), initialState.getPieceAt(0, 0));
        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(3, 1));
        assertEquals('.', initialState.getPieceAt(1, 2));
    }

    @Test
    void testGetPieceAtOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(4, 0));
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(0, -1));
        assertThrows(IllegalArgumentException.class, () -> initialState.getPieceAt(0, 3));
    }

    @Test
    void testIsSolvedInitialState() {
        assertFalse(initialState.isSolved());
    }

    @Test
    void testIsSolvedCorrectlySolvedState() {
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

        assertTrue(solvedState.isSolved());
    }

    @Test
    void testIsSolvedPartiallySolvedState() {
        KnightSwapState partialSolvedState = new KnightSwapState();
        partialSolvedState.board[0][0] = PieceType.LIGHT.getSymbol();
        partialSolvedState.board[3][0] = PieceType.DARK.getSymbol();

        assertFalse(partialSolvedState.isSolved());

        KnightSwapState onlyDarkSolved = new KnightSwapState();
        for (int i = 0; i < 4; i++) {
            Arrays.fill(onlyDarkSolved.board[i], '.');
        }
        onlyDarkSolved.board[3][0] = PieceType.DARK.getSymbol();
        onlyDarkSolved.board[3][1] = PieceType.DARK.getSymbol();
        onlyDarkSolved.board[3][2] = PieceType.DARK.getSymbol();
        onlyDarkSolved.board[0][0] = PieceType.LIGHT.getSymbol();
        assertFalse(onlyDarkSolved.isSolved());
    }

    @Test
    void testIsSolvedOneKnightWrongPosition() {
        KnightSwapState state = new KnightSwapState();
        for (int i = 0; i < 4; i++) {
            Arrays.fill(state.board[i], '.');
        }
        state.board[0][0] = PieceType.LIGHT.getSymbol();
        state.board[0][1] = PieceType.LIGHT.getSymbol();
        state.board[0][2] = PieceType.DARK.getSymbol();
        state.board[3][0] = PieceType.DARK.getSymbol();
        state.board[3][1] = PieceType.DARK.getSymbol();
        state.board[3][2] = PieceType.LIGHT.getSymbol();

        assertFalse(state.isSolved());
    }

    @Test
    void testGetLegalMovesInitialState() {
        Set<TwoPhaseMoveState.TwoPhaseMove<Position>> expectedMoves = new HashSet<>();
        expectedMoves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        expectedMoves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 2), new Position(1, 1)));

        assertEquals(expectedMoves, initialState.getLegalMoves());
    }

    @Test
    void testGetLegalMovesAfterOneMove() {
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        Set<TwoPhaseMoveState.TwoPhaseMove<Position>> expectedMoves = new HashSet<>();

        expectedMoves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(2, 1)));
        expectedMoves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 1), new Position(2, 2)));
        expectedMoves.add(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 2), new Position(2, 1)));

        assertEquals(expectedMoves.size(), initialState.getLegalMoves().size());
        assertTrue(initialState.getLegalMoves().containsAll(expectedMoves));
        assertTrue(expectedMoves.containsAll(initialState.getLegalMoves()));
    }

    @Test
    void testClone() {
        KnightSwapState clonedState = (KnightSwapState) initialState.clone();

        assertNotSame(initialState, clonedState);
        assertNotSame(initialState.board, clonedState.board);
        assertTrue(Arrays.deepEquals(initialState.board, clonedState.board));
        assertEquals(initialState.getCurrentPlayer(), clonedState.getCurrentPlayer());

        clonedState.board[0][0] = '.';
        assertNotEquals(clonedState.getPieceAt(0, 0), initialState.getPieceAt(0, 0));
    }

    @Test
    void testIsLegalToMoveFromValid() {
        assertTrue(initialState.isLegalToMoveFrom(new Position(3, 0)));
    }

    @Test
    void testIsLegalToMoveFromInValid() {
        assertFalse(initialState.isLegalToMoveFrom(new Position(-1, 0)));
    }

    @Test
    void testIsLegalToMoveFromOpponentPiece() {
        assertFalse(initialState.isLegalToMoveFrom(new Position(0, 0)));
    }

    @Test
    void testIsLegalToMoveFromEmptySquare() {
        assertFalse(initialState.isLegalToMoveFrom(new Position(1, 1)));
    }

    @Test
    void testIsLegalMoveValidInitialMoves() {
        assertTrue(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1))));
        assertTrue(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 2), new Position(1, 1))));
    }

    @Test
    void testIsLegalMoveNullMove() {
        assertFalse(initialState.isLegalMove(null));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(null, new Position(1, 1))));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), null)));
    }

    @Test
    void testIsLegalMoveOutOfBoundsCoordinates() {
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(-1, 0), new Position(1, 1))));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(5, 1))));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, -1), new Position(2, 0))));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(1, 3))));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(-1, -1), new Position(4, 3))));
    }

    @Test
    void testIsLegalMoveOpponentPiece() {
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(2, 1))));
    }

    @Test
    void testIsLegalMoveEmptySquare() {
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(1, 0), new Position(3, 1))));
    }

    @Test
    void testIsLegalMoveToOccupiedSquare() {
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(3, 1))));
    }

    @Test
    void testIsLegalMoveNotKnightMove() {
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(2, 0))));
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(2, 1))));
    }

    @Test
    void testIsLegalMoveTargetAttacked() {
        KnightSwapState stateForAttackTest = new KnightSwapState();
        stateForAttackTest.board[0][0] = PieceType.DARK.getSymbol();
        stateForAttackTest.board[3][0] = PieceType.LIGHT.getSymbol();
        assertFalse(stateForAttackTest.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(2, 2))));
    }

    @Test
    void testIsLegalToMoveFromCurrentPlayerDark() throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field currentPlayerField = KnightSwapState.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        currentPlayerField.set(initialState, PieceType.DARK);

        assertTrue(initialState.isLegalToMoveFrom(new Position(0, 0)), "isLegalToMoveFrom should return true for dark piece when dark player is current.");

        assertFalse(initialState.isLegalToMoveFrom(new Position(3, 0)), "isLegalToMoveFrom should return false for light piece when dark player is current.");

        assertFalse(initialState.isLegalToMoveFrom(new Position(1, 1)), "isLegalToMoveFrom should return false for empty square when dark player is current.");
    }

    @Test
    void testMakeMoveLegalMove() {
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1));
        initialState.makeMove(move);

        assertEquals('.', initialState.getPieceAt(3, 0));
        assertEquals(PieceType.LIGHT.getSymbol(), initialState.getPieceAt(1, 1));
        assertEquals(PieceType.DARK, initialState.getCurrentPlayer());

        String expectedBoardString = """
                Current turn: DARK
                Board:
                D D D\s
                . L .\s
                . . .\s
                . L L\s
                """;
        assertEquals(expectedBoardString, initialState.toString());
    }

    @Test
    void testMakeMoveIllegalMove() {
        TwoPhaseMoveState.TwoPhaseMove<Position> illegalMove = new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(1, 2));

        assertThrows(IllegalArgumentException.class, () -> initialState.makeMove(illegalMove), "makeMove should throw IllegalArgumentException for an illegal move.");

        KnightSwapState stateBeforeIllegalMove = (KnightSwapState) initialState.clone();

        try {
            initialState.makeMove(illegalMove);
            fail("makeMove did not throw IllegalArgumentException for illegal move: " + illegalMove);
        } catch (IllegalArgumentException e) {
            assertEquals(stateBeforeIllegalMove, initialState, "State should remain unchanged after an illegal move attempt.");
        }
    }

    @Test
    void testEqualsIdenticalStates() {
        KnightSwapState otherState = new KnightSwapState();
        assertEquals(initialState, otherState);
    }

    @Test
    void testEqualsDifferentBoardStates() {
        KnightSwapState otherState = new KnightSwapState();
        otherState.board[0][0] = '.';
        assertNotEquals(initialState, otherState);
    }

    @Test
    void testEqualsDifferentPlayers() {
        KnightSwapState otherState = new KnightSwapState();
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        assertNotEquals(initialState, otherState);
    }

    @Test
    void testEqualsDifferentPlayersButSameBoard() throws NoSuchFieldException, IllegalAccessException {
        KnightSwapState state1 = new KnightSwapState();
        KnightSwapState state2 = new KnightSwapState();

        assertEquals(state1, state2);

        java.lang.reflect.Field currentPlayerField = KnightSwapState.class.getDeclaredField("currentPlayer");
        currentPlayerField.setAccessible(true);
        currentPlayerField.set(state2, PieceType.DARK);

        assertNotEquals(state1, state2, "States should not be equal if only current players differ but boards are same.");
    }

    @Test
    void testEqualsDifferentClass() {
        assertNotEquals(new Object(), initialState);
    }

    @Test
    void testEqualsNull() {
        assertNotEquals(null, initialState);
    }

    @Test
    void testEqualsSameInstance() {
        assertEquals(initialState, initialState, "State should be equal to itself.");
    }

    @Test
    void testHashCodeConsistency() {
        KnightSwapState otherState = new KnightSwapState();
        assertEquals(initialState.hashCode(), otherState.hashCode());

        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        KnightSwapState stateAfterMove = (KnightSwapState) initialState.clone();
        assertEquals(initialState.hashCode(), stateAfterMove.hashCode());
    }

    @Test
    void testHashCodeDifference() {
        KnightSwapState otherState = new KnightSwapState();
        otherState.board[0][0] = '.';
        assertNotEquals(initialState.hashCode(), otherState.hashCode());
    }

    @Test
    void testToStringInitialState() {
        String expectedInitialString = """
                Current turn: LIGHT
                Board:
                D D D\s
                . . .\s
                . . .\s
                L L L\s
                """;
        assertEquals(expectedInitialString, initialState.toString());
    }

    @Test
    void testToStringAfterMove() {
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        String expectedAfterMoveString = """
                Current turn: DARK
                Board:
                D D D\s
                . L .\s
                . . .\s
                . L L\s
                """;
        assertEquals(expectedAfterMoveString, initialState.toString());
    }

    @Test
    void testGetCurrentPlayer() {
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer());
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        assertEquals(PieceType.DARK, initialState.getCurrentPlayer());
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(2, 1)));
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer());
    }

    @Test
    void testIsLegalMoveTargetAttackedByOpponent() {
        KnightSwapState stateForAttackLogicTest = new KnightSwapState();
        for (int r = 0; r < 4; r++) Arrays.fill(stateForAttackLogicTest.board[r], '.');
        stateForAttackLogicTest.board[0][1] = PieceType.DARK.getSymbol();
        stateForAttackLogicTest.board[3][0] = PieceType.LIGHT.getSymbol();

        assertFalse(stateForAttackLogicTest.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(2, 2))));
    }

    @Test
    void testIsAttackedByReflectionTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isAttackedMethod = KnightSwapState.class.getDeclaredMethod("isAttacked", Position.class, PieceType.class);
        isAttackedMethod.setAccessible(true);

        KnightSwapState state = new KnightSwapState();
        state.board[0][0] = PieceType.DARK.getSymbol();
        assertTrue((boolean) isAttackedMethod.invoke(state, new Position(2, 1), PieceType.DARK));
    }

    @Test
    void testIsAttackedByReflectionFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isAttackedMethod = KnightSwapState.class.getDeclaredMethod("isAttacked", Position.class, PieceType.class);
        isAttackedMethod.setAccessible(true);

        KnightSwapState state = new KnightSwapState();
        assertFalse((boolean) isAttackedMethod.invoke(state, new Position(1, 1), PieceType.DARK));
    }
}