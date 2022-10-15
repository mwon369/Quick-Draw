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
                  // Only starts predicting once the player has started drawing
                  if (isDrawing) {
                    // show predictions
                    try {
                      setPredictionsListView();
                    } catch (TranslateException e) {
                      System.out.print("Noooo it doesn't work!");
                      throw new RuntimeException(e);
                    }

                    // indicate the word position
                    wordPosition = findWordPosition() + 1;
                    if (wordPosition != 0) {
                      updateIndicator();
                    }

                    // check to see if the player has drawn the word correctly
                    if (isWin(classifications, accuracy, confidence)) {
                      // update timer label here as well otherwise the timer label skips a second
                      timerLabel.setText(String.valueOf(temp));

                      // add category to words played for the current round
                      wordsPlayedDuringRound.add(targetCategory);
                      SoundManager.playWinSound();

                      // generate new word and reset the canvas
                      giveNewWord();
                      updateGui();
                      return;
                    }
                  }
                  // update timer label display to user
                  timerLabel.setText(String.valueOf(temp));
                });

            // if time has run out, cancel the timer
            if (time == 0) {
              timer.cancel();
              RapidFireController.super.playSoundAndAnimation();
              Platform.runLater(() -> endGame());
              // otherwise continue game as usual
            } else {
              // play different sound effects depending on how much time the user has left
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
    } while (wordsPlayedDuringRound.contains(targetCategory));
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
   * @throws IOException if an issue occurs with loading the user profile image on the menu
   * @throws URISyntaxException if an issue occurs with loading the user profile image on the menu
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
            : "You drew 1 word!");
    winLossLabel.setVisible(true);
    winLossLabel.setWrapText(true);

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

    // update rapid fire high score
    if (wordsDrawn > user.getRapidFireHighScore()) {
      user.setRapidFireHighScore(wordsDrawn);
      // save updated user data
      try {
        UsersManager.saveUsersToJson();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * This method checks to see if the user has given up and skipped a word after starting the round
   * and increments the users losses if they do
   */
  @Override
  protected void checkSkip() {
    if (!isGameOver) {
      System.out.println("Losses don't increase when skipping in rapid fire mode");
    }
  }

  /**
   * This method gets the models predictions and sets it to the ListView component
   *
   * @throws TranslateException Thrown to indicate that an error is raised during processing of the
   *     input or output.
   */
  private void setPredictionsListView() throws TranslateException {
    // retrieve predictions
    classifications = model.getPredictions(getCurrentSnapshot(), 340);
    // set predictions to the list view component and colour top x predictions
    List<String> predictionsList = getPredictionsListForDisplay(classifications);
    predictionsListView.getItems().setAll(predictionsList);
    colourTopPredictions(predictionsListView, accuracy);
  }
}
