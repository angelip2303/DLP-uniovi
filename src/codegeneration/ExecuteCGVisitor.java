package codegeneration;

import ast.expressions.FunctionInvocation;
import ast.program.FunctionDefinition;
import ast.program.Program;
import ast.program.VariableDefinition;
import ast.statements.*;
import ast.types.FunctionType;
import ast.types.Void;
import codegeneration.util.CGParam;
import codegeneration.util.CodeGenerator;

import java.util.ArrayList;
import java.util.List;

public class ExecuteCGVisitor extends AbstractCGVisitor<CGParam, Void> {

    public ExecuteCGVisitor(CodeGenerator cg) {
        super(cg);
    }

    /**
     *  execute[[Assignment: statement -> expression1 expression2]] =
     *      address[[expression1]]
     *      value[[expression2]]
     *
     *      < store > expression2.type.suffix()
     */
    @Override
    public Void visit(Assignment assignment, CGParam param) {
        codeGenerator.writeANewLine(assignment.getLine());

        assignment.leftHandSide.accept(codeGenerator.getAddressCGVisitor(), param);
        assignment.rightHandSide.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode("store" + assignment.rightHandSide.getType().suffix());

        return null;
    }

    /**
     *  execute[[Read: statement -> expression]] =
     *      address[[expression]]
     *      < in > expression.type.suffix()
     *
     *      < store > expression.type.suffix()
     */
    @Override
    public Void visit(Read read, CGParam param) {
        codeGenerator.writeANewLine(read.getLine());

        read.expression.accept(codeGenerator.getAddressCGVisitor(), param);
        codeGenerator.writeLineOfCode("in" + read.expression.getType().suffix());
        codeGenerator.writeLineOfCode("store" + read.expression.getType().suffix());

        return null;
    }

    /**
     *  execute[[Write: statement -> expression]] =
     *      value[[expression]]
     *      < out > expression.type.suffix()
     */
    @Override
    public Void visit(Write write, CGParam param) {
        codeGenerator.writeANewLine(write.getLine());

        codeGenerator.writeLineOfCode("' * Write");
        write.expression.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode("out" + write.expression.getType().suffix());

        return null;
    }

    /**
     *  execute[[FunctionDefinition: definition -> type ID variableDefinition* statement*]] =
     *      ID <:>
     *          type.parameters.forEach(parameter -> execute[[parameter]])
     *          variableDefinition*.forEach(varDefinition -> execute[[varDefinition]])
     *          int bytesLocals = variableDefinition*.isEmpty()
     *                              ? 0
     *                              : - variableDefinition*.get(variableDefinition*.size() - 1).offset
     *          < enter > bytesLocals
     *          int bytesParams = type.parameters.stream().mapToInt(parameter -> parameter.type.numberOfBytes())
     *          int bytesReturn = type.returnType.numberOfBytes()
     *          statement*.forEach( statement -> execute[[statement]](bytesReturn, bytesLocals, bytesParams) )
     *
     *          if (type.returnType instanceof Void) < ret > bytesReturn, bytesLocals, bytesParams
     */
    @Override
    public Void visit(FunctionDefinition functionDefinition, CGParam param) {
        codeGenerator.writeANewLine(functionDefinition.getLine());

        FunctionType function = (FunctionType) functionDefinition.getType(); // ease of use

        // We establish a separation between functions!
        codeGenerator.write("");
        codeGenerator.writeALabel(functionDefinition.getName() + ":");

        // I want to show the parameters to make sure everything is working as intended :D
        codeGenerator.writeLineOfCode("' * Parameters");
        function.parameters.forEach(parameter -> parameter.accept(codeGenerator.getExecuteCGVisitor(), param));

        // We have to separate the varDefinitions (local variables) and the rest of the statements, this is done to
        // perform some operations like calculating the bytes we need to keep for those variables
        var variableDefinitions = new ArrayList<VariableDefinition>();
        var statements = new ArrayList<Statement>();
        functionDefinition
                .statements
                .forEach(statement -> {
                    if (statement instanceof VariableDefinition) variableDefinitions.add((VariableDefinition) statement);
                    else statements.add(statement);
                });

        // Now it is time for the local variables to be executed and to calculate the bytesLocals
        codeGenerator.writeLineOfCode("' * Local variables");
        variableDefinitions
                .forEach(variableDefinition -> variableDefinition.accept(codeGenerator.getExecuteCGVisitor(), param));
        int bytesLocals = variableDefinitions.isEmpty()
                ? 0
                : - variableDefinitions.get(variableDefinitions.size() - 1).offset;

        codeGenerator.writeLineOfCode("enter " + bytesLocals);

        int bytesParams = function.parameters.stream().mapToInt(parameter -> parameter.getType().numberOfBytes()).sum();
        int bytesReturn = function.returnType.numberOfBytes();

        CGParam returnType = new CGParam(bytesLocals, bytesParams, bytesReturn);
        statements.forEach(statement -> statement.accept(codeGenerator.getExecuteCGVisitor(), returnType));

        if (function.returnType instanceof Void)
            codeGenerator.writeLineOfCode(String.format(
                    "ret %d, %d, %d",
                    bytesReturn,
                    bytesLocals,
                    bytesParams)
            );

        return null;
    }

    /**
     *  execute[[VarDefinition: definition -> type ID]] =
     *      ' * type ID (offset definition.offset)
     */
    @Override
    public Void visit(VariableDefinition variableDefinition, CGParam param) {
        codeGenerator.writeLineOfCode(String.format(
                "' * %s %s (offset %d)",
                variableDefinition.getType().getDataType(),
                variableDefinition.getName(),
                variableDefinition.offset
        ));
        return null;
    }

