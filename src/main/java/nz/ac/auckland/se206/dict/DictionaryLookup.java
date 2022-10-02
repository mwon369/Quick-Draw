package nz.ac.auckland.se206.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DictionaryLookup {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  public static WordInfo searchWordInfo(String query) throws IOException, WordNotFoundException {

    // call the API
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + query).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();
    // parse API response to a string
    String jsonString = responseBody.string();

    // get title and message
    try {
      JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
      String title = jsonObj.getString("title");
      String subMessage = jsonObj.getString("message");
      throw new WordNotFoundException(query, title, subMessage);
    } catch (ClassCastException e) {
    }

    // parse API response into a JSON array
    JSONArray jArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    List<WordEntry> entries = new ArrayList<WordEntry>();

    // loop through the JSON array
    for (int e = 0; e < jArray.length(); e++) {
      JSONObject jsonEntryObj = jArray.getJSONObject(e);
      JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");

      // make variables to store returned data from API
      String partOfSpeech = "[not specified]";
      List<String> definitions = new ArrayList<String>();

      // assign all meanings for each word and assign to JSON object
      for (int m = 0; m < jsonMeanings.length(); m++) {
        JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);
        String pos = jsonMeaningObj.getString("partOfSpeech");

        if (!pos.isEmpty()) {
          partOfSpeech = pos;
        }

        // use the JSON object to get an array of definitions in JSON format
        JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");

        // loop through all the definition objects and append their string
        // type versions to our list of definitions that we will return
        for (int d = 0; d < jsonDefinitions.length(); d++) {
          JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);

          String definition = jsonDefinitionObj.getString("definition");
          if (!definition.isEmpty()) {
            definitions.add(definition);
          }
        }
      }

      // create wordEntry object
      WordEntry wordEntry = new WordEntry(partOfSpeech, definitions);
      entries.add(wordEntry);
    }

    return new WordInfo(query, entries);
  }
}
