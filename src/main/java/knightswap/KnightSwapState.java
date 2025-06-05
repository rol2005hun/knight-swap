package knightswap;

import puzzle.State;
import java.util.Set;

/**
 * Represents the state of the KnightSwap puzzle game.
 */
public class KnightSwapState implements State<String> {


    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public Set<String> getLegalMoves() {
        return Set.of();
    }

    @Override
    public State<String> clone() {
        return null;
    }

    @Override
    public boolean isLegalMove(String s) {
        return false;
    }

    @Override
    public void makeMove(String s) {

    }
}