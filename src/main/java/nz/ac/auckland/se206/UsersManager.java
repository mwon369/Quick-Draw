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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UsersManager {

  // hashmap will map each username to a User object
  private static HashMap<String, User> usersMap;
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
   * @throws IOException
   */
  public static void saveUsersToJson() throws IOException {
    // check if .profiles exists. If not, make it
    File directory = new File(".profiles");
    if (!directory.exists()) {
      directory.mkdirs();
    }

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
   * @throws IOException
   */
  public static void loadUsersFromJson() throws IOException {
    // check if .profiles exists. If not, make it
    File directory = new File(".profiles");
    if (!directory.exists()) {
      directory.mkdirs();
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
}
