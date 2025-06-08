package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The main application class for the Knight Swap GUI game.
 * This class extends {@link javafx.application.Application} and is responsible for
 * initializing and displaying the game's user interface.
 */
public class KnightSwapApplication extends Application {
    private static Stage primaryStage;

    /**
     * Public constructor to prevent instantiation of this utility class.
     * This class contains only static methods and should not be instantiated.
     */
    public KnightSwapApplication() {}

    /**
     * The entry point for the JavaFX application. This method is called after the
     * system is ready for the application to begin. It loads the FXML file for
     * the chessboard, sets up the primary stage, and displays the game window.
     *
     * @param stage The primary stage for this application, onto which
     * the application scene can be set. The stage is provided by the
     * JavaFX runtime.
     * @throws IOException If the {@code welcomescreen.fxml or chessboard.fxml} file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showWelcomeScreen();
    }

    /**
     * Loads and displays the welcome screen of the application.
     * This screen allows for the user to enter his name, and after start the game.
     *
     * @throws IOException If the {@code welcomescreen.fxml} file cannot be loaded.
     */
    public static void showWelcomeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/welcomescreen.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Knight Swap - Welcome");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Logger.info("Welcome screen loaded!");
    }

    /**
     * Loads and displays the welcome screen of the application.
     * This screen allows for the user to enter his name, and after start the game.
     *
     * @param playerName The player's name, who runs the application.
     * @throws IOException If the {@code chessboard.fxml} file cannot be loaded.
     */
    public static void showGameScreen(String playerName) throws IOException {
        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/chessboard.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Knight Swap - Player: " + playerName);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Logger.info("Game screen loaded!");
    }
}