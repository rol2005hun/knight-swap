package knightswap;

import javafx.application.Application;
import knightswap.gui.KnightSwapApplication;

/**
 * The GUI main class which runs the application for users to play.
 */
public class GuiGame {
    /**
     * Private constructor to prevent instantiation of this utility class.
     * This class contains only static methods and should not be instantiated.
     */
    private GuiGame() {}

    /**
     * The main entry point of the KnightSwap puzzle GUI application.
     * The users can play the game with running this class.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        Application.launch(KnightSwapApplication.class, args);
    }
}
