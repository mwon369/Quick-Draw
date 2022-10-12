package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;
import nz.ac.auckland.se206.words.CategorySelector.CategoryDifficulty;

public class DiscoverGreatWallBadge extends Badge {

  public DiscoverGreatWallBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "The Great Wall";
    this.description =
        "Complete a game in either classic or hidden game mode with the word 'The Great Wall of China'";
    this.badgeIcon = "DiscoverGreatWallBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWordsGiven(CategoryDifficulty.H).contains("The Great Wall of China");
  }
}
