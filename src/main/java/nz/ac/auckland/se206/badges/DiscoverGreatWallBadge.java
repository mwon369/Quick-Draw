package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class DiscoverGreatWallBadge extends Badge {

  public DiscoverGreatWallBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "The Great Wall";
    this.description = "Complete a game in classic mode with the word 'The Great Wall of China'";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWordsGiven().contains("The Great Wall of China");
  }
}
