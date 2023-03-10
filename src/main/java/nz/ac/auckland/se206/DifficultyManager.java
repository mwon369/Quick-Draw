package nz.ac.auckland.se206;

import static java.util.Map.entry;

import java.util.Map;

public class DifficultyManager {
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
    MASTER,
  }

  // easy difficulty is top 3, medium is top 2, hard is top 1
  private static Map<Difficulty, Integer> accuracyMap =
      Map.ofEntries(
          entry(Difficulty.EASY, 3), entry(Difficulty.MEDIUM, 2), entry(Difficulty.HARD, 1));

  // easy difficulty is 60 seconds, medium is 45 seconds, hard is 30 seconds, master is 15 seconds
  private static Map<Difficulty, Integer> timeLimitMap =
      Map.ofEntries(
          entry(Difficulty.EASY, 60),
          entry(Difficulty.MEDIUM, 45),
          entry(Difficulty.HARD, 30),
          entry(Difficulty.MASTER, 15));

  // easy difficulty is 1%, medium is 10%, hard is 25%, master is 50%
  private static Map<Difficulty, Double> confidenceMap =
      Map.ofEntries(
          entry(Difficulty.EASY, 0.01),
          entry(Difficulty.MEDIUM, 0.1),
          entry(Difficulty.HARD, 0.25),
          entry(Difficulty.MASTER, 0.5));

  /**
   * This method returns the top 'X' guesses which the user must aim to hit
   *
   * @param difficulty accuracy difficulty setting
   * @return the top 'X' guesses
   */
  public static int getAccuracy(Difficulty difficulty) {
    return accuracyMap.get(difficulty);
  }

  /**
   * This method returns the time limit in seconds
   *
   * @param difficulty time limit difficulty setting
   * @return the time limit in seconds
   */
  public static int getTimeLimit(Difficulty difficulty) {
    return timeLimitMap.get(difficulty);
  }

  /**
   * This method returns the confidence percentage
   *
   * @param difficulty confidence difficulty setting
   * @return the confidence percentage
   */
  public static double getConfidence(Difficulty difficulty) {
    return confidenceMap.get(difficulty);
  }
}
