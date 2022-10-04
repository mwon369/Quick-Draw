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

    FXMLLoader loader2 = new FXMLLoader(App.class.getResource("/fxml/settings.fxml"));
    settingsScene = loader2.load();
    settingsController = loader2.getController();
    SceneManager.addUi(AppUi.SETTINGS, settingsScene);
    SceneManager.getUiRoot(AppUi.SETTINGS).getStylesheets().add("/css/settings.css");
  }

  /**
   * This method switches the root of the scene from the main menu to the canvas
   *
   * @param event an ActionEvent representing when the play button has been clicked
   */
  @FXML
  private void onPlay(ActionEvent event) {
    // retrieve the source of button and switch to the canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.CANVAS));
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
