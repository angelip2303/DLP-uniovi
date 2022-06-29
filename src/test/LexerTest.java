package test;

import parser.*;

import org.antlr.v4.runtime.*;

public class LexerTest {

	public static void main(String... args) throws Exception {
		// creates a lexer that feeds off of input CharStream
		CharStream input = CharStreams.fromFileName("input.txt");

		// creates a buffer of tokens pulled from the lexer
		CmmLexer lexer = new CmmLexer(input);

		// tests the lexer for the given input file
		Token token;

		int i = 0;
		while ((token = lexer.nextToken()).getType() != CmmLexer.EOF) {
			// We get the semantic value of the token
			Object semanticValue = null;
			switch (token.getType()) {
				case CmmLexer.CHAR_CONSTANT:
					semanticValue = LexerHelper.lexemeToChar(token.getText());
					break;
				case CmmLexer.INT_CONSTANT:
					semanticValue = LexerHelper.lexemeToInt(token.getText());
					break;
				case CmmLexer.REAL_CONSTANT:
					semanticValue = LexerHelper.lexemeToReal(token.getText());
					break;
				default:
					semanticValue = token.getText();
			}

			// We test the token
			expectedTokens[i].assertEquals(
					token.getLine(),
					token.getCharPositionInLine() + 1,
					token.getText(),
					token.getType(),
					semanticValue);
			i++;
		}
	}

	private static final LexicalInfo[] expectedTokens = new LexicalInfo[] {
			new LexicalInfo(7, 2, "0", CmmLexer.INT_CONSTANT, 0),
			new LexicalInfo(7, 4, "123", CmmLexer.INT_CONSTANT, 123),
			new LexicalInfo(8, 3, "0", CmmLexer.INT_CONSTANT, 0),
			new LexicalInfo(8, 4, "12", CmmLexer.INT_CONSTANT, 12),
			new LexicalInfo(13, 2, "12.3", CmmLexer.REAL_CONSTANT, 12.3),
			new LexicalInfo(13, 7, "2.", CmmLexer.REAL_CONSTANT, 2.0),
			new LexicalInfo(13, 11, ".34", CmmLexer.REAL_CONSTANT, 0.34),
			new LexicalInfo(14, 2, "34.12E-3", CmmLexer.REAL_CONSTANT, 0.03412),
			new LexicalInfo(14, 11, "3e3", CmmLexer.REAL_CONSTANT, 3000.0),
			new LexicalInfo(18, 3, "var1", CmmLexer.ID, "var1"),
			new LexicalInfo(18, 9, "_var_1", CmmLexer.ID, "_var_1"),
			new LexicalInfo(18, 18, "VAR_1_AB_2", CmmLexer.ID, "VAR_1_AB_2"),
			new LexicalInfo(23, 2, "'a'", CmmLexer.CHAR_CONSTANT, 'a'),
			new LexicalInfo(23, 6, "'b'", CmmLexer.CHAR_CONSTANT, 'b'),
			new LexicalInfo(23, 10, "'.'", CmmLexer.CHAR_CONSTANT, '.'),
			new LexicalInfo(23, 14, "'-'", CmmLexer.CHAR_CONSTANT, '-'),
			new LexicalInfo(23, 18, "'~'", CmmLexer.CHAR_CONSTANT, '~'),
			new LexicalInfo(24, 2, "'\\n'", CmmLexer.CHAR_CONSTANT, '\n'),
			new LexicalInfo(24, 7, "'\\t'", CmmLexer.CHAR_CONSTANT, '\t'),
			new LexicalInfo(25, 2, "'\\126'", CmmLexer.CHAR_CONSTANT, '~'),
	};
}
