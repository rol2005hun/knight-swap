package knightswap.gui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import knightswap.data.PlayerScore;
import knightswap.data.ScoreboardManager;
import knightswap.utils.GuiUtils;
import knightswap.utils.RankedPlayerScore;
import org.tinylog.Logger;

import java.util.List;

/**
 * Controller class for the Leaderboard screen, displaying top player scores.
 * It populates a {@link TableView} with ranked player data retrieved from
 * {@link ScoreboardManager} and manages the window's closure.
 */
public class LeaderboardController {
    @FXML
    private TableView<RankedPlayerScore> leaderboardTable;
    @FXML
    private TableColumn<RankedPlayerScore, Integer> rankColumn;
    @FXML
    private TableColumn<RankedPlayerScore, String> nameColumn;
    @FXML
    private TableColumn<RankedPlayerScore, Integer> scoreColumn;

    private Stage gameStage;

    private ScoreboardManager scoreboardManager;

    private final ObservableList<RankedPlayerScore> scores = FXCollections.observableArrayList();

    /**
     * Constructs a new {@code LeaderboardController}.
     * The FXML loader invokes this constructor.
     */
    public LeaderboardController() {
        Logger.debug("LeaderboardController instance created.");
    }

    /**
     * Sets the {@link ScoreboardManager} instance for this controller.
     * This method is used for Dependency Injection.
     *
     * @param scoreboardManager The {@link ScoreboardManager} instance.
     */
    public void setScoreboardManager(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        Logger.debug("ScoreboardManager injected into LeaderboardController.");
    }

    /**
     * Sets the main game {@link Stage} associated with this leaderboard window.
     * This stage will be re-shown when the leaderboard window is closed.
     *
     * @param gameStage The {@link Stage} instance of the main game board.
     */
    public void setGameStage(Stage gameStage) {
        this.gameStage = gameStage;
        Logger.debug("Game stage set for LeaderboardController.");
    }

    /**
     * Initializes the controller after all {@code @FXML} annotated fields are injected.
     * Configures the {@link TableColumn} cell value factories, sets up sorting,
     * and triggers the loading of player scores into the table.
     */
    @FXML
    private void initialize() {
        Logger.info("Leaderboard controller initializing.");

        rankColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().rank()));
        GuiUtils.setCenteredCellFactory(rankColumn);

        nameColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().playerScore().getPlayerName()));
        GuiUtils.setCenteredCellFactory(nameColumn);

        scoreColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().playerScore().getBestScore()));
        GuiUtils.setCenteredCellFactory(scoreColumn);

        leaderboardTable.getSortOrder().add(scoreColumn);
        scoreColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    /**
     * Loads the top player scores from the {@link ScoreboardManager} and populates
     * the {@link #leaderboardTable}.
     * The table is cleared before loading the top 100 scores.
     */
    public void loadScores() {
        scores.clear();
        Logger.debug("Clearing existing scores from leaderboard table.");

        List<PlayerScore> topScores = scoreboardManager.getTopScores(100);
        Logger.debug("Received {} top scores from ScoreboardManager.", topScores.size());

        int currentRank = 1;
        int previousScore = -1;
        if (!topScores.isEmpty()) {
            previousScore = topScores.getFirst().getBestScore();
        }

        for (int i = 0; i < topScores.size(); i++) {
            PlayerScore ps = topScores.get(i);

            if (i > 0 && ps.getBestScore() != previousScore) {
                currentRank = i + 1;
            }
            scores.add(new RankedPlayerScore(ps, currentRank));
            previousScore = ps.getBestScore();
        }

        Logger.debug("Added {} ranked scores to observable list.", scores.size());

        leaderboardTable.setItems(scores);
        Logger.info("Leaderboard scores loaded successfully. Ranks calculated dynamically.");
        leaderboardTable.sort();
    }

    /**
     * Handles the "Close" button click event on the leaderboard screen.
     * Closes the current leaderboard window. If the {@link #gameStage} reference
     * is available, it re-shows the main game screen; otherwise, it attempts to
     * load a new chessboard screen as a fallback.
     *
     * @param event The {@link ActionEvent} triggered by the button.
     */
    @FXML
    private void handleCloseButton(ActionEvent event) {
        Stage helpStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        helpStage.close();
        Logger.info("Leaderboard closed.");

        if (gameStage != null) {
            gameStage.show();
            Logger.info("Returned to main game screen (state preserved).");
        } else {
            Logger.warn("Game stage reference was null when closing leaderboard. Attempting fallback to main game screen.");
            GuiUtils.loadChessboardFallback(getClass());
        }
    }
}