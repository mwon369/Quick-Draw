package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class HundredGameBadge extends Badge {

  public HundredGameBadge(boolean isCompleted) {
    super(isCompleted);
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getLosses() + user.getWins() == 100;
  }
}
