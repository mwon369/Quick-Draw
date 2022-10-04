package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class FiveConsecutiveWinsBadge extends Badge {

  public FiveConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 3;
  }
}
