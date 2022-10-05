package nz.ac.auckland.se206.words;

import java.util.ArrayList;

public class Category {

  private String word;
  private String definition;
  private ArrayList<String> hints;

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
