package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

import java.util.List;

/**
 * A class that holds tests for methods overridden for LimitedDrawKlondike.
 */
public class LimitedDrawKlondikeTest {
  KlondikeModel model;
  List<Card> deck;

  @Before
  public void setUp() throws Exception {
    this.model = new LimitedDrawKlondike(2);
    this.deck = model.getDeck();
  }

  // helper method
  void putCardAtIndex(String card, String suit, int idx) {
    for (int i = 0; i <= this.deck.size(); i++) {
      if (this.deck.get(i).toString().contains(card + suit)) {
        this.deck.set(i, this.deck.set(idx, this.deck.get(i)));
        break;
      }
    }
  }


  // tests for constructor exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNegativeRedraws() {
    this.model = new LimitedDrawKlondike(-1);
  }

  @Test
  public void testDiscardDraw2Redraws1Card() {
    this.model.startGame(this.deck.subList(0, 2), false, 1, 1);
    this.model.discardDraw(); // first redraw
    this.model.discardDraw(); // second redraw
    this.model.discardDraw(); // card is discarded and NOT redrawn
    Assert.assertThrows(IllegalStateException.class, this.model::discardDraw);
  }

  @Test
  public void testDiscardDraw0Redraws1Card() {
    this.model = new LimitedDrawKlondike(0);
    this.model.startGame(this.deck.subList(0, 2), false, 1, 1);
    this.model.discardDraw(); // card is discarded and NOT redrawn
    Assert.assertThrows(IllegalStateException.class, this.model::discardDraw);
  }

  @Test
  public void testDiscardDraw2Redraws3Cards() {
    this.model.startGame(this.deck.subList(0, 4), false, 1, 3);
    this.model.discardDraw(); // first card, first redraw
    this.model.discardDraw(); // second card, first redraw
    this.model.discardDraw(); // third card, first redraw
    this.model.discardDraw(); // first card, second redraw
    this.model.discardDraw(); // second card, second redraw
    this.model.discardDraw(); // third card, second redraw
    this.model.discardDraw(); // first card is NOT redrawn
    this.model.discardDraw(); // second card is NOT redrawn
    this.model.discardDraw(); // third card is NOT redrawn
    Assert.assertThrows(IllegalStateException.class, this.model::discardDraw);
  }

  @Test
  public void testDiscardDraw2RedrawsWithCardsPlayed() {
    this.deck = this.deck.subList(0, 4);
    this.putCardAtIndex("A", "", 1); // ace is first card in the draw pile
    this.model.startGame(this.deck, false, 1, 3);
    this.model.discardDraw(); // first card, first redraw
    this.model.discardDraw(); // second card, first redraw
    this.model.discardDraw(); // third card, first redraw
    this.model.discardDraw(); // first card, second redraw
    this.model.discardDraw(); // second card, second redraw
    this.model.discardDraw(); // third card, second redraw
    this.model.moveDrawToFoundation(0); // ace is moved, leaving only 2 cards
    this.model.discardDraw(); // second card is NOT redrawn
    this.model.discardDraw(); // third card is NOT redrawn
    Assert.assertThrows(IllegalStateException.class, this.model::discardDraw);
  }
}