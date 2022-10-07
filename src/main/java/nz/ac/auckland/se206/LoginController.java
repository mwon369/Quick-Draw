package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
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
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LoginController {
  @FXML private ImageView registerButton;
  @FXML private Label errorMessageLabel;
  @FXML private HBox profilesHBox;

  @FXML private ScrollPane profilesScrollPane;

  @FXML private Label smallTitle;
  @FXML private Label bigTitle;

  private RotateTransition rotation;

  private Map<String, User> users;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We load the user profiles to their
   * vboxes. Also starts the animation for the title
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

    // set animation for the title
    Timer timer = new Timer();
    bigTitle.setVisible(false);
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            Platform.runLater(
                () -> {
                  bigTitle.setVisible(!bigTitle.isVisible());
                  smallTitle.setVisible(!smallTitle.isVisible());
                });
          }
        },
        0,
        430);
  }

  /**
   * This method loads a user into a vbox on the login page
   *
   * @param user The user to be loaded
   */
  public void loadUserGUI(User user) {
    VBox vbox = new VBox();
    Label username = new Label(user.getUsername());
    username.getStyleClass().add("username");
    // create image
    ImageView image = new ImageView("/images/personIcon.png");
    image.setFitHeight(100);
    // add vbox details
    vbox.getChildren().add(image);
    vbox.getChildren().add(username);
    vbox.getStyleClass().add("profileCard");
    vbox.setOnMouseClicked((e) -> onLogin(e));
    vbox.setOnMouseEntered((e) -> onProfileHover(e));
    vbox.setOnMouseExited((e) -> onProfileExited(e));
    vbox.setMaxHeight(130);
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
      // retrieve username in vbox
      if (node instanceof Label) {
        Label username = (Label) node;
        // if user cannot log in for some reason, display error message
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
   * This method makes the profile vbox wobble left and right upon mouse hover
   *
   * @param event
   */
  @FXML
  private void onProfileHover(MouseEvent event) {
    VBox vbox = (VBox) event.getSource();
    // set the rotation details
    rotation = new RotateTransition(Duration.seconds(0.4), vbox);
    rotation.setCycleCount(Animation.INDEFINITE);
    rotation.setByAngle(15);
    rotation.setFromAngle(-7.5);
    rotation.setAutoReverse(true);
    rotation.play();
  }

  /**
   * This method stops the profile vbox wobble upon mouse exit
   *
   * @param event
   */
  @FXML
  private void onProfileExited(MouseEvent event) {
    VBox vbox = (VBox) event.getSource();
    vbox.setRotate(0);
    rotation.stop();
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
