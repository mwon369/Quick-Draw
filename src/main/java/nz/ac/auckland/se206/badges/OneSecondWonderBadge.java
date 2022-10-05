package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class OneSecondWonderBadge extends Badge {

  public OneSecondWonderBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() <= 1;
  }
}
