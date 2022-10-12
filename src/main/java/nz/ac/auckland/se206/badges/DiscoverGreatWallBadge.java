package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;
import nz.ac.auckland.se206.words.CategorySelector.CategoryDifficulty;

public class DiscoverGreatWallBadge extends Badge {

  /**
   * This constructor method constructs the Discover Great Wall Badge so that it can be saved and
   * loaded to user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public DiscoverGreatWallBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "The Great Wall";
    this.description =
        "Complete a game in either classic or hidden game mode with the word 'The Great Wall of China'";
    this.badgeIcon = "DiscoverGreatWallBadge";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWordsGiven(CategoryDifficulty.H).contains("The Great Wall of China");
  }
}
