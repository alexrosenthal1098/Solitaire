package cs3500.klondike.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cs3500.klondike.mocks.MockAppendable;
import cs3500.klondike.mocks.MockTextualView;
import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;
import cs3500.klondike.view.TextualView;

/**
 * A test class for KlondikeIOUtils.
 */
public class KlondikeIOUtilsTest {
  Scanner scanner;
  Appendable out;
  KlondikeModel model;
  TextualView view;

  @Before
  public void setUp() {
    this.scanner = new Scanner("");
    this.out = new StringBuilder();
    this.model = new BasicKlondike();
    this.model.startGame(this.model.getDeck(), false, 1, 3);
    this.view = new KlondikeTextualView(this.model, this.out);
  }

  // tests for hasNextIntOrQuit
  @Test(expected = NullPointerException.class)
  public void testHasNextNullScanner() {
    KlondikeIOUtils.hasNextIntOrQuit(null);
  }

  @Test
  public void testHasNextClosedScanner() {
    this.scanner.close();
    try {
      KlondikeIOUtils.hasNextIntOrQuit(this.scanner);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("No more inputs.", e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testHasNextNoMoreInput() {
    try {
      KlondikeIOUtils.hasNextIntOrQuit(this.scanner);
    }
    catch (NumberFormatException e) {
      Assert.assertEquals("No more inputs.", e.getMessage());
    }
  }

  @Test
  public void testHasNextNegativeInt() {
    this.scanner = new Scanner("-10");
    Assert.assertEquals(ControllerStatus.CONTINUE, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNext0() {
    this.scanner = new Scanner("0");
    Assert.assertEquals(ControllerStatus.CONTINUE, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextPositiveInt() {
    this.scanner = new Scanner("3");
    Assert.assertEquals(ControllerStatus.CONTINUE, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextIntMultipleInputs() {
    this.scanner = new Scanner("3 q 3 hi");
    Assert.assertEquals(ControllerStatus.CONTINUE, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextq() {
    this.scanner = new Scanner("q");
    Assert.assertEquals(ControllerStatus.QUIT, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextQ() {
    this.scanner = new Scanner("Q");
    Assert.assertEquals(ControllerStatus.QUIT, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextqMultpleInputs() {
    this.scanner = new Scanner("q 1 2 3");
    Assert.assertEquals(ControllerStatus.QUIT, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextQuit() {
    this.scanner = new Scanner("quit");
    Assert.assertEquals(ControllerStatus.INVALID, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextString() {
    this.scanner = new Scanner("thisinputisinvalid");
    Assert.assertEquals(ControllerStatus.INVALID, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextCommand() {
    this.scanner = new Scanner("mpp");
    Assert.assertEquals(ControllerStatus.INVALID, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextDouble() {
    this.scanner = new Scanner("1.5");
    Assert.assertEquals(ControllerStatus.INVALID, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }

  @Test
  public void testHasNextInvalidMultipleInputs() {
    this.scanner = new Scanner("invalid 3 q hi 10 1.4");
    Assert.assertEquals(ControllerStatus.INVALID, KlondikeIOUtils.hasNextIntOrQuit(this.scanner));
  }



  // tests for getIntsOrQuit
  @Test(expected = NullPointerException.class)
  public void testGetIntsNullScanner() {
    KlondikeIOUtils.getIntsOrQuit(3, null, this.out);
  }

  @Test(expected = NullPointerException.class)
  public void testGetIntsNullAppendable() {
    KlondikeIOUtils.getIntsOrQuit(1, this.scanner, null);
  }

  @Test
  public void testGetIntsClosedScanner() {
    this.scanner.close();
    try {
      KlondikeIOUtils.getIntsOrQuit(3, this.scanner, this.out);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("No more inputs.", e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testGetIntsNoMoreInput() {
    try {
      KlondikeIOUtils.getIntsOrQuit(3, this.scanner, this.out);
    }
    catch (NoSuchElementException e) {
      Assert.assertEquals("No more inputs.", e.getMessage());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetInts0() {
    KlondikeIOUtils.getIntsOrQuit(0, this.scanner, this.out);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetIntsNegative() {
    KlondikeIOUtils.getIntsOrQuit(-3, this.scanner, this.out);
  }

  @Test
  public void testGetIntsFirstQuit() {
    this.scanner = new Scanner("q");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(1, this.scanner, this.out);
    Assert.assertEquals(0, ints.size());
  }

  @Test
  public void testGetIntsQuitWithMoreInput() {
    this.scanner = new Scanner("q 3 2");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(2, this.scanner, this.out);
    Assert.assertEquals(0, ints.size());
  }

  @Test
  public void testGetIntsQuitAfterInvalid() {
    this.scanner = new Scanner("invalid input q");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(2, this.scanner, this.out);
    Assert.assertEquals(0, ints.size());
  }

  @Test
  public void testGetIntsInvalidThenValid() {
    this.scanner = new Scanner("notgood 3");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(1, this.scanner, this.out);
    Assert.assertEquals(1, ints.size());
  }

  @Test
  public void testGetIntsAllValid() {
    this.scanner = new Scanner("1 2 3 4");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(4, this.scanner, this.out);
    Assert.assertEquals(4, ints.size());
  }

  @Test
  public void testGetIntsFullListWithInvalids() {
    this.scanner = new Scanner("1 hi sorry 2 3 omg so sorry 4");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(4, this.scanner, this.out);
    Assert.assertEquals(4, ints.size());
  }

  @Test
  public void testGetIntsPartialListQuitEarly() {
    this.scanner = new Scanner("1 hi sorry 2 q");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(4, this.scanner, this.out);
    Assert.assertEquals(2, ints.size());
  }

  @Test
  public void testGetIntsCorrectValues() {
    this.scanner = new Scanner("1 2 3 4");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(4, this.scanner, this.out);
    Assert.assertEquals(new ArrayList<>(Arrays.asList(1, 2, 3, 4)), ints);
  }

  @Test
  public void testGetIntsFullListNoExtra() {
    this.scanner = new Scanner("1 2 3 4 5 6");
    ArrayList<Integer> ints = KlondikeIOUtils.getIntsOrQuit(4, this.scanner, this.out);
    Assert.assertEquals(4, ints.size());
  }



  // tests for displayMessageState
  @Test(expected = NullPointerException.class)
  public void testDisplayStateMessageNullView() {
    KlondikeIOUtils.displayStateMessage(null, this.out, "hi", 1);
  }

  @Test(expected = NullPointerException.class)
  public void testDisplayStateMessageNullOut() {
    KlondikeIOUtils.displayStateMessage(this.view, null, "hi", 1);
  }

  @Test
  public void testDisplayStateMessageIOException() {
    try {
      KlondikeIOUtils.displayStateMessage(this.view, new MockAppendable(), "hi", 1);
      Assert.assertTrue("If this point is reached then no exception was thrown, " +
              "so the test should fail.", false);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("I/O issue occurred", e.getMessage());
    }
  }

  @Test
  public void testDisplayStateMessageEmptyString() {
    KlondikeIOUtils.displayStateMessage(this.view, this.out, "", -1);
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n" +
            "Score: -1\n", this.out.toString());
  }

  @Test
  public void testDisplayStateMessageNormalCase() {
    KlondikeIOUtils.displayStateMessage(this.view, this.out,
            "Invalid move. Play again.\n", 1);
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n" +
            "Score: 1\n" +
            "Invalid move. Play again.\n", this.out.toString());
  }

  @Test
  public void testDisplayStateMessageUseCorrectView() {
    TextualView mockView = new MockTextualView(this.out);
    KlondikeIOUtils.displayStateMessage(mockView, this.out, "Hello world!\n", 4);
    Assert.assertEquals("Mock view\n" +
            "Score: 4\n" +
            "Hello world!\n", this.out.toString());
  }

  @Test
  public void testDisplayStateMessageTwoDifferentOuts() {
    Appendable newOut = new StringBuilder();
    KlondikeIOUtils.displayStateMessage(this.view, newOut, "Hello world!", 0);
    Assert.assertEquals("Score: 0\nHello world!", newOut.toString());
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n", this.out.toString());
  }


  @Test
  public void displayStateMessageNullString() {
    KlondikeIOUtils.displayStateMessage(this.view, this.out, null, 1);
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n" +
            "Score: 1\nnull", this.out.toString());
  }



  // tests for displayMessageMessage
  @Test(expected = NullPointerException.class)
  public void testDisplayMessageStateNullView() {
    KlondikeIOUtils.displayMessageState(null, this.out, "hi", 1);
  }

  @Test(expected = NullPointerException.class)
  public void testDisplayMessageStateNullOut() {
    KlondikeIOUtils.displayMessageState(this.view, null, "hi", 1);
  }

  @Test
  public void testDisplayMessageStateIOException() {
    try {
      KlondikeIOUtils.displayMessageState(this.view, new MockAppendable(), "hi", 1);
      Assert.assertTrue("If this point is reached then no exception was thrown, " +
              "so the test should fail.", false);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("I/O issue occurred", e.getMessage());
    }
  }

  @Test
  public void testDisplayMessageStateEmptyString() {
    KlondikeIOUtils.displayMessageState(this.view, this.out, "", -1);
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n" +
            "Score: -1\n", this.out.toString());
  }

  @Test
  public void testDisplayMessageStateNormalCase() {
    KlondikeIOUtils.displayMessageState(this.view, this.out,
            "Invalid move. Play again.\n", 1);
    Assert.assertEquals("" +
            "Invalid move. Play again.\n" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n" +
            "Score: 1\n", this.out.toString());
  }

  @Test
  public void testDisplayMessageStateUseCorrectView() {
    TextualView mockView = new MockTextualView(this.out);
    KlondikeIOUtils.displayMessageState(mockView, this.out, "Hello world! ", 4);
    Assert.assertEquals("Hello world! Mock view\n" +
            "Score: 4\n", this.out.toString());
  }

  @Test
  public void testDisplayMessageStateTwoDifferentOuts() {
    Appendable newOut = new StringBuilder();
    KlondikeIOUtils.displayMessageState(this.view, newOut, "Hello world! ", 0);
    Assert.assertEquals("Hello world! Score: 0\n", newOut.toString());
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n", this.out.toString());
  }


  @Test
  public void displayMessageStateNullString() {
    KlondikeIOUtils.displayMessageState(this.view, this.out, null, 1);
    Assert.assertEquals("" +
            "nullDraw: 2♡, 3♡, 4♡\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡\n" +
            "Score: 1\n", this.out.toString());
  }



  // tests for write
  @Test(expected = NullPointerException.class)
  public void writeNullOut() {
    KlondikeIOUtils.write(null, ":(((((");
  }

  @Test
  public void writeIOException() {
    try {
      KlondikeIOUtils.write(new MockAppendable(), "mock appendable throws an error");
      Assert.assertTrue("If this point is reached then no exception was thrown, " +
              "so the test should fail.", false);
    }
    catch (IllegalStateException e) {
      Assert.assertEquals("I/O issue occurred.", e.getMessage());
    }
  }

  @Test
  public void writeNullMessage() {
    KlondikeIOUtils.write(this.out, null);
    Assert.assertEquals("null", this.out.toString());
  }

  @Test
  public void writeEmptyMessage() {
    KlondikeIOUtils.write(this.out, "");
    Assert.assertEquals("", this.out.toString());
  }

  @Test
  public void writeNormalMessage() {
    KlondikeIOUtils.write(this.out, "Input must be an integer. Try again.");
    Assert.assertEquals("Input must be an integer. Try again.", this.out.toString());
  }

  @Test
  public void writeMessageWithInts() {
    KlondikeIOUtils.write(this.out, "2 + 2 = 4");
    Assert.assertEquals("2 + 2 = 4", this.out.toString());
  }
}