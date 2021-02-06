public class Substitution {// problem found with numbers
	static int ind=0;
	public static String substitution(String text,String keyword, boolean decipher){
		
		String encipheredText="";
		if(decipher)//if deciphering, flip the case of keyword to reverse the shifting caused by enciphering
		{
			String temp="";
			for(int x=0;x<keyword.length();x++)
				if(Character.isLowerCase(keyword.charAt(x)))
					temp+=Character.toUpperCase(keyword.charAt(x));
				else
					temp+=Character.toLowerCase(keyword.charAt(x));	
			keyword=temp;
		}
		for(int x=0;x<text.length();x++){
			if(Character.isLetter(text.charAt(x))){ //if character is letter, shifts character and adds into encipheredText
				encipheredText+=shift(text.charAt(x),keyword.charAt(ind%keyword.length()));
				ind++;
			}
			else
				encipheredText+=text.charAt(x);//add nonletter character to encipheredText
		}
		
		return encipheredText;
	}

	
	private static char shift(char t, char s){
		int temp;
		if(Character.isLowerCase(s))
			temp=t-s+32;
		else
			temp=t+s-128;//convert from alpha to numeric val
			
			while(temp>26)//stay within alphabetic range
				temp-=26;
			while(temp<1)
				temp+=26;
			return (char) (temp+64);
	}
	
	public static void main(String [] args){
		System.out.println(substitution("THIS IS A TEST.THIS IS LINE 2.", "Jayne",false));
		System.out.println(substitution("DGJE DC Z UQND.SIUN SR MUIO 2.","Jayne",true));
	}
}
