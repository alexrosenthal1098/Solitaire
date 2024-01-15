package cs3500.klondike.controller.commands;

import java.util.Objects;
import java.util.Scanner;

import cs3500.klondike.controller.ControllerStatus;
import cs3500.klondike.controller.KlondikeIOUtils;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.TextualView;

/**
 * A command that calls the discardDraw method on a KlondikeModel.
 */
public class DDCommand implements KlondikeCommand {
  Scanner scanner;

  /**
   * A constructor for MDDCommand.
   */
  public DDCommand(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public ControllerStatus run(KlondikeModel model, TextualView view, Appendable out) {
    Objects.requireNonNull(model); // ensure arguments aren't null
    Objects.requireNonNull(view);
    Objects.requireNonNull(out);
    try {
      model.discardDraw(); // doesn't need to read input so simply try the move
      return ControllerStatus.CONTINUE; // return continue status
    }
    catch (IllegalStateException e) { // display error message if the move is invalid
      KlondikeIOUtils.displayStateMessage(view, out,
              "Invalid move. Play again. " + e.getMessage() + "\n", model.getScore());
      return ControllerStatus.INVALID; // return invalid status

    }
  }
}
