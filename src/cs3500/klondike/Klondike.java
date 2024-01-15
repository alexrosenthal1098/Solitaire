package cs3500.klondike;

import java.io.InputStreamReader;

import cs3500.klondike.controller.KlondikeTextualController;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.model.hw04.KlondikeCreator;
import cs3500.klondike.model.hw04.LimitedDrawKlondike;

/**
 * A program that plays the game Klondike Solitaire.
 */
public class Klondike {

  /**
   * Uses command line input to start and play a game of Klondike Solitaire using the console.
   * @param args The command line inputs when the program is run.
   */
  public static void main(String[] args) {
    // initialize a controller with the console
    KlondikeTextualController controller = new KlondikeTextualController(
            new InputStreamReader(System.in), System.out);
    // declare a model, numPiles, and numDraw
    KlondikeModel model;
    int numPiles;
    int numDraw;
    try { // try to parse command line inputs and create the model
      // get game type
      KlondikeCreator.GameType gameType = KlondikeCreator.GameType.valueOf(args[0].toUpperCase());
      switch (gameType) {
        case LIMITED:  // if it is a limited draw game
          // create the model manually using the provided draw amount
          // which is modified to REdraw amount by subtracting 1
          model = new LimitedDrawKlondike(Integer.parseInt(args[1]) - 1);
          numPiles = args.length >= 3 ? Integer.parseInt(args[2]) : 7; // get number of piles
          numDraw = args.length >= 4 ? Integer.parseInt(args[3]) : 3; // get number of draws
          break;
        case BASIC: // if it is a basic klondike game
        case WHITEHEAD: // or a whitehead klondike game
          model = KlondikeCreator.create(gameType); // use the KlondikeCreator class to get a model
          numPiles = args.length >= 2 ? Integer.parseInt(args[1]) : 7; // get number of piles
          numDraw = args.length >= 3 ? Integer.parseInt(args[2]) : 3; // get number of draws
          break;
        default:
          throw new IllegalArgumentException("Invalid game type.");
      }
    }
    catch (Exception e) { // catch any exceptions
      // throw exception with message
      throw new IllegalArgumentException("Error occurred: " + e.getMessage());
    }

    try { // start the game with the controller
      controller.playGame(model, model.getDeck(), false, numPiles, numDraw);
    }
    catch (Exception e) { // if any exceptions occur, catch it
      // display an error instead of crashing
      System.out.println("Error occurred: " + e.getMessage());
    }
  }
}
