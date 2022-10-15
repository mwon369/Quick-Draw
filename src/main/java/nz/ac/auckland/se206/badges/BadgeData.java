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

  /**
   * This method returns the badge Id
   *
   * @return the badge Id
   */
  public int getBadgeId() {
    return badgeId;
  }

  /**
   * This method sets the badge id
   *
   * @param badgeId the badge id to set
   */
  public void setBadgeId(int badgeId) {
    this.badgeId = badgeId;
  }

  /**
   * This method gets whether the badge has been completed
   *
   * @return boolean representing whether the badge has been completed
   */
  public boolean getCompleted() {
    return isCompleted;
  }

  /**
   * This method sets the completion boolean for a badge
   *
   * @param isCompleted boolean representing whether the badge has been completed
   */
  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
