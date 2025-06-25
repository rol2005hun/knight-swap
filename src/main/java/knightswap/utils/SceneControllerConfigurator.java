package knightswap.utils;

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