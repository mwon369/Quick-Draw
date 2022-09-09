package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class User {

  private String username; // first column

  private String password; // second column

  private int wins; // third column

  private int losses; // fourth column

  private int fastestWin; // fifth column

  private ArrayList<String> wordsGiven;
  private ArrayList<String> wordList;
  private CategorySelector selector;

  public User(String username, String password) {
    // assign the fields for each new user created
    this.username = username;
    this.password = password;
    wordsGiven = new ArrayList<String>();
    wordsGiven.add(username);
    try {
      selector = new CategorySelector();
    } catch (IOException | CsvException | URISyntaxException e) {
      e.printStackTrace();
    }
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

  public void setWins(int numWins) {
    this.wins = numWins;
  }

  public void setLosses(int numLosses) {
    this.losses = numLosses;
  }

  public void setFastestWin(int fastestWin) {
    this.fastestWin = fastestWin;
  }

  /** setWordList method fills the ArrayList wordList with words the user has not been given */
  public void setWordList() {
    wordList = (ArrayList<String>) this.selector.getWordList(Difficulty.E);
    // Checks if there are words given to the user
    if (wordsGiven.size() != 1) {
      wordList.removeAll(wordsGiven);
    }
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

  public void setWordsGiven(String[] words) {
    for (int i = 1; i < words.length; i++) {
      if (!words[i].equals("")) {
        wordsGiven.add(words[i]);
      }
    }
  }

  /**
   * giveWordToDraw returns a word for the user to draw, which they have not drawn before
   *
   * @return word for user to draw
   */
  public String giveWordToDraw() {
    // resets wordList if user has drawn all words
    if (wordList.isEmpty()) {
      wordList = (ArrayList<String>) selector.getWordList(Difficulty.E);
      // resetting words given to user once all words have been given
      wordsGiven.clear();
      wordsGiven.add(username);
    }
    return wordList.get(new Random().nextInt(wordList.size()));
  }
}
