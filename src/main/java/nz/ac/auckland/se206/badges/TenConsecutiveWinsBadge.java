package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TenConsecutiveWinsBadge extends Badge {

  /**
   * This constructor method constructs the Ten Consecutive Wins Badge so that it can be saved and
   * loaded to user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public TenConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "I'm On Fire!!";
    this.description = "Achieve 10 consecutive wins in either classic or hidden game mode";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 10;
  }
}
