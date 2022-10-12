package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class UserInfoController extends UserCreationController {

  @FXML private Label usernameLabel;

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
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
  }

  @FXML
  private void onChangeName() {
    SoundManager.playButtonClick();
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

    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Account successfully created!");
    errorMessageLabel.setVisible(true);

    usernameField.clear();
    onClear();
    usernameLabel.setText(UsersManager.getSelectedUser().getUsername());
  }

  @FXML
  private void onChangePicture() {
    File file = new File(UsersManager.getSelectedUser().getProfilePic());
    file.delete();
    try {
      this.saveCurrentSnapshotOnFile(UsersManager.getSelectedUser());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
