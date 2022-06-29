package ast.program;

import ast.statements.Statement;
import ast.types.FunctionType;
import visitor.Visitor;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionDefinition extends AbstractDefinition {

    public List<Statement> statements;

    public FunctionDefinition(int line, int column, String name, List<Statement> statements, FunctionType function) {
        super(line, column, name, function);
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "FunctionDefinition{" +
                "name='" + getName() + '\'' +
                ", function=" + getType().toString() +
                ", statements=" +
                    this.statements
                        .stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ", "{", "}"))
                + '}';
    }

    @Override
    public <TP, TR> TR accept(Visitor<TP, TR> visitor, TP param) {
        return visitor.visit(this, param);
    }

}
