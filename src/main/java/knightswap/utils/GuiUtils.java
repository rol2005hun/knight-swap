package knightswap.utils;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.Objects;

/**
 * Provides static utility methods for common JavaFX GUI operations
 * within the Knight Swap application. These include loading fallback screens,
 * setting stage icons, configuring table cell factories, and loading FXML scenes.
 */
public class GuiUtils {
    /**
     * Private constructor to prevent instantiation of this utility class,
     * as all its methods are static.
     */
    private GuiUtils() {}

    /**
     * Attempts to load and display the main game board ({@code chessboard.fxml})
     * as a fallback. This method is called if the reference to the original game stage
     * is lost, ensuring the application can still proceed.
     *
     * @param callingClass The {@link Class} from which this method is invoked,
     * used for correctly loading FXML resources from the classpath.
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
            setStageIcon(newGameStage, callingClass);
            newGameStage.show();
            Logger.info("A new game screen was opened (fallback).");
        } catch (IOException e) {
            Logger.error("Failed to load 'chessboard.fxml' during fallback: {}", e.getMessage(), e);
        } catch (Exception e) {
            Logger.error("An unexpected error occurred during fallback chessboard load: {}", e.getMessage(), e);
        }
    }

    /**
     * Sets the application icon for a given {@link Stage}.
     * It attempts to load {@code /logo.png} from the classpath resources.
     * Logs an error if the icon cannot be found or loaded.
     *
     * @param stage The {@link Stage} to which the icon will be applied.
     * @param callingClass The {@link Class} from which this method is called,
     * used for resource loading.
     */
    public static void setStageIcon(Stage stage, Class<?> callingClass) {
        try {
            Image icon = new Image(Objects.requireNonNull(callingClass.getResourceAsStream("/logo.png"),
                    "Icon resource not found at resources folder."));
            stage.getIcons().add(icon);
            Logger.info("Application icon set successfully.");
        } catch (NullPointerException e) {
            Logger.error("ERROR: Application icon not found at resources folder. Please verify the icon file's path and your build configuration. Details: {}", e.getMessage(), e);
        } catch (Exception e) {
            Logger.error("Failed to load application icon at resources folder: {}", e.getMessage(), e);
        }
    }

    /**
     * Configures a {@link TableColumn}'s cell factory to center the text content within each cell.
     * This method is generic and can be applied to any {@link TableColumn} type within a {@link TableView}.
     *
     * @param <S> The type of the items in the {@link TableView} that the column belongs to.
     * @param <T> The type of the value displayed in the {@link TableColumn}'s cells.
     * @param column The {@link TableColumn} to which the centered cell factory will be applied.
     */
    public static <S, T> void setCenteredCellFactory(TableColumn<S, T> column) {
        column.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    /**
     * A functional interface to allow configuring a controller after it's loaded.
     * This is useful when the FXML loading utility needs to pass data or
     * set specific properties on the controller instance.
     */
    @FunctionalInterface
    public interface SceneControllerConfigurator {
        /**
         * Configures the loaded FXML controller.
         *
         * @param controller The controller object.
         */
        void configure(Object controller);
    }

    /**
     * Loads an FXML file, sets it as the scene for a given stage, and performs
     * an optional action on the loaded controller.
     *
     * @param fxmlPath The path to the FXML file (e.g., "/screens/myscreen.fxml").
     * @param title The title for the stage.
     * @param stage The {@link Stage} to set the scene on.
     * @param controllerConsumer An optional {@link SceneControllerConfigurator} to perform actions on the controller.
     * @return The {@link FXMLLoader} instance that loaded the FXML, allowing access to the controller.
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static FXMLLoader loadAndSetScene(String fxmlPath, String title, Stage stage, SceneControllerConfigurator controllerConsumer) throws IOException {
        Logger.debug("Loading FXML: {}", fxmlPath);
        FXMLLoader loader = new FXMLLoader(GuiUtils.class.getResource(fxmlPath));
        Parent root = loader.load();

        if (controllerConsumer != null) {
            controllerConsumer.configure(loader.getController());
        }

        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
        Logger.debug("Scene set for stage with title: {}", title);

        return loader;
    }
}