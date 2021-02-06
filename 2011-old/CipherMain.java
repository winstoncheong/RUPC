import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CipherMain{
	public static void main(String[] args){
		final boolean fromFile = true;
		Scanner sc = null;
		if(fromFile){
			try{
				// sc=new Scanner(new File("Rowan/Year2011/sample1.txt"));
				// sc=new Scanner(new File("Rowan/Year2011/sample2.txt"));
				// sc=new Scanner(new File("Rowan/Year2011/sample3.txt"));
				// sc=new Scanner(new File("Rowan/Year2011/test1.txt"));
				// sc=new Scanner(new File("Rowan/Year2011/test2.txt"));
				sc = new Scanner(new File("Rowan/Year2011/test3.txt"));

				sc.close();
			}catch(FileNotFoundException e){
				System.out.println(e.getLocalizedMessage());
				e.printStackTrace();
			}
		}else
			sc = new Scanner(System.in);

		int datasets = sc.nextInt();
		System.out.println("Analyzig " + datasets + " data sets");
		for(int x = 0; x < datasets; x++){
			System.out.println("Data set " + (x + 1) + ":");

			String transposeKeyword = sc.next();
			String substitutionKeyword = sc.next();
			System.out.println("Transpose Keyword: " + transposeKeyword);
			System.out.println("Substitution Keyword: " + substitutionKeyword);
			int lines = sc.nextInt(); // encipher
			sc.nextLine();// throws away empty line
			String[] arr = new String[lines];
			Substitution.ind = 0;
			for(int i = 0; i < lines; i++)
				arr[i] = sc.nextLine();
			System.out.println("Enciphering " + lines + " line(s):");
			if(!transposeKeyword.equals("PASS"))
				for(int i = 0; i < arr.length; i++)
					arr[i] = Transposition.transposition(arr[i], transposeKeyword, false);
			if(!substitutionKeyword.equals("PASS"))
				for(int i = 0; i < arr.length; i++)
					arr[i] = Substitution.substitution(arr[i], substitutionKeyword, false);
			for(String str: arr)
				System.out.println(str);

			lines = sc.nextInt(); // decipher
			sc.nextLine();
			arr = new String[lines];
			Substitution.ind = 0;
			for(int i = 0; i < lines; i++)
				arr[i] = sc.nextLine();
			System.out.println("Deciphering " + lines + " line(s):");
			if(!substitutionKeyword.equals("PASS"))
				for(int i = 0; i < arr.length; i++)
					arr[i] = Substitution.substitution(arr[i], substitutionKeyword, true);
			if(!transposeKeyword.equals("PASS"))
				for(int i = 0; i < arr.length; i++)
					arr[i] = Transposition.transposition(arr[i], transposeKeyword, true);

			for(String str: arr)
				System.out.println(str);

		}
	}
}