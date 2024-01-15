package cs3500.klondike.model.hw04;

import cs3500.klondike.model.hw02.BasicKlondike;
import cs3500.klondike.model.hw02.KlondikeModel;

/**
 * A class that holds a static method to create a game of Klondike Solitaire.
 */
public class KlondikeCreator {

  /**
   * All possible game types of Klondike Solitaire.
   */
  public enum GameType { BASIC, LIMITED, WHITEHEAD }

  /**
   * A static method that creates the given game type of Klondike Solitaire.
   * @param type The game type to create.
   * @return The model of the game.
   */
  public static KlondikeModel create(GameType type) {
    switch (type) {
      case BASIC:
        return new BasicKlondike();
      case LIMITED:
        return new LimitedDrawKlondike(2);
      case WHITEHEAD:
        return new WhiteheadKlondike();
      default:
        break;
    }
    throw new IllegalArgumentException("Invalid game type entered.");
  }
}
