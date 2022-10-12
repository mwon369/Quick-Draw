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
public class CanvasController {

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

  @FXML private ListView<String> predictionsListView;
  private List<Classification> classifications;

  @FXML private Label winLossLabel;

  // items for the canvas tools
  @FXML private Pane penPane;
  @FXML private Pane eraserPane;
  @FXML private Pane clearPane;
  @FXML private Pane savePane;
  @FXML private Pane soundPane;
  @FXML private ImageView penIcon;
  @FXML private ImageView eraserIcon;
  @FXML private ImageView clearIcon;
  @FXML private ImageView saveIcon;
  @FXML private ImageView soundIcon;

  @FXML private Circle topTwoHundredCircle;
  @FXML private Circle topOneHundredCircle;
  @FXML private Circle topFiftyCircle;
  @FXML private Circle topTwentyFiveCircle;

  private List<Pane> toolPanes;

  private TextToSpeech textToSpeech;
  private Timer speakPredictionsTimer;
  private boolean isGameOver;
  private boolean isDrawing;
  private boolean isPenDrawn = false;
  private boolean isMuted;
  private User user;
  private int userWins;
  private int userLosses;
  private int userFastestWin;
  private int userStreak;
  private int wordPosition = -1;

  // mouse coordinates
  private double currentX;
  private double currentY;

  // difficulties
  private int accuracy;
  private int timeLimit;
  private double confidence;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException {
    isMuted = false;
    graphic = canvas.getGraphicsContext2D();
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    model = new DoodlePrediction();

    // initialise tool panes and add each tool pane
    toolPanes = Arrays.asList(penPane, eraserPane);

    // configure canvas
    resetCanvas();

    // initialise text to speech
    textToSpeech = new TextToSpeech();
    speakPredictionsTimer = new Timer();
  }

