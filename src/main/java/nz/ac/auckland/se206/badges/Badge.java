package nz.ac.auckland.se206.badges;

import nz.ac.auckland.se206.User;

public abstract class Badge {
  protected boolean isCompleted;
  protected String title;
  protected String description;

  /**
   * This constructor method constructs a badge object, otherwise known as an instance
   *
   * @param isCompleted a boolean to check if a badge has been achieved
   */
  public Badge(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }

  /**
   * This method gets the isCompleted instance field for a specific badge
   *
   * @return a boolean indicating if a badge is completed
   */
  public boolean getCompleted() {
    return isCompleted;
  }

  /**
   * This method gets the title instance field for a specific badge
   *
   * @return the title of the badge
   */
  public String getTitle() {
    return title;
  }

  /**
   * This method gets the description instance field for a specific badge
   *
   * @return the description of the badge
   */
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
