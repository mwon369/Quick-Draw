package nz.ac.auckland.se206.badges;

public class BadgeFactory {
  /**
   * This method creates a child class of Badge depending on their id
   *
   * @param badgeId the id of the badge
   * @param isCompleted whether the badge has been earned
   * @return a Badge object
   */
  public static Badge createBadge(int badgeId, boolean isCompleted) {
    /*
     * This switch case statement is used to create a sub-class of Badge depending on their id
     */
    switch (badgeId) {
      case 1:
        // if they win under 30 seconds
        return new UnderThirtySecondWinBadge(isCompleted);
      case 2:
        return new UnderTenSecondWinBadge(isCompleted);
      case 3:
        return new OneSecondWonderBadge(isCompleted);
      case 4:
        // if they get two consecutive wins
        return new TwoConsecutiveWinsBadge(isCompleted);
      case 5:
        return new FiveConsecutiveWinsBadge(isCompleted);
      case 6:
        return new TenConsecutiveWinsBadge(isCompleted);
      case 7:
        // if they encounter the great wall of china
        return new DiscoverGreatWallBadge(isCompleted);
      case 8:
        // if they play ten games
        return new TenGameBadge(isCompleted);
      case 9:
        return new ThirtyGameBadge(isCompleted);
      case 10:
        return new HundredGameBadge(isCompleted);
      default:
        return null;
    }
  }
}
