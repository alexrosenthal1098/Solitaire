package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cs3500.klondike.model.hw02.Card;

/**
 * A class that holds tests for method overridden for WhiteheadKlondike.
 */
public class WhiteheadKlondikeTest {
  WhiteheadKlondike model;
  List<Card> deck;

  @Before
  public void setUp() throws Exception {
    this.model = new WhiteheadKlondike();
    this.deck = model.getDeck();
  }

  // tests for dealCascades
  @Test
  public void testDealCascadesKeepsAllCardsVisible() {
    this.model.startGame(this.deck, false, 7, 3);
    this.model.dealCascades(new ArrayList<>(this.deck), 5);
    int leftCol = 0;
    for (int pileNum = 0; pileNum < this.model.getNumPiles(); pileNum++, leftCol++) {
      for (int cardNum = leftCol; cardNum < this.model.getPileHeight(pileNum); cardNum++) {
        Assert.assertTrue(this.model.isCardVisible(pileNum, cardNum));
      }
    }
  }



  // tests for validatePileMove
  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveInvalidBuildValues() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(30), this.deck.get(19), this.deck.get(45)));
    this.model.validatePileMove(build, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveInvalidBuildCorrectDestination() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(26), this.deck.get(19), this.deck.get(45)));
    this.model.validatePileMove(build, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveMultipleCardsNotSameSuit() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(6), this.deck.get(31)));
    this.model.validatePileMove(build, 1);
  }

  @Test
  public void testValidatePileMoveMultipleCardsToEmpty7and6() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(6), this.deck.get(5)));
    this.model.moveToFoundation(0, 0);
    this.model.validatePileMove(build, 0);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidatePileMoveOneCardToEmpty10() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(9)));
    this.model.moveToFoundation(0, 0);
    this.model.validatePileMove(build, 0);
    Assert.assertTrue(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveMultipleCardsWrongColorFromDestination() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(19), this.deck.get(18)));
    this.model.validatePileMove(build, 1);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidatePileMoveMultipleCardsWrongValueFromDestination() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(19), this.deck.get(18)));
    this.model.validatePileMove(build, 0);
  }

  @Test
  public void testValidatePileMoveMultipleCardsValid() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(50), this.deck.get(49)));
    this.model.validatePileMove(build, 5);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidatePileMoveOneCardValid() {
    this.model.startGame(this.deck, false, 7, 3);
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(0)));
    this.model.validatePileMove(build, 6);
    Assert.assertTrue(true);
  }



  // tests for validateBuild
  @Test
  public void testValidateBuildOneCard() {
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(this.deck.get(20)));
    this.model.validateBuild(build);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateBuildCorrectValuesSameSuits() {
    ArrayList<Card> build = new ArrayList<>(this.deck.subList(0, 5));
    Collections.reverse(build);
    this.model.validateBuild(build);
    Assert.assertTrue(true);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidateBuildBackwardsBuild() {
    ArrayList<Card> build = new ArrayList<>(this.deck.subList(0, 5));
    this.model.validateBuild(build);
  }

  @Test(expected = IllegalStateException.class)
  public void testValidateBuildIncorrectValues() {
    ArrayList<Card> build = new ArrayList<>(Arrays.asList(
            this.deck.get(20), this.deck.get(15), this.deck.get(50)));
    this.model.validateBuild(build);
  }
}