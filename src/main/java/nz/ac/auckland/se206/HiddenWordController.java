package nz.ac.auckland.se206;

import java.util.Optional;
import java.util.Timer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import nz.ac.auckland.se206.words.CategoryManager;

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
    // increment losses if the user skips the word during the round
    checkSkip();

    // reset the gui
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
   * @param timeString representing the time left in the game
   */
  @Override
  protected void stopGame(boolean isWin, String timeString) {
    // reveal the word to the user
    wordLabel.setText(
        isWin
            ? "You got it! The word was: " + targetCategory
            : "The word was: " + targetCategory + ". Better luck next time!");

    // call parent class implementation of method
    super.stopGame(isWin, timeString);
  }

  /**
   * This method is to be called when the user switches from the main menu to the hidden word canvas
   * so they can't click the hint button before getting a word
   */
  protected void disableHints() {
    hintButton.setDisable(true);
  }
}
