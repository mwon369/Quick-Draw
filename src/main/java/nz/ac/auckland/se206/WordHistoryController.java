package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

  public void initialize() {

    //        user = UsersManager.getSelectedUser();
    //        userWordHistory = user.getAllPreviousWords();
    //        userEasyWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.E);
    //        userMediumWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.M);
    //        userHardWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.H);
    //
    //        // set list view to contain all the words loaded
    //        wordListView.getItems().setAll(userWordHistory);
  }

  protected void showWordHistory() {}

  @FXML
  private void onFilterEasyWords() {}

  @FXML
  private void onFilterMediumWords() {}

  @FXML
  private void onFilterHardWords() {}

  @FXML
  private void onGoBackToStats() {}

  @FXML
  private void onClearFilters() {}

  @FXML
  private void onSearch() {}
}
