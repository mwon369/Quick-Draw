package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class TenGameBadge extends Badge {

  public TenGameBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Novice";
    this.description = "Complete 10 games in either classic or hidden game mode";
    this.badgeIcon = "TenGameBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getLosses() + user.getWins() == 10;
  }
}
