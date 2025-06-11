package knightswap.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Objects;

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

    /**
     * Sets the application icon for a given Stage.
     * This method attempts to load an image from the specified resource path and
     * add it to the Stage's icon list. It logs appropriate messages if the icon
     * is loaded successfully or if an error occurs (e.g., file not found).
     *
     * @param stage The {@link javafx.stage.Stage} to which the icon should be applied.
     * This path should be relative to the classpath root.
     * @param callingClass The {@link java.lang.Class} from which this method is called,
     * used to correctly load the resource from the classpath.
     */
    public static void setStageIcon(Stage stage, Class<?> callingClass) {
        try {
            Image icon = new Image(Objects.requireNonNull(callingClass.getResourceAsStream("/logo.png"),
                    "Icon resource not found at resources folder."));
            stage.getIcons().add(icon);
            Logger.info("Application icon set successfully.");
        } catch (NullPointerException e) {
            Logger.error("ERROR: Application icon not found at resources folder. Please verify the icon file's path and your build configuration. Details: {}", e.getMessage());
        } catch (Exception e) {
            Logger.error("Failed to load application icon at resources folder: {}", e.getMessage());
        }
    }
}