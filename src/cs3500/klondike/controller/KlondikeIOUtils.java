package cs3500.klondike.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

import cs3500.klondike.view.TextualView;

/**
 * A class that contains static methods useful for handling input and output
 * in a game of KlondikeSolitaire.
 */
public class KlondikeIOUtils {

  /**
   * Determines if the next token of the given scanner is an
   * integer or a quit symbol. Does not advance the scanner if the next token
   * is an integer, but does advance it if not.
   * @param scanner The scanner used to determine the next token.
   * @return A controller status corresponding to the state of the next token.
   *         CONTINUE means the next token is an integer. QUIT means it is the quit symbol.
   *         INVALID means it is neither.
   * @throws NullPointerException If the given scanner is null.
   */
  public static ControllerStatus hasNextIntOrQuit(Scanner scanner) {
    Objects.requireNonNull(scanner);
    try {
      if (scanner.hasNextInt()) {
        return ControllerStatus.CONTINUE;
      }
      String input = scanner.next();
      if (input.equalsIgnoreCase("q")) {
        return ControllerStatus.QUIT;
      }
      else {
        return ControllerStatus.INVALID;
      }
    }
    catch (IllegalStateException | NoSuchElementException e) {
      throw new IllegalStateException("No more inputs.");
    }
  }

  /**
   * Loops over input from the scanner until either the given "size" amount of integers are
   * gathered or the quit symbol is entered. Non-integer and non-quit values are not accepted
   * and will prompt a message to re-enter input.
   * @param amount The number of inputs to gather.
   * @param scanner The scanner used to read input.
   * @param out An output location to display the message to.
   * @return A list of the integers gathered before the quit symbol was entered (if ever).
   * @throws NullPointerException If the given scanner or appendable are null.
   * @throws IllegalArgumentException If the given amount is not positive.
   */
  public static ArrayList<Integer> getIntsOrQuit(int amount, Scanner scanner, Appendable out) {
    Objects.requireNonNull(scanner);
    Objects.requireNonNull(out);
    if (amount <= 0) { // check for non-positive amount of ints
      throw new IllegalArgumentException("Amount of integers must be positive.");
    }

    ArrayList<Integer> ints = new ArrayList<>(); // initialize arraylist to hold ints
    while (ints.size() < amount) { // loop until the given size is reached
      // get status of next integer
      ControllerStatus status = KlondikeIOUtils.hasNextIntOrQuit(scanner);
      switch (status) {
        case QUIT: // if quit status:
          return ints; // return the ints we've gathered
        case INVALID: // if invalid status:
          // display invalid message
          KlondikeIOUtils.write(out,"Input must be an integer. Try again.\n");
          continue; // try reading input again
        case CONTINUE: // if continue status:
          ints.add(scanner.nextInt()); // read next int and add it to the list
          break;
        default:
      }
    }
    return ints; // return the ints we've gathered
  }

  /**
   * Displays the state of the game followed by the given message to the given output location.
   * @param view A viewer used to display the state of the game.
   * @param out An output location to display the message to.
   * @param message A message to display.
   * @param score The games current score.
   * @throws NullPointerException If the given view or appendable are null.
   */
  public static void displayStateMessage(TextualView view, Appendable out, String message,
                                             int score) {
    Objects.requireNonNull(view);
    Objects.requireNonNull(out);
    try {
      view.render(); // render the game
      out.append("Score: ").append(String.valueOf(score)).append("\n"); // display the score
      out.append(message); // display given message
    }
    catch (IOException e) {
      throw new IllegalStateException("I/O issue occurred");
    }
  }

  /**
   * Displays the given message followed by the state of the game to the given output location.
   * @param view A viewer used to display the state of the game.
   * @param out An output location to display the message to.
   * @param message A message to display.
   * @param score The games current score.
   * @throws NullPointerException If the given view or appendable are null.
   */
  public static void displayMessageState(TextualView view, Appendable out, String message,
                                         int score) {
    Objects.requireNonNull(view);
    Objects.requireNonNull(out);
    try {
      out.append(message); // display given message
      view.render(); // render the game
      out.append("Score: ").append(String.valueOf(score)).append("\n"); // display the score
    }
    catch (IOException e) {
      throw new IllegalStateException("I/O issue occurred");
    }
  }

  /**
   * Write the given message to the given output location.
   * @param out An output location to display the message to.
   * @param message A message to display.i
   * @throws NullPointerException If the given appendable is null.
   */
  public static void write(Appendable out, String message) {
    Objects.requireNonNull(out);
    try {
      out.append(message);
    }
    catch (IOException e) {
      throw new IllegalStateException("I/O issue occurred.");
    }
  }
}
