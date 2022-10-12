package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class OneSecondWonderBadge extends Badge {

  public OneSecondWonderBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "One Second Wonder";
    this.description = "Win a game in classic mode in 1 second";
    this.badgeIcon = "OneSecondWonderBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getFastestWin() <= 1;
  }
}
