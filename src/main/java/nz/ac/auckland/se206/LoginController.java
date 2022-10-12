package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Optional;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
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
  @FXML private HBox profilesHbox;

  @FXML private ScrollPane profilesScrollPane;

  @FXML private ImageView musicIcon;

  @FXML private TextField search;

  private MenuController menuController;

  private RotateTransition rotation;
  private HashSet<String> currentDisplay;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We load the user profiles to their
   * vboxes. Adds listener to the search field to search for profiles
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
                // change display only if the new list of users is different from current
                // display
                if (!currentDisplay.containsAll(newDisplay)
                    || !newDisplay.containsAll(currentDisplay)) {
                  profilesHbox.getChildren().remove(0, profilesHbox.getChildren().size() - 1);
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
    loadAllUsersGui();
  }

  /**
   * This method loads a user into a vbox on the login page
   *
   * @param user The user to be loaded
   */
  public void loadUserGui(User user) {
    currentDisplay.add(user.getUsername());
    Label username = new Label(user.getUsername());
    username.getStyleClass().add("username");
    // create image for person icon
    File file = new File(user.getProfilePic());
    ImageView image = new ImageView(file.toURI().toString());
    image.setFitHeight(100);
    image.setFitWidth(100);

    // add vbox details
    VBox vbox = new VBox();
    vbox.getChildren().add(image);
    vbox.getChildren().add(username);
    vbox.getStyleClass().add("profileCard");
    vbox.setOnMouseClicked((e) -> onLogin(e));
    vbox.setMaxHeight(130);

    // create image for delete button
    ImageView delete = new ImageView("/images/clearCanvas.png");
    delete.setFitHeight(20);
    delete.setFitWidth(20);

    // set hbox which will contain the delete icon
    HBox imageHbox = new HBox(delete);
    imageHbox.setAlignment(Pos.CENTER);
    imageHbox.setVisible(false);
    imageHbox.getStyleClass().add("deleteIcon");
    imageHbox.setOnMouseEntered((e) -> onDeleteHover(e));
    imageHbox.setOnMouseClicked((e) -> onDelete(e));

    // set wrapper vbox for profile
    VBox bigVbox = new VBox();
    bigVbox.getChildren().add(imageHbox);
    bigVbox.getChildren().add(vbox);
    bigVbox.setAlignment(Pos.CENTER);
    bigVbox.setSpacing(5);
    bigVbox.getStyleClass().add("bigVbox");
    bigVbox.setOnMouseEntered((e) -> onProfileHover(e));
    bigVbox.setOnMouseExited((e) -> onProfileExited(e));
    profilesHbox.getChildren().add(0, bigVbox);
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

        menuController.showUserInfo();
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
    VBox bigVbox = (VBox) event.getSource();
    VBox vbox;
    for (Node node : bigVbox.getChildren()) {
      if (node instanceof HBox) {
        node.setVisible(true);
      } else if (node instanceof VBox) {
        vbox = (VBox) node;
        // set the rotation details
        rotation = new RotateTransition(Duration.seconds(0.4), vbox);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.setByAngle(15);
        rotation.setFromAngle(-7.5);
        rotation.setAutoReverse(true);
        rotation.play();
      }
    }
  }

  /**
   * This method stops the profile vbox wobble upon mouse exit
   *
   * @param event the MouseEvent
   */
  @FXML
  private void onProfileExited(MouseEvent event) {
    VBox bigVbox = (VBox) event.getSource();
    VBox vbox;
    for (Node node : bigVbox.getChildren()) {
      if (node instanceof HBox) {
        node.setVisible(false);
      } else if (node instanceof VBox) {
        // reset and stop rotation
        vbox = (VBox) node;
        vbox.setRotate(0);
        rotation.stop();
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
    SoundManager.playButtonClick();
    errorMessageLabel.setVisible(false);
    // retrieve the source of ImageView and switch to the create user scene
    VBox vbox = (VBox) event.getSource();
    Scene sceneVboxIsIn = vbox.getScene();
    sceneVboxIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_CREATION));
    search.clear();
  }

  /**
   * This method deletes a user from data
   *
   * @param event MouseEvent from source
   */
  private void onDelete(MouseEvent event) {
    SoundManager.playButtonClick();

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    // create a style class so it can be styled through canvas.css
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStylesheets().add(getClass().getResource("/css/canvas.css").toExternalForm());
    dialogPane.getStyleClass().add("hintDialog");

    // set visual attributes
    alert.setTitle("Delete profile?");
    alert.setHeaderText("Are you sure you want to delete this profile?");
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() != ButtonType.OK) {
      SoundManager.playButtonClick();
      return;
    }
    SoundManager.playButtonClick();

    // find user to delete
    HBox hbox = (HBox) event.getSource();
    String usernameToDelete;
    for (Node node : ((VBox) hbox.getParent()).getChildren()) {
      if (node instanceof VBox) {
        for (Node node2 : ((VBox) node).getChildren()) {
          if (node2 instanceof Label) {
            // get username and delete user and user gui
            usernameToDelete = ((Label) node2).getText();
            UsersManager.deleteUser(usernameToDelete);
            deleteUserGui(usernameToDelete);
            try {
              UsersManager.saveUsersToJson();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }

  /**
   * This method deletes a user GUI for a particular username
   *
   * @param username the user to delete
   */
  private void deleteUserGui(String username) {
    for (int i = 0; i < profilesHbox.getChildren().size(); i++) {
      if (profilesHbox.getChildren().get(i) instanceof VBox) {
        VBox vbox = (VBox) profilesHbox.getChildren().get(i);
        for (Node node : vbox.getChildren()) {
          if (node instanceof VBox) {
            VBox innerVbox = (VBox) node;
            for (Node node2 : innerVbox.getChildren()) {
              if (node2 instanceof Label) {
                if (((Label) node2).getText().equals(username)) {
                  profilesHbox.getChildren().remove(i);
                  String keyword = search.getText();
                  search.setText("");
                  search.setText(keyword);
                  return;
                }
              }
            }
          }
        }
      }
    }
  }

  /** This method loads all the user GUIS */
  private void loadAllUsersGui() {
    profilesHbox.getChildren().remove(0, profilesHbox.getChildren().size() - 1);
    for (String user : UsersManager.getUsersMap().keySet()) {
      loadUserGui(UsersManager.getUsersMap().get(user));
    }
  }

  private void onDeleteHover(MouseEvent event) {
    onButtonHover();
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

  public void setController(MenuController controller) {
    this.menuController = controller;
  }
}
