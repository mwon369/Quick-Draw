package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.words.CategorySelector;

public class WordHistoryController {

  @FXML private Button backToProfileButton;
  @FXML private Button clearFiltersButton;
  @FXML private Button searchButton;
  @FXML private Button easyWordsButton;
  @FXML private Button mediumWordsButton;
  @FXML private Button hardWordsButton;

  @FXML private ListView<String> wordListView;
  @FXML private TextField searchTextField;

  @FXML private Label noWordsLabel;

  private ArrayList<String> userWordHistory;
  private ArrayList<String> userEasyWordHistory;
  private ArrayList<String> userMediumWordHistory;
  private ArrayList<String> userHardWordHistory;
  private User user;

  protected void showWordHistory() {
    user = UsersManager.getSelectedUser();
    userWordHistory = user.getAllPreviousWords();
    userEasyWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.E);
    userMediumWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.M);
    userHardWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.H);

    // set list view to contain all the words loaded
    wordListView.getItems().setAll(userWordHistory);
  }

  @FXML
  private void onFilterEasyWords() {}

  @FXML
  private void onFilterMediumWords() {}

  @FXML
  private void onFilterHardWords() {}

  @FXML
  private void onGoBackToProfile(ActionEvent event) {
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_STATS));
  }

  @FXML
  private void onClearFilters() {}

  @FXML
  private void onSearch() {}
}
