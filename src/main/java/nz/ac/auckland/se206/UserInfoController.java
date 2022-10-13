package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
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

  /** This method creates an account for a new user */
  @Override
  public void initialize() {
    graphic = canvas.getGraphicsContext2D();
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });
    // initialise tool panes and add each tool pane
    toolPanes = Arrays.asList(penPane, eraserPane, colorPane);
    onSelectPen();
  }

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

    // Create a temporary user object to store changes
    User user = UsersManager.getSelectedUser();
    String oldName = UsersManager.getSelectedUser().getUsername();
    String profilePic =
        UsersManager.getSelectedUser().getProfilePic().replaceAll(oldName, usernameField.getText());

    // Change profile picture file name
    File file = new File(UsersManager.getSelectedUser().getProfilePic());
    file.renameTo(new File(user.getProfilePic()));

    // Delete old user data
    App.getLoginController().deleteUserGui(oldName);
    UsersManager.editUser(oldName);

    // Add new user data into userMap
    user.setProfilePic(profilePic);
    user.setUserName(usernameField.getText());
    UsersManager.updateMap(user);
    try {
      UsersManager.saveUsersToJson();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    App.getLoginController().loadAllUsersGui();

    // Inform user of the result of the changes
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Username Successfully changed!");
    errorMessageLabel.setVisible(true);

    // Update display
    usernameField.clear();
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
  }

  /** This method changes the current user's profile picture with a new one that they have drawn */
  @FXML
  private void onChangePicture() {
    User user = UsersManager.getSelectedUser();
    try {
      // save new pic
      this.saveCurrentSnapshotOnFile(user);

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
  private void onMenu(ActionEvent event) {
    SoundManager.playButtonClick();
    // Get scene to swithc to main menu
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
    onSelectPen();
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
