package cs3500.klondike.model.hw04;

import org.junit.Assert;
import org.junit.Test;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A class that contains tests for the KlondikeCreator class.
 */
public class KlondikeCreatorTest {

  @Test(expected = NullPointerException.class)
  public void testCreateNull() {
    KlondikeCreator.create(null);
  }

  @Test
  public void testCreateBasic() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.BASIC);
    Assert.assertTrue(model instanceof BasicKlondike);
  }

  @Test
  public void testCreateLimited() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.LIMITED);
    Assert.assertTrue(model instanceof LimitedDrawKlondike);
  }

  @Test
  public void testCreateLimitedHasDefaultNumRedraws() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.LIMITED);
    model.startGame(model.getDeck().subList(0, 2), false, 1, 1);
    model.discardDraw(); // first redraw
    model.discardDraw(); // second redraw
    model.discardDraw(); // card is discarded and NOT redrawn
    Assert.assertThrows(IllegalStateException.class, model::discardDraw);
  }

  @Test
  public void testCreateWhitehead() {
    KlondikeModel model = KlondikeCreator.create(KlondikeCreator.GameType.WHITEHEAD);
    Assert.assertTrue(model instanceof WhiteheadKlondike);
  }
}