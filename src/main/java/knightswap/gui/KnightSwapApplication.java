package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import knightswap.data.ScoreBoardManager;
import knightswap.gui.controllers.HelpController;
import knightswap.gui.controllers.KnightSwapController;
import knightswap.gui.controllers.LeaderBoardController;
import knightswap.util.GuiUtils;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The main application class for the Knight Swap GUI game.
 * This class extends {@link javafx.application.Application} and is responsible for
 * initializing and displaying the game's user interface. It acts as the central
 * orchestrator for scene management and provides access to shared resources like
 * the {@link ScoreBoardManager}.
 */
public class KnightSwapApplication extends Application {
    private static ScoreBoardManager scoreBoardManager;
    private static Stage primaryStage;

    /**
     * Default constructor for the KnightSwapApplication.
     * This constructor is automatically called by the JavaFX runtime
     * when the application is launched.
     */
    public KnightSwapApplication() {}

    /**
     * Returns the singleton instance of the {@link ScoreBoardManager}.
     * If the instance does not exist yet, it creates and initializes it.
     * This method ensures that only one instance of {@link ScoreBoardManager} is ever created (Singleton pattern),
     * providing a centralized point for managing game scores.
     *
     * @return The single instance of {@link ScoreBoardManager}.
     */
    public static ScoreBoardManager getScoreBoardManager() {
        if (scoreBoardManager == null) {
            scoreBoardManager = new ScoreBoardManager();
            Logger.info("ScoreBoardManager initialized.");
        }
        return scoreBoardManager;
    }

    /**
     * The entry point for the JavaFX application. This method is called by the
     * JavaFX runtime after the system is ready for the application to begin.
     * It sets up the primary stage, applies the application icon, and displays
     * the initial welcome screen.
     *
     * @param stage The primary stage for this application, onto which
     * the application scene can be set. This stage is provided by the
     * JavaFX runtime.
     * @throws IOException If the {@code welcomescreen.fxml} file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        GuiUtils.setStageIcon(stage, getClass());

        showWelcomeScreen();
    }

    /**
     * Loads and displays the welcome screen of the application on the primary stage.
     * This screen allows the user to enter their name before starting the game.
     *
     * @throws IOException If the {@code /welcomescreen.fxml} file cannot be loaded from resources.
     */
    public static void showWelcomeScreen() throws IOException {
        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/welcomescreen.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Logger.info("Welcome screen loaded!");
    }

    /**
     * Loads and displays the main game screen (chessboard) of the application on the primary stage.
     * This method sets the player's name and initializes the game UI.
     *
     * @param playerName The name of the player who is starting the game.
     * @throws IOException If the {@code /chessboard.fxml} file cannot be loaded from resources.
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
     * Loads and displays the help screen of the application in a new dedicated window.
     * The main game screen is hidden temporarily to give focus to the help screen.
     * The {@link HelpController} is configured to re-show the game stage upon closing the help screen.
     *
     * @param gameStageToReturnTo The {@link javafx.stage.Stage} of the currently active game screen,
     * which will be hidden and then re-shown when the help screen is closed.
     * @throws IOException If the {@code /helpscreen.fxml} file cannot be loaded from resources.
     */
    public static void showHelpScreen(Stage gameStageToReturnTo) throws IOException {
        gameStageToReturnTo.hide();
        Logger.info("Main game screen hidden to show help.");

        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/helpscreen.fxml"));
        Stage helpStage = new Stage();
        Scene helpScene = new Scene(loader.load());

        GuiUtils.setStageIcon(helpStage, KnightSwapApplication.class);

        HelpController helpController = loader.getController();
        helpController.setGameStage(gameStageToReturnTo);

        helpStage.setTitle("Knight Swap - Help");
        helpStage.setScene(helpScene);
        helpStage.setResizable(false);
        helpStage.show();
        Logger.info("Help screen opened in a new window.");
    }

    /**
     * Loads and displays the leaderboard screen of the application in a new dedicated window.
     * The main game screen is hidden temporarily to give focus to the leaderboard.
     * The {@link LeaderBoardController} is configured to re-show the game stage upon closing the leaderboard.
     *
     * @param gameStageToReturnTo The {@link javafx.stage.Stage} of the currently active game screen,
     * which will be hidden and then re-shown when the leaderboard is closed.
     * @throws IOException If the {@code /leaderboard.fxml} file cannot be loaded from resources.
     */
    public static void showLeaderBoard(Stage gameStageToReturnTo) throws IOException {
        gameStageToReturnTo.hide();
        Logger.info("Main game screen hidden to show the leaderboard.");

        FXMLLoader loader = new FXMLLoader(KnightSwapApplication.class.getResource("/leaderboard.fxml"));
        Stage helpStage = new Stage();
        Scene helpScene = new Scene(loader.load());

        GuiUtils.setStageIcon(helpStage, KnightSwapApplication.class);

        LeaderBoardController leaderBoardController = loader.getController();
        leaderBoardController.setGameStage(gameStageToReturnTo);

        helpStage.setTitle("Knight Swap - Leaderboard");
        helpStage.setScene(helpScene);
        helpStage.setResizable(false);
        helpStage.show();
        Logger.info("Leaderboard opened in a new window.");
    }

    /**
     * This method is called when the application is stopped, either by the user
     * closing the window or by the system shutting down. It ensures that the
     * game scores are saved before the application fully exits.
     *
     * @throws Exception If an error occurs during the shutdown process or score saving.
     */
    @Override
    public void stop() throws Exception {
        super.stop();

        if (scoreBoardManager != null) {
            scoreBoardManager.saveScores();
            Logger.info("Scores saved on application shutdown.");
        }
    }
}