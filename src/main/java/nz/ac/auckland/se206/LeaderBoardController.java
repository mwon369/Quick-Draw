package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LeaderBoardController {
  @FXML private ListView<String> userListView;
  @FXML private ListView<Integer> statsListView;
  @FXML private Label statsLabel;
  @FXML private Label titleLabel;

  @FXML private Button changeLeaderBoardButton;

  private List<String> timeUserList = new ArrayList<>();

  private List<String> wordUserList = new ArrayList<>();

  private boolean isShowingFastestTime;

  /** This method displays the current leader board to the user */
  public void readyLeaderBoard() {
    // Sort the users in order
    isShowingFastestTime = true;
    UsersManager.resetArray();
    UsersManager.mergeSort(0, UsersManager.getuserLength(true) - 1, true);
    UsersManager.mergeSort(0, UsersManager.getuserLength(false) - 1, false);
    timeUserList = this.fastestTimeOrderForDisplay();
    wordUserList = this.mostWordsDrawnOrderForDisplay();
    // Display the results
    onShowFastestWin();
  }

  /**
   * This method formats the displaying of the user data in the ListView
   *
   * @return a list of string formatted for display
   */
  private List<String> fastestTimeOrderForDisplay() {
    List<String> userOrder = new ArrayList<>();
    String[] usernameArray = UsersManager.getUserArray(true);
    // For loop formatting every user to be displayed on the leaderboard
    for (int i = 0; i < usernameArray.length; i++) {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("%1$-5s %2$s ", i + 1 + ") ", usernameArray[i]));
      userOrder.add(sb.toString());
    }
    return userOrder;
  }

  /**
   * This method formats the displaying of the user data in the ListView
   *
   * @return a list of string formatted for display
   */
  private List<String> mostWordsDrawnOrderForDisplay() {
    List<String> userOrder = new ArrayList<>();
    String[] usernameArray = UsersManager.getUserArray(false);
    int index = 0;
    // For loop formatting every user to be displayed on the leaderboard
    for (int i = usernameArray.length - 1; i > -1; i--) {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("%1$-5s %2$s ", ++index + ") ", usernameArray[i]));
      userOrder.add(sb.toString());
    }
    return userOrder;
  }

  /**
   * This method is called when the user clicks on the menu button and will change the GUI to the
   * menu GUI
   *
   * @param event an ActionEvent representing the type of action that occurred
   */
  @FXML
  private void onMenu(ActionEvent event) {
    SoundManager.playButtonClick();
    // Get scene to switch to main menu
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
    userListView.getItems().setAll("");
    statsListView.getItems().clear();
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }

  /** This method displays the fastest win leader board */
  @FXML
  private void onShowFastestWin() {
    // Set all fields to show fastest win leader board
    userListView.getItems().setAll(timeUserList);
    statsListView.getItems().setAll(UsersManager.getUserTIme());
    statsLabel.setText("Time");
    titleLabel.setText("Fastest Time Leaderboard");
  }

  /** This method displays the rapid fire leader board */
  @FXML
  private void onShowMostWordsDrawn() {
    // Set all fields to show rapid fire leader board
    userListView.getItems().setAll(wordUserList);
    statsListView.getItems().setAll(UsersManager.getUserMostWordsDrawn());
    statsLabel.setText("Words Drawn");
    titleLabel.setText("Rapid Fire Leaderboard");
  }
}
