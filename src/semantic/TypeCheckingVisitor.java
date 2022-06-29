package semantic;

import ast.TypeCheckingFactory;
import ast.expressions.*;
import ast.program.FunctionDefinition;
import ast.program.Program;
import ast.program.VariableDefinition;
import ast.statements.*;
import ast.types.*;
import ast.types.Character;
import ast.types.Double;
import ast.types.Integer;
import visitor.AbstractVisitor;

import java.lang.Void;
import java.util.List;
import java.util.stream.Collectors;

public class TypeCheckingVisitor extends AbstractVisitor<Type, Void> {

    // -*- Expressions -*-

    /**
     * P:
     *      Arithmetic: expression1 -> expression2 expression3
     * R:
     *      expression1.type = expression2.type.arithmetic(expression3.type);
     */
    @Override
    public Void visit(Arithmetic arithmetic, Type param) {
        super.visit(arithmetic, param);
        arithmetic.setLvalue(false);

        // We infer the type of the arithmetic expression
        arithmetic.setType(arithmetic.leftOperand.getType().arithmetic(arithmetic.rightOperand.getType()));

        return null;
    }

    /**
     * P:
     *      Cast: expression1 -> type expression2
     * R:
     *      expression1.type = type.cast(expression2.type);
     */
    @Override
    public Void visit(Cast cast, Type param) {
        super.visit(cast, param);
        cast.setLvalue(false);

        // We infer the type of the cast expression
        cast.setType(cast.expression.getType().cast(cast.castType));

        return null;
    }

    /**
     * P:
     *      CharacterLiteral: expression1 -> expression2
     * R:
     *      expression1.type = TypeCheckingFactory.getCharacterType();
     */
    @Override
    public Void visit(CharacterLiteral characterLiteral, Type param) {
        super.visit(characterLiteral, param);
        characterLiteral.setLvalue(false);

        // We infer the type of the CharacterLiteral expression
        characterLiteral.setType(TypeCheckingFactory.getCharacterType());

        return null;
    }

    /**
     * P:
     *      Comparison: expression1 -> expression2 expression3
     * R:
     *      expression1.type = expression2.type.comparison(expression3.type);
     */
    @Override
    public Void visit(Comparison comparison, Type param) {
        super.visit(comparison, param);
        comparison.setLvalue(false);

        // We infer the type of the Comparison expression
        comparison.setType(comparison.leftOperand.getType().comparison(comparison.rightOperand.getType()));

        return null;
    }

    /**
     * P:
     *      DoubleLiteral: expression1 -> expression2
     * R:
     *      expression1.type = TypeCheckingFactory.getDoubleType();
     */
    @Override
    public Void visit(DoubleLiteral doubleLiteral, Type param) {
        super.visit(doubleLiteral, param);
        doubleLiteral.setLvalue(false);

        // We infer the type of the DoubleLiteral expression
        doubleLiteral.setType(TypeCheckingFactory.getDoubleType());

        return null;
    }

    /**
     * P:
     *      FunctionInvocation: expression1 -> expression2 expression3*
     * R:
     *      List<Type> argTypes = expression3*
     *                             .stream()
     *                             .map(exp -> exp.type)
     *                             .toList();
     *      expression1.type = expression2.type.parenthesis(argTypes);
     */
    @Override
    public Void visit(FunctionInvocation functionInvocation, Type param) {
        super.visit(functionInvocation, param);
        functionInvocation.setLvalue(false);

        // We infer the type of the FunctionInvocation expression
        List<Type> argTypes = functionInvocation.arguments
                            .stream()
                            .map(Expression::getType)
                            .collect(Collectors.toList());
        functionInvocation.setType(functionInvocation.variable.getType().parenthesis(argTypes));

        return null;
    }

    /**
     * P:
     *      Indexing: expression1 -> expression2 expression3
     * R:
     *      expression1.type = expression2.type.squareBrackets(expression3.type);
     */
    @Override
    public Void visit(Indexing indexing, Type param) {
        super.visit(indexing, param);
        indexing.setLvalue(true);

        // We infer the type of the Indexing expression
        indexing.setType(indexing.expression.getType().squareBrackets(indexing.index.getType()));

        return null;
    }

