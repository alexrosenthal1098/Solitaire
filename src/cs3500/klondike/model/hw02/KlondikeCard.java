package cs3500.klondike.model.hw02;

import java.util.Objects;

/**
 * A card used for KlondikeSolitaire.
 */
public class KlondikeCard implements Card {
  private final CardValue value;
  private final CardSuit suit;

  /**
   * Constructor for KlondikeCard.
   * @param value This card's value.
   * @param suit This card's suit.
   * @throws NullPointerException if either value or suit are null.
   */
  public KlondikeCard(CardValue value, CardSuit suit) {
    this.value = Objects.requireNonNull(value);
    this.suit = Objects.requireNonNull(suit);
  }

  @Override
  public String toString() {
    return this.value + this.suit.toString();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof KlondikeCard)) {
      return false;
    }
    KlondikeCard that = (KlondikeCard) other;
    return (this.value == that.value) && (this.suit == that.suit);
  }

  @Override
  public int hashCode() {
    return this.value.hashCode() + this.suit.hashCode();
  }

  /**
   * Returns the value of the given card. Aces are worth 1,
   * numbered cards are worth their number, jacks worth 11, queens 12, and kings 13.
   * @return The card value as an integer.
   */
  public static int getValue(Card card) {
    // since 10's value is 2 characters long we can't use valueString
    if (card.toString().contains("10")) {
      return 10;
    }

    // get the character that represents the cards value
    String valueString = Objects.requireNonNull(card).toString().substring(0, 1);

    switch (valueString) { // check for cards with a letter as a value (aces and face cards)
      case "A":
        return 1;
      case "J":
        return 11;
      case "Q":
        return 12;
      case "K":
        return 13;
      default:
        break;
    }

    try {
      return Integer.parseInt(valueString); // try to parse the value as an integer
    }
    catch (Exception e) { // if none of these worked, the given card is an invalid for klondike
      throw new IllegalArgumentException("Given card is invalid for klondike solitaire.");
    }
  }

  /**
   * Returns the suit of the given card.
   * @return The card suit as a String.
   */
  public static String getSuit(Card card) {
    Objects.requireNonNull(card);

    if (KlondikeCard.getValue(card) == 10) { // if the value is 10 than the suit string is 3rd char
      return card.toString().substring(2, 3);
    }
    else {
      return card.toString().substring(1, 2); // if the value isn't ten then get 2nd char
    }
  }
}
