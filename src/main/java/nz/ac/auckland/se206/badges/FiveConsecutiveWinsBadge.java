package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class FiveConsecutiveWinsBadge extends Badge {

  /**
   * This constructor method constructs the Five Consecutive Wins Badge so that it can be saved and
   * loaded to user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public FiveConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "On A Roll";
    this.description = "Achieve 5 consecutive wins in either classic or hidden game mode";
    this.badgeIcon = "FiveConsecutiveWinsBadge";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 5;
  }
}
