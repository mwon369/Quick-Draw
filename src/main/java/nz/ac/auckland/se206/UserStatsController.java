package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class UserStatsController {

  private final StringBuilder sb = new StringBuilder();

  @FXML private Label userWinsLabel;
  @FXML private Label userLossesLabel;
  @FXML private Label userWinRatioLabel;
  @FXML private Label userFastestWinLabel;
  @FXML private Label userLabel;
  @FXML private Label totalGamesLabel;
  @FXML private Label previousWordsLabel;

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

    // show win ratio and previous words but only if user has played
    // at least one game
    if (currentUser.getWins() + currentUser.getLosses() == 0) {
      // win ratio
      sb.append("Win Ratio: N/A");
      userWinRatioLabel.setText(sb.toString());
      sb.setLength(0);

      // previous words
      sb.append("Previous Words: N/A");
      previousWordsLabel.setText(sb.toString());

    } else {
      sb.append("Win Ratio:  ");
      sb.append(String.format("%.1f", currentUser.getWinRatio()));
      sb.append("%");
      userWinRatioLabel.setText(sb.toString());
      sb.setLength(0);

      ArrayList<String> previousWords = currentUser.getLastThreeWords();
      System.out.println(previousWords.toString());
      sb.append("Previous Words: ");

      int wordNumber = 0;
      for (String word : previousWords) {
        sb.append(word);
        wordNumber++;
        if (wordNumber != previousWords.size()) {
          sb.append(", ");
        }
      }
      previousWordsLabel.setText(sb.toString());
      previousWords.clear();
    }
    sb.setLength(0);

    // show the wins but only if the user has won before
    if (currentUser.getWins() == 0) {
      sb.append("Fastest Win Time:  N/A");
    } else {
      // show the fastest win time
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
    // retrieve the source of button and switch to the login page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MENU));
  }
}
