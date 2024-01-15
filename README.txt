In order to simplify the abstraction process, I changed the logic of the game action methods
movePile, moveDraw, moveToFoundation, and moveDrawToFoundation (discardDraw is the only action
method that stayed the same) in BasicKlondike.
The methods now follow this logical format:
1. Ensure that the game state and arguments are valid for this move (using helpers)
2. Use a helper to check if the logic of the move is valid (ex: validateMovePile, validate)
3. Finally, actually move the cards

I also changed the logic of the isGameOver method to follow this format:
1. Check if there are any draw cards, if there are return false
2. Check if there are any moves to a foundation pile or moves from pile to pile left to be made
3. If there are no moves left, return true.

Deleted helper methods:
    void checkValidMovePile(Card bottom, Card top)
    void movePileValidated(int srcPile, int destPile, int bottomCardIndex)
    void checkValidMoveFoundation(Card bottom, Card top)
    void moveToFoundationValidated(int srcPile, int foundationPile, Card pileCard)
    void noValidMovesLeft(ArrayList<Card> cardsToMove)

New helper methods:
    void validatePileMove(ArrayList<Card> cardsToMove, int destPile)
    void validateFoundationMove(Card cardToMove, int foundationPile)
    void anyFoundationMoves()
    void anyPileMoves()

