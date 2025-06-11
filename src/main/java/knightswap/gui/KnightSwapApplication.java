package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import knightswap.data.ScoreBoardManager;
import knightswap.gui.controllers.HelpController;
import knightswap.gui.controllers.KnightSwapController;
import knightswap.gui.controllers.LeaderBoardController;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Objects;

/**
 * The main application class for the Knight Swap GUI game.
 * This class extends {@link javafx.application.Application} and is responsible for
 * initializing and displaying the game's user interface.
 */
public class KnightSwapApplication extends Application {
    private static ScoreBoardManager scoreBoardManager;
    private static Stage primaryStage;

    /**
     * Default constructor for the KnightSwapApplication.
     * This class is typically instantiated by the JavaFX runtime.
     */
    public KnightSwapApplication() {}

    /**
     * Returns the singleton instance of the ScoreBoardManager.
     * If the instance does not exist yet, it creates and initializes it.
     * This method ensures that only one instance of ScoreBoardManager is ever created (Singleton pattern).
     *
     * @return The single instance of ScoreBoardManager.
     */
    public static ScoreBoardManager getScoreBoardManager() {
        if (scoreBoardManager == null) {
            scoreBoardManager = new ScoreBoardManager();
            Logger.info("ScoreBoardManager initialized.");
        }
        return scoreBoardManager;
    }

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

        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/logo.png")));
            stage.getIcons().add(icon);
            Logger.info("Application icon set successfully.");
        } catch (NullPointerException e) {
            Logger.error("ERROR: Application icon 'logo.png' not found." +
                    "Please verify the icon file's path and your build configuration.");
        } catch (Exception e) {
            Logger.error("Failed to load application icon: {}", e.getMessage());
        }

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
     * This screen allows for the user to play the puzzle game.
     *
     * @param playerName The player's name, who runs the application.
     * @throws IOException If the {@code chessboard.fxml} file cannot be loaded.
     */
    public static void showGameScreen(String playerName) throws IOException {
        KnightSwapController.setPlayerName(playerName);
        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/chessboard.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Knight Swap - Player: " + playerName);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        getScoreBoardManager();

        Logger.info("Game screen loaded!");
    }

    /**
     * Loads and displays the help screen of the application.
     * This screen allows for the user to get some help for solving the puzzle.
     *
     * @throws IOException If the {@code helpscreen.fxml} file cannot be loaded.
     */
    public static void showHelpScreen(Stage gameStageToReturnTo) throws IOException {
        gameStageToReturnTo.hide();
        Logger.info("Main game screen hidden to show help.");

        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/helpscreen.fxml"));
        Stage helpStage = new Stage();
        Scene helpScene = new Scene(loader.load());

        HelpController helpController = loader.getController();
        helpController.setGameStage(gameStageToReturnTo);

        helpStage.setTitle("Knight Swap - Help");
        helpStage.setScene(helpScene);
        helpStage.setResizable(false);
        helpStage.show();
        Logger.info("Help screen opened in a new window.");
    }

    /**
     * Loads and displays the leaderboard of the application.
     * This screen allows the user to check their global rank.
     *
     * @throws IOException If the {@code leaderboard.fxml} file cannot be loaded.
     */
    public static void showLeaderBoard(Stage gameStageToReturnTo) throws IOException {
        gameStageToReturnTo.hide();
        Logger.info("Main game screen hidden to show the leaderboard.");

        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/leaderboard.fxml"));
        Stage helpStage = new Stage();
        Scene helpScene = new Scene(loader.load());

        LeaderBoardController leaderBoardController = loader.getController();
        leaderBoardController.setGameStage(gameStageToReturnTo);

        helpStage.setTitle("Knight Swap - Leaderboard");
        helpStage.setScene(helpScene);
        helpStage.setResizable(false);
        helpStage.show();
        Logger.info("Help screen opened in a new window.");
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (scoreBoardManager != null) {
            scoreBoardManager.saveScores();
            Logger.info("Scores saved on application shutdown.");
        }
    }
}