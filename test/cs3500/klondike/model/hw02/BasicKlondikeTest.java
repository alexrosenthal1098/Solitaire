package cs3500.klondike.model.hw02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class that holds test for protected methods in BasicKlondike.
 */
public class BasicKlondikeTest {
  BasicKlondike game;
  List<Card> deck;

  @Before
  public void setUp() throws Exception {
    this.game = new BasicKlondike();
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

  // tests for validateGameStarted
  @Test(expected = IllegalStateException.class)
  public void testValidateGameNotStarted() {
    this.game.validateGameStarted();
  }

  @Test
  public void testValidateGameStarted() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validateGameStarted();
    Assert.assertTrue(true);
  }


  // tests for validatePileNum
  @Test(expected = IllegalArgumentException.class)
  public void testValidatePNumNegative() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validatePileNum(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidatePNumTooHigh() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validatePileNum(7);
  }

  @Test
  public void testValidatePNumValid3() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validatePileNum(3);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidatePNumValid0() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validatePileNum(0);
    Assert.assertTrue(true);
  }


  // tests for validateCardNum
  @Test(expected = IllegalArgumentException.class)
  public void testValidateCNumNegativeCard() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validateCardNum(0, -4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateCNumTooHighCard() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validateCardNum(0, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateCNumEmptyPile() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.moveToFoundation(0, 0);
    this.game.validateCardNum(0, 0);
  }

  @Test
  public void testValidateCNumValid0() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validateCardNum(0, 0);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateCNumValid5() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.validateCardNum(6, 5);
    Assert.assertTrue(true);
  }


  // tests for validateContents
  @Test(expected = IllegalArgumentException.class)
  public void testValidateContentsTooManyPiles() {
    this.game.validateContents(this.deck, 100, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateContentsNegativePiles() {
    this.game.validateContents(this.deck, -3, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateContents0DrawCards() {
    this.game.validateContents(this.deck, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateContentsNegativeDrawCards() {
    this.game.validateContents(this.deck, 3, -13);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateContentsInvalidDeck() {
    this.game.validateContents(this.deck.subList(10, 20), 1, 1);
  }

  @Test
  public void testValidateContentsStandard() {
    this.game.validateContents(this.deck, 3, 1);
    Assert.assertTrue(true);
  }


  // tests for validateDeck
  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckNullDeck() {
    this.game.validateDeck(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckWithNulls() {
    this.deck.set(0, null);
    this.game.validateDeck(this.deck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckEmpty() {
    this.game.validateDeck(new ArrayList<>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckExtraCard() {
    this.deck.add(this.deck.get(0));
    this.game.validateDeck(this.deck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckMissingCard() {
    this.deck.remove(0);
    this.game.validateDeck(this.deck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckAdditionalRunTooSmall() {
    this.deck.addAll(this.deck.subList(0, 10));
    this.game.validateDeck(this.deck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckAdditionalRunTooLarge() {
    // remove all kings and add an additional run that includes kings
    ArrayList<Card> fullRun = new ArrayList<>(this.deck.subList(0, 13));
    this.deck.remove(12);
    this.deck.remove(24);
    this.deck.remove(36);
    this.deck.remove(48);
    this.deck.addAll(fullRun);
    this.game.validateDeck(this.deck);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateDeckAdditionalRunWithDuplicate() {
    this.deck.addAll(this.deck.subList(0, 13));
    this.deck.add(this.deck.get(3));
    this.game.validateDeck(this.deck);
  }

  @Test
  public void testValidateDeckStandard() {
    this.game.validateDeck(this.deck);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateDeck6FullRuns() {
    this.deck.addAll(this.deck.subList(0, 26));
    this.game.validateDeck(this.deck);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateDeck1FullRuns() {
    this.game.validateDeck(this.deck.subList(0, 13));
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateDeck2PartialRuns() {
    ArrayList<Card> newDeck = new ArrayList<>(this.deck.subList(0, 10));
    newDeck.addAll(this.deck.subList(13, 23));
    this.game.validateDeck(newDeck);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateDeckOnlyAces() {
    ArrayList<Card> newDeck = new ArrayList<>();
    newDeck.add(this.deck.get(0));
    newDeck.add(this.deck.get(13));
    newDeck.add(this.deck.get(26));
    newDeck.add(this.deck.get(39));
    this.game.validateDeck(newDeck);
    Assert.assertTrue(true);
  }


  // tests for validateRuns
  @Test(expected = IllegalArgumentException.class)
  public void testValidateRunsExtraCards() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 10))); // ace through 10 of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 23))); // ace through 10 of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 36))); // ace through 10 of diamonds
    runs.add(new ArrayList<>(this.deck.subList(37, 52))); // ace through King of clubs
    this.game.validateRuns(runs);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRunsMissingCards() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 10))); // ace through 10 of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 23))); // ace through 10 of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 36))); // ace through 10 of diamonds
    runs.add(new ArrayList<>(this.deck.subList(39, 42))); // ace through 3 of clubs
    this.game.validateRuns(runs);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRunsAdditionalRunTooSmall() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 13))); // ace through K of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 26))); // ace through K of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 39))); // ace through K of diamonds
    runs.add(new ArrayList<>(this.deck.subList(39, 52))); // ace through K of clubs
    runs.add(new ArrayList<>(this.deck.subList(0, 5))); // ace through 5 of hearts
    this.game.validateRuns(runs);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRunsAdditionalRunTooLarge() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 10))); // ace through 10 of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 23))); // ace through 10 of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 36))); // ace through 10 of diamonds
    runs.add(new ArrayList<>(this.deck.subList(39, 49))); // ace through 10 of clubs
    runs.add(new ArrayList<>(this.deck.subList(39, 52))); // ace through K of clubs
    this.game.validateRuns(runs);
  }

  @Test
  public void testValidateRunsStandard() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 13))); // ace through K of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 26))); // ace through K of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 39))); // ace through K of diamonds
    runs.add(new ArrayList<>(this.deck.subList(39, 52))); // ace through K of clubs
    this.game.validateRuns(runs);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateRunsMoreRuns() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 13))); // ace through K of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 26))); // ace through K of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 39))); // ace through K of diamonds
    runs.add(new ArrayList<>(this.deck.subList(39, 52))); // ace through K of clubs
    runs.add(new ArrayList<>(this.deck.subList(26, 39))); // ace through K of diamonds
    runs.add(new ArrayList<>(this.deck.subList(39, 52))); // ace through K of clubs
    this.game.validateRuns(runs);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateRunsLessRuns() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 13))); // ace through K of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 26))); // ace through K of spades
    this.game.validateRuns(runs);
    Assert.assertTrue(true);
  }

  @Test()
  public void testValidateRunsPartialRuns() {
    ArrayList<ArrayList<Card>> runs = new ArrayList<>();
    runs.add(new ArrayList<>(this.deck.subList(0, 10))); // ace through 10 of hearts
    runs.add(new ArrayList<>(this.deck.subList(13, 23))); // ace through 10 of spades
    runs.add(new ArrayList<>(this.deck.subList(26, 36))); // ace through 10 of diamonds
    this.game.validateRuns(runs);
    Assert.assertTrue(true);
  }


  // tests for getTotalCascades
  @Test(expected = IllegalArgumentException.class)
  public void testGetTotalCascadesInvalid() {
    this.game.getTotalCascades(-3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetTotalCascades0() {
    this.game.getTotalCascades(0);
  }

  @Test
  public void testGetTotalCascades5() {
    Assert.assertEquals(15, this.game.getTotalCascades(5));
  }


  // tests for initFoundation
  @Test
  public void testInitFoundationStandardSize() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.initFoundation(new ArrayList<>(this.deck));
    Assert.assertEquals(4, this.game.getNumFoundations());
  }

  @Test
  public void testInitFoundationMoreAces() {
    this.game.startGame(this.deck, false, 7, 3);
    this.deck.addAll(this.deck.subList(0, 13));
    this.game.initFoundation(new ArrayList<>(this.deck));
    Assert.assertEquals(5, this.game.getNumFoundations());

  }

  @Test
  public void testInitFoundationLessAces() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.initFoundation(new ArrayList<>(this.deck.subList(0, 39)));
    Assert.assertEquals(3, this.game.getNumFoundations());
  }

  @Test
  public void testInitFoundationAllNull() {
    this.game.startGame(this.deck, false, 7, 3);
    this.game.initFoundation(new ArrayList<>(this.deck));
    Assert.assertNull(this.game.getCardAt(0));
    Assert.assertNull(this.game.getCardAt(1));
    Assert.assertNull(this.game.getCardAt(2));
    Assert.assertNull(this.game.getCardAt(3));
  }


  // tests for initVisibility
  @Test
  public void testInitVisibilitySetsAllTrue() {
    this.game.startGame(this.deck, false, 3, 1);
    this.game.initVisibility(new ArrayList<>(this.deck));
    Assert.assertTrue(this.game.isCardVisible(0, 0));
    Assert.assertTrue(this.game.isCardVisible(1, 1));
    Assert.assertTrue(this.game.isCardVisible(2, 2));
    Assert.assertTrue(this.game.isCardVisible(1, 0));
    Assert.assertTrue(this.game.isCardVisible(2, 0));
    Assert.assertTrue(this.game.isCardVisible(2, 1));
  }


  // tests for dealCascades
  @Test
  public void testDealCascades() {
    this.game.startGame(this.deck, false, 3, 1);
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
  public void testDealCascadesCorrectVisibility() {
    this.game.startGame(this.deck, false, 3, 1);
    // test that the top of piles are visible
    Assert.assertTrue(this.game.isCardVisible(0, 0));
    Assert.assertTrue(this.game.isCardVisible(1, 1));
    Assert.assertTrue(this.game.isCardVisible(2, 2));
    // test that the rest of piles aren't visible
    Assert.assertFalse(this.game.isCardVisible(1, 0));
    Assert.assertFalse(this.game.isCardVisible(2, 0));
    Assert.assertFalse(this.game.isCardVisible(2, 1));
  }



  // tests for validatePileMove
  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveOneNonKingToEmpty() {
    this.putCardAtIndex("A", "",  0);
    this.putCardAtIndex("10", "", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 0);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(this.deck.get(1))), 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveMultipleCardsNonKingToEmpty() {
    this.putCardAtIndex("A", "",  0);
    this.putCardAtIndex("10", "♠", 1);
    this.putCardAtIndex("9", "♠", 2);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 0);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(
            this.deck.get(1), this.deck.get(2))), 0);
  }

  @Test
  public void testValidatePileMoveOneKingToEmpty() {
    this.putCardAtIndex("A", "",  0);
    this.putCardAtIndex("K", "", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 0);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(this.deck.get(1))), 0);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidatePileMoveMultipleCardsKingToEmpty() {
    this.putCardAtIndex("A", "",  0);
    this.putCardAtIndex("K", "♠", 1);
    this.putCardAtIndex("Q", "♠", 2);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 0);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(
            this.deck.get(1), this.deck.get(2))), 0);
    Assert.assertTrue(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveOneWrongColor() {
    this.putCardAtIndex("5", "♠",  0);
    this.putCardAtIndex("4", "♣", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(this.deck.get(1))), 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveMultipleCardsWrongColor() {
    this.putCardAtIndex("5", "♠",  0);
    this.putCardAtIndex("4", "♣", 1);
    this.putCardAtIndex("3", "♢", 2);
    this.putCardAtIndex("2", "♣", 3);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(
            this.deck.get(1), this.deck.get(2), this.deck.get(3))), 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveOneWrongValue() {
    this.putCardAtIndex("3", "♠",  0);
    this.putCardAtIndex("4", "♣", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(this.deck.get(1))), 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveMultipleCardsWrongValue() {
    this.putCardAtIndex("7", "♠",  0);
    this.putCardAtIndex("4", "♣", 1);
    this.putCardAtIndex("3", "♢", 2);
    this.putCardAtIndex("2", "♣", 3);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(
            this.deck.get(1), this.deck.get(2), this.deck.get(3))), 0);
  }

  @Test
  public void testValidatePileMoveOneValid() {
    this.putCardAtIndex("5", "♢",  0);
    this.putCardAtIndex("4", "♣", 1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(this.deck.get(1))), 0);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidatePileMoveMultipleCardsWrongValid() {
    this.putCardAtIndex("5", "♢",  0);
    this.putCardAtIndex("4", "♣", 1);
    this.putCardAtIndex("3", "♢", 2);
    this.putCardAtIndex("2", "♣", 3);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validatePileMove(new ArrayList<>(Arrays.asList(
            this.deck.get(1), this.deck.get(2), this.deck.get(3))), 0);
    Assert.assertTrue(true);
  }



  // tests for validateFoundationMove
  @Test(expected = IllegalStateException.class)
  public void testValidateFoundationMoveNonAceToEmpty() {
    this.putCardAtIndex("5", "♢",  1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validateFoundationMove(this.deck.get(1), 0);
  }

  @Test
  public void testValidateFoundationMoveAceToEmpty() {
    this.putCardAtIndex("A", "♢",  1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.validateFoundationMove(this.deck.get(1), 3);
    Assert.assertTrue(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidateFoundationWrongSuit() {
    this.putCardAtIndex("A", "♢",  0);
    this.putCardAtIndex("2", "♣",  1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 2);
    this.game.validateFoundationMove(this.deck.get(1), 2);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidateFoundationWrongValue() {
    this.putCardAtIndex("A", "♢",  0);
    this.putCardAtIndex("3", "♢",  1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 2);
    this.game.validateFoundationMove(this.deck.get(1), 2);
  }

  @Test
  public void testValidateFoundationValid() {
    this.putCardAtIndex("A", "♢",  0);
    this.putCardAtIndex("2", "♢",  1);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 2);
    this.game.validateFoundationMove(this.deck.get(1), 2);
    Assert.assertTrue(true);
  }



  // tests for anyFoundationMoves
  @Test
  public void testAnyFoundationMovesAceToEmpty() {
    this.putCardAtIndex("A", "♢",  0);
    this.game.startGame(this.deck, false, 1, 3);
    Assert.assertTrue(this.game.anyFoundationMoves());
  }

  @Test
  public void testAnyFoundationMovesThreeOntoTwo() {
    this.putCardAtIndex("A", "♢",  0);
    this.putCardAtIndex("2", "♢",  3);
    this.putCardAtIndex("3", "♢",  5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.moveToFoundation(0, 0);
    this.game.moveToFoundation(1, 0);
    Assert.assertTrue(this.game.anyFoundationMoves());
  }

  @Test
  public void testAnyFoundationMovesNoMovesLeft() {
    this.putCardAtIndex("2", "♢",  0);
    this.putCardAtIndex("3", "♢",  3);
    this.putCardAtIndex("4", "♢",  5);
    this.game.startGame(this.deck, false, 3, 3);
    Assert.assertFalse(this.game.anyFoundationMoves());
  }



  // tests for anyPileMoves
  @Test
  public void testAnyPileMovesKingToEmpty() {
    this.putCardAtIndex("A", "",  0);
    this.putCardAtIndex("K", "",  2);
    this.game.startGame(this.deck, false, 2, 3);
    this.game.moveToFoundation(0, 0);
    Assert.assertTrue(this.game.anyPileMoves());
  }

  @Test
  public void testAnyPileMovesSingleCardMove() {
    this.putCardAtIndex("10", "♢",  0);
    this.putCardAtIndex("9", "♠",  2);
    this.game.startGame(this.deck, false, 2, 3);
    Assert.assertTrue(this.game.anyPileMoves());
  }

  @Test
  public void testAnyPileMovesMultipleCardMove() {
    this.putCardAtIndex("10", "♢",  0);
    this.putCardAtIndex("9", "♠",  3);
    this.putCardAtIndex("J", "♠",  5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.movePile(1, 1, 0);
    Assert.assertTrue(this.game.anyPileMoves());
  }

  @Test
  public void testAnyPileMovesMoveToSameValue() {
    this.putCardAtIndex("10", "♢",  0);
    this.putCardAtIndex("9", "♠",  3);
    this.putCardAtIndex("10", "♡",  5);
    this.game.startGame(this.deck, false, 3, 3);
    this.game.movePile(1, 1, 2);
    Assert.assertTrue(this.game.anyPileMoves());
  }

  @Test
  public void testAnyPilesMovesNoMovesLeftRandomCards() {
    this.putCardAtIndex("K", "♢",  0);
    this.putCardAtIndex("5", "♠",  3);
    this.putCardAtIndex("8", "♡",  5);
    this.game.startGame(this.deck, false, 3, 3);
    Assert.assertFalse(this.game.anyPileMoves());
  }

  @Test
  public void testAnyPilesMovesNoMovesLeftEmptyBoard() {
    this.putCardAtIndex("A", "",  0);
    this.game.startGame(this.deck, false, 1, 3);
    this.game.moveToFoundation(0, 0);
    Assert.assertFalse(this.game.anyPileMoves());
  }
}