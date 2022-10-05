package nz.ac.auckland.se206.words;

import java.util.ArrayList;

public class Category {

  private String definition;
  private ArrayList<String> hints;

  public Category(String definition, ArrayList<String> hints) {
    this.definition = definition;
    this.hints = hints;
  }

  public String getDefinition() {
    return this.definition;
  }

  public ArrayList<String> getHints() {
    return this.hints;
  }
}
