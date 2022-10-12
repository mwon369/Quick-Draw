package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public abstract class Badge {
  protected boolean isCompleted;
  protected String title;
  protected String description;
  protected String badgeIcon;

  public Badge(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public String getBadgeIcon() {
    return this.badgeIcon;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  /**
   * This method checks to see if the user has completed the requirements to earnt the badge
   *
   * @param user the user which is being checked
   */
  public abstract void checkCompletion(User user);
}
