package nz.ac.auckland.se206;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UsersManager {

  // hashmap will map each username to a User object
  private static HashMap<String, User> usersMap;
  private static User userSelected;
  private static List<User> userList;
  private static String[] userArray;
  private static Integer[] userTime;

  /**
   * Returns a specific user reference based on the passed in username
   *
   * @param username a String representing the user
   * @return a user reference that is linked to the passed in String
   */
  public static User getUser(String username) {
    return usersMap.get(username);
  }

  /**
   * This method gets the user that is currently logged in
   *
   * @return the user currently logged in
   */
  public static User getSelectedUser() {
    return userSelected;
  }

  /**
   * This method sets the user that is currently logged in
   *
   * @param username a String that maps to the user currently logged in
   */
  public static void setSelectedUser(String username) {
    userSelected = usersMap.get(username);
  }

  /**
   * This method returns a hash map that contains all our user account data where each username maps
   * to their account details
   *
   * @return a map containing all our user information
   */
  public static Map<String, User> getUsersMap() {
    return usersMap;
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
   * This method returns the number of users created
   *
   * @return the number of users
   */
  public static int getuserLength() {
    return userList.size();
  }

  /**
   * This method returns an array of all usernames
   *
   * @return an array containing all the currently registered usernames
   */
  public static String[] getUserArray() {
    return userArray;
  }

  /**
   * This method returns an array of all fastest times
   *
   * @return an array containing all fastest times from users
   */
  public static Integer[] getUserTIme() {
    return userTime;
  }

  /**
   * This method creates and loads a user into the hashmap and saves the user to the json file
   *
   * @param user the User object to create
   */
  public static void createUser(User user) {
    usersMap.put(user.getUsername(), user);
    try {
      saveUsersToJson();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method will save the hashmap to a json map representation. Saves to a .json file
   *
   * @throws IOException if an IOException is thrown
   */
  public static void saveUsersToJson() throws IOException {
    // check if .profiles exists. If not, make it

    // check if userData.json exists within .profiles folder. If not, make it.
    File dataFile = new File(".profiles/userData.json");
    if (!dataFile.exists()) {
      dataFile.createNewFile();
    }
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileWriter dataWriter = new FileWriter(dataFile);

    gson.toJson(usersMap, new TypeToken<Map<String, User>>() {}.getType(), dataWriter);
    dataWriter.close();
  }

  /**
   * Load users from Json file, making use of GSON. Will put the users to a hashmap
   *
   * @throws IOException if an IOException is thrown
   */
  public static void loadUsersFromJson() throws IOException {
    // check if .profiles exists. If not, make it
    File directory = new File(".profiles");
    File directory2 = new File(".profiles/profilePictures");
    if (!directory.exists()) {
      directory.mkdirs();
      directory2.mkdirs();
    }

    // check if userData.json exists within .profiles folder. If not, make it.
    File dataFile = new File(".profiles/userData.json");
    if (!dataFile.exists()) {
      dataFile.createNewFile();
    }
    Reader reader = new BufferedReader(new FileReader(dataFile));
    Gson gson = new Gson();

    // retrieve the json map and convert to HashMap<String, User>
    usersMap = gson.fromJson(reader, new TypeToken<HashMap<String, User>>() {}.getType());
    userList = new ArrayList<>();
    // if data file is empty, usersMap should be initialised
    if (usersMap == null) {
      usersMap = new HashMap<String, User>();
    }
    for (User user : usersMap.values()) {
      user.loadBadgeList();
    }
  }

  /**
   * canLogIn checks if the user has entered the correct user details of an existing user
   *
   * @param username username given by user
   * @return true if a there is a user with the username
   */
  public static boolean canLogIn(String username) {
    User user = usersMap.get(username);
    // checks if valid user details have been added
    return user != null;
  }

  /**
   * This method deletes a user for a given username
   *
   * @param username the user to delete
   */
  public static void deleteUser(String username) {
    File file = new File(usersMap.get(username).getProfilePic());
    file.delete();
    userList.remove(usersMap.get(username));
    usersMap.remove(username);
  }

  /**
   * This method sorts the users in terms of fastest time first
   *
   * @param start the first index
   * @param end the last index
   */
  public static void mergeSort(int start, int end) {
    int median;
    // Checks to see if there is only one user
    // recursively call merge sort to sort the users
    if (start < end) {
      median = (end + start) / 2;
      mergeSort(start, median);
      mergeSort(median + 1, end);
      merge(start, median + 1, end);
    }
  }

  /**
   * This method sorts the users using merge sort algorithm
   *
   * @param left the leftmost index
   * @param middle the middle index
   * @param right the rigthmost index
   */
  private static void merge(int left, int middle, int right) {
    // Intialising all lists for sorting
    String[] copyUserArray = userArray.clone();
    Integer[] copyUserTime = userTime.clone();
    int i = left, j = middle, k = left;
    // while loop checking the top half of the array
    while (i <= middle - 1 && j <= right) {
      // checking if elements need to be swapped
      if (userTime[i] <= userTime[j]) {
        copyUserTime[k] = userTime[i];
        copyUserArray[k] = userArray[i];
        i++;
      } else {
        copyUserTime[k] = userTime[j];
        copyUserArray[k] = userArray[j];
        j++;
      }
      k++;
    }
    // The next while loops are used to sort the remainder of the array that has not
    // been sorted
    while (i <= middle - 1) {
      copyUserTime[k] = userTime[i];
      copyUserArray[k] = userArray[i];
      i++;
      k++;
    }
    while (j <= right) {
      copyUserTime[k] = userTime[j];
      copyUserArray[k] = userArray[j];
      j++;
      k++;
    }
    // Returning the sorted arrays
    userTime = copyUserTime;
    userArray = copyUserArray;
  }

  /** This method sets up the arrays needed to be sorted for determining the leader board */
  public static void resetArray() {
    userList.clear();
    // for loop adding users that have played a game
    for (User user : usersMap.values()) {
      if (user.getFastestWin() != 60) {
        userList.add(user);
      }
    }
    userArray = new String[userList.size()];
    userTime = new Integer[userList.size()];
    // Copying user info into the arrays
    for (int a = 0; a < userList.size(); a++) {
      userTime[a] = userList.get(a).getFastestWin();
      userArray[a] = userList.get(a).getUsername();
    }
  }
}
