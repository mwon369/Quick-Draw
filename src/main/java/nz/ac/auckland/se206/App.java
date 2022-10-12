package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.words.CategoryManager;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  private static LoginController loginController;
  private static MenuController menuController;
  private static UserCreationController userCreationController;
  private static UserStatsController userStatsController;
  private static SettingsController settingsController;
  private static CanvasController canvasController;
  private static HiddenWordController hiddenWordController;
  private static WordHistoryController wordHistoryController;
  private static BadgeViewController badgeViewController;
  private static WordChooserController wordChooserController;
  private static ZenCanvasController zenCanvasController;

  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Menu" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If FXML files are not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Load users
    UsersManager.loadUsersFromJson();

    // Add user creation controller
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/userCreation.fxml"));
    SceneManager.addUi(AppUi.USER_CREATION, loader.load());
    SceneManager.getUiRoot(AppUi.USER_CREATION).getStylesheets().add("/css/userCreation.css");
    userCreationController = loader.getController();

    // add login controller
    loader = new FXMLLoader(App.class.getResource("/fxml/login.fxml"));
    SceneManager.addUi(AppUi.LOGIN, loader.load());
    SceneManager.getUiRoot(AppUi.LOGIN).getStylesheets().add("/css/login.css");
    loginController = loader.getController();

    // add menu controller
    loader = new FXMLLoader(App.class.getResource("/fxml/menu.fxml"));
    SceneManager.addUi(AppUi.MENU, loader.load());
    SceneManager.getUiRoot(AppUi.MENU).getStylesheets().add("/css/menu.css");
    menuController = loader.getController();

    // add settings controller
    loader = new FXMLLoader(App.class.getResource("/fxml/settings.fxml"));
    SceneManager.addUi(AppUi.SETTINGS, loader.load());
    SceneManager.getUiRoot(AppUi.SETTINGS).getStylesheets().add("/css/settings.css");
    settingsController = loader.getController();

    // add user stats controller
    loader = new FXMLLoader(App.class.getResource("/fxml/userStats.fxml"));
    SceneManager.addUi(AppUi.USER_STATS, loader.load());
    SceneManager.getUiRoot(AppUi.USER_STATS).getStylesheets().add("/css/userStats.css");
    userStatsController = loader.getController();

    // add canvas controller
    loader = new FXMLLoader(App.class.getResource("/fxml/canvas.fxml"));
    SceneManager.addUi(AppUi.CANVAS, loader.load());
    SceneManager.getUiRoot(AppUi.CANVAS).getStylesheets().add("/css/canvas.css");
    canvasController = loader.getController();

    // add hidden word controller
    loader = new FXMLLoader(App.class.getResource("/fxml/hiddenWordCanvas.fxml"));
    SceneManager.addUi(AppUi.HIDDEN_WORD, loader.load());
    SceneManager.getUiRoot(AppUi.HIDDEN_WORD).getStylesheets().add("/css/canvas.css");
    hiddenWordController = loader.getController();

    // add zen canvas controller
    loader = new FXMLLoader(App.class.getResource("/fxml/zenCanvas.fxml"));
    SceneManager.addUi(AppUi.ZEN_MODE, loader.load());
    SceneManager.getUiRoot(AppUi.ZEN_MODE).getStylesheets().add("/css/canvas.css");
    zenCanvasController = loader.getController();

    // load word chooser controller
    loader = new FXMLLoader(App.class.getResource("/fxml/wordChooser.fxml"));
    SceneManager.addUi(AppUi.WORD_CHOOSER, loader.load());
    SceneManager.getUiRoot(AppUi.WORD_CHOOSER).getStylesheets().add("/css/wordChooser.css");
    wordChooserController = loader.getController();

    // add word history controller
    loader = new FXMLLoader(App.class.getResource("/fxml/wordHistory.fxml"));
    SceneManager.addUi(AppUi.WORD_HISTORY, loader.load());
    SceneManager.getUiRoot(AppUi.WORD_HISTORY).getStylesheets().add("/css/wordChooser.css");
    wordHistoryController = loader.getController();

    // add badge view controller
    loader = new FXMLLoader(App.class.getResource("/fxml/badgeview.fxml"));
    SceneManager.addUi(AppUi.BADGE_VIEW, loader.load());
    SceneManager.getUiRoot(AppUi.BADGE_VIEW).getStylesheets().add("/css/badgeview.css");
    badgeViewController = loader.getController();

    // Load category info
    CategoryManager.loadCategoryInfoFromJson();

    final Scene scene = new Scene(SceneManager.getUiRoot(AppUi.LOGIN), 880, 720);
    stage.setScene(scene);
    stage.show();

    SoundManager.playBackgroundMusic();

    // ensure everything terminates when user closes the window
    stage.setOnCloseRequest(
        new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            Platform.exit();
            System.exit(0);
          }
        });
  }

  /**
   * Getter method for login controller for the game
   *
   * @return the LoginController
   */
  public static LoginController getLoginController() {
    return loginController;
  }

  /**
   * Getter method for menu controller for the game
   *
   * @return the MenuController
   */
  public static MenuController getMenuController() {
    return menuController;
  }

  /**
   * Getter method for user creation controller for the game
   *
   * @return the UserCreationController
   */
  public static UserCreationController getUserCreationController() {
    return userCreationController;
  }

  /**
   * Getter method for user stats controller for the game
   *
   * @return the UserStatsController
   */
  public static UserStatsController getUserStatsController() {
    return userStatsController;
  }

  /**
   * Getter method for canvas controller for the game
   *
   * @return the CanvasController
   */
  public static CanvasController getCanvasController() {
    return canvasController;
  }

  /**
   * Getter method for settings controller for the game
   *
   * @return the SettingsController
   */
  public static SettingsController getSettingsController() {
    return settingsController;
  }

  /**
   * Getter method for badge view controller for the game
   *
   * @return the BadgeViewController
   */
  public static BadgeViewController getBadgeViewController() {
    return badgeViewController;
  }

  /**
   * Getter method for hidden word controller for the game
   *
   * @return the HiddenWordController
   */
  public static HiddenWordController getHiddenWordController() {
    return hiddenWordController;
  }

  /**
   * Getter method for word history controller for the game
   *
   * @return the WordHistoryController
   */
  public static WordHistoryController getWordHistoryController() {
    return wordHistoryController;
  }

  /**
   * Getter method for word chooser controller for the game
   *
   * @return the WordChooserController
   */
  public static WordChooserController getWordChooserController() {
    return wordChooserController;
  }

  /**
   * Getter method for zen canvas controller for the game
   *
   * @return the ZenCanvasController
   */
  public static ZenCanvasController getZenCanvasController() {
    return zenCanvasController;
  }
}
