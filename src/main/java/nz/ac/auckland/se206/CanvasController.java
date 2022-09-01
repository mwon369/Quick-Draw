package nz.ac.auckland.se206;

import static nz.ac.auckland.se206.ml.DoodlePrediction.printPredictions;

import ai.djl.ModelException;
import ai.djl.modality.Classifications.Classification;
import ai.djl.translate.TranslateException;
import com.opencsv.exceptions.CsvException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager.AppUi;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.speech.TextToSpeech;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

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
public class CanvasController {

  private static final int TIMER_START_TIME = 60;

  @FXML private Canvas canvas;

  @FXML private Label wordLabel;
  private String targetCategory;

  // items for the timer
  @FXML private Label timerLabel;
  private Timer timer;

  @FXML private Button mainMenuButton;

  @FXML private Button startNewGameButton;

  @FXML private Button readyButton;

  private GraphicsContext graphic;

  private DoodlePrediction model;

  @FXML private CategorySelector categorySelector;

  @FXML private ListView<String> predictionsListView;
  private List<Classification> classifications;

  @FXML private Label winLossLabel;

  // items for the canvas tools
  @FXML private Pane penPane;
  @FXML private Pane eraserPane;
  @FXML private Pane clearPane;
  @FXML private Pane savePane;
  @FXML private ImageView penIcon;
  @FXML private ImageView eraserIcon;
  @FXML private ImageView clearIcon;
  @FXML private ImageView saveIcon;

  private List<Pane> toolPanes;

  private TextToSpeech textToSpeech;
  private Timer speakPredictionsTimer;
  private boolean isGameOver;

