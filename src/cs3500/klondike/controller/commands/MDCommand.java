package cs3500.klondike.controller.commands;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import cs3500.klondike.controller.ControllerStatus;
import cs3500.klondike.controller.KlondikeIOUtils;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.TextualView;

/**
 * A command that calls the moveDraw method on a KlondikeModel.
 */
public class MDCommand implements KlondikeCommand {
  private final Scanner scanner;

  /**
   * A constructor for MDCommand.
   * @param scanner A scanner used for reading input
   */
  public MDCommand(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public ControllerStatus run(KlondikeModel model, TextualView view, Appendable out) {
    Objects.requireNonNull(model); // ensure arguments aren't null
    Objects.requireNonNull(view);
    Objects.requireNonNull(out);
    // collect the required number of ints
    ArrayList<Integer> args = KlondikeIOUtils.getIntsOrQuit(1, this.scanner, out);
    if (args.size() != 1) { // if all inputs weren't collected, then input must have quit
      return ControllerStatus.QUIT; // return quit status
    }
    // if an integer is next, make it 0 indexed and try the move
    try {
      model.moveDraw(args.get(0) - 1);
      return ControllerStatus.CONTINUE; // return continue status

    }
    catch (IllegalStateException | IllegalArgumentException e) {
      KlondikeIOUtils.displayStateMessage(view, out, // display error message if the move is invalid
              "Invalid move. Play again. " + e.getMessage() + "\n", model.getScore());
      return ControllerStatus.INVALID; // return invalid status
    }
  }
}
