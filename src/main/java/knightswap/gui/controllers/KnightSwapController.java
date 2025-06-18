package knightswap.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import knightswap.data.PlayerScore;
import knightswap.data.ScoreBoardManager;
import knightswap.gui.KnightSwapApplication;
import org.tinylog.Logger;

import knightswap.KnightSwapState;
import knightswap.utils.PieceType;
import knightswap.utils.Position;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller for the main KnightSwap game board graphical user interface.
 * Manages user input, updates the visual game state, and interacts with the game logic
 * provided by {@link KnightSwapState}.
 */
public class KnightSwapController {
    @FXML private Label currentScoreLabel;
    @FXML private Label bestScoreLabel;
    @FXML private Label statusLabel;
    @FXML private GridPane boardGrid;

    private static final int BOARD_ROWS = 4;
    private static final int BOARD_COLS = 3;
    private static final double BUTTON_SIZE = 110.0;

    private int movesMade;
    private static String playerName;
    private Button[][] buttons;
    private Button firstClickButton = null;
    private Position firstClickPosition = null;

    private KnightSwapState gameState;

    private final Map<Button, String> originalStyles = new HashMap<>();

    private static final String DARK_SQUARE_STYLE = "-fx-background-color: #A0522D; -fx-background-radius: 0;";
    private static final String LIGHT_SQUARE_STYLE = "-fx-background-color: #FFF8DC; -fx-background-radius: 0;";
    private static final String HIGHLIGHT_STYLE = "-fx-background-color: #6B4226; -fx-background-radius: 0; -fx-border-color: yellow; -fx-border-width: 2;";

    /**
     * Constructs a new {@code KnightSwapController}.
     * This constructor is invoked by the FXML loader.
     */
    public KnightSwapController() {
        Logger.debug("KnightSwapController instance created.");
    }

    /**
     * Initializes the controller after all {@code @FXML} annotated fields are injected.
     * Sets up the chessboard GUI, initializes the {@link KnightSwapState}, and updates initial display elements.
     */
    @FXML
    public void initialize() {
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Guest";
            Logger.warn("Player name not retrieved, defaulting to 'Guest'. This should be set by a prior screen.");
        } else {
            Logger.info("Game initialized for player: {}", playerName);
        }

        movesMade = 0;
        gameState = new KnightSwapState();
        Logger.debug("New game state initialized for game board.");

        buttons = new Button[BOARD_ROWS][BOARD_COLS];

        setupGridPane();
        Logger.debug("Chessboard buttons initialized and action handlers set.");

