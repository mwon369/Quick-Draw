package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TwoConsecutiveWinsBadge extends Badge {

  public TwoConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  protected void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 2;
  }
}
