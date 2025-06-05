package knightswap;

import org.tinylog.Logger;

/**
 * The main class for the KnightSwap puzzle game.
 */
public class ConsoleGame {
    public static void main(String[] args) {
        Logger.info("The application has been started.");

        KnightSwapState initialState = new KnightSwapState();
        Logger.info("Initial state created:\n{}", initialState);
    }
}