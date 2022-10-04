package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class UnderThirtySecondWinBadge extends Badge {

  public UnderThirtySecondWinBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  protected void checkCompletion(User user) {
    isCompleted = user.getFastestWin() < 30;
  }
}
