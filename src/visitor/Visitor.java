package visitor;

import ast.expressions.*;
import ast.program.*;
import ast.statements.*;
import ast.types.*;

import ast.types.Character;
import ast.types.Double;
import ast.types.Integer;
import ast.types.Void;

public interface Visitor<TP, TR> {

    // -*- Expressions -*-

    TR visit(Arithmetic arithmetic, TP param);
    TR visit(Cast cast, TP param);
    TR visit(CharacterLiteral characterLiteral, TP param);
    TR visit(Comparison comparison, TP param);
    TR visit(DoubleLiteral doubleLiteral, TP param);
    TR visit(FunctionInvocation functionInvocation, TP param);
    TR visit(Indexing indexing, TP param);
    TR visit(IntLiteral intLiteral, TP param);
    TR visit(Logical logical, TP param);
    TR visit(RecordAccess recordAccess, TP param);
    TR visit(UnaryMinus unaryMinus, TP param);
    TR visit(UnaryNot unaryNot, TP param);
    TR visit(Variable variable, TP param);
    TR visit(Modulus modulus, TP param);

    // -*- Program -*-

    TR visit(FunctionDefinition functionDefinition, TP param);
    TR visit(Program program, TP param);
    TR visit(VariableDefinition variableDefinition, TP param);

    // -*- Statements -*-

    TR visit(Assignment assignment, TP param);
    TR visit(If ifStatement, TP param);
    TR visit(Read read, TP param);
    TR visit(Return returnStatement, TP param);
    TR visit(While whileStatement, TP param);
    TR visit(Write writeStatement, TP param);

    // -*- Types -*-

    TR visit(Array array, TP param);
    TR visit(Character character, TP param);
    TR visit(Double doubleType, TP param);
    TR visit(ErrorType errorType, TP param);
    TR visit(FunctionType functionType, TP param);
    TR visit(Integer integer, TP param);
    TR visit(RecordField recordField, TP param);
    TR visit(Struct struct, TP param);
    TR visit(Void voidType, TP param);

}
