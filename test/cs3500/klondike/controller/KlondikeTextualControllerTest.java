package cs3500.klondike.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import cs3500.klondike.mocks.MockAppendable;
import cs3500.klondike.mocks.MockKlondikeModel;
import cs3500.klondike.mocks.MockReadable;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;


/**
 * A test class for KlondikeTextualController.
 */
public class KlondikeTextualControllerTest {
  Readable in;
  Appendable out;
  KlondikeModel model;
  List<Card> deck;
  TextualView view;
  KlondikeTextualController controller;


  @Before
  public void setUp() {
    this.in = new StringReader("");
    this.out = new StringBuilder();
    this.model = new BasicKlondike();
    this.deck = this.model.getDeck();
    this.view = new KlondikeTextualView(this.model, this.out);
    this.controller = new KlondikeTextualController(this.in, this.out);
  }



  // helper methods
  void putCardAtIndex(String card, String suit, int idx) {
    for (int i = 0; i <= this.deck.size(); i++) {
      if (this.deck.get(i).toString().contains(card + suit)) {
        this.deck.set(i, this.deck.set(idx, this.deck.get(i)));
        break;
      }
    }
  }

  void interaction(String input, List<Card> deck, boolean shuffle, int numPiles, int numDraw) {
    this.in = new StringReader(input);
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(this.model, deck, shuffle, numPiles, numDraw);
  }




