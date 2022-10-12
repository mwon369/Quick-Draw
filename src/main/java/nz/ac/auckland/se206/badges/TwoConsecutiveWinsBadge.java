package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TwoConsecutiveWinsBadge extends Badge {

  public TwoConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Just Getting Started";
    this.description = "Achieve 2 consecutive wins in either classic or hidden game mode";
    this.badgeIcon = "TwoConsecutiveWinsBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 2;
  }
}
