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

  public void loadLeaderBoard() {
    UsersManager.mergeSort(0, UsersManager.getuserLength());
    List<String> userList = this.userOrderForDisplay(UsersManager.getUserArray());
    userListView.getItems().setAll(userList);
  }

  private List<String> userOrderForDisplay(User[] userList) {
    List<String> userOrder = new ArrayList<>();
    for (int i = 0; i < userList.length; i++) {
      StringBuilder sb = new StringBuilder();
      sb.append(i + 1 + ".) ")
          .append(userList[i].getUsername())
          .append("\t")
          .append(userList[i].getFastestWin());
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
  }
}