  // mouse coordinates
  private double currentX;
  private double currentY;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException {
    graphic = canvas.getGraphicsContext2D();

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setFill(Color.BLACK);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });

    model = new DoodlePrediction();

    // initialise tool panes and add each tool pane
    toolPanes = new ArrayList<Pane>();
    toolPanes.add(penPane);
    toolPanes.add(eraserPane);

    // configure canvas
    onSelectPen();
    canvas.setDisable(true);

    // initialise a category selector
    try {
      categorySelector = new CategorySelector();
    } catch (IOException | CsvException | URISyntaxException e1) {
      System.out.println("Error occured whilst selecting a random category");
      e1.printStackTrace();
    }

    readyButton.setDisable(true);
    winLossLabel.setVisible(false);

    // initialise text to speech
    textToSpeech = new TextToSpeech();
    speakPredictionsTimer = new Timer();
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**
   * This method executes when the user clicks the "Predict" button. It gets the current drawing,
   * queries the DL model and prints on the console the top 5 predictions of the DL model and the
   * elapsed time of the prediction in milliseconds.
   *
   * @throws TranslateException If there is an error in reading the input/output of the DL model.
   */
  @FXML
  private void onPredict() throws TranslateException {
    System.out.println("==== PREDICTION  ====");
    System.out.println("Top 5 predictions");

    final long start = System.currentTimeMillis();

    printPredictions(model.getPredictions(getCurrentSnapshot(), 5));

    System.out.println("prediction performed in " + (System.currentTimeMillis() - start) + " ms");
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * Switches to the main menu. Clears the word label and canvas, disables the readyButton
   *
   * @param event an ActionEvent representing the type of action that occurred
   */
  @FXML
  private void switchToMainMenu(ActionEvent event) {
    isGameOver = true;
    // reset the category label
    wordLabel.setText("");

    // configure, disable and clear the canvas
    onSelectPen();
    canvas.setDisable(true);
    onClear();
    readyButton.setDisable(true);

    winLossLabel.setVisible(false);

    // reset the timer and cancel the timer if needed
    timerLabel.setText(String.valueOf(TIMER_START_TIME));
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
   * Starts a new game, gets and display a random category, disables and clears canvas. Will enable
   * the ready button
   */
  @FXML
  private void onStartNewGame() {
    isGameOver = true;
    // select and display random category (easy)
    targetCategory = categorySelector.getRandomCategory(Difficulty.E);
    wordLabel.setText("Your word is: " + targetCategory);

    // configure, disable and clear the canvas, disable the ready button
    onSelectPen();
    canvas.setDisable(true);
    onClear();
    readyButton.setDisable(false);
    winLossLabel.setVisible(false);

    // reset the timer label and cancel previous timer if needed
    timerLabel.setText(String.valueOf(TIMER_START_TIME));
    if (timer != null) {
      timer.cancel();
    }
    timer = new Timer();

    // clear the predictions list
    predictionsListView.getItems().clear();
  }

  /**
   * Readies the game by enabling the canvas and starting the timer. Will disable the ready button
   */
  @FXML
  private void onReady() {
    isGameOver = false;

    // tell the player to start drawing
    Task<Void> startDrawingSpeechTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            textToSpeech.speak("Start Drawing!");
            return null;
          }
        };
    Thread startDrawingSpeechThread = new Thread(startDrawingSpeechTask);
    startDrawingSpeechThread.start();

    canvas.setDisable(false);
    readyButton.setDisable(true);
    startTimer();
    speakPredictions();
  }

  /**
   * Starts the timer starting from timerStartTime and terminates when timer reaches 0. Also
   * retrieves the predictions list every second
   */
  private void startTimer() {
    // schedule task every 1000 milliseconds = 1 second
    timer.scheduleAtFixedRate(
        new TimerTask() {
          private int time = TIMER_START_TIME;

          @Override
          public void run() {
            // create temporary variable for the time
            int temp = time;
            Platform.runLater(
                () -> {
                  try {
                    // retrieve predictions list and update the JavaFx ListView component
                    classifications = model.getPredictions(getCurrentSnapshot(), 10);
                    List<String> predictionsList = getPredictionsListForDisplay(classifications);
                    predictionsListView.getItems().setAll(predictionsList);
                    // check if player has won
                    if (isWin(classifications, 3)) {
                      timer.cancel();
                      stopGame(true);
                      return;
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
              timer.cancel();
              Platform.runLater(
                  () -> {
                    stopGame(false);
                  });
              return;
            } else {
              // otherwise, decrement the time by 1 second
              time -= 1;
            }
          }
        },
        0,
        1000);
  }

  /**
   * this method retrieves a formatted predictions list. Each element is a formatted string that can
   * be printed to display a prediction
   *
   * @param predictionsListClassification the predictions list that is a list of Classification
   *     objects
   * @return a formatted list of predictions. Each string is of the format "n : classification :
   *     xx.xx%" where n is the top n prediction and xx.xx is the probability
   */
  private List<String> getPredictionsListForDisplay(
      List<Classification> predictionsListClassification) {
    List<String> predictionsListForDisplay = new ArrayList<String>();
    int i = 1;
    // for each Classification, format it and append to the
    // predictionsListForDisplay
    for (Classification classification : predictionsListClassification) {
      StringBuilder sb = new StringBuilder();
      sb.append(i)
          .append(" : ")
          .append(classification.getClassName().replace("_", " "))
          .append(" : ")
          .append(String.format("%.0f%%", 100 * classification.getProbability()))
          .append(System.lineSeparator());
      predictionsListForDisplay.add(sb.toString());
      i++;
    }
    return predictionsListForDisplay;
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
   * This method speaks the top 3 predictions of the user's drawings every 10 seconds, terminating
   * whenever the game is over
   */
  private void speakPredictions() {
    // schedule the speaking for every 10 seconds and starts after 1 second
    speakPredictionsTimer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            if (isGameOver) {
              this.cancel();
              return;
            }
            textToSpeech.speak("I see ");

            // retrieve the classifications list and store in temporary list for speaking
            List<Classification> temporaryList = classifications;

            // run through the top three predictions
            for (int i = 0; i < 3; i++) {
              if (isGameOver) {
                this.cancel();
                return;
              }

              // get each prediction and speak it
              textToSpeech.speak(temporaryList.get(i).getClassName().replace("_", " "));
              if (isGameOver) {
                this.cancel();
                return;
              }
              textToSpeech.speak(i != 2 ? " orrrrr " : "");
            }
          }
        },
        1000,
        10000);
  }

  /**
   * This method stops the game and disables the canvas. It displays "You win!" or "You lose" if
   * user wins or loses the game respectively
   *
   * @param isWin boolean representing whether the user won the game or not
   */
  private void stopGame(boolean isWin) {
    canvas.setDisable(true);
    // disable mouse dragging on canvas
    canvas.setOnMouseDragged(e -> {});

    // set and display the win/loss
    winLossLabel.setText(isWin ? "You win!" : "You Lose!");
    winLossLabel.setVisible(true);

    isGameOver = true;

    // speak whether user won or lost
    Task<Void> endSpeechTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            textToSpeech.speak(isWin ? "I got it! It is " + targetCategory : "You lose!");
            return null;
          }
        };

    Thread endSpeechThread = new Thread(endSpeechTask);
    endSpeechThread.start();
  }

  /** This method changes the user's input to simulate an eraser for the canvas */
  @FXML
  private void onSelectEraser() {
    canvas.setOnMouseDragged(
        e -> {
          // clear where the user touches
          graphic.clearRect(e.getX() - 2, e.getY() - 2, 10, 10);
        });
    colorCurrentTool("eraserPane");
  }

  /** This method changes the user's input to simulate a black pen for the canvas */
  @FXML
  private void onSelectPen() {
    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setFill(Color.BLACK);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
    colorCurrentTool("penPane");
  }

  /**
   * This method changes the color of the currently selected tool so the user knows which tool is
   * currently selected
   *
   * @param toolPaneId the pane Id of the tool
   */
  private void colorCurrentTool(String toolPaneId) {
    // for each tool pane, check if its id is equal to the specific tool pane id
    for (Pane toolPane : toolPanes) {
      // if id is equal, change the color to a specific color, otherwise, change to transparent
      toolPane.setBackground(
          toolPane.getId().equals(toolPaneId)
              ? new Background(new BackgroundFill(Color.web("#faebd7"), null, null))
              : new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }
  }

  /**
   * This method saves the current canvas drawing when the save icon is clicked It saves the image
   * to a bitmap file
   */
  @FXML
  private void onSave() {
    // create /drawings folder if not already made
    final File tmpFolder = new File("drawings");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }
    // files will be saved in /drawings folder
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("drawings"));
    fileChooser.setTitle("Save");

    // save as .bmp file type
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("bmp", ".bmp"));

    // open save dialog
    File fileToSave =
        fileChooser.showSaveDialog(
            Stage.getWindows().stream().filter(Window::isShowing).findFirst().orElse(null));
    if (fileToSave != null) {
      try {

        ImageIO.write(getCurrentSnapshot(), "bmp", fileToSave);
      } catch (IOException e) {
        System.out.println("Error in saving file");
        e.printStackTrace();
      }
    }
  }
}
