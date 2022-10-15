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

  private String username;

  private int wins;
  private int losses;
  private int fastestWin;
  private int winStreak;
  private int rapidFireHighScore;
  private String profilePic;

  private ArrayList<BadgeData> badgeDataList;
  private Map<CategoryDifficulty, ArrayList<String>> wordsGiven;
  private Map<CategoryDifficulty, ArrayList<String>> wordList;
  private transient CategorySelector selector; // transient means not saved to json file

  private ArrayList<String> allPreviousWords;

  // difficulty settings
  private Difficulty accuracyDifficulty;
  private Difficulty timeLimitDifficulty;
  private Difficulty confidenceDifficulty;
  private Difficulty wordDifficulty;

  private transient ArrayList<Badge> badgeList;

  /**
   * This constructor method creates a user object that can be saved and loaded
   *
   * @param username a String representing the name of the user's account
   */
  public User(String username) {
    // assign the fields for each new user created
    this.username = username;
    this.fastestWin = 60;
    this.allPreviousWords = new ArrayList<String>();
    this.badgeList = new ArrayList<>();
    this.badgeDataList = new ArrayList<>();
    this.rapidFireHighScore = 0;

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

  /**
   * This method gets a specific user's username
   *
   * @return the users username
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * This method gets a specific users number of wins
   *
   * @return the users wins
   */
  public int getWins() {
    return this.wins;
  }

  /**
   * This method gets a specific users number of losses
   *
   * @return the users losses
   */
  public int getLosses() {
    return this.losses;
  }

  /**
   * This method gets a specific users fastest win time
   *
   * @return the users fastest win time
   */
  public int getFastestWin() {
    return this.fastestWin;
  }

  /**
   * This method gets a specific users highest win streak
   *
   * @return the users win streak
   */
  public int getWinStreak() {
    return this.winStreak;
  }

  /**
   * This method gets a specific users win rate
   *
   * @return the users win ratio
   */
  public double getWinRatio() {
    int totalGames = wins + losses;
    return (double) wins / totalGames * 100;
  }

  /**
   * This method gets the list of badges for a specific user
   *
   * @return the users badge list
   */
  public ArrayList<Badge> getBadgeList() {
    return badgeList;
  }

  /**
   * This method sets the number of wins for a specific user
   *
   * @param numWins the users wins
   */
  public void setWins(int numWins) {
    this.wins = numWins;
  }

  /**
   * This method sets the number of losses for a specific user
   *
   * @param numLosses the users losses
   */
  public void setLosses(int numLosses) {
    this.losses = numLosses;
  }

  /**
   * This method sets the win streak number for a specific user
   *
   * @param streak the users win streak
   */
  public void setWinStreak(int streak) {
    this.winStreak = streak;
  }

  /**
   * This method sets the fastest win time for a specific user
   *
   * @param fastestWin the fastest win time
   */
  public void setFastestWin(int fastestWin) {
    this.fastestWin = fastestWin;
  }

  /**
   * This method gets the accuracy difficulty for a specific user
   *
   * @return The accuracy difficulty setting
   */
  public Difficulty getAccuracyDifficulty() {
    return this.accuracyDifficulty;
  }

  /**
   * This method gets the time limit difficulty for a specific user
   *
   * @return The time limit difficulty setting
   */
  public Difficulty getTimeLimitDifficulty() {
    return this.timeLimitDifficulty;
  }

  /**
   * This method sets the accuracy difficulty for a specific user
   *
   * @param difficulty the difficulty setting
   */
  public void setAccuracyDifficulty(Difficulty difficulty) {
    this.accuracyDifficulty = difficulty;
  }

  /**
   * This method sets the username for a specific user
   *
   * @param userName the new username
   */
  public void setUserName(String userName) {
    this.username = userName;
  }

  /**
   * This method sets the time limit difficulty for a specific user
   *
   * @param difficulty the difficulty setting
   */
  public void setTimeLimitDifficulty(Difficulty difficulty) {
    this.timeLimitDifficulty = difficulty;
  }

  /**
   * This method gets the confidence difficulty for a specific user
   *
   * @return The confidence difficulty setting
   */
  public Difficulty getConfidenceDifficulty() {
    return this.confidenceDifficulty;
  }

  /**
   * This method gets the profile picture directory for a specific user
   *
   * @return the path directory to the user's profile picture as a string
   */
  public String getProfilePic() {
    return this.profilePic;
  }

  /**
   * This method sets the confidence difficulty for a specific user
   *
   * @param difficulty the difficulty setting
   */
  public void setConfidenceDifficulty(Difficulty difficulty) {
    this.confidenceDifficulty = difficulty;
  }

  /**
   * This method gets the word difficulty for a specific user
   *
   * @return The word difficulty setting
   */
  public Difficulty getWordDifficulty() {
    return this.wordDifficulty;
  }

  /**
   * This method gets the word difficulty for a specific user
   *
   * @param difficulty the difficulty setting
   */
  public void setWordDifficulty(Difficulty difficulty) {
    this.wordDifficulty = difficulty;
  }

  /**
   * This method sets the profile picture directory a specific user
   *
   * @param pic the String representing the directory to the user's profile picture
   */
  public void setProfilePic(String pic) {
    this.profilePic = pic;
  }

  /**
   * This method gets the entire word list for a specific user
   *
   * @return a map which contains lists of words associated with each difficulty
   */
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
    wordsGiven.get(difficulty).add(0, word);
  }

  /**
   * This method gets a difficulty filtered list of words that the user has been given
   *
   * @param difficulty the word difficulty
   * @return a list of words that belong to the passed in difficulty
   */
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
  public void updatePreviousWords(String category) {
    allPreviousWords.add(0, category);
  }

  /**
   * This method returns the entire list of words that the user has played without any consideration
   * for the difficulty of the words
   *
   * @return a list of words that the user has played
   */
  public ArrayList<String> getAllPreviousWords() {
    return this.allPreviousWords;
  }

  /**
   * This method loads and creates all badge instances for the user that has been saved in the
   * BadgedataList
   */
  public void loadBadgeList() {
    badgeList = new ArrayList<>();
    for (BadgeData data : badgeDataList) {
      badgeList.add(BadgeFactory.createBadge(data.getBadgeId(), data.getCompleted()));
    }
  }

  /** This method checks to see if the user has earned any new badges after completing a game */
  public void updateBadges() {
    for (int i = 0; i < badgeList.size(); i++) {
      // Checks if any unearned badges have be achieved
      if (!badgeList.get(i).isCompleted()) {
        badgeList.get(i).checkCompletion(this);
        badgeDataList.get(i).setCompleted(badgeList.get(i).isCompleted());
      }
    }
  }

  public int getRapidFireHighScore() {
    return this.rapidFireHighScore;
  }

  public void setRapidFireHighScore(int newHighScore) {
    this.rapidFireHighScore = newHighScore;
  }
}
