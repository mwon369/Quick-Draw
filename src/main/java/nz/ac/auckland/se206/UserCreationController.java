package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserCreationController {
  @FXML private Label errorMessageLabel;

  @FXML private TextField usernameField;

  /** This method creates an account for a new user */
  @FXML
  private void onCreateAccount() {
    // check if username or password is empty
    if (usernameField.getText().isBlank()) {
      errorMessageLabel.setTextFill(Color.RED);
      errorMessageLabel.setText("You have not entered a username and/or a password");
      errorMessageLabel.setVisible(true);
      return;
    }

    // check if username is already taken
    if (UsersManager.getAllUsernames().contains(usernameField.getText())) {
      errorMessageLabel.setTextFill(Color.RED);
      errorMessageLabel.setText("This username is already in use");
      errorMessageLabel.setVisible(true);
      return;
    }

    // create user
    User newUser = new User(usernameField.getText());
    UsersManager.createUser(newUser);
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Account successfully created!");
    errorMessageLabel.setVisible(true);

    App.getLoginController().loadUserGUI(newUser);

    // clear all fields
    usernameField.clear();
  }

  /**
   * The method switches from the user creation page to the login page
   *
   * @param event an ActionEvent representing when the back to login button has been clicked
   */
  @FXML
  private void onLoginPage(ActionEvent event) {
    usernameField.clear();
    errorMessageLabel.setText("");
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.onButtonHover();
  }
}
