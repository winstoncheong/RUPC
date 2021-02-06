import java.util.ArrayList;
import java.util.Scanner;

public class Transposition{
	private static char[][] characters = new char[5][5];

	public static String transposition(String text, String keyword, boolean decipher){
		String transposed = "";
		ArrayList<Character> chars = new ArrayList<Character>();
		for(int x = 0; x < keyword.length(); x++)
			// retain unique letters
			if(!chars.contains(keyword.charAt(x)))
				chars.add(keyword.charAt(x));
		keyword = "";
		String numericKey = "";
		while(chars.size() > 5)
			// shortens chars down to 5 characters
			chars.remove(chars.size() - 1);

		if(chars.size() < 5){
			keyword = "ABCDE";
			for(char x = 0; x < 5; x++)
				// numericKey is generated as 01234
				numericKey += x;
		}else{
			for(char x: chars)
				// assignment of keyword
				keyword += x;
			for(char x = 'A', n = 0; x <= 'Z'; x++)
				if(chars.contains(x)){
					chars.set(chars.indexOf(x), n); // replaces chars with the number representing their order.
					n++;
				}
			for(char x: chars)
				// assignment of numeicKey
				numericKey = numericKey + x;
		}

		for(int x = 0; x < text.length(); x += 25){// loops through text in groups of 25 //extra unnecessary precaution useful only if
													// text.length()>25
			String segment = "";
			if(x + 25 < text.length()) // takes group of 25
				segment = text.substring(x, x + 25);
			else
				segment = text.substring(x);
			if(segment.length() < 25)
				segment += "ABCDEFGHIJKLMNOPQRSTUVWX".substring(0, 25 - segment.length());// adds to segment the amount needed to make it's length 25
			if(decipher) // reordering occurs first if deciphering
				segment = reorder(segment, numericKey, decipher);
			putInto(segment, decipher);
			segment = takeFrom(decipher);
			if(!decipher) // and second if enciphering
				segment = reorder(segment, numericKey, decipher);
			transposed += segment;
		}
		return transposed;
	}

	private static String reorder(String text, String numericKeyword, boolean decipher){
		String ordered = "";
		for(char x = 0; x < 5; x++){
			if(decipher){ // if deciphering
				int start = numericKeyword.charAt(x) * 5; // takes numeral at the xth position of numericKeyword
				ordered += text.substring(start, start + 5); // and adds that section of 5 letters to ordered
			}else{ // if enciphering
				int start = numericKeyword.indexOf(x) * 5; // finds location of the number x
				ordered += text.substring(start, start + 5); // and addes that labeled section to ordered
			}
		}
		return ordered;
	}

	// is this considered optimized?
	private static void putInto(String text, boolean decipher){
		int x = 0;
		for(int a = 0; a < 5; a++){
			for(int b = 0; b < 5; b++){
				if(decipher) // if deciphering, places into characters array vertically
					characters[b][a] = text.charAt(x);
				else
					// if enciphering, places into characters array horizontally
					characters[a][b] = text.charAt(x);
				x++;
			}
		}
	}

	private static String takeFrom(boolean decipher){
		String transformed = "";
		for(int a = 0; a < 5; a++){
			for(int b = 0; b < 5; b++){
				if(decipher) // if deciphering, takes horizontally
					transformed += characters[a][b];
				else
					// if enciphering, takes vertically
					transformed += characters[b][a];
			}
		}
		return transformed;
	}

	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		System.out.println(transposition("ABCDEFGHIJKLMNOPQRSTUVWXY", "SNAPE", false));
		System.out.println(transposition("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.", "HOGWARTS", false));

		sc.close();
	}
}