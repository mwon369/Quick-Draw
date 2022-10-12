package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class ThirtyGameBadge extends Badge {

  /**
   * This constructor method constructs the Thirty Game Badge so that it can be saved and loaded to
   * user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public ThirtyGameBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Intermediate";
    this.description = "Complete 30 games in either classic or hidden game mode";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getLosses() + user.getWins() == 30;
  }
}
