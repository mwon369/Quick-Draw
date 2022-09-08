package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LoginController {
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorMessageLabel;
  @FXML private Hyperlink createUserLink;

  /**
   * This method switches from the login page to the menu page
   *
   * @param event an ActionEvent representing when the login page has been clicked
   */
  @FXML
  private void onLogin(ActionEvent event) {
    // Checks if the user details entered is valid for an existing user
    if (UsersManager.canLogIn(usernameField.getText(), passwordField.getText())) {
      // retrieve the source of button and switch to the menu
      UsersManager.setSelectedUser(usernameField.getText());
      Button button = (Button) event.getSource();
      Scene sceneButtonIsIn = button.getScene();
      sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
      // Reset all objects in GUI
      usernameField.clear();
      passwordField.clear();
      errorMessageLabel.setText("");
      return;
    }
    // Checks if the user has entered an details
    if (usernameField.getText().isBlank() || passwordField.getText().isBlank()) {
      errorMessageLabel.setText("You have not entered a username and/or a password");
      errorMessageLabel.setVisible(true);
      usernameField.clear();
      passwordField.clear();
      return;
    }
    // Prints error message when user enters wrong username or password
    errorMessageLabel.setText("You have entered the wrong username and/or password");
    errorMessageLabel.setVisible(true);
    usernameField.clear();
    passwordField.clear();
    return;
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
    usernameField.clear();
    passwordField.clear();
    errorMessageLabel.setText("");
  }
}
