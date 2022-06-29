package codegeneration;

import ast.TypeCheckingFactory;
import ast.expressions.*;
import ast.types.Character;
import ast.types.Void;
import codegeneration.util.CGParam;
import codegeneration.util.CodeGenerator;

public class ValueCGVisitor extends AbstractCGVisitor<CGParam, Void> {

    public ValueCGVisitor(CodeGenerator cg) {
        super(cg);
    }

    /**
     *  value[[Arithmetic: expression1 -> expression2 (+|-|*|/) expression3]] =
     *      value[[expression2]]
     *      expression2.type.convertTo(expression1.type);
     *      value[[expression3]]
     *      expression3.type.convertTo(expression1.type);
     *
     *      switch(expression1.operator) {
     *          case "+":
     *              < add > expression1.type.suffix()
     *              break;
     *          case "-":
     *              < sub > expression1.type.suffix()
     *              break;
     *          case "*":
     *              < mul > expression1.type.suffix()
     *              break;
     *          case "/":
     *              < div > expression1.type.suffix()
     *              break;
     *      }
     */
    @Override
    public Void visit(Arithmetic arithmetic, CGParam param) {
        arithmetic.leftOperand.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(arithmetic.leftOperand.getType().convertTo(arithmetic.getType()));
        arithmetic.rightOperand.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(arithmetic.rightOperand.getType().convertTo(arithmetic.getType()));

        switch (arithmetic.operator) {
            case "+":
                codeGenerator.writeLineOfCode("add" + arithmetic.getType().suffix());
                break;
            case "-":
                codeGenerator.writeLineOfCode("sub" + arithmetic.getType().suffix());
                break;
            case "*":
                codeGenerator.writeLineOfCode("mul" + arithmetic.getType().suffix());
                break;
            case "/":
                codeGenerator.writeLineOfCode("div" + arithmetic.getType().suffix());
                break;
        }

        return null;
    }

    /**
     *  value[[Cast: expression1 -> type expression2]] =
     *      value[[expression2]]
     *      expression2.type.convertTo(type);
     */
    @Override
    public Void visit(Cast cast, CGParam param) {
        cast.expression.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(cast.expression.getType().convertTo(cast.getType()));

        return null;
    }

    /**
     *  value[[CharLiteral: expression -> CHAR_CONSTANT]] =
     *      < pushb > (int) CHAR_CONSTANT
     */
    @Override
    public Void visit(CharacterLiteral characterLiteral, CGParam param) {
        codeGenerator.writeLineOfCode("pushb " + (int) characterLiteral.value);
        return null;
    }

    /**
     *  value[[Comparison: expression1 -> expression2 ('>' | '>=' | '<' | '<=' | '!=' | '==') expression3]] =
     *      value[[expression2]]
     *      expression2.promoteToInt(); // to perform b2i in case we are comparing characters: int and double aren't "promoted"
     *      value[[expression3]]
     *      expression3.promoteToInt();
     *
     *      String suffix =
     *              comparison.leftOperand.getType() instanceof Character
     *                     ? ""
     *                     : comparison.leftOperand.getType().suffix();
     *
     *      switch(expression1.operator) {
     *          case ">":
     *              < gt > suffix
     *              break;
     *          case ">=":
     *              < ge > suffix
     *              break;
     *          case "<":
     *              < lt > suffix
     *              break;
     *          case "<=":
     *              < le > suffix
     *              break;
     *          case "!=":
     *              < ne > suffix
     *              break;
     *          case "==":
     *              < eq > suffix
     *              break;
     *      }
     */
    @Override
    public Void visit(Comparison comparison, CGParam param) {
        comparison.leftOperand.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(comparison.leftOperand.getType().promoteToInt());
        comparison.rightOperand.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(comparison.rightOperand.getType().promoteToInt());

        // In case we are working with chars, the suffix should be the one for integers. There's no gtb, gti is used instead
        String suffix =
                comparison.leftOperand.getType() instanceof Character
                        ? TypeCheckingFactory.getIntegerType().suffix()
                        : comparison.leftOperand.getType().suffix();

        switch (comparison.operator) {
            case ">":
                codeGenerator.writeLineOfCode("gt" + suffix);
                break;
            case ">=":
                codeGenerator.writeLineOfCode("ge" + suffix);
                break;
            case "<":
                codeGenerator.writeLineOfCode("lt" + suffix);
                break;
            case "<=":
                codeGenerator.writeLineOfCode("le" + suffix);
                break;
            case "!=":
                codeGenerator.writeLineOfCode("ne" + suffix);
                break;
            case "==":
                codeGenerator.writeLineOfCode("eq" + suffix);
                break;
        }

        return null;
    }