        updateBoard();
        updateScoreAndStatusLabels();
        Logger.info("KnightSwap GUI initialized and displayed.");
    }

    /**
     * Dynamically sets up the GridPane with buttons for the chessboard.
     * Creates ColumnConstraints and RowConstraints and adds buttons to the grid.
     */
    private void setupGridPane() {
        boardGrid.getChildren().clear();
        boardGrid.getColumnConstraints().clear();
        boardGrid.getRowConstraints().clear();
        Logger.debug("Clearing existing GridPane children and constraints for dynamic setup.");

        for (int col = 0; col < BOARD_COLS; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(BUTTON_SIZE);
            colConst.setHgrow(Priority.ALWAYS);
            boardGrid.getColumnConstraints().add(colConst);
        }
        Logger.debug("Column constraints for {} columns set.", BOARD_COLS);

        for (int row = 0; row < BOARD_ROWS; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(BUTTON_SIZE);
            rowConst.setVgrow(Priority.ALWAYS);
            boardGrid.getRowConstraints().add(rowConst);
        }
        Logger.debug("Row constraints for {} rows set.", BOARD_ROWS);

        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Button btn = new Button();
                btn.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
                btn.setFont(Font.font("Segoe UI Symbol", 45));
                btn.setOnAction(this::handleButtonClick);
                btn.setFocusTraversable(false);

                if ((row + col) % 2 == 0) {
                    btn.setStyle(DARK_SQUARE_STYLE);
                    originalStyles.put(btn, DARK_SQUARE_STYLE);
                } else {
                    btn.setStyle(LIGHT_SQUARE_STYLE);
                    originalStyles.put(btn, LIGHT_SQUARE_STYLE);
                }
                buttons[row][col] = btn;
                boardGrid.add(btn, col, row);
            }
        }
        Logger.info("Dynamically added {} buttons to the GridPane.", BOARD_ROWS * BOARD_COLS);
    }


    /**
     * Handles click events on the chessboard buttons.
     * Implements a two-click selection mechanism: the first click selects a piece,
     * and the second click attempts to move it to a new square.
     *
     * @param event The {@link ActionEvent} generated by the button click.
     */
    @FXML
    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        Integer clickedRowIndex = GridPane.getRowIndex(clickedButton);
        Integer clickedColIndex = GridPane.getColumnIndex(clickedButton);

        int clickedRow = (clickedRowIndex == null) ? 0 : clickedRowIndex;
        int clickedCol = (clickedColIndex == null) ? 0 : clickedColIndex;

        Position clickedPosition = new Position(clickedRow, clickedCol);

        Logger.debug("Button clicked at ({}, {}).", clickedRow, clickedCol);

        if (firstClickButton == null) {
            char pieceAtClickedPos = gameState.getPieceAt(clickedRow, clickedCol);

            if ((gameState.getCurrentPlayer() == PieceType.LIGHT && pieceAtClickedPos == PieceType.LIGHT.getSymbol()) ||
                    (gameState.getCurrentPlayer() == PieceType.DARK && pieceAtClickedPos == PieceType.DARK.getSymbol())) {

                firstClickButton = clickedButton;
                firstClickPosition = clickedPosition;
                firstClickButton.setStyle(HIGHLIGHT_STYLE);
                statusLabel.setText("Selected: (" + clickedRow + ", " + clickedCol + "). Choose target.");
                Logger.info("Piece selected at ({}, {}). Current player: {}.", clickedRow, clickedCol, gameState.getCurrentPlayer());
            } else if (pieceAtClickedPos == '.') {
                statusLabel.setText("Empty square! Select a piece.");
                Logger.debug("Clicked on empty square at ({}, {}). No piece selected.", clickedRow, clickedCol);
            } else {
                statusLabel.setText("Not your piece! (" + gameState.getCurrentPlayer() + " to move)");
                Logger.warn("Clicked on opponent's piece ({}) at ({}, {}). Current player: {}.", pieceAtClickedPos, clickedRow, clickedCol, gameState.getCurrentPlayer());
            }
        } else {
            if (clickedButton == firstClickButton) {
                firstClickButton.setStyle(originalStyles.get(firstClickButton));
                firstClickButton = null;
                firstClickPosition = null;
                statusLabel.setText("Selection cancelled.");
                Logger.info("Selection at ({}, {}) cancelled.", clickedRow, clickedCol);
            }
            else {
                String moveString = String.format("%d %d %d %d",
                        firstClickPosition.row(), firstClickPosition.col(),
                        clickedPosition.row(), clickedPosition.col());

                Logger.debug("Attempting to move from {} to {}. Move string: {}", firstClickPosition, clickedPosition, moveString);
                if (gameState.isLegalMove(moveString)) {
                    gameState.makeMove(moveString);
                    movesMade++;
                    Logger.info("Successful move from {} to {}. Moves made: {}. Next player: {}.",
                            firstClickPosition, clickedPosition, movesMade, gameState.getCurrentPlayer());

                    firstClickButton.setStyle(originalStyles.get(firstClickButton));
                    firstClickButton = null;
                    firstClickPosition = null;

                    updateBoard();
                    updateScoreAndStatusLabels();

                    if (gameState.isSolved()) {
                        statusLabel.setText("Congratulations! Puzzle solved.");
                        Logger.info("KnightSwap puzzle solved in {} moves by {}.", movesMade, playerName);
                        disableAllButtons();

                        ScoreBoardManager manager = KnightSwapApplication.getScoreBoardManager();
                        if (manager != null) {
                            manager.addOrUpdatePlayerScore(playerName, movesMade);
                            Logger.info("Score for player '{}' updated/added: {} moves.", playerName, movesMade);
                            updateScoreAndStatusLabels();
                        } else {
                            Logger.error("ScoreBoardManager is null, cannot save score for player {}. This indicates a critical application setup issue.", playerName);
                        }
                    }
                } else {
                    Logger.warn("Illegal move attempted from {} to {}. Reason: Not a valid game move.",
                            firstClickPosition, clickedPosition);
                    statusLabel.setText("Invalid move! Try again.");
                    firstClickButton.setStyle(originalStyles.get(firstClickButton));
                    firstClickButton = null;
                    firstClickPosition = null;
                }
            }
        }
    }

    /**
     * Displays the help screen with game solution steps.
     * The current game board window is hidden when the help screen appears.
     *
     * @param event The {@link ActionEvent} triggered by the "Help" button.
     * @throws IOException If the FXML for the help screen cannot be loaded.
     */
    public void showHelpScreen(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Logger.debug("Request to show help screen from game stage.");
        KnightSwapApplication.showHelpScreen(currentStage);
    }

    /**
     * Displays the leaderboard screen.
     * The current game board window is hidden when the leaderboard screen appears.
     *
     * @param event The {@link ActionEvent} triggered by the "Leaderboard" button.
     * @throws IOException If the FXML for the leaderboard screen cannot be loaded.
     */
    public void showLeaderBoard(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Logger.debug("Request to show leaderboard from game stage.");
        KnightSwapApplication.showLeaderBoard(currentStage);
    }

    /**
     * Resets the game board to its initial configuration and resets the move counter.
     * This method is called when the "Reset" button is clicked.
     */
    @FXML
    private void handleResetButton() {
        Logger.info("Reset button clicked. Resetting game state.");
        movesMade = 0;
        gameState = new KnightSwapState();
        firstClickButton = null;
        firstClickPosition = null;

        enableAllButtons();

        updateBoard();
        updateScoreAndStatusLabels();
        Logger.info("Game board reset successful. Moves reset to 0. Game ready for player: {}.", playerName);
    }

    /**
     * Updates the visual appearance of the chessboard based on the current {@link KnightSwapState}.
     * Sets the correct piece symbols and square colors for all buttons.
     */
    private void updateBoard() {
        Logger.debug("Updating chessboard visuals.");
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                char pieceChar = gameState.getPieceAt(row, col);
                String pieceSymbol;

                if (pieceChar == PieceType.LIGHT.getSymbol()) {
                    pieceSymbol = "♘";
                } else if (pieceChar == PieceType.DARK.getSymbol()) {
                    pieceSymbol = "♞";
                } else {
                    pieceSymbol = "";
                }
                buttons[row][col].setText(pieceSymbol);
                buttons[row][col].setStyle(originalStyles.get(buttons[row][col]));
            }
        }

        if (firstClickButton != null) {
            firstClickButton.setStyle(HIGHLIGHT_STYLE);
            Logger.debug("Retaining highlight on selected button at {}.", firstClickPosition);
        }
        Logger.debug("Chessboard visuals updated successfully.");
    }

    /**
     * Updates the {@code currentScoreLabel}, {@code bestScoreLabel}, and {@code statusLabel}
     * with current game information.
     * Displays the current player's turn, moves made, and the player's best score.
     */
    private void updateScoreAndStatusLabels() {
        Logger.debug("Updating score and status labels.");
        if (gameState.isSolved()) {
            statusLabel.setText("Congratulations! Puzzle solved.");
            Logger.debug("Status label set to 'Puzzle solved'.");
        } else {
            statusLabel.setText(String.format("%s to move.", gameState.getCurrentPlayer() == PieceType.LIGHT ? "Light" : "Dark"));
            Logger.debug("Status label set to indicate current player: {}.", gameState.getCurrentPlayer());
        }

        currentScoreLabel.setText(String.valueOf(movesMade));
        Logger.debug("Current score label set to: {}.", movesMade);

        ScoreBoardManager manager = KnightSwapApplication.getScoreBoardManager();
        if (manager != null && playerName != null && !playerName.isEmpty()) {
            Optional<PlayerScore> bestScore = manager.getPlayerScore(playerName);
            if (bestScore.isPresent()) {
                bestScoreLabel.setText(String.valueOf(bestScore.get().getBestScore()));
                Logger.debug("Best score label set to {} for player {}.", bestScore.get().getBestScore(), playerName);
            } else {
                bestScoreLabel.setText("0");
                Logger.debug("Best score label set to 0 (no previous score for player {}).", playerName);
            }
        } else {
            bestScoreLabel.setText("0");
            Logger.warn("Best score label set to 0. ScoreBoardManager or player name is unavailable (Manager available: {}, PlayerName available: {}).", manager != null, playerName != null);
        }
    }

    /**
     * Disables all chessboard buttons, preventing further interaction.
     * This is typically used when the puzzle is solved.
     */
    private void disableAllButtons() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                buttons[row][col].setDisable(true);
            }
        }
        Logger.debug("All chessboard buttons disabled.");
    }

    /**
     * Enables all chessboard buttons, allowing user interaction.
     * This is typically used when resetting the puzzle.
     */
    private void enableAllButtons() {
        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                buttons[row][col].setDisable(false);
            }
        }
        Logger.debug("All chessboard buttons enabled.");
    }

    /**
     * Sets the name of the current player. This static method is typically called
     * from another controller (e.g., a login or start screen) to set the player's identity
     * before the game board is initialized.
     *
     * @param name The {@link String} name of the player.
     */
    public static void setPlayerName(String name) {
        playerName = name;
        Logger.debug("Player name static field set to: {}.", name);
    }
}