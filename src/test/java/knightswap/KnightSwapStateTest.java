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
    void testInitialNextPlayer() {
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
    void testIsLegalMoveValidInitialMoves() {
        assertTrue(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1))));
        assertTrue(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 2), new Position(1, 1))));
    }

    @Test
    void testIsLegalMoveOpponentPiece() {
        assertFalse(initialState.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(1, 2))));
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
        for (int r = 0; r < 4; r++) Arrays.fill(stateForAttackTest.board[r], '.');
        stateForAttackTest.board[0][0] = PieceType.DARK.getSymbol();
        stateForAttackTest.board[3][0] = PieceType.LIGHT.getSymbol();

        assertFalse(stateForAttackTest.isLegalMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(2, 1))));
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
    void testEqualsNullAndDifferentClass() {
        assertNotEquals(null, initialState);
        assertNotEquals(new Object(), initialState);
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
    void testGetNextPlayer() {
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer());
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(3, 0), new Position(1, 1)));
        assertEquals(PieceType.DARK, initialState.getCurrentPlayer());
        initialState.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 0), new Position(2, 1)));
        assertEquals(PieceType.LIGHT, initialState.getCurrentPlayer());
    }

    @Test
    void testIsAttackedTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isAttackedMethod = KnightSwapState.class.getDeclaredMethod("isAttacked", Position.class, PieceType.class);
        isAttackedMethod.setAccessible(true);

        assertFalse((boolean) isAttackedMethod.invoke(initialState, new Position(1, 1), PieceType.DARK));

        KnightSwapState stateForAttackTest = new KnightSwapState();
        for (int r = 0; r < 4; r++) Arrays.fill(stateForAttackTest.board[r], '.');
        stateForAttackTest.board[0][0] = PieceType.DARK.getSymbol();
        stateForAttackTest.board[2][1] = '.';
        stateForAttackTest.board[3][0] = '.';

        assertTrue((boolean) isAttackedMethod.invoke(stateForAttackTest, new Position(2, 1), PieceType.DARK));

        KnightSwapState stateForLightAttackTest = new KnightSwapState();
        stateForLightAttackTest.board[3][0] = PieceType.LIGHT.getSymbol();
        stateForLightAttackTest.board[1][1] = '.';
        stateForLightAttackTest.board[0][0] = '.';

        assertTrue((boolean) isAttackedMethod.invoke(stateForLightAttackTest, new Position(1, 1), PieceType.LIGHT));
    }

    @Test
    void testIsAttackedFalse() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isAttackedMethod = KnightSwapState.class.getDeclaredMethod("isAttacked", Position.class, PieceType.class);
        isAttackedMethod.setAccessible(true);

        assertFalse((boolean) isAttackedMethod.invoke(initialState, new Position(0, 0), PieceType.DARK));
        assertFalse((boolean) isAttackedMethod.invoke(initialState, new Position(1, 1), PieceType.DARK));

        KnightSwapState stateNoAttacker = new KnightSwapState();
        stateNoAttacker.board[0][0] = '.';
        stateNoAttacker.board[3][0] = PieceType.LIGHT.getSymbol();
        System.out.println(stateNoAttacker);

        assertFalse((boolean) isAttackedMethod.invoke(stateNoAttacker, new Position(1, 2), PieceType.DARK));
    }
}