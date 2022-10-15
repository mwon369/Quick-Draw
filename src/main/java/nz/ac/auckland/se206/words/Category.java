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

  /**
   * This method gets the word
   *
   * @return the word to get
   */
  public String getWord() {
    return this.word;
  }

  /**
   * This method gets the definition of the word
   *
   * @return the definition
   */
  public String getDefinition() {
    return this.definition;
  }

  /**
   * This method gets the list of hints for the word
   *
   * @return the list of hints
   */
  public ArrayList<String> getHints() {
    return this.hints;
  }
}
