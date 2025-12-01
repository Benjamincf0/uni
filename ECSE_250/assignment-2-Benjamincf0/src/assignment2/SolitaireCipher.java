package assignment2;

public class SolitaireCipher {
	public Deck key;

	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}

	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		int[] stream = new int[size];
		for (int i = 0; i < size; i++) {
			stream[i] = key.generateNextKeystreamValue();
		}
		return stream;
	}

	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		if (msg == null) {
			return "";
		}
		// Filter and convert the message to uppercase letters
		int[] a = new int[msg.length()];
		int j = 0; // a int[] counter
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			if (ch >= 'A' && ch <= 'Z') {
				a[j] = ch - 'A';
				j++;
			} else if (ch >= 'a' && ch <= 'z') {
				a[j] = ch - 'a';
				j++;
			}
		}

		// Generate the keystream
		int[] keystream = getKeystream(j);

		// Encode the message
		char[] out = new char[j];
		for (int i = 0; i < j; i++) {
			int encodedValue = (a[i] + keystream[i]) % 26;
			out[i] = (char) (encodedValue + 'A');
		}

		return new String(out);
	}

	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {
		if (msg == null) {
			return "";
		}

		 // Generate the keystream
		int[] keystream = getKeystream(msg.length());

		// Decode the message
		char[] chars = new char[msg.length()];
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			int decodedValue = (ch - 'A' - keystream[i] + 26) % 26;
			chars[i] = (char) (decodedValue + 'A');
		}

		return new String(chars);
	}

}
