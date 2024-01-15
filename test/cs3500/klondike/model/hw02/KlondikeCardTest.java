package cs3500.klondike.model.hw02;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for the KlondikeCard class.
 */
public class KlondikeCardTest {
  Card kCard1;
  Card kCard2;

  @Before
  public void setUp() throws Exception {
    this.kCard1 = new KlondikeCard(CardValue.ACE, CardSuit.HEARTS);
    this.kCard2 = new KlondikeCard(CardValue.TEN, CardSuit.SPADES);
  }

  //          tests for CardValue
  @Test
  public void testCardValueToString() {
    CardValue ace = CardValue.ACE;
    Assert.assertEquals("A", ace.toString());
    CardValue ten = CardValue.TEN;
    Assert.assertEquals("10", ten.toString());
  }

  //          tests for CardSuit
  @Test
  public void testCardSuitToString() {
    CardSuit diamonds = CardSuit.DIAMONDS;
    Assert.assertEquals("♢", diamonds.toString());
    CardSuit clubs = CardSuit.CLUBS;
    Assert.assertEquals("♣", clubs.toString());
  }


  //            tests for KlondikeCard
  // test for constructor exceptions
  @Test(expected = NullPointerException.class)
  public void testCardNullValue() {
    Card badCard = new KlondikeCard(null, CardSuit.DIAMONDS);
  }

  @Test(expected = NullPointerException.class)
  public void testCardNullSuit() {
    Card badCard = new KlondikeCard(CardValue.FIVE, null);
  }


  // tests for toString
  @Test
  public void testKlondikeCardToString() {
    Assert.assertEquals("A♡", this.kCard1.toString());
    Assert.assertEquals("10♠", this.kCard2.toString());
  }


  // tests for equals
  @Test
  public void testEqualsWrongObject() {
    Assert.assertFalse(this.kCard1.equals(new Object()));
  }

  @Test
  public void testEqualsSameCard() {
    Assert.assertTrue(this.kCard2.equals(this.kCard2));
  }

  @Test
  public void testEqualsDifferentWrongCard() {
    Assert.assertFalse(this.kCard2.equals(this.kCard1));
  }

  @Test
  public void testEqualsDifferentButSameCard() {
    Card aceOfHearts = new KlondikeCard(CardValue.ACE, CardSuit.HEARTS);
    Assert.assertTrue(aceOfHearts.equals(this.kCard1));
  }


  // tests for hashcode
  @Test
  public void testHashCodeIsDifferentForNotEqualCards() {
    Assert.assertNotEquals(this.kCard1.hashCode(), this.kCard2.hashCode());
  }

  @Test
  public void testHashCodeIsSameForEqualCards() {
    Card aceOfHearts = new KlondikeCard(CardValue.ACE, CardSuit.HEARTS);
    Assert.assertEquals(this.kCard1.hashCode(), aceOfHearts.hashCode());
  }

  // tests for getValue
  @Test
  public void testCardValueGetValue() {
    Card ace = new KlondikeCard(CardValue.ACE, CardSuit.CLUBS);
    Card three = new KlondikeCard(CardValue.THREE, CardSuit.DIAMONDS);
    Card ten = new KlondikeCard(CardValue.TEN, CardSuit.HEARTS);
    Card king = new KlondikeCard(CardValue.KING, CardSuit.SPADES);
    Assert.assertEquals(1, KlondikeCard.getValue(ace));
    Assert.assertEquals(3, KlondikeCard.getValue(three));
    Assert.assertEquals(10, KlondikeCard.getValue(ten));
    Assert.assertEquals(13, KlondikeCard.getValue(king));
  }

  // tests for getSuit
  @Test
  public void testCardValueGetSuit() {
    Card ace = new KlondikeCard(CardValue.ACE, CardSuit.CLUBS);
    Card three = new KlondikeCard(CardValue.THREE, CardSuit.DIAMONDS);
    Card ten = new KlondikeCard(CardValue.TEN, CardSuit.HEARTS);
    Card king = new KlondikeCard(CardValue.KING, CardSuit.SPADES);
    Assert.assertEquals("♣", KlondikeCard.getSuit(ace));
    Assert.assertEquals("♢", KlondikeCard.getSuit(three));
    Assert.assertEquals("♡", KlondikeCard.getSuit(ten));
    Assert.assertEquals("♠", KlondikeCard.getSuit(king));
  }

  @Test (expected = NullPointerException.class)
  public void testCardValueGetValueInvalid() {
    KlondikeCard.getValue(null);
  }

}