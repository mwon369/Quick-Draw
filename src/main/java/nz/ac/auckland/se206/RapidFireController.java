package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RapidFireController extends CanvasController {

  @FXML private Label gameTitleLabel;

  private int wordsDrawn = 0;
  private ArrayList<String> wordsPlayedDuringRound;

  /**
   * Starts a new game, gets and display a random category, disables and clears canvas. Will enable
   * the ready button
   */
  @FXML
  protected void onStartNewGame() {
    // set words drawn to 0 for each new game
    wordsPlayedDuringRound = new ArrayList<>();
    wordsDrawn = 0;
    gameTitleLabel.setText("Words Drawn: 0");
    super.onStartNewGame();
  }

  /**
   * Starts the timer starting from timerStartTime and terminates when timer reaches 0. Also
   * retrieves the predictions list every second
   */
  @Override
  protected void startTimer() {
    // set label
    gameTitleLabel.setText("Words Drawn: 0");
    // schedule task every 1000 milliseconds = 1 second
    timer.scheduleAtFixedRate(
        new TimerTask() {
          protected int time = timeLimit;

          @Override
          public void run() {
            // create temporary variable for the time
            int temp = time;
            Platform.runLater(
                () -> {
                  try {
                    // Only starts predicting once the player has started drawing
                    if (isDrawing) {
                      // retrieve predictions list and update the JavaFx ListView component
                      classifications = model.getPredictions(getCurrentSnapshot(), 340);
                      List<String> predictionsList = getPredictionsListForDisplay(classifications);
                      wordPosition = findWordPosition() + 1;
                      if (wordPosition != 0) {
                        updateIndicator();
                      }
                      predictionsListView.getItems().setAll(predictionsList);

                      // colour top predictions based on accuracy setting
                      colourTopPredictions(predictionsListView, accuracy);

                      // check to see if the player has drawn the word correctly
                      if (isWin(classifications, accuracy, confidence)) {
                        SoundManager.playWinSound();
                        // add category to words played for the current round
                        wordsPlayedDuringRound.add(targetCategory);

                        // update timer label here as well otherwise the timer label skips a second
                        timerLabel.setText(String.valueOf(temp));

                        // generate new word and reset the canvas
                        giveNewWord();
                        updateGui();
                        return;
                      }
                    }
                  } catch (TranslateException e) {
                    System.out.println("Unable to retrieve predictions");
                    e.printStackTrace();
                  }
                  // update timer label display to user
                  timerLabel.setText(String.valueOf(temp));
                });

            // if time has run out, cancel the timer
            if (time == 0) {

              SoundManager.playAlarmBell();
              timer.cancel();
              Platform.runLater(() -> endGame());
              return;

            } else {

              // play different sound effects depending
              // on how much time the user has left
              if (time <= 5) {
                SoundManager.playTimerTickFast();
              } else {
                SoundManager.playTimerTick();
              }

              // continuously decrement the time by 1 second
              time -= 1;
            }
          }
        },
        0,
        1000);
  }

  /** This method automatically generates a new word for the player to draw */
  private void giveNewWord() {
    // making sure the new word given hasn't been played in the same round
    do {
      targetCategory = user.giveWordToDraw(user.getWordDifficulty());
    } while (!wordsPlayedDuringRound.contains(targetCategory));
    wordLabel.setText("Your word is: " + targetCategory);
  }

  /** This method automatically updates the Gui everytime the user draws a word correctly */
  private void updateGui() {
    // increment words drawn and display to user
    wordsDrawn++;
    gameTitleLabel.setText("Words Drawn: " + wordsDrawn);
    // reset gui elements accordingly
    onClear();
    resetIndicator();
  }

  /**
   * Switches to the main menu. Clears the word label and canvas, disables the readyButton
   *
   * @param event an ActionEvent representing the type of action that occurred
   * @throws IOException if an IOException is thrown
   * @throws URISyntaxException if a URISyntaxException is thrown
   */
  @FXML
  protected void onSwitchToMainMenu(ActionEvent event) throws URISyntaxException, IOException {
    gameTitleLabel.setText("Rapid Fire Mode");
    super.onSwitchToMainMenu(event);
  }

  /**
   * This method ends the game for rapid fire mode and reads out how many words were drawn correctly
   */
  private void endGame() {
    // enable saving
    savePane.setDisable(false);

    // disable drawing
    canvas.setDisable(true);
    canvas.setOnMouseDragged(e -> {});

    // set and display how many words they drew
    winLossLabel.setText(
        wordsDrawn > 1 || wordsDrawn == 0
            ? "You drew " + wordsDrawn + " words"
            : "Your drew 1 word!");
    winLossLabel.setVisible(true);

    // indicate that the game is over
    isGameOver = true;
    // speak how many words the player drew
    Timer speakOutcome = new Timer();
    speakOutcome.schedule(
        new TimerTask() {
          @Override
          public void run() {
            textToSpeech.speak(
                wordsDrawn > 1 || wordsDrawn == 0
                    ? "You drew " + wordsDrawn + "words!"
                    : "You drew 1 word!");
            // close the text to speech thread
            speakOutcome.cancel();
          }
        },
        500);
  }
}
