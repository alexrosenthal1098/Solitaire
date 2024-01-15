package cs3500.klondike.model.hw02;

/**
 * Possible card values.
 */
public enum CardValue {
  ACE("A"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7"),
  EIGHT("8"),
  NINE("9"),
  TEN("10"),
  JACK("J"),
  QUEEN("Q"),
  KING("K");

  private final String valueString;

  /**
   * Constructor for CardValue.
   * @param valueString A string representing the value.
   */
  CardValue(String valueString) {
    this.valueString = valueString;
  }

  /**
   * Represent this card value as a string.
   * @return The card value as a string.
   */
  public String toString() {
    return valueString;
  }
}
