package nz.ac.auckland.se206.words;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

public class CategoryManager {

  private static HashMap<String, Category> categoryMap;

  public static void loadCategoryInfoFromJson() throws IOException {

    // instantiate File object by going to directory with categoryInfo.json and reading it in
    File dataFile =
        new File(String.valueOf(CategoryManager.class.getResource("categoryInfo.json")));
    Reader reader = new BufferedReader(new FileReader(dataFile));
    Gson gson = new Gson();

    // retrieve the json map and convert to HashMap<String, Category>
    categoryMap = gson.fromJson(reader, new TypeToken<HashMap<String, Category>>() {}.getType());

    // if data file is empty, usersMap should be initialised
    if (categoryMap == null) {
      categoryMap = new HashMap<>();
    }
  }

  public static Category getCategory(String word) {
    return categoryMap.get(word);
  }
}
