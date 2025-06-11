package knightswap.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import knightswap.gui.KnightSwapApplication;
import org.tinylog.Logger;

/**
 * Controller class for the KnightSwap game board GUI.
 * Manages user interactions with the welcome screen,
 * displays the game's name, handles user's name input.
 */
public class WelcomeController {
    @FXML private TextField playerNameTextField;

    /**
     * Default constructor for the WelcomeController.
     * This class is typically instantiated by the FXML loader.
     */
    public WelcomeController() {}

    /**
     * Handles the action when the "Start Game" button is clicked.
     * This method validates the player's name input. If the name is valid (not empty),
     * it proceeds to show the main game screen, hiding the current welcome screen.
     * If the name is empty, it updates the text field's prompt to indicate
     * that a name is required.
     */
    @FXML
    private void handleStartGameButton() {
        String playerName = playerNameTextField.getText().trim();

        if (!playerName.isEmpty()) {
            try {
                // TODO: make more simple the stage saving with adding 2nd parameter to showGameScreen
                // Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                KnightSwapApplication.showGameScreen(playerName);
            } catch (Exception e) {
                Logger.error("Failed to start game: {}", e.getMessage());
            }
        } else {
            playerNameTextField.setPromptText("Name required!");
            Logger.warn("Player tried to start game with empty name.");
        }
    }
}