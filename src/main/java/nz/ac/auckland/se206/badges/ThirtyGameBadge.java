package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public class ThirtyGameBadge extends Badge {

  public ThirtyGameBadge(boolean isCompleted) {
    super(isCompleted);
    this.title = "Intermediate";
    this.description = "Complete 30 games in either classic or hidden game mode";
    this.badgeIcon = "ThirtyGameBadge";
  }

  @Override
  public void checkCompletion(User user) {
    isCompleted = user.getLosses() + user.getWins() == 30;
  }
}