  // tests for constructor exceptions
  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullAppendable() {
    KlondikeController c = new KlondikeTextualController(new StringReader(""), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testControllerNullReadable() {
    KlondikeController c = new KlondikeTextualController(null, System.out);
  }



  // tests for playGame
  @Test(expected = IllegalArgumentException.class)
  public void testPlayGameNullModel() {
    this.controller.playGame(null, this.deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameNullDeck() {
    this.controller.playGame(this.model, null, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameInvalidDeck() {
    this.controller.playGame(this.model, this.deck.subList(0, 20),
            false,7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameTooManyPiles() {
    this.controller.playGame(this.model, this.deck, false, 10, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGame0Draws() {
    this.controller.playGame(this.model, this.deck, false, 7, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameNegativeDraws() {
    this.controller.playGame(this.model, this.deck, false, 7, -4);
  }

  @Test
  public void testPlayGameStartsGame() {
    this.interaction("q", this.deck, false, 7, 3);
    this.model.discardDraw();         // this will throw an error if the game is not started
    Assert.assertTrue(true); // so if it doesn't throw an error, we know the test passed
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameHandlesIOExceptionFromReadable() {
    this.controller = new KlondikeTextualController(new MockReadable(), this.out);
    this.controller.playGame(this.model, this.deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameHandlesIOExceptionFromAppendable() {
    this.in = new StringReader("q");
    this.controller = new KlondikeTextualController(this.in, new MockAppendable());
    this.controller.playGame(this.model, this.deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameNoInputs() {
    this.interaction("", this.deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameNoMoreInputsAfterMoves() {
    this.interaction("dd dd mpf 1 1", this.deck, false, 7, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameNoMoreInputsHalfwayThroughMove() {
    this.interaction("mpp 3 3", this.deck, false, 7, 3);
  }

  @Test
  public void testPlayGameDisplaysStateProperly() {
    this.putCardAtIndex("A", "", 0);
    this.interaction("mpf 1 1 q", this.deck,false, 7, 3);
    KlondikeModel newModel = new BasicKlondike();
    TextualView newView = new KlondikeTextualView(newModel);
    newModel.startGame(this.deck, false, 7, 3);
    String boardState1 = newView.toString();
    newModel.moveToFoundation(0, 0);
    String boardState2 = newView.toString();
    Assert.assertTrue(this.out.toString().contains(boardState1)
            && this.out.toString().contains(boardState2));
    // This shows that the controller doesn't display the same game state over and over, it updates
    // as you make moves
  }

  @Test
  public void testPlayGameQuitGameMessage() {
    this.interaction("q", this.deck, false, 7, 3);
    Assert.assertTrue(
            this.out.toString().contains("Game quit!\nState of game when quit:\n")
            && this.out.toString().contains("Score: 0"));
  }

  @Test
  public void testPlayGameQuitProperLines() {
    this.interaction("q", this.deck, false, 1, 3);
    Assert.assertEquals(10, this.out.toString().split("\n").length);
    // The state is 1 line for each of draw, foundation, cascade piles, and score. 4 total
    // The quit message is 2 lines, and then the state is displayed again. 4 + 2 + 4 = 10
  }

  @Test
  public void testPlayGameQuitAfterMove() {
    this.putCardAtIndex("A", "", 0);
    this.interaction("mpf 1 1 q", this.deck, false, 7, 3);
    Assert.assertTrue(
            this.out.toString().contains("Game quit!\nState of game when quit:\n")
                    && this.out.toString().contains("Score: 1"));
  }

  @Test
  public void testPlayGameQuitInMiddleOfCommand() {
    this.interaction("mpf 1 q", this.deck, false, 7, 3);
    Assert.assertTrue(
            this.out.toString().contains("Game quit!\nState of game when quit:\n")
                    && this.out.toString().contains("Score: 0"));
  }

  @Test
  public void testPlayGameQuitCapitalQ() {
    this.interaction("q", this.deck, false, 7, 3);
    Assert.assertTrue(
            this.out.toString().contains("Game quit!\nState of game when quit:\n")
                    && this.out.toString().contains("Score: 0"));
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayGameQuitTheWordQuit() {
    this.interaction("quit", this.deck, false, 7, 3);
  }

  @Test
  public void testPlayGameQuitWhenRetryingInput() {
    this.interaction("mpp 1 1 a q", this.deck, false, 7, 3);
    Assert.assertTrue(
            this.out.toString().contains("Game quit!\nState of game when quit:\n")
                    && this.out.toString().contains("Score: 0"));
  }

  @Test
  public void testPlayGameInvalidCommandAllCaps() {
    this.interaction("MDF q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("" +
            "Invalid move. Play again. Command not recognized."));
  }

  @Test
  public void testPlayGameInvalidCommandMisspelling() {
    this.interaction("mfd q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("" +
            "Invalid move. Play again. Command not recognized."));
  }

  @Test
  public void testPlayGameInvalidCommandSpelledOut() {
    this.interaction("discard-draw q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("" +
            "Invalid move. Play again. Command not recognized."));
  }

  @Test
  public void testPlayGameInvalidCommandInteger() {
    this.interaction("100100 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("" +
            "Invalid move. Play again. Command not recognized."));
  }

  @Test
  public void testPlayGameInvalidCommandGibberish() {
    this.interaction("!@#$%^&*( q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("" +
            "Invalid move. Play again. Command not recognized."));
  }

  @Test
  public void testPlayGameDDValidMoveAffectsModel() {
    KlondikeModel newModel = new BasicKlondike();
    newModel.startGame(this.deck, false, 7, 3);
    List<Card> drawCards = newModel.getDrawCards();
    this.interaction("dd q", this.deck, false, 7, 3);
    Assert.assertNotEquals(drawCards, this.model.getDrawCards());
  }

  @Test
  public void testPlayGameDDInvalidDisplaysMessage() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    this.controller = new KlondikeTextualController(new StringReader("dd q"), this.out);
    this.controller.playGame(newModel, this.deck.subList(0, 3),
            false, 2, 1);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameDDCallsCorrectMethod() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("dd q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertEquals("dd", mockModel.log.toString());
  }

  @Test
  public void testPlayGameMDValidMoveAffectsModel() {
    this.putCardAtIndex("K", "♢", 0);
    this.putCardAtIndex("Q", "♣", 1);
    this.interaction("md 1 q", this.deck, false, 1, 1);
    Assert.assertEquals("Q♣", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameMDInvalidMoveDisplaysMessage() {
    this.interaction("md 1 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameMDInvalidInputDisplaysMessage() {
    this.interaction("md a 3 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
  }

  @Test
  public void testPlayGameMDInvalidInputDisplaysMultipleMessage() {
    this.interaction("md a a a q", this.deck, false, 7, 3);
    int invalidMessages = (this.out.toString().length() -
            this.out.toString().replace("Input must be an integer. Try again.",
                    "").length()) / 36;
    Assert.assertEquals(3, invalidMessages);
  }

  @Test
  public void testPlayGameMDInvalidInputsFixedForValidMove() {
    this.putCardAtIndex("K", "♢", 0);
    this.putCardAtIndex("Q", "♣", 1);
    this.interaction("md one one one 1 q", this.deck,
            false, 1, 1);
    Assert.assertEquals("Q♣", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameMDCallsCorrectMethodOnModel() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("md 1 1 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("md"));
  }

  @Test
  public void testPlayGameMDUses0IndexingAndDoesntGarble() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("md 3 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("2"));
  }

  @Test
  public void testPlayGameMDStillCallsMethodWithNegativeInputs() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("md -3 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("-4"));
  }

  @Test
  public void testPlayGameMDCantInterruptCommandWithAnother() {
    this.putCardAtIndex("K", "♢", 0);
    this.putCardAtIndex("Q", "♣", 1);
    this.interaction("md dd 1 q", this.deck,
            false, 1, 1);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
    Assert.assertEquals("Q♣", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameMPFValidMoveAffectsModel() {
    this.putCardAtIndex("A", "♣", 0);
    this.interaction("mpf 1 4 q", this.deck, false, 3, 1);
    Assert.assertEquals("A♣", this.model.getCardAt(3).toString());
  }

  @Test
  public void testPlayGameMPFInvalidMoveDisplaysMessage() {
    this.interaction("mpf 6 4 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameMPFInvalidInputDisplaysMessage() {
    this.interaction("mpf help 1 1 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
  }

  @Test
  public void testPlayGameMPFInvalidInputDisplaysMultipleMessage() {
    this.interaction("mpf the more tests i write the more pain i feel q",
            this.deck, false, 7, 3);
    int invalidMessages = (this.out.toString().length() -
            this.out.toString().replace("Input must be an integer. Try again.",
                    "").length()) / 36;
    Assert.assertEquals(10, invalidMessages);
  }

  @Test
  public void testPlayGameMPFInvalidInputsFixedForValidMove() {
    this.putCardAtIndex("A", "♣", 0);
    this.interaction("mpf zippidee 1 doo da 3 q", this.deck,
            false, 7, 3);
    Assert.assertEquals("A♣", this.model.getCardAt(2).toString());
  }

  @Test
  public void testPlayGameMPFCallsCorrectMethodOnModel() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mpf 3 3 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("mpf"));
  }

  @Test
  public void testPlayGameMPFUses0IndexingAndDoesntGarble() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mpf 10 10 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("9 9"));
  }

  @Test
  public void testPlayGameMPFStillCallsMethodWithNegativeInputs() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mpf -5 0 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("-6 -1"));
  }

  @Test
  public void testPlayGameMPFCantInterruptCommandWithAnother() {
    this.putCardAtIndex("A","♣", 0);
    this.interaction("mpf dd mpp 1 2 q", this.deck,
            false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
    Assert.assertEquals("A♣", this.model.getCardAt(1).toString());
  }

  @Test
  public void testPlayGameMDFValidMoveAffectsModel() {
    this.putCardAtIndex("A", "♣", 1);
    this.interaction("mdf 1 q", this.deck, false, 1, 1);
    Assert.assertEquals("A♣", this.model.getCardAt(0).toString());
  }

  @Test
  public void testPlayGameMDFInvalidMoveDisplaysMessage() {
    this.interaction("mdf -2 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameMDFInvalidInputDisplaysMessage() {
    this.interaction("mdf help 1 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
  }

  @Test
  public void testPlayGameMDFInvalidInputDisplaysMultipleMessage() {
    this.interaction("mdf blah blah blah q", this.deck, false, 7, 3);
    int invalidMessages = (this.out.toString().length() -
            this.out.toString().replace("Input must be an integer. Try again.",
                    "").length()) / 36;
    Assert.assertEquals(3, invalidMessages);
  }

  @Test
  public void testPlayGameMDFInvalidInputsFixedForValidMove() {
    this.putCardAtIndex("A", "♣", 1);
    this.interaction("mdf one two three 2 q", this.deck,
            false, 1, 1);
    Assert.assertEquals("A♣", this.model.getCardAt(1).toString());
  }

  @Test
  public void testPlayGameMDFCallsCorrectMethodOnModel() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mdf 3 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("mdf"));
  }

  @Test
  public void testPlayGameMDFUses0IndexingAndDoesntGarble() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mdf 2 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("1"));
  }

  @Test
  public void testPlayGameMDFStillCallsMethodWithNegativeInputs() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mdf -1 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("-2"));
  }

  @Test
  public void testPlayGameMDFCantInterruptCommandWithAnother() {
    this.putCardAtIndex("A","♣", 1);
    this.interaction("mdf dd 3 q", this.deck,
            false, 1, 1);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
    Assert.assertEquals("A♣", this.model.getCardAt(2).toString());
  }

  @Test
  public void testPlayGameMPPValidMoveAffectsModel() {
    this.putCardAtIndex("3", "♣", 0);
    this.putCardAtIndex("2", "♡", 2);
    this.interaction("mpp 2 1 1 q", this.deck, false, 2, 100);
    Assert.assertEquals("3♣", this.model.getCardAt(0, 0).toString());
    Assert.assertEquals("2♡", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameMPPInvalidMoveDisplaysMessage() {
    this.interaction("mpp -2 1 2 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameMPPInvalidInputDisplaysMessage() {
    this.interaction("mpp help 1 1 1 q", this.deck, false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
  }

  @Test
  public void testPlayGameMPPInvalidInputDisplaysMultipleMessage() {
    this.interaction("mpp im 2 really getting 2 tired of this 5 q",
            this.deck, false, 7, 3);
    int invalidMessages = (this.out.toString().length() -
            this.out.toString().replace("Input must be an integer. Try again.",
                    "").length()) / 36;
    Assert.assertEquals(6, invalidMessages);
  }

  @Test
  public void testPlayGameMPPInvalidInputsFixedForValidMove() {
    this.putCardAtIndex("J", "♣", 0);
    this.putCardAtIndex("10", "♡", 2);
    this.interaction("mpp help me 2 im dying 1 inside 1 q", this.deck,
            false, 2, 4);
    Assert.assertEquals("J♣", this.model.getCardAt(0, 0).toString());
    Assert.assertEquals("10♡", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameMPPCallsCorrectMethodOnModel() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mpp 3 2 1 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("mpp"));
  }

  @Test
  public void testPlayGameMPPUses0IndexingAndDoesntGarble() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mpp 6 3 5 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("5 3 4"));
  }

  @Test
  public void testPlayGameMPPStillCallsMethodWithNegativeInputs() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.controller = new KlondikeTextualController(new StringReader("mpp -4 0 5 q"), this.out);
    this.controller.playGame(mockModel, this.deck, false, 7, 3);
    Assert.assertTrue(mockModel.log.toString().contains("-5 0 4"));
  }

  @Test
  public void testPlayGameMPPCantInterruptCommandWithAnother() {
    this.putCardAtIndex("2", "♣", 0);
    this.putCardAtIndex("A", "♡", 2);
    this.interaction("mpp dd 2 md 1 mdf 1 q", this.deck,
            false, 2, 1);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer. Try again."));
    Assert.assertEquals("2♣", this.model.getCardAt(0, 0).toString());
    Assert.assertEquals("A♡", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameCanStillMoveAfterInvalidMoves() {
    this.putCardAtIndex("4", "♣", 0);
    this.putCardAtIndex("3", "♡", 5);
    this.interaction("mpf 1 1 mdf 2 mpp 1 1 3 mpp 3 1 1 q", this.deck,
            false, 3, 1); // everything is invalid except for mpp 3 1 1
    Assert.assertEquals("4♣", this.model.getCardAt(0, 0).toString());
    Assert.assertEquals("3♡", this.model.getCardAt(0, 1).toString());
  }

  @Test
  public void testPlayGameCanMakeMultipleMoves() {
    this.putCardAtIndex("7", "♠", 0);
    this.putCardAtIndex("A", "♣", 4);
    this.putCardAtIndex("9", "♣", 3);
    this.putCardAtIndex("8", "♡", 5);
    this.interaction("mpp 1 1 3 mpp 3 2 2 mpf 3 1 q", this.deck,
            false, 3, 1);
    // 7 is moved onto the eight, and both are moved onto the 9, which reveals the ace which is
    // moved in foundation pile 1 (index 0)
    Assert.assertEquals("9♣", this.model.getCardAt(1, 1).toString());
    Assert.assertEquals("8♡", this.model.getCardAt(1, 2).toString());
    Assert.assertEquals("7♠", this.model.getCardAt(1, 3).toString());
    Assert.assertEquals("A♣", this.model.getCardAt(0).toString());
  }

  @Test
  public void testPlayGameMoveNonKingOntoEmptyPile() {
    this.putCardAtIndex("A", "♠", 0);
    this.putCardAtIndex("Q", "♣", 3);
    this.interaction("mpf 1 1 mpp 2 1 1 q", this.deck,
            false, 3, 1);
    // move the ace to a foundation pile, then try to move the queen onto the empty pile
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameMoveWriteValueOntoWrongSuit() {
    this.putCardAtIndex("Q", "♣", 0);
    this.putCardAtIndex("J", "♠", 1);
    this.interaction("md 1 q", this.deck, false, 1, 1);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameMoveSomeOfPileOntoAnother() {
    this.putCardAtIndex("5", "♣", 0);
    this.putCardAtIndex("4", "♡", 4);
    this.putCardAtIndex("3", "♣", 7);
    this.putCardAtIndex("5", "♠", 9);
    this.interaction("mpp 3 1 2 mpp 2 2 1 mpp 1 2 4 q", this.deck,
            false, 4, 1);
    Assert.assertEquals("5♣", this.model.getCardAt(0, 0).toString());
    Assert.assertEquals("5♠", this.model.getCardAt(3, 3).toString());
    Assert.assertEquals("4♡", this.model.getCardAt(3, 4).toString());
    Assert.assertEquals("3♣", this.model.getCardAt(3, 5).toString());
  }

  @Test
  public void testPlayGameMoveKingWithMoreToEmpty() {
    this.putCardAtIndex("A", "♣", 0);
    this.putCardAtIndex("Q", "♡", 4);
    this.putCardAtIndex("K", "♣", 7);
    this.putCardAtIndex("J", "♠", 9);
    this.interaction("mpf 1 4 mpp 4 1 2 mpp 2 2 3 mpp 3 3 1 q", this.deck,
            false, 4, 1);
    Assert.assertEquals("A♣", this.model.getCardAt(3).toString());
    Assert.assertEquals("K♣", this.model.getCardAt(0, 0).toString());
    Assert.assertEquals("Q♡", this.model.getCardAt(0, 1).toString());
    Assert.assertEquals("J♠", this.model.getCardAt(0, 2).toString());
  }

  @Test
  public void testPlayGameMoveAceOntoTwo() {
    this.putCardAtIndex("A", "♣", 0);
    this.putCardAtIndex("2", "♡", 2);
    this.interaction("mpp 1 1 2 q", this.deck,
            false, 2, 1);
    Assert.assertEquals("2♡", this.model.getCardAt(1, 1).toString());
    Assert.assertEquals("A♣", this.model.getCardAt(1, 2).toString());
  }

  @Test
  public void testPlayGameMoveSomeColorWrongSuitFoundation() {
    this.putCardAtIndex("A", "♣", 0);
    this.putCardAtIndex("2", "♠", 2);
    this.interaction("mpf 1 2 mpf 1 2 q", this.deck,
            false, 2, 1);
    Assert.assertTrue(this.out.toString().contains("Invalid move. Play again."));
  }

  @Test
  public void testPlayGameValidMoveLineCount() {
    this.putCardAtIndex("Q", "♣", 0);
    this.putCardAtIndex("J", "♡", 1);
    this.interaction("md 1 q", this.deck, false, 1, 1);
    Assert.assertEquals(16, this.out.toString().split("\n").length);
  }

  @Test
  public void testPlayGameInvalidMoveLineCount() {
    this.putCardAtIndex("2", "♣", 6);
    this.interaction("mdf 3 q", this.deck, false, 3, 1);
    Assert.assertEquals(21, this.out.toString().split("\n").length);
  }

  @Test
  public void testPlayGameInvalidAndValidMovesLineCount() {
    this.putCardAtIndex("Q", "♣", 0);
    this.putCardAtIndex("J", "♡", 3);
    this.putCardAtIndex("2", "♣", 6);
    this.interaction("mpp 2 1 1 mdf 3 q", this.deck, false, 3, 1);
    Assert.assertEquals(27, this.out.toString().split("\n").length);
  }





  // tests for validateGame
  @Test
  public void testValidateGameNullModel() {
    try {
      this.controller.validateGame(null, this.deck, false, 7, 3);
    }
    catch (IllegalArgumentException e) {
      Assert.assertEquals("Cannot play game with a null model.", e.getMessage());
    }
  }

  @Test
  public void testValidateGameGameAlreadyStarted() {
    this.model.startGame(this.deck, false, 7, 3);
    try {
      this.controller.validateGame(this.model, this.deck, false, 7, 3);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("Game already started.", e.getMessage());
    }
  }

  @Test
  public void testValidateGameNullDeck() {
    try {
      this.controller.validateGame(this.model, null, false, 7, 3);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("Invalid deck.", e.getMessage());
    }
  }

  @Test
  public void testValidateGameInvalidDeck() {
    try {
      this.controller.validateGame(this.model, this.deck.subList(0, 20),
              false,7, 3);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("Invalid deck.", e.getMessage());
    }
  }

  @Test
  public void testValidateGameTooManyPiles() {
    try {
      this.controller.validateGame(this.model, this.deck, false, 10, 3);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("Number of cascade piles is impossible.", e.getMessage());
    }
  }

  @Test
  public void testValidateGame0Draws() {
    try {
      this.controller.validateGame(this.model, this.deck, false, 7, 0);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("Number of draw cards must be a positive number.",
              e.getMessage());
    }
  }

  @Test
  public void testValidateGameNegativeDraws() {
    try {
      this.controller.validateGame(this.model, this.deck, false, 7, -4);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("Number of draw cards must be a positive number.",
              e.getMessage());
    }
  }

  @Test
  public void testValidateGameValidGame() {
    this.controller.validateGame(this.model, this.deck, false, 7, 3);
    Assert.assertTrue(true);
  }

  @Test
  public void testValidateGameValidGame2() {
    this.controller.validateGame(this.model, this.deck.subList(0, 26),
            true, 1, 15);
    Assert.assertTrue(true);
  }





  // tests for getCommandStr
  @Test
  public void testGetCommandStrNoInputs() {
    try {
      this.controller.getCommandString();
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("No more inputs left.", e.getMessage());
    }
  }

  @Test
  public void testGetCommandStrGetsNextInput() {
    this.in = new StringReader("hi bye");
    KlondikeTextualController newController = new KlondikeTextualController(this.in, this.out);
    Assert.assertEquals("hi", newController.getCommandString());
  }

  @Test
  public void testGetCommandStrq() {
    this.in = new StringReader("q");
    KlondikeTextualController newController = new KlondikeTextualController(this.in, this.out);
    Assert.assertEquals("q", newController.getCommandString());
  }

  @Test
  public void testGetCommandStrMPP() {
    this.in = new StringReader("mpp");
    KlondikeTextualController newController = new KlondikeTextualController(this.in, this.out);
    Assert.assertEquals("mpp", newController.getCommandString());
  }

  @Test
  public void testGetCommandStrLiterallyNull() {
    this.in = new StringReader("null");
    KlondikeTextualController newController = new KlondikeTextualController(this.in, this.out);
    Assert.assertEquals("null", newController.getCommandString());
  }

  @Test
  public void testGetCommandStrInt() {
    this.in = new StringReader("3");
    KlondikeTextualController newController = new KlondikeTextualController(this.in, this.out);
    Assert.assertEquals("3", newController.getCommandString());
  }





  // tests for parseCommand
  @Test
  public void testParseCommandq() {
    Assert.assertEquals(ControllerStatus.QUIT, this.controller.parseCommand("q"));
  }

  @Test
  public void testParseCommandQ() {
    Assert.assertEquals(ControllerStatus.QUIT, this.controller.parseCommand("q"));
  }

  @Test
  public void testParseCommandDD() {
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.parseCommand("dd"));
  }

  @Test
  public void testParseCommandMD() {
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.parseCommand("md"));
  }

  @Test
  public void testParseCommandMDF() {
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.parseCommand("mdf"));
  }

  @Test
  public void testParseCommandMPF() {
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.parseCommand("mpf"));
  }

  @Test
  public void testParseCommandMPP() {
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.parseCommand("mpp"));
  }

  @Test(expected = NullPointerException.class)
  public void testParseCommandNull() {
    this.controller.parseCommand(null);
  }

  @Test
  public void testParseCommandNumber() {
    Assert.assertEquals(ControllerStatus.INVALID, this.controller.parseCommand("11"));
  }

  @Test
  public void testParseCommandMisspelling() {
    Assert.assertEquals(ControllerStatus.INVALID, this.controller.parseCommand("mp"));
  }

  @Test
  public void testParseCommandQuitFullWord() {
    Assert.assertEquals(ControllerStatus.INVALID, this.controller.parseCommand("quit"));
  }





  // tests for gameOver
  @Test
  public void testIsGameOverNoItsNot() {
    this.interaction("q", this.deck, false, 7, 3);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.isGameOver(52));
  }

  @Test
  public void testIsGameOverLostReturnsQuit() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 2);
    this.putCardAtIndex("2", "♠", 3);
    this.putCardAtIndex("A", "♢", 4);
    this.putCardAtIndex("2", "♢", 5);
    KlondikeModel newModel = new BasicKlondike();
    this.controller.playGame(newModel, this.deck.subList(0, 6),
            false, 3, 3);
    Assert.assertEquals(ControllerStatus.QUIT, this.controller.isGameOver(6));
  }

  @Test
  public void testIsGameOverLostDisplaysMessage() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 2);
    this.putCardAtIndex("2", "♠", 3);
    this.putCardAtIndex("A", "♢", 4);
    this.putCardAtIndex("2", "♢", 5);
    KlondikeModel newModel = new BasicKlondike();
    this.controller.playGame(newModel, this.deck.subList(0, 6),
            false, 3, 3);
    this.controller.isGameOver(6);
    Assert.assertTrue(this.out.toString().contains("Game over. Score: 0"));
  }

  @Test
  public void testIsGameOverScoreNot0() {
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 2);
    this.putCardAtIndex("2", "♠", 3);
    this.putCardAtIndex("A", "♢", 4);
    this.putCardAtIndex("2", "♢", 5);
    this.putCardAtIndex("A", "♣", 6);
    this.putCardAtIndex("2", "♣", 7);
    this.interaction("mdf 1 mdf 1", this.deck.subList(0, 8),
            false, 3, 3);
    this.controller.isGameOver(8);
    Assert.assertTrue(this.out.toString().contains("Game over. Score: 2"));
  }

  @Test
  public void testIsGameOverWonReturnsQuit() {
    this.putCardAtIndex("A", "", 0);
    this.interaction("mpf 1 1", this.deck.subList(0, 1),
            false, 1, 3);
    Assert.assertEquals(ControllerStatus.QUIT, this.controller.isGameOver(1));
  }

  @Test
  public void testIsGameOverWonDisplaysMessage() {
    this.putCardAtIndex("A", "", 0);
    this.interaction("mpf 1 1", this.deck.subList(0, 1),
            false, 1, 3);
    Assert.assertTrue(this.out.toString().contains("You win!"));
  }

  @Test
  public void testIsGameOverNegativeMaxScore() {
    this.interaction("q", this.deck, false, 7, 3);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.isGameOver(-1));
  }

  @Test
  public void testIsGameOver0MaxScore() {
    this.interaction("q", this.deck, false, 7, 3);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.controller.isGameOver(0));
  }

  @Test
  public void testIsGameOverWonButHighMaxScore() {
    this.putCardAtIndex("A", "", 0);
    this.interaction("mpf 1 1", this.deck.subList(0, 1),
            false, 1, 3);
    Assert.assertEquals(ControllerStatus.QUIT, this.controller.isGameOver(1000));
  }

  @Test
  public void testIsGameOverWonButHighMaxScoreMessage() {
    this.putCardAtIndex("A", "", 0);
    this.interaction("mpf 1 1", this.deck.subList(0, 1),
            false, 1, 3);
    this.controller.isGameOver(1000);
    Assert.assertTrue(this.out.toString().contains("Game over."));
  }



  // tests using LimitedDrawKlondike
  @Test
  public void testLimitedDrawRunOutOfCards() {
    String input = "";
    for (int discards = 0; discards < 25; discards++) {
      input += "dd ";
    }

    this.in = new StringReader(input + "q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new LimitedDrawKlondike(0), this.deck,
            false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("No cards left in the draw pile."));
  }

  @Test
  public void testWhiteheadMoveAnyCardIntoEmptyPile() {
    this.in = new StringReader("mpf 1 1 mpp 7 1 1 q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new WhiteheadKlondike(), this.deck,
            false, 7, 3);
    Assert.assertFalse(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testWhiteheadMovePileWrongSuit() {
    this.in = new StringReader("mpf 3 1 mpp 3 1 5 q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new WhiteheadKlondike(), this.deck,
            false, 7, 3);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testWhiteheadMovePileValidMove() {
    this.in = new StringReader("mpf 1 1 mpp 7 1 1 mpf 7 2 mpp 7 1 6 q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new WhiteheadKlondike(), this.deck,
            false, 7, 3);
    Assert.assertFalse(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testWhiteheadCantMoveBuildWithDifferentSuits() {
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♡", 1);
    this.putCardAtIndex("J", "♢", 2);
    this.in = new StringReader("mpp 2 2 1 q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new WhiteheadKlondike(), this.deck,
            false, 2, 3);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testWhiteheadProperlyMovingMultipleCards() {
    this.putCardAtIndex("K", "♡", 0);
    this.putCardAtIndex("Q", "♡", 1);
    this.putCardAtIndex("J", "♡", 2);
    this.in = new StringReader("mpp 2 2 1 q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new WhiteheadKlondike(), this.deck,
            false, 2, 3);
    Assert.assertFalse(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testBasicPlayedToCompletion() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("5", "♡", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.putCardAtIndex("2", "♡", 3);
    this.putCardAtIndex("4", "♡", 4);
    this.putCardAtIndex("3", "♡", 5);
    this.putCardAtIndex("7", "♡", 6);
    this.in = new StringReader("mpf 1 1 mpf 2 1 mpf 3 1 mpf 3 1 mpf 2 1 mpf 3 1 mdf 1 q");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new BasicKlondike(), this.deck.subList(0, 7),
            false, 3, 3);
    Assert.assertTrue(this.out.toString().contains("You win"));
  }

  @Test
  public void testLimitedPlayedToCompletion() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("5", "♡", 1);
    this.putCardAtIndex("6", "♡", 2);
    this.putCardAtIndex("2", "♡", 3);
    this.putCardAtIndex("4", "♡", 4);
    this.putCardAtIndex("3", "♡", 5);
    this.putCardAtIndex("7", "♡", 6);
    this.in = new StringReader("mpf 1 1 mpf 2 1 mpf 3 1 mpf 3 1 mpf 2 1 mpf 3 1 mdf 1");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new LimitedDrawKlondike(2), this.deck.subList(0, 7),
            false, 3, 3);
    Assert.assertTrue(this.out.toString().contains("You win"));
  }

  @Test
  public void testLimitedDrawCanOnlyDiscardUntilLost() {
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    this.putCardAtIndex("4", "♡", 3);
    this.in = new StringReader("dd dd dd dd");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new LimitedDrawKlondike(3), this.deck.subList(0, 7),
            false, 3, 3);
    Assert.assertTrue(this.out.toString().contains("Game over."));
  }

  @Test
  public void testWhiteheadPlayedToCompletion() {
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    this.putCardAtIndex("4", "♡", 3);
    this.putCardAtIndex("5", "♡", 4);
    this.putCardAtIndex("6", "♡", 5);
    this.putCardAtIndex("7", "♡", 6);
    this.in = new StringReader("mpf 1 1 mpp 2 1 1 mpf 2 1 mpp 3 1 2 mpp 1 1 3 mpp 3 2 1 " +
            "mpf 3 1 mpf 1 1 mpf 1 1 mpf 2 1 mdf 1");
    this.controller = new KlondikeTextualController(this.in, this.out);
    this.controller.playGame(new WhiteheadKlondike(), this.deck.subList(0, 7),
            false, 3, 3);
    Assert.assertTrue(this.out.toString().contains("You win"));
  }
}