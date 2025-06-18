package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import knightswap.data.ScoreBoardManager;
import knightswap.gui.controllers.HelpController;
import knightswap.gui.controllers.KnightSwapController;
import knightswap.gui.controllers.LeaderBoardController;
import knightswap.utils.GuiUtils;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * The main application class for the Knight Swap GUI game.
 * This class extends {@link Application} and orchestrates the display of various scenes,
 * providing central access to the {@link ScoreBoardManager}.
 */
public class KnightSwapApplication extends Application {
    private static ScoreBoardManager scoreBoardManager;
    private static Stage primaryStage;

    /**
     * Constructs a new {@code KnightSwapApplication}.
     * This constructor is automatically invoked by the JavaFX runtime.
     */
    public KnightSwapApplication() {}

    /**
     * Retrieves the singleton instance of the {@link ScoreBoardManager}.
     * The manager is initialized upon its first request.
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
     * The entry point for the JavaFX application.
     * Sets up the primary stage and displays the initial welcome screen.
     *
     * @param stage The primary {@link Stage} for this application.
     * @throws IOException If the {@code welcomescreen.fxml} file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        GuiUtils.setStageIcon(stage, getClass());

        showWelcomeScreen();
    }

    /**
     * Loads and displays the welcome screen on the {@link #primaryStage}.
     *
     * @throws IOException If the {@code /welcomescreen.fxml} file cannot be loaded.
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
     * Loads and displays the main game board screen on the {@link #primaryStage}.
     * Sets the player's name via {@link KnightSwapController#setPlayerName(String)}
     * and ensures the {@link ScoreBoardManager} is initialized.
     *
     * @param playerName The {@link String} name of the player.
     * @throws IOException If the {@code /chessboard.fxml} file cannot be loaded.
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
     * Loads and displays the leaderboard screen in a new window.
     * The main game screen (given by {@code gameStageToReturnTo}) is hidden
     * and will be re-shown when the leaderboard is closed.
     *
     * @param gameStageToReturnTo The {@link Stage} of the main game screen to return to.
     * @throws IOException If the {@code /leaderboard.fxml} file cannot be loaded.
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
     * Called when the application is stopped.
     * Ensures that the game scores are saved via {@link ScoreBoardManager#saveScores()}
     * before the application fully exits.
     *
     * @throws Exception If an error occurs during shutdown.
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