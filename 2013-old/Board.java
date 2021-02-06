import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Board{

	// for debugging
	private static final boolean DEBUGGING = false;

	// used for printout of column position.
	private static final String letters = "ABCDEFGHIJ";

	// Strings populating the gameBoard. Used like enums, to allow readability.
	private static final String HIT = "2", BLANK = "0", MISS = "1", SUNK = "3";

	private String[][] gameBoard;

	// used in the normal case.
	private int[][] scores = new int[10][10];

	private boolean hasA, hasB, hasD, hasS, hasP, foundUnsunk;

	private String shipsConsidering;

	// calculated from the has* booleans from setShips method
	private int maxShipLength;

	// for when considering unsunk ships
	private Set<PossibleShip> possibleShips = new HashSet<PossibleShip>();// new TreeSet<PossibleShip>(new Comparator<PossibleShip>(){
	// public int value(PossibleShip o){
	// return 1000 * o.getLength() + 100 * o.getI() + 10 * o.getJ() + (o.isVert() ? 1 : 0);
	// }
	//
	// @Override
	// public int compare(PossibleShip o1, PossibleShip o2){
	// return value(o1) - value(o2);
	// }
	// });

	// for printout
	private int bestMoveValue;

	// for printout. Positions that have the best move score
	private Set<String> bestMovePositions = new TreeSet<String>(new Comparator<String>(){

		@Override
		public int compare(String o1, String o2){
			int val = o1.charAt(1) - o2.charAt(1);// # of first should be < than # of second
			if(val == 0)
				return o1.charAt(0) - o2.charAt(0);// letter of first should be < letter of second
			return val;
		}
	});

	// used to wean out possiblePositions
	private int largestPossibleShip;

	// for considering possible positions when have unsunk ships.
	private Set<Position> possiblePositions = new HashSet<Position>();

	/**
	 * Takes in the ships available in a space separated format.
	 * 
	 * @param ships
	 *            String of ships available for the game, space separated.
	 */
	public Board(String ships){
		setShips(ships);

		// reset vars in case
		bestMoveValue = 0;
		bestMovePositions.clear();
		largestPossibleShip = 0;
		possiblePositions.clear();
	}

	/**
	 * Takes in the ships space separated, and parses the string. Sets
	 * appropriate boolean parameters
	 * 
	 * @param ships
	 *            String with ships available space separated.
	 */
	private void setShips(String shipsAvailable){
		shipsConsidering = shipsAvailable;
		if(shipsAvailable.contains("A"))
			hasA = true;
		if(shipsAvailable.contains("B"))
			hasB = true;
		if(shipsAvailable.contains("D"))
			hasD = true;
		if(shipsAvailable.contains("S"))
			hasS = true;
		if(shipsAvailable.contains("P"))
			hasP = true;

		// calculate max ship length
		if(hasA)
			maxShipLength = 5;
		else if(hasB)
			maxShipLength = 4;
		else if(hasD || hasS)
			maxShipLength = 3;
		else
			// must have at least one ship.
			maxShipLength = 2;

	}

	/**
	 * Uses the game board passed in.
	 * 
	 * @param board
	 *            Board containing data on hits.
	 */
	public void setGameBoard(String[][] board){
		gameBoard = board;
	}

	public void computeBestMove(){
		// iterate through game board looking for unsunk
		for(int i = 0; i < gameBoard.length; i++){
			for(int j = 0; j < gameBoard[i].length; j++){
				if(gameBoard[i][j].equals(HIT)){// has unsunk
					foundUnsunk = true;
					// intelligently collect the unsunk locations/possible ships
					if(possibleShips.isEmpty()){
						// add both horiz and vert ship of len 1
						possibleShips.add(new PossibleShip(true, i, j, 1));
						possibleShips.add(new PossibleShip(false, i, j, 1));
						if(DEBUGGING)
							System.out.println(possibleShips);
					}else{
						// check if left is also unsunk. If so, get the ship from set, and add to length.
						if(j > 0 && gameBoard[i][j - 1].equals(HIT))
							for(PossibleShip s: possibleShips)
								if(s.coversLocation(i, j - 1) && !s.isVert()){
									s.lengthen();
									break; // only one such ship, so no more iterating
								}

						// check if top is also unsunk. if so, get the ship from set, and add to length;
						if(i > 0 && gameBoard[i - 1][j].equals(HIT))
							for(PossibleShip s: possibleShips)
								if(s.coversLocation(i - 1, j) && s.isVert()){
									s.lengthen();
									break;// only one such ship, so no more iterating.
								}
					}
				}
			}
		}

		if(foundUnsunk){
			// possible unsunk ship orientations have been collected.
			if(DEBUGGING)
				System.out.println("All possibleShips: " + possibleShips);

			// Remove ships that have both ends blocked (either by wall or miss) and replaces with single unit ships that are ortho
			// Also remove ships that are longer than the max length of ship allowed in the game instance.
			Iterator<PossibleShip> iter = possibleShips.iterator();
			Set<PossibleShip> splittedShips = new HashSet<>();
			while(iter.hasNext()){
				PossibleShip s = iter.next();
				if(DEBUGGING)
					System.out.println("Possible ship: " + s);
				boolean blocked;
				int i = s.getI(), j = s.getJ(), length = s.getLength();

				if(length > maxShipLength){
					iter.remove();
					continue;
				}

				if(s.isVert()){
					blocked = isBlocked(i - 1, j) && isBlocked(i + length, j);
				}else{ // horizontal
					blocked = isBlocked(i, j - 1) && isBlocked(i, j + length);
				}

				if(blocked){
					iter.remove();
					for(int k = 0; k < length; k++){
						splittedShips.add(new PossibleShip(!s.isVert(), i + (s.isVert() ? k : 0), j + (s.isVert() ? 0 : k), 1));
					}
				}
			}
			// ships that are bounded, incorrect have been removed.
			// possible ships have been isolated.

			// bounded ships were split. now add the ortho ships
			possibleShips.addAll(splittedShips);

			// Gets the set of spaces to be considered, and calculates the score of the space.
			for(PossibleShip s: possibleShips){
				int i = s.getI(), j = s.getJ(), length = s.getLength();

				// consider those spaces that extends to the largest line of unsunks
				if(length > largestPossibleShip){
					largestPossibleShip = length;
					possiblePositions.clear();
				}

				if(s.isVert()){

					// calculate the upper space
					int hlen = 0;
					for(int k = 0; k < i; k++){
						if(gameBoard[i - k - 1][j].equals(BLANK)) // this be blank or hit
							hlen++;
						else
							break;
					}

					// calculate the lower space
					int llen = 0;
					for(int k = 0; k < 10 - i - length; k++){ // ranges might be off
						if(gameBoard[i + length + k][j].equals(BLANK))// this be blank or hit
							llen++;
						else
							break;
					}

					int hscore = 0, lscore = 0, extension;
					// go through possible extension amounts (ships possible - current length
					// for each, accumulate the hscore and lowscore
					if(DEBUGGING)
						System.out.println(s);
					if(hasA){
						extension = 5 - length;
						hscore += calculatePossiblePositionScore(extension, hlen, llen);
						lscore += calculatePossiblePositionScore(extension, llen, hlen);
						if(DEBUGGING)
							System.out.println("From a:" + hscore + " " + lscore);
					}
					if(hasB){
						extension = 4 - length;
						hscore += calculatePossiblePositionScore(extension, hlen, llen);
						lscore += calculatePossiblePositionScore(extension, llen, hlen);
						if(DEBUGGING)
							System.out.println("From b:" + hscore + " " + lscore);
					}
					if(hasD){
						extension = 3 - length;
						hscore += calculatePossiblePositionScore(extension, hlen, llen);
						lscore += calculatePossiblePositionScore(extension, llen, hlen);
						if(DEBUGGING)
							System.out.println("From d:" + hscore + " " + lscore);
					}
					if(hasS){
						extension = 3 - length;
						hscore += calculatePossiblePositionScore(extension, hlen, llen);
						lscore += calculatePossiblePositionScore(extension, llen, hlen);
						if(DEBUGGING)
							System.out.println("From s:" + hscore + " " + lscore);
					}
					if(hasP){
						extension = 2 - length;
						hscore += calculatePossiblePositionScore(extension, hlen, llen);
						lscore += calculatePossiblePositionScore(extension, llen, hlen);
						if(DEBUGGING)
							System.out.println("From p:" + hscore + " " + lscore);
					}

					// add the scores to the position
					Position p = getPosition(possiblePositions, i - 1, j);
					if(p == null)
						possiblePositions.add(new Position(i - 1, j, hscore));
					else
						p.incrementScore(hscore);

					p = getPosition(possiblePositions, i + length, j);
					if(p == null)
						possiblePositions.add(new Position(i + length, j, lscore));
					else
						p.incrementScore(lscore);
				}else{ // horizontal

					// calculate the left space
					int llen = 0;
					for(int k = 0; k < j; k++){
						if(gameBoard[i][j - k - 1].equals(BLANK)) // this be blank or hit
							llen++;
						else
							break;
					}

					// calculate the right space
					int rlen = 0;
					for(int k = 0; k < 10 - j - length; k++){ // ranges might be off
						if(gameBoard[i][j + length + k].equals(BLANK))// this be blank or hit
							rlen++;
						else
							break;
					}

					int lscore = 0, rscore = 0, extension;
					// go through possible extension amounts (ships possible - current length
					// for each, accumulate the hscore and lowscore

					if(DEBUGGING)
						System.out.println(s);
					if(hasA){
						extension = 5 - length;
						lscore += calculatePossiblePositionScore(extension, llen, rlen);
						rscore += calculatePossiblePositionScore(extension, rlen, llen);
						if(DEBUGGING){
							System.out.println(extension + " " + llen + " " + rlen + " " + length);
							System.out.println("From a:" + lscore + " " + rscore);
						}
					}
					if(hasB){
						extension = 4 - length;
						lscore += calculatePossiblePositionScore(extension, llen, rlen);
						rscore += calculatePossiblePositionScore(extension, rlen, llen);
						if(DEBUGGING){
							System.out.println(extension + " " + llen + " " + rlen + " " + length);
							System.out.println("From b:" + lscore + " " + rscore);
						}
					}
					if(hasD){
						extension = 3 - length;
						lscore += calculatePossiblePositionScore(extension, llen, rlen);
						rscore += calculatePossiblePositionScore(extension, rlen, llen);
						if(DEBUGGING){
							System.out.println(extension + " " + llen + " " + rlen + " " + length);
							System.out.println("From d:" + lscore + " " + rscore);
						}
					}
					if(hasS){
						extension = 3 - length;
						lscore += calculatePossiblePositionScore(extension, llen, rlen);
						rscore += calculatePossiblePositionScore(extension, rlen, llen);
						if(DEBUGGING){
							System.out.println(extension + " " + llen + " " + rlen + " " + length);
							System.out.println("From s:" + lscore + " " + rscore);
						}
					}
					if(hasP){
						extension = 2 - length;
						lscore += calculatePossiblePositionScore(extension, llen, rlen);
						rscore += calculatePossiblePositionScore(extension, rlen, llen);
						if(DEBUGGING){
							System.out.println(extension + " " + llen + " " + rlen + " " + length);
							System.out.println("From p:" + lscore + " " + rscore);
						}
					}

					if(DEBUGGING){
						System.out.println("Lscore: " + lscore);
						System.out.println("Rscore: " + rscore);
					}
					// add the scores to the position
					Position p = getPosition(possiblePositions, i, j - 1);
					if(p == null)
						possiblePositions.add(new Position(i, j - 1, lscore));
					else
						p.incrementScore(lscore);

					p = getPosition(possiblePositions, i, j + length);
					if(p == null)
						possiblePositions.add(new Position(i, j + length, rscore));
					else
						p.incrementScore(rscore);
				}
			}
			// possible positions have been selected and scored.

			// look at ships of longest length, as they are higher in priority. ~ not needed anymore

			// System.out.println("Check ordering is right: " + possibleShips);
			if(DEBUGGING)
				System.out.println(possiblePositions);

			// iterate through possible positions and set bestMove value and position
			for(Position p: possiblePositions){
				if(bestMoveValue < p.getScore()){
					bestMoveValue = p.getScore();
					bestMovePositions.clear();
					bestMovePositions.add(stringifyPosition(p.getI(), p.getJ()));
				}else if(bestMoveValue == p.getScore())
					bestMovePositions.add(stringifyPosition(p.getI(), p.getJ()));
			}

			return;
		}
		// no unsunk
		// better not to use positions..

		// count positions by head of ship. Extending ship from position either right or down
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(!gameBoard[i][j].equals(BLANK)) // skip position if not blank
					continue;

				if(i != 10){// consider verticals
					// compute the largest length that is available for the
					// position
					int k = 1; // k is length of extension from head

					// min of largest ship avail and length movable from board
					int longestLength = Math.min(4, 9 - i);

					for(; k <= longestLength; k++){
						if(!gameBoard[i + k][j].equals(BLANK)){
							break; // found longest extension
						}
					}
					k--; // upon exit of for loop, k is 1+extension;

					// add to scores[] overlaying ships, using the booleans
					incrementValues(i, j, true, k); // if k = 0, just returns;

				}

				if(j != 10){// consider horizontal
					int k = 1; // k is length of extension from head

					// min of largest ship avail and length movable from board
					int longestLength = Math.min(4, 9 - j);

					for(; k <= longestLength; k++){
						if(!gameBoard[i][j + k].equals(BLANK)){
							break;// found longest extension
						}
					}
					k--; // upon exit of for loop, k is 1+extension;

					// increment values
					incrementValues(i, j, false, k);
				}
			}
		}

		// look for maximum in scores[].
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				if(scores[i][j] > bestMoveValue){
					bestMoveValue = scores[i][j];
					bestMovePositions.clear();
					bestMovePositions.add(stringifyPosition(i, j));
				}else if(scores[i][j] == bestMoveValue)
					bestMovePositions.add(stringifyPosition(i, j));
			}

		}
		// simple case is done.
	}

	/**
	 * Uses the formula
	 * x = min(x_len, extension) - if(extension-1>ylen) then extension-1-ylen else 0;
	 * 
	 * @param extension
	 * @param hlen
	 * @param llen
	 * @return
	 */
	private int calculatePossiblePositionScore(int extension, int xlen, int ylen){
		int x = Math.min(xlen, extension);
		if(extension - 1 > ylen)
			x -= extension - 1 - ylen;
		return(x > 0 ? x : 0);
	}

	/**
	 * Look through the passed in set for a position object with position i,j
	 * 
	 * @param set
	 * @param i
	 * @param j
	 * @return <code>null</code> if such a position is not found.
	 */
	private Position getPosition(Set<Position> set, int i, int j){
		for(Position p: set)
			if(p.getI() == i && p.getJ() == j)
				return p;
		return null;
	}

	/**
	 * Checks whether the location is blocked (is not blank)
	 * 
	 * @param i row
	 * @param j column
	 * @return whether location is blocked (i.e. is not blank)
	 */
	private boolean isBlocked(int i, int j){
		return i < 0 || i > 10 || j < 0 || j > 10 || !gameBoard[i][j].equals(BLANK);
	}

	public static String stringifyPosition(int i, int j){
		return Character.toString(letters.charAt(j)) + (i + 1);
	}

	/**
	 * Increments appropriate positions in the scores array based on found
	 * locations for ships. Considers ships through the boolean values has*
	 * 
	 * @param i
	 *            row position
	 * @param j
	 *            column position
	 * @param vertical
	 *            either vert or horiz extension
	 * @param k
	 *            length of extension (from 1 to 4)
	 */
	private void incrementValues(int i, int j, boolean vertical, int k){
		switch(k){ // use fallthrough ability of switch case to prevent code
					// duplication
			case 4:
				if(hasA)
					for(int k2 = 0; k2 <= 4; k2++){
						scores[i + (vertical ? k2 : 0)][j + (vertical ? 0 : k2)]++;// increment
																					// position
					}
			case 3:
				if(hasB)
					for(int k2 = 0; k2 <= 3; k2++){
						scores[i + (vertical ? k2 : 0)][j + (vertical ? 0 : k2)]++;// increment
																					// position
					}
			case 2:
				if(hasD || hasS)
					for(int k2 = 0; k2 <= 2; k2++){
						scores[i + (vertical ? k2 : 0)][j + (vertical ? 0 : k2)] += (hasD && hasS ? 2 : 1);// increment position
					}
			case 1:
				if(hasP)
					for(int k2 = 0; k2 <= 1; k2++){
						scores[i + (vertical ? k2 : 0)][j + (vertical ? 0 : k2)]++;// increment position
					}
			default:
				break;
		}
	}

	/**
	 * Called for printing out of result
	 * 
	 * @return highest score on the board.
	 */
	public int getBestMoveValue(){
		return bestMoveValue;
	}

	/**
	 * Called for printing out of result.
	 * 
	 * @return best moves.
	 */
	public String getBestMovePosition(){
		return bestMovePositions.toString();
	}

}