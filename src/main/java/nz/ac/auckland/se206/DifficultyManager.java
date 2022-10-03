package nz.ac.auckland.se206;

public class DifficultyManager {
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
    MASTER,
  }

  public static int getAccuracy(Difficulty difficulty) {
    switch (difficulty) {
      case EASY:
        return 3;
      case MEDIUM:
        return 2;
      case HARD:
        return 1;
      default:
        return 3;
    }
  }
}
