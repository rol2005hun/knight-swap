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
import puzzle.TwoPhaseMoveState;

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

    private static final String DARK_SQUARE_STYLE = "-fx-background-color: #A0522D; -fx-background-radius: 0;";
    private static final String LIGHT_SQUARE_STYLE = "-fx-background-color: #FFF8DC; -fx-background-radius: 0;";
    private static final String HIGHLIGHT_STYLE = "-fx-background-color: #6B4226; -fx-background-radius: 0; -fx-border-color: yellow; -fx-border-width: 2;";

    private int movesMade;
    private String playerName;
    private Button[][] buttons;
    private Button firstClickButton = null;
    private Position firstClickPosition = null;

    private KnightSwapState gameState;
    private ScoreBoardManager scoreBoardManager;

    private final Map<Button, String> originalStyles = new HashMap<>();

    /**
     * Constructs a new {@code KnightSwapController}.
     * The FXML loader invokes this constructor.
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
        Logger.info("KnightSwap GUI controller initializing.");

        buttons = new Button[BOARD_ROWS][BOARD_COLS];
        setupGridPane();
    }

    /**
     * Initializes the controller's internal state and sets up the initial display.
     * This method should be called only after all necessary dependencies (such as data managers or external services)
     * and configuration parameters (like player name) have been successfully injected into the controller.
     */
    public void startGame() {
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Guest";
            Logger.warn("Player name not set, defaulting to 'Guest'. This should be set via setPlayerName().");
        } else {
            Logger.info("Starting game for player: {}", playerName);
        }

        if (scoreBoardManager == null) {
            Logger.error("ScoreBoardManager is null. Game cannot start correctly without it. Ensure it's injected.");
        }

        resetGame();
        Logger.info("KnightSwap game started for player: {}.", playerName);
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

        for (int row = 0; row < BOARD_ROWS; row++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPrefHeight(BUTTON_SIZE);
            rowConst.setVgrow(Priority.ALWAYS);
            boardGrid.getRowConstraints().add(rowConst);
        }
        Logger.debug("Row constraints for {} rows set.", BOARD_ROWS);

        for (int col = 0; col < BOARD_COLS; col++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPrefWidth(BUTTON_SIZE);
            colConst.setHgrow(Priority.ALWAYS);
            boardGrid.getColumnConstraints().add(colConst);
        }
        Logger.debug("Column constraints for {} columns set.", BOARD_COLS);

        for (int row = 0; row < BOARD_ROWS; row++) {
            for (int col = 0; col < BOARD_COLS; col++) {
                Button btn = new Button();
                btn.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
                btn.setFont(Font.font("Segoe UI Symbol", 45));
                btn.setOnAction(this::handleButtonClick);
                btn.setFocusTraversable(false);

                String style = ((row + col) % 2 == 0) ? DARK_SQUARE_STYLE : LIGHT_SQUARE_STYLE;
                btn.setStyle(style);
                originalStyles.put(btn, style);

                buttons[row][col] = btn;
                GridPane.setConstraints(btn, col, row);
                boardGrid.getChildren().add(btn);
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
        int clickedRow = GridPane.getRowIndex(clickedButton);
        int clickedCol = GridPane.getColumnIndex(clickedButton);
        Position clickedPosition = new Position(clickedRow, clickedCol);

        Logger.debug("Button clicked at ({}, {}).", clickedRow, clickedCol);

        if (firstClickButton == null) {
            handleFirstClick(clickedButton, clickedPosition);
        } else {
            handleSecondClick(clickedButton, clickedPosition);
        }
    }

    /**
     * Handles the first click in the two-click move sequence.
     * It selects a piece if it belongs to the current player.
     *
     * @param clickedButton The {@link Button} that was clicked.
     * @param clickedPosition The {@link Position} of the clicked button on the board.
     */
    private void handleFirstClick(Button clickedButton, Position clickedPosition) {
        if (gameState.isLegalToMoveFrom(clickedPosition)) {
            firstClickButton = clickedButton;
            firstClickPosition = clickedPosition;
            firstClickButton.setStyle(HIGHLIGHT_STYLE);
            statusLabel.setText("Selected: (" + clickedPosition.row() + ", " + clickedPosition.col() + "). Choose target.");
            Logger.info("Piece selected at {}. Current player: {}.", clickedPosition, gameState.getCurrentPlayer());
        } else {
            char pieceAtClickedPos = gameState.getPieceAt(clickedPosition.row(), clickedPosition.col());
            if (pieceAtClickedPos == '.') {
                statusLabel.setText("Empty square! Select a piece.");
                Logger.debug("Clicked on empty square at {}. No piece selected.", clickedPosition);
            } else {
                statusLabel.setText("Not your piece! (" + gameState.getCurrentPlayer() + " to move)");
                Logger.warn("Clicked on opponent's piece ({}) at {}. Current player: {}.", pieceAtClickedPos, clickedPosition, gameState.getCurrentPlayer());
            }
        }
    }

    /**
     * Handles the second click in the two-click move sequence.
     * Attempts to move the previously selected piece to the clicked position.
     * If the move is valid, it updates the game state and GUI; otherwise, it resets the selection.
     *
     * @param clickedButton The {@link Button} that was clicked as the target.
     * @param clickedPosition The {@link Position} of the target button on the board.
     */
    private void handleSecondClick(Button clickedButton, Position clickedPosition) {
        if (clickedButton == firstClickButton) {
            resetSelection();
            statusLabel.setText("Selection cancelled.");
            Logger.info("Selection at {} cancelled.", clickedPosition);
        } else {
            TwoPhaseMoveState.TwoPhaseMove<Position> currentMove = new TwoPhaseMoveState.TwoPhaseMove<>(firstClickPosition, clickedPosition);

            Logger.debug("Attempting to move from {} to {}. Move: {}", firstClickPosition, clickedPosition, currentMove);
            if (gameState.isLegalMove(currentMove)) {
                gameState.makeMove(currentMove);
                movesMade++;
                Logger.info("Successful move from {} to {}. Moves made: {}. Next player: {}.", firstClickPosition, clickedPosition, movesMade, gameState.getCurrentPlayer());

                resetSelection();
                updateBoard();
                updateScoreAndStatusLabels();

                if (gameState.isSolved()) {
                    handleGameSolved();
                }
            } else {
                Logger.warn("Illegal move attempted from {} to {}. Reason: Not a valid game move.",
                        firstClickPosition, clickedPosition);
                statusLabel.setText("Invalid move! Try again.");
                resetSelection();
            }
        }
    }

    /**
     * Resets the current piece selection, removing the highlight from the previously selected button.
     */
    private void resetSelection() {
        if (firstClickButton != null) {
            firstClickButton.setStyle(originalStyles.get(firstClickButton));
        }
        firstClickButton = null;
        firstClickPosition = null;
        Logger.debug("Piece selection reset.");
    }

    /**
     * Handles the game-solved state. Displays a congratulatory message,
     * disables further moves, and attempts to save the player's score.
     */
    private void handleGameSolved() {
        statusLabel.setText("Congratulations! Puzzle solved.");
        Logger.info("KnightSwap puzzle solved in {} moves by {}.", movesMade, playerName);
        disableAllButtons();

        if (scoreBoardManager != null) {
            scoreBoardManager.addOrUpdatePlayerScore(playerName, movesMade);
            Logger.info("Score for player '{}' updated/added: {} moves.", playerName, movesMade);
            updateScoreAndStatusLabels();
        } else {
            Logger.error("ScoreBoardManager is null, cannot save score for player {}. This should not happen if injected correctly.", playerName);
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
        resetGame();
        Logger.info("Game board reset successful. Moves reset to 0. Game ready for player: {}.", playerName);
    }

    /**
     * Resets the game to its initial state, including move count, game board, and UI elements.
     * This method is called upon initialization (now `startGame()`) and when the reset button is clicked.
     */
    private void resetGame() {
        movesMade = 0;
        gameState = new KnightSwapState();
        resetSelection();

        enableAllButtons();
        updateBoard();
        updateScoreAndStatusLabels();
        Logger.debug("Game state and UI reset.");
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
            statusLabel.setText(String.format("%s to move.", gameState.getCurrentPlayer()));
            Logger.debug("Status label set to indicate current player: {}.", gameState.getCurrentPlayer());
        }

        currentScoreLabel.setText(String.valueOf(movesMade));
        Logger.debug("Current score label set to: {}.", movesMade);

        if (scoreBoardManager != null && playerName != null && !playerName.isEmpty()) {
            Optional<PlayerScore> bestScore = scoreBoardManager.getPlayerScore(playerName);
            if (bestScore.isPresent()) {
                bestScoreLabel.setText(String.valueOf(bestScore.get().getBestScore()));
                Logger.debug("Best score label set to {} for player {}.", bestScore.get().getBestScore(), playerName);
            } else {
                bestScoreLabel.setText("0");
                Logger.debug("Best score label set to 0 (no previous score for player {}).", playerName);
            }
        } else {
            bestScoreLabel.setText("0");
            Logger.warn("Best score label set to 0. ScoreBoardManager or player name is unavailable (Manager available: {}, PlayerName available: {}).", scoreBoardManager != null, playerName != null);
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
    public void setPlayerName(String name) {
        this.playerName = name;
        Logger.debug("Player name set for KnightSwapController instance: {}.", name);
    }

    /**
     * Sets the {@link ScoreBoardManager} for this controller.
     * This method is used for Dependency Injection, allowing the controller
     * to interact with the game's score management system.
     *
     * @param scoreBoardManager The {@link ScoreBoardManager} instance to be used by this controller.
     */
    public void setScoreBoardManager(ScoreBoardManager scoreBoardManager) {
        this.scoreBoardManager = scoreBoardManager;
        Logger.debug("ScoreBoardManager injected into KnightSwapController.");
    }
}