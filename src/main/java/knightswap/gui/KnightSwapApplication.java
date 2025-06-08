package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The main application class for the Knight Swap GUI game.
 * This class extends {@link javafx.application.Application} and is responsible for
 * initializing and displaying the game's user interface.
 */
public class KnightSwapApplication extends Application {
    /**
     * Public constructor to prevent instantiation of this utility class.
     * This class contains only static methods and should not be instantiated.
     */
    public KnightSwapApplication() {}

    /**
     * The entry point for the JavaFX application. This method is called after the
     * system is ready for the application to begin. It loads the FXML file for
     * the chessboard, sets up the primary stage, and displays the game window.
     *
     * @param stage The primary stage for this application, onto which
     * the application scene can be set. The stage is provided by the
     * JavaFX runtime.
     * @throws IOException If the {@code chessboard.fxml} file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(KnightSwapApplication.class.getResource("/chessboard.fxml")));
        stage.setTitle("Knight Swap");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}