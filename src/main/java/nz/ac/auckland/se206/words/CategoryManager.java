package nz.ac.auckland.se206.words;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryManager {

  private static HashMap<String, Category> categoryMap;

  // map used to convert our CSV file of categories to JSON
  private static LinkedHashMap<String, Category> categoryLinkedMap;

  /**
   * This method converts the entire list of categories from CSV to JSON. This is done to help speed
   * up the custom definition writing process.
   *
   * @throws IOException
   */
  public static void saveCategoriesToJson() throws IOException {
    // load all categories from CSV into our categoryLinkedMap
    try {
      loadAllCategories();
    } catch (URISyntaxException | CsvException e) {
      throw new RuntimeException(e);
    }

    // check if .profiles exists. If not, make it
    File directory = new File(".profiles");
    if (!directory.exists()) {
      directory.mkdirs();
    }

    // check if categoryList.json exists within .profiles folder. If not, make it.
    File dataFile = new File(".profiles/categoryList.json");
    if (!dataFile.exists()) {
      dataFile.createNewFile();
    }
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    FileWriter dataWriter = new FileWriter(dataFile);

    // write the categoryLinkedMap to JSON
    gson.toJson(categoryLinkedMap, new TypeToken<Map<String, Category>>() {}.getType(), dataWriter);
    dataWriter.close();
  }

  /**
   * This is a helper method which loads in all the categories from the CSV file and writes each
   * category into a LinkedHashMap (so that the insertion order is maintained)
   *
   * @throws IOException
   * @throws URISyntaxException
   * @throws CsvException
   */
  private static void loadAllCategories() throws IOException, URISyntaxException, CsvException {
    // initialize variables required to load categories
    categoryLinkedMap = new LinkedHashMap<>();
    CategorySelector categorySelector = new CategorySelector();

    // load all categories and then map each category to a corresponding object
    List<String[]> categoryList = categorySelector.getLines();
    for (String[] category : categoryList) {
      categoryLinkedMap.put(
          category[0], new Category(category[0], "fill this later", new ArrayList<>()));
    }
  }

  public static void loadCategoryInfoFromJson() throws IOException {

    // instantiate File object by going to directory with categoryInfo.json and reading it in
    File dataFile = new File("src/main/resources/categoryInfo.json");
    Reader reader = new BufferedReader(new FileReader(dataFile));
    Gson gson = new Gson();

    // retrieve the json map and convert to HashMap<String, Category>
    categoryMap = gson.fromJson(reader, new TypeToken<HashMap<String, Category>>() {}.getType());

    // if data file is empty, usersMap should be initialised
    if (categoryMap == null) {
      categoryMap = new HashMap<>();
    }

    // test code
    System.out.println(categoryMap.size());
    System.out.println(categoryMap.get("zigzag").getDefinition());
    System.out.println(categoryMap.get("zigzag").getHints().get(0));
  }

  public static Category getCategory(String category) {
    return categoryMap.get(category);
  }
}
