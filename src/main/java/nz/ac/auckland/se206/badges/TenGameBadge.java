package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TenGameBadge extends Badge {

  /**
   * This constructor method constructs the Ten Game Badge so that it can be saved and loaded to
   * user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public TenGameBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Novice";
    this.description = "Complete 10 games in either classic or hidden game mode";
    this.badgeIcon = "TenGameBadge";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getLosses() + user.getWins() == 10;
  }
}
