package knightswap.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    private static List<PlayerScore> playerScores;

    /**
     * Constructs a new {@code ScoreBoardManager}.
     * Initializes the {@link Gson} instance and attempts to load existing scores
     * from the {@code scores.json} file.
     */
    public ScoreBoardManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        playerScores = new ArrayList<>();
        loadScores();
    }

    /**
     * Loads player scores from the {@code scores.json} file.
     * If the file does not exist or an error occurs during loading,
     * the internal list of scores is initialized as empty.
     */
    private void loadScores() {
        if (!Files.exists(SCORE_FILE_PATH)) {
            Logger.warn("Score file not found: {}", SCORE_FILE_PATH);
            playerScores = new ArrayList<>();
            return;
        }

        try (FileReader reader = new FileReader(SCORE_FILE_PATH.toFile())) {
            Type listType = new TypeToken<ArrayList<PlayerScore>>() {}.getType();
            playerScores = gson.fromJson(reader, listType);
            if (playerScores == null) {
                playerScores = new ArrayList<>();
            }
            Logger.info("Loaded {} player scores from {}", playerScores.size(), SCORE_FILE_PATH);
        } catch (IOException e) {
            Logger.error("Error loading scores from file {}: {}", SCORE_FILE_PATH, e.getMessage());
            playerScores = new ArrayList<>();
        }
    }

    /**
     * Saves the current list of {@link PlayerScore} objects to the {@code scores.json} file.
     * If an error occurs during saving, it is logged.
     */
    public void saveScores() {
        try {
            try (FileWriter writer = new FileWriter(SCORE_FILE_PATH.toFile())) {
                gson.toJson(playerScores, writer);
                Logger.info("Saved {} player scores to {}", playerScores.size(), SCORE_FILE_PATH);
            }
        } catch (IOException e) {
            Logger.error("Error saving scores to file {}: {}", SCORE_FILE_PATH, e.getMessage());
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
                score.setBestScore(moves);
                Logger.info("Updated best score for {} to {} moves.", playerName, moves);
            } else {
                Logger.info("Score for {} ({}) is not better than existing best ({}).", playerName, moves, score.getBestScore());
            }
        } else {
            playerScores.add(new PlayerScore(playerName, moves));
            Logger.info("Added new player {} with score {} moves.", playerName, moves);
        }
        saveScores();
    }

    /**
     * Retrieves a list of the top players, sorted by their {@code bestScore} in ascending order.
     * The list is limited to the specified number of entries.
     *
     * @param limit The {@code int} maximum number of top scores to return.
     * @return A {@link List} of {@link PlayerScore} objects representing the top scores.
     */
    public static List<PlayerScore> getTopScores(int limit) {
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