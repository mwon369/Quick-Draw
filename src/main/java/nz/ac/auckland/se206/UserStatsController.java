package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserStatsController {

  @FXML private Label userLabel;
  @FXML private PieChart statsPieChart;
  @FXML private Label noGamesLabel;
  @FXML private Label fastestWinLabel;
  @FXML private Label winStreakLabel;
  @FXML private Label rapidFireScoreLabel;
  @FXML private VBox otherStatVBox;

  private User currentUser;

  private final String[] colours = {"orange", "#FFA071"};

  /** This method retrieves and displays the user's profile statistics */
  protected void onRetrieveStats() {
    // get logged in user
    currentUser = UsersManager.getSelectedUser();
    userLabel.setText(currentUser.getUsername());

    // don't show pie chart and other stats if user has no games played
    if (currentUser.getWins() + currentUser.getLosses() == 0
        && currentUser.getRapidFireHighScore() == 0) {
      fastestWinLabel.setVisible(false);
      winStreakLabel.setVisible(false);
      rapidFireScoreLabel.setVisible(false);
      otherStatVBox.setVisible(false);
      statsPieChart.setVisible(false);
      noGamesLabel.setVisible(true);
      noGamesLabel.setWrapText(true);
      return;
    }

    noGamesLabel.setVisible(false);

    // only show pie chart if user has wins/losses (rapid fire mode doesn't
    // count towards these so we need to check)
    if (currentUser.getWins() + currentUser.getLosses() > 0) {
      // create pie chart data and make pie chart visible
      statsPieChart.setVisible(true);
      ObservableList<PieChart.Data> pieChartData =
          FXCollections.observableArrayList(
              new PieChart.Data("Wins", currentUser.getWins()),
              new PieChart.Data("Losses", currentUser.getLosses()));

      // set data to pie chart and colour it
      statsPieChart.setData(pieChartData);
      colourPieChart(pieChartData);
      changeLegendLabels();
      changeLegendSymbols();

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

    // set other stat data
    if (currentUser.getWins() == 0) {
      fastestWinLabel.setText("Your fastest win time: N/A");
    } else {
      fastestWinLabel.setText(
          currentUser.getFastestWin() > 1 || currentUser.getFastestWin() == 0
              ? "Your fastest win time: " + currentUser.getFastestWin() + " seconds"
              : "Your fastest win time: 1 second");
    }

    winStreakLabel.setText("Your current win streak: " + currentUser.getWinStreak());
    rapidFireScoreLabel.setText(
        "Your Rapid Fire high score: " + currentUser.getRapidFireHighScore());

    // make labels visible
    otherStatVBox.setVisible(true);
    fastestWinLabel.setVisible(true);
    winStreakLabel.setVisible(true);
    rapidFireScoreLabel.setVisible(true);
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

  /**
   * This method colours the pie chart with custom colours whenever we load the user stats page.
   * This is to prevent the colours from alternating when you continuously reload the user stats
   * page
   *
   * @param pieChartData a list of the pie chart data
   */
  private void colourPieChart(ObservableList<PieChart.Data> pieChartData) {
    int i = 0;
    for (PieChart.Data data : pieChartData) {
      data.getNode().setStyle("-fx-pie-color: " + colours[i % colours.length] + ";");
      i++;
    }
  }

  /**
   * This method changes the colours of the pie chart legend symbols so that they are matching with
   * the colours of the pie chart itself
   */
  private void changeLegendSymbols() {
    // get the set of all labels in the pie chart legend
    Set<Node> legendLabels = statsPieChart.lookupAll("Label.chart-legend-item");
    int i = 0;

    // iterate through the labels
    for (Node legendLabel : legendLabels) {
      Label label = (Label) legendLabel;
      Circle circle;
      // if we're iterating through the first label,
      // change the colour of the label symbol to orange
      if (i == 0) {
        circle = new Circle(5.0, Color.ORANGE);
      } else {
        // otherwise change the label symbol colour to salmon
        circle = new Circle(5.0, Color.SALMON);
      }
      label.setGraphic(circle);
      i++;
    }
  }

  /**
   * This method changes the text of the chart legend labels so that they display the number of wins
   * and losses the user has.
   */
  private void changeLegendLabels() {
    // get the set of all labels in the pie chart legend
    Set<Node> legendLabels = statsPieChart.lookupAll("Label.chart-legend-item");
    int i = 0;
    // iterate through the labels
    for (Node legendLabel : legendLabels) {
      Label label = (Label) legendLabel;
      // if we're iterating through the first label, set the text for wins
      // otherwise set the text for losses
      label.setText(
          i == 0 ? "Wins: " + currentUser.getWins() : "Losses: " + currentUser.getLosses());
      i++;
    }
  }
}
