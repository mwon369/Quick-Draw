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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
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
  @FXML private Pane badgeOnePane;
  @FXML private Pane badgeTwoPane;
  @FXML private Pane badgeThreePane;
  @FXML private Pane badgeFourPane;
  @FXML private Pane badgeFivePane;
  @FXML private Pane badgeSixPane;
  @FXML private Pane badgeSevenPane;
  @FXML private Pane badgeEightPane;
  @FXML private Pane badgeNinePane;
  @FXML private Pane badgeTenPane;

  @FXML private Label badgeTitleLabel;
  @FXML private Label badgeDescriptionLabel;
  private List<ImageView> badgeIcons;
  private List<Pane> badgePanes;
  private ArrayList<Badge> badgeList;

  public void initialize() {
    badgeTitleLabel.setVisible(false);
    badgeDescriptionLabel.setVisible(false);
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
    badgePanes =
        Arrays.asList(
            badgeOnePane,
            badgeTwoPane,
            badgeThreePane,
            badgeFourPane,
            badgeFivePane,
            badgeSixPane,
            badgeSevenPane,
            badgeEightPane,
            badgeNinePane,
            badgeTenPane);
  }

  /**
   * Method changes the scene back to the user stats GUI
   *
   * @param event
   */
  @FXML
  private void onGoBack(ActionEvent event) {
    // retrieve the source of button and switch to the user stats page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_STATS));
    badgeTitleLabel.setVisible(false);
    badgeDescriptionLabel.setVisible(false);
    colorCurrentTool("");
  }

  /**
   * This method loads the correct icon for each of the badges the user has earned. If they have not
   * earned the badge, it will show a question mark instead
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  public void loadBadgeIcons() throws URISyntaxException, IOException {

    User user = UsersManager.getSelectedUser();
    badgeList = user.getBadgeList();
    // Going through each badge and loading the correct icon
    for (int i = 0; i < badgeIcons.size(); i++) {
      if (badgeList.get(i).isCompleted()) {
        badgeIcons.get(i).setImage(loadImage("achieveIcon"));
      } else {
        badgeIcons.get(i).setImage(loadImage("questionicon"));
      }
    }
  }

  /**
   * Thus method loads an image into an Image class to be used for ImageView
   *
   * @param imageName The name of the image
   * @return the loaded image
   * @throws URISyntaxException
   * @throws IOException
   */
  private Image loadImage(String imageName) throws URISyntaxException, IOException {
    // load an image to switch to
    File file;
    file = new File(getClass().getResource("/images/" + imageName + ".png").toURI());
    BufferedImage bufferImage = ImageIO.read(file);
    return SwingFXUtils.toFXImage(bufferImage, null);
  }

  /** This method displays the badge information when a badge is selected */
  private void showInformation(int badgeId, String paneName) {
    // Displaying the badge's info to user
    badgeTitleLabel.setText(badgeList.get(badgeId).getTitle());
    badgeDescriptionLabel.setText(badgeList.get(badgeId).getDescription());
    badgeTitleLabel.setVisible(true);
    badgeDescriptionLabel.setVisible(true);
    badgeDescriptionLabel.setWrapText(true);
    // Highlighting the current badge selected
    this.colorCurrentTool(paneName);
  }

  @FXML
  private void onBadgeOneSelect() {
    showInformation(0, "badgeOnePane");
  }

  @FXML
  private void onBadgeTwoSelect() {
    showInformation(1, "badgeTwoPane");
  }

  @FXML
  private void onBadgeThreeSelect() {
    showInformation(2, "badgeThreePane");
  }

  @FXML
  private void onBadgeFourSelect() {
    showInformation(3, "badgeFourPane");
  }

  @FXML
  private void onBadgeFiveSelect() {
    showInformation(4, "badgeFivePane");
  }

  @FXML
  private void onBadgeSixSelect() {
    showInformation(5, "badgeSixPane");
  }

  @FXML
  private void onBadgeSevenSelect() {
    showInformation(6, "badgeSevenPane");
  }

  @FXML
  private void onBadgeEightSelect() {
    showInformation(7, "badgeEightPane");
  }

  @FXML
  private void onBadgeNineSelect() {
    showInformation(8, "badgeNinePane");
  }

  @FXML
  private void onBadgeTenSelect() {
    showInformation(9, "badgeTenPane");
  }

  /**
   * This method changes the color of the currently selected badge so the user knows which badge is
   * currently selected
   *
   * @param badgePaneId the pane Id of the badge
   */
  private void colorCurrentTool(String badgePaneId) {
    // for each tool pane, check if its id is equal to the specific tool pane id
    for (Pane badgePane : badgePanes) {
      // if id is equal, change the color to a specific color, otherwise, change to
      // transparent
      badgePane.setBackground(
          badgePane.getId().equals(badgePaneId)
              ? new Background(new BackgroundFill(Color.web("#E29F00"), null, null))
              : new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
    }
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.onButtonHover();
  }
}