    /**
     * P:
     *      IntLiteral: expression1 -> expression2
     * R:
     *      expression1.type = TypeCheckingFactory.getIntegerType();
     */
    @Override
    public Void visit(IntLiteral intLiteral, Type param) {
        super.visit(intLiteral, param);
        intLiteral.setLvalue(false);

        // We infer the type of the IntLiteral expression
        intLiteral.setType(TypeCheckingFactory.getIntegerType());

        return null;
    }

    /**
     * P:
     *      Logical: expression1 -> expression2 expression3
     * R:
     *      expression1.type = expression2.type.logical(expression3.type);
     */
    @Override
    public Void visit(Logical logical, Type param) {
        super.visit(logical, param);
        logical.setLvalue(false);

        // We infer the type of the IntLiteral expression
        logical.setType(logical.leftOperand.getType().logical(logical.rightOperand.getType()));

        return null;
    }

    /**
     * P:
     *      RecordAccess: expression1 -> ID expression2
     * R:
     *      expression1.type = expression2.type.dot(ID);
     */
    @Override
    public Void visit(RecordAccess recordAccess, Type param) {
        super.visit(recordAccess, param);
        recordAccess.setLvalue(true);

        // We infer the type of the RecordAccess expression
        recordAccess.setType(recordAccess.expression.getType().dot(recordAccess.name));

        return null;
    }

    /**
     * P:
     *      UnaryMinus: expression1 -> expression2
     * R:
     *      expression1.type = expression2.type.minus();
     */
    @Override
    public Void visit(UnaryMinus unaryMinus, Type param) {
        super.visit(unaryMinus, param);
        unaryMinus.setLvalue(false);

        // We infer the type of the UnaryMinus expression
        unaryMinus.setType(unaryMinus.expression.getType().minus());

        return null;
    }

    /**
     * P:
     *      UnaryNot: expression1 -> expression2
     * R:
     *      expression1.type = expression2.type.not();
     */
    @Override
    public Void visit(UnaryNot unaryNot, Type param) {
        super.visit(unaryNot, param);
        unaryNot.setLvalue(false);

        // We infer the type of the UnaryNot expression
        unaryNot.setType(unaryNot.expression.getType().not());

        return null;
    }

    /**
     * P:
     *      Variable: expression1 -> ID
     * R:
     *      expression1.type = expression1.definition == null
     *                         ? new ErrorType(
     *                             expression1.getLine(),
     *                             expression1.getColumn(),
     *                             "You have not defined the variable: " + ID
     *                             )
     *                         : expression1.definition.type;
     */
    @Override
    public Void visit(Variable variable, Type param) {
        super.visit(variable, param);
        variable.setLvalue(true);

        // We infer the type of the Variable expression
        variable.setType(variable.definition == null
                ? new ErrorType(
                variable.getLine(),
                variable.getColumn(),
                "You have not defined the variable: " + variable.name
        )
                : variable.definition.getType());

        return null;
    }

    /**
     * P:
     *      Modulus: expression1 -> expression2 expression3
     * R:
     *      expression1.type = expression2.type.modulus(expression3.type);
     */
    @Override
    public Void visit(Modulus modulus, Type param) {
        super.visit(modulus, param);
        modulus.setLvalue(false);

        // We infer the type of the Modulus expression
        modulus.setType(modulus.leftOperand.getType().modulus(modulus.rightOperand.getType()));

        return null;
    }

    // -*- Program -*-

    /**
     * P:
     *      FunctionDefinition: definition -> type ID statement*
     * R:
     *      statement*.forEach( st -> st.returnType = type.returnType);
     */
    @Override
    public Void visit(FunctionDefinition functionDefinition, Type param) {
        super.visit(functionDefinition, ((FunctionType) functionDefinition.getType()).returnType);
        return null;
    }

    @Override
    public Void visit(Program program, Type param) {
        super.visit(program, param);
        return null;
    }

