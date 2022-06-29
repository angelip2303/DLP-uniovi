import ast.program.Program;
import ast.types.Type;
import codegeneration.OffsetVisitor;
import codegeneration.util.CodeGenerator;
import errorhandler.ErrorHandler;
import introspector.model.IntrospectorModel;
import introspector.view.IntrospectorTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import parser.CmmLexer;
import parser.CmmParser;
import semantic.IdentificationVisitor;
import semantic.TypeCheckingVisitor;
import visitor.Visitor;

public class MainCodeGeneration {
	
	public static void main(String... args) throws Exception {
		if (args.length < 1) {
			System.err.println("Please, pass me the input file.");
			return;
		}

		if (args.length < 2) {
			System.err.println("Please, pass me the output file.");
			return;
		}

		// create a lexer that feeds off of input CharStream
		CharStream input = CharStreams.fromFileName(args[0]);
		CmmLexer lexer = new CmmLexer(input);

		// create a parser that feeds off the tokens buffer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		CmmParser parser = new CmmParser(tokens);

		// we create the ast
		Program ast = parser.program().ast;

		// traverse the ast tree with the visitor
		Visitor<Void, Void> identificationVisitor = new IdentificationVisitor();
		identificationVisitor.visit(ast, null);

		// traverse the ast tree with the visitor
		Visitor<Type, Void> typeCheckingVisitor = new TypeCheckingVisitor();
		typeCheckingVisitor.visit(ast, null);

		// handle errors just-in-case
		if (ErrorHandler.getInstance().anyErrors())
			ErrorHandler.getInstance().showErrors(System.err);
		else {
			// * The AST is shown
			IntrospectorModel model = new IntrospectorModel("Program", ast);
			new IntrospectorTree("Introspector", model);

			// traverse the ast tree with the visitor
			Visitor<Void, Void> offsetVisitor = new OffsetVisitor();
			offsetVisitor.visit(ast, null);

			new CodeGenerator().writeSource(args[1], args[0], ast);
		}
	}

}
