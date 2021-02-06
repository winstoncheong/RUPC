import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Year2006{
	public enum direction{
		up, down, left, right
	};

	public static void run(BufferedReader r) throws NumberFormatException, IOException{
		int datasets = Integer.parseInt(r.readLine());
		for(int i = 0; i < datasets; i++){
			System.out.println("Configuration " + (i + 1));
			String[] strarr = r.readLine().split(" ");
			int row = Integer.parseInt(strarr[0]);
			int col = Integer.parseInt(strarr[1]);
			int[][] numarr = new int[row][col];

			for(int x = 0; x < row; x++){// load all nums
				String[] numline = r.readLine().replaceAll("  ", " ").trim().split(" ");// replace all doublely spaced with single space, split by
																						// space
				for(int y = 0; y < col; y++){
					numarr[x][y] = Integer.parseInt(numline[y]);
				}
			}

			System.out.println("\tcurrent configuration:");
			print2DArray(numarr);
			System.out.println("\tvalue: " + manhattanDistance(numarr));

			direction[] directions = direction.values();// get all enum directions
			for(direction d: directions){
				System.out.print("\n\tslide tile " + d + ":");
				move(numarr, d);
			}

			System.out.println();
		}
	}

	public static int manhattanDistance(int[][] arr){
		int distance = 0;
		int total = arr.length * arr[0].length;
		for(int i = 1; i < total; i++){
			int[] loc = findElement(arr, i);
			int c = (i - 1) % arr[0].length;// the row i should be in
			int r = (i - 1) / arr[0].length;// the col i should be in
			// System.out.println(r+" "+c);
			distance += Math.abs(r - loc[0]) + Math.abs(c - loc[1]);// calculate the manhattan distance for single point
			// System.out.println("dist:"+distance);
		}
		return distance;
	}

	/*
	 * returns array with the nth row and mth col that has the element n
	 * counting starts at 0
	 */
	public static int[] findElement(int[][] arr, int n){
		for(int i = 0; i < arr.length; i++){
			for(int j = 0; j < arr[0].length; j++){
				if(arr[i][j] == n)
					return new int[]{i, j};
			}
		}

		return null;
	}

	/*
	 * Tries to move element '0' in direction d.
	 * Prints out array and manhattan distance if possible, "impossible" if impossible
	 */
	public static void move(int[][] arr, direction d){
		int[] loc = findElement(arr, 0);
		int[][] copy = new int[arr.length][arr[0].length];

		// copy the array
		for(int i = 0; i < arr.length; i++)
			for(int j = 0; j < arr[0].length; j++)
				copy[i][j] = arr[i][j];

		boolean possible = true;
		switch(d){
			case down:
				if(loc[0] == 0)
					possible = false;
				else{
					copy[loc[0]][loc[1]] = copy[loc[0] - 1][loc[1]];
					copy[loc[0] - 1][loc[1]] = 0;
				}
				break;
			case up:
				if(loc[0] == arr.length - 1)
					possible = false;
				else{
					copy[loc[0]][loc[1]] = copy[loc[0] + 1][loc[1]];
					copy[loc[0] + 1][loc[1]] = 0;
				}
				break;
			case right:
				if(loc[1] == 0)
					possible = false;
				else{
					copy[loc[0]][loc[1]] = copy[loc[0]][loc[1] - 1];
					copy[loc[0]][loc[1] - 1] = 0;
				}
				break;
			case left:
				if(loc[1] == arr[0].length - 1)
					possible = false;
				else{
					copy[loc[0]][loc[1]] = copy[loc[0]][loc[1] + 1];
					copy[loc[0]][loc[1] + 1] = 0;
				}

		}
		if(possible){
			System.out.println();
			print2DArray(copy);
			System.out.println("\tvalue:" + manhattanDistance(copy));
		}else
			System.out.println("impossible");

	}

	public static void print2DArray(int[][] arr){
		int length = (int) Math.log10(arr.length * arr[0].length) + 1;
		for(int i = 0; i < arr.length; i++){
			System.out.print("\t\t");
			for(int j = 0; j < arr[i].length; j++){
				for(int k = 0; k < length - Integer.toString(arr[i][j]).length() + 1; k++)
					System.out.print(" ");
				System.out.print(arr[i][j]);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws NumberFormatException, IOException{
		// DownloadFiles.downloadSubFiles("http://elvis.rowan.edu/rupc/Problems/2006");
		// run(new BufferedReader(new InputStreamReader(System.in)));
		runTest("sample1.txt");
		runTest("sample2.txt");
		runTest("sample3.txt");
		runTest("testdata.txt");
		// print2DArray(new int[][]{{1,2,3},{4,5,6},{7,0,8}});
		// System.out.println(manhattanDistance(new int[][]{{1,2,3},{4,0,6},{7,5,8}}));

	}

	private static void runTest(String fileName) throws NumberFormatException, IOException{
		BufferedReader r = null;
		try{
			r = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/Rowan/Year2006/" + fileName)));
			r.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		run(r);
	}
}