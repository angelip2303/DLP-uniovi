package parser;

public class LexerHelper {
	
	public static int lexemeToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e) {
			System.out.println(e);
		}
		return -1;
	}

	// TODO: Implement the lexemeToChar and lexemeToReal methods

	public static char lexemeToChar(String str) {
		if(str.charAt(1) != '\\')
			return str.charAt(1);

		if(str.charAt(2) == 'n' )
			return '\n';

		if(str.charAt(2) == 't')
			return '\t';

		int number = Integer.parseInt(str.substring(2, str.length() -1));

		return (char)number;
	}

	public static double lexemeToReal(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch(NumberFormatException e) {
			System.out.println(e);
		}
		return -1;
	}


	
}
