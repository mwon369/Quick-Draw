package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class FiveConsecutiveWinsBadge extends Badge {

  public FiveConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "On A Roll";
    this.description = "Achieve 5 consecutive wins in either classic or hidden game mode";
    this.badgeIcon = "FiveConsecutiveWinsBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 5;
  }
}
