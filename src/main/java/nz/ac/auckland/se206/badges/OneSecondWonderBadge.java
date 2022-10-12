package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class OneSecondWonderBadge extends Badge {

  /**
   * This constructor method constructs the One Second Wonder Badge so that it can be saved and
   * loaded to user profiles
   *
   * @param isCompleted a boolean to determine whether this badge is completed
   */
  public OneSecondWonderBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "One Second Wonder";
    this.description = "Win a game in classic mode in 1 second";
  }

  /**
   * This method checks if the badge is completed for the specific user that is currently playing
   *
   * @param user the user which is being checked
   */
  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() <= 1;
  }
}
