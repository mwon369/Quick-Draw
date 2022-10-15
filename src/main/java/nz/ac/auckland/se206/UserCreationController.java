package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserCreationController extends ZenCanvasController {

  @FXML protected Label errorMessageLabel;

  @FXML protected TextField usernameField;

  /** This method creates an account for a new user */
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

  /**
   * This method verifies the user's sign up details and saves the users account details to the JSON
   * file
   */
  @FXML
  private void onCreateAccount() {
    SoundManager.playButtonClick();
    // check if username or password is empty
    if (usernameField.getText().isBlank()) {
      errorMessageLabel.setTextFill(Color.RED);
      errorMessageLabel.setText("You have not entered a username!");
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
    try {
      this.saveCurrentSnapshotOnFile(newUser);
    } catch (IOException e) {
      e.printStackTrace();
    }
    UsersManager.createUser(newUser);
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Account successfully created!");
    errorMessageLabel.setVisible(true);

    App.getLoginController().loadUserGui(newUser);

    // clear all fields
    usernameField.clear();
    onClear();
  }

  /**
   * The method switches from the user creation page to the login page
   *
   * @param event an ActionEvent representing when the back to login button has been clicked
   */
  @FXML
  private void onLoginPage(ActionEvent event) {
    SoundManager.playButtonClick();
    // clear the canvas if they exit out of the sign up screen
    onClear();
    // clear username and error message
    usernameField.clear();
    errorMessageLabel.setText("");
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  protected void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**
   * This method saves the users canvas drawing as an image and sets it as their profile picture
   *
   * @param user the user save the current snapshot for
   * @return the file to save
   * @throws IOException throws an IO exception of an error occurs with saving image to file
   */
  protected File saveCurrentSnapshotOnFile(User user) throws IOException {
    // You can change the location as you see fit.
    final File tmpFolder = new File(".profiles/profilePictures");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }
    // We save the image to a file in the tmp folder.
    user.setProfilePic(".profiles/profilePictures/" + user.getUsername() + ".png");
    final File imageToClassify = new File(user.getProfilePic());

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "png", imageToClassify);

    return imageToClassify;
  }
}
