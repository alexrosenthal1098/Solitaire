package cs3500.klondike.model.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A model that represents the card game Klondike Solitaire.
 */
public class BasicKlondike implements KlondikeModel {
  protected ArrayList<Card>[] cascades; // array of cascade piles (which are ArrayLists)
  // leftmost pile is array index 0, topmost card is arraylist index 0
  protected Card[] foundations; // array of the top card in each foundation pile
  protected ArrayList<Card> draws; // array of ALL cards in the draw pile (face down and face up)
  // the cards are used in order starting from index 0, which is the leftmost card in the draw pile
  private int drawAmount; // amount of draw cards visible
  protected HashMap<Card, Boolean> visibility; // a map of each card to its visibility
  protected boolean gameStarted; // a boolean representing if the game has started


  /**
   * An empty constructor for BasicKlondike that simply initializes the game as not started.
   */
  public BasicKlondike() {
    this.gameStarted = false; // the game has not started
  }

  @Override
  public List<Card> getDeck() {
    // Create a new deck, use hard coded array of suits and values along with for loops
    // to create a standard 52 card deck
    ArrayList<Card> deck = new ArrayList<>();
    CardSuit[] suits = new CardSuit[]{CardSuit.HEARTS, CardSuit.SPADES, CardSuit.DIAMONDS,
        CardSuit.CLUBS};
    CardValue[] values = new CardValue[]{CardValue.ACE, CardValue.TWO, CardValue.THREE,
        CardValue.FOUR, CardValue.FIVE, CardValue.SIX, CardValue.SEVEN, CardValue.EIGHT,
        CardValue.NINE, CardValue.TEN, CardValue.JACK, CardValue.QUEEN, CardValue.KING};
    for (int suit = 0; suit < 4; suit++) { // use int suit as index value for the 4 suits
      for (int value = 0; value < 13; value++) { // use int value as index for the 13 values
        deck.add(new KlondikeCard(values[value], suits[suit]));
      }
    }
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, boolean shuffle, int numPiles, int numDraw)
          throws IllegalArgumentException {
    if (this.gameStarted) { // check if game has already started
      throw new IllegalStateException("Game already started.");
    }

    this.validateContents(deck, numPiles, numDraw); // validate arguments
    this.initFoundation(deck); // initialize the foundation piles
    this.initVisibility(deck); // initialize the visibility hashmap
    // copy given deck into an ArrayList
    ArrayList<Card> deckCopy = new ArrayList<>(Objects.requireNonNull(deck));
    if (shuffle) { // shuffle the deck if shuffle arg is true
      Collections.shuffle(deckCopy);
    }
    this.dealCascades(deckCopy, numPiles); // deal the cascade cards
    this.drawAmount = numDraw; // set drawAmount
    this.gameStarted = true; // start the game!!
  }

