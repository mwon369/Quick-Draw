package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class LoginController {
  @FXML private VBox registerButton;
  @FXML private Label errorMessageLabel;
  @FXML private HBox profilesHBox;

  @FXML private ScrollPane profilesScrollPane;

  private RotateTransition rotation;

  @FXML private ImageView musicIcon;

  @FXML private TextField search;

  private HashSet<String> currentDisplay;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We load the user profiles to their
   * vboxes. Adds listener to the search field to search for profiles
   *
   * @throws IOException
   */
  public void initialize() {
    currentDisplay = new HashSet<String>();
    search
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                HashSet<String> newDisplay = new HashSet<String>();
                boolean found = false;
                for (String username : UsersManager.getUsersMap().keySet()) {
                  // if username contains the search text as substring, count as a match
                  if (username.contains(newValue)) {
                    found = true;
                    newDisplay.add(username);
                  }
                }
                // change display only if the new list of users is different from current display
                if (!currentDisplay.containsAll(newDisplay)
                    || !newDisplay.containsAll(currentDisplay)) {
                  profilesHBox.getChildren().remove(0, profilesHBox.getChildren().size() - 1);
                  for (String username : newDisplay) {
                    loadUserGui(UsersManager.getUsersMap().get(username));
                  }
                  // replace current display
                  currentDisplay.clear();
                  currentDisplay.addAll(newDisplay);
                }

                // configure error message if needed
                errorMessageLabel.setText(found || newValue.isEmpty() ? "" : "No users found!");
                errorMessageLabel.setTextFill(Color.RED);
                errorMessageLabel.setVisible(found || newValue.isEmpty() ? false : true);
              }
            });

    if (UsersManager.getUsersMap().keySet().size() == 0) {
      // when there are no users
      return;
    }
    for (User user : UsersManager.getUsersMap().values()) {
      currentDisplay.add(user.getUsername());
      loadUserGui(user);
    }
  }

  /**
   * This method loads a user into a vbox on the login page
   *
   * @param user The user to be loaded
   */
  public void loadUserGui(User user) {
    currentDisplay.add(user.getUsername());
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
    SoundManager.playButtonClick();
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
        search.clear();
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
    onButtonHover();
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
    SoundManager.playButtonClick();
    errorMessageLabel.setVisible(false);
    // retrieve the source of ImageView and switch to the create user scene
    VBox vbox = (VBox) event.getSource();
    Scene sceneVBoxIsIn = vbox.getScene();
    sceneVBoxIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_CREATION));
    search.clear();
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }

  /** This method toggles on and off the background music */
  @FXML
  private void onToggleMusic() {
    SoundManager.playButtonClick();
    SoundManager.toggleBackgroundMusic();
    // toggle music icon
    App.getMenuController().setMusicIcon();
    setMusicIcon();
  }

  /** This method sets the music icon according to whether background music is playing or not */
  public void setMusicIcon() {
    try {
      // if music off, display noMusic icon
      musicIcon.setImage(
          SoundManager.isBackgroundMusicOn()
              ? new Image(this.getClass().getResource("/images/music.png").toURI().toString())
              : new Image(this.getClass().getResource("/images/noMusic.png").toURI().toString()));
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }
}
