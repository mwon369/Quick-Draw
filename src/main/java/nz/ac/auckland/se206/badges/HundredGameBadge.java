package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class HundredGameBadge extends Badge {

  /**
   * This constructor method constructs the Hundred Game Badge so that it can be saved and loaded to
   * user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public HundredGameBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Master";
    this.description = "Complete 100 games in classic mode";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getLosses() + user.getWins() == 100;
  }
}
