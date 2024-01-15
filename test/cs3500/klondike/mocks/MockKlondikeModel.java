package cs3500.klondike.mocks;

import java.util.ArrayList;
import java.util.List;

import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A mock class that implements KlondikeModel and is used for testing. Contains a log
 * used for tracking method arguments.
 */
public class MockKlondikeModel implements KlondikeModel {
  public StringBuilder log = new StringBuilder(); // used to log methods called on the model
  public boolean started = false; // used to check if the game was started

  @Override
  public List<Card> getDeck() {
    return null;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException, IllegalStateException {
    this.started = true;
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalArgumentException, IllegalStateException {
    this.log.append("mpp ").append(srcPile).append(" ")
            .append(numCards).append(" ").append(destPile);
  }

  @Override
  public void moveDraw(int destPile) throws IllegalArgumentException, IllegalStateException {
    this.log.append("md ").append(destPile);
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    this.log.append("mpf ").append(srcPile).append(" ").append(foundationPile);
  }

  @Override
  public void moveDrawToFoundation(int foundationPile)
          throws IllegalArgumentException, IllegalStateException {
    this.log.append("mdf ").append(foundationPile);
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    this.log.append("dd");
  }

  @Override
  public int getNumRows() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumPiles() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getNumDraw() throws IllegalStateException {
    return 0;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public int getScore() throws IllegalStateException {
    return 0;
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalArgumentException, IllegalStateException {
    return 0;
  }

  @Override
  public boolean isCardVisible(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return false;
  }

  @Override
  public Card getCardAt(int pileNum, int card)
          throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalArgumentException, IllegalStateException {
    return null;
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    return new ArrayList<Card>();
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    return 0;
  }
}
