import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BattleshipMain{

	public static void run(BufferedReader r) throws IOException{
		int datasets = Integer.parseInt(r.readLine());
		System.out.println("Analyzing " + datasets + " data sets");
		for(int d = 0; d < datasets; d++){
			System.out.println("Data Set " + (d + 1));
			int ships = Integer.parseInt(r.readLine());// read #ships but dont need
			Board b = new Board(r.readLine()); // take line containing ships and pass to board
												// constructor
			// read board data in
			String[][] board = new String[10][10];
			for(int i = 0; i < 10; i++){ // get all lines
				String line = r.readLine();
				board[i] = line.split(" ");
			}
			b.setGameBoard(board);

			b.computeBestMove(); // make Board calculate best move.
			System.out.println("\tBest Move Value: " + b.getBestMoveValue() + " at " + b.getBestMovePosition());
		}

	}

	public static void main(String[] args) throws IOException{

		// run(new BufferedReader(new InputStreamReader(System.in)));
		 runTest("sample1.txt");
		runTest("sample2.txt");
		runTest("sample3.txt");
		// runTest("test1.txt");
		// runTest("test2.txt");
		// runTest("test3.txt");
	}

	private static void runTest(String fileName) throws IOException{
		BufferedReader r = null;
		String path = System.getProperty("user.dir");
		Package packageName = BattleshipMain.class.getPackage();
		try{
			if(packageName != null)// condition is removable
				r = new BufferedReader(new FileReader(
						path + File.separator + "Rowan" + File.separator + packageName.getName() + File.separator + fileName));
			else
				r = new BufferedReader(new FileReader(path + File.separator + fileName));
			run(r);
			r.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}

	}
}