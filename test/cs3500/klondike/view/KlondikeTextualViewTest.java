package cs3500.klondike.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;
import cs3500.klondike.model.hw04.WhiteheadKlondike;


/**
 * Tests for the KlondikeTextualView class, including package-private methods.
 */
public class KlondikeTextualViewTest {
  KlondikeModel model1;
  KlondikeModel model2;
  KlondikeModel model3;
  KlondikeModel model4;
  KlondikeTextualView textView1;
  KlondikeTextualView textView2;
  KlondikeTextualView textView3;
  KlondikeTextualView textView4;

  @Before
  public void setUp() {
    this.model1 = new BasicKlondike();
    this.model2 = new BasicKlondike();
    this.model3 = new BasicKlondike();
    this.model4 = new BasicKlondike();
    this.textView1 = new KlondikeTextualView(this.model1);
    this.textView2 = new KlondikeTextualView(this.model2);
    this.textView3 = new KlondikeTextualView(this.model3);
    this.textView4 = new KlondikeTextualView(this.model4);
    this.model1.startGame(this.model1.getDeck(), false, 7, 3);
    this.model2.startGame(this.model2.getDeck(), false, 1, 2);

    List<Card> deck = this.model3.getDeck();
    deck.addAll(deck.subList(0, 13));
    this.model3.startGame(deck, false, 8, 1);

    this.model4.startGame(this.model4.getDeck().subList(0, 3),
            false, 2, 1);
  }


  // tests for drawToString
  @Test
  public void testDrawToStringDefault() {
    Assert.assertEquals("Draw: 3♢, 4♢, 5♢", this.textView1.drawToString());
  }

  @Test
  public void testDrawToStringTwoCards() {
    Assert.assertEquals("Draw: 2♡, 3♡", this.textView2.drawToString());
  }

  @Test
  public void testDrawToStringEmptyDraw() {
    Assert.assertEquals("Draw: ", this.textView4.drawToString());
  }

  @Test
  public void testDrawToStringLimitedDraw() {
    KlondikeModel limited = new LimitedDrawKlondike(2);
    limited.startGame(limited.getDeck(), false, 7, 3);
    KlondikeTextualView limitedView = new KlondikeTextualView(limited);
    Assert.assertEquals("Draw: 3♢, 4♢, 5♢", limitedView.drawToString());
  }

  @Test
  public void testDrawToStringWhitehead() {
    KlondikeModel whitehead = new WhiteheadKlondike();
    whitehead.startGame(whitehead.getDeck(), false, 7, 3);
    KlondikeTextualView whiteheadView = new KlondikeTextualView(whitehead);
    Assert.assertEquals("Draw: 3♢, 4♢, 5♢", whiteheadView.drawToString());
  }



  // tests for foundationToString
  @Test
  public void testFoundationToStringDefault() {
    Assert.assertEquals("Foundation: <none>, <none>, <none>, <none>",
            this.textView1.foundationToString());
  }

  @Test
  public void testFoundationToString5Piles() {
    Assert.assertEquals("Foundation: <none>, <none>, <none>, <none>, <none>",
            this.textView3.foundationToString());
  }

  @Test
  public void testFoundationToStringAfterPlaying() {
    this.model2.moveToFoundation(0, 1);
    for (int i = 0; i < 9; i++) {
      this.model2.moveDrawToFoundation(1);
    }

    for (int i = 0; i < 3; i++) {
      this.model2.discardDraw();
    }
    for (int i = 0; i < 4; i++) {
      this.model2.moveDrawToFoundation(3);
    }
    Assert.assertEquals("Foundation: <none>, 10♡, <none>, 4♠",
            this.textView2.foundationToString());
  }

  @Test
  public void testFoundationToStringLimitedDraw() {
    KlondikeModel limited = new LimitedDrawKlondike(2);
    limited.startGame(limited.getDeck(), false, 7, 3);
    KlondikeTextualView limitedView = new KlondikeTextualView(limited);
    Assert.assertEquals("Foundation: <none>, <none>, <none>, <none>",
            limitedView.foundationToString());
  }

  @Test
  public void testFoundationToStringWhitehead() {
    KlondikeModel whitehead = new WhiteheadKlondike();
    whitehead.startGame(whitehead.getDeck(), false, 7, 3);
    KlondikeTextualView whiteheadView = new KlondikeTextualView(whitehead);
    Assert.assertEquals("Foundation: <none>, <none>, <none>, <none>",
            whiteheadView.foundationToString());
  }



  // tests for cascadeToString
  @Test
  public void testCascadeToStringDefault() {
    Assert.assertEquals("" +
                    " A♡  ?  ?  ?  ?  ?  ?\n" +
                    "    8♡  ?  ?  ?  ?  ?\n" +
                    "       A♠  ?  ?  ?  ?\n" +
                    "          6♠  ?  ?  ?\n" +
                    "            10♠  ?  ?\n" +
                    "                K♠  ?\n" +
                    "                   2♢",
            this.textView1.cascadeToString());
  }

  @Test
  public void testCascadeToString1Piles() {
    Assert.assertEquals(" A♡", this.textView2.cascadeToString());
  }

  @Test
  public void testCascadeToStringEmpty() {
    this.model2.moveToFoundation(0, 0);
    Assert.assertEquals("", this.textView2.cascadeToString());
  }

  @Test
  public void testCascadeToStringAfterPlaying() {
    this.model1.moveToFoundation(0, 0);
    this.model1.moveToFoundation(2, 1);
    this.model1.movePile(2, 1, 4);
    Assert.assertEquals("" +
                    "  X  ? 3♡  ?  ?  ?  ?\n" +
                    "    8♡     ?  ?  ?  ?\n" +
                    "           ?  ?  ?  ?\n" +
                    "          6♠  ?  ?  ?\n" +
                    "            10♠  ?  ?\n" +
                    "             9♡ K♠  ?\n" +
                    "                   2♢",
            this.textView1.cascadeToString());
  }

