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

    // Add scenes and stylesheets
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/menu.fxml"));
    SceneManager.addUi(AppUi.MENU, loader.load());
    SceneManager.getUiRoot(AppUi.MENU).getStylesheets().add("/css/menu.css");
    menuController = loader.getController();

    SceneManager.addUi(AppUi.USER_CREATION, loadFxml("userCreation"));
    SceneManager.getUiRoot(AppUi.USER_CREATION).getStylesheets().add("/css/userCreation.css");

    loader = new FXMLLoader(App.class.getResource("/fxml/login.fxml"));
    SceneManager.addUi(AppUi.LOGIN, loader.load());
    SceneManager.getUiRoot(AppUi.LOGIN).getStylesheets().add("/css/login.css");
    loginController = loader.getController();
    loginController.setController(menuController);

    SceneManager.addUi(AppUi.ZEN_MODE, loadFxml("zenCanvas"));
    SceneManager.getUiRoot(AppUi.ZEN_MODE).getStylesheets().add("/css/canvas.css");

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

  public static LoginController getLoginController() {
    return loginController;
  }

  public static MenuController getMenuController() {
    return menuController;
  }
}
