package knightswap.gui.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import knightswap.data.PlayerScore;
import knightswap.data.ScoreBoardManager;
import knightswap.utils.GuiUtils;
import org.tinylog.Logger;

import java.util.List;

/**
 * Controller class for the Leaderboard screen, displaying top player scores.
 * It populates a {@link TableView} with ranked player data retrieved from
 * {@link ScoreBoardManager} and manages the window's closure.
 */
public class LeaderBoardController {
    @FXML
    private TableView<PlayerScore> leaderboardTable;
    @FXML
    private TableColumn<PlayerScore, Integer> rankColumn;
    @FXML
    private TableColumn<PlayerScore, String> nameColumn;
    @FXML
    private TableColumn<PlayerScore, Integer> scoreColumn;

    private Stage gameStage;

    private final ObservableList<PlayerScore> scores = FXCollections.observableArrayList();

    /**
     * Constructs a new {@code LeaderBoardController}.
     * This constructor is invoked by the FXML loader.
     */
    public LeaderBoardController() {}

    /**
     * Sets the main game {@link Stage} associated with this leaderboard window.
     * This stage will be re-shown when the leaderboard window is closed.
     *
     * @param gameStage The {@link Stage} instance of the main game board.
     */
    public void setGameStage(Stage gameStage) {
        this.gameStage = gameStage;
    }

    /**
     * Initializes the controller after all {@code @FXML} annotated fields are injected.
     * Configures the {@link TableColumn} cell value factories, sets up sorting,
     * and triggers the loading of player scores into the table.
     */
    @FXML
    private void initialize() {
        rankColumn.setCellValueFactory(param -> {
            int currentRank = 1;
            int previousScore = -1;

            ObservableList<PlayerScore> items = param.getTableView().getItems();

            for (int i = 0; i < items.size(); i++) {
                PlayerScore currentItem = items.get(i);

                if (i > 0) {
                    if (currentItem.getBestScore() != previousScore) {
                        currentRank++;
                    }
                }

                previousScore = currentItem.getBestScore();

                if (currentItem == param.getValue()) {
                    return new ReadOnlyObjectWrapper<>(currentRank);
                }
            }
            return new ReadOnlyObjectWrapper<>(0);
        });
        GuiUtils.setCenteredCellFactory(rankColumn);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        GuiUtils.setCenteredCellFactory(nameColumn);


        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("bestScore"));
        GuiUtils.setCenteredCellFactory(scoreColumn);

        leaderboardTable.getSortOrder().add(scoreColumn);
        scoreColumn.setSortType(TableColumn.SortType.ASCENDING);

        loadScores();
    }

    /**
     * Loads the top player scores from the {@link ScoreBoardManager} and populates
     * the {@link #leaderboardTable}.
     * The table is cleared before loading the top 100 scores.
     */
    private void loadScores() {
        scores.clear();

        List<PlayerScore> topScores = ScoreBoardManager.getTopScores(100);
        scores.addAll(topScores);

        leaderboardTable.setItems(scores);
        Logger.info("Leaderboard scores loaded successfully. Ranks calculated dynamically.");
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
            GuiUtils.loadChessboardFallback(getClass());
        }
    }
}