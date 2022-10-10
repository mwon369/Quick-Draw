package nz.ac.auckland.se206;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class UserStatsController {

  private final StringBuilder sb = new StringBuilder();

  @FXML private Label userLabel;
  @FXML private PieChart statsPieChart;
  final Label caption = new Label("");

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

    ObservableList<PieChart.Data> pieChartData =
        FXCollections.observableArrayList(
            new PieChart.Data("Wins", currentUser.getWins()),
            new PieChart.Data("Losses", currentUser.getLosses()));

    for (final PieChart.Data data : statsPieChart.getData()) {
      data.getNode()
          .addEventHandler(
              MouseEvent.MOUSE_PRESSED,
              new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                  caption.setTranslateX(e.getSceneX());
                  caption.setTranslateY(e.getSceneY());
                  caption.setText(data.getPieValue() + "%");
                }
              });
    }

    statsPieChart.setData(pieChartData);
    statsPieChart.setTitle("Your Wins and Losses");
  }

  /**
   * This method returns the user back to the main menu
   *
   * @param event, mouse click
   */
  @FXML
  private void onGoBackToMenu(ActionEvent event) {
    // retrieve the source of button and switch to the main menu page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.MENU));
  }

  @FXML
  private void onBadgeView(ActionEvent event) {
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
    wordHistoryController.showWordHistory();
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(AppUi.WORD_HISTORY));
  }
}
