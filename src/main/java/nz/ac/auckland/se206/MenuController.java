package nz.ac.auckland.se206;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {

  @FXML private Label title;

  @FXML private Button playButton;

  @FXML private ImageView musicIcon;

  @FXML private Label userNameLabel;

  @FXML private ImageView profilePictureImageView;

  /** This method sets the title animation to enlarge and get smaller periodically */
  public void initialize() {
    // define title animation
    ScaleTransition st = new ScaleTransition(Duration.millis(750), title);
    st.setFromX(1);
    st.setFromY(1);
    st.setToX(1.15);
    st.setToY(1.15);
    st.setCycleCount(Animation.INDEFINITE);
    st.setAutoReverse(true);
    st.play();
  }

  /**
   * This method switches the root of the scene from the main menu to the canvas
   *
   * @param event an ActionEvent representing when the play button has been clicked
   */
  @FXML
  private void onPlay(ActionEvent event) {
    // load the time limit for the set dificulty
    App.getCanvasController().setUpDifficulty();

    SoundManager.playButtonClick();
    SoundManager.playEnterGameSound();
    SoundManager.setBackgroundMusicVolume(0.05);

    // retrieve the source of button and switch to the canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
  }

  /**
   * This method switches the root of the scene from the main menu to the hidden word mode
   *
   * @param event an ActionEvent representing when the play button has been clicked
   */
  @FXML
  private void onPlayHiddenWord(ActionEvent event) {
    // retrieve the source of button and switch to the canvas
    App.getHiddenWordController().setUpDifficulty();
    SoundManager.playButtonClick();
    SoundManager.playEnterGameSound();
    SoundManager.setBackgroundMusicVolume(0.05);

    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.HIDDEN_WORD));
  }

  /**
   * This method switches the root of the scene from the main menu to the login page
   *
   * @param event an ActionEvent representing when the logout button has been clicked
   */
  @FXML
  private void onLogout(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  @FXML
  private void onUserInfo(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_INFO));
    App.getUserInfoController().loadUserInfo();
  }

  /**
   * This method switches the root of the scene from the main menu to the user stats page
   *
   * @param event an ActionEvent representing when the profile button has been clicked
   */
  @FXML
  private void onCheckStats(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the user stats page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_STATS));
    App.getUserStatsController().onRetrieveStats();
  }

  /**
   * This method switches the root of the scene from the main menu to the zen mode canvas
   *
   * @param event a button click
   */
  @FXML
  private void onPlayZenMode(ActionEvent event) {
    SoundManager.playButtonClick();
    SoundManager.playEnterGameSound();
    SoundManager.setBackgroundMusicVolume(0.05);
    // retrieve the source of button and switch to zen mode canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.ZEN_MODE));
  }

  /**
   * This method switches the root of the scene from the main menu to the settings page
   *
   * @param event an ActionEvent representing when the settings button has been clicked
   */
  @FXML
  private void onSettings(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the settings page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.SETTINGS));
    App.getSettingsController().loadUserDifficulties();
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
    App.getLoginController().setMusicIcon();
    setMusicIcon();
  }

  /**
   * This method swaps to the leader board gui
   *
   * @param event an ActionEvent representing when the settings button has been clicked
   */
  @FXML
  private void onLeaderBoard(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the settings page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LEADERBOARD));
    App.getLeaderBoardController().readyLeaderBoard();
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

  /**
   * This method displays the user's information when in the menu GUI
   *
   * @throws IOException if an IOException is thrown
   */
  public void showUserInfo() throws IOException {
    userNameLabel.setText(UsersManager.getSelectedUser().getUsername());
    profilePictureImageView.setImage(loadImage());
  }

  /**
   * This method loads the user profile picture
   *
   * @return The profile picture in an Image object
   * @throws IOException if an issue occurs with loading the image
   */
  public Image loadImage() throws IOException {
    // Loading the image into a file and converting it into an Image object
    File file = new File(UsersManager.getSelectedUser().getProfilePic());
    BufferedImage bufferImage = ImageIO.read(file);
    return SwingFXUtils.toFXImage(bufferImage, null);
  }
}
