package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TwoConsecutiveWinsBadge extends Badge {

  /**
   * This constructor method constructs the Two Consecutive Wins Badge so that it can be saved and
   * loaded to user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public TwoConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Just Getting Started";
    this.description = "Achieve 2 consecutive wins in either classic or hidden game mode";
    this.badgeIcon = "TwoConsecutiveWinsBadge";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 2;
  }
}
