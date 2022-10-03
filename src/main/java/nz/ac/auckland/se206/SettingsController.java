package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import nz.ac.auckland.se206.DifficultyManager.Difficulty;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class SettingsController {
  @FXML private ToggleGroup accuracy;

  @FXML public RadioButton easyAccuracy;

  @FXML private RadioButton mediumAccuracy;

  @FXML private RadioButton hardAccuracy;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We add a listener to the accuracy
   * difficulty radio buttons
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    accuracy
        .selectedToggleProperty()
        .addListener(
            new ChangeListener<Toggle>() {
              public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {

                RadioButton rb = (RadioButton) accuracy.getSelectedToggle();

                if (rb != null) {
                  UsersManager.getSelectedUser()
                      .setAccuracyDifficulty(
                          Enum.valueOf(Difficulty.class, rb.getText().toUpperCase()));
                }
              }
            });
  }

  /** This method loads the user's selected difficulties */
  protected void loadUserDifficulties() {
    String accuracyId =
        String.valueOf(UsersManager.getSelectedUser().getAccuracyDifficulty())
            .toLowerCase()
            .concat("Accuracy");
    try {
      RadioButton rb = (RadioButton) this.getClass().getDeclaredField(accuracyId).get(this);
      rb.setSelected(true);
      rb.requestFocus();
    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method switches the root of the scene from the settings page to the menu page
   *
   * @param event an ActionEvent representing when the menu button has been clicked
   */
  @FXML
  private void onMenu(ActionEvent event) {
    // save user difficulties
    try {
      UsersManager.saveUsersToJson();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // retrieve the source of button and switch to the menu page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }
}
