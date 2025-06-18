package knightswap.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import knightswap.gui.KnightSwapApplication;
import org.tinylog.Logger;

/**
 * Controller for the Welcome screen of the Knight Swap application.
 * Manages user interaction for entering a player name and initiating the game.
 */
public class WelcomeController {
    @FXML private TextField playerNameTextField;

    /**
     * Constructs a new {@code WelcomeController}.
     * This constructor is invoked by the FXML loader.
     */
    public WelcomeController() {
        Logger.debug("WelcomeController instance created.");
    }

    /**
     * Handles the action when the "Start Game" button is clicked.
     * Validates the entered player name: if not empty, it proceeds to load
     * and display the main game board; otherwise, it prompts the user to enter a name.
     */
    @FXML
    private void handleStartGameButton() {
        String playerName = playerNameTextField.getText().trim();

        if (!playerName.isEmpty()) {
            try {
                KnightSwapController.setPlayerName(playerName);
                KnightSwapApplication.showGameScreen(playerName);
                Logger.info("Game screen successfully loaded for player: {}", playerName);
            } catch (Exception e) {
                Logger.error("Failed to load game screen for player {}: {}", playerName, e.getMessage(), e);
            }
        } else {
            playerNameTextField.setPromptText("Name required!");
            Logger.warn("Player attempted to start game with an empty name. Prompting for name.");
        }
    }
}