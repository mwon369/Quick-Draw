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
    String oldName = UsersManager.getSelectedUser().getUsername();
    String profilePic =
        UsersManager.getSelectedUser().getProfilePic().replaceAll(oldName, usernameField.getText());
    File file = new File(UsersManager.getSelectedUser().getProfilePic());
    UsersManager.getSelectedUser().setProfilePic(profilePic);
    file.renameTo(new File(UsersManager.getSelectedUser().getProfilePic()));
    UsersManager.getSelectedUser().setUserName(usernameField.getText());
    UsersManager.updateMap();
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Username Successfully changed!");
    errorMessageLabel.setVisible(true);

    usernameField.clear();
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
    try {
      UsersManager.saveUsersToJson();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onChangePicture() {
    SoundManager.playButtonClick();
    File file = new File(UsersManager.getSelectedUser().getProfilePic());
    file.delete();
    try {
      this.saveCurrentSnapshotOnFile(UsersManager.getSelectedUser());
    } catch (IOException e) {
      e.printStackTrace();
    }
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Profile Picture Successfully changed");
    errorMessageLabel.setVisible(true);
    try {
      profilePictureImageView.setImage(App.getMenuController().loadImage());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    App.getLoginController().loadAllUsersGui();
  }

  @FXML
  private void onMenu(ActionEvent event) {
    SoundManager.playButtonClick();
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
    try {
      App.getMenuController().showUserInfo();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadUserInfo() {
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
    try {
      profilePictureImageView.setImage(App.getMenuController().loadImage());
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }
}
