package cs3500.klondike.controller;

import cs3500.klondike.controller.commands.KlondikeCommand;
import cs3500.klondike.controller.commands.MPPCommand;
import cs3500.klondike.controller.commands.MDCommand;
import cs3500.klondike.controller.commands.MPFCommand;
import cs3500.klondike.controller.commands.MDFCommand;
import cs3500.klondike.controller.commands.DDCommand;
import cs3500.klondike.model.hw02.Card;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.KlondikeTextualView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Function;

/**
 * A controller for playing a text-based version of Klondike Solitaire.
 */
public class KlondikeTextualController implements KlondikeController {
  private final Scanner scanner; // the scanner used to read user input
  private final Appendable out; // the appendable location of controller output
  private KlondikeModel model; // the model being controlled
  private KlondikeTextualView view; // a way to view the model
  // a hashmap of strings to a Function that returns the respective command object with
  // the given scanner passed to the constructor
  private final Map<String, Function<Scanner, KlondikeCommand>> commands = new HashMap<>();

  /**
   * A constructor for a KlondikeTextualController. Takes in an input source and output location,
   * and creates a hashmap of valid commands for the game.
   * @param r A readable source.
   * @param a An appendable location.
   */
  public KlondikeTextualController(Readable r, Appendable a) {
    if (r == null || a == null) {
      throw new IllegalArgumentException("Given input and output sources cannot be null.");
    }
    this.scanner = new Scanner(r);
    this.out = a;
    this.commands.put("mpp", MPPCommand::new);
    this.commands.put("md", MDCommand::new);
    this.commands.put("mpf", MPFCommand::new);
    this.commands.put("mdf", MDFCommand::new);
    this.commands.put("dd", DDCommand::new);
  }

  @Override
  public void playGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                       int numDraw) {
    this.validateGame(model, deck, shuffle, numPiles, numDraw); // validate given arguments
    // display the starting state of the game
    KlondikeIOUtils.displayStateMessage(this.view, this.out, "", this.model.getScore());
    while (true) { // begin the game loop
      if (this.isGameOver(deck.size()) == ControllerStatus.QUIT) { // check if the game is over
        return; // if it is, the helper will display the message so just quit
      }

      String commandStr = this.getCommandString(); // get the next token from the scanner
      switch (this.parseCommand(commandStr)) { // parse the token as a command and get the status
        case QUIT: // if status is quit, display quit message and end the game
          KlondikeIOUtils.displayMessageState(this.view, this.out,
                  "Game quit!\nState of game when quit:\n", this.model.getScore());
          return;
        case INVALID: // if status is invalid, display message and try reading command again
          KlondikeIOUtils.displayStateMessage(this.view, this.out,
                  "Invalid move. Play again. Command not recognized.\n",
                  this.model.getScore());
          continue;
        case CONTINUE: // if status is continue, get the command object and run it
          KlondikeCommand commandObject = this.commands.get(commandStr).apply(this.scanner);
          switch (commandObject.run(this.model, this.view, this.out)) { // check status from command
            case QUIT: // if status is quit, display quit message and end the game
              KlondikeIOUtils.displayMessageState(this.view, this.out,
                      "Game quit!\nState of game when quit:\n", this.model.getScore());
              return;
            case INVALID: // if status is invalid, start over and read new command
              continue; // the command object itself handles sending the "invalid move" message
            case CONTINUE: // if the status is continue, display the state of the game and move on
              KlondikeIOUtils.displayStateMessage(this.view, this.out, "", this.model.getScore());
              break;
            default: // no default case
          }
          break;
        default: // no default case
      }
    }
  }



  //                  Helper Methods
  // validates the arguments of the game and starts the game,
  // throws exceptions if the arguments are invalid or the game can't start
  void validateGame(KlondikeModel model, List<Card> deck, boolean shuffle, int numPiles,
                    int numDraw) {
    if (model == null) { // make sure the model is not null
      throw new IllegalArgumentException("Cannot play game with a null model.");
    }
    this.model = model;
    this.view = new KlondikeTextualView(this.model, this.out);
    try { // try to start the game
      this.model.startGame(deck, shuffle, numPiles, numDraw);
    }
    catch (IllegalStateException | IllegalArgumentException e) {
      throw new IllegalStateException(e.getMessage()); // if it fails, throw IllegalStateException
    }
  }

  // Gets the next command or throws an error if there is no more input to read.
  String getCommandString() {
    try {
      return this.scanner.next();
    }
    catch (NoSuchElementException | IllegalStateException e) {
      throw new IllegalStateException("No more inputs left.");
    }
  }

  // determines if the given command is valid, invalid, or a quit command
  // returns a corresponding controller status
  ControllerStatus parseCommand(String commandStr) {
    if (commandStr.equalsIgnoreCase("q")) { // check if the command is q or Q
      return ControllerStatus.QUIT; // return quit status
    }
    else if (this.commands.containsKey(commandStr)) { // check if the hashmap contains the given key
      return ControllerStatus.CONTINUE; // return continue status
    }
    else { // the hashmap does not contain the given key
      return ControllerStatus.INVALID; // return invalid status
    }
  }

  // checks if the game is over using the given max score
  // returns a controller status of QUIT if it is over, or CONTINUE if it is not
  ControllerStatus isGameOver(int maxScore) {
    if (this.model.isGameOver()) { // check if the ga
      if (this.model.getScore() == maxScore) {
        KlondikeIOUtils.write(this.out, "You win!\n");
        return ControllerStatus.QUIT;
      }
      else {
        KlondikeIOUtils.write(this.out, "Game over. Score: "
                + this.model.getScore() + "\n");
        return ControllerStatus.QUIT;
      }
    }
    return ControllerStatus.CONTINUE;
  }
}