  @Test
  public void testCascadeToStringLimited() {
    KlondikeModel limited = new LimitedDrawKlondike(2);
    limited.startGame(limited.getDeck(), false, 7, 3);
    KlondikeTextualView limitedView = new KlondikeTextualView(limited);
    Assert.assertEquals("" +
                    " A♡  ?  ?  ?  ?  ?  ?\n" +
                    "    8♡  ?  ?  ?  ?  ?\n" +
                    "       A♠  ?  ?  ?  ?\n" +
                    "          6♠  ?  ?  ?\n" +
                    "            10♠  ?  ?\n" +
                    "                K♠  ?\n" +
                    "                   2♢",
            limitedView.cascadeToString());
  }

  @Test
  public void testCascadeToStringWhitehead() {
    KlondikeModel whitehead = new WhiteheadKlondike();
    whitehead.startGame(whitehead.getDeck(), false, 7, 3);
    KlondikeTextualView whiteheadView = new KlondikeTextualView(whitehead);
    Assert.assertEquals("" +
                    " A♡ 2♡ 3♡ 4♡ 5♡ 6♡ 7♡\n" +
                    "    8♡ 9♡10♡ J♡ Q♡ K♡\n" +
                    "       A♠ 2♠ 3♠ 4♠ 5♠\n" +
                    "          6♠ 7♠ 8♠ 9♠\n" +
                    "            10♠ J♠ Q♠\n" +
                    "                K♠ A♢\n" +
                    "                   2♢",
            whiteheadView.cascadeToString());
  }



  // tests for toString
  @Test
  public void testToStringDefault() {
    Assert.assertEquals("" +
            "Draw: 3♢, 4♢, 5♢\n" +
            "Foundation: <none>, <none>, <none>, <none>\n" +
            " A♡  ?  ?  ?  ?  ?  ?\n" +
            "    8♡  ?  ?  ?  ?  ?\n" +
            "       A♠  ?  ?  ?  ?\n" +
            "          6♠  ?  ?  ?\n" +
            "            10♠  ?  ?\n" +
            "                K♠  ?\n" +
            "                   2♢",
            this.textView1.toString());
  }

  @Test
  public void testToStringAfterPlaying() {
    this.model1.moveToFoundation(0, 0);
    this.model1.moveToFoundation(2, 1);
    this.model1.movePile(2, 1, 4);
    this.model1.discardDraw();
    this.model1.discardDraw();
    this.model1.moveDraw(3);
    this.model1.movePile(5, 1, 0);
    for (int i = 0; i < 8; i++) {
      this.model1.discardDraw();
    }
    this.model1.moveDrawToFoundation(3);
    this.model1.moveDrawToFoundation(3);
    this.model1.moveDrawToFoundation(3);
    Assert.assertEquals("Draw: 4♣, 5♣, 6♣\n" +
                    "Foundation: A♡, A♠, <none>, 3♣\n" +
                    " K♠  ? 3♡  ?  ?  ?  ?\n" +
                    "    8♡     ?  ?  ?  ?\n" +
                    "           ?  ?  ?  ?\n" +
                    "          6♠  ?  ?  ?\n" +
                    "          5♢10♠ J♠  ?\n" +
                    "             9♡     ?\n" +
                    "                   2♢",
            this.textView1.toString());
  }

  @Test
  public void testToStringEmptyCascade() {
    this.model2.moveToFoundation(0, 0);
    Assert.assertEquals("" +
            "Draw: 2♡, 3♡\n" +
            "Foundation: A♡, <none>, <none>, <none>\n", this.textView2.toString());
  }

  @Test
  public void testToStringLimited() {
    KlondikeModel limited = new LimitedDrawKlondike(2);
    limited.startGame(limited.getDeck(), false, 7, 3);
    KlondikeTextualView limitedView = new KlondikeTextualView(limited);
    Assert.assertEquals("" +
                    "Draw: 3♢, 4♢, 5♢\n" +
                    "Foundation: <none>, <none>, <none>, <none>\n" +
                    " A♡  ?  ?  ?  ?  ?  ?\n" +
                    "    8♡  ?  ?  ?  ?  ?\n" +
                    "       A♠  ?  ?  ?  ?\n" +
                    "          6♠  ?  ?  ?\n" +
                    "            10♠  ?  ?\n" +
                    "                K♠  ?\n" +
                    "                   2♢",
            limitedView.toString());
  }

  @Test
  public void testToStringWhitehead() {
    KlondikeModel whitehead = new WhiteheadKlondike();
    whitehead.startGame(whitehead.getDeck(), false, 7, 3);
    KlondikeTextualView whiteheadView = new KlondikeTextualView(whitehead);
    Assert.assertEquals("" +
                    "Draw: 3♢, 4♢, 5♢\n" +
                    "Foundation: <none>, <none>, <none>, <none>\n" +
                    " A♡ 2♡ 3♡ 4♡ 5♡ 6♡ 7♡\n" +
                    "    8♡ 9♡10♡ J♡ Q♡ K♡\n" +
                    "       A♠ 2♠ 3♠ 4♠ 5♠\n" +
                    "          6♠ 7♠ 8♠ 9♠\n" +
                    "            10♠ J♠ Q♠\n" +
                    "                K♠ A♢\n" +
                    "                   2♢",
            whiteheadView.toString());
  }
}
