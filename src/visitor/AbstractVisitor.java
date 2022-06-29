package visitor;

import ast.expressions.*;
import ast.program.*;
import ast.statements.*;
import ast.types.*;

import ast.types.Character;
import ast.types.Double;
import ast.types.Integer;
import ast.types.Void;

public abstract class AbstractVisitor<TP, TR> implements Visitor<TP, TR> {

    // -*- Expressions -*-

    @Override
    public TR visit(Arithmetic arithmetic, TP param) {
        arithmetic.leftOperand.accept(this, param);
        arithmetic.rightOperand.accept(this, param);

        return null;
    }

    @Override
    public TR visit(Cast cast, TP param) {
        cast.castType.accept(this, param);
        cast.expression.accept(this, param);

        return null;
    }

    @Override
    public TR visit(CharacterLiteral characterLiteral, TP param) {
        return null;
    }

    @Override
    public TR visit(Comparison comparison, TP param) {
        comparison.leftOperand.accept(this, param);
        comparison.rightOperand.accept(this, param);

        return null;
    }

    @Override
    public TR visit(DoubleLiteral doubleLiteral, TP param) {
        return null;
    }

    @Override
    public TR visit(FunctionInvocation functionInvocation, TP param) {
        functionInvocation.arguments.forEach(arg -> arg.accept(this, param));
        functionInvocation.variable.accept(this, param);

        return null;
    }

    @Override
    public TR visit(Indexing indexing, TP param) {
        indexing.expression.accept(this, param);
        indexing.index.accept(this, param);

        return null;
    }

    @Override
    public TR visit(IntLiteral intLiteral, TP param) {
        return null;
    }

    @Override
    public TR visit(Logical logical, TP param) {
        logical.leftOperand.accept(this, param);
        logical.rightOperand.accept(this, param);

        return null;
    }

    @Override
    public TR visit(RecordAccess recordAccess, TP param) {
        recordAccess.expression.accept(this, param);
        return null;
    }

    @Override
    public TR visit(UnaryMinus unaryMinus, TP param) {
        unaryMinus.expression.accept(this, param);
        return null;
    }

    @Override
    public TR visit(UnaryNot unaryNot, TP param) {
        unaryNot.expression.accept(this, param);
        return null;
    }

    @Override
    public TR visit(Variable variable, TP param) {
        return null;
    }

    @Override
    public TR visit(Modulus modulus, TP param) {
        modulus.leftOperand.accept(this, param);
        modulus.rightOperand.accept(this, param);

        return null;
    }

    // -*- Program -*-

    @Override
    public TR visit(FunctionDefinition functionDefinition, TP param) {
        functionDefinition.getType().accept(this, param);
        functionDefinition.statements.forEach(statement -> statement.accept(this, param));

        return null;
    }

    @Override
    public TR visit(Program program, TP param) {
        program.definitions.forEach(def -> def.accept(this, param));
        return null;
    }

    @Override
    public TR visit(VariableDefinition variableDefinition, TP param) {
        variableDefinition.getType().accept(this, param);
        return null;
    }

    // -*- Statements -*-

    @Override
    public TR visit(Assignment assignment, TP param) {
        assignment.leftHandSide.accept(this, param);
        assignment.rightHandSide.accept(this, param);

        return null;
    }

    @Override
    public TR visit(If ifStatement, TP param) {
        ifStatement.expression.accept(this, param);
        ifStatement.ifStatements.forEach(statement -> statement.accept(this, param));
        ifStatement.elseStatements.forEach(statement -> statement.accept(this, param));

        return null;
    }

    @Override
    public TR visit(Read read, TP param) {
        read.expression.accept(this, param);
        return null;
    }

    @Override
    public TR visit(Return returnStatement, TP param) {
        returnStatement.expression.accept(this, param);
        return null;
    }

    @Override
    public TR visit(While whileStatement, TP param) {
        whileStatement.statements.forEach(statement -> statement.accept(this, param));
        whileStatement.expression.accept(this, param);

        return null;
    }

    @Override
    public TR visit(Write writeStatement, TP param) {
        writeStatement.expression.accept(this, param);
        return null;
    }

    // -*- Types -*-

    @Override
    public TR visit(Array array, TP param) {
        array.type.accept(this, param);
        return null;
    }

    @Override
    public TR visit(Character character, TP param) {
        return null;
    }

    @Override
    public TR visit(Double doubleType, TP param) {
        return null;
    }

    @Override
    public TR visit(ErrorType errorType, TP param) {
        return null;
    }

    @Override
    public TR visit(FunctionType functionType, TP param) {
        functionType.parameters.forEach(parameter -> parameter.accept(this, param));
        functionType.returnType.accept(this, param);

        return null;
    }

    @Override
    public TR visit(Integer integer, TP param) {
        return null;
    }

    @Override
    public TR visit(RecordField recordField, TP param) {
        recordField.type.accept(this, param);
        return null;
    }

    @Override
    public TR visit(Struct struct, TP param) {
        struct.recordFields.forEach(e -> e.accept(this, param));
        return null;
    }

    @Override
    public TR visit(Void voidType, TP param) {
        return null;
    }

}
