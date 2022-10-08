package nz.ac.auckland.se206.badges;

public class BadgeFactory {
  /**
   * This method creates a child class of Badge depending on their id
   *
   * @param badgeID the id of the badge
   * @param isCompleted whether the badge has been earned
   * @return
   */
  public static Badge createBadge(int badgeID, boolean isCompleted) {
    /*
     * This switch case statement is used to create a sub-class of Badge depending
     * on their id
     */
    switch (badgeID) {
      case 1:
        return new UnderThirtySecondWinBadge(isCompleted);
      case 2:
        return new UnderTenSecondWinBadge(isCompleted);
      case 3:
        return new OneSecondWonderBadge(isCompleted);
      case 4:
        return new TwoConsecutiveWinsBadge(isCompleted);
      case 5:
        return new FiveConsecutiveWinsBadge(isCompleted);
      case 6:
        return new TenConsecutiveWinsBadge(isCompleted);
      case 7:
        return new DiscoverGreatWallBadge(isCompleted);
      case 8:
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
