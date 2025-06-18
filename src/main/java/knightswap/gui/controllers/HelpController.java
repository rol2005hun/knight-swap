package knightswap.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import knightswap.utils.GuiUtils;
import org.tinylog.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Help screen, displaying game solution steps.
 * Manages the help window's lifecycle, including its display and closure,
 * and ensures proper return to the main game screen.
 */
public class HelpController implements Initializable {
    @FXML
    private TextArea solutionStepsTextArea;

    private Stage gameStage;

    /**
     * Constructs a new {@code HelpController}.
     * This constructor is invoked by the FXML loader.
     */
    public HelpController() {}

    /**
     * Sets the main game {@link Stage} associated with this help window.
     * This stage will be re-shown when the help window is closed.
     *
     * @param gameStage The {@link Stage} instance of the main game board.
     */
    public void setGameStage(Stage gameStage) {
        this.gameStage = gameStage;
    }

    /**
     * Initializes the controller and populates the text area with predefined solution steps.
     * This method is automatically called by the FXML loader after all {@code @FXML} fields are injected.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param rb The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String solution = """
            (3, 0) -> (1, 1)
            (0, 1) -> (2, 2)
            (3, 2) -> (2, 0)
            (0, 0) -> (2, 1)
            (3, 1) -> (1, 2)
            (0, 2) -> (1, 0)
            (1, 1) -> (3, 2)
            (2, 1) -> (0, 2)
            (1, 2) -> (0, 0)
            (2, 2) -> (3, 0)
            (2, 0) -> (1, 2)
            (1, 0) -> (2, 2)
            (3, 2) -> (2, 0)
            (0, 2) -> (1, 0)
            (0, 0) -> (2, 1)
            (3, 0) -> (1, 1)
            (1, 2) -> (0, 0)
            (1, 0) -> (3, 1)
            (2, 1) -> (0, 2)
            (2, 2) -> (3, 0)
            (2, 0) -> (0, 1)
            (1, 1) -> (3, 2)
        """;

        solutionStepsTextArea.setText(solution.stripIndent().indent(0));
    }

    /**
     * Handles the "Close" button click event.
     * This method closes the current help window. If the {@link #gameStage} reference
     * is available, it re-shows the main game screen; otherwise, it attempts to
     * load a new chessboard screen as a fallback.
     *
     * @param event The {@link ActionEvent} triggered by the button.
     */
    @FXML
    private void handleCloseButton(ActionEvent event) {
        Stage helpStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        helpStage.close();
        Logger.info("Help screen closed.");

        if (gameStage != null) {
            gameStage.show();
            Logger.info("Returned to main game screen (state preserved).");
        } else {
            GuiUtils.loadChessboardFallback(getClass());
        }
    }
}