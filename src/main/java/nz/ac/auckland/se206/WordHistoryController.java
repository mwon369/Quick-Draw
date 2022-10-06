package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

  private enum difficultyFilters {
    E,
    M,
    H,
    ALL,
  }

  protected static difficultyFilters difficultyFilter;

  protected void showWordHistory() {
    // initialize variables to store user words
    user = UsersManager.getSelectedUser();
    userWordHistory = user.getAllPreviousWords();
    userEasyWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.E);
    userMediumWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.M);
    userHardWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.H);

    // set list view to display all the words played
    difficultyFilter = difficultyFilters.ALL;
    wordListView.getItems().setAll(userWordHistory);
  }

  /** This method displays all the easy words the user has played */
  @FXML
  private void onFilterEasyWords() {
    difficultyFilter = difficultyFilters.E;
    searchTextField.clear();
    wordListView.getItems().clear();
    wordListView.getItems().addAll(userEasyWordHistory);
  }

  /** This method displays all the medium words the user has played */
  @FXML
  private void onFilterMediumWords() {
    difficultyFilter = difficultyFilters.M;
    searchTextField.clear();
    wordListView.getItems().clear();
    wordListView.getItems().addAll(userMediumWordHistory);
  }

  /** This method displays all the hard words the user has played */
  @FXML
  private void onFilterHardWords() {
    difficultyFilter = difficultyFilters.H;
    searchTextField.clear();
    wordListView.getItems().clear();
    wordListView.getItems().addAll(userHardWordHistory);
  }

  /** This method clears any filters so that all played words are displayed */
  @FXML
  private void onClearFilters() {
    difficultyFilter = difficultyFilters.ALL;
    searchTextField.clear();
    wordListView.getItems().clear();
    wordListView.getItems().addAll(userWordHistory);
  }

  /**
   * This method allows users to search for a specific word in their word history. If a difficulty
   * filter is currently active then they will only be able to search through words of that specific
   * difficulty
   */
  @FXML
  private void onSearch() {
    switch (difficultyFilter) {
      case E:
        // only searches through the easy words
        wordListView.getItems().clear();
        wordListView.getItems().addAll(searchList(searchTextField.getText(), userEasyWordHistory));
        break;
      case M:
        // only searches through the medium words
        wordListView.getItems().clear();
        wordListView
            .getItems()
            .addAll(searchList(searchTextField.getText(), userMediumWordHistory));
        break;
      case H:
        // only searches through the hard words
        wordListView.getItems().clear();
        wordListView.getItems().addAll(searchList(searchTextField.getText(), userHardWordHistory));
        break;
      case ALL:
        // searches through all words
        wordListView.getItems().clear();
        wordListView.getItems().addAll(searchList(searchTextField.getText(), userWordHistory));
        break;
    }
  }

  /**
   * This helper method takes an input from the TextField and filters through the ArrayLists to find
   * and return Strings that match the input
   *
   * @param searchText, the input entered into the TextField
   * @param wordList, the words played by the user
   * @return a list with all the matching Strings
   */
  private List<String> searchList(String searchText, List<String> wordList) {

    // split string into multiple words so that the search works for strings with spaces
    List<String> searchWordsList = Arrays.asList(searchText.trim().split(" "));

    // iterate through all the words and return a list of those which match/contain
    // all the words that were entered into the searchTextField
    return wordList.stream()
        .filter(
            word -> { // word = airplane for e.g.
              return searchWordsList.stream()
                  .allMatch(
                      input -> // input = airpl for e.g.
                      word.toLowerCase().contains(input.toLowerCase()));
            })
        .collect(Collectors.toList());
  }

  /**
   * This method allows the user to switch back to their profile page
   *
   * @param event, a button click
   */
  @FXML
  private void onGoBackToProfile(ActionEvent event) {
    onClearFilters();
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_STATS));
  }
}
