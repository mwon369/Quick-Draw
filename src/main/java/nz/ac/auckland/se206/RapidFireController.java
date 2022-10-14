package nz.ac.auckland.se206;

import ai.djl.translate.TranslateException;
import java.util.List;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RapidFireController extends CanvasController {

  @FXML private Label gameTitleLabel;

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
                      // topNum value will depend on game difficulty later on
                      colourTopPredictions(predictionsListView, accuracy);
                      // check if player has won
                      if (isWin(classifications, accuracy, confidence)) {
                        SoundManager.playWinSound();
                        timer.cancel();
                        stopGame(true, timerLabel.getText());
                        return;
                      }
                    }
                  } catch (TranslateException e) {
                    System.out.println("Unable to retrieve predictions");
                    e.printStackTrace();
                  }

                  // update timer
                  timerLabel.setText(String.valueOf(temp));
                });

            // if time has run out, cancel timer
            if (time == 0) {
              SoundManager.playAlarmBell();
              timer.cancel();
              Platform.runLater(
                  () -> {
                    stopGame(false, timerLabel.getText());
                  });
              return;
            } else {
              if (time <= 5) {
                SoundManager.playTimerTickFast();
              } else {
                SoundManager.playTimerTick();
              }
              // otherwise, decrement the time by 1 second
              time -= 1;
            }
          }
        },
        0,
        1000);
  }
}
