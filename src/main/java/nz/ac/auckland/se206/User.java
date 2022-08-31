package nz.ac.auckland.se206;

import java.util.List;

public class User {

  private int userId;

  private String username;

  private String password;

  private List<String> wordsGiven;

  private int wins;

  private int losses;

  private int fastestWin;

  public User(String username, String password) {
    // assign the fields for each new user created
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }
}
