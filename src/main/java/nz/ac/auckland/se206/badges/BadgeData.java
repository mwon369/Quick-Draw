package nz.ac.auckland.se206.badges;

public class BadgeData {
  private int badgeId;
  private boolean isCompleted;

  /**
   * This constructor method constructs a badgeId object which will be used to help with saving and
   * loading each user's badges
   *
   * @param badgeId the unique id number of the badge
   * @param isCompleted a boolean determining whether it is completed
   */
  public BadgeData(int badgeId, boolean isCompleted) {
    this.isCompleted = isCompleted;
    this.badgeId = badgeId;
  }

  public int getBadgeId() {
    return badgeId;
  }

  public void setBadgeID(int badgeID) {
    this.badgeId = badgeID;
  }

  public boolean getCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
