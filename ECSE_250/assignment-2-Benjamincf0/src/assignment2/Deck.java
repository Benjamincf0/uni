package assignment2;

import java.util.Random;

public class Deck {
	public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
	public static Random gen = new Random();

	public int numOfCards; // contains the total number of cards in the deck
	public Card head; // contains a pointer to the card on the top of the deck

	/* 
	 * TODO: Initializes a Deck object using the inputs provided
	 */
	public Deck(int numOfCardsPerSuit, int numOfSuits) {
		/**** ADD CODE HERE ****/
		if (suitsInOrder.length < numOfSuits || numOfSuits < 1 || numOfCardsPerSuit > 13 || numOfCardsPerSuit < 1) {
			throw new IllegalArgumentException("Wrong numOfCardsPerSuit or numOfSuits");
		}

		Card current = head;
		for (int i = 0; i < numOfSuits; i++) {
			for (int j = 1; j <= numOfCardsPerSuit; j++) {
				if (head == null) {
					head = new PlayingCard(suitsInOrder[i], j);
					current = head;
					continue;
				}
				Card newCard = new PlayingCard(suitsInOrder[i], j);
				newCard.prev = current;
				current.next = newCard;
				current = current.next;
			}
		}
		Card redJoker = new Joker("red");
		redJoker.prev = current;
		current.next = redJoker;
		current = current.next;

		Card blackJoker = new Joker("black");
		blackJoker.prev = current;
		current.next = blackJoker;
		current = current.next;

		current.next = head;
		head.prev = current;
		
		numOfCards = numOfSuits * numOfCardsPerSuit + 2;
	}

	/* 
	 * TODO: Implements a copy constructor for Deck using Card.getCopy().
	 * This method runs in O(n), where n is the number of cards in d.
	 */
	public Deck(Deck d) {
		/**** ADD CODE HERE ****/
		if (d != null && d.head != null) {
			Card c1 = d.head;
			head = c1.getCopy();
			Card c2 = head;
			while(c1.next != d.head) {
				Card nextCard = c1.next.getCopy();
				c2.next = nextCard;
				nextCard.prev = c2;

				c1 = c1.next;
				c2 = c2.next;
			}

			c2.next = head;
			head.prev = c2;
			numOfCards = d.numOfCards;
		}
	}

	/*
	 * For testing purposes we need a default constructor.
	 */
	public Deck() {}

	/* 
	 * TODO: Adds the specified card at the bottom of the deck. This 
	 * method runs in $O(1)$. 
	 */
	public void addCard(Card c) {
		/**** ADD CODE HERE ****/
		if (c == null) {
			return;
		}
		if (numOfCards == 0) {
			head = c;
			head.next = head;
			head.prev = head;
		} else {
			Card tail = head.prev;
			tail.next = c;
			head.prev = c;
	
			c.next = head;
			c.prev = tail;
		}

		numOfCards++;
	}

	/*
	 * TODO: Shuffles the deck using the algorithm described in the pdf. 
	 * This method runs in O(n) and uses O(n) space, where n is the total 
	 * number of cards in the deck.
	 */
	public void shuffle() {
		/**** ADD CODE HERE ****/ 
		if (numOfCards == 0) {
			return;
		}
		// copy values into array;
		Card[] a = new Card[numOfCards];
		Card current = head;
		for (int i = 0; i < numOfCards; i++) {
			a[i] = current;
			current = current.next;
		}

		// shuffle
		for (int i = numOfCards - 1; i >= 1; i--) {
			int j = gen.nextInt(i + 1);
			Card temp = a[j];
			a[j] = a[i];
			a[i] = temp;
		}

		// rebuild shuffled deck
		head = a[0];
		current = head;
		for (int i = 0; i < a.length-1; i++) {
			Card nextCard = a[i+1];
			current.next = nextCard;
			nextCard.prev = current;

			current = current.next;
		}
		current.next = head;
		head.prev = current;
	}

	/*
	 * TODO: Returns a reference to the joker with the specified color in 
	 * the deck. This method runs in O(n), where n is the total number of 
	 * cards in the deck. 
	 */
	public Joker locateJoker(String color) {
		/**** ADD CODE HERE ****/
		if (head == null) {
			return null;
		}
		Card current = head;
		do {
			if (current instanceof Joker && ((Joker) current).getColor().equals(color)) {
				return (Joker) current;
			}
			current = current.next;
		} while (current != head);
		return null;
	}

	/*
	 * TODO: Moved the specified Card, p positions down the deck. You can 
	 * assume that the input Card does belong to the deck (hence the deck is
	 * not empty). This method runs in O(p).
	 */
	public void moveCard(Card c, int p) {
		/**** ADD CODE HERE ****/
		if (p == 0) {
			return;
		}
		// fill hole
		c.prev.next = c.next;
		c.next.prev = c.prev;

		// locate new neighbours
		Card newPrev = c;
		for (int i = 0; i < p; i++) {
			newPrev = newPrev.next;
		}

		// insert Card
		c.prev = newPrev;
		c.next = newPrev.next;

		newPrev.next = c;
		c.next.prev = c;
	}

