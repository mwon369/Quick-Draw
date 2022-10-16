package nz.ac.auckland.se206;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.badges.Badge;

public class BadgeViewController {

  @FXML private ImageView badgeIconOne;
  @FXML private ImageView badgeIconTwo;
  @FXML private ImageView badgeIconThree;
  @FXML private ImageView badgeIconFour;
  @FXML private ImageView badgeIconFive;
  @FXML private ImageView badgeIconSix;
  @FXML private ImageView badgeIconSeven;
  @FXML private ImageView badgeIconEight;
  @FXML private ImageView badgeIconNine;
  @FXML private ImageView badgeIconTen;
  @FXML private VBox badgeOneVbox;
  @FXML private VBox badgeTwoVbox;
  @FXML private VBox badgeThreeVbox;
  @FXML private VBox badgeFourVbox;
  @FXML private VBox badgeFiveVbox;
  @FXML private VBox badgeSixVbox;
  @FXML private VBox badgeSevenVbox;
  @FXML private VBox badgeEightVbox;
  @FXML private VBox badgeNineVbox;
  @FXML private VBox badgeTenVbox;

  @FXML private Label badgeTitleLabel;
  @FXML private Label badgeDescriptionLabel;
  private List<ImageView> badgeIcons;
  private List<VBox> badgeVboxes;
  private ArrayList<Badge> badgeList;

  /**
   * This method initializes the Badge View Controller so that the FXML can load and display the
   * users badges
   */
  public void initialize() {
    // The next 10 lines are used to add all the badge image views and all the badge
    // panes into their respective lists so that other methods can access these
    // image views and panes
    badgeIcons =
        Arrays.asList(
            badgeIconOne,
            badgeIconTwo,
            badgeIconThree,
            badgeIconFour,
            badgeIconFive,
            badgeIconSix,
            badgeIconSeven,
            badgeIconEight,
            badgeIconNine,
            badgeIconTen);
    badgeVboxes =
        Arrays.asList(
            badgeOneVbox,
            badgeTwoVbox,
            badgeThreeVbox,
            badgeFourVbox,
            badgeFiveVbox,
            badgeSixVbox,
            badgeSevenVbox,
            badgeEightVbox,
            badgeNineVbox,
            badgeTenVbox);
    // Setup badge GUI for display
    badgeTitleLabel.setVisible(false);
    badgeDescriptionLabel.setVisible(false);
  }

  /**
   * Method changes the scene back to the user stats GUI
   *
   * @param event a button click
   */
  @FXML
  private void onGoBack(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the user stats page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_STATS));
    // hide the badge title label and description label
    badgeTitleLabel.setVisible(false);
    badgeDescriptionLabel.setVisible(false);
    borderCurrentTool("");
  }

  /**
   * This method loads the correct icon for each of the badges the user has earned. If they have not
   * earned the badge, it will show a question mark instead
   *
   * @throws URISyntaxException if there is an error reading the badge image
   * @throws IOException if there is an error reading the badge image
   */
  public void loadBadgeIcons() throws URISyntaxException, IOException {
    // get user badge list
    User user = UsersManager.getSelectedUser();
    badgeList = user.getBadgeList();
    // Going through each badge and loading the correct icon
    for (int i = 0; i < badgeIcons.size(); i++) {
      badgeIcons.get(i).getStyleClass().add("badges");
      if (badgeList.get(i).isCompleted()) {
        badgeIcons.get(i).setImage(loadImage("Badges/" + badgeList.get(i).getBadgeIcon()));
      } else {
        badgeIcons.get(i).setImage(loadImage("questionicon"));
      }
      // resize badge image to standard size
      badgeIcons.get(i).setFitHeight(100);
      badgeIcons.get(i).setFitWidth(100);
    }
  }

  /**
   * Thus method loads an image into an Image class to be used for ImageView
   *
   * @param imageName The name of the image
   * @return the loaded image
   * @throws URISyntaxException if the URL associated with the image is not formatted strictly
   *     according toRFC2396 and cannot be converted to a URI
   * @throws IOException if an error occurs during reading or when not able to create required
   *     ImageInputStream
   */
  private Image loadImage(String imageName) throws URISyntaxException, IOException {
    // load an image to switch to
    File file = new File(getClass().getResource("/images/" + imageName + ".png").toURI());
    BufferedImage bufferImage = ImageIO.read(file);
    return SwingFXUtils.toFXImage(bufferImage, null);
  }

  /**
   * This method displays the badge information when a badge is selected. Also borders the badge
   *
   * @param badgeId the badge to show information for
   * @param vboxName the id of the badge vbox to border
   */
  private void showInformation(int badgeId, String vboxName) {
    // Displaying the badge's info to user
    badgeTitleLabel.setText(badgeList.get(badgeId).getTitle());
    badgeDescriptionLabel.setText(badgeList.get(badgeId).getDescription());
    badgeTitleLabel.setVisible(true);
    badgeDescriptionLabel.setVisible(true);
    badgeDescriptionLabel.setWrapText(true);
    // Highlighting the current badge selected
    this.borderCurrentTool(vboxName);
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeOneSelect() {
    SoundManager.playButtonClick();
    showInformation(0, "badgeOneVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeTwoSelect() {
    SoundManager.playButtonClick();
    showInformation(1, "badgeTwoVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeThreeSelect() {
    SoundManager.playButtonClick();
    showInformation(2, "badgeThreeVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeFourSelect() {
    SoundManager.playButtonClick();
    showInformation(3, "badgeFourVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeFiveSelect() {
    SoundManager.playButtonClick();
    showInformation(4, "badgeFiveVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeSixSelect() {
    SoundManager.playButtonClick();
    showInformation(5, "badgeSixVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeSevenSelect() {
    SoundManager.playButtonClick();
    showInformation(6, "badgeSevenVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeEightSelect() {
    SoundManager.playButtonClick();
    showInformation(7, "badgeEightVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeNineSelect() {
    SoundManager.playButtonClick();
    showInformation(8, "badgeNineVbox");
  }

  /** This method plays the button click sound and also displays the badge description */
  @FXML
  private void onBadgeTenSelect() {
    SoundManager.playButtonClick();
    showInformation(9, "badgeTenVbox");
  }

  /**
   * This method adds a border to the currently selected badge so the user knows which badge is
   * currently selected
   *
   * @param badgeVboxId the vbox Id of the badge
   */
  private void borderCurrentTool(String badgeVboxId) {
    // for each tool pane, check if its id is equal to the specific tool pane id
    for (VBox badgeVbox : badgeVboxes) {
      // if id is equal, change the color to a specific color, otherwise, change to
      // transparent
      badgeVbox.setBorder(
          badgeVbox.getId().equals(badgeVboxId)
              ? new Border(
                  new BorderStroke(
                      Color.rgb(226, 159, 0),
                      BorderStrokeStyle.SOLID,
                      new CornerRadii(2),
                      new BorderWidths(4)))
              : new Border(
                  new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, null, null)));
      badgeVbox.setPadding(new Insets(0, -4, 0, -4));
    }
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }
}
