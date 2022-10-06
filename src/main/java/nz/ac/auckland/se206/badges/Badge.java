package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public abstract class Badge {
  protected boolean isCompleted;
  protected String title;
  protected String description;

  public abstract void checkCompletion(User user);

  public Badge(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  public boolean isCompleted() {
    return isCompleted;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }
}