  /** This method sets up the difficulties */
  protected void setUpDifficulty() {
    user = UsersManager.getSelectedUser();
    // initialise difficulties
    accuracy = DifficultyManager.getAccuracy(user.getAccuracyDifficulty());
    timeLimit = DifficultyManager.getTimeLimit(user.getTimeLimitDifficulty());
    confidence = DifficultyManager.getConfidence(user.getConfidenceDifficulty());
    timerLabel.setText(String.valueOf(timeLimit));
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    isPenDrawn = false;
    isDrawing = false;
    predictionsListView.getItems().setAll("");
    resetIndicator();
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
   * @throws IOException
   * @throws URISyntaxException
   */
  @FXML
  private void onSwitchToMainMenu(ActionEvent event) throws URISyntaxException, IOException {
    SoundManager.playButtonClick();
    SoundManager.setBackgroundMusicVolume(0.2);

    isMuted = false;
    soundIcon.setImage(loadImage("unmute"));
    resetCanvas();
    // reset the timer and cancel the timer if needed
    timerLabel.setText(String.valueOf(timeLimit));
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

  private void resetCanvas() {
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
    savePane.setDisable(true);
    winLossLabel.setVisible(false);
  }

  /**
   * Starts a new game, gets and display a random category, disables and clears canvas. Will enable
   * the ready button
   */
  @FXML
  private void onStartNewGame() {
    SoundManager.playButtonClick();

    resetCanvas();
    // select and display random category (easy)
    userStreak = user.getWinStreak();
    userWins = user.getWins();
    userLosses = user.getLosses();
    userFastestWin = user.getFastestWin();

    targetCategory = user.giveWordToDraw(user.getWordDifficulty());
    wordLabel.setText("Your word is: " + targetCategory);
    // configure, disable and clear the canvas, disable the ready button

    readyButton.setDisable(false);

    // reset the timer label and cancel previous timer if needed
    timerLabel.setText(String.valueOf(timeLimit));
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
    SoundManager.playButtonClick();
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

    // Starting the game and begin the timer and predictions
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
          private int time = timeLimit;

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

  private void colourTopPredictions(ListView<String> predictionsListView, int topNum) {

    // set the CellFactory field for the ListView
    predictionsListView.setCellFactory(
        list -> {
          // now we can edit & return specific cells in the ListView
          return new ListCell<String>() {
            @Override
            protected void updateItem(String prediction, boolean empty) {
              super.updateItem(prediction, empty);
              // check if prediction text is empty
              if (empty || prediction.equals("")) {
                // reset color & text for each cell so ListView is blank
                setStyle("-fx-background-color: white;");
                setText("");
              } else {
                // set the colour for the top x model predictions
                setStyle(
                    getIndex() < topNum
                        ? "-fx-background-color: #DEC98A;"
                        : "-fx-background-color: white;");
                // set the text to the prediction for each cell
                setText(prediction);
              }
            }
          };
        });
  }

  @FXML
  private void isDrawing() {
    if (isPenDrawn) {
      isDrawing = true;
    }
  }

  /**
   * this method retrieves a formatted predictions list. Each element is a formatted string that can
   * be printed to display a prediction
   *
   * @param predictionsListClassification the predictions list that is a list of Classification
   *     objects
   * @return a formatted list of predictions. Each string is of the format "n : classification :
   *     xx%" where n is the top n prediction and xx is the probability
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
      if (i == 11) {
        break;
      }
    }
    return predictionsListForDisplay;
  }

  /**
   * This method checks if the target word is within a list of classifications
   *
   * @param classifications a list of classifications
   * @return true if the target word is in the list of classifications, false otherwise
   */
  private boolean isWin(List<Classification> classifications, int margin, double confidence) {
    // scan through classifications and check if the target word is in it and
    // confidence is
    // sufficient with rounding
    for (int i = 0; i < margin; i++) {
      if (classifications.get(i).getClassName().replace("_", " ").equals(targetCategory)
          && Math.round(classifications.get(i).getProbability() * 100)
              >= Math.round(confidence * 100)) {
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
            // checks to see if the player has started drawing
            if (!isDrawing || isMuted) {
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
   * This method mutes and unmutes the text to speech from speaking the top 3 predictions
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  @FXML
  private void onToggleSound() throws URISyntaxException, IOException {
    SoundManager.playButtonClick();
    isMuted = isMuted ? false : true;
    String soundState = isMuted ? "mute" : "unmute";
    soundIcon.setImage(loadImage(soundState));
  }

  /**
   * This method loads an image into an image class to be used by an ImageView class
   *
   * @param soundState the name of the image to be loaded
   * @return An Image object containing the correct image
   * @throws URISyntaxException
   * @throws IOException
   */
  private Image loadImage(String soundState) throws URISyntaxException, IOException {
    // load an image to switch to
    File file;
    file = new File(getClass().getResource("/images/" + soundState + ".png").toURI());
    BufferedImage bufferImage = ImageIO.read(file);
    return SwingFXUtils.toFXImage(bufferImage, null);
  }

  /**
   * This method stops the game and disables the canvas. It displays "You win!" or "You lose" if
   * user wins or loses the game respectively
   *
   * @param isWin boolean representing whether the user won the game or not
   */
  private void stopGame(boolean isWin, String timeString) {
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
          isPenDrawn = true;
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
      // if id is equal, change the color to a specific color, otherwise, change to
      // transparent
      toolPane.setBackground(
          toolPane.getId().equals(toolPaneId)
              ? new Background(new BackgroundFill(Color.web("#E29F00"), null, null))
              : new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }
  }

  /**
   * This method updates the indicators depending on the current position of the word being drawn
   */
  private void updateIndicator() {
    /*
     * This set of conditional statements checks if the sord's position meets the
     * indicator's requirements to be highlighted
     */
    if (wordPosition <= 200) {
      topTwoHundredCircle.setFill(Color.GREEN);
    } else {
      topTwoHundredCircle.setFill(Color.rgb(247, 236, 198, 1));
    }
    if (wordPosition <= 100) {
      topOneHundredCircle.setFill(Color.GREEN);
    } else {
      topOneHundredCircle.setFill(Color.rgb(247, 236, 198, 1));
    }
    if (wordPosition <= 50) {
      topFiftyCircle.setFill(Color.GREEN);
    } else {
      topFiftyCircle.setFill(Color.rgb(247, 236, 198, 1));
    }
    if (wordPosition <= 25) {
      topTwentyFiveCircle.setFill(Color.GREEN);
    } else {
      topTwentyFiveCircle.setFill(Color.rgb(247, 236, 198, 1));
    }
  }

  /** This method resets the all indicators of the word's current position to transparent */
  private void resetIndicator() {
    // Resetting all indicators
    topTwoHundredCircle.setFill(Color.rgb(247, 236, 198, 1));
    topOneHundredCircle.setFill(Color.rgb(247, 236, 198, 1));
    topFiftyCircle.setFill(Color.rgb(247, 236, 198, 1));
    topTwentyFiveCircle.setFill(Color.rgb(247, 236, 198, 1));
  }

  /**
   * This method finds the position of the word in the prediction list
   *
   * @return the index of the location of the word to draw
   */
  private int findWordPosition() {
    // Finding the position of the word in the prediction list
    for (Classification classification : classifications) {
      if (classification.getClassName().replaceAll("_", " ").equals(targetCategory)) {
        return classifications.indexOf(classification);
      }
    }
    return -1;
  }

  /**
   * This method saves the current canvas drawing when the save icon is clicked It saves the image
   * to a bitmap file
   */
  @FXML
  private void onSave() {
    SoundManager.playButtonClick();
    // create /drawings folder if not already made
    final File tmpFolder = new File("drawings");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }
    // files will be saved in /drawings folder
    FileChooser fileChooser = new FileChooser();
    fileChooser.setInitialDirectory(new File("drawings"));
    fileChooser.setTitle("Save");

    // files by default will be named the category given to the user alongside the
    // current time & date
    String[] timeStamp = java.time.LocalDateTime.now().toString().split("\\.");
    String timeAndDate = timeStamp[0].replaceAll(":", "-").replace("T", "-");
    StringBuilder sb = new StringBuilder();
    sb.append(targetCategory);
    sb.append(" - ");
    // only append the time & date
    sb.append(timeAndDate);
    fileChooser.setInitialFileName(sb.toString());

    // save as .bmp, .PNG or .jpg file type
    fileChooser
        .getExtensionFilters()
        .addAll(
            new ExtensionFilter("bmp", ".bmp"),
            new ExtensionFilter(".png", "*.PNG"),
            new ExtensionFilter(".jpg", "*.jpg"));

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

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }
}
