package knightswap;

import puzzle.TwoPhaseMoveState;
import knightswap.utils.Position;
import puzzle.solver.BreadthFirstSearch;
import puzzle.solver.Node;
import org.tinylog.Logger;

import java.util.Optional;

/**
 * Provides a command-line interface for solving the KnightSwap puzzle
 * using the Breadth-First Search (BFS) algorithm.
 * This class cannot be instantiated.
 */
public final class ConsoleGame {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ConsoleGame() {}

    /**
     * The main entry point for the console-based KnightSwap puzzle solver.
     * Initializes the puzzle, applies a BFS algorithm to find a solution,
     * and logs the outcome, including the solution path length if found.
     *
     * @param args Command line arguments (not utilized).
     */
    public static void main(String[] args) {
        Logger.info("The KnightSwap game has been started.");

        KnightSwapState initialState = new KnightSwapState();
        Logger.debug("Initial state created:\n{}", initialState);

        BreadthFirstSearch<TwoPhaseMoveState.TwoPhaseMove<Position>> bfsSolver = new BreadthFirstSearch<>();

        Logger.info("Starting Breadth-First Search to solve the puzzle...");

        Optional<Node<TwoPhaseMoveState.TwoPhaseMove<Position>>> solution = bfsSolver.solveAndPrintSolution(initialState);
        Logger.debug("BFS solution process completed.");

        if (solution.isPresent()) {
            Node<TwoPhaseMoveState.TwoPhaseMove<Position>> solutionNode = solution.get();
            int steps = calculatePathLength(solutionNode);
            Logger.info("Solution found! Number of steps: {}.", steps);
        } else {
            Logger.warn("No solution found for the KnightSwap puzzle.");
        }

        Logger.info("KnightSwap puzzle solver application finished.");
    }

    /**
     * Calculates the number of steps (moves) from the initial state to the given solution {@link Node}.
     * This is determined by traversing the parent chain up to the root node.
     *
     * @param node The {@link Node} representing the solution state.
     * @return The {@code int} number of steps in the solution path.
     */
    private static int calculatePathLength(Node<?> node) {
        int count = 0;
        Node<?> current = node;
        while (current.getParent().isPresent()) {
            count++;
            current = current.getParent().get();
        }
        return count;
    }
}