package nz.ac.auckland.se206;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import nz.ac.auckland.se206.DifficultyManager.Difficulty;
import nz.ac.auckland.se206.badges.Badge;
import nz.ac.auckland.se206.badges.BadgeData;
import nz.ac.auckland.se206.badges.BadgeFactory;
import nz.ac.auckland.se206.words.CategorySelector;
import nz.ac.auckland.se206.words.CategorySelector.CategoryDifficulty;

public class User {

  private String username; // first column

  private String password; // second column

  private int wins; // third column

  private int losses; // fourth column

  private int fastestWin; // fifth column

  private int winStreak; // sixth column

  private ArrayList<BadgeData> badgeDataList;
  private Map<CategoryDifficulty, ArrayList<String>> wordsGiven;
  private Map<CategoryDifficulty, ArrayList<String>> wordList;
  // private ArrayList<String> wordsGiven;
  // private ArrayList<String> wordList;
  private transient CategorySelector selector; // transient means not saved to json file

  private ArrayList<String> lastThreeWords;

  // difficulty settings
  private Difficulty accuracyDifficulty;
  private Difficulty timeLimitDifficulty;
  private Difficulty confidenceDifficulty;
  private Difficulty wordDifficulty;

  private transient ArrayList<Badge> badgeList;

  public User(String username, String password) {
    // assign the fields for each new user created
    this.username = username;
    this.password = password;
    this.fastestWin = 60;
    this.lastThreeWords = new ArrayList<String>();
    this.badgeList = new ArrayList<>();
    this.badgeDataList = new ArrayList<>();

    for (int i = 1; i < 11; i++) {
      badgeList.add(BadgeFactory.createBadge(i, false));
      badgeDataList.add(new BadgeData(i, false));
    }

    wordsGiven = new HashMap<CategoryDifficulty, ArrayList<String>>();
    wordsGiven.put(CategoryDifficulty.E, new ArrayList<String>());
    wordsGiven.put(CategoryDifficulty.M, new ArrayList<String>());
    wordsGiven.put(CategoryDifficulty.H, new ArrayList<String>());

    try {
      selector = new CategorySelector();
    } catch (IOException | CsvException | URISyntaxException e) {
      e.printStackTrace();
    }

    // initialise map of category difficulty to its list of words
    wordList = new HashMap<CategoryDifficulty, ArrayList<String>>();
    wordList.put(
        CategoryDifficulty.E, (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.E));
    wordList.put(
        CategoryDifficulty.M, (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.M));
    wordList.put(
        CategoryDifficulty.H, (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.H));

    // initialise default difficulties
    accuracyDifficulty = Difficulty.EASY;
    timeLimitDifficulty = Difficulty.EASY;
    confidenceDifficulty = Difficulty.EASY;
    wordDifficulty = Difficulty.EASY;
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

  public int getWinStreak() {
    return this.winStreak;
  }

  public double getWinRatio() {
    int totalGames = wins + losses;
    return (double) wins / totalGames * 100;
  }

  public ArrayList<Badge> getBadgeList() {
    return badgeList;
  }

  public void setWins(int numWins) {
    this.wins = numWins;
  }

  public void setLosses(int numLosses) {
    this.losses = numLosses;
  }

  public void setWinStreak(int streak) {
    this.winStreak = streak;
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

  public Difficulty getTimeLimitDifficulty() {
    return this.timeLimitDifficulty;
  }

  public void setTimeLimitDifficulty(Difficulty difficulty) {
    this.timeLimitDifficulty = difficulty;
  }

  public Difficulty getConfidenceDifficulty() {
    return this.confidenceDifficulty;
  }

  public void setConfidenceDifficulty(Difficulty difficulty) {
    this.confidenceDifficulty = difficulty;
  }

  public Difficulty getWordDifficulty() {
    return this.wordDifficulty;
  }

  public void setWordDifficulty(Difficulty difficulty) {
    this.wordDifficulty = difficulty;
  }

  public Map<CategoryDifficulty, ArrayList<String>> getWordList() {
    return this.wordList;
  }

  /**
   * updateWordList saves the new word given to the user and updates what words can be given to the
   * user for a new game
   *
   * @param word word given to user for the round
   */
  public void updateWordList(CategoryDifficulty difficulty, String word) {
    wordList.get(difficulty).remove(word);
    wordsGiven.get(difficulty).add(word);
  }

  public ArrayList<String> getWordsGiven(CategoryDifficulty difficulty) {
    return wordsGiven.get(difficulty);
  }

  /**
   * giveWordToDraw returns a word for the user to draw, which they have not drawn before
   *
   * @return word for user to draw
   */
  public String giveWordToDraw(Difficulty difficulty) {
    switch (difficulty) {
      case EASY:
        // resets wordList if user has drawn all easy words
        if (wordList.get(CategoryDifficulty.E).isEmpty()) {
          wordList.put(
              CategoryDifficulty.E,
              (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.E));
          // resetting words given to user once all words have been given
          wordsGiven.get(CategoryDifficulty.E).clear();
        }
        return wordList
            .get(CategoryDifficulty.E)
            .get(new Random().nextInt(wordList.get(CategoryDifficulty.E).size()));
      case MEDIUM:
        // retrieve an arraylist of all easy and medium
        ArrayList<String> easyMediumCategories = new ArrayList<String>();
        easyMediumCategories.addAll(wordList.get(CategoryDifficulty.E));
        easyMediumCategories.addAll(wordList.get(CategoryDifficulty.M));
        if (easyMediumCategories.isEmpty()) {
          // resets all wordlists if user has drawn all easy and medium words
          for (CategoryDifficulty categoryDifficulty :
              Arrays.asList(CategoryDifficulty.E, CategoryDifficulty.M)) {
            wordList.put(
                categoryDifficulty,
                (ArrayList<String>) this.selector.getWordList(categoryDifficulty));
            // clear words given
            wordsGiven.get(categoryDifficulty).clear();
            // add to all categories
            easyMediumCategories.addAll(wordList.get(categoryDifficulty));
          }
        }
        return easyMediumCategories.get(
            new Random()
                .nextInt(
                    wordList.get(CategoryDifficulty.E).size()
                        + wordList.get(CategoryDifficulty.M).size()));
      case HARD:
        // retrieve an arraylist of all words
        ArrayList<String> allCategories = new ArrayList<String>();
        allCategories.addAll(wordList.get(CategoryDifficulty.E));
        allCategories.addAll(wordList.get(CategoryDifficulty.M));
        allCategories.addAll(wordList.get(CategoryDifficulty.H));
        if (allCategories.isEmpty()) {
          // resets all wordlists if user has drawn all words
          for (CategoryDifficulty categoryDifficulty : CategoryDifficulty.values()) {
            wordList.put(
                categoryDifficulty,
                (ArrayList<String>) this.selector.getWordList(categoryDifficulty));
            // clear words given
            wordsGiven.get(categoryDifficulty).clear();
            // add to all categories
            allCategories.addAll(wordList.get(categoryDifficulty));
          }
        }
        return allCategories.get(
            new Random()
                .nextInt(
                    wordList.get(CategoryDifficulty.E).size()
                        + wordList.get(CategoryDifficulty.M).size()
                        + wordList.get(CategoryDifficulty.H).size()));
      case MASTER:
        // resets wordList if user has drawn all hard words
        if (wordList.get(CategoryDifficulty.H).isEmpty()) {
          wordList.put(
              CategoryDifficulty.H,
              (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.H));
          // resetting words given to user once all hard words have been given
          wordsGiven.get(CategoryDifficulty.H).clear();
        }
        return wordList
            .get(CategoryDifficulty.H)
            .get(new Random().nextInt(wordList.get(CategoryDifficulty.H).size()));
      default:
        // resets wordList if user has drawn all easy words
        if (wordList.get(CategoryDifficulty.E).isEmpty()) {
          wordList.put(
              CategoryDifficulty.E,
              (ArrayList<String>) this.selector.getWordList(CategoryDifficulty.E));
          // resetting words given to user once all easy words have been given
          wordsGiven.get(CategoryDifficulty.E).clear();
        }
        return wordList
            .get(CategoryDifficulty.E)
            .get(new Random().nextInt(wordList.get(CategoryDifficulty.E).size()));
    }
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

  public void loadBadgeList() {
    badgeList = new ArrayList<>();
    for (BadgeData data : badgeDataList) {
      badgeList.add(BadgeFactory.createBadge(data.getBadgeID(), data.isCompleted()));
    }
  }

  public void updateBadges() {
    for (int i = 0; i < badgeList.size(); i++) {
      if (!badgeList.get(i).isCompleted()) {
        badgeList.get(i).checkCompletion(this);
        badgeDataList.get(i).setCompleted(badgeList.get(i).isCompleted());
      }
    }
  }
}
