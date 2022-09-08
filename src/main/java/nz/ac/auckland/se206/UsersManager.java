package nz.ac.auckland.se206;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UsersManager {

  // hashmap will map each username to a User object
  private static HashMap<String, User> usersMap = new HashMap<String, User>();
  private static User userSelected;

  public static User getUser(String username) {
    return usersMap.get(username);
  }

  public static User getSelectedUser() {
    return userSelected;
  }

  public static void setSelectedUser(String username) {
    userSelected = usersMap.get(username);
  }

  /**
   * This method returns a set of all usernames
   *
   * @return a set containing all the currently registered usernames
   */
  public static Set<String> getAllUsernames() {
    return usersMap.keySet();
  }

  /**
   * This method creates a loads a user into the hashmap and saves the user to the fil
   *
   * @param user the User object to create
   */
  public static void createUser(User user) {
    loadUser(user);

    try {
      saveUsers();
      loadWordList();
    } catch (URISyntaxException | IOException | CsvException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method saves all users' details to a csv file
   *
   * @throws URISyntaxException
   * @throws IOException
   */
  public static void saveUsers() throws URISyntaxException, IOException {
    File fileName = new File("src/main/resources/users.csv");
    CSVWriter writer = new CSVWriter(new FileWriter(fileName));
    // first, write out the headers
    String[] headers = {"Username", "Password", "Wins", "Losses", "Fastest Time"};
    writer.writeNext(headers);
    // write each user detail on a new row
    for (User user : usersMap.values()) {
      String[] details = new String[5];
      details[0] = user.getUsername();
      details[1] = user.getPassword();
      details[2] = String.valueOf(user.getWins());
      details[3] = String.valueOf(user.getLosses());
      details[4] = String.valueOf(user.getFastestWin());
      writer.writeNext(details);
    }
    writer.close();
    saveWordsGiven();
  }

  /**
   * This method puts a user onto the hashMap of users
   *
   * @param user the user of User type to put into the hashMap
   */
  public static void loadUser(User user) {
    usersMap.put(user.getUsername(), user);
  }

  /**
   * This method loads all users from the user details CSV file and puts each user into the hashMap
   */
  public static void loadUsersFromCSV() {
    try {
      for (String[] line : getLines()) {
        // create a new user - username and password should always exist in the csv file
        // (mandatory
        // fields)
        User user = new User(line[0], line[1]);
        // try to add details if available
        try {
          user.setWins(Integer.valueOf(line[2]));
          user.setLosses(Integer.valueOf(line[3]));
          user.setFastestWin(Integer.valueOf(line[4]));
          UsersManager.loadUser(user);
        } catch (ArrayIndexOutOfBoundsException e) {
          // if no more details, add user and try next user
          UsersManager.loadUser(user);
          UsersManager.loadWordList();
          continue;
        }
        UsersManager.loadWordList();
      }
    } catch (NumberFormatException | IOException | CsvException | URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * This method reads the CSV file for user details and returns a list of the user details
   *
   * @return A list of String[] where each String[] represents a user and their details
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  private static List<String[]> getLines() throws IOException, CsvException, URISyntaxException {
    File fileName = new File("src/main/resources/users.csv");
    // if file does not exist, create one and put headers
    if (!fileName.isFile()) {
      fileName.createNewFile();
      saveUsers();
    }
    try (FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      // returns a list of string array where each string array is each line in the
      // csv file
      // each word in each string array is separated by commas in each line in the csv
      // file
      List<String[]> data = reader.readAll();
      // need to remove the headers
      data.remove(0);
      return data;
    }
  }

  /**
   * canLogIn checks if the user has entered the correct user details of an existing user
   *
   * @param username username given by user
   * @param password password given by user
   * @return true if a user with the correct username and password is entered. False otherwise
   */
  public static boolean canLogIn(String username, String password) {
    User user = usersMap.get(username);
    // checks if valid user details have been added
    if (user == null || !user.getPassword().equals(password)) {
      return false;
    }
    return true;
  }

  /**
   * saveWordsGiven method saves the words encountered by each user into words_given.csv file
   *
   * @throws IOException
   */
  private static void saveWordsGiven() throws IOException {
    File fileName = new File("src/main/resources/words_given.csv");
    CSVWriter writer = new CSVWriter(new FileWriter(fileName));
    // for loop to save each user's words encountered per line, with the first
    // element being the username of the user
    for (User user : usersMap.values()) {
      // create an array to write into csv file
      String[] words = new String[user.getWordsGiven().size()];
      words = user.getWordsGiven().toArray(words);
      writer.writeNext(words);
    }
    writer.close();
  }

  /**
   * getWordList method loads all words encountered by each user into a list of string arrays. Each
   * element of the list contains an array of string of words a specific user has encountered
   *
   * @return a list of string arrays containing the words encountered by each user
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  private static List<String[]> getWordList() throws IOException, CsvException, URISyntaxException {
    File fileName = new File("src/main/resources/words_given.csv");
    // if file does not exist, create one and put headers
    if (!fileName.isFile()) {
      fileName.createNewFile();
      saveWordsGiven();
    }
    try (FileReader fr = new FileReader(fileName, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(fr)) {
      // returns a list of string array where each string array is each line in the
      // csv file
      // each word in each string array is separated by commas in each line in the csv
      // file
      List<String[]> wordList = reader.readAll();
      return wordList;
    }
  }

  /**
   * loadWordList loads the wordlist of each user into their respective instance field wordsGiven
   *
   * @throws IOException
   * @throws CsvException
   * @throws URISyntaxException
   */
  private static void loadWordList() throws IOException, CsvException, URISyntaxException {
    for (String[] wordList : getWordList()) {
      User currentUser = usersMap.get(wordList[0]);
      currentUser.setWordsGiven(wordList);
      currentUser.setWordList();
    }
  }
}
