package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {

  @FXML private Button playButton;

  /**
   * This method switches the root of the scene from the main menu to the canvas
   *
   * @param event an ActionEvent representing when the play button has been clicked
   */
  @FXML
  private void onPlay(ActionEvent event) {
    // retrieve the source of button and switch to the canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  /**
   * This method switches the root of the scene from the main menu to the login page
   *
   * @param event an ActionEvent representing when the logout button has been clicked
   */
  @FXML
  private void onLogout(ActionEvent event) {
    // retrive the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }
}
