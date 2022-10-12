package nz.ac.auckland.se206.badges;

public class BadgeData {
  private int badgeId;
  private boolean isCompleted;

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

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
