package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import knightswap.data.ScoreboardManager;
import knightswap.gui.controllers.HelpController;
import knightswap.gui.controllers.KnightSwapController;
import knightswap.gui.controllers.LeaderboardController;
import knightswap.utils.GuiUtils;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The main application class for the Knight Swap GUI game.
 * This class extends {@link Application} and orchestrates the display of various scenes,
 * providing central access to the {@link ScoreboardManager}.
 */
public class KnightSwapApplication extends Application {
    private static ScoreboardManager scoreboardManager;
    private static Stage primaryStage;

    /**
     * Constructs a new {@code KnightSwapApplication}.
     * This constructor is automatically invoked by the JavaFX runtime.
     */
    public KnightSwapApplication() {}

    /**
     * Retrieves the singleton instance of the {@link ScoreboardManager}.
     * The manager is initialized upon its first request.
     *
     * @return The single instance of {@link ScoreboardManager}.
     */
    public static ScoreboardManager getScoreboardManager() {
        if (scoreboardManager == null) {
            scoreboardManager = new ScoreboardManager();
            Logger.info("ScoreboardManager initialized.");
        }
        return scoreboardManager;
    }

    /**
     * The entry point for the JavaFX application.
     * Sets up the primary stage and displays the initial welcome screen.
     *
     * @param stage The primary {@link Stage} for this application.
     * @throws Exception If an error occurs during the application start.
     */
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Logger.debug("Application starting. Primary stage initialized.");
        GuiUtils.setStageIcon(primaryStage, getClass());
        showWelcomeScreen();
    }

    /**
     * Loads and displays the welcome screen on the {@link #primaryStage}.
     *
     * @throws IOException If the {@code /welcomescreen.fxml} file cannot be loaded.
     */
    public static void showWelcomeScreen() throws IOException {
        Logger.info("Attempting to load welcome screen.");
        GuiUtils.loadAndSetScene("/welcomescreen.fxml", "Welcome!", primaryStage, null);
        Logger.info("Welcome screen loaded successfully.");
    }

    /**
     * Loads and displays the main game board screen on the {@link #primaryStage}.
     * Sets the player's name via {@link KnightSwapController#setPlayerName(String)}
     * and ensures the {@link ScoreboardManager} is initialized.
     *
     * @param playerName The {@link String} name of the player.
     * @throws IOException If the {@code /chessboard.fxml} file cannot be loaded.
     */
    public static void showGameScreen(String playerName) throws IOException {
        Logger.info("Attempting to load game screen for player: {}.", playerName);

        FXMLLoader loader = GuiUtils.loadAndSetScene("/chessboard.fxml", "Knight Swap - Player: " + playerName, primaryStage, null);

        KnightSwapController gameController = loader.getController();

        gameController.setPlayerName(playerName);
        Logger.debug("KnightSwapController player name injected.");

        gameController.setScoreboardManager(getScoreboardManager());
        Logger.debug("ScoreboardManager injected into KnightSwapController.");

        gameController.startGame();

        Logger.info("Game screen loaded successfully for player: {}.", playerName);
    }

    /**
     * Loads and displays the help screen in a new window.
     * The main game screen (given by {@code gameStageToReturnTo}) is hidden
     * and will be re-shown when the help screen is closed.
     *
     * @param gameStageToReturnTo The {@link Stage} of the main game screen to return to.
     * @throws IOException If the {@code /helpscreen.fxml} file cannot be loaded.
     */
    public static void showHelpScreen(Stage gameStageToReturnTo) throws IOException {
        gameStageToReturnTo.hide();
        Logger.info("Main game screen hidden to show help.");

        Stage helpStage = new Stage();
        GuiUtils.setStageIcon(helpStage, GuiUtils.class);
        Logger.debug("Attempting to load help screen in new stage.");

        GuiUtils.loadAndSetScene("/helpscreen.fxml", "Knight Swap - Help", helpStage, controller -> {
            if (controller instanceof HelpController) {
                ((HelpController) controller).setGameStage(gameStageToReturnTo);
                Logger.debug("HelpController game stage set.");
            } else {
                Logger.warn("Loaded controller for helpscreen.fxml is not a HelpController. Cannot set game stage.");
            }
        });
        Logger.info("Help screen opened in a new window.");
    }

    /**
     * Loads and displays the leaderboard screen in a new window.
     * The main game screen (given by {@code gameStageToReturnTo}) is hidden
     * and will be re-shown when the leaderboard is closed.
     *
     * @param gameStageToReturnTo The {@link Stage} of the main game screen to return to.
     * @throws IOException If the {@code /leaderboard.fxml} file cannot be loaded.
     */
    public static void showLeaderboard(Stage gameStageToReturnTo) throws IOException {
        gameStageToReturnTo.hide();
        Logger.info("Main game screen hidden to show the leaderboard.");

        Stage leaderboardStage = new Stage();
        GuiUtils.setStageIcon(leaderboardStage, GuiUtils.class);
        Logger.debug("Attempting to load leaderboard screen in new stage.");

        GuiUtils.loadAndSetScene("/leaderboard.fxml", "Knight Swap - Leaderboard", leaderboardStage, controller -> {
            if (controller instanceof LeaderboardController leaderboardController) {
                leaderboardController.setGameStage(gameStageToReturnTo);
                Logger.debug("LeaderboardController game stage set.");

                leaderboardController.setScoreboardManager(getScoreboardManager());
                leaderboardController.loadScores();
            } else {
                Logger.warn("Loaded controller for leaderboard.fxml is not a LeaderboardController. Cannot set game stage.");
            }
        });

        Logger.info("Leaderboard opened in a new window.");
    }

    /**
     * Called when the application is stopped.
     * Ensures that the game scores are saved via {@link ScoreboardManager#saveScores()}
     * before the application fully exits.
     *
     * @throws Exception If an error occurs during shutdown.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        Logger.info("Application is shutting down.");

        if (scoreboardManager != null) {
            scoreboardManager.saveScores();
            Logger.info("Scores saved on application shutdown.");
        } else {
            Logger.warn("ScoreboardManager was null on shutdown, no scores to save.");
        }
    }
}