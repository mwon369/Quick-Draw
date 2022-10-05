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
    badgePanes = Arrays.asList(badgeOnePane, badgeTwoPane);
  }

  @FXML
  private void onGoBackToMenu(ActionEvent event) {
    // retrieve the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MENU));
    badgeTitleLabel.setVisible(false);
    badgeDescriptionLabel.setVisible(false);
    colorCurrentTool("");
  }

  public void loadBadgeIcons() throws URISyntaxException, IOException {

    User user = UsersManager.getSelectedUser();
    badgeList = user.getBadgeList();
    for (int i = 0; i < badgeIcons.size(); i++) {
      if (badgeList.get(i).isCompleted()) {
        badgeIcons.get(i).setImage(loadImage("achieveIcon"));
      } else {
        badgeIcons.get(i).setImage(loadImage("questionicon"));
      }
    }
  }

  private Image loadImage(String imageName) throws URISyntaxException, IOException {
    // load an image to switch to
    File file;
    file = new File(getClass().getResource("/images/" + imageName + ".png").toURI());
    BufferedImage bufferImage = ImageIO.read(file);
    return SwingFXUtils.toFXImage(bufferImage, null);
  }

  @FXML
  private void onbadgeOneSelect() {
    badgeTitleLabel.setText(badgeList.get(0).getTitle());
    badgeDescriptionLabel.setText(badgeList.get(0).getDescription());
    badgeTitleLabel.setVisible(true);
    badgeDescriptionLabel.setVisible(true);
    this.colorCurrentTool("badgeOnePane");
  }

  @FXML
  private void onbadgeTwoSelect() {
    badgeTitleLabel.setText(badgeList.get(1).getTitle());
    badgeDescriptionLabel.setText(badgeList.get(1).getDescription());
    badgeTitleLabel.setVisible(true);
    badgeDescriptionLabel.setVisible(true);
    this.colorCurrentTool("badgeTwoPane");
  }

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
}
