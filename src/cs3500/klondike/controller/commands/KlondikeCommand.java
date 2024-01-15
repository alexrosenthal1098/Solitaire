package cs3500.klondike.controller.commands;

import cs3500.klondike.controller.ControllerStatus;
import cs3500.klondike.model.hw02.KlondikeModel;
import cs3500.klondike.view.TextualView;

/**
 * A command for a game of Klondike Solitaire.
 */
public interface KlondikeCommand {

  /**
   * Run the given command on the model, reading necessary input from the given scanner
   * and displaying error messages to the given appendable.
   * @param model The model of Klondike Solitaire that the command is performed on.
   * @param view The viewer for the given model.
   * @param out A source of output for displaying error messages.
   * @throws NullPointerException If any of the given arguments are null.
   */
  ControllerStatus run(KlondikeModel model, TextualView view, Appendable out);
}
