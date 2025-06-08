package knightswap.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class KnightSwapApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(KnightSwapApplication.class.getResource("/chessboard.fxml")));
        stage.setTitle("Knight Swap");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
