package knightswap.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.tinylog.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Manages the persistence and retrieval of player scores.
 * This class handles loading scores from and saving scores to a JSON file,
 * as well as updating individual player records.
 */
public class ScoreBoardManager {
    private static final Path SCORE_FILE_PATH = Paths.get("scores.json");

    private final Gson gson;
    private List<PlayerScore> playerScores;

    /**
     * Constructs a new {@code ScoreBoardManager}.
     * Initializes the {@link Gson} instance and attempts to load existing scores
     * from the {@code scores.json} file.
     */
    public ScoreBoardManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        Logger.debug("ScoreBoardManager instance created. Attempting to load scores.");
        loadScores();
    }

    /**
     * Loads' player scores from the {@code scores.json} file.
     * If the file does not exist or an error occurs during loading,
     * the internal list of scores is initialized as empty.
     */
    private void loadScores() {
        if (!Files.exists(SCORE_FILE_PATH)) {
            Logger.info("Score file '{}' not found. Starting with an empty score list.", SCORE_FILE_PATH);
            playerScores = new ArrayList<>();
            return;
        }

        try (FileReader reader = new FileReader(SCORE_FILE_PATH.toFile())) {
            Type listType = new TypeToken<ArrayList<PlayerScore>>() {}.getType();
            playerScores = gson.fromJson(reader, listType);
            if (playerScores == null) {
                playerScores = new ArrayList<>();
                Logger.warn("Score file '{}' was empty or contained invalid JSON, initializing with an empty list.", SCORE_FILE_PATH);
            }
            Logger.info("Successfully loaded {} player scores from '{}'.", playerScores.size(), SCORE_FILE_PATH);
        } catch (IOException e) {
            Logger.error("Failed to load scores from file '{}': {}. Initializing with an empty score list.", SCORE_FILE_PATH, e.getMessage(), e);
            playerScores = new ArrayList<>();
        } catch (JsonSyntaxException e) {
            Logger.error("Failed to parse scores from file '{}' (invalid JSON): {}. Initializing with an empty score list.", SCORE_FILE_PATH, e.getMessage(), e);
            playerScores = new ArrayList<>();
        }
    }

    /**
     * Saves the current list of {@link PlayerScore} objects to the {@code scores.json} file.
     * If an error occurs during saving, it is logged.
     */
    public void saveScores() {
        try {
            Path parentDir = SCORE_FILE_PATH.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
                Logger.debug("Created directory for score file: {}", parentDir);
            }

            try (FileWriter writer = new FileWriter(SCORE_FILE_PATH.toFile())) {
                gson.toJson(playerScores, writer);
                Logger.info("Successfully saved {} player scores to '{}'.", playerScores.size(), SCORE_FILE_PATH);
            }
        } catch (IOException e) {
            Logger.error("Failed to save scores to file '{}': {}.", SCORE_FILE_PATH, e.getMessage(), e);
        }
    }

    /**
     * Adds a new player's score or updates an existing player's best score.
     * If a player with the given {@code playerName} already exists, their {@code bestScore}
     * is updated only if the new {@code moves} value is lower (better).
     * After updating or adding, the scores are saved to the file.
     *
     * @param playerName The {@link String} name of the player.
     * @param moves The {@code int} number of moves achieved in the game.
     */
    public void addOrUpdatePlayerScore(String playerName, int moves) {
        Optional<PlayerScore> existingScore = playerScores.stream()
                .filter(score -> score.getPlayerName().equals(playerName))
                .findFirst();

        if (existingScore.isPresent()) {
            PlayerScore score = existingScore.get();
            if (moves < score.getBestScore()) {
                Logger.debug("Updating best score for player '{}': from {} to {} moves.", playerName, score.getBestScore(), moves);
                score.setBestScore(moves);
                saveScores();
            } else {
                Logger.debug("Score for player '{}' ({} moves) is not better than existing best ({} moves). No update needed.", playerName, moves, score.getBestScore());
            }
        } else {
            playerScores.add(new PlayerScore(playerName, moves));
            Logger.info("Added new player '{}' with initial score {} moves.", playerName, moves);
            saveScores();
        }
    }

    /**
     * Retrieves a list of the top players, sorted by their {@code bestScore} in ascending order.
     * The list is limited to the specified number of entries.
     *
     * @param limit The {@code int} maximum number of top scores to return.
     * @return A {@link List} of {@link PlayerScore} objects representing the top scores.
     */
    public List<PlayerScore> getTopScores(int limit) {
        return playerScores.stream()
                .sorted(Comparator.comparingInt(PlayerScore::getBestScore))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a {@link PlayerScore} record for a specific player by their name.
     *
     * @param playerName The {@link String} name of the player to search for.
     * @return An {@link Optional} containing the {@link PlayerScore} if found,
     * otherwise an empty {@link Optional}.
     */
    public Optional<PlayerScore> getPlayerScore(String playerName) {
        return playerScores.stream()
                .filter(score -> score.getPlayerName().equals(playerName))
                .findFirst();
    }
}