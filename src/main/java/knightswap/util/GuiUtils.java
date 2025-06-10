package knightswap.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class GuiUtils {

    /**
     * Attempts to load and display the main game screen (chessboard.fxml) as a fallback,
     * if a direct return to a parent Stage is not possible (e.g., the parent Stage is null).
     *
     @param callingClass The class from which this method was called (used for resource loading).
     */
    public static void loadChessboardFallback(Class<?> callingClass) {
        Logger.warn("Main game stage reference was null. Attempting to open a new game screen as fallback.");
        try {
            FXMLLoader loader = new FXMLLoader(callingClass.getResource("/chessboard.fxml"));
            if (loader.getLocation() == null) {
                Logger.error("ERROR: 'chessboard.fxml' resource not found at /chessboard.fxml during fallback. " +
                        "Check path and build configuration.");
                return;
            }
            Parent root = loader.load();
            Stage newGameStage = new Stage();
            newGameStage.setScene(new Scene(root));
            newGameStage.setTitle("Knight Swap Game");
            newGameStage.show();
            Logger.info("A new game screen was opened (fallback).");
        } catch (IOException e) {
            Logger.error("Failed to load 'chessboard.fxml' during fallback: {}", e.getMessage());
        }
    }
}