package nz.ac.auckland.se206.badges;

public class BadgeData {
  private int badgeID;
  private boolean isCompleted;

  public BadgeData(int badgeID, boolean isCompleted) {
    this.isCompleted = isCompleted;
    this.badgeID = badgeID;
  }

  public int getBadgeID() {
    return badgeID;
  }

  public void setBadgeID(int badgeID) {
    this.badgeID = badgeID;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