	/*
	 * TODO: Performs a triple cut on the deck using the two input cards. You 
	 * can assume that the input cards belong to the deck and the first one is 
	 * nearest to the top of the deck. This method runs in O(1)
	 */
	public void tripleCut(Card firstCard, Card secondCard) {
		/**** ADD CODE HERE ****/
		if (firstCard == head) {
			head = secondCard.next;
			return;
		}
		if (secondCard == head.prev) {
			head = firstCard;
			return;
		}
		Card bottom = head.prev;
		
		Card newTop = secondCard.next;
		Card newBot = firstCard.prev;
		  
		secondCard.next = head;
		head.prev = secondCard;

		firstCard.prev = bottom;
		bottom.next = firstCard;

		newTop.prev = newBot;
		newBot.next = newTop;

		head = newTop;
	}

	/*
	 * TODO: Performs a count cut on the deck. Note that if the value of the 
	 * bottom card is equal to a multiple of the number of cards in the deck, 
	 * then the method should not do anything. This method runs in O(n).
	 */
	public void countCut() {
		/**** ADD CODE HERE ****/
		Card bottom = head.prev;
		int num = bottom.getValue() % numOfCards;

		if (num == 0) {
			return;
		}

		Card botPrev = head;
		for (int i = 0; i < num - 1; i++) {
			botPrev = botPrev.next;
		}

		bottom.prev.next = head;
		head.prev = bottom.prev;

		head = botPrev.next;

		bottom.prev = botPrev;
		botPrev.next = bottom;

		bottom.next = head;
		head.prev = bottom;


	}

	/*
	 * TODO: Returns the card that can be found by looking at the value of the 
	 * card on the top of the deck, and counting down that many cards. If the 
	 * card found is a Joker, then the method returns null, otherwise it returns
	 * the Card found. This method runs in O(n).
	 */
	public Card lookUpCard() {
		/**** ADD CODE HERE ****/
		int num = head.getValue();
		Card c = head;
		for (int i = 0; i < num; i++) {
			c = c.next;
		}
		if (c instanceof PlayingCard) {
			return c;
		}
		return null;
	}

	/*
	 * TODO: Uses the Solitaire algorithm to generate one value for the keystream 
	 * using this deck. This method runs in O(n).
	 */
	public int generateNextKeystreamValue() {
		/**** ADD CODE HERE ****/
		Card rj = locateJoker("red");
		Card bj = locateJoker("black");

		Card c = null;
		while (c == null) {
			moveCard(rj, 1);
			moveCard(bj, 2);

			Card j1 = head;
			while (!(j1 instanceof Joker)) {
				j1 = j1.next;
			}
			Card j2;
			if (j1 == rj) {
				j2 = bj;
			} else {
				j2 = rj;
			}
			tripleCut(j1, j2);
			countCut();
			c = lookUpCard();
		}

		return c.getValue();
	}


	public abstract class Card { 
		public Card next;
		public Card prev;

		public abstract Card getCopy();
		public abstract int getValue();

	}

	public class PlayingCard extends Card {
		public String suit;
		public int rank;

		public PlayingCard(String s, int r) {
			this.suit = s.toLowerCase();
			this.rank = r;
		}

		public String toString() {
			String info = "";
			if (this.rank == 1) {
				//info += "Ace";
				info += "A";
			} else if (this.rank > 10) {
				String[] cards = {"Jack", "Queen", "King"};
				//info += cards[this.rank - 11];
				info += cards[this.rank - 11].charAt(0);
			} else {
				info += this.rank;
			}
			//info += " of " + this.suit;
			info = (info + this.suit.charAt(0)).toUpperCase();
			return info;
		}

		public PlayingCard getCopy() {
			return new PlayingCard(this.suit, this.rank);   
		}

		public int getValue() {
			int i;
			for (i = 0; i < suitsInOrder.length; i++) {
				if (this.suit.equals(suitsInOrder[i]))
					break;
			}

			return this.rank + 13*i;
		}

	}

	public class Joker extends Card{
		public String redOrBlack;

		public Joker(String c) {
			if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
				throw new IllegalArgumentException("Jokers can only be red or black"); 

			this.redOrBlack = c.toLowerCase();
		}

		public String toString() {
			//return this.redOrBlack + " Joker";
			return (this.redOrBlack.charAt(0) + "J").toUpperCase();
		}

		public Joker getCopy() {
			return new Joker(this.redOrBlack);
		}

		public int getValue() {
			return numOfCards - 1;
		}

		public String getColor() {
			return this.redOrBlack;
		}
	}

}
