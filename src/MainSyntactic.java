import ast.program.Program;
import ast.types.Type;
import errorhandler.ErrorHandler;
import introspector.model.IntrospectorModel;
import introspector.view.IntrospectorTree;
import parser.*;

import org.antlr.v4.runtime.*;
import semantic.IdentificationVisitor;
import semantic.TypeCheckingVisitor;
import visitor.Visitor;

public class MainSyntactic {
	
	public static void main(String... args) throws Exception {
		   if (args.length<1) {
		        System.err.println("Please, pass me the input file.");
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
			IntrospectorModel model=new IntrospectorModel("Program", ast);
			new IntrospectorTree("Introspector", model);
		}
	}
	

}
