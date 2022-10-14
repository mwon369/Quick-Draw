package nz.ac.auckland.se206;

import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.words.CategorySelector;

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
public class ZenCanvasController extends CanvasController {
  @FXML private Label outcomeLabel;

  @FXML private Button chooseWordButton;
  @FXML private Button randomWordButton;

  @FXML protected Pane colorPane;
  @FXML protected ColorPicker colorPicker;

  private CategorySelector categorySelector;

  /**
   * Switches to the main menu. Clears the word label and canvas, disables the readyButton and
   * resets the pen colour to black
   *
   * @param event an ActionEvent representing the type of action that occurred
   * @throws IOException if there is a problem with the input/output
   * @throws URISyntaxException if there is a problem with the syntax
   */
  @FXML
  @Override
  protected void onSwitchToMainMenu(ActionEvent event) throws URISyntaxException, IOException {
    SoundManager.playButtonClick();
    SoundManager.setBackgroundMusicVolume(0.2);

    isMuted = true;
    soundIcon.setImage(loadImage("mute"));
    colorPicker.setValue(Color.BLACK);
    resetCanvas();

    if (timer != null) {
      timer.cancel();
    }

    // reset the predictions list
    predictionsListView.getItems().clear();

    // switch to the main menu scene
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.MENU));
  }

  /**
   * This method clears the canvas and resets all interactive UI elements to their default values.
   * For Zen mode it also resets the pen colour to black.
   */
  @Override
  protected void resetCanvas() {
    resetIndicator();
    isPenDrawn = false;
    isGameOver = true;
    // reset the category label
    wordLabel.setText("");

    // configure, disable and clear the canvas
    onSelectPen();
    canvas.setDisable(true);
    onClear();
    readyButton.setDisable(true);
    // user will be able to save their drawing at any point in zen mode
    savePane.setDisable(false);
    outcomeLabel.setVisible(false);
    // reset pen colour to black
    colorPicker.setValue(Color.BLACK);
  }

  /**
   * This method switches the scene and sets the target category to whatever the user selects in the
   * Word Chooser FXML scene
   *
   * @param event a button click
   */
  @FXML
  private void onChooseWord(ActionEvent event) {
    SoundManager.playButtonClick();
    // erase all drawing and reset all GUI elements
    resetCanvas();

    // reset timer
    if (timer != null) {
      timer.cancel();
    }
    timer = new Timer();

    // clear the predictions list
    predictionsListView.getItems().clear();

    // retrieve the source of button and switch to word chooser scene
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.WORD_CHOOSER));
  }

  /**
   * Starts a new game, gets and display a random category, disables and clears canvas. Will enable
   * the ready button
   */
  @FXML
  private void onGetRandomWord() throws IOException, URISyntaxException, CsvException {
    SoundManager.playButtonClick();
    // clear canvas
    resetCanvas();

    // get random category and display to user
    categorySelector = new CategorySelector();
    targetCategory = categorySelector.getRandomWord();
    wordLabel.setText("Your word is: " + targetCategory);
    readyButton.setDisable(false);

    // cancel timer
    if (timer != null) {
      timer.cancel();
    }
    timer = new Timer();

    // clear the predictions list
    predictionsListView.getItems().clear();
  }

  /**
   * Uses a timer object to refresh the model guesses every second and check if the user has won.
   * There is no associated time with the timer object so the user can draw for as long as they
   * want, even if they have won
   */
  @Override
  protected void startTimer() {
    // schedule task every 1000 milliseconds = 1 second
    timer.scheduleAtFixedRate(
        new TimerTask() {

          @Override
          public void run() {
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
                      colourTopPredictions(predictionsListView, 1);
                      if (isWin(classifications, 1)) {
                        outcomeLabel.setText(
                            "Nice, your word was #1 ! Feel free to keep drawing or choose a new word!");
                        outcomeLabel.setVisible(true);
                      }
                    }
                  } catch (TranslateException e) {
                    System.out.println("Unable to retrieve predictions");
                    e.printStackTrace();
                  }
                });
          }
        },
        0,
        1000);
  }

  /**
   * This method checks if the target word is within a list of classifications
   *
   * @param classifications a list of classifications
   * @return true if the target word is in the list of classifications, false otherwise
   */
  private boolean isWin(List<Classification> classifications, int margin) {
    // scan through classifications and check if the target word is in it
    for (int i = 0; i < margin; i++) {
      if (classifications.get(i).getClassName().replace("_", " ").equals(targetCategory)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method changes the user's selected tool to the pen which is the colour they have selected
   * from the colour picker
   */
  @FXML
  @Override
  protected void onSelectPen() {
    canvas.setOnMouseDragged(
        e -> {
          isPenDrawn = true;
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(colorPicker.getValue());
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
    colorCurrentTool("penPane");
  }
}
