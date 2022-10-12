package nz.ac.auckland.se206;

import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import nz.ac.auckland.se206.DifficultyManager.Difficulty;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class SettingsController {
  @FXML private Label title;
  @FXML private ToggleGroup accuracy;
  @FXML private RadioButton easyAccuracy;
  @FXML private RadioButton mediumAccuracy;
  @FXML private RadioButton hardAccuracy;

  @FXML private ToggleGroup timeLimit;
  @FXML private RadioButton easyTimeLimit;
  @FXML private RadioButton mediumTimeLimit;
  @FXML private RadioButton hardTimeLimit;
  @FXML private RadioButton masterTimeLimit;

  @FXML private ToggleGroup confidence;
  @FXML private RadioButton easyConfidence;
  @FXML private RadioButton mediumConfidence;
  @FXML private RadioButton hardConfidence;
  @FXML private RadioButton masterConfidence;

  @FXML private ToggleGroup word;
  @FXML private RadioButton easyWord;
  @FXML private RadioButton mediumWord;
  @FXML private RadioButton hardWord;
  @FXML private RadioButton masterWord;

  /**
   * JavaFX calls this method once the GUI elements are loaded. We add a listener to the accuracy
   * difficulty radio buttons
   */
  public void initialize() {
    // add toggle listener to accuracy group
    accuracy
        .selectedToggleProperty()
        .addListener(
            new ChangeListener<Toggle>() {
              public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                SoundManager.playButtonClick();
                RadioButton rb = (RadioButton) accuracy.getSelectedToggle();
                if (rb != null) {
                  // set accuracy difficulty
                  UsersManager.getSelectedUser()
                      .setAccuracyDifficulty(
                          Enum.valueOf(Difficulty.class, rb.getText().toUpperCase()));
                }
              }
            });

    // add toggle listener to time limit group
    timeLimit
        .selectedToggleProperty()
        .addListener(
            new ChangeListener<Toggle>() {
              public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                SoundManager.playButtonClick();
                RadioButton rb = (RadioButton) timeLimit.getSelectedToggle();
                if (rb != null) {
                  // set time limit difficulty
                  UsersManager.getSelectedUser()
                      .setTimeLimitDifficulty(
                          Enum.valueOf(Difficulty.class, rb.getText().toUpperCase()));
                }
              }
            });

    // add toggle listener to confidence group
    confidence
        .selectedToggleProperty()
        .addListener(
            new ChangeListener<Toggle>() {
              public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                SoundManager.playButtonClick();
                RadioButton rb = (RadioButton) confidence.getSelectedToggle();
                if (rb != null) {
                  // set confidence difficulty
                  UsersManager.getSelectedUser()
                      .setConfidenceDifficulty(
                          Enum.valueOf(Difficulty.class, rb.getText().toUpperCase()));
                }
              }
            });

    // add toggle listener to words group
    word.selectedToggleProperty()
        .addListener(
            new ChangeListener<Toggle>() {
              public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                SoundManager.playButtonClick();
                RadioButton rb = (RadioButton) word.getSelectedToggle();
                if (rb != null) {
                  // set word difficulty
                  UsersManager.getSelectedUser()
                      .setWordDifficulty(
                          Enum.valueOf(Difficulty.class, rb.getText().toUpperCase()));
                }
              }
            });

    // define title animation
    ScaleTransition st = new ScaleTransition(Duration.millis(750), title);
    st.setFromX(1);
    st.setFromY(1);
    st.setToX(1.15);
    st.setToY(1.15);
    // cycle the animation indefinitely
    st.setCycleCount(Animation.INDEFINITE);
    st.setAutoReverse(true);
    st.play();
  }

  /** This method loads the user's selected difficulties and makes certain radio buttons selected */
  protected void loadUserDifficulties() {
    ArrayList<String> difficultyRbIds = new ArrayList<String>();

    // add the radio button ids to be selected for each setting
    difficultyRbIds.add(
        String.valueOf(UsersManager.getSelectedUser().getAccuracyDifficulty())
            .toLowerCase()
            .concat("Accuracy"));
    difficultyRbIds.add(
        String.valueOf(UsersManager.getSelectedUser().getTimeLimitDifficulty())
            .toLowerCase()
            .concat("TimeLimit"));
    difficultyRbIds.add(
        String.valueOf(UsersManager.getSelectedUser().getConfidenceDifficulty())
            .toLowerCase()
            .concat("Confidence"));
    difficultyRbIds.add(
        String.valueOf(UsersManager.getSelectedUser().getWordDifficulty())
            .toLowerCase()
            .concat("Word"));

    // select each radio button for each setting
    for (String rbId : difficultyRbIds) {
      try {
        // get corresponding difficulty radio button and select it
        RadioButton rb = (RadioButton) this.getClass().getDeclaredField(rbId).get(this);
        rb.setSelected(true);
        rb.requestFocus();
      } catch (NoSuchFieldException
          | SecurityException
          | IllegalArgumentException
          | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * This method switches the root of the scene from the settings page to the menu page
   *
   * @param event an ActionEvent representing when the menu button has been clicked
   */
  @FXML
  private void onSwitchToMainMenu(ActionEvent event) {
    // save user difficulties
    try {
      UsersManager.saveUsersToJson();
    } catch (IOException e) {
      e.printStackTrace();
    }
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the menu page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }
}
