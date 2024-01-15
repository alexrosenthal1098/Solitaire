package cs3500.klondike.view;

import java.io.IOException;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A simple text-based rendering of the Klondike game.
 */
public class KlondikeTextualView implements TextualView {
  private final KlondikeModel model;
  private final Appendable out;

  /**
   * A constructor that takes in a model to use for the view.
   * @param model The model that this view displays.
   */
  public KlondikeTextualView(KlondikeModel model) {
    this.model = model;
    this.out = System.out;
  }

  /**
   * A constructor that takes in an appendable and a model.
   * @param model The model that is used to create a textual view.
   * @param out The output location to append to.
   */
  public KlondikeTextualView(KlondikeModel model, Appendable out) {
    this.model = model;
    this.out = out;
  }

  @Override
  public void render() throws IOException {
    this.out.append(this.toString()).append("\n");
  }

  @Override
  public String toString() {
    return this.drawToString() + "\n" + this.foundationToString() + "\n" + this.cascadeToString();
  }

  // Creates and returns a textual view of the draw cards.
  String drawToString() {
    List<Card> drawCards = this.model.getDrawCards(); // get the currently available draw cards
    if (drawCards.isEmpty()) { // if there are none, display an empty draw pile
      return "Draw: ";
    }

    StringBuilder drawString = new StringBuilder("Draw:"); // create a StringBuilder
    for (Card card : drawCards) { // iterate through the available draw cards
      // add card.toString() with a space and comma for formatting
      drawString.append(" ").append(card.toString()).append(",");
    }

    // return the string without the last extra comma
    return drawString.substring(0, drawString.length() - 1);
  }

  // Creates and returns a textual view of the foundation piles.
  String foundationToString() {
    StringBuilder foundationString = new StringBuilder("Foundation:"); // create a StringBuilder
    // iterate through the foundation pile cards
    for (int i = 0; i < this.model.getNumFoundations(); i++) {
      Card card = this.model.getCardAt(i); // get card at the current foundation pile
      if (card == null) { // if there is no card in that pile, add "<none>"
        foundationString.append(" <none>,");
      }
      else { // if there is a card, add its toString() with formatting
        foundationString.append(" ").append(card.toString()).append(",");
      }
    }

    // return the string without the last extra comma
    return foundationString.substring(0, foundationString.length() - 1);
  }

  // Creates and returns a textual view of the cascade cards.
  String cascadeToString() {
    int rows = this.model.getNumRows();
    int piles = this.model.getNumPiles();

    String[] lines = new String[rows]; // create array of strings that represent each line
    for (int lineNum = 0; lineNum < rows; lineNum++) { // iterate through each line (row)
      StringBuilder line = new StringBuilder(); // create a StringBuilder for each line
      for (int pileNum = 0; pileNum < piles; pileNum++) { // iterate through pile (column)
        int height = this.model.getPileHeight(pileNum); // get pile height for current pile

        if (height == 0 && lineNum == 0) {
          line.append("  X"); // if the pile height and line number are 0, add "X"
        }
        // if the pile height is less than the line number, add empty space
        else if (height < (lineNum + 1)) {
          line.append("   ");
        }
        // if the card at this row and pile is visible, add its toString
        else if (this.model.isCardVisible(pileNum, lineNum)) {
          line.append(String.format("%3s", this.model.getCardAt(pileNum, lineNum).toString()));
        }
        else { // if the card is not visible, add a question mark
          line.append("  ?");
        }
      }
      lines[lineNum] = String.valueOf(line); // add the line as a string to the array of lines
    }
    return String.join("\n", lines); // join the lines with newline character and return
  }
}
