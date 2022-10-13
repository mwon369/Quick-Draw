package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LeaderBoardController {
  @FXML private ListView<String> userListView;
  @FXML private ListView<Integer> timeListView;

  public void readyLeaderBoard() {
    UsersManager.mergeSort(0, UsersManager.getuserLength() - 1);
    List<String> userList = this.userOrderForDisplay();
    userListView.getItems().setAll(userList);
    timeListView.getItems().setAll(UsersManager.getUserTIme());
  }

  private List<String> userOrderForDisplay() {
    List<String> userOrder = new ArrayList<>();
    String[] usernameArray = UsersManager.getUserArray();
    for (int i = 0; i < usernameArray.length; i++) {
      StringBuilder sb = new StringBuilder();
      sb.append(String.format("%1$-5s %2$s ", i + 1 + ".) ", usernameArray[i]));
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
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }
}
