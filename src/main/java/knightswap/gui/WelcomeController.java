package knightswap.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.tinylog.Logger;

/**
 * Controller class for the KnightSwap game board GUI.
 * Manages user interactions with the welcome screen,
 * displays the game's name, handles user's name input.
 */
public class WelcomeController {
    @FXML private TextField playerNameTextField;
    @FXML private Button startGameButton;

    /**
     * Default constructor for the WelcomeController.
     * This class is typically instantiated by the FXML loader.
     */
    public WelcomeController() {}

    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is automatically called by the FXML loader.
     */
    @FXML
    public void initialize() {
        startGameButton.setOnAction(_ -> {
            String playerName = playerNameTextField.getText().trim();

            if (!playerName.isEmpty()) {
                try {
                    KnightSwapApplication.showGameScreen(playerName);
                } catch (Exception e) {
                    Logger.error(e);
                }
            } else {
                playerNameTextField.setPromptText("Name required!");
            }
        });
    }
}