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
 * Manages loading and saving player scores from/to a JSON file.
 * Provides methods to update scores and retrieve sorted lists of records.
 */
public class ScoreBoardManager {
    private static final Path SCORE_FILE_PATH = Paths.get("scores.json");

    private final Gson gson;
    private static List<PlayerScore> playerScores;

    /**
     * Constructs a new ScoreBoardManager.
     * Initializes Gson and attempts to load existing scores from the JSON file.
     */
    public ScoreBoardManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        playerScores = new ArrayList<>();
        loadScores();
    }

    /**
     * Loads player scores from the JSON file.
     * If the file does not exist or is empty, an empty list is initialized.
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
     * Saves the current list of player scores to the JSON file.
     */
    public void saveScores() {
        try {
            //Files.createDirectories(SCORE_FILE_PATH.getParent());
            try (FileWriter writer = new FileWriter(SCORE_FILE_PATH.toFile())) {
                gson.toJson(playerScores, writer);
                Logger.info("Saved {} player scores to {}", playerScores.size(), SCORE_FILE_PATH);
            }
        } catch (IOException e) {
            Logger.error("Error saving scores to file {}: {}", SCORE_FILE_PATH, e.getMessage());
        }
    }

    /**
     * Adds a new player's score or updates an existing player's best score
     * if the new score is lower (better).
     *
     * @param playerName The name of the player.
     * @param moves The number of moves in the completed game.
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
     * Retrieves the top N player scores, sorted by best moves (ascending).
     *
     * @param limit The maximum number of top scores to retrieve.
     * @return A sorted list of {@link PlayerScore} objects.
     */
    public static List<PlayerScore> getTopScores(int limit) {
        return playerScores.stream()
                .sorted(Comparator.comparingInt(PlayerScore::getBestScore))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Gets a player's score record by name.
     *
     * @param playerName The name of the player.
     * @return An Optional containing the PlayerScore if found, empty otherwise.
     */
    public Optional<PlayerScore> getPlayerScore(String playerName) {
        return playerScores.stream()
                .filter(score -> score.getPlayerName().equals(playerName))
                .findFirst();
    }
}