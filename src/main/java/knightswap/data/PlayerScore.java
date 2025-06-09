package knightswap.data;

/**
 * Represents a player's record, storing their name and best score achieved in the Knight Swap game.
 * The best score is defined as the minimum number of moves required to solve the puzzle.
 */
public class PlayerScore {
    private final String playerName;
    private int bestScore;

    /**
     * Constructs a new {@code PlayerScore} object with the specified player name and best score.
     *
     * @param playerName The {@code String} name of the player.
     * @param bestScore The {@code int} best (minimum) number of moves achieved by the player for a solved puzzle.
     */
    public PlayerScore(String playerName, int bestScore) {
        this.playerName = playerName;
        this.bestScore = bestScore;
    }

    /**
     * Public getter for the player's name.
     *
     * @return the {@code String} name of the player.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Public getter for the player's best score.
     * The best score represents the minimum number of moves.
     *
     * @return the {@code int} best score (minimum moves) of the player.
     */
    public int getBestScore() {
        return bestScore;
    }

    /**
     * Sets the player's best score.
     * This method should typically be called when a player achieves a new, lower (better) score.
     *
     * @param bestScore The new {@code int} best score to set for the player.
     */
    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    /**
     * Returns a string representation of the {@code PlayerScore} object.
     * The format is "PlayerName: BestScore score".
     *
     * @return a {@code String} representation of this player's record.
     */
    @Override
    public String toString() {
        return playerName + ": " + bestScore + " score";
    }
}