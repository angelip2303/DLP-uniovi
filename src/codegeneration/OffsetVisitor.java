package codegeneration;

import ast.program.FunctionDefinition;
import ast.program.VariableDefinition;
import ast.statements.Statement;
import ast.types.FunctionType;
import ast.types.RecordField;
import ast.types.Struct;
import visitor.AbstractVisitor;

public class OffsetVisitor extends AbstractVisitor<Void, Void> {

    int globalsByteSum = 0;

    /**
     * P:
     *      FunctionDefinition: definition -> type ID statement*
     * R:
     *      int localsByteSum = 0;
     *      for (Statement statement : statement*)
     *          if (statement instanceof VariableDefinition varDefinition) {
     *              localsByteSum += varDefinition.type.numberOfBytes(); // including ourselves
     *              varDefinition.offset = -localsByteSum;
     *          }
     */
    @Override
    public Void visit(FunctionDefinition functionDefinition, Void param) {
        super.visit(functionDefinition, param);

        // We compute the offset of the local variables defined in the function
        int localsByteSum = 0;
        for (Statement statement : functionDefinition.statements)
            if (statement instanceof VariableDefinition) {
                var variableDefinition = (VariableDefinition) statement;
                localsByteSum += variableDefinition.getType().numberOfBytes(); // including ourselves
                variableDefinition.offset = -localsByteSum;
            }

        return null;
    }

    /**
     * P:
     *      VariableDefinition: definition -> type ID
     * R:
     *      // being globalsByteSum a counter starting at 0
     *      if (definition.getScope() == 0) {
     *          definition.offset = globalsByteSum;
     *          globalsByteSum += type.numberOfBytes(); // excluding ourselves
     *      }
     */
    @Override
    public Void visit(VariableDefinition variableDefinition, Void param) {
        super.visit(variableDefinition, param);

        // We compute the offset of the VariableDefinition in case it's a global variable
        if (variableDefinition.getScope() == 0) {
            variableDefinition.offset = globalsByteSum;
            globalsByteSum += variableDefinition.getType().numberOfBytes(); // excluding ourselves
        }

        return null;
    }

    /**
     * P:
     *      FunctionType: type -> type ID definition*
     * R:
     *      int paramsBytesSum = 0;
     *      for (int i = definition*.size() - 1; i >= 0; i--) {
     *          VariableDefinition varDefinition = definition*.get(i);
     *          varDefinition.offset = 4 + paramsBytesSum; // notice the CI (Control Information)
     *          paramsBytesSum += varDefinition.type.numberOfBytes(); // excluding ourselves
     *      }
     */
    @Override
    public Void visit(FunctionType functionType, Void param) {
        super.visit(functionType, param);

        // We compute the offset of the parameters
        int paramsBytesSum = 0;
        for (int i = functionType.parameters.size() - 1; i >= 0; i--) {
          VariableDefinition variableDefinition = functionType.parameters.get(i);
            variableDefinition.offset = 4 + paramsBytesSum; // notice the CI (Control Information)
          paramsBytesSum += variableDefinition.getType().numberOfBytes(); // excluding ourselves
        }

        return null;
    }

    /**
     * P:
     *      Struct: type -> recordField*
     * R:
     *      int recordFieldsByteSum = 0;
     *      for (RecordField recordField : recordField*) {
     *          recordField.offset = recordFieldsByteSum;
     *          recordFieldsByteSum += recordField.type.numberOfBytes(); // excluding ourselves
     *      }
     */
    @Override
    public Void visit(Struct struct, Void param) {
        super.visit(struct, param);

        int recordFieldsByteSum = 0;
        for (RecordField recordField : struct.recordFields) {
            recordField.offset = recordFieldsByteSum;
            recordFieldsByteSum += recordField.type.numberOfBytes(); // excluding ourselves
        }

        return null;
    }

}
