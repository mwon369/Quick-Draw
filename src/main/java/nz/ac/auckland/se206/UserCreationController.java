package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserCreationController {
  @FXML private Label errorMessageLabel;

  @FXML private TextField usernameField;

  @FXML private PasswordField passwordField;

  @FXML private PasswordField passwordCheckerField;

  /** This method creates an account for a new user */
  @FXML
  private void onCreateAccount() {
    // check if username or password is empty
    if (usernameField.getText().isBlank()
        || passwordField.getText().isBlank()
        || passwordCheckerField.getText().isBlank()) {
      errorMessageLabel.setText("You have not entered a username and/or a password");
      errorMessageLabel.setVisible(true);
      return;
    }

    // check if username is already taken
    if (UsersManager.getAllUsernames().contains(usernameField.getText())) {
      errorMessageLabel.setText("This username is already in use");
      errorMessageLabel.setVisible(true);
      return;
    }

    // check if passwords match
    if (!passwordField.getText().equals(passwordCheckerField.getText())) {
      errorMessageLabel.setText("Passwords do not match");
      errorMessageLabel.setVisible(true);
      return;
    }

    // create user
    User newUser = new User(usernameField.getText(), passwordField.getText());
    UsersManager.createUser(newUser);
    errorMessageLabel.setText("Account successfully created!");
    errorMessageLabel.setVisible(true);
  }

  /**
   * Thie method switches from the user creation page to the login page
   *
   * @param event an ActionEvent representing when the back to login button has been clicked
   */
  @FXML
  private void onLoginPage(ActionEvent event) {
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }
}
