package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserStatsController {

  @FXML private Label userLabel;
  @FXML private PieChart statsPieChart;
  @FXML private Label noGamesLabel;

  /** This method retrieves and displays the user's profile statistics */
  protected void onRetrieveStats() {
    // get logged in user
    User currentUser = UsersManager.getSelectedUser();
    userLabel.setText(currentUser.getUsername());

    // don't show pie chart if user has no games played
    if (currentUser.getWins() + currentUser.getLosses() == 0) {
      statsPieChart.setVisible(false);
      noGamesLabel.setVisible(true);
      return;
    }

    // show pie chart
    noGamesLabel.setVisible(false);
    statsPieChart.setVisible(true);
    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Wins", currentUser.getWins()),
            new PieChart.Data("Losses", currentUser.getLosses()));

    statsPieChart.setData(pieChartData);

    // show individual numbers for each wedge
    statsPieChart
        .getData()
        .forEach(
            data -> {
              String number = String.format(String.valueOf((int) data.getPieValue()));
              Tooltip toolTip = new Tooltip(number);
              Tooltip.install(data.getNode(), toolTip);
            });
  }

  /**
   * This method returns the user back to the main menu
   *
   * @param event a mouse click
   */
  @FXML
  private void onGoBackToMenu(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the main menu page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MENU));
  }

  /**
   * This method sets the scene to the badge viewer scene
   *
   * @param event a button click
   */
  @FXML
  private void onBadgeView(ActionEvent event) {
    SoundManager.playButtonClick();
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.BADGE_VIEW));
    try {
      App.getBadgeViewController().loadBadgeIcons();
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method sets the scene to the word history scene
   *
   * @param event a button click
   */
  @FXML
  private void onWordHistory(ActionEvent event) {
    SoundManager.playButtonClick();
    App.getWordHistoryController().showWordHistory();
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
