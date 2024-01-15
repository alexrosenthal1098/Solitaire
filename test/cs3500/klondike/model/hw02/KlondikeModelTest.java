package cs3500.klondike.model.hw02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;

/**
 * Tests for public interface methods of KlondikeModel.
 */
public abstract class KlondikeModelTest {
  KlondikeModel game;
  List<Card> deck;

  @Before
  public void setUp() {
    this.game = this.allModels();
    this.deck = this.game.getDeck();
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

  protected abstract KlondikeModel allModels();

  protected abstract KlondikeModel basicAndLimited();

  /**
   * A class for testing that returns a BasicKlondike object.
   */
  public static final class BasicTest extends KlondikeModelTest {
    protected KlondikeModel allModels() {
      return new BasicKlondike();
    }

    protected KlondikeModel basicAndLimited() {
      return new BasicKlondike();
    }
  }

  /**
   * A class for testing that returns a LimitedDrawKlondike object.
   */
  public static final class LimitedTest extends KlondikeModelTest {
    protected KlondikeModel allModels() {
      return new LimitedDrawKlondike(2);
    }

    protected KlondikeModel basicAndLimited() {
      return new LimitedDrawKlondike(2);
    }
  }

  /**
   * A class for testing that returns a WhiteheadKlondike object.
   */
  public static final class WhiteheadTest extends KlondikeModelTest {
    protected KlondikeModel allModels() {
      return new WhiteheadKlondike();
    }

    protected KlondikeModel basicAndLimited() {
      return new BasicKlondike();
    }
  }


  // tests for getDeck()
  @Test
  public void testGetDeckHas52Card() {
    List<Card> deck = this.game.getDeck();
    Assert.assertEquals(52, deck.size());
  }

  @Test
  public void testGetDeckHasEqualSuits() {
    List<Card> deck = this.game.getDeck();
    int numHearts = deck.stream().mapToInt(c -> c.toString().contains("♡") ? 1 : 0).sum();
    int numSpades = deck.stream().mapToInt(c -> c.toString().contains("♠") ? 1 : 0).sum();
    int numDiamonds = deck.stream().mapToInt(c -> c.toString().contains("♢") ? 1 : 0).sum();
    int numClubs = deck.stream().mapToInt(c -> c.toString().contains("♣") ? 1 : 0).sum();
    Assert.assertEquals(13, numHearts);
    Assert.assertEquals(13, numSpades);
    Assert.assertEquals(13, numDiamonds);
    Assert.assertEquals(13, numClubs);
  }

  @Test
  public void testGetDeckHasEqualValues() {
    List<Card> deck = this.game.getDeck();
    int numAces = deck.stream().mapToInt(c -> c.toString().contains("A") ? 1 : 0).sum();
    int numTwos = deck.stream().mapToInt(c -> c.toString().contains("2") ? 1 : 0).sum();
    int numThrees = deck.stream().mapToInt(c -> c.toString().contains("3") ? 1 : 0).sum();
    int numFours = deck.stream().mapToInt(c -> c.toString().contains("4") ? 1 : 0).sum();
    int numFives = deck.stream().mapToInt(c -> c.toString().contains("5") ? 1 : 0).sum();
    int numSixes = deck.stream().mapToInt(c -> c.toString().contains("6") ? 1 : 0).sum();
    int numSevens = deck.stream().mapToInt(c -> c.toString().contains("7") ? 1 : 0).sum();
    int numEights = deck.stream().mapToInt(c -> c.toString().contains("8") ? 1 : 0).sum();
    int numNines = deck.stream().mapToInt(c -> c.toString().contains("9") ? 1 : 0).sum();
    int numTens = deck.stream().mapToInt(c -> c.toString().contains("10") ? 1 : 0).sum();
    int numJacks = deck.stream().mapToInt(c -> c.toString().contains("J") ? 1 : 0).sum();
    int numQueens = deck.stream().mapToInt(c -> c.toString().contains("Q") ? 1 : 0).sum();
    int numKings = deck.stream().mapToInt(c -> c.toString().contains("K") ? 1 : 0).sum();
    Assert.assertEquals(4, numAces);
    Assert.assertEquals(4, numTwos);
    Assert.assertEquals(4, numThrees);
    Assert.assertEquals(4, numFours);
    Assert.assertEquals(4, numFives);
    Assert.assertEquals(4, numSixes);
    Assert.assertEquals(4, numSevens);
    Assert.assertEquals(4, numEights);
    Assert.assertEquals(4, numNines);
    Assert.assertEquals(4, numTens);
    Assert.assertEquals(4, numJacks);
    Assert.assertEquals(4, numQueens);
    Assert.assertEquals(4, numKings);
  }



  // tests for startGame()
  @Test(expected = IllegalStateException.class)
  public void testStartGameAlreadyStarted() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.startGame(this.deck, true, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidDeck() {
    this.game.startGame(this.deck.subList(5, 45), true, 3, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumPiles() {
    this.game.startGame(this.deck, false, 50, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidNumDraw() {
    this.game.startGame(this.deck, false, 3, -3);
  }

  @Test
  public void testStartGameDeckIsCopy() {
    List<Card> deck2 = new ArrayList<>(this.deck);
    this.game.startGame(this.deck, true, 7, 3);
    Assert.assertEquals(this.deck, deck2);
  }

  @Test
  public void testStartGameFoundationInit() {
    this.game.startGame(this.deck, true, 7, 3);
    Assert.assertNull(this.game.getCardAt(0));
    Assert.assertNull(this.game.getCardAt(1));
    Assert.assertNull(this.game.getCardAt(2));
    Assert.assertNull(this.game.getCardAt(3));
  }

  @Test
  public void testStartGameProperVisibility() {
    this.game = this.basicAndLimited();
    this.game.startGame(this.deck, true, 7, 3);
    Assert.assertTrue(this.game.isCardVisible(6, 6));
    Assert.assertFalse(this.game.isCardVisible(6, 5));
  }

  @Test
  public void testStartGameWithoutShuffle() {
    this.game.startGame(this.deck, false, 1, 3);
    Assert.assertEquals(new KlondikeCard(CardValue.ACE, CardSuit.HEARTS),
            this.game.getCardAt(0, 0));
    Assert.assertEquals(new KlondikeCard(CardValue.TWO, CardSuit.HEARTS),
            this.game.getDrawCards().get(0));
  }

  @Test
  public void testStartGameWithShuffle() {
    this.game.startGame(this.deck, true, 3, 3);
    boolean card1NoShuffle = new KlondikeCard(CardValue.ACE, CardSuit.HEARTS)
            .equals(this.game.getCardAt(0, 0));
    boolean card2NoShuffle = new KlondikeCard(CardValue.FOUR, CardSuit.HEARTS)
            .equals(this.game.getCardAt(1, 1));
    boolean card3NoShuffle = new KlondikeCard(CardValue.SIX, CardSuit.HEARTS)
            .equals(this.game.getCardAt(2, 2));
    Assert.assertFalse(card1NoShuffle && card2NoShuffle && card3NoShuffle);
  }

  @Test
  public void testStartGameDealing() {
    this.game.startGame(this.deck,false, 3, 1);
    // test that the piles have the correct heights
    Assert.assertEquals(1, this.game.getPileHeight(0));
    Assert.assertEquals(2, this.game.getPileHeight(1));
    Assert.assertEquals(3, this.game.getPileHeight(2));
    // test that the correct cards were dealt
    Assert.assertEquals(new KlondikeCard(CardValue.ACE, CardSuit.HEARTS),
            this.game.getCardAt(0, 0));
    Assert.assertEquals(new KlondikeCard(CardValue.FOUR, CardSuit.HEARTS),
            this.game.getCardAt(1, 1));
    Assert.assertEquals(new KlondikeCard(CardValue.SIX, CardSuit.HEARTS),
            this.game.getCardAt(2, 2));
  }

  @Test
  public void testStartGameCorrectNumberDrawCards() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(3, this.game.getDrawCards().size());
  }

  @Test
  public void testStartGameCorrectDrawCards() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(new KlondikeCard(CardValue.THREE, CardSuit.DIAMONDS),
            this.game.getDrawCards().get(0));
    Assert.assertEquals(new KlondikeCard(CardValue.FOUR, CardSuit.DIAMONDS),
            this.game.getDrawCards().get(1));
    Assert.assertEquals(new KlondikeCard(CardValue.FIVE, CardSuit.DIAMONDS),
            this.game.getDrawCards().get(2));
  }



  // tests for movePile
  @Test(expected = IllegalStateException.class)
  public void testMovePileGameNotStarted() {
    this.game.movePile(0, 1, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileMoveNotAllowedWrongValue() {
    this.game = this.basicAndLimited();
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(0, 1, 1); // ace cannot move onto a 3
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileMoveNotAllowedWrongSuit() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♢", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 1, 0); // red queen can't go on red king
  }

  @Test(expected = IllegalStateException.class)
  public void testMovePileToEmptyMustBeKing() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("3", "♢", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0
    this.game.movePile(1, 1, 0); // a 3 can't go in an empty pile
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileSrcEmpty() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0
    this.game.movePile(0, 1, 1); // pile 0 is empty
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidSrcPile() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.movePile(8, 1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidDestPile() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.movePile(0, 1, -5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidNumCards0() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.movePile(0, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileInvalidNumCardsTooHigh() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.movePile(0, 3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovePileSamePileNumber() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.movePile(0, 1, 0);
  }

  @Test
  public void testMovePileUpdatesHeights() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 2);
    this.game.startGame(this.deck, false, 2, 3);
    int pile0Height = this.game.getPileHeight(0);
    int pile1Height = this.game.getPileHeight(1);
    this.game.movePile(1, 1, 0); // move black queen onto red king
    Assert.assertEquals(pile0Height + 1, this.game.getPileHeight(0));
    Assert.assertEquals(pile1Height - 1, this.game.getPileHeight(1));
  }

  @Test
  public void testMovePileCorrectCard() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 2);
    this.game.startGame(this.deck, false, 2, 3);
    Card cardToMove = this.game.getCardAt(1, 1); // black queen
    this.game.movePile(1, 1, 0); // move black queen onto red king
    Assert.assertEquals(cardToMove, this.game.getCardAt(0, 1));
  }

  @Test
  public void testMovePileMultipleCards() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 3);
    this.putCardAtIndex("J", "♢", 5);
    this.game.startGame(this.deck, false, 3, 3);
    Card card1 = this.game.getCardAt(2, 2); // red jack
    Card card2 = this.game.getCardAt(1, 1); // black queen
    this.game.movePile(2, 1, 1); // move red jack onto black queen

    // move red jack and black queen onto red king
    this.game.movePile(1, 2, 0);
    Assert.assertEquals(card1, this.game.getCardAt(0, 2));
    Assert.assertEquals(card2, this.game.getCardAt(0, 1));
  }

  @Test
  public void testMovePileSameVisibility() {
    this.game = this.basicAndLimited();
    // visibility of the cards that move along with the card they are placed on top of
    // should remain the same
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 1, 0); // move black queen onto red king
    Assert.assertTrue(this.game.isCardVisible(0, 0));
    Assert.assertTrue(this.game.isCardVisible(0, 1));
  }

  @Test
  public void testMovePileRevealsUnderneath() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 1, 0); // move black queen onto red king
    Assert.assertTrue(this.game.isCardVisible(1, 0));
  }

  @Test
  public void testMovePileToEmptyWithKing() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 2);
    this.putCardAtIndex("A", "♠", 0);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0); // move ace to foundation
    Card card = this.game.getCardAt(1, 1); // red king
    this.game.movePile(1, 1, 0); // move king to empty pile 0
    Assert.assertEquals(card, this.game.getCardAt(0, 0));
  }



  // tests for moveDraw
  @Test(expected = IllegalStateException.class)
  public void testMoveDrawGameNotStarted() {
    this.game.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawNoDrawCardsLeft() {
    this.game.startGame(this.deck.subList(0, 3), false, 2, 1);
    this.game.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawMoveNotAllowedWrongValue() {
    this.game = this.basicAndLimited();
    this.game.startGame(this.deck, false, 7, 3);
    this.game.moveDraw(0); // can't move red three onto red ace
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawMoveNotAllowedWrongSuit() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♡", 1);
    this.game.startGame(this.deck, false, 1, 1);
    this.game.moveDraw(0); // can't move red queen onto red king
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDrawToEmptyMustBeKing() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("A", "", 0);
    this.putCardAtIndex("Q", "♡", 1);
    this.game.startGame(this.deck, false, 1, 1);
    this.game.moveToFoundation(0, 0); // move ace to foundation
    this.game.moveDraw(0); // can't move queen onto empty pile
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDrawInvalidDestPile() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.moveDraw(20);
  }

  @Test
  public void testMoveDrawTakesCorrectCardAndDestination() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    Card card = this.game.getDrawCards().get(0); // black queen
    this.game.moveDraw(0); // move black queen onto red king
    Assert.assertEquals(card, this.game.getCardAt(0, 1));
  }

  @Test
  public void testMoveDrawCorrectVisibility() {
    this.game = this.basicAndLimited();
    // the draw card that moves should be visible
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0); // move black queen onto red king
    Assert.assertTrue(this.game.isCardVisible(0, 1));
  }

  @Test
  public void testMoveDrawMaintainsVisibility() {
    this.game = this.basicAndLimited();
    // the cards underneath the draw card that moves should still be visible
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0); // move black queen onto red king
    Assert.assertTrue(this.game.isCardVisible(0, 0));
  }

  @Test
  public void testMoveDrawToEmptyWithKing() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("A", "", 0);
    this.putCardAtIndex("K", "♡", 1);
    this.game.startGame(this.deck, false, 1, 1);
    this.game.moveToFoundation(0, 0); // move ace to foundation
    this.game.moveDraw(0); // move king into empty pile
    Assert.assertTrue(this.game.getCardAt(0, 0).toString().contains("K"));
  }

  @Test
  public void testMoveDrawCyclesDrawPile() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    Card draw2 = this.game.getDrawCards().get(1);
    Card draw3 = this.game.getDrawCards().get(2);
    this.game.moveDraw(0); // move black queen onto red king
    Assert.assertEquals(draw2, this.game.getDrawCards().get(0));
    Assert.assertEquals(draw3, this.game.getDrawCards().get(1));
  }

  @Test
  public void testMoveDrawDrawsNewCard() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.putCardAtIndex("A", "♠", 4);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0); // move black queen onto red king
    Assert.assertEquals(new KlondikeCard(CardValue.ACE, CardSuit.SPADES),
            this.game.getDrawCards().get(2));
  }

  @Test
  public void testMoveDrawDoesntDecreaseDrawPile() {
    this.game = basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0); // move black queen onto red king
    Assert.assertEquals(3, this.game.getDrawCards().size());
  }



  // tests for moveToFoundation
  @Test(expected = IllegalStateException.class)
  public void testMoveToFGameNotStarted() {
    this.game.moveToFoundation(0, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveToFSrcPileEmpty() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 7, 3);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0
    this.game.moveToFoundation(0, 0); // pile 0 is empty
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveToFMoveInvalidMoveWrongSuit() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♠", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0;
    this.game.moveToFoundation(1, 0); // cannot move a spade onto a heart
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveToFMoveInvalidMoveWrongValue() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("3", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0;
    this.game.moveToFoundation(1, 0); // cannot move a 3 onto an ace
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFInvalidSrcPile() {
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToFInvalidFoundationPile() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 5);
  }

  @Test
  public void testMoveToFCorrectSourcePile() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 2, 3);
    int pile0Height = this.game.getPileHeight(0);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0;
    Assert.assertEquals(pile0Height - 1, this.game.getPileHeight(0));
  }

  @Test
  public void testMoveToFCorrectFoundation() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 2, 3);
    Card card = this.game.getCardAt(0, 0);
    this.game.moveToFoundation(0, 0); // move ace to foundation 0;
    Assert.assertEquals(card, this.game.getCardAt(0));
  }

  @Test
  public void testMoveToFRevealsUnderneath() {
    this.putCardAtIndex("A", "", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(1, 0); // move ace to foundation 0
    Assert.assertTrue(this.game.isCardVisible(1, 0));
  }

  @Test
  public void testMoveToFUpdatesScore() {
    this.putCardAtIndex("A", "", 2);
    this.game.startGame(this.deck, false, 2, 3);
    int score = this.game.getScore();
    this.game.moveToFoundation(1, 0); // move ace to foundation 0
    Assert.assertEquals(score + 1, this.game.getScore());
  }



  // tests for moveDrawToFoundation
  @Test(expected = IllegalStateException.class)
  public void testMoveDToFGameNotStarted() {
    this.game.moveDrawToFoundation(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDToFInvalidMoveWrongValue() {
    this.putCardAtIndex("2", "", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDrawToFoundation(0); // cannot put a 2 on an empty foundation pile
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDToFInvalidMoveWrongSuit() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♢", 2);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDrawToFoundation(0);
    this.game.moveDrawToFoundation(0); // cannot put a diamond on a heart
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveDToFEmptyDrawPile() {
    this.game.startGame(this.deck.subList(0, 1), false, 1, 3);
    this.game.moveDrawToFoundation(0); // there are no draw cards left
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveDToFInvalidFoundationPile() {
    this.putCardAtIndex("A", "♡", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDrawToFoundation(5);
  }

  @Test
  public void testMoveDToFCorrectDrawCard() {
    this.putCardAtIndex("A", "♡", 1);
    this.game.startGame(this.deck, false, 1, 3);
    Card card = this.game.getDrawCards().get(0);
    this.game.moveDrawToFoundation(0); // move ace to foundation pile 0
    Assert.assertEquals(card, this.game.getCardAt(0));
  }

  @Test
  public void testMoveDToFCorrectFoundationPile() {
    this.putCardAtIndex("A", "♡", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDrawToFoundation(0); // move ace to foundation pile 0
    Assert.assertNotNull(this.game.getCardAt(0));
    Assert.assertNull(this.game.getCardAt(1));
    Assert.assertNull(this.game.getCardAt(2));
    Assert.assertNull(this.game.getCardAt(3));
  }

  @Test
  public void testMoveDToFCyclesDrawPile() {
    this.putCardAtIndex("A", "♡", 1);
    this.game.startGame(this.deck, false, 1, 2);
    Card nextDrawCard = this.game.getDrawCards().get(1);
    this.game.moveDrawToFoundation(0); // move ace to foundation pile 0
    Assert.assertEquals(nextDrawCard, this.game.getDrawCards().get(0));
  }

  @Test
  public void testMoveDToFDrawsNewCard() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("10", "♡", 4);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDrawToFoundation(0); // move ace to foundation pile 0
    Assert.assertEquals(new KlondikeCard(CardValue.TEN, CardSuit.HEARTS),
            this.game.getDrawCards().get(2));
  }

  @Test
  public void testMoveDToFDoesntDecreaseDrawPile() {
    this.putCardAtIndex("A", "♡", 1);
    this.game.startGame(this.deck, false, 1, 2);
    this.game.moveDrawToFoundation(0); // move ace to foundation pile 0
    int drawPileSize = this.game.getDrawCards().size();
    Assert.assertEquals(drawPileSize, this.game.getDrawCards().size());
  }



  // tests for discardDraw
  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawGameNotStarted() {
    this.game.discardDraw();
  }

  @Test(expected = IllegalStateException.class)
  public void testDiscardDrawNoDrawsLeft() {
    this.game.startGame(this.deck.subList(0, 1), false, 1, 1);
    this.game.discardDraw();
  }

  @Test
  public void testDiscardDrawCyclesDrawPile() {
    this.game.startGame(this.deck, false, 1, 3);
    Card drawCard2 = this.game.getDrawCards().get(1);
    Card drawCard3 = this.game.getDrawCards().get(2);
    this.game.discardDraw();
    Assert.assertEquals(drawCard2, this.game.getDrawCards().get(0));
    Assert.assertEquals(drawCard3, this.game.getDrawCards().get(1));
  }

  @Test
  public void testDiscardDrawDrawsNewCard() {
    this.putCardAtIndex("A", "♡", 4);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.discardDraw();
    Assert.assertEquals(new KlondikeCard(CardValue.ACE, CardSuit.HEARTS),
            this.game.getDrawCards().get(2));
  }

  @Test
  public void testDiscardDrawDoesntDecreaseDrawPile() {
    this.game.startGame(this.deck, false, 1, 3);
    int drawSize = this.game.getDrawCards().size();
    this.game.discardDraw();
    Assert.assertEquals(drawSize, this.game.getDrawCards().size());
  }

  @Test
  public void testDiscardDrawReusesDeck() {
    this.game.startGame(this.deck, false, 1, 3);
    Card card = this.game.getDrawCards().get(0);
    for (int i = 0; i < 51; i++ ) {
      this.game.discardDraw();
    }
    Assert.assertEquals(card, this.game.getDrawCards().get(0));
  }



  // tests for getNumRows
  @Test (expected = IllegalStateException.class)
  public void testGetNumRowsGameNotStarted() {
    this.game.getNumRows();
  }

  @Test
  public void testGetNumRows1Piles() {
    this.game.startGame(this.deck, true, 1, 3);
    Assert.assertEquals(1, this.game.getNumRows());
  }

  @Test
  public void testGetNumRows7Piles() {
    this.game.startGame(this.deck, true, 7, 3);
    Assert.assertEquals(7, this.game.getNumRows());
  }

  @Test
  public void testGetNumRowsMoreRowsThanPiles() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("Q", "♠", 0);
    this.putCardAtIndex("K", "♡", 5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.movePile(0, 1, 2);
    Assert.assertEquals(4, this.game.getNumRows());
  }

  @Test
  public void testGetNumRowsLessRowsThanPiles() {
    this.putCardAtIndex("A", "♡", 5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.moveToFoundation(2, 0);
    Assert.assertEquals(2, this.game.getNumRows());
  }



  // tests for getNumPiles
  @Test (expected = IllegalStateException.class)
  public void testGetNumPilesGameNotStarted() {
    this.game.getNumPiles();
  }

  @Test
  public void testGetNumPiles1() {
    this.game.startGame(this.deck, true, 1, 3);
    Assert.assertEquals(1, this.game.getNumPiles());
  }

  @Test
  public void testGetNumPiles7() {
    this.game.startGame(this.deck, true, 7, 3);
    Assert.assertEquals(7, this.game.getNumPiles());
  }

  @Test
  public void testGetNumPilesEmptyPiles() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 5, 3);
    this.game.moveToFoundation(0, 0);
    Assert.assertEquals(5, this.game.getNumPiles());
  }



  // tests for getNumDraw() {
  @Test (expected = IllegalStateException.class)
  public void testGetNumDrawGameNotStarted() {
    this.game.getNumDraw();
  }

  @Test
  public void testGetNumDraw3() {
    this.game.startGame(this.deck, true, 7, 3);
    Assert.assertEquals(3, this.game.getNumDraw());
  }

  @Test
  public void testGetNumDraw100() {
    this.game.startGame(this.deck, true, 7, 100);
    Assert.assertEquals(100, this.game.getNumDraw());
  }

  @Test
  public void testGetNumDrawPartiallyFullDrawPile() {
    this.game.startGame(this.deck.subList(0, 26), true, 3, 30);
    Assert.assertEquals(30, this.game.getNumDraw());
  }



  // tests for isGameOver
  @Test (expected = IllegalStateException.class)
  public void testIsGameOverGameNotStarted() {
    this.game.isGameOver();
  }

  @Test
  public void testIsGameOverStartOfGame() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertFalse(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverNoMoreMoves() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 2);
    this.putCardAtIndex("2", "♠", 3);
    this.putCardAtIndex("A", "♢", 4);
    this.putCardAtIndex("2", "♢", 5);
    this.game.startGame(this.deck.subList(0, 6), false, 3, 1);
    Assert.assertTrue(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverNoMovesButCanDiscardDraw() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 2);
    this.putCardAtIndex("2", "♠", 3);
    this.putCardAtIndex("A", "♢", 4);
    this.putCardAtIndex("2", "♢", 5);
    this.putCardAtIndex("A", "♣", 6);
    this.putCardAtIndex("2", "♣", 7);
    this.game.startGame(this.deck.subList(0, 8), false, 3, 1);
    Assert.assertFalse(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverPileMovesLeft() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("3", "♡", 3);
    this.putCardAtIndex("A", "♠", 2);
    this.putCardAtIndex("2", "♠", 4);
    this.putCardAtIndex("3", "♠", 5);
    this.game.startGame(this.deck.subList(0, 6), false, 3, 1);
    Assert.assertFalse(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverPileMovesLeftKing() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("K", "♠", 8);
    this.putCardAtIndex("2", "♡", 36);
    this.putCardAtIndex("3", "♡", 37);
    this.putCardAtIndex("4", "♡", 38);
    this.game.startGame(this.deck.subList(0, 39), false, 8, 1);
    this.game.moveToFoundation(0, 0);
    this.game.moveDrawToFoundation(0);
    this.game.moveDrawToFoundation(0);
    this.game.moveDrawToFoundation(0);
    Assert.assertFalse(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverFoundationMovesLeftAce() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    this.putCardAtIndex("A", "♠", 3);
    this.putCardAtIndex("2", "♠", 4);
    this.putCardAtIndex("3", "♠", 5);
    this.game.startGame(this.deck.subList(0, 6), false, 3, 1);
    Assert.assertFalse(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverFoundationMovesLeftNotAce() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    this.putCardAtIndex("A", "♠", 3);
    this.putCardAtIndex("2", "♠", 4);
    this.putCardAtIndex("3", "♠", 5);
    this.game.startGame(this.deck.subList(0, 6), false, 3, 1);
    this.game.moveToFoundation(0, 0);
    Assert.assertFalse(this.game.isGameOver());
  }

  @Test
  public void testIsGameOverWonGame() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("A", "♠", 1);
    this.putCardAtIndex("A", "♢", 2);
    this.putCardAtIndex("A", "♣", 3);
    this.game.startGame(this.deck.subList(0, 4), false, 1, 1);
    this.game.moveToFoundation(0, 0);
    this.game.moveDrawToFoundation(1);
    this.game.moveDrawToFoundation(2);
    this.game.moveDrawToFoundation(3);
    Assert.assertTrue(this.game.isGameOver());
  }



  // tests for getScore
  @Test (expected = IllegalStateException.class)
  public void testGetScoreGameNotStarted() {
    this.game.getScore();
  }

  @Test
  public void testGetScore0() {
    this.game.startGame(this.deck, false, 7, 2);
    Assert.assertEquals(0, this.game.getScore());
  }

  @Test
  public void testGetScoreMultipleCardsInEachPile() {
    this.game.startGame(this.deck, false, 7, 2);
    Assert.assertEquals(0, this.game.getScore());
  }

  @Test
  public void testGetScoreWonGame() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("A", "♠", 1);
    this.putCardAtIndex("A", "♢", 2);
    this.putCardAtIndex("A", "♣", 3);
    List<Card> deck1 = this.deck.subList(0, 4);
    this.game.startGame(deck1, true, 1, 4);
    this.game.moveToFoundation(0, 0);
    this.game.moveDrawToFoundation(1);
    this.game.moveDrawToFoundation(2);
    this.game.moveDrawToFoundation(3);
    Assert.assertEquals(deck1.size(), this.game.getScore());
  }



  // tests for getPileHeight() {
  @Test (expected = IllegalStateException.class)
  public void testGetPileHeightGameNotStarted() {
    this.game.getPileHeight(0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetPileHeightNegativeIndex() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.getPileHeight(-3);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetPileHeightIndexTooHigh() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.getPileHeight(7);
  }

  @Test
  public void testGetPileHeightAtGameStart() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(5, this.game.getPileHeight(4));
  }

  @Test
  public void testGetPileHeightAfterMove() {
    this.putCardAtIndex("A", "♡", 5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.moveToFoundation(2, 0);
    Assert.assertEquals(2, this.game.getPileHeight(2));
  }

  @Test
  public void testGetPileHeightEmptyPile() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 7, 3);
    this.game.moveToFoundation(0, 0);
    Assert.assertEquals(0, this.game.getPileHeight(0));
  }



  // tests for isCardVisible
  @Test (expected = IllegalStateException.class)
  public void testIsCardVisibleGameNotStarted() {
    this.game.isCardVisible(0, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidPileNum() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.isCardVisible(10, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testIsCardVisibleInvalidCardNum() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.isCardVisible(0, -1);
  }

  @Test
  public void testIsCardVisibleTopOfPile() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertTrue(this.game.isCardVisible(6, 6));
  }

  @Test
  public void testIsCardVisibleBottomOfPile() {
    this.game = this.basicAndLimited();
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertFalse(this.game.isCardVisible(3, 0));
  }

  @Test
  public void testIsCardVisibleAfterReveal() {
    this.putCardAtIndex("A", "♡", 5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.moveToFoundation(2, 0);
    Assert.assertTrue(this.game.isCardVisible(2, 1));
  }



  // tests for getCardAt for cascade piles
  @Test (expected = IllegalStateException.class)
  public void testGetCardAtPGameNotStarted() {
    this.game.getCardAt(0, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCardAtPInvalidPileNum() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.getCardAt(-3, 0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCardAtPInvalidCardNum() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.getCardAt(0, 10);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCardAtPNotVisible() {
    this.game = this.basicAndLimited();
    this.game.startGame(this.deck, false, 7, 3);
    this.game.getCardAt(3, 0);
  }

  @Test
  public void testGetCardAtPTopOfPile() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(new KlondikeCard(CardValue.ACE, CardSuit.HEARTS),
            this.game.getCardAt(0, 0));
  }

  @Test
  public void testGetCardAtPBottomOfPile() {
    this.game = this.basicAndLimited();
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0);
    Assert.assertEquals(new KlondikeCard(CardValue.KING, CardSuit.HEARTS),
            this.game.getCardAt(0, 0));
  }

  @Test
  public void testGetCardAtPAfterReveal() {
    this.putCardAtIndex("K", "♡", 4);
    this.putCardAtIndex("A", "♡", 5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.moveToFoundation(2, 0);
    Assert.assertEquals(new KlondikeCard(CardValue.KING, CardSuit.HEARTS),
            this.game.getCardAt(2, 1));
  }



  // tests for getCardAt for foundation piles
  @Test (expected = IllegalStateException.class)
  public void testGetCardAtFGameNotStarted() {
    this.game.getCardAt(0);
  }

  @Test (expected = IllegalArgumentException.class)
  public void testGetCardAtFInvalidFoundationNum() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.getCardAt(10);
  }

  @Test
  public void testGetCardAtFNull() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertNull(this.game.getCardAt(3));
  }

  @Test
  public void testGetCardAtFAce() {
    this.putCardAtIndex("A", "♡", 0);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.moveToFoundation(0, 2);
    Assert.assertEquals(new KlondikeCard(CardValue.ACE, CardSuit.HEARTS),
            this.game.getCardAt(2));
  }

  @Test
  public void testGetCardAtFMultipleCardsInPile() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 2);
    this.game.moveDrawToFoundation(2);
    this.game.moveDrawToFoundation(2);
    Assert.assertEquals(new KlondikeCard(CardValue.THREE, CardSuit.HEARTS),
            this.game.getCardAt(2));
  }



  // tests for getDrawCards
  @Test (expected = IllegalStateException.class)
  public void testGetDrawCardsGameNotStarted() {
    this.game.getDrawCards();
  }

  @Test
  public void testGetDrawCardsNoneLeft() {
    this.game.startGame(this.deck.subList(0, 1), false, 1, 5);
    Assert.assertEquals(new ArrayList<Card>(), this.game.getDrawCards());
  }

  @Test
  public void testGetDrawCardsProperSize() {
    this.game.startGame(this.deck, false, 7, 100);
    Assert.assertEquals(24, this.game.getDrawCards().size());
  }

  @Test
  public void testGetDrawCardsCorrectCards() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(new KlondikeCard(CardValue.THREE, CardSuit.DIAMONDS),
            this.game.getDrawCards().get(0));
    Assert.assertEquals(new KlondikeCard(CardValue.FOUR, CardSuit.DIAMONDS),
            this.game.getDrawCards().get(1));
    Assert.assertEquals(new KlondikeCard(CardValue.FIVE, CardSuit.DIAMONDS),
            this.game.getDrawCards().get(2));
  }



  // tests for getNumFoundations
  @Test (expected = IllegalStateException.class)
  public void testGetNumFoundationsGameNotStarted() {
    this.game.getNumFoundations();
  }

  @Test
  public void testGetNumFoundationsStandard() {
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(4, this.game.getNumFoundations());
  }

  @Test
  public void testGetNumFoundationsMore() {
    this.deck.addAll(this.deck.subList(0, 13));
    this.game.startGame(this.deck, false, 7, 3);
    Assert.assertEquals(5, this.game.getNumFoundations());
  }

  @Test
  public void testGetNumFoundationsLess() {
    this.game.startGame(this.deck.subList(0, 13), false, 2, 3);
    Assert.assertEquals(1, this.game.getNumFoundations());
  }

  @Test
  public void testGetNumFoundationsCardInPile() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("A", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 1);
    this.game.moveDrawToFoundation(3);
    Assert.assertEquals(4, this.game.getNumFoundations());
  }




  //          tests for unique behavior of WhiteheadKlondike

  // tests for cardVisibility
  @Test
  public void testWhiteheadAllCardsVisible() {
    this.game = new WhiteheadKlondike();
    this.game.startGame(this.deck, false, 7, 3);
    int leftCol = 0;
    for (int pileNum = 0; pileNum < this.game.getNumPiles(); pileNum++, leftCol++) {
      for (int cardNum = leftCol; cardNum < this.game.getPileHeight(pileNum); cardNum++) {
        Assert.assertTrue(this.game.isCardVisible(pileNum, cardNum));
      }
    }
  }



  // tests for movePile
  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMovePileInvalidBuildValues() {
    this.game = new WhiteheadKlondike();
    this.game.startGame(this.deck, false, 7, 3);
    this.game.movePile(3, 2, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMovePileInvalidBuildCorrectDestination() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("7", "♢", 1);
    this.putCardAtIndex("3", "♢", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 2, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMovePileMultipleCardsNotSameSuit() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♡", 0);
    this.putCardAtIndex("7", "♢", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 2, 0);
  }

  @Test
  public void testWhiteheadMovePileMultipleCardsToEmpty() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("7", "♡", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0);
    this.game.movePile(1, 2, 0);
    Assert.assertTrue(true);
  }

  @Test
  public void testWhiteheadMovePileOneCardToEmpty() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0);
    this.game.movePile(1, 1, 0);
    Assert.assertTrue(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMovePileMultipleCardsWrongColorFromDestination() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♠", 0);
    this.putCardAtIndex("7", "♡", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0);
    this.game.movePile(1, 2, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMovePileMultipleCardsWrongValueFromDestination() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("7", "♡", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0);
    this.game.movePile(1, 2, 0);
  }

  @Test
  public void testWhiteheadMovePileMultipleCardsValid() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("7", "♡", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 2, 0);
    Assert.assertTrue(true);
  }

  @Test
  public void testWhiteheadMovePileOneCardValid() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("7", "♡", 2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.movePile(1, 1, 0);
    Assert.assertTrue(true);
  }



  // tests for moveDraw
  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveDrawWrongColor() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("7", "♠", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testWhiteheadMoveDrawWrongValue() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("3", "♢", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0);
  }

  @Test
  public void testWhiteheadMoveDrawSameSuit() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("7", "♢", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0);
    Assert.assertTrue(true);
  }

  @Test
  public void testWhiteheadMoveDrawSameColor() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("8", "♢", 0);
    this.putCardAtIndex("7", "♡", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveDraw(0);
    Assert.assertTrue(true);
  }

  @Test
  public void testWhiteheadMoveDrawToEmpty() {
    this.game = new WhiteheadKlondike();
    this.putCardAtIndex("A", "♢", 0);
    this.putCardAtIndex("A", "♡", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 0);
    this.game.moveDraw(0);
    Assert.assertTrue(true);
  }
}