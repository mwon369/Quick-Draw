package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategoryManager;
import nz.ac.auckland.se206.words.CategorySelector.CategoryDifficulty;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class HiddenWordController extends CanvasController {

  @FXML private Button hintButton;

  /**
   * Starts a new game, gets and display a random category, disables and clears canvas. Will enable
   * the ready button
   */
  @FXML
  @Override
  protected void onStartNewGame() {
    SoundManager.playButtonClick();
    resetCanvas();
    // get user fields and target category
    userStreak = user.getWinStreak();
    userWins = user.getWins();
    userLosses = user.getLosses();
    userFastestWin = user.getFastestWin();
    targetCategory = user.giveWordToDraw(user.getWordDifficulty());

    // make sure label can go over two lines if needed
    wordLabel.setWrapText(true);
    wordLabel.setText("Word Definition: " + CategoryManager.getDefinition(targetCategory));

    // configure, disable and clear the canvas, disable the ready button
    readyButton.setDisable(false);
    hintButton.setDisable(false);

    // reset the timer label and cancel previous timer if needed
    timerLabel.setText(String.valueOf(timeLimit));
    if (timer != null) {
      timer.cancel();
    }
    timer = new Timer();

    // clear the predictions list
    predictionsListView.getItems().clear();
  }

  /** This method displays a pop-up window when the user clicks the 'Hint' button */
  @FXML
  private void onGetHint() {
    SoundManager.playButtonClick();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    // create a style class so it can be styled through canvas.css
    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getStylesheets().add(getClass().getResource("/css/canvas.css").toExternalForm());
    dialogPane.getStyleClass().add("hintDialog");

    // set visual attributes
    alert.setTitle("Word Hint");
    alert.setHeaderText(CategoryManager.getHint(targetCategory));

    for (Node node : alert.getDialogPane().getChildren()) {
      // set button hover sounds
      if (node instanceof ButtonBar) {
        ButtonBar buttonBar = (ButtonBar) node;
        for (Node button : buttonBar.getButtons()) {
          button.setOnMouseEntered(
              event -> {
                SoundManager.playButtonHover();
              });
        }
      }
    }

    // play button click when user closes hint alert
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK) {
      SoundManager.playButtonClick();
    }
  }

  /**
   * This method stops the game and disables the canvas. It displays "You win!" or "You lose" if
   * user wins or loses the game respectively
   *
   * @param isWin boolean representing whether the user won the game or not
   */
  @Override
  protected void stopGame(boolean isWin, String timeString) {
    // reveal the word to the user
    wordLabel.setText(
        isWin
            ? "You got it! The word was: " + targetCategory
            : "The word was: " + targetCategory + ". Better luck next time!");

    hintButton.setDisable(true);
    savePane.setDisable(false);
    canvas.setDisable(true);
    // disable mouse dragging on canvas
    canvas.setOnMouseDragged(e -> {});

    // set and display the win/loss
    winLossLabel.setText(isWin ? "You win!" : "You Lose!");
    winLossLabel.setVisible(true);

    // update stats after the game ends
    int time = Integer.parseInt(timeString);
    if (isWin) {
      user.setWins(++userWins);
      user.setWinStreak(++userStreak);
      if (timeLimit - time < userFastestWin) {
        user.setFastestWin(timeLimit - time);
      }
    } else {
      user.setLosses(++userLosses);
      user.setWinStreak(0);
    }
    user.updateBadges();
    // update and save both instance word lists fields after game ends

    // update the word list
    switch (user.getWordDifficulty()) {
      case EASY:
        user.updateWordList(CategoryDifficulty.E, targetCategory);
        break;
      case MEDIUM:
        for (CategoryDifficulty categoryDifficulty :
            Arrays.asList(CategoryDifficulty.E, CategoryDifficulty.M)) {
          if (user.getWordList().get(categoryDifficulty).contains(targetCategory)) {
            user.updateWordList(categoryDifficulty, targetCategory);
          }
        }
        break;
      case HARD:
        for (CategoryDifficulty categoryDifficulty : user.getWordList().keySet()) {
          if (user.getWordList().get(categoryDifficulty).contains(targetCategory)) {
            user.updateWordList(categoryDifficulty, targetCategory);
          }
        }
        break;
      case MASTER:
        user.updateWordList(CategoryDifficulty.H, targetCategory);
        break;
    }
    user.updatePreviousWords(targetCategory);

    try {
      UsersManager.saveUsersToJson();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    isGameOver = true;

    // speak whether user won or lost
    Timer speakOutcome = new Timer();
    speakOutcome.schedule(
        new TimerTask() {
          @Override
          public void run() {
            textToSpeech.speak(isWin ? "I got it! It is " + targetCategory : "You lose!");
            // close the thread
            speakOutcome.cancel();
          }
        },
        500);
  }
}
