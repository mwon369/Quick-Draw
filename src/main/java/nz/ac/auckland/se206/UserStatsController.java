package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserStatsController {

  private final StringBuilder sb = new StringBuilder();

  @FXML private Label userWinsLabel;
  @FXML private Label userLossesLabel;
  @FXML private Label userWinRatioLabel;
  @FXML private Label userFastestWinLabel;
  @FXML private Label userLabel;
  @FXML private Label totalGamesLabel;
  @FXML private Label winStreakLabel;

  private Parent badgeViewScene;
  private BadgeViewController badgeViewController;
  private Parent wordHistoryScene;
  private WordHistoryController wordHistoryController;

  /**
   * This method loads the badgeview FXML. The reason we load it here instead of in App.java is
   * because this class needs a reference to the corresponding controller for the badgeview FXML
   *
   * <p>
   *
   * @throws IOException
   */
  public void initialize() throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/badgeview.fxml"));
    badgeViewScene = loader.load();
    badgeViewController = loader.getController();
    SceneManager.addUi(AppUi.BADGE_VIEW, badgeViewScene);
    SceneManager.getUiRoot(AppUi.BADGE_VIEW).getStylesheets().add("/css/badgeview.css");

    loader = new FXMLLoader(App.class.getResource("/fxml/wordHistory.fxml"));
    wordHistoryScene = loader.load();
    wordHistoryController = loader.getController();
    SceneManager.addUi(AppUi.WORD_HISTORY, wordHistoryScene);
    SceneManager.getUiRoot(AppUi.WORD_HISTORY).getStylesheets().add("/css/wordChooser.css");
  }

  /** This method retrieves and displays the user's profile statistics */
  protected void onRetrieveStats() {
    User currentUser = UsersManager.getSelectedUser();

    // change username label
    sb.append(currentUser.getUsername());
    userLabel.setText(sb.toString());
    sb.setLength(0);

    // show total games
    sb.append("Total Games: ");
    sb.append(currentUser.getWins() + currentUser.getLosses());
    totalGamesLabel.setText(sb.toString());
    sb.setLength(0);

    // show wins
    sb.append("Total Wins:  ");
    sb.append(currentUser.getWins());
    userWinsLabel.setText(sb.toString());
    sb.setLength(0);

    // show losses
    sb.append("Total Losses:  ");
    sb.append(currentUser.getLosses());
    userLossesLabel.setText(sb.toString());
    sb.setLength(0);

    // show losses
    sb.append("Win Streak:  ");
    sb.append(currentUser.getWinStreak());
    winStreakLabel.setText(sb.toString());
    sb.setLength(0);

    // show win ratio and previous words but only if user has played
    // at least one game
    if (currentUser.getWins() + currentUser.getLosses() == 0) {
      // win ratio
      sb.append("Win Ratio: N/A");

    } else {
      sb.append("Win Ratio:  ");
      sb.append(String.format("%.1f", currentUser.getWinRatio()));
      sb.append("%");
    }
    userWinRatioLabel.setText(sb.toString());
    sb.setLength(0);

    // show the wins but only if the user has won before
    if (currentUser.getWins() == 0) {
      sb.append("Fastest Win Time:  N/A");
    } else {
      sb.append("Fastest Win Time:  ");
      sb.append(currentUser.getFastestWin());
      sb.append(" seconds");
    }
    userFastestWinLabel.setText(sb.toString());
    sb.setLength(0);
  }

  /**
   * This method returns the user back to the main menu
   *
   * @param event, mouse click
   */
  @FXML
  private void onGoBackToMenu(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the main menu page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MENU));
  }

  @FXML
  private void onBadgeView(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.BADGE_VIEW));
    try {
      badgeViewController.loadBadgeIcons();
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  private void onWordHistory(ActionEvent event) {
    SoundManager.playButtonClick();
    wordHistoryController.showWordHistory();
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.WORD_HISTORY));
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }
}
