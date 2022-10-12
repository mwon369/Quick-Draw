package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class UnderTenSecondWinBadge extends Badge {

  public UnderTenSecondWinBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "The Flash";
    this.description = "Win a game in either classic or hidden game mode in less than 10 seconds";
    this.badgeIcon = "UnderTenSecondWinBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() < 10;
  }
}