    /**
     *  value[[DoubleLiteral: expression -> REAL_CONSTANT]] =
     *      < pushf > REAL_CONSTANT
     */
    @Override
    public Void visit(DoubleLiteral doubleLiteral, CGParam param) {
        codeGenerator.writeLineOfCode("pushf " + doubleLiteral.value);
        return null;
    }

    /**
     *  value[[IntLiteral: expression -> INT_CONSTANT]] =
     *      < pushi > INT_CONSTANT
     */
    @Override
    public Void visit(IntLiteral intLiteral, CGParam param) {
        codeGenerator.writeLineOfCode("pushi " + intLiteral.value);
        return null;
    }

    /**
     *  value[[Logical: expression1 -> expression2 ('&&' | '||') expression3]] =
     *      value[[expression2]]
     *      value[[expression3]]
     *
     *      switch(expression1.operator) {
     *          case "&&":
     *              < and >
     *              break;
     *          case "||":
     *              < or >
     *              break;
     *      }
     */
    @Override
    public Void visit(Logical logical, CGParam param) {
        logical.leftOperand.accept(codeGenerator.getValueCGVisitor(), param);
        logical.rightOperand.accept(codeGenerator.getValueCGVisitor(), param);

        switch (logical.operator) {
            case "&&":
                codeGenerator.writeLineOfCode("and");
                break;
            case "||":
                codeGenerator.writeLineOfCode("or");
                break;
        }

        return null;
    }

    /**
     *  value[[Variable: expression1 -> ID]] =
     *      address[[expression1]]
     *      < load > expression1.type.suffix()
     */
    @Override
    public Void visit(Variable variable, CGParam param) {
        variable.accept(codeGenerator.getAddressCGVisitor(), param);
        codeGenerator.writeLineOfCode("load" + variable.getType().suffix());

        return null;
    }

    /**
     *  value[[Modulus: expression1 -> expression2 (%) expression3]] =
     *      value[[expression2]]
     *      expression2.type.convertTo(expression1.type);
     *      value[[expression3]]
     *      expression3.type.convertTo(expression1.type);
     *
     *      < modi >
     */
    @Override
    public Void visit(Modulus modulus, CGParam param) {
        modulus.leftOperand.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(modulus.leftOperand.getType().convertTo(modulus.getType()));
        modulus.rightOperand.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(modulus.leftOperand.getType().convertTo(modulus.getType()));

        codeGenerator.writeLineOfCode("mod" + modulus.getType().suffix());

        return null;
    }

    /**
     *  value[[UnaryNot: expression1 -> expression2]] =
     *      value[[expression2]]
     *      expression2.promoteToInt()
     *
     *      < not >
     */
    @Override
    public Void visit(UnaryNot unaryNot, CGParam param) {
        unaryNot.expression.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode(unaryNot.expression.getType().promoteToInt());
        codeGenerator.writeLineOfCode("not");

        return null;
    }

    /**
     *  value[[Indexing: expression1 -> expression2 expression3]] =
     *      address[[expression]]
     *      < load > expression1.type.suffix()
     */
    @Override
    public Void visit(Indexing indexing, CGParam param) {
        indexing.accept(codeGenerator.getAddressCGVisitor(), param);
        codeGenerator.writeLineOfCode("load" + indexing.getType().suffix());

        return null;
    }

    /**
     *  value[[RecordAccess: expression1 -> expression2 ID]] =
     *      address[[expression1]]
     *      < load > expression1.type.suffix()
     */
    @Override
    public Void visit(RecordAccess recordAccess, CGParam param) {
        recordAccess.accept(codeGenerator.getAddressCGVisitor(), param);
        codeGenerator.writeLineOfCode("load" + recordAccess.getType().suffix());

        return null;
    }

    /**
     *  value[[FunctionInvocation: expression1 -> expression2 expression3*]] =
     *      expression3*.forEach(expression -> value[[expression]])
     *      < call > expression2.name
     */
    @Override
    public Void visit(FunctionInvocation functionInvocation, CGParam param) {
        functionInvocation.arguments.forEach(argument -> argument.accept(codeGenerator.getValueCGVisitor(), param));
        codeGenerator.writeLineOfCode("call " + functionInvocation.variable.name);

        return null;
    }

}
