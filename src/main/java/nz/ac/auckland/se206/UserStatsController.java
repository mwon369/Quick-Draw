package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserStatsController {

  private final StringBuilder sb = new StringBuilder();

  @FXML private Label statsTitleLabel;
  @FXML private Label userWinsLabel;
  @FXML private Label userLossesLabel;
  @FXML private Label userWinRatioLabel;
  @FXML private Label userFastestWinLabel;
  @FXML private Label noGamesPlayedLabel;

  /**
   * This method retrieves and displays the user's profile statistics when they click the "Get My
   * Stats!" button.
   */
  @FXML
  private void onRetrieveStats() {
    User currentUser = UsersManager.getSelectedUser();

    // change title label
    sb.append("Profile Statistics For: ");
    sb.append(currentUser.getUsername());
    statsTitleLabel.setText(sb.toString());
    sb.setLength(0);

    // check if the user has played no games
    if (currentUser.getWins() + currentUser.getLosses() == 0) {
      noGamesPlayedLabel.setVisible(true);
      return;
    }

    // show wins
    sb.append("Total Wins:  ");
    sb.append(currentUser.getWins());
    userWinsLabel.setText(sb.toString());
    userWinsLabel.setVisible(true);
    sb.setLength(0);

    // show losses
    sb.append("Total Losses:  ");
    sb.append(currentUser.getLosses());
    userLossesLabel.setText(sb.toString());
    userLossesLabel.setVisible(true);
    sb.setLength(0);

    // show win ratio
    sb.append("Win Ratio:  ");
    sb.append(String.format("%.1f", currentUser.getWinRatio()));
    sb.append("%");
    userWinRatioLabel.setText(sb.toString());
    userWinRatioLabel.setVisible(true);
    sb.setLength(0);

    // check if the user has never won before
    if (currentUser.getWins() == 0) {
      sb.append("Fastest Win Time:  N/A");
      userFastestWinLabel.setText(sb.toString());
      userFastestWinLabel.setVisible(true);
      sb.setLength(0);
      return;
    }

    // show the fastest win time
    sb.append("Fastest Win Time:  ");
    sb.append(currentUser.getFastestWin());
    sb.append(" seconds");
    userFastestWinLabel.setText(sb.toString());
    userFastestWinLabel.setVisible(true);
    sb.setLength(0);
  }

  /**
   * This method returns the user back to the main menu
   *
   * @param event, mouse click
   */
  @FXML
  private void onGoBackToMenu(ActionEvent event) {
    // reset visibility of labels
    userWinsLabel.setVisible(false);
    userLossesLabel.setVisible(false);
    userWinRatioLabel.setVisible(false);
    userFastestWinLabel.setVisible(false);
    noGamesPlayedLabel.setVisible(false);

    // change title label back to default
    sb.append("Profile Statistics For: ");
    statsTitleLabel.setText(sb.toString());
    sb.setLength(0);

    // retrieve the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MENU));
  }
}
