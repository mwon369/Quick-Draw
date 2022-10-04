package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import nz.ac.auckland.se206.words.CategorySelector;

public class WordChooserController {

  @FXML private ListView<String> wordListView;

  @FXML
  private void onGoBackToGame(ActionEvent event) {
    // retrieve the source of button and switch back to zen canvas
    Button button = (Button) event.getSource();
    Scene sceneButtonIsIn = button.getScene();
    sceneButtonIsIn.setRoot(SceneManager.getUiRoot(SceneManager.AppUi.ZEN_MODE));
  }

  /**
   * This method loads all the words from the category CSV file and displays it on the ListView
   * element so the user can choose a word from there
   */
  protected void loadAllWords() {
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

    wordListView.getItems().setAll(words);
  }
}
