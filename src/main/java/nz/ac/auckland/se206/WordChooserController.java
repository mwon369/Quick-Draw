package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import nz.ac.auckland.se206.words.CategorySelector;

public class WordChooserController {

  @FXML private ListView<String> wordListView;
  @FXML private Label chosenWordLabel;

  protected ZenCanvasController zenCanvasController = null;

  private final StringBuilder sb = new StringBuilder();

  /**
   * This method initializes the wordChooser scene which involves loading all the words into the
   * list view and enabling word selection
   */
  public void initialize() {

    // declare variables required to load in all words
    CategorySelector categorySelector;
    ArrayList<String> words = new ArrayList<>();

    try {
      categorySelector = new CategorySelector();
    } catch (IOException | CsvException | URISyntaxException e) {
      throw new RuntimeException(e);
    }

    // get words from every difficulty
    words.addAll(categorySelector.getWordList(CategorySelector.Difficulty.E));
    words.addAll(categorySelector.getWordList(CategorySelector.Difficulty.M));
    words.addAll(categorySelector.getWordList(CategorySelector.Difficulty.H));

    // set list view to contain all the words loaded
    wordListView.getItems().setAll(words);

    // notify the user of what word they've currently selected
    wordListView
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observableValue, s, t1) -> {
              sb.append("Current word chosen: ");
              sb.append(wordListView.getSelectionModel().getSelectedItem());
              chosenWordLabel.setText(sb.toString());
              sb.setLength(0);
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
    String[] labelSplit = chosenWordLabel.getText().split(": ");

    // set the target category, word label and enable the ready button
    // but only if a category has been selected
    if (labelSplit.length > 1) {
      zenCanvasController.targetCategory = labelSplit[1];
      zenCanvasController.wordLabel.setText("Your word is: " + zenCanvasController.targetCategory);
      zenCanvasController.readyButton.setDisable(false);

      // otherwise notify the player that they need to select a word
    } else {
      zenCanvasController.wordLabel.setText("Select a word to play!");
    }

    // retrieve the source of button and switch back to zen canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_MODE));
  }
}
