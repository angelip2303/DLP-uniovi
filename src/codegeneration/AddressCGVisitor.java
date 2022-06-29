package codegeneration;

import ast.expressions.Indexing;
import ast.expressions.RecordAccess;
import ast.expressions.Variable;
import ast.program.VariableDefinition;
import ast.types.Struct;
import ast.types.Void;
import codegeneration.util.CGParam;
import codegeneration.util.CodeGenerator;

public class AddressCGVisitor extends AbstractCGVisitor<CGParam, Void> {

    public AddressCGVisitor(CodeGenerator cg) {
        super(cg);
    }

    /**
     *  address[[Variable: expression -> ID]] =
     *      if (expression.definition.scope == 0)
     *          < pusha > expression.definition.offset
     *      else {
     *          < push bp >
     *          < pushi > expression.definition.offset
     *          < addi >
     *      }
     */
    @Override
    public Void visit(Variable variable, CGParam param) {
        VariableDefinition variableDefinition = (VariableDefinition) variable.definition;
        if (variable.definition.getScope() == 0) codeGenerator.writeLineOfCode("pusha " + variableDefinition.offset);
        else {
            codeGenerator.writeLineOfCode("push bp");
            codeGenerator.writeLineOfCode("pushi " + variableDefinition.offset);
            codeGenerator.writeLineOfCode("addi");
        }

        return null;
    }

    /**
     *  address[[Indexing: expression1 -> expression2 expression3]] =
     *      address[[expression2]]
     *      value[[expression3]]
     *      < pushi > expression2.type.numberOfBytes()
     *      < muli >
     *      < addi >
     */
    @Override
    public Void visit(Indexing indexer, CGParam param) {
        indexer.expression.accept(codeGenerator.getAddressCGVisitor(), param);
        indexer.index.accept(codeGenerator.getValueCGVisitor(), param);
        codeGenerator.writeLineOfCode("pushi " + indexer.getType().numberOfBytes());
        codeGenerator.writeLineOfCode("muli ");
        codeGenerator.writeLineOfCode("addi");

        return null;
    }

    /**
     *  address[[FieldAccess: expression1 -> expression2 ID]] =
     *      address[[expression2]]
     *      < pushi > expression1.getField(ID).offset
     *      < addi >
     */
    @Override
    public Void visit(RecordAccess recordAccess, CGParam param) {
        Struct struct = (Struct) recordAccess.expression.getType();

        recordAccess.expression.accept(codeGenerator.getAddressCGVisitor(), param);
        codeGenerator.writeLineOfCode("pushi " + struct.getField(recordAccess.name).offset);
        codeGenerator.writeLineOfCode("addi");

        return null;
    }

}
