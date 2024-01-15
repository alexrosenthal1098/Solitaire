package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.KlondikeCard;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;

import java.util.ArrayList;

/**
 * A model that represents the card game Whitehead Solitaire.
 */
public class WhiteheadKlondike extends BasicKlondike {

  public WhiteheadKlondike() {
    super();
  }

  @Override
  protected void dealCascades(ArrayList<Card> deck, int numPiles) {
    // to avoid duplicate code, deal the cascades the same was as BasicKlondike, and then
    // iterate through every pile card and change the visibility to true
    super.dealCascades(deck, numPiles);
    for (ArrayList<Card> pile : this.cascades) {
      for (Card card : pile) {
        this.visibility.put(card, true);
      }
    }
  }

  @Override
  protected void validatePileMove(ArrayList<Card> cardsToMove, int destPile) {
    this.validateBuild(cardsToMove); // validate that the cards to move are a valid build
    Card bottom = cardsToMove.get(0); // get the bottom card from the build

    String bottomSuit = KlondikeCard.getSuit(bottom); // get the suit of a card in the build
    boolean sameSuitBuild = cardsToMove.stream() // check if every card in the build has that suit
            .allMatch(c -> KlondikeCard.getSuit(c).equals(bottomSuit));

    if (!sameSuitBuild) { // if all the cards are not the same suit, then invalid move
      throw new IllegalStateException("All cards must have the same suit to move multiple cards.");
    }

    if (this.cascades[destPile].isEmpty()) { // if the destination pile is empty, valid move
      return;
    }

    Card top = this.cascades[destPile].get(this.cascades[destPile].size() - 1);
    boolean bottomBlack = bottom.toString().contains("♠") || bottom.toString().contains("♣");
    boolean topBlack = top.toString().contains("♠") || top.toString().contains("♣");
    int bottomValue = KlondikeCard.getValue(bottom);
    int topValue = KlondikeCard.getValue(top);
    // check that cards have the same color and that the card going on top
    // (the lower card if you think about textual view) has a value that's one less
    if (bottomBlack != topBlack || topValue != (bottomValue + 1)) {
      throw new IllegalStateException("This move is not logically possible.");
    }
  }

  // check if the given build is valid, i. e. the value of the card decreases by one
  // for every card you go down
  void validateBuild(ArrayList<Card> build) {
    int lastVal = KlondikeCard.getValue(build.get(0)); // get value of the top card in the build
    for (Card card : build.subList(1, build.size())) {
      if (lastVal - KlondikeCard.getValue(card) != 1) {
        throw new IllegalStateException("Cannot move an invalid build.");
      }
      lastVal = lastVal - 1;
    }
  }
}
