package nz.ac.auckland.se206.words;

import java.util.ArrayList;

public class Category {

  private String word;
  private String definition;
  private ArrayList<String> hints;

  /**
   * This constructor method creates a category object which is used to represent the word that is
   * given to the user
   *
   * @param word the word itself
   * @param definition the definition of the word
   * @param hints a list of hints to help the user understand the definition
   */
  public Category(String word, String definition, ArrayList<String> hints) {
    this.word = word;
    this.definition = definition;
    this.hints = hints;
  }

  public String getWord() {
    return this.word;
  }

  public String getDefinition() {
    return this.definition;
  }

  public ArrayList<String> getHints() {
    return this.hints;
  }
}
