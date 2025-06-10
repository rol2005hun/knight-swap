package knightswap.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import knightswap.util.GuiUtils;
import org.tinylog.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class HelpController implements Initializable {
    @FXML
    private TextArea solutionStepsTextArea;

    private Stage gameStage;

    public void setGameStage(Stage gameStage) {
        this.gameStage = gameStage;
    }

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
        solutionStepsTextArea.setText(solution.trim());
    }

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
