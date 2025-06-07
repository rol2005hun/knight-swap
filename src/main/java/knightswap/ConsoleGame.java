package knightswap;

import puzzle.solver.BreadthFirstSearch;
import puzzle.solver.Node;
import org.tinylog.Logger;

import java.util.Optional;

/**
 * The main class which solves the KnightSwap problem with BFS algorithm.
 */
public final class ConsoleGame {
    /**
     * The main entry point of the KnightSwap puzzle solver application.
     * Initializes the puzzle, attempts to solve it using Breadth-First Search,
     * and logs the solution path length if found.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Logger.info("The KnightSwap game has been started.");

        KnightSwapState initialState = new KnightSwapState();
        Logger.info("Initial state created:\n{}", initialState);

        BreadthFirstSearch<String> bfsSolver = new BreadthFirstSearch<>();

        Logger.info("Starting Breadth-First Search to solve the puzzle...");

        Optional<Node<String>> solution = bfsSolver.solveAndPrintSolution(initialState);

        if (solution.isPresent()) {
            Node<String> solutionNode = solution.get();
            int steps = calculatePathLength(solutionNode);
            Logger.info("Solution found! Number of steps: {}.", steps);
        } else {
            Logger.warn("No solution found for the KnightSwap puzzle.");
        }

        Logger.info("KnightSwap puzzle solver application finished.");
    }

    /**
     * Helper method to calculate the length of the path leading to the solution.
     * It traverses the parent chain of Node objects until it reaches the root Node.
     *
     * @param node the Node containing the solution
     * @return the number of steps from the root Node to the solution Node
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