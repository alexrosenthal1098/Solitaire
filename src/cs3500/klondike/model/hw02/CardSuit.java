package cs3500.klondike.model.hw02;

/**
 * Possible card suits.
 */
public enum CardSuit {
  HEARTS("♡"),
  SPADES("♠"),
  DIAMONDS("♢"),
  CLUBS("♣");

  private final String suit;

  /**
   * Constructor for CardSuit.
   * @param suit A string representing the suit.
   */
  CardSuit(String suit) {
    this.suit = suit;
  }

  /**
   * Represent this card value as a string.
   * @return The card value as a string.
   */
  public String toString() {
    return this.suit;
  }
}
