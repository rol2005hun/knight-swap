package knightswap.utils;

import javafx.scene.control.TableView;
import knightswap.data.PlayerScore;

/**
 * Represents a player's score along with their calculated rank in the leaderboard.
 * This immutable record wraps a {@link PlayerScore} object and an integer rank.
 * It is primarily used for displaying ranked player data in a {@link TableView}
 * without modifying the original {@code PlayerScore} class.
 *
 * @param playerScore The {@link PlayerScore} object containing the player's name and best score.
 * @param rank The {@code int} calculated rank of the player in the leaderboard.
 */
public record RankedPlayerScore(PlayerScore playerScore, int rank) {
    @Override
    public String toString() {
        return "RankedPlayerScore{" +
                "playerScore=" + playerScore +
                ", rank=" + rank +
                '}';
    }
}