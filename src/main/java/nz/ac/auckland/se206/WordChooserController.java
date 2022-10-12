package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import nz.ac.auckland.se206.words.CategorySelector;

public class WordChooserController {

  @FXML private ListView<String> wordListView;
  @FXML private Label chosenWordLabel;
  @FXML private Label noWordsLabel;
  @FXML private TextField searchTextField;

  private boolean found = false;
  private List<String[]> lines;
  private ArrayList<String> words = new ArrayList<>();
  private final StringBuilder sb = new StringBuilder();

  /**
   * This method initializes the wordChooser scene which involves loading all the words into the
   * list view and enabling word selection
   */
  public void initialize() throws IOException, URISyntaxException, CsvException {
    // declare variables required to load in all words
    CategorySelector categorySelector;

    try {
      categorySelector = new CategorySelector();
    } catch (IOException | CsvException | URISyntaxException e) {
      throw new RuntimeException(e);
    }

    // get all words in exact alphabetical order
    lines = categorySelector.getLines();
    for (String[] line : lines) {
      words.add(line[0]);
    }
    // set list view to contain all the words loaded
    wordListView.getItems().setAll(words);

    // notify the user of what word they've currently selected
    wordListView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observableValue, s, t1) -> {
              // only set the word label if a word from the list is actually selected
              if (wordListView.getSelectionModel().getSelectedItem() != null) {
                sb.append("Current word chosen: ");
                sb.append(wordListView.getSelectionModel().getSelectedItem());
                chosenWordLabel.setText(sb.toString());
                sb.setLength(0);
              }
            });

    // add change listener so searching happens as the user types
    searchTextField
        .textProperty()
        .addListener(
            new ChangeListener<String>() {
              @Override
              public void changed(
                  ObservableValue<? extends String> observable, String oldValue, String newValue) {
                found = false;
                // call this function everytime the text input changes
                onSearch();
                noWordsLabel.setText(found || newValue.isEmpty() ? "" : "No words found!");
                noWordsLabel.setVisible(!found && !newValue.isEmpty());
              }
            });
  }

  /**
   * This method switches the scene back to the zen canvas and also updates the target category
   * based on what the user selected
   *
   * @param event
   */
  @FXML
  private void onGoBackToGame(ActionEvent event) {
    SoundManager.playButtonClick();
    String[] labelSplit = chosenWordLabel.getText().split(": ");

    // set the target category, word label and enable the ready button
    // but only if a category has been selected
    if (labelSplit.length > 1) {
      App.getZenCanvasController().targetCategory = labelSplit[1];
      App.getZenCanvasController()
          .wordLabel
          .setText("Your word is: " + App.getZenCanvasController().targetCategory);
      App.getZenCanvasController().readyButton.setDisable(false);

      // otherwise notify the player that they need to select a word
    } else {
      App.getZenCanvasController().wordLabel.setText("Select a word to play!");
    }

    // retrieve the source of button and switch back to zen canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_MODE));

    // clear existing searches
    onClearSearch();
  }

  /** Allows the user to search for specific words when the search button is clicked */
  @FXML
  private void onSearch() {
    SoundManager.playButtonClick();
    wordListView.getItems().clear();
    // show filtered words if something is returned
    if (!searchList(searchTextField.getText(), words).isEmpty()) {
      wordListView.getItems().addAll(searchList(searchTextField.getText(), words));
      found = true;
    }
  }

  /**
   * This helper function handles the main logic of filtering down the list of words based on what
   * the user has entered in to the search text field
   *
   * @param searchText
   * @param allWords
   * @return
   */
  private List<String> searchList(String searchText, List<String> allWords) {

    // split string into multiple words so that the search works for strings with spaces
    List<String> searchWordsList = Arrays.asList(searchText.trim().split(" "));

    // iterate through all the words and return a list of those which match/contain
    // all the words that were entered into the searchTextField
    return allWords.stream()
        .filter(
            word -> { // word = airplane for e.g.
              return searchWordsList.stream()
                  .allMatch(
                      input -> // input = airpl for e.g.
                      word.toLowerCase().contains(input.toLowerCase()));
            })
        .collect(Collectors.toList());
  }

  /** Clears any searches the user has made */
  @FXML
  private void onClearSearch() {
    SoundManager.playButtonClick();
    // reset any player searches
    wordListView.getItems().setAll(words);
    searchTextField.clear();
  }

  /** This method plays the on button hover sound effect */
  @FXML
  private void onButtonHover() {
    SoundManager.playButtonHover();
  }
}
