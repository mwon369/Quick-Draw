package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class DiscoverGreatWallBadge extends Badge {

  public DiscoverGreatWallBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  protected void checkCompletion(User user) {
    isCompleted = user.getWordsGiven().contains("The Great Wall of China");
  }
}
