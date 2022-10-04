package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import nz.ac.auckland.se206.DifficultyManager.Difficulty;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.CategoryDifficulty;

public class User {

  private String username; // first column

  private String password; // second column

  private int wins; // third column

  private int losses; // fourth column

  private int fastestWin; // fifth column

  private ArrayList<String> wordsGiven;
  private ArrayList<String> wordList;
  private transient CategorySelector selector; // transient means not saved to json file

  private ArrayList<String> lastThreeWords;

  // difficulty settings
  private Difficulty accuracyDifficulty;

  public User(String username, String password) {
    // assign the fields for each new user created
    this.username = username;
    this.password = password;
    this.fastestWin = 60;
    this.lastThreeWords = new ArrayList<String>();

    wordsGiven = new ArrayList<String>();

    try {
      selector = new CategorySelector();
    } catch (IOException | CsvException | URISyntaxException e) {
      e.printStackTrace();
    }
    wordList = (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.E);
    accuracyDifficulty = Difficulty.EASY;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public int getWins() {
    return this.wins;
  }

  public int getLosses() {
    return this.losses;
  }

  public int getFastestWin() {
    return this.fastestWin;
  }

  public double getWinRatio() {
    int totalGames = wins + losses;
    return (double) wins / totalGames * 100;
  }

  public void setWins(int numWins) {
    this.wins = numWins;
  }

  public void setLosses(int numLosses) {
    this.losses = numLosses;
  }

  public void setFastestWin(int fastestWin) {
    this.fastestWin = fastestWin;
  }

  public Difficulty getAccuracyDifficulty() {
    return this.accuracyDifficulty;
  }

  public void setAccuracyDifficulty(Difficulty difficulty) {
    this.accuracyDifficulty = difficulty;
  }

  /**
   * updateWordList saves the new word given to the user and updates what words can be given to the
   * user for a new game
   *
   * @param word word given to user for the round
   */
  public void updateWordList(String word) {
    wordList.remove(word);
    wordsGiven.add(word);
  }

  public ArrayList<String> getWordsGiven() {
    return wordsGiven;
  }

  /**
   * giveWordToDraw returns a word for the user to draw, which they have not drawn before
   *
   * @return word for user to draw
   */
  public String giveWordToDraw() {
    // resets wordList if user has drawn all words
    if (wordList.isEmpty()) {
      wordList = (ArrayList<String>) selector.getWordList(CategoryDifficulty.E);
      // resetting words given to user once all words have been given
      wordsGiven.clear();
    }
    return wordList.get(new Random().nextInt(wordList.size()));
  }

  /**
   * This method updates the last three words that the user has been given with a new additional
   * word
   *
   * @param category the new word to add in
   */
  public void updateLastThreeWords(String category) {
    lastThreeWords.add(0, category);
    if (lastThreeWords.size() > 3) {
      lastThreeWords.remove(lastThreeWords.size() - 1);
    }
  }

  public ArrayList<String> getLastThreeWords() {
    return this.lastThreeWords;
  }
}
