package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LoginController {
  @FXML private Hyperlink createUserLink;

  /**
   * This method switches from the login page to the menu page
   *
   * @param event an ActionEvent representing when the login page has been clicked
   */
  @FXML
  private void onLogin(ActionEvent event) {
    // retrieve the source of button and switch to the menu
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  /**
   * This method switches from the login page to the user creation page
   *
   * @param event an ActionEvent representing when the create user button has been clicked
   */
  @FXML
  private void onCreateUser(ActionEvent event) {
    // retrieve the source of hyperlink and switch to the create user scene
    Hyperlink hyperlink = (Hyperlink) event.getSource();
    Scene sceneHyperlinkIsIn = hyperlink.getScene();
    sceneHyperlinkIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_CREATION));
  }
}
