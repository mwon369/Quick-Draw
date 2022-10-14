package nz.ac.auckland.se206;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserInfoController extends UserCreationController {

  @FXML private Label usernameLabel;
  @FXML private ImageView profilePictureImageView;

  /** This method updates the user's username and changes their profile picture's name as well */
  @FXML
  private void onChangeName() {
    SoundManager.playButtonClick();
    // check if username or password is empty
    if (usernameField.getText().isBlank()) {
      errorMessageLabel.setTextFill(Color.RED);
      errorMessageLabel.setText("You have not entered a username");
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

    // Create a temporary user object to store current details
    User user = UsersManager.getSelectedUser();

    // Copy profile pic to new file name
    Path from = Paths.get(".profiles/profilePictures/" + user.getUsername() + ".png");
    Path to = Paths.get(".profiles/profilePictures/" + usernameField.getText() + ".png");
    try {
      Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // delete the user data and gui
    UsersManager.deleteUser(user.getUsername());
    App.getLoginController().deleteUserGui(user.getUsername());

    // update user data for profile pic directory and username
    user.setProfilePic(".profiles/profilePictures/" + usernameField.getText() + ".png");
    user.setUserName(usernameField.getText());

    // update users map
    UsersManager.createUser(user);
    UsersManager.setSelectedUser(user.getUsername());

    // reload login UIs
    App.getLoginController().loadUserGui(user);

    // Inform user of the result of the changes
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Username Successfully changed!");
    errorMessageLabel.setVisible(true);

    // Update display username
    usernameField.clear();
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
  }

  /** This method changes the current user's profile picture with a new one that they have drawn */
  @FXML
  private void onChangePicture() {
    SoundManager.playButtonClick();
    User user = UsersManager.getSelectedUser();
    try {
      // save new pic
      saveCurrentSnapshotOnFile(user);

      // delete and load the user GUI
      App.getLoginController().deleteUserGui(user.getUsername());
      App.getLoginController().loadUserGui(user);
      App.getMenuController().showUserInfo();

      // display successful messsage
      errorMessageLabel.setTextFill(Color.GREEN);
      errorMessageLabel.setText("Profile Image Successfully Saved!");
      errorMessageLabel.setVisible(true);
      loadUserInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is called when the user clicks on the menu button and will change the GUI to the
   * menu GUI
   *
   * @param event an ActionEvent representing the type of action that occurred
   */
  @FXML
  private void onSwitchToMenu(ActionEvent event) {
    SoundManager.playButtonClick();
    // Get scene to switch to main menu
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
    // Update user info in the event it has been changed
    try {
      App.getMenuController().showUserInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // reset all fields
    onClear();
    usernameField.clear();
    errorMessageLabel.setText("");
    errorMessageLabel.setVisible(false);
    onSelectPen();
    colorPicker.setValue(Color.BLACK);
  }

  /** This method loads the user's current user name and profile picture */
  public void loadUserInfo() {
    // Displaying the user's information in the GUI
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
    try {
      profilePictureImageView.setImage(App.getMenuController().loadImage());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