    @Override
    public Void visit(VariableDefinition variableDefinition, Type param) {
        super.visit(variableDefinition, param);
        return null;
    }

    // -*- Statements -*-

    /**
     * P:
     *      Assignment: statement -> expression1 expression2
     * R:
     *      expression1.type = expression2.type.assign(expression3.type);
     */
    @Override
    public Void visit(Assignment assignment, Type param) {
        assignment.leftHandSide.accept(this, param);
        assignment.rightHandSide.accept(this, param);

        // In case the LHS is not an L-Value
        if (assignment.leftHandSide.getLvalue())
            new ErrorType(
                    assignment.leftHandSide.getLine(),
                    assignment.leftHandSide.getColumn(),
                    "L-value required");

        // We infer the type of the Assignment statement
        assignment.leftHandSide.getType().assign(assignment.rightHandSide.getType());

        return null;
    }

    /**
     * P:
     *      If: statement1 -> expression statement2* statement3*
     * R:
     *      statement2*.stream().forEach( st -> st.returnType = statement1.returnType);
     *      statement3*.stream().forEach( st -> st.returnType = statement1.returnType);
     *      expression.type.asBoolean();
     */
    @Override
    public Void visit(If ifStatement, Type param) {
        super.visit(ifStatement, param);

        // We infer the type of the If statement
        ifStatement.expression.getType().asBoolean();

        return null;
    }

    /**
     * P:
     *      Read: statement -> expression
     * R:
     *      expression.type.readable();
     */
    @Override
    public Void visit(Read read, Type param) {
        read.expression.accept(this, param);

        // In case the LHS is not an L-Value
        if (read.expression.getLvalue())
            new ErrorType(
                    read.expression.getLine(),
                    read.expression.getColumn(),
                    "L-value required");

        // We infer the type of the Read statement
        read.expression.getType().readable();

        return null;
    }

    /**
     * P:
     *      Return: statement -> expression
     * R:
     *      expression.type.equivalent(statement.returnType);
     */
    @Override
    public Void visit(Return returnStatement, Type param) {
        super.visit(returnStatement, param);

        // We infer the type of the Return statement
        returnStatement.expression.getType().mustBeSubType(param); // TODO change naming

        return null;
    }

    /**
     * P:
     *      While: statement1 -> expression statement2*
     * R:
     *      statement2*.stream().forEach( st -> st.returnType = statement1.returnType);
     *      expression.type.asBoolean();
     */
    @Override
    public Void visit(While whileStatement, Type param) {
        super.visit(whileStatement, param);

        // We infer the type of the While statement
        whileStatement.expression.getType().asBoolean();

        return null;
    }

    /**
     * P:
     *      Write: statement -> expression
     * R:
     *      expression.type.writable();
     */
    @Override
    public Void visit(Write writeStatement, Type param) {
        super.visit(writeStatement, param);

        // We infer the type of the Write statement
        writeStatement.expression.getType().writable();

        return null;
    }

    // -*- Types -*-

    @Override
    public Void visit(Array array, Type param) {
        super.visit(array, param);
        return null;
    }

    @Override
    public Void visit(Character character, Type param) {
        super.visit(character, param);
        return null;
    }

    @Override
    public Void visit(Double doubleType, Type param) {
        super.visit(doubleType, param);
        return null;
    }

    @Override
    public Void visit(ErrorType errorType, Type param) {
        super.visit(errorType, param);
        return null;
    }

    @Override
    public Void visit(FunctionType functionType, Type param) {
        super.visit(functionType, param);
        return null;
    }

    @Override
    public Void visit(Integer integer, Type param) {
        super.visit(integer, param);
        return null;
    }

    @Override
    public Void visit(RecordField recordField, Type param) {
        super.visit(recordField, param);
        return null;
    }

    @Override
    public Void visit(Struct struct, Type param) {
        super.visit(struct, param);
        return null;
    }

    @Override
    public Void visit(ast.types.Void voidType, Type param) {
        super.visit(voidType, param);
        return null;
    }

}
