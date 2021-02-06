public class Position{
	private int i, j, score;

	public Position(int i, int j, int score){
		this.i = i;
		this.j = j;
		this.score = score;
	}

	public void incrementScore(int score){
		this.score = score;
	}

	public int getScore(){
		return score;
	}

	public int getI(){
		return i;
	}

	public int getJ(){
		return j;
	}

	public String toString(){
		return "(" + i + ", " + j + ") score:" + score;
	}

}
