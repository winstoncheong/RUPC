import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Sudoku {
	static int [][] grid;
	static boolean printDeterminator=true;
	public static void run(Scanner sc){
		int g = sc.nextInt();
		System.out.println("Analyzing "+g+" Grids");
		for(int gridNum=0;gridNum<g;gridNum++){
			printDeterminator=true;
			System.out.println("  Grid "+(gridNum+1)+":");
			int s = sc.nextInt();//size
			System.out.println("    Size: "+ s+" x "+s);
			grid=new int[s][s];
			for(int i=0;i<s;i++)
				for(int j=0;j<s;j++)
					grid[i][j]=sc.nextInt();

			//testing grid
			boolean determinedCorrect=false;
			if(isPossibleSudoku(s)){
				determinedCorrect=testSudokuX();
				printDeterminator=true;
				if(!determinedCorrect){
					determinedCorrect=testSudoku();
					printDeterminator=true;
				}
			}
			if(!determinedCorrect)
				testLatinSquare();
		}
	}


	public static boolean testLatinSquare() {
		String type="Latin square";
		boolean correct=true;
		correct&=checkColumns(type);
		correct&=checkRows(type);
		if(correct)testUnsolved(type);
		return correct;
	}

	public static boolean testSudoku(){
		String type="Sudoku";
		boolean correct=true;
		correct&=checkBlocks(type);
		correct&=checkColumns(type);
		correct&=checkRows(type);
		if(correct)testUnsolved(type);
		return correct;
	}

	public static boolean testSudokuX(){
		String type="Sudoku-X";
		boolean correct=true;
		correct&=checkDiagonals(type);
		correct&=checkBlocks(type);
		correct&=checkColumns(type);
		correct&=checkRows(type);
		if(correct)testUnsolved(type);
		return correct;

	}

	public static void testUnsolved(String type){
		for(int i=0;i<grid.length;i++){
			for(int j=0;j<grid.length;j++)
				if(grid[i][j]==0){
					System.out.println("    Unsolved "+type);
					return;
				}
		}
		System.out.println("    Solved "+type);
	}

	private static boolean checkBlocks(String type) {
		int[] ints=new int[grid.length+1];
		boolean correct=true;
		int size=(int) Math.sqrt(grid.length);
		for(int blockNum=0;blockNum<grid.length;blockNum++){
			for(int row=0;row<size;row++){
				for(int col=0; col<size;col++){
					int trow=(blockNum/size)*size+row;
					int tcol=(blockNum%size)*size+col;
					ints[grid[trow][tcol]]++;
				}
			}

			for(int i=1;i<ints.length;i++){
				if(ints[i]>1){
					if(printDeterminator){ System.out.println("    Incorrect "+type); printDeterminator=false;};
					System.out.println("      "+i+" is duplicated in block "+(blockNum+1));
					System.out.print("        ");
					for(int row=0;row<size;row++){
						for(int col=0;col<size;col++){
							int trow=(blockNum/size)*size+row;
							int tcol=(blockNum%size)*size+col;
							if(grid[trow][tcol]==i)
								System.out.print("("+(trow+1)+", "+(tcol+1)+") ");
						}
					}
					System.out.println();
					correct=false;
				}
			}
			ints=new int[grid.length+1];
		}
		return correct;
	}

	private static boolean checkDiagonals(String type){
		boolean correct=true;
		int[] ints=new int[grid.length+1];

		for(int i=0;i<grid.length;i++)
			ints[grid[i][i]]++;

		for(int n=1;n<ints.length;n++)
			if(ints[n]>1){
				if(printDeterminator){ System.out.println("    Incorrect "+type); printDeterminator=false;};
				System.out.println("      "+n+" is repeated on diagonal 1");
				System.out.print("        ");
				for(int t=0;t<grid.length;t++)
					if(grid[t][t]==n)
						System.out.print("("+(t+1)+","+(t+1)+") ");
				System.out.println();
				correct=false;
			}

		int s=grid.length;
		ints=new int[grid.length+1];

		for(int i=0;i<grid.length;i++)
			ints[grid[i][s-i-1]]++;

		for(int n=1;n<ints.length;n++)
			if(ints[n]>1){
				if(printDeterminator){ System.out.println("    Incorrect "+type); printDeterminator=false;};
				System.out.println("      "+n+" is repeated on diagonal 2");
				System.out.print("        ");
				for(int t=0;t<grid.length;t++)
					if(grid[t][s-t-1]==n)
						System.out.print("("+(t+1)+","+(s-t)+") ");
				System.out.println();
				correct=false;
			}
		return correct;
	}

	private static boolean checkColumns(String type){
		boolean correct=true;
		for(int i=0;i<grid.length;i++){
			if(!checkColumn(i,type)){
				printDeterminator=false;
				correct=false;
			}
		}
		return correct;
	}

	private static boolean checkRows(String type){
		boolean correct=true;
		for(int i=0;i<grid.length;i++){
			if(!checkRow(i, type)){
				correct=false;
				printDeterminator=false;
			}
		}
		return correct;
	}
	private static boolean checkColumn(int n, String type){
		boolean correct=true;
		int[]ints=new int[grid.length+1];


		for(int i = 0 ;i<grid.length;i++){
			ints[grid[i][n]]++;
		}

		for(int i=1;i<grid.length;i++){
			if(ints[i]>1){
				if(printDeterminator){ System.out.println("    Incorrect "+type); printDeterminator=false;};
				System.out.println("      "+i+" is repeated in column "+n);
				System.out.print("        ");
				for(int c=0;c<grid.length;c++)
					if(grid[c][n]==i)
						System.out.print("("+(c+1)+","+(n+1)+") ");
				System.out.println();
				correct=false;
			}
		}
		return correct;
	}

	private static boolean checkRow(int n, String type){
		boolean correct=true;
		int[] ints= new int[grid.length+1];
		for(int j=0;j<grid.length;j++){
			ints[grid[n][j]]++;
		}


		for(int j=1;j<grid.length;j++){
			if(ints[j]>1){
				if(printDeterminator){ System.out.println("    Incorrect "+type); printDeterminator=false;};
				System.out.println("      "+j+" is repeated in row "+n);
				System.out.print("        ");
				for(int c=0;c<grid.length;c++)
					if(grid[n][c]==j)
						System.out.print("("+(n+1)+","+(c+1)+") ");
				System.out.println();

				correct=false;
			}
		}
		return correct;
	}

	public static boolean isPossibleSudoku(int n){
		return Math.pow(Math.sqrt(n),2)==n ;
	}

	public static void main(String[] args) {
		//	DownloadFiles.downloadSubFiles("http://elvis.rowan.edu/rupc/Problems/2008");
		//run(new Scanner(System.in));
		runTest("Rowan/Year2008/SampleInput1.txt");
//		runTest("Rowan/Year2008/SampleInput2.txt");
//				runTest("Rowan/Year2008/TestInput1.txt");
	}


	private static void runTest(String fileName){
		Scanner file=null;
		try {
			file = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//		while(file.hasNextLine())//for printing out
		//			System.out.println(file.nextLine());
		run(file);
	}
}