    /**
     *  execute[[Program: program -> definition*]] =
     *      definition*.forEach(definition -> execute[[definition]]);
     */
    @Override
    public Void visit(Program program, CGParam param) {
        List<VariableDefinition> variableDefinitions = new ArrayList<>();
        List<FunctionDefinition> functionDefinitions = new ArrayList<>();

        program.definitions.forEach(definition -> {
            if (definition instanceof VariableDefinition) variableDefinitions.add((VariableDefinition) definition);
            else if (definition instanceof FunctionDefinition) functionDefinitions.add((FunctionDefinition) definition);
        });

        variableDefinitions.forEach(variableDefinition -> variableDefinition.accept(this, param));

        codeGenerator.write("");
        codeGenerator.write("' Invocation to the main function");
        codeGenerator.write("call main");
        codeGenerator.write("halt");
        codeGenerator.write("");

        functionDefinitions.forEach(variableDefinition -> variableDefinition.accept(this, param));

        return null;
    }

    /**
     *  execute[[While: statement1 -> expression statement2*]](int bytesReturn, int bytesLocals, int bytesParams) =
     *      String conditionLabel = cg.nextLabel()
     *      String exitLabel = cg.nextLabel()
     *
     *      conditionLabel < : >
     *          value[[expression]]
     *          < jz > exitLabel
     *          statement2*.forEach(statement -> execute[[statement]](bytesReturn, bytesLocals, bytesParams))
     *          < jmp > conditionLabel
     *      exitLabel < : >
     */
    @Override
    public Void visit(While whileStatement, CGParam param) {
        codeGenerator.writeLineOfCode("' * While");
        codeGenerator.writeANewLine(whileStatement.getLine());

        String conditionLabel = codeGenerator.nextLabel();
        String exitLabel = codeGenerator.nextLabel();

        codeGenerator.writeALabel(conditionLabel + ":");
        whileStatement.expression.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode("jz " + exitLabel);
        codeGenerator.writeLineOfCode("' * Body of the while statement");
        whileStatement.statements.forEach(statement -> statement.accept(codeGenerator.getExecuteCGVisitor(), param));
        codeGenerator.writeLineOfCode("jmp " + conditionLabel);
        codeGenerator.writeALabel(exitLabel + ":");

        return null;
    }

    /**
     *  execute[[If: statement1 -> expression statement2* statement3*]](int bytesReturn, int bytesLocals, int bytesParams) =
     *      String elseLabel = cg.nextLabel()
     *      String exitLabel = cg.nextLabel()
     *
     *      value[[expression]]
     *      < jz > elseLabel
     *      statement2*.forEach(statement -> execute[[statement]](bytesReturn, bytesLocals, bytesParams))
     *      < jmp > exitLabel
     *      elseLabel < : >
     *          statement3*.forEach(statement -> execute[[statement]](bytesReturn, bytesLocals, bytesParams))
     *      exitLabel < : >
     */
    @Override
    public Void visit(If ifStatement, CGParam param) {
        codeGenerator.writeLineOfCode("' * If statement");
        codeGenerator.writeANewLine(ifStatement.getLine());

        String elseLabel = codeGenerator.nextLabel();
        String exitLabel = codeGenerator.nextLabel();

        ifStatement.expression.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode("jz " + elseLabel);
        codeGenerator.writeLineOfCode("' * Body of the if branch");
        ifStatement.ifStatements.forEach(statement -> statement.accept(codeGenerator.getExecuteCGVisitor(), param));
        codeGenerator.writeLineOfCode("jmp " + exitLabel);
        codeGenerator.writeALabel(elseLabel + ":");
        codeGenerator.writeLineOfCode("' * Body of the else branch");
        ifStatement.elseStatements.forEach(statement -> statement.accept(codeGenerator.getExecuteCGVisitor(), param));
        codeGenerator.writeALabel(exitLabel + ":");

        return null;
    }

    /**
     *  execute[[If: expression1 -> expression2 expression3*]] =
     *      expression3*.forEach(expression -> value[[expression]])
     *      < call > expression2.name
     *
     *      if (!expression2.definition.returnType instanceof Void)
     *          < pop > expression2.definition.returnType.suffix()
     */
    @Override
    public Void visit(FunctionInvocation functionInvocation, CGParam param) {
        codeGenerator.writeANewLine(functionInvocation.getLine());

        functionInvocation.arguments.forEach(expression -> expression.accept(codeGenerator.getValueCGVisitor(), param));
        codeGenerator.writeLineOfCode("call " + functionInvocation.variable.name);

        FunctionType function = (FunctionType) functionInvocation.variable.definition.getType();
        if (!(function.returnType instanceof Void))
            codeGenerator.writeLineOfCode("pop" + function.returnType.suffix());

        return null;
    }

    /**
     *    execute[[Return: statement -> expression]](int bytesReturn, int bytesLocals, int bytesParams) =
     *      value[[expression]]
     *      < ret > bytesReturn, bytesLocals, bytesParams
     */
    @Override
    public Void visit(Return returnStatement, CGParam param) {
        codeGenerator.writeANewLine(returnStatement.getLine());
        codeGenerator.writeLineOfCode("' * Return");

        returnStatement.expression.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(String.format(
                "ret %d, %d, %d",
                param.bytesReturn,
                param.bytesLocals,
                param.bytesParams
        ));

        return null;
    }

}
