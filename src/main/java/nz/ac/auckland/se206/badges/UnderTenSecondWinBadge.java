package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class UnderTenSecondWinBadge extends Badge {

  public UnderTenSecondWinBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  protected void checkCompletion(User user) {
    isCompleted = user.getFastestWin() < 10;
  }
}
