package semantic;

import ast.expressions.Variable;
import ast.program.Definition;
import ast.program.FunctionDefinition;
import ast.program.VariableDefinition;
import ast.types.Array;
import ast.types.ErrorType;
import ast.types.RecordField;
import ast.types.Struct;
import symboltable.SymbolTable;
import visitor.AbstractVisitor;

import java.lang.Void;
import java.util.ArrayList;
import java.util.List;

public class IdentificationVisitor extends AbstractVisitor<Void, Void> {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public Void visit(Variable variable, Void param) {
        super.visit(variable, param);

        // We have to link it to its corresponding definition
        Definition definition = symbolTable.findInCurrentScope(variable.name);
        if (definition == null) definition = symbolTable.find(variable.name);
        if (definition == null) // in this case the definition has not been defined
            new ErrorType(
                    variable.getLine(),
                    variable.getColumn(),
                    variable.name + " has not been defined"
            );

        // LINK!
        variable.definition = definition;

        return null;
    }

    @Override
    public Void visit(VariableDefinition variableDefinition, Void param) {
        insertDefinition(variableDefinition);
        super.visit(variableDefinition, param);
        return null;
    }

    @Override
    public Void visit(FunctionDefinition functionDefinition, Void param) {
        // FIRST --> we must insert the FunctionDefinition
        insertDefinition(functionDefinition);
        // SECOND --> we must indicate we are in a deeper scope
        symbolTable.set();
        // THIRD --> we visit the body
        super.visit(functionDefinition, param);
        // FOURTH --> RESET!
        symbolTable.reset();

        return null;
    }

    @Override
    public Void visit(Struct struct, Void param) {
        List<String> names = new ArrayList<>();
        struct.recordFields.forEach(recordField -> {
            if (names.contains(recordField.name)) // we have to check if the record field has already been defined
                new ErrorType(
                        recordField.getLine(),
                        recordField.getColumn(),
                        String.format("%s has already been defined in the struct.", recordField.name)
                );
            else names.add(recordField.name);
        });
        return null;
    }

    private void insertDefinition(Definition definition) {
        String errorMessage = definition.getName() + " could not be defined as it has already been in this scope";
        if (!symbolTable.insert(definition))
            new ErrorType(
                    definition.getLine(),
                    definition.getColumn(),
                    errorMessage);
    }

}
