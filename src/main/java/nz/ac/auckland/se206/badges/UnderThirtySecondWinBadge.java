package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class UnderThirtySecondWinBadge extends Badge {

  /**
   * This constructor method constructs the Under Thirty Second Win Badge so that it can be saved
   * and loaded to user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public UnderThirtySecondWinBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Speedster";
    this.description = "Win a game in either classic or hidden game mode in less than 30 seconds";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() < 30;
  }
}
