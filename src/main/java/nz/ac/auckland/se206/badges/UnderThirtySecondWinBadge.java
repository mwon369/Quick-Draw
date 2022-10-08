package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class UnderThirtySecondWinBadge extends Badge {

  public UnderThirtySecondWinBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Speedster";
    this.description = "Win a game in either classic or hidden game mode in less than 30 seconds";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() < 30;
  }
}
