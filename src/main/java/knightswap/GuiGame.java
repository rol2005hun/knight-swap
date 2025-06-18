package knightswap;

import javafx.application.Application;
import knightswap.gui.KnightSwapApplication;

/**
 * The main entry point for launching the graphical user interface (GUI)
 * of the Knight Swap puzzle game.
 * This class cannot be instantiated.
 */
public final class GuiGame {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GuiGame() {}

    /**
     * The main method that launches the JavaFX {@link KnightSwapApplication}.
     * This is the entry point for users to play the game with the GUI.
     *
     * @param args Command line arguments (not utilized by the application).
     */
    public static void main(String[] args) {
        Application.launch(KnightSwapApplication.class, args);
    }
}