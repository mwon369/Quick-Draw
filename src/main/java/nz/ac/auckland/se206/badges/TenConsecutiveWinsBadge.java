package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TenConsecutiveWinsBadge extends Badge {

  public TenConsecutiveWinsBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "I'm On Fire!!";
    this.description = "Achieve 10 consecutive wins in classic mode";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getWinStreak() == 10;
  }
}
