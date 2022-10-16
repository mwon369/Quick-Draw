package nz.ac.auckland.se206.words;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CategorySelector {

  public enum CategoryDifficulty {
    E,
    M,
    H
  }

  private Map<CategoryDifficulty, List<String>> difficultyToCategories;

  /**
   * This method creates a map of each difficulty to its corresponding list of words
   *
   * @throws IOException if an error occurs whilst reading the category file
   * @throws CsvException if an error occurs whilst reading the category file
   * @throws URISyntaxException if an error occurs whilst reading the category file
   */
  public CategorySelector() throws IOException, CsvException, URISyntaxException {
    difficultyToCategories = new HashMap<>();
    for (CategoryDifficulty difficulty : CategoryDifficulty.values()) {
      // creates an empty ArrayList for each difficulty
      difficultyToCategories.put(difficulty, new ArrayList<>());
    }

    for (String[] line : getLines()) {
      // adds each word on each line to its corresponding difficulty ArrayList
      difficultyToCategories.get(CategoryDifficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * This method gets a random word of a specified difficulty
   *
   * @param difficulty difficulty of words to select from
   * @return a random cateogry of the specified difficlty
   */
  public String getRandomCategory(CategoryDifficulty difficulty) {
    // returns a random category of a specified difficulty
    return difficultyToCategories
        .get(difficulty)
        .get(new Random().nextInt(difficultyToCategories.get(difficulty).size()));
  }

  /**
   * Thus method returns a list of string array where each string array is each line in the csv file
   *
   * @return a list of String[] where each String[] represents a line in the csv file
   * @throws IOException if an error occurs whilst reading the category file
   * @throws CsvException if an error occurs whilst reading the category file
   * @throws URISyntaxException if an error occurs whilst reading the category file
   */
  public List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File fileName =
        new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());

    try (FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      // returns a list of string array where each string array is each line in the
      // csv file
      // each word in each string array is separated by commas in each line in the csv
      // file
      return reader.readAll();
    }
  }

  /**
   * This method returns a list of categories of a particular difficulty
   *
   * @param difficulty the difficulty to retrieve the list of categories for
   * @return the list of categories
   */
  public List<String> getWordList(CategoryDifficulty difficulty) {
    return difficultyToCategories.get(difficulty);
  }

  /**
   * This method randomly retrieves a random word out of all 345 categories and returns it for the
   * user to play
   *
   * @return a random word
   * @throws IOException if an error occurs whilst reading the category file
   * @throws URISyntaxException if an error occurs whilst reading the category file
   * @throws CsvException if an error occurs whilst reading the category file
   */
  public String getRandomWord() throws IOException, URISyntaxException, CsvException {
    List<String[]> lines = getLines();
    ArrayList<String> words = new ArrayList<>();
    for (String[] line : lines) {
      words.add(line[0]);
    }
    // returns a random word
    return words.get(new Random().nextInt(words.size()));
  }

  /**
   * This method returns a random easy word. This should be called only if the user word difficulty
   * setting is EASY
   *
   * @return an easy word
   */
  public String getWordEasyDifficulty() {
    // get list of all words for easy difficulty
    List<String> easyWords = difficultyToCategories.get(CategoryDifficulty.E);
    return easyWords.get(new Random().nextInt(easyWords.size()));
  }

  /**
   * This method returns a random easy or medium word. This should be called only if the user word
   * difficulty setting is MEDIUM
   *
   * @return an easy or medium word
   */
  public String getWordMediumDifficulty() {
    // get list of all words for easy and medium difficulty
    List<String> easyMediumWords = difficultyToCategories.get(CategoryDifficulty.E);
    easyMediumWords.addAll(difficultyToCategories.get(CategoryDifficulty.M));

    return easyMediumWords.get(new Random().nextInt(easyMediumWords.size()));
  }

  /**
   * This method returns a random easy, medium or hard word. This should be called only if the user
   * word difficulty is HARD
   *
   * @return an easy, medium or hard word
   */
  public String getWordHardDifficulty() {
    // get list of all words for each difficulty
    List<String> easyMediumHardWords = difficultyToCategories.get(CategoryDifficulty.E);
    easyMediumHardWords.addAll(difficultyToCategories.get(CategoryDifficulty.M));
    easyMediumHardWords.addAll(difficultyToCategories.get(CategoryDifficulty.H));

    return easyMediumHardWords.get(new Random().nextInt(easyMediumHardWords.size()));
  }

  /**
   * This method returns a random hard word. This should be called only if the user word difficulty
   * is MASTER
   *
   * @return a hard word
   */
  public String getWordMasterDifficulty() {
    // get list of all words for hard difficulty
    List<String> hardWords = difficultyToCategories.get(CategoryDifficulty.H);
    return hardWords.get(new Random().nextInt(hardWords.size()));
  }
}
