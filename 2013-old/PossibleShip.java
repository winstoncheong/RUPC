import java.util.Set;

public class PossibleShip{

	private boolean vert;
	// length is the length of the entire ship, not just the extension from the head.
	private int i, j, length;

	/**
	 * Constructs a PossibleShip object used for handling the case of unsunk ships.
	 * 
	 * @param vert <code>true</code> means the ship is oriented vertically. <code>false</code> means the ship is oriented horizontally.
	 * @param i beginning row position
	 * @param j beginning column position
	 * @param length Length of the entire ship. Not just the extension of the head.
	 */
	public PossibleShip(boolean vert, int i, int j, int length){
		this.vert = vert;
		this.i = i;
		this.j = j;
		this.length = length;
	}

	public boolean isVert(){
		return vert;
	}

	public boolean coversLocation(int i, int j){
		if(vert)
			return this.j == j && i >= this.i && i - this.i < length;
		// horizontally
		return this.i == i && j >= this.j && j - this.j < length;
	}

	/**
	 * Increases the length of the possible ship by 1, because it has found an adjacent unsunk location.
	 */
	public void lengthen(){
		length++;
	}

	public int getI(){
		return i;
	}

	public int getJ(){
		return j;
	}

	public int getLength(){

		return length;
	}

	// public boolean equals(Object o){
	// if(!(o instanceof PossibleShip))
	// return false;
	//
	//
	// }

	public String toString(){
		return Board.stringifyPosition(i, j) + " len:" + length + " orient:" + (vert ? "vert" : "horiz");
	}
}