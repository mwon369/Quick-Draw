package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
  @FXML private Label filterLabel;

  private ArrayList<String> userWordHistory;
  private ArrayList<String> userEasyWordHistory;
  private ArrayList<String> userMediumWordHistory;
  private ArrayList<String> userHardWordHistory;

  private enum difficultyFilters {
    E,
    M,
    H,
    ALL,
  }

  protected static difficultyFilters difficultyFilter;

  public void initialize() {
    // add event handler so user can search by pressing the ENTER key
    searchTextField.setOnKeyPressed(
        new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent key) {
            if (key.getCode().equals(KeyCode.ENTER)) {
              onSearch();
            }
          }
        });
  }

  protected void showWordHistory() {
    // initialize variables to store user words
    User user = UsersManager.getSelectedUser();
    userWordHistory = user.getAllPreviousWords();
    userEasyWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.E);
    userMediumWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.M);
    userHardWordHistory = user.getWordsGiven(CategorySelector.CategoryDifficulty.H);

    // tell user what theyre filtering by
    filterLabel.setText("Filtering search by: ALL words");

    // set list view to display all the words played if the user has played a game before
    difficultyFilter = difficultyFilters.ALL;
    if (!userWordHistory.isEmpty()) {
      noWordsLabel.setText("");
      wordListView.getItems().setAll(userWordHistory);
      return;
    }
    // otherwise notify them that they haven't played a game
    noWordsLabel.setWrapText(true);
    noWordsLabel.setText("You haven't played a game yet!");
  }

  /** This method displays all the easy words the user has played */
  @FXML
  private void onFilterEasyWords() {
    difficultyFilter = difficultyFilters.E;
    searchTextField.clear();
    wordListView.getItems().clear();

    // tell user what theyre filtering by
    filterLabel.setText("Filtering search by: EASY words");

    // set list view to display all the easy words played if user has played
    // at least one easy word
    if (!userEasyWordHistory.isEmpty()) {
      noWordsLabel.setText("");
      wordListView.getItems().addAll(userEasyWordHistory);
      return;
    }
    // otherwise notify them that they haven't played any easy words
    noWordsLabel.setWrapText(true);
    noWordsLabel.setText("You haven't played any easy words yet!");
  }

  /** This method displays all the medium words the user has played */
  @FXML
  private void onFilterMediumWords() {
    difficultyFilter = difficultyFilters.M;
    searchTextField.clear();
    wordListView.getItems().clear();

    // tell user what theyre filtering by
    filterLabel.setText("Filtering search by: MEDIUM words");

    // set list view to display all the medium words played if user has played
    // at least one medium word
    if (!userMediumWordHistory.isEmpty()) {
      noWordsLabel.setText("");
      wordListView.getItems().addAll(userMediumWordHistory);
      return;
    }
    // otherwise notify them that they haven't played any medium words
    noWordsLabel.setWrapText(true);
    noWordsLabel.setText("You haven't played any medium words yet!");
  }

  /** This method displays all the hard words the user has played */
  @FXML
  private void onFilterHardWords() {
    difficultyFilter = difficultyFilters.H;
    searchTextField.clear();
    wordListView.getItems().clear();

    // tell user what theyre filtering by
    filterLabel.setText("Filtering search by: HARD words");

    // set list view to display all the hard words played if user has played
    // at least one hard word
    if (!userHardWordHistory.isEmpty()) {
      noWordsLabel.setText("");
      wordListView.getItems().addAll(userHardWordHistory);
      return;
    }
    // otherwise notify them that they haven't played any hard words
    noWordsLabel.setWrapText(true);
    noWordsLabel.setText("You haven't played any hard words yet!");
  }

  /** This method clears any filters so that all played words are displayed */
  @FXML
  private void onClearFilters() {
    difficultyFilter = difficultyFilters.ALL;
    searchTextField.clear();
    wordListView.getItems().clear();

    filterLabel.setText("Filtering search by: ALL words");

    // set list view to display all the words played if the user has played a game before
    if (!userWordHistory.isEmpty()) {
      noWordsLabel.setText("");
      wordListView.getItems().setAll(userWordHistory);
      return;
    }
    // otherwise notify them that they haven't played a game
    noWordsLabel.setWrapText(true);
    noWordsLabel.setText("You haven't played a game yet!");
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
        List<String> easyWords = searchList(searchTextField.getText(), userEasyWordHistory);

        if (!easyWords.isEmpty()) {
          wordListView.getItems().addAll(easyWords);
          return;
        }
        // display message if no matches found
        noWordsLabel.setWrapText(true);
        noWordsLabel.setText("No results found");

        break;
      case M:
        // only searches through the medium words
        wordListView.getItems().clear();
        List<String> mediumWords = searchList(searchTextField.getText(), userMediumWordHistory);

        if (!mediumWords.isEmpty()) {
          wordListView.getItems().addAll(mediumWords);
          return;
        }
        // display message if no matches found
        noWordsLabel.setWrapText(true);
        noWordsLabel.setText("No results found");

        break;
      case H:
        // only searches through the hard words
        wordListView.getItems().clear();
        List<String> hardWords = searchList(searchTextField.getText(), userHardWordHistory);

        if (!hardWords.isEmpty()) {
          wordListView.getItems().addAll(hardWords);
          return;
        }
        // display message if no matches found
        noWordsLabel.setWrapText(true);
        noWordsLabel.setText("No results found");

        break;
      case ALL:
        // searches through all words
        wordListView.getItems().clear();
        List<String> allWords = searchList(searchTextField.getText(), userWordHistory);

        if (!allWords.isEmpty()) {
          wordListView.getItems().addAll(allWords);
          return;
        }
        // display message if no matches found
        noWordsLabel.setWrapText(true);
        noWordsLabel.setText("No results found");

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
    difficultyFilter = difficultyFilters.ALL;
    searchTextField.clear();
    wordListView.getItems().clear();
    // retrieve the source of button and switch to the badge view page
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.USER_STATS));
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.onButtonHover();
  }
}
