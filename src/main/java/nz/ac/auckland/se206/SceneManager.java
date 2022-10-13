package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {
  // enum representing the different root nodes
  public enum AppUi {
    MENU,
    CANVAS,
    HIDDEN_WORD,
    LOGIN,
    USER_CREATION,
    USER_STATS,
    ZEN_MODE,
    WORD_CHOOSER,
    SETTINGS,
    BADGE_VIEW,
    WORD_HISTORY,
    USER_INFO,
    LEADERBOARD,
    RAPID_FIRE,
  }

  // hashmap that maps enum AppUi to the corresponding root note
  private static HashMap<AppUi, Parent> sceneMap = new HashMap<AppUi, Parent>();

  /**
   * This method adds a root node to the hashmap
   *
   * @param appUi word representation of the root node
   * @param uiRoot JavaFx Parent object representing the root node
   */
  public static void addUi(AppUi appUi, Parent uiRoot) {
    sceneMap.put(appUi, uiRoot);
  }

  /**
   * This method gets the JavaFx Parent object corresponding to the input AppUi
   *
   * @param appUi the AppUi representation of the Parent object
   * @return the JavaFx Parent object
   */
  public static Parent getUiRoot(AppUi appUi) {
    return sceneMap.get(appUi);
  }
}
