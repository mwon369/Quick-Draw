package nz.ac.auckland.se206;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserCreationController {
  @FXML protected Canvas canvas;

  @FXML protected Label errorMessageLabel;

  @FXML protected TextField usernameField;

  // items for the canvas tools
  @FXML protected Pane penPane;
  @FXML protected Pane eraserPane;
  @FXML protected Pane clearPane;
  @FXML protected Pane savePane;
  @FXML protected Pane soundPane;
  @FXML protected Pane colorPane;
  @FXML protected ImageView penIcon;
  @FXML protected ImageView eraserIcon;
  @FXML protected ImageView clearIcon;
  @FXML protected ImageView colorIcon;
  @FXML protected ColorPicker colorPicker;

  protected List<Pane> toolPanes;
  protected double currentX;
  protected double currentY;

  protected GraphicsContext graphic;

  /** This method creates an account for a new user */
  public void initialize() {
    graphic = canvas.getGraphicsContext2D();
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });
    // initialise tool panes and add each tool pane
    toolPanes = Arrays.asList(penPane, eraserPane, colorPane);
    onSelectPen();
  }

  /**
   * This method verifies the user's sign up details and saves the users account details to the JSON
   * file
   */
  @FXML
  private void onCreateAccount() {
    SoundManager.playButtonClick();
    // check if username or password is empty
    if (usernameField.getText().isBlank()) {
      errorMessageLabel.setTextFill(Color.RED);
      errorMessageLabel.setText("You have not entered a username and/or a password");
      errorMessageLabel.setVisible(true);
      return;
    }

    // check if username is already taken
    if (UsersManager.getAllUsernames().contains(usernameField.getText())) {
      errorMessageLabel.setTextFill(Color.RED);
      errorMessageLabel.setText("This username is already in use");
      errorMessageLabel.setVisible(true);
      return;
    }

    // create user
    User newUser = new User(usernameField.getText());
    try {
      this.saveCurrentSnapshotOnFile(newUser);
    } catch (IOException e) {
      e.printStackTrace();
    }
    UsersManager.createUser(newUser);
    errorMessageLabel.setTextFill(Color.GREEN);
    errorMessageLabel.setText("Account successfully created!");
    errorMessageLabel.setVisible(true);

    App.getLoginController().loadUserGui(newUser);

    // clear all fields
    usernameField.clear();
    onClear();
  }

  /**
   * The method switches from the user creation page to the login page
   *
   * @param event an ActionEvent representing when the back to login button has been clicked
   */
  @FXML
  private void onLoginPage(ActionEvent event) {
    SoundManager.playButtonClick();
    // clear the canvas if they exit out of the sign up screen
    onClear();
    // clear username and error message
    usernameField.clear();
    errorMessageLabel.setText("");
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.LOGIN));
  }

  /** This method plays the on button hover sound effect */
  @FXML
  protected void onButtonHover() {
    SoundManager.playButtonHover();
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  protected void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /** This method changes the user's input to simulate an eraser for the canvas */
  @FXML
  protected void onSelectEraser() {
    canvas.setOnMouseDragged(
        e -> {
          // clear where the user touches
          graphic.clearRect(e.getX() - 2, e.getY() - 2, 10, 10);
        });
    colorCurrentTool("eraserPane");
  }

  /** This method changes the user's input to simulate a black pen for the canvas */
  @FXML
  protected void onSelectPen() {
    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          final double size = 4;

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

  /**
   * This method changes the color of the currently selected tool so the user knows which tool is
   * currently selected
   *
   * @param toolPaneId the pane Id of the tool
   */
  protected void colorCurrentTool(String toolPaneId) {
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
   * This method saves the users canvas drawing as an image and sets it as their profile picture
   *
   * @param user the user save the current snapshot for
   * @param user
   * @return the file to save
   * @throws IOException throws an IO exception of an error occurs with saving image to file
   */
  protected File saveCurrentSnapshotOnFile(User user) throws IOException {
    // You can change the location as you see fit.
    final File tmpFolder = new File(".profiles/profilePictures");

    if (!tmpFolder.exists()) {
      tmpFolder.mkdir();
    }
    // We save the image to a file in the tmp folder.
    user.setProfilePic(".profiles/profilePictures/" + user.getUsername() + ".png");
    final File imageToClassify = new File(user.getProfilePic());

    // Save the image to a file.
    ImageIO.write(getCurrentSnapshot(), "png", imageToClassify);

    return imageToClassify;
  }

  /**
   * This method gets the current snapshot of the canvas and can snap any color of type
   * BufferedImage.TYPE_INT_ARGB
   *
   * @return the BufferedImage snapshot
   */
  protected BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }
}
