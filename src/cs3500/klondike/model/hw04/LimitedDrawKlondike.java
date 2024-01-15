package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import java.util.HashMap;

/**
 * A model that represents the limited draw version of the card game Klondike Solitaire.
 */
public class LimitedDrawKlondike extends BasicKlondike {
  private final int numRedraws;
  private final HashMap<Card, Integer> redraws;

  /**
   * A constructor that takes in a number of redraws allowed.
   * @param numRedraws The number of times a card can be redrawn in this game.
   */
  public LimitedDrawKlondike(int numRedraws) {
    super();
    if (numRedraws < 0) {
      throw new IllegalArgumentException("Number of redraws cannot be negative.");
    }
    this.numRedraws = numRedraws;
    this.redraws = new HashMap<>();
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    if (this.getDrawCards().isEmpty()) { // check if the draw pile is empty
      throw new IllegalStateException("No cards left in the draw pile.");
    }

    Card drawCard = this.draws.get(0); // get the current draw card
    int cardRedraws = this.redraws.getOrDefault(drawCard, 0); // get its redraw amount
    // if the number of times this card has been redrawn is less than the max amount
    if (cardRedraws < this.numRedraws) {
      this.redraws.put(drawCard, cardRedraws + 1); // increase the redraw amount by 1 in the hashmap
      // remove the card from the top of the draw pile and add it to the bottom (end of the list)
      this.draws.add(this.draws.remove(0));
    }
    else { // if the card has been redrawn the max amount of times
      this.draws.remove(0); // remove it from the draw pile completely
    }
  }
}
