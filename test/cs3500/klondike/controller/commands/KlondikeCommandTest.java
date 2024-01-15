package cs3500.klondike.controller.commands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Scanner;

import cs3500.klondike.controller.ControllerStatus;
import cs3500.klondike.mocks.MockAppendable;
import cs3500.klondike.mocks.MockKlondikeModel;
import cs3500.klondike.mocks.MockTextualView;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;


/**
 * A class that holds tests for KlondikeCommand.
 */
public class KlondikeCommandTest {
  KlondikeCommand dd;
  KlondikeCommand md;
  KlondikeCommand mdf;
  KlondikeCommand mpf;
  KlondikeCommand mpp;
  Scanner scanner;
  KlondikeModel model;
  List<Card> deck;
  Appendable out;
  TextualView view;

  @Before
  public void setUp() {
    this.scanner = new Scanner("");
    this.dd = new DDCommand(this.scanner);
    this.md = new MDCommand(this.scanner);
    this.mdf = new MDFCommand(this.scanner);
    this.mpf = new MPFCommand(this.scanner);
    this.mpp = new MPPCommand(this.scanner);
    this.model = new BasicKlondike();
    this.deck = this.model.getDeck();
    this.model.startGame(this.deck, false, 7, 3);
    this.out = new StringBuilder();
    this.view = new KlondikeTextualView(this.model, this.out);
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


  // tests for DDCommand
  @Test(expected = NullPointerException.class)
  public void testDDNullModel() {
    this.dd.run(null, this.view, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testDDNullView() {
    this.dd.run(this.model, null, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testDDNullOut() {
    this.dd.run(this.model, this.view, null);
  }

  @Test
  public void testDDValidMoveAffectsModel() {
    List<Card> drawCards = this.model.getDrawCards();
    this.dd.run(this.model, this.view, this.out);
    Assert.assertNotEquals(drawCards, this.model.getDrawCards());
  }

  @Test
  public void testDDInvalidMoveDisplaysMessage() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    newModel.startGame(this.deck.subList(0, 3), false, 2, 1);
    this.dd.run(newModel, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testDDRightMethodIsCalledOnModel() {
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.dd.run(mockModel, this.view, this.out);
    Assert.assertEquals("dd", mockModel.log.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testDDMockOutThrowsIOError() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    newModel.startGame(this.deck.subList(0, 3), false, 2, 1);
    Appendable mockOut = new MockAppendable();
    this.dd.run(newModel, this.view, mockOut);
  }

  @Test
  public void testDDRightViewerIsUsed() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    newModel.startGame(this.deck.subList(0, 3), false, 2, 1);
    TextualView newView = new KlondikeTextualView(newModel, this.out);
    this.dd.run(newModel, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains(
            "Draw: 3♢, 4♢, 5♢\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡  ?  ?  ?  ?  ?  ?\n" +
            "    8♡  ?  ?  ?  ?  ?\n" +
            "       A♠  ?  ?  ?  ?\n" +
            "          6♠  ?  ?  ?\n" +
            "            10♠  ?  ?\n" +
            "                K♠  ?\n" +
            "                   2♢"));
  }

  @Test
  public void testDDReturnContinue() {
    Assert.assertEquals(ControllerStatus.CONTINUE, this.dd.run(this.model, this.view, this.out));
  }

  @Test
  public void testDDReturnInvalid() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("A", "♡", 0);
    this.putCardAtIndex("2", "♡", 1);
    this.putCardAtIndex("3", "♡", 2);
    newModel.startGame(this.deck.subList(0, 3), false, 2, 1);
    Assert.assertEquals(ControllerStatus.INVALID, this.dd.run(newModel, this.view, this.out));
  }



  // tests for MDCommand
  @Test(expected = NullPointerException.class)
  public void testMDNullModel() {
    this.md.run(null, this.view, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMDNullView() {
    this.md.run(this.model, null, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMDNullOut() {
    this.md.run(this.model, this.view, null);
  }

  @Test
  public void testMDValidMoveAffectsModel() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 1);
    newModel.startGame(this.deck, false, 1, 1);
    this.scanner = new Scanner("1");
    this.md = new MDCommand(this.scanner);
    Card two = newModel.getDrawCards().get(0);
    this.md.run(newModel, this.view, this.out);
    Assert.assertEquals(two, newModel.getCardAt(0, 1));
  }

  @Test
  public void testMDInvalidMoveDisplaysMessage() {
    this.scanner = new Scanner("3");
    this.md = new MDCommand(this.scanner);
    this.md.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testMDCantInterruptWithAnotherCommand() {
    this.scanner = new Scanner("dd 1");
    this.md = new MDCommand(this.scanner);
    List<Card> draws = this.model.getDrawCards();
    this.md.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
    Assert.assertEquals(draws, this.model.getDrawCards());
  }

  @Test
  public void testMDInvalidInputDisplaysMessage() {
    this.scanner = new Scanner("a a a a a 1");
    this.md = new MDCommand(this.scanner);
    this.md.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
  }

  @Test
  public void testMDQuitStatusAfterFirstInput() {
    this.scanner = new Scanner("q");
    this.md = new MDCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.md.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDQuitStatusAfterInvalid() {
    this.scanner = new Scanner("aaaaahhhhh im gonna quit!!!! im gonna do it omg q");
    this.md = new MDCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.md.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDContinueStatusCorrectInputs() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 1);
    newModel.startGame(this.deck, false, 1, 1);
    this.scanner = new Scanner("1");
    this.md = new MDCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.md.run(newModel, this.view, this.out));
  }


  @Test
  public void testMDContinueStatusFixedInputs() {
    KlondikeModel newModel = new BasicKlondike();
    this.putCardAtIndex("2", "♡", 0);
    this.putCardAtIndex("A", "♠", 1);
    newModel.startGame(this.deck, false, 1, 1);
    this.scanner = new Scanner("this is not right but up next is a 1");
    this.md = new MDCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.md.run(newModel, this.view, this.out));
  }

  @Test
  public void testMDInvalidStatusCorrectInputs() {
    this.scanner = new Scanner("3");
    this.md = new MDCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.md.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDInvalidStatusFixedInput() {
    this.scanner = new Scanner("lets try this move 1");
    this.md = new MDCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.md.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDUses0IndexAndDoesntGarble() {
    this.scanner = new Scanner("3");
    this.md = new MDCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.md.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("2"));
  }

  @Test
  public void testMDStillSendsNegativeIntegers() {
    this.scanner = new Scanner("-10");
    this.md = new MDCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.md.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("-11"));
  }

  @Test
  public void testMDCallsCorrectMethod() {
    this.scanner = new Scanner("1");
    this.md = new MDCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.md.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("md"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMDMockOutThrowsIOError() {
    Appendable mockOut = new MockAppendable();
    this.md.run(this.model, this.view, mockOut);
  }

  @Test(expected = IllegalStateException.class)
  public void testDClosedScannerThrowsIOError() {
    this.scanner = new Scanner("1 1 1");
    this.md = new MDCommand(this.scanner);
    this.scanner.close();
    this.md.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMDNoInputsThrowsIOError() {
    this.scanner = new Scanner("");
    this.md = new MDCommand(this.scanner);
    this.md.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMDNotEnoughInputsThrowsIOError() {
    this.scanner = new Scanner("idk what to do lol");
    this.md = new MDCommand(this.scanner);
    this.md.run(this.model, this.view, this.out);
  }

  @Test
  public void testMDCorrectViewIsUsed() {
    this.scanner = new Scanner("1");
    this.md = new MDCommand(this.scanner);
    TextualView mockView = new MockTextualView(this.out);
    this.md.run(this.model, mockView, this.out);
    Assert.assertTrue(this.out.toString().contains("Mock view"));
  }



  // tests for MDFCommand
  @Test(expected = NullPointerException.class)
  public void testMDFNullModel() {
    this.mdf.run(null, this.view, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMDFNullView() {
    this.mdf.run(this.model, null, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMDFNullOut() {
    this.mdf.run(this.model, this.view, null);
  }

  @Test
  public void testMDFValidMoveAffectsModel() {
    this.putCardAtIndex("A", "", 1);
    KlondikeModel newModel = new BasicKlondike();
    newModel.startGame(this.deck, false, 1, 3);
    this.scanner = new Scanner("1");
    this.mdf = new MDFCommand(this.scanner);
    this.mdf.run(newModel, this.view, this.out);
    Assert.assertTrue(newModel.getCardAt(0).toString().contains("A"));
  }

  @Test
  public void testMDFInvalidMoveDisplaysMessage() {
    this.scanner = new Scanner("1");
    this.mdf = new MDFCommand(this.scanner);
    this.mdf.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testMDFCantInterruptWithAnotherCommand() {
    this.scanner = new Scanner("dd 1 1 1");
    this.mdf = new MDFCommand(this.scanner);
    List<Card> draws = this.model.getDrawCards();
    this.mdf.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
    Assert.assertEquals(draws, this.model.getDrawCards());
  }

  @Test
  public void testMDFInvalidInputDisplaysMessage() {
    this.scanner = new Scanner("a a 1");
    this.mdf = new MDFCommand(this.scanner);
    this.mdf.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
  }

  @Test
  public void testMDFQuitStatusAfterFirstInput() {
    this.scanner = new Scanner("q");
    this.mdf = new MDFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mdf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDFQuitStatusAfterInvalid() {
    this.scanner = new Scanner("a q");
    this.mdf = new MDFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mdf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDFContinueStatusCorrectInputs() {
    this.putCardAtIndex("A", "", 1);
    KlondikeModel newModel = new BasicKlondike();
    newModel.startGame(this.deck, false, 1, 3);
    this.scanner = new Scanner("1");
    this.mdf = new MDFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.mdf.run(newModel, this.view, this.out));
  }

  @Test
  public void testMDFContinueStatusFixedInputs() {
    this.putCardAtIndex("A", "", 1);
    KlondikeModel newModel = new BasicKlondike();
    newModel.startGame(this.deck, false, 1, 3);
    this.scanner = new Scanner("help idk what to do, ig ill just type a random number 1");
    this.mdf = new MDFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.mdf.run(newModel, this.view, this.out));
  }

  @Test
  public void testMDFInvalidStatusCorrectInputs() {
    this.scanner = new Scanner("3");
    this.mdf = new MDFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.mdf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDFInvalidStatusFixedInputs() {
    this.scanner = new Scanner("idk lets just try 6");
    this.mdf = new MDFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.mdf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMDFUses0IndexAndDoesntGarble() {
    this.scanner = new Scanner("5");
    this.mdf = new MDFCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mdf.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("4"));
  }

  @Test
  public void testMDFStillSendsNegativeIntegers() {
    this.scanner = new Scanner("-5");
    this.mdf = new MDFCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mdf.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("-6"));
  }

  @Test
  public void testMDFCallsCorrectMethod() {
    this.scanner = new Scanner("8");
    this.mdf = new MDFCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mdf.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("mdf"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMDFMockOutThrowsIOError() {
    Appendable mockOut = new MockAppendable();
    this.mdf.run(this.model, this.view, mockOut);
  }

  @Test(expected = IllegalStateException.class)
  public void testMDFClosedScannerThrowsIOError() {
    this.scanner = new Scanner("1 1 1");
    this.mdf = new MPPCommand(this.scanner);
    this.scanner.close();
    this.mdf.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMDFNoInputsThrowsIOError() {
    this.scanner = new Scanner("");
    this.mdf = new MDFCommand(this.scanner);
    this.mdf.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMDFNotEnoughInputsThrowsIOError() {
    this.scanner = new Scanner("hi");
    this.mdf = new MDFCommand(this.scanner);
    this.mdf.run(this.model, this.view, this.out);
  }

  @Test
  public void testMDFCorrectViewIsUsed() {
    this.scanner = new Scanner("1 1 1");
    this.mdf = new MPPCommand(this.scanner);
    TextualView mockView = new MockTextualView(this.out);
    this.mdf.run(this.model, mockView, this.out);
    Assert.assertTrue(this.out.toString().contains("Mock view"));
  }



  // tests for MPFCommand
  @Test(expected = NullPointerException.class)
  public void testMPFNullModel() {
    this.mpf.run(null, this.view, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMPFNullView() {
    this.mpf.run(this.model, null, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMPFNullOut() {
    this.mpf.run(this.model, this.view, null);
  }

  @Test
  public void testMPFValidMoveAffectsModel() {
    this.scanner = new Scanner("1 2");
    this.mpf = new MPFCommand(this.scanner);
    Card ace = this.model.getCardAt(0, 0);
    this.mpf.run(this.model, this.view, this.out);
    Assert.assertEquals(ace, this.model.getCardAt(1));
  }

  @Test
  public void testMPFInvalidMoveDisplaysMessage() {
    this.scanner = new Scanner("2 3");
    this.mpf = new MPFCommand(this.scanner);
    this.mpf.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testMPFCantInterruptWithAnotherCommand() {
    this.scanner = new Scanner("6 dd 4");
    this.mpf = new MPFCommand(this.scanner);
    List<Card> draws = this.model.getDrawCards();
    this.mpf.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
    Assert.assertEquals(draws, this.model.getDrawCards());
  }

  @Test
  public void testMPFInvalidInputDisplaysMessage() {
    this.scanner = new Scanner("hi there 1 1");
    this.mpf = new MPFCommand(this.scanner);
    this.mpf.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
  }

  @Test
  public void testMPFQuitStatusAfterFirstInput() {
    this.scanner = new Scanner("q");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFQuiStatusAfterInvalid() {
    this.scanner = new Scanner("a q");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFQuitStatusAfterValidAndInvalid() {
    this.scanner = new Scanner("hi 1 there q");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFContinueStatusCorrectInputs() {
    this.scanner = new Scanner("1 2");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFContinueStatusFixedInputs() {
    this.scanner = new Scanner("i 1 four 4");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFInvalidStatusCorrectInputs() {
    this.scanner = new Scanner("2 7");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFInvalidStatusFixedInputs() {
    this.scanner = new Scanner("ooh lets try 1 and 10");
    this.mpf = new MPFCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.mpf.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPFUses0IndexAndDoesntGarble() {
    this.scanner = new Scanner("1 1");
    this.mpf = new MPFCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mpf.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("0 0"));
  }

  @Test
  public void testMPFStillSendsNegativeIntegers() {
    this.scanner = new Scanner("-5 -6");
    this.mpf = new MPFCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mpf.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("-6 -7"));
  }

  @Test
  public void testMPFCallsCorrectMethod() {
    this.scanner = new Scanner("8 8 ");
    this.mpf = new MPFCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mpf.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("mpf"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMPFMockOutThrowsIOError() {
    Appendable mockOut = new MockAppendable();
    this.mpf.run(this.model, this.view, mockOut);
  }

  @Test(expected = IllegalStateException.class)
  public void testMPFClosedScannerThrowsIOError() {
    this.scanner = new Scanner("1 1 1");
    this.mpf = new MPFCommand(this.scanner);
    this.scanner.close();
    this.mpf.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMPFNoInputsThrowsIOError() {
    this.scanner = new Scanner("");
    this.mpf = new MPFCommand(this.scanner);
    this.mpf.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMPFNotEnoughInputsThrowsIOError() {
    this.scanner = new Scanner("3");
    this.mpf = new MPFCommand(this.scanner);
    this.mpf.run(this.model, this.view, this.out);
  }

  @Test
  public void testMPFCorrectViewIsUsed() {
    this.scanner = new Scanner("10 10");
    this.mpf = new MPFCommand(this.scanner);
    TextualView mockView = new MockTextualView(this.out);
    this.mpf.run(this.model, mockView, this.out);
    Assert.assertTrue(this.out.toString().contains("Mock view"));
  }



  // tests for MPPCommand
  @Test(expected = NullPointerException.class)
  public void testMPPNullModel() {
    this.mpp.run(null, this.view, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMPPNullView() {
    this.mpp.run(this.model, null, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testMPPNullOut() {
    this.mpp.run(this.model, this.view, null);
  }

  @Test
  public void testMPPValidMoveAffectsModel() {
    this.scanner = new Scanner("3 1 7");
    this.mpp = new MPPCommand(this.scanner);
    Card ace = this.model.getCardAt(2, 2);
    this.mpp.run(this.model, this.view, this.out);
    Assert.assertEquals(ace, this.model.getCardAt(6, 7));
  }

  @Test
  public void testMPPInvalidMoveDisplaysMessage() {
    this.scanner = new Scanner("1 1 1");
    this.mpp = new MPPCommand(this.scanner);
    this.mpp.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Invalid move."));
  }

  @Test
  public void testMPPCantInterruptWithAnotherCommand() {
    this.scanner = new Scanner("1 dd 1 1");
    this.mpp = new MPPCommand(this.scanner);
    List<Card> draws = this.model.getDrawCards();
    this.mpp.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
    Assert.assertEquals(draws, this.model.getDrawCards());
  }

  @Test
  public void testMPPInvalidInputDisplaysMessage() {
    this.scanner = new Scanner("a a 1 1 1");
    this.mpp = new MPPCommand(this.scanner);
    this.mpp.run(this.model, this.view, this.out);
    Assert.assertTrue(this.out.toString().contains("Input must be an integer."));
  }

  @Test
  public void testMPPQuitStatusAfterFirstInput() {
    this.scanner = new Scanner("q");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPQuiStatusAfterInvalid() {
    this.scanner = new Scanner("a q");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPQuitStatusAfterValidAndInvalid() {
    this.scanner = new Scanner("1 a 1 a q");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.QUIT, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPContinueStatusCorrectInputs() {
    this.scanner = new Scanner("3 1 7");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPContinueStatusFixedInputs() {
    this.scanner = new Scanner("a 3 b 1 seven seven wait that doesn't work ? 7");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.CONTINUE, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPInvalidStatusCorrectInputs() {
    this.scanner = new Scanner("-1 -1 -1");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPInvalidStatusFixedInputs() {
    this.scanner = new Scanner("hi -1 im doing a move -1 that wont work -1");
    this.mpp = new MPPCommand(this.scanner);
    Assert.assertEquals(ControllerStatus.INVALID, this.mpp.run(this.model, this.view, this.out));
  }

  @Test
  public void testMPPUses0IndexAndDoesntGarble() {
    this.scanner = new Scanner("1 1 1");
    this.mpp = new MPPCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mpp.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("0 1 0"));
  }

  @Test
  public void testMPPStillSendsNegativeIntegers() {
    this.scanner = new Scanner("-1 -1 -1");
    this.mpp = new MPPCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mpp.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("-2 -1 -2"));
  }

  @Test
  public void testMPPCallsCorrectMethod() {
    this.scanner = new Scanner("1 1 1");
    this.mpp = new MPPCommand(this.scanner);
    MockKlondikeModel mockModel = new MockKlondikeModel();
    this.mpp.run(mockModel, this.view, this.out);
    Assert.assertTrue(mockModel.log.toString().contains("mpp"));
  }

  @Test(expected = IllegalStateException.class)
  public void testMPPMockOutThrowsIOError() {
    Appendable mockOut = new MockAppendable();
    this.mpp.run(this.model, this.view, mockOut);
  }

  @Test(expected = IllegalStateException.class)
  public void testMPPClosedScannerThrowsIOError() {
    this.scanner = new Scanner("1 1 1");
    this.mpp = new MPPCommand(this.scanner);
    this.scanner.close();
    this.mpp.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMPPNoInputsThrowsIOError() {
    this.scanner = new Scanner("");
    this.mpp = new MPPCommand(this.scanner);
    this.mpp.run(this.model, this.view, this.out);
  }

  @Test(expected = IllegalStateException.class)
  public void testMPPNotEnoughInputsThrowsIOError() {
    this.scanner = new Scanner("1 2");
    this.mpp = new MPPCommand(this.scanner);
    this.mpp.run(this.model, this.view, this.out);
  }

  @Test
  public void testMPPCorrectViewIsUsed() {
    this.scanner = new Scanner("1 1 1");
    this.mpp = new MPPCommand(this.scanner);
    TextualView mockView = new MockTextualView(this.out);
    this.mpp.run(this.model, mockView, this.out);
    Assert.assertTrue(this.out.toString().contains("Mock view"));
  }
}