package knightswap.data;

import org.tinylog.Logger;

/**
 * Represents a player's record in the Knight Swap game, holding their name and the best score.
 * The {@code bestScore} signifies the fewest moves taken to solve the puzzle.
 */
public class PlayerScore {
    private final String playerName;
    private int bestScore;

    /**
     * Constructs a new {@code PlayerScore} instance.
     *
     * @param playerName The {@link String} name of the player.
     * @param bestScore The {@code int} minimum number of moves achieved by the player.
     */
    public PlayerScore(String playerName, int bestScore) {
        this.playerName = playerName;
        this.bestScore = bestScore;
        Logger.debug("PlayerScore created for player '{}' with best score {}.", playerName, bestScore);
    }

    /**
     * Retrieves the player's name.
     *
     * @return The {@link String} name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retrieves the player's best score.
     *
     * @return The {@code int} representing the player's minimum moves.
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Updates the player's best score.
     *
     * @param newScore The new {@code int} best score to be set.
     */
    public void setBestScore(int newScore) {
        this.bestScore = newScore;
        Logger.debug("Best score for player '{}' updated from {} to {}.", playerName, this.bestScore, newScore);
    }

    /**
     * Returns a formatted string of the player's record.
     * The format is "{@code PlayerName}: {@code BestScore} score".
     *
     * @return A {@link String} representation of the player's score.
     */
    @Override
    public String toString() {
        return playerName + ": " + bestScore + " score";
    }
}