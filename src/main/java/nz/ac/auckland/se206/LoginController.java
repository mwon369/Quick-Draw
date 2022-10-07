package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LoginController {
  @FXML private ImageView registerButton;
  @FXML private Label errorMessageLabel;
  @FXML private HBox profilesHBox;

  @FXML private ScrollPane profilesScrollPane;

  @FXML private Path path;
  @FXML private ImageView borderIcon;

  private Map<String, User> users;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We load the user profiles to their
   * vboxes. Also starts the animation for the border
   *
   * @throws IOException
   */
  public void initialize() {
    users = UsersManager.getUsersMap();
    if (users.keySet().size() == 0) {
      // when there are no users
      return;
    }
    for (User user : users.values()) {
      loadUserGUI(user);
    }

    // set animation for the border
    PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(5000));
    pathTransition.setPath(path);
    pathTransition.setNode(borderIcon);
    pathTransition.setCycleCount(Timeline.INDEFINITE);
    pathTransition.setInterpolator(Interpolator.LINEAR);
    pathTransition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
    pathTransition.play();
  }

  public void loadUserGUI(User user) {
    VBox vbox = new VBox();
    Label username = new Label(user.getUsername());
    username.getStyleClass().add("username");
    ImageView image = new ImageView("/images/personIcon.png");
    image.setFitHeight(100);
    vbox.getChildren().add(image);
    vbox.getChildren().add(username);
    vbox.getStyleClass().add("profileCard");
    vbox.setOnMouseClicked((e) -> onLogin(e));
    profilesHBox.getChildren().add(0, vbox);
  }

  /**
   * This method switches from the login page to the menu page
   *
   * @param event an ActionEvent representing when the login page has been clicked
   */
  @FXML
  private void onLogin(MouseEvent event) {
    VBox vbox = (VBox) event.getSource();
    for (Node node : vbox.getChildren()) {
      if (node instanceof Label) {
        Label username = (Label) node;
        if (!UsersManager.canLogIn(username.getText())) {
          errorMessageLabel.setTextFill(Color.RED);
          errorMessageLabel.setText("Error occured while signing you in!");
          errorMessageLabel.setVisible(true);
          return;
        }
        errorMessageLabel.setVisible(false);
        UsersManager.setSelectedUser(username.getText());
        Scene sceneButtonIsIn = vbox.getScene();
        sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
        return;
      }
    }
  }

  /**
   * This method switches from the login page to the user creation page
   *
   * @param event an ActionEvent representing when the create user button has been clicked
   */
  @FXML
  private void onRegister(MouseEvent event) {
    errorMessageLabel.setVisible(false);
    // retrieve the source of ImageView and switch to the create user scene
    ImageView button = (ImageView) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_CREATION));
  }
}
