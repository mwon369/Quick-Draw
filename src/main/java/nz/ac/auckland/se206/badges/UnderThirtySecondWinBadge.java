package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class UnderThirtySecondWinBadge extends Badge {

  public UnderThirtySecondWinBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Speedster";
    this.description = "Win a game in classic mode in 30 seconds or less";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() < 30;
  }
}