  @Override
  public void movePile(int srcPile, int numCards, int destPile)
          throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validatePileNum(srcPile); // check both pile indexes
    this.validatePileNum(destPile);
    if (numCards <= 0) { // check that numCards is positive
      throw new IllegalArgumentException("Number of cards must be positive.");
    }
    if (srcPile == destPile) { // make sure the pile indexes aren't the same
      throw new IllegalArgumentException("Pile numbers cannot be the same.");
    } // make sure there are enough cards in the source pile
    if (numCards > this.cascades[srcPile].size()) {
      throw new IllegalArgumentException("Not enough cards in the source pile.");
    }
    // get the index of the bottom card (highest up if you're thinking about textual view)
    int bottomCardIndex = this.cascades[srcPile].size() - numCards;
    if (!this.isCardVisible(srcPile, bottomCardIndex)) { // check that the bottom card is visible
      throw new IllegalArgumentException("Not enough visible cards to move.");
    }
    ArrayList<Card> cardsToMove = new ArrayList<>(this.cascades[srcPile].subList(bottomCardIndex,
            this.cascades[srcPile].size())); // create an ArrayList of the cards to move
    this.validatePileMove(cardsToMove, destPile); // check if the move is valid
    // now that we know the move is valid, move the cards out of the source pile
    this.cascades[srcPile] = new ArrayList<>(this.cascades[srcPile].subList(0, bottomCardIndex));
    // and add all cards to the destination pile, can't use addAll because of potential duplicates
    for (Card card : cardsToMove) {
      this.cascades[destPile].add(card);
    }
    // if the new source pile is not empty, reveal its top card (set visibility to true)
    if (!this.cascades[srcPile].isEmpty()) {
      this.visibility.put(this.cascades[srcPile].get(this.getPileHeight(srcPile) - 1), true);
    }
  }

  @Override
  public void moveDraw(int destPile) throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validatePileNum(destPile); // check the destination pile index
    if (this.getDrawCards().isEmpty()) { // check if there are cards left in the draw pile
      throw new IllegalStateException("No cards left in the draw pile.");
    }
    Card drawCard = this.draws.get(0);
    // check if the move is valid
    this.validatePileMove(new ArrayList<Card>(Arrays.asList(drawCard)), destPile);
    // now that we know the move is valid, move the cards
    this.cascades[destPile].add(drawCard); // move the card to the destination
    this.draws.remove(0); // remove the card from the draw pile
  }

  @Override
  public void moveToFoundation(int srcPile, int foundationPile)
          throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validatePileNum(srcPile); // check the srcPile index
    this.validateFoundationNum(foundationPile); // check the foundation pile index
    if (this.cascades[srcPile].isEmpty()) { // check if the source pile is empty
      throw new IllegalStateException("Source pile is empty");
    }
    // get the card to be moved from the source pile
    Card pileCard = this.cascades[srcPile].get(this.cascades[srcPile].size() - 1);
    this.validateFoundationMove(pileCard, foundationPile); // check if the move is valid
    // now that we know the move is valid, move the cards
    this.foundations[foundationPile] = pileCard;
    // remove the right card from the source pile using index not object in case of duplicates
    this.cascades[srcPile].remove(this.cascades[srcPile].size() - 1);
    // if the new source pile is not empty, reveal its top card (set visibility to true)
    if (!this.cascades[srcPile].isEmpty()) {
      this.visibility.put(this.cascades[srcPile].get(this.getPileHeight(srcPile) - 1), true);
    }
  }

  @Override
  public void moveDrawToFoundation(int foundationPile) throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    if (this.getDrawCards().isEmpty()) { // check if there are cards left in the draw pile
      throw new IllegalStateException("No cards left in the draw pile.");
    }
    this.validateFoundationNum(foundationPile);// check the foundation pile index

    Card drawCard = this.getDrawCards().get(0); // get the draw card
    this.validateFoundationMove(drawCard, foundationPile); // check if them ove is valid
    // now that we know the move is valid, move the cards
    this.foundations[foundationPile] = drawCard; // move the draw card to the foundation pile
    this.draws.remove(0); // remove the card from the draw pile
  }

  @Override
  public void discardDraw() throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    if (this.getDrawCards().isEmpty()) { // check if the draw pile is empty
      throw new IllegalStateException("No cards left in the draw pile.");
    }

    // remove the card from the top of the draw pile and add it to the bottom (end of the list)
    this.draws.add(this.draws.remove(0));
  }

  @Override
  public int getNumRows() {
    this.validateGameStarted(); // check that the game has started
    int maxHeight = 0; // initialize max height of a pile
    for (ArrayList<Card> pile : this.cascades) { // iterate through cascade piles to find max size
      if (pile.size() > maxHeight) {
        maxHeight = pile.size();
      }
    }
    return maxHeight; // return maximum height of a pile
  }

  @Override
  public int getNumPiles() {
    this.validateGameStarted(); // check that the game has started
    return this.cascades.length;
  }

  @Override
  public int getNumDraw() {
    this.validateGameStarted(); // check that the game has started
    return this.drawAmount;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    if (!this.draws.isEmpty()) { // if there are draw cards left the game is not over
      return false;
    }
    return !this.anyFoundationMoves() && !this.anyPileMoves();
  }

  @Override
  public int getScore() throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    int score = 0; // initialize score
    for (Card card : this.foundations) { // iterate through foundation cards
      if (card == null) { // if there are no cards in the foundation pile, continue
        continue;
      }
      score += KlondikeCard.getValue(card); // add the value of the top foundation card
    }
    return score; // return the score
  }

  @Override
  public int getPileHeight(int pileNum) throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validatePileNum(pileNum); // check the pile index
    return this.cascades[pileNum].size();
  }

  @Override
  public boolean isCardVisible(int pileNum, int card) throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validatePileNum(pileNum); // check the pile index
    this.validateCardNum(pileNum, card); // check the card index
    // return boolean from the visibility hash map
    return this.visibility.get(this.cascades[pileNum].get(card));
  }

  @Override
  public Card getCardAt(int pileNum, int card) throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validatePileNum(pileNum); // check the pile index
    this.validateCardNum(pileNum, card); // check the card index
    if (!this.isCardVisible(pileNum, card)) { // check if the card is visible
      throw new IllegalArgumentException("Card is not visible.");
    }
    return this.cascades[pileNum].get(card); // return the pile card
  }

  @Override
  public Card getCardAt(int foundationPile) throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    this.validateFoundationNum(foundationPile); // check the foundation pile index
    return this.foundations[foundationPile]; // return the foundation card
  }

  @Override
  public List<Card> getDrawCards() throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    ArrayList<Card> drawCards = new ArrayList<>(this.drawAmount); // initialize empty list of cards
    for (int i = 0; i < this.drawAmount; i++) { // iterate for drawAmount
      if (i >= this.draws.size()) { // if we have reached the end of the draw pile, break
        break;
      }
      else { // if not, add the next card to the list
        drawCards.add(draws.get(i));
      }
    }
    return drawCards; // return the list of draw cards
  }

  @Override
  public int getNumFoundations() throws IllegalStateException {
    this.validateGameStarted(); // check that the game has started
    return this.foundations.length; // return the amount of foundation piles
  }

  //                HELPER METHODS

  // validates that the game has started
  protected void validateGameStarted() {
    if (!this.gameStarted) {
      throw new IllegalStateException("The game has not started yet.");
    }
  }

  // validates that the given pile number is a valid index
  protected void validatePileNum(int pileNum) {
    if (pileNum < 0 || pileNum >= this.cascades.length) {
      throw new IllegalArgumentException("Invalid pile number.");
    }
  }

  // validates that the given card number is a valid index for the given pile
  protected void validateCardNum(int pileNum, int card) {
    this.validatePileNum(pileNum); // check the pile index
    if (card < 0 || card >= this.cascades[pileNum].size()) {
      throw new IllegalArgumentException("Invalid card number.");
    }
  }

  void validateFoundationNum(int foundationPile) {
    if (foundationPile < 0 || foundationPile >= this.foundations.length) {
      throw new IllegalArgumentException("Invalid foundation pile.");
    }
  }

  // validates the given deck, number of piles, and number of draw cards.
  protected void validateContents(List<Card> deck, int numPiles, int numDraw) {
    this.validateDeck(deck); // validate the deck

    int pileCards = this.getTotalCascades(numPiles); // get total cascade cards

    // check conditions for number of piles and number of draw cards
    if (pileCards > deck.size() || numPiles < 0) {
      throw new IllegalArgumentException("Number of cascade piles is impossible.");
    }
    if (numDraw <= 0) {
      throw new IllegalArgumentException("Number of draw cards must be a positive number.");
    }
  }

  // validates the given deck
  protected void validateDeck(List<Card> deck) throws IllegalArgumentException {
    if (deck == null || deck.stream().anyMatch(Objects::isNull) || deck.isEmpty()) {
      throw new IllegalArgumentException("Invalid deck.");
    } // check for null/empty deck or null cards in the deck
    ArrayList<Card> deckCopy = new ArrayList<>(deck); // copy the deck to avoid mutation

    ArrayList<ArrayList<Card>> runs = new ArrayList<>();    // create 2d array for runs
    for (Card card : deckCopy) {
      if (card.toString().contains("A")) { // if a card is an ace, create a new run
        runs.add(new ArrayList<>(List.of(card)));
      }
    }
    for (ArrayList<Card> run : runs) { // remove the aces from the deck
      deckCopy.remove(run.get(0));
    }

    for (Card card : deckCopy) { // iterate through every card in the deck
      boolean added = false; // initialize boolean to see if the card was added to a run
      for (ArrayList<Card> run : runs) { // iterate through every run to see if the card belongs
        // Check if the current card has the same suit and isn't a duplicate in the current run
        boolean correctSuit = KlondikeCard.getSuit(card).equals(KlondikeCard.getSuit(run.get(0)));
        boolean duplicate = run.stream().anyMatch(c -> c.equals(card));
        if (correctSuit && !duplicate) { // add card to the current run if the checks pass
          run.add(card);
          added = true; // update the added boolean to show that the card was added
          break;
        }
      }
      if (!added) { // if the card wasn't added to any pile, throw exception
        throw new IllegalArgumentException("Invalid deck.");
      }
    }
    this.validateRuns(runs); // validate the runs
  }

  // validates the given runs of a deck.
  protected void validateRuns(ArrayList<ArrayList<Card>> runs) {
    // get the length and maximum value of the first run
    int size = runs.get(0).size();
    int maxValue = runs.get(0).stream().mapToInt(KlondikeCard::getValue).max().getAsInt();
    if (size != maxValue) { // if the length and max value aren't equal, the deck is invalid
      throw new IllegalArgumentException("Invalid deck.");
    }

    // iterate through the rest of the runs
    for (ArrayList<Card> run : runs.subList(1, runs.size())) {
      int size2 = run.size(); // get length
      int maxValue2 = run.stream().mapToInt(KlondikeCard::getValue).max().getAsInt(); // get max val
      // check that length and max value are the same as the first run
      if (size2 != size || maxValue2 != maxValue) {
        throw new IllegalArgumentException("Invalid deck.");
      }
    }
  }

  // Returns the total number of cascade cards using the given number of piles.
  protected int getTotalCascades(int numPiles) {
    if (numPiles <= 0) { // check that the number of piles is positive
      throw new IllegalArgumentException("Number of piles must be positive.");
    }

    // use for loop to calculate the number of cascade cards that would be dealt with this
    // number of piles
    int pileCards = 0;
    for (int i = numPiles; i > 0; i-- ) {
      pileCards += i;
    }
    return pileCards; // return the number of pile cards
  }

  // Initializes the foundation pile array.
  protected void initFoundation(List<Card> deck) {
    // count the number of aces in the deck
    int aces = 0;
    for (Card card : deck) {
      if (card.toString().contains("A")) {
        aces++;
      }
    }
    this.foundations = new Card[aces]; // initialize the foundation array with correct size
  }

  // Initializes the visibility hashmap.
  protected void initVisibility(List<Card> deck) {
    this.visibility = new HashMap<Card, Boolean>(); // create a new hashmap
    for (Card c : deck) {
      this.visibility.put(c, true); // initialize all cards to visible (true)
    }
  }

  // Initialize the cascade piles and deal cards into them using the given deck and number of piles.
  protected void dealCascades(ArrayList<Card> deck, int numPiles) {
    this.cascades = new ArrayList[numPiles]; // initialize cascades using number of piles
    for (int i = 0; i < numPiles; i++) { // iterate through the outer array
      this.cascades[i] = new ArrayList<>(); // start a new list for each pile
    }

    int leftCol = 0; // left most pile that needs more cards in it
    for (int row = 0; row < numPiles; row++) { // iterates through rows
      for (int pile = leftCol; pile < numPiles; pile++) { // iterates through piles (columns)
        Card c = deck.remove(0); // remove the card from the deck
        this.cascades[pile].add(c); // add it to the current cascade pile
        if (row != pile) { // change visibility of cards NOT at the top of the pile to false
          this.visibility.put(c, false);
        }
      }
      leftCol++; // increase the left column
    }

    // after all cascade cards are dealt, put the rest of the deck in the draw pile
    this.draws = deck;
  }

  // check if the given ArrayList of cards where the 0th index is the top most card (when
  // looking at a text view) is a valid move onto the given destination pile
  protected void validatePileMove(ArrayList<Card> cardsToMove, int destPile) {
    Card bottom = cardsToMove.get(0);

    if (this.cascades[destPile].isEmpty()) { // check if the destination pile is empty
      if (!cardsToMove.get(0).toString().contains("K")) {
        // if the bottom card is NOT a king, invalid move
        throw new IllegalStateException("A card must be a king to move it to an empty pile.");
      }
      else { // if the bottom card is a king, valid move so return
        return;
      }
    }

    Card top = this.cascades[destPile].get(this.cascades[destPile].size() - 1);
    boolean bottomRed = bottom.toString().contains("♡") || bottom.toString().contains("♢");
    boolean topRed = top.toString().contains("♡") || top.toString().contains("♢");
    int bottomValue = KlondikeCard.getValue(bottom);
    int topValue = KlondikeCard.getValue(top);
    // check that cards have opposite color and that the card going on top
    // (the lower card if you think about textual view) has a value that's one less
    if (bottomRed == topRed || topValue != (bottomValue + 1)) {
      throw new IllegalStateException("This move is not logically possible.");
    }
  }

  // check if the given card is a valid move onto the given foundation pile
  protected void validateFoundationMove(Card cardToMove, int foundationPile) {
    if (this.foundations[foundationPile] == null) { // check if the foundation pile is empty
      if (!cardToMove.toString().contains("A")) { // if the card to move is NOT an ace, invalid move
        throw new IllegalStateException("An ace must be the first card in a foundation pile.");
      }
      else { // if the card to move is an ace, valid move so return
        return;
      }
    }

    Card foundationCard = this.foundations[foundationPile];
    boolean sameSuit = KlondikeCard.getSuit(cardToMove)
            .equals(KlondikeCard.getSuit(foundationCard));
    int bottomValue = KlondikeCard.getValue(cardToMove);
    int topValue = KlondikeCard.getValue(foundationCard);
    // check that the cards have the same suit and that the card going on top
    // has a value that's one more than the one below it
    if (!sameSuit || topValue != (bottomValue - 1)) {
      throw new IllegalStateException("This move is not logically possible.");
    }
  }

  // return whether there are any valid foundation moves left to be made, not including draw cards
  protected boolean anyFoundationMoves() {
    for (ArrayList<Card> pile : this.cascades) { // loop through all piles
      if (pile.isEmpty()) { // if the pile is empty
        continue; // move on to the next pile
      }
      Card cardToMove = pile.get(pile.size() - 1); // get the card at the bottom of the pile
      // loop through all foundation piles
      for (int foundationIdx = 0; foundationIdx < this.foundations.length; foundationIdx++) {
        try { // try moving the current card to the current foundation pile
          this.validateFoundationMove(cardToMove, foundationIdx);
          return true; // if the move is valid, there is at least one foundation move so return true
        }
        catch (Exception ignored) { }
      }
    }
    return false; // if no valid foundation moves were found, return false
  }

  // return whether there are any valid pile moves left to be made, not including draw cards
  protected boolean anyPileMoves() {
    for (ArrayList<Card> srcPile : this.cascades) { // loop over all piles to get a card from
      for (int srcCardIdx = 0; srcCardIdx < srcPile.size(); srcCardIdx++) {
        // loop over all cards to move using index
        Card cardToMove = srcPile.get(srcCardIdx); // get the card to be moved
        if (!this.visibility.get(cardToMove)) { // if this card is not visible
          continue; // continue to the next card
        }
        for (ArrayList<Card> destPile : this.cascades) { // loop over all piles to move a card to
          for (int destCardIdx = 0; destCardIdx < destPile.size(); destCardIdx++) {
            // loop over all destination card indexes
            try { // try to move a build starting with this card to each pile
              // get the build
              ArrayList<Card> cardsToMove =
                      new ArrayList<>(srcPile.subList(srcCardIdx, srcPile.size()));
              this.validatePileMove(cardsToMove, destCardIdx); // try the move
              return true; // if the move is valid, there is at least one pile move so return true
            }
            catch (Exception ignored) { }
          }
        }
      }
    }
    return false; // if no valid pile moves were found, return false;
  }
}
