package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class MenuController {

  private Parent userStatsScene;
  private UserStatsController userStatsController;

  private Parent settingsScene;
  private SettingsController settingsController;

  private Parent canvasScene;
  private CanvasController canvasController;

  private Parent hiddenWordCanvasScene;
  private HiddenWordController hiddenWordController;

  @FXML private Button playButton;

  /**
   * This method loads the userStats FXML and settings FXML when the menu FXML is loaded. The reason
   * we load it here instead of in App.java is because this class needs a reference to the
   * corresponding controller for the userStats FXML and settings FXML
   *
   * <p>That way when userStatsController.onRetrieveStats() is called the FXML elements in the
   * loaded userStats scene are actually updated Additionally, when
   * settingsController.loadUserDifficulties() is called, the FXML elements in the loaded settings
   * scene are updated
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/userStats.fxml"));
    userStatsScene = loader.load();
    userStatsController = loader.getController();
    SceneManager.addUi(AppUi.USER_STATS, userStatsScene);
    SceneManager.getUiRoot(AppUi.USER_STATS).getStylesheets().add("/css/userStats.css");

    loader = new FXMLLoader(App.class.getResource("/fxml/settings.fxml"));
    settingsScene = loader.load();
    settingsController = loader.getController();
    SceneManager.addUi(AppUi.SETTINGS, settingsScene);
    SceneManager.getUiRoot(AppUi.SETTINGS).getStylesheets().add("/css/settings.css");

    loader = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml"));
    canvasScene = loader.load();
    canvasController = loader.getController();
    SceneManager.addUi(AppUi.CANVAS, canvasScene);
    SceneManager.getUiRoot(AppUi.CANVAS).getStylesheets().add("/css/canvas.css");

    loader = new FXMLLoader(App.class.getResource("/fxml/hiddenWordCanvas.fxml"));
    hiddenWordCanvasScene = loader.load();
    hiddenWordController = loader.getController();
    SceneManager.addUi(AppUi.HIDDEN_WORD, hiddenWordCanvasScene);
    SceneManager.getUiRoot(AppUi.HIDDEN_WORD).getStylesheets().add("/css/canvas.css");
  }

  /**
   * This method switches the root of the scene from the main menu to the canvas
   *
   * @param event an ActionEvent representing when the play button has been clicked
   */
  @FXML
  private void onPlay(ActionEvent event) {
    // load the time limit for the set dificulty
    canvasController.setUpDifficulty();

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
    hiddenWordController.setUpDifficulty();

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
    // retrieve the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  /**
   * This method switches the root of the scene from the main menu to the user stats page
   *
   * @param event an ActionEvent representing when the profile button has been clicked
   */
  @FXML
  private void onCheckStats(ActionEvent event) {
    // retrieve the source of button and switch to the user stats page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.USER_STATS));
    userStatsController.onRetrieveStats();
  }

  @FXML
  private void onPlayZenMode(ActionEvent event) {
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
    // retrieve the source of button and switch to the settings page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.SETTINGS));
    settingsController.loadUserDifficulties();
  }
